package com.zazsona.decorheads;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin
{
    public static String PLUGIN_LORE = ChatColor.BLUE+"DecorHeads";

    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
        int pluginId = 10174;
        Metrics metrics = new Metrics(this, pluginId);
        getServer().getPluginManager().registerEvents(new HeadDropListener(), this);
        getServer().getPluginManager().registerEvents(new PlacedHeadRetriever(), this);
        this.getCommand("DecorHeads").setExecutor(new DecorHeadsCommand());
        CraftingManager.addRecipes();
    }

    @Override
    public void onDisable()
    {
    }
}
