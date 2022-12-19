package com.zazsona.decorheads.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class PluginConfig extends YamlConfigWrapper
{
    public static final String VERSION_KEY = "version";
    public static final String PLUGIN_ENABLED_KEY = "plugin-enabled";
    public static final String CRAFTING_KEY = "crafting";
    public static final String DROPS_KEY = "drops";
    public static final String DROP_TYPES_KEY = "drop-types";
    public static final String ENVIRONMENTAL_DROPS_KEY = "environmental-drops";
    public static final String WIKI_RECIPE_LEARN_KEY = "learn-recipes-from-wiki";
    public static final String UPDATE_NOTIFICATIONS_KEY = "update-notifications";
    public static final String HEAD_META_PATCHER_KEY = "head-meta-patcher";

    public PluginConfig(FileConfiguration config, File saveLocation)
    {
        super(config, saveLocation);
    }

    public PluginConfig(File configFile)
    {
        super(configFile);
    }

    /**
     * Gets the version of the config
     * @return the version, following semantic versioning.
     */
    public String getVersion()
    {
        return config.getString(VERSION_KEY);
    }

    /**
     * Sets the version of the config
     * @param major major version
     * @param minor minor version
     * @param patch patch version
     */
    public void setVersion(int major, int minor, int patch)
    {
        String version = String.format("%d.%d.%d", major, minor, patch);
        config.set(VERSION_KEY, version);
    }

    /**
     * Sets the version of the config
     * @param version version string following semantic versioning (X.Y.Z)
     * @throws IllegalArgumentException - Provided version does not follow the X.Y.Z convention
     */
    public void setVersion(String version)
    {
        if (!Pattern.matches("[0-9]+[.][0-9]+[.][0-9]+", version))
            throw new IllegalArgumentException("Version must follow semantic versioning pattern. (X.Y.Z)");

        config.set(VERSION_KEY, version);
    }

    public void setPluginEnabled(boolean newEnabled)
    {
        config.set(PLUGIN_ENABLED_KEY, newEnabled);
    }

    public boolean isPluginEnabled()
    {
        return config.getBoolean(PLUGIN_ENABLED_KEY);
    }

    public void setCraftingEnabled(boolean newEnabled)
    {
        config.set(CRAFTING_KEY, newEnabled);
    }

    public boolean isCraftingEnabled()
    {
        return config.getBoolean(CRAFTING_KEY);
    }

    public void setDropsEnabled(boolean newEnabled)
    {
        config.set(DROPS_KEY, newEnabled);
    }

    public boolean isDropsEnabled()
    {
        return config.getBoolean(DROPS_KEY);
    }

    public void setEnvironmentalDropsEnabled(boolean newEnabled)
    {
        config.set(ENVIRONMENTAL_DROPS_KEY, newEnabled);
    }

    public boolean isEnvironmentalDropsEnabled()
    {
        return config.getBoolean(ENVIRONMENTAL_DROPS_KEY);
    }

    public void setLearnRecipesFromWikiEnabled(boolean newEnabled)
    {
        config.set(WIKI_RECIPE_LEARN_KEY, newEnabled);
    }

    public boolean isLearnRecipesFromWikiEnabled()
    {
        return config.getBoolean(WIKI_RECIPE_LEARN_KEY);
    }

    public void setUpdateNotificationsLevel(UpdateNotificationLevel level)
    {
        String levelLabel = level.toString().toLowerCase();
        config.set(UPDATE_NOTIFICATIONS_KEY, levelLabel);
    }

    public UpdateNotificationLevel getUpdateNotificationsLevel()
    {
        String levelLabel = config.getString(UPDATE_NOTIFICATIONS_KEY);
        String levelName = levelLabel.toUpperCase();
        UpdateNotificationLevel level = UpdateNotificationLevel.valueOf(levelName);
        return level;
    }

    public void setDropTypeEnabled(DropType dropType, boolean newEnabled)
    {
        String configKey = convertDropTypeToConfigKey(dropType);
        config.set(configKey, newEnabled);
    }

    public boolean isDropTypeEnabled(DropType dropType)
    {
        String configKey = convertDropTypeToConfigKey(dropType);
        return config.getBoolean(configKey);
    }

    public void setHeadMetaPatcherEnabled(boolean newEnabled)
    {
        config.set(HEAD_META_PATCHER_KEY, newEnabled);
    }

    public boolean isHeadMetaPatcherEnabled()
    {
        return config.getBoolean(HEAD_META_PATCHER_KEY);
    }

    public static String convertDropTypeToConfigKey(DropType dropType)
    {
        String sourceKey = dropType.name().toLowerCase().replace("_", "-");
        String configKey = String.format("%s.%s", DROP_TYPES_KEY, sourceKey);
        return configKey;
    }

    public static DropType convertConfigKeyToDropType(String configKey) throws IllegalArgumentException
    {
        String[] configTree = configKey.split("[.]");
        String dropTypeName = configTree[configTree.length-1];
        DropType dropType = DropType.matchDropType(dropTypeName);
        return dropType;
    }

}