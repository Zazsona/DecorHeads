package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.headsources.HeadSourceType;
import org.bukkit.plugin.Plugin;

public class PluginConfig
{
    public static final String PLUGIN_ENABLED_KEY = "plugin-enabled";
    public static final String CRAFTING_KEY = "crafting";
    public static final String DROPS_KEY = "drops";
    public static final String DROP_SOURCES_KEY = "drop-sources";
    public static final String PLAYERLESS_DROP_EVENTS_KEY = "playerless-drop-events";
    public static final String WIKI_RECIPE_LEARN_KEY = "learn-recipes-from-wiki";
    public static final String UPDATE_NOTIFICATIONS_KEY = "update-notifications";

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

    public static void setPlayerlessDropEventsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(PLAYERLESS_DROP_EVENTS_KEY, newEnabled);
        save();
    }

    public static boolean isPlayerlessDropEventsEnabled()
    {
        return plugin.getConfig().getBoolean(PLAYERLESS_DROP_EVENTS_KEY);
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

    public static void setUpdateNotificationsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(UPDATE_NOTIFICATIONS_KEY, newEnabled);
        save();
    }

    public static boolean isUpdateNotificationsEnabled()
    {
        return plugin.getConfig().getBoolean(UPDATE_NOTIFICATIONS_KEY);
    }

    public static void setDropSourceEnabled(HeadSourceType sourceType, boolean newEnabled)
    {
        String configKey = convertSourceTypeToConfigKey(sourceType);
        plugin.getConfig().set(configKey, newEnabled);
        save();
    }

    public static boolean isDropSourceEnabled(HeadSourceType sourceType)
    {
        String configKey = convertSourceTypeToConfigKey(sourceType);
        return plugin.getConfig().getBoolean(configKey);
    }

    public static String convertSourceTypeToConfigKey(HeadSourceType sourceType)
    {
        if (sourceType != HeadSourceType.SHAPELESS_CRAFT && sourceType != HeadSourceType.SHAPED_CRAFT)
        {
            String sourceKey = sourceType.name().toLowerCase().replace("_", "-");
            String configKey = String.format("%s.%s", DROP_SOURCES_KEY, sourceKey);
            return configKey;
        }
        else
            throw new IllegalArgumentException(String.format("%s is not a valid drop head source.", sourceType.name()));

    }

    public static HeadSourceType convertConfigKeyToSourceType(String configKey) throws IllegalArgumentException
    {
        String[] configTree = configKey.split("[.]");
        String headTypeName = configTree[configTree.length-1];
        String enumFormatHeadTypeName = headTypeName.toUpperCase().replace("-", "_");
        HeadSourceType headSourceType = HeadSourceType.valueOf(enumFormatHeadTypeName);
        return headSourceType;
    }

}
