package com.zazsona.decorheads;

import com.zazsona.decorheads.config.HeadLoader;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.dependencies.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

import static com.zazsona.decorheads.dependencies.Metrics.*;

public class MetricsManager
{
    private static final int pluginId = 10174;
    private static final String dropCraftPermissionsChart = "drop_craft_restricted";
    private static final String headsLoadedChart = "heads_loaded";
    private static final String dropsChartId = "drops_enabled";
    private static final String craftingChartId = "crafting_enabled";

    private static MetricsManager instance;
    private boolean enabled = false;

    private MetricsManager()
    {
        //Required empty private constructor
    }

    public static MetricsManager getInstance()
    {
        if (instance == null)
            instance = new MetricsManager();
        return instance;
    }

    public void enable()
    {
        if (enabled)
            return;

        Metrics metrics = new Metrics(Core.getSelfPlugin(), pluginId);
        addDropsEnabledChart(metrics);
        addCraftingEnabledChart(metrics);
        addDropCraftPermissionsActiveChart(metrics);
        addHeadsLoadedChart(metrics);
        enabled = true;
    }

    private void addDropsEnabledChart(Metrics metrics)
    {
        metrics.addCustomChart(new SimplePie(dropsChartId, () ->
        {
            boolean dropsEnabled = (PluginConfig.isPluginEnabled() && PluginConfig.isDropsEnabled());
            return String.valueOf(dropsEnabled);
        }));
    }

    private void addCraftingEnabledChart(Metrics metrics)
    {
        metrics.addCustomChart(new SimplePie(craftingChartId, () ->
        {
            boolean craftingEnabled = (PluginConfig.isPluginEnabled() && PluginConfig.isCraftingEnabled());
            return String.valueOf(craftingEnabled);
        }));
    }

    private void addDropCraftPermissionsActiveChart(Metrics metrics)
    {
        metrics.addCustomChart(new SimplePie(dropCraftPermissionsChart, () ->
        {
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            for (Player player : onlinePlayers)
            {
                if (!player.hasPermission(Permissions.DROP_HEADS) && !player.hasPermission(Permissions.CRAFT_HEADS))
                {
                    return String.valueOf(true);
                }
            }
            return String.valueOf(false);
        }));
    }

    private void addHeadsLoadedChart(Metrics metrics)
    {
        metrics.addCustomChart(new SimplePie(headsLoadedChart, () ->
        {
            return String.valueOf(HeadLoader.getInstance().getLoadedHeads().size());
        }));
    }
}
