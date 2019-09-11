package com.Zazsona;

import org.bukkit.plugin.Plugin;

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

    public static void setDropChance(HeadManager.HeadType headType, double dropPercentage)
    {
        plugin.getConfig().set("DropPercentages."+headType.name(), dropPercentage);
        save();
    }

    public static double getDropChance(HeadManager.HeadType headType)
    {
        String value = String.valueOf(plugin.getConfig().get("DropPercentages."+headType.name()));
        return Double.parseDouble(value);
    }
}
