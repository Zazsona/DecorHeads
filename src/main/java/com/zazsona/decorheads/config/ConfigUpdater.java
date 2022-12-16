package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigUpdater
{
    // TODO: Migrate 2.3.1 configs to new split configs (separate files or single? Hmmm!) (Also do the blockMetaLogger.json file?)
    private static final String configFileName = "config.yml";
    private static final String legacyConfigFileName = "legacy-config.yml";
    private static final String versionKey = "version";
    private static ConfigUpdater instance;

    private ConfigUpdater()
    {
        //Singleton required private constructor
    }

    public static ConfigUpdater getInstance()
    {
        if (instance == null)
            instance = new ConfigUpdater();
        return instance;
    }

    public void updateConfig()
    {
        Plugin plugin = Core.getSelfPlugin();
        File configFile = new File(plugin.getDataFolder().getPath()+"/"+configFileName);
        boolean upgradingFromLegacy = false;
        if (configFile.exists())
        {
            FileConfiguration config = plugin.getConfig();
            String configVersion = config.getString(versionKey, null);
            if (configVersion == null)
            {
                upgradingFromLegacy = true;
                archiveLegacyConfig(plugin, configFile);
            }
        }
        else
        {
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
        }

        FileConfiguration config = plugin.getConfig();
        updateTo210(config, upgradingFromLegacy);
        updateTo220(config, upgradingFromLegacy);
        updateTo240(config, upgradingFromLegacy);
        config.set(versionKey, plugin.getDescription().getVersion());
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    private void updateTo210(FileConfiguration config, boolean upgradingFromLegacy)
    {
        if (config.getString(versionKey).compareTo("2.1.0") < 0 || upgradingFromLegacy)
        {
            boolean updateNotificationsEnabled = config.getBoolean("update-notifications");
            UpdateNotificationLevel level = (updateNotificationsEnabled) ? UpdateNotificationLevel.MINOR : UpdateNotificationLevel.DISABLED;
            config.set("update-notifications", level.toString().toLowerCase());
        }
    }

    private void updateTo220(FileConfiguration config, boolean upgradingFromLegacy)
    {
        if (config.getString(versionKey).compareTo("2.2.0") < 0 || upgradingFromLegacy)
            config.set("head-meta-patcher", true); // Since we're updating from versions which may have had meta loss bugs, turn it on by default.
    }

    private void updateTo240(FileConfiguration config, boolean upgradingFromLegacy)
    {
        if (config.getString(versionKey).compareTo("2.4.0") >= 0 && !upgradingFromLegacy)
            return;

        boolean environmentalDrops = config.getBoolean("playerless-drop-events");
        config.set("environmental-drops", environmentalDrops);
        config.set("playerless-drop-events", null);

        boolean mineDrops = config.getBoolean("drop-sources.mine-drop");
        boolean brewDrops = config.getBoolean("drop-sources.brew-drop");
        boolean smeltDrops = config.getBoolean("drop-sources.smelt-drop");
        boolean craftDrops = config.getBoolean("drop-sources.craft-drop");
        boolean entityDeathDrops = config.getBoolean("drop-sources.entity-death-drop");
        boolean playerDeathDrops = config.getBoolean("drop-sources.player-death-drop");

        config.createSection("drop-types");
        config.set("drop-types.mine", mineDrops);
        config.set("drop-types.brew", brewDrops);
        config.set("drop-types.smelt", smeltDrops);
        config.set("drop-types.craft", craftDrops);
        config.set("drop-types.entity-death", entityDeathDrops);
        config.set("drop-types.player-death", playerDeathDrops);

        config.set("drop-sources", null);
    }

    private void archiveLegacyConfig(Plugin plugin, File configFile)
    {
        try
        {
            if (configFile.exists())
            {
                Bukkit.getLogger().info(String.format("[%s] Found legacy 1.x DecorHeads config. Archiving...", Core.PLUGIN_NAME));
                String archivePath = configFile.getParent()+"/"+legacyConfigFileName;
                moveConfig(configFile, archivePath);
                plugin.reloadConfig();
                plugin.saveDefaultConfig();
                plugin.reloadConfig();
                Bukkit.getLogger().info(String.format("[%s] Archive complete. Thanks for being a long-time supporter of the plugin!", Core.PLUGIN_NAME));
            }
        }
        catch (SecurityException e)
        {
            Bukkit.getLogger().severe(String.format("[%s] Unable to archive legacy config. You may experience unintended behaviour.", Core.PLUGIN_NAME));
        }
    }

    private void moveConfig(File configFile, String targetPath) throws SecurityException
    {
        if (configFile.exists())
        {
            File backupFile = new File(targetPath);
            configFile.renameTo(backupFile);
        }
    }
}
