package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import org.bukkit.plugin.Plugin;

public class PluginConfig
{
    public static final String ENABLED_KEY = "enabled";
    public static final String CRAFTING_KEY = "crafting";
    public static final String DROPS_KEY = "drops";
    public static final String BLOCK_DROPS_KEY = "block-drops";
    public static final String ENTITY_DROPS_KEY = "entity-drops";
    public static final String PLAYER_DROPS_KEY = "player-drops";
    public static final String PLAYERLESS_DROP_EVENTS_KEY = "playerless-drop-events";
    public static final String WIKI_RECIPE_LEARN_KEY = "learn-recipes-from-wiki";
    private static Plugin plugin = Core.getPlugin(Core.class);

    public static void save()
    {
        plugin.saveConfig();
    }

    public static void setEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(ENABLED_KEY, newEnabled);
        save();
    }

    public static boolean isEnabled()
    {
        return plugin.getConfig().getBoolean(ENABLED_KEY);
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

    public static void setBlockDropsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(BLOCK_DROPS_KEY, newEnabled);
        save();
    }

    public static boolean isBlockDropsEnabled()
    {
        return plugin.getConfig().getBoolean(BLOCK_DROPS_KEY);
    }

    public static void setEntityDropsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(ENTITY_DROPS_KEY, newEnabled);
        save();
    }

    public static boolean isEntityDropsEnabled()
    {
        return plugin.getConfig().getBoolean(ENTITY_DROPS_KEY);
    }

    public static void setPlayerDropsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(PLAYER_DROPS_KEY, newEnabled);
        save();
    }

    public static boolean isPlayerDropsEnabled()
    {
        return plugin.getConfig().getBoolean(PLAYER_DROPS_KEY);
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
}
