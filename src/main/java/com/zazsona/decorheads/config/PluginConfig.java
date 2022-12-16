package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import org.bukkit.plugin.Plugin;

public class PluginConfig
{
    public static final String PLUGIN_ENABLED_KEY = "plugin-enabled";
    public static final String CRAFTING_KEY = "crafting";
    public static final String DROPS_KEY = "drops";
    public static final String DROP_TYPES_KEY = "drop-types";
    public static final String ENVIRONMENTAL_DROPS_KEY = "environmental-drops";
    public static final String WIKI_RECIPE_LEARN_KEY = "learn-recipes-from-wiki";
    public static final String UPDATE_NOTIFICATIONS_KEY = "update-notifications";
    public static final String HEAD_META_PATCHER_KEY = "head-meta-patcher";

    private static Plugin plugin = Core.getPlugin(Core.class);

    public static void save()
    {
        plugin.saveConfig();
    }

    public static void setPluginEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(PLUGIN_ENABLED_KEY, newEnabled);
        save();
    }

    public static boolean isPluginEnabled()
    {
        return plugin.getConfig().getBoolean(PLUGIN_ENABLED_KEY);
    }

    public static void setCraftingEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(CRAFTING_KEY, newEnabled);
        save();
    }

    public static boolean isCraftingEnabled()
    {
        return plugin.getConfig().getBoolean(CRAFTING_KEY);
    }

    public static void setDropsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(DROPS_KEY, newEnabled);
        save();
    }

    public static boolean isDropsEnabled()
    {
        return plugin.getConfig().getBoolean(DROPS_KEY);
    }

    public static void setEnvironmentalDropsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(ENVIRONMENTAL_DROPS_KEY, newEnabled);
        save();
    }

    public static boolean isEnvironmentalDropsEnabled()
    {
        return plugin.getConfig().getBoolean(ENVIRONMENTAL_DROPS_KEY);
    }

    public static void setLearnRecipesFromWikiEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(WIKI_RECIPE_LEARN_KEY, newEnabled);
        save();
    }

    public static boolean isLearnRecipesFromWikiEnabled()
    {
        return plugin.getConfig().getBoolean(WIKI_RECIPE_LEARN_KEY);
    }

    public static void setUpdateNotificationsLevel(UpdateNotificationLevel level)
    {
        String levelLabel = level.toString().toLowerCase();
        plugin.getConfig().set(UPDATE_NOTIFICATIONS_KEY, levelLabel);
        save();
    }

    public static UpdateNotificationLevel getUpdateNotificationsLevel()
    {
        String levelLabel = plugin.getConfig().getString(UPDATE_NOTIFICATIONS_KEY);
        String levelName = levelLabel.toUpperCase();
        UpdateNotificationLevel level = UpdateNotificationLevel.valueOf(levelName);
        return level;
    }

    public static void setDropTypeEnabled(DropType dropType, boolean newEnabled)
    {
        String configKey = convertDropTypeToConfigKey(dropType);
        plugin.getConfig().set(configKey, newEnabled);
        save();
    }

    public static boolean isDropTypeEnabled(DropType dropType)
    {
        String configKey = convertDropTypeToConfigKey(dropType);
        return plugin.getConfig().getBoolean(configKey);
    }

    public static void setHeadMetaPatcherEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(HEAD_META_PATCHER_KEY, newEnabled);
        save();
    }

    public static boolean isHeadMetaPatcherEnabled()
    {
        return plugin.getConfig().getBoolean(HEAD_META_PATCHER_KEY);
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
