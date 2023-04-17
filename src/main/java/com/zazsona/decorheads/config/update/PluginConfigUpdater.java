package com.zazsona.decorheads.config.update;

import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.UpdateNotificationLevel;
import com.zazsona.decorheads.config.VersionedYamlConfigWrapper;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.regex.Pattern;

public class PluginConfigUpdater implements IVersionedYamlConfigUpdater<PluginConfig>
{
    // TODO: Migrate blockMetaLogger.json file

    @Override
    public PluginConfig update(PluginConfig config) throws IOException
    {
        return update(config, PluginConfig.getMaxConfigVersion());
    }

    public PluginConfig update(PluginConfig config, String targetVersion)
    {
        // Validate Inputs
        if (!Pattern.matches(VersionedYamlConfigWrapper.VERSION_PATTERN, targetVersion))
            throw new IllegalArgumentException("Version must follow semantic versioning pattern. (X.Y.Z)");

        // Check if config is already on targetVersion or greater
        FileConfiguration configYaml = config.getConfigData();
        String configVersion = config.getVersion();
        if (configVersion.compareTo(targetVersion) >= 0)
            return config;

        updateTo210(configYaml);
        updateTo220(configYaml);
        updateTo300(configYaml);
        return config;
    }

    private void updateTo210(FileConfiguration config)
    {
        if (config.getString(PluginConfig.VERSION_KEY).compareTo("2.1.0") < 0)
        {
            boolean updateNotificationsEnabled = config.getBoolean("update-notifications");
            UpdateNotificationLevel level = (updateNotificationsEnabled) ? UpdateNotificationLevel.MINOR : UpdateNotificationLevel.DISABLED;
            config.set("update-notifications", level.toString().toLowerCase());
            config.set(PluginConfig.VERSION_KEY, "2.1.0");
        }
    }

    private void updateTo220(FileConfiguration config)
    {
        if (config.getString(PluginConfig.VERSION_KEY).compareTo("2.2.0") < 0)
        {
            config.set("head-meta-patcher", true); // Since we're updating from versions which may have had meta loss bugs, turn it on by default.
            config.set(PluginConfig.VERSION_KEY, "2.2.0");
        }
    }

    private void updateTo300(FileConfiguration config)
    {
        if (config.getString(PluginConfig.VERSION_KEY).compareTo("3.0.0") >= 0)
            return;

        boolean environmentalDrops = config.getBoolean("playerless-drop-events");
        config.set("environmental-drops", environmentalDrops);
        config.set("playerless-drop-events", null);
        config.set("learn-recipes-from-wiki", null);

        boolean mineDrops = config.getBoolean("drop-sources.mine-drop");
        boolean brewDrops = config.getBoolean("drop-sources.brew-drop");
        boolean smeltDrops = config.getBoolean("drop-sources.smelt-drop");
        boolean craftDrops = config.getBoolean("drop-sources.craft-drop");
        boolean entityDeathDrops = config.getBoolean("drop-sources.entity-death-drop");

        // TODO: Test "mine" becomes "block-break"
        config.createSection("drop-types");
        config.set("drop-types.block-break", mineDrops);
        config.set("drop-types.brew", brewDrops);
        config.set("drop-types.smelt", smeltDrops);
        config.set("drop-types.craft", craftDrops);
        config.set("drop-types.entity-death", entityDeathDrops);
        config.set("drop-types.player-death", null);

        config.set("drop-sources", null);
        config.set(PluginConfig.VERSION_KEY, "3.0.0");
    }
}
