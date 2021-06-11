package com.zazsona.decorheads;

import com.zazsona.decorheads.headdata.BlockDropHead;
import com.zazsona.decorheads.headdata.Head;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Core extends JavaPlugin
{
    public static final String PLUGIN_NAME = "DecorHeads";
    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
        int pluginId = 10174;
        Metrics metrics = new Metrics(this, pluginId);
        //getServer().getPluginManager().registerEvents(new HeadDropListener(), this);
        //getServer().getPluginManager().registerEvents(new PlacedHeadRetriever(), this);
        //this.getCommand("DecorHeads").setExecutor(new DecorHeadsCommand());
        //CraftingManager.addRecipes();

        HeadLoader headLoader = new HeadLoader();
        headLoader.loadHeads();
    }

    @Override
    public void onDisable()
    {
    }
}
