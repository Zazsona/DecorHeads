package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class PluginConfig
{
    private static final String enabledKey = "enabled";
    private static final String dropsKey = "drops";
    private static final String craftingKey = "crafting";
    private static final String mobDropsKey = "mob-drops";
    private static final String playerDropsKey = "player-drops";
    private static Plugin plugin = Core.getPlugin(Core.class);

    public static void save()
    {
        plugin.saveConfig();
    }

    public static void setEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(enabledKey, newEnabled);
        save();
    }

    public static boolean isEnabled()
    {
        return plugin.getConfig().getBoolean(enabledKey);
    }

    public static void setDropsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(dropsKey, newEnabled);
        save();
    }

    public static boolean isDropsEnabled()
    {
        return plugin.getConfig().getBoolean(dropsKey);
    }

    public static void setCraftingEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(craftingKey, newEnabled);
        save();
    }

    public static boolean isCraftingEnabled()
    {
        return plugin.getConfig().getBoolean(craftingKey);
    }

    public static void setMobDropsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(mobDropsKey, newEnabled);
        save();
    }

    public static boolean isMobDropsEnabled()
    {
        return plugin.getConfig().getBoolean(mobDropsKey);
    }

    public static void setPlayerDropsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set(playerDropsKey, newEnabled);
        save();
    }

    public static boolean isPlayerDropsEnabled()
    {
        return plugin.getConfig().getBoolean(playerDropsKey);
    }
}
