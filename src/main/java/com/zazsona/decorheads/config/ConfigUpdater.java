package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigUpdater
{
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
        if (configFile.exists())
        {
            FileConfiguration config = plugin.getConfig();
            String configVersion = config.getString(versionKey, null);
            if (configVersion == null)
                archiveLegacyConfig(plugin, configFile);
        }
        else
        {
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
        }

        plugin.getConfig().set(versionKey, plugin.getDescription().getVersion());
        plugin.getConfig().options().copyDefaults(true);        // Anything 2.0.0+ should be handled just fine by this.
        plugin.saveConfig();
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
