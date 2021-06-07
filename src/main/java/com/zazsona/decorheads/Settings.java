package com.zazsona.decorheads;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class Settings
{
    private static Plugin plugin = Core.getPlugin(Core.class);

    public static void save()
    {
        plugin.saveConfig();
    }

    public static void setEnabled(boolean newEnabled)
    {
        plugin.getConfig().set("Enabled", newEnabled);
        save();
    }

    public static boolean isEnabled()
    {
        return (boolean) plugin.getConfig().get("Enabled");
    }

    public static void setDropsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set("DropsEnabled", newEnabled);
        save();
    }

    public static boolean isDropsEnabled()
    {
        return (boolean) plugin.getConfig().get("DropsEnabled");
    }

    public static void setCraftingEnabled(boolean newEnabled)
    {
        plugin.getConfig().set("CraftingEnabled", newEnabled);
        save();
    }

    public static boolean isCraftingEnabled()
    {
        return (boolean) plugin.getConfig().get("CraftingEnabled");
    }

    public static void setMobDropsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set("MobDropsEnabled", newEnabled);
        save();
    }

    public static boolean isMobDropsEnabled()
    {
        return (boolean) plugin.getConfig().get("MobDropsEnabled");
    }

    public static void setPlayerDropsEnabled(boolean newEnabled)
    {
        plugin.getConfig().set("PlayerDropsEnabled", newEnabled);
        save();
    }

    public static boolean isPlayerDropsEnabled()
    {
        return (boolean) plugin.getConfig().get("PlayerDropsEnabled");
    }

    public static void setPlayersHeadPvPOnly(boolean pvpOnly)
    {
        plugin.getConfig().set("PlayersHeadsPvPOnly", pvpOnly);
        save();
    }

    public static boolean isPlayerHeadsPvPOnly()
    {
        return (boolean) plugin.getConfig().get("PlayersHeadsPvPOnly");
    }

    public static void setDropChance(HeadManager.HeadType headType, double dropPercentage)
    {
        plugin.getConfig().set("DropPercentages."+headType.name(), dropPercentage);
        save();
    }

    public static double getDropChance(HeadManager.HeadType headType)
    {
        String value = String.valueOf(plugin.getConfig().get("DropPercentages."+headType.name()));
        return getDropChance(value);
    }

    public static double getDropChance(String key)
    {
        try
        {
            return Double.parseDouble(key);
        }
        catch (NumberFormatException e)
        {
            Bukkit.getLogger().warning("Attempted to get drop rate for "+key+", but none was found. If it's craftable only, ignore this message!");
            return 0;
        }
    }
}
