package com.zazsona.decorheads;

import com.zazsona.decorheads.config.HeadLoader;
import com.zazsona.decorheads.config.HeadUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Core extends JavaPlugin
{
    public static final String PLUGIN_NAME = "DecorHeads";
    @Override
    public void onEnable()
    {
        try
        {
            getConfig().options().copyDefaults(true);
            saveConfig();
            int pluginId = 10174;
            Metrics metrics = new Metrics(this, pluginId);
            //getServer().getPluginManager().registerEvents(new HeadDropListener(), this);
            //getServer().getPluginManager().registerEvents(new PlacedHeadRetriever(), this);
            //this.getCommand("DecorHeads").setExecutor(new DecorHeadsCommand());
            //CraftingManager.addRecipes();

            HeadUpdater headUpdater = HeadUpdater.getInstance();
            headUpdater.updateHeadsFile();
            HeadLoader headLoader = HeadLoader.getInstance();
            headLoader.loadHeads();
        }
        catch (Exception e)
        {
            Bukkit.getLogger().warning(String.format("[%s] %s", Core.PLUGIN_NAME, e.getMessage()));
            e.printStackTrace();
        }

    }

    public static Plugin getSelfPlugin()
    {
        return Core.getPlugin(Core.class);
    }
}
