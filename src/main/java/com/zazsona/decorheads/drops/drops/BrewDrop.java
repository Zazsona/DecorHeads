package com.zazsona.decorheads.drops.drops;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.Permissions;
import com.zazsona.decorheads.blockmeta.InventoryBlockUtil;
import com.zazsona.decorheads.DropType;
import com.zazsona.decorheads.blockmeta.library.node.LoadedBlockPluginProperties;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.drops.filters.IDropFilter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BrewDrop extends Drop
{
    public BrewDrop(@NotNull String key, double dropRate, @NotNull ItemStack result)
    {
        super(key, dropRate, result);
    }

    public BrewDrop(@NotNull String key, double dropRate, @NotNull IHead result)
    {
        super(key, dropRate, result);
    }

    public BrewDrop(@NotNull String key, double dropRate, @NotNull ItemStack result, @NotNull List<IDropFilter> filters)
    {
        super(key, dropRate, result, filters);
    }

    public BrewDrop(@NotNull String key, double dropRate, @NotNull IHead result, @NotNull List<IDropFilter> filters)
    {
        super(key, dropRate, result, filters);
    }

    @Override
    public DropType getDropType()
    {
        return DropType.BREW;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBrewComplete(BrewEvent e)
    {
        try
        {
            PluginConfig config = DecorHeadsPlugin.getInstanceConfig();
            boolean enabledPass = (config.isPluginEnabled() && config.isDropsEnabled() && config.isDropTypeEnabled(getDropType()));
            boolean rollPass = DropUtil.rollDrop(getDropRate());

            if (enabledPass && rollPass && isFiltersPass(e))
            {
                Block block = e.getBlock();
                Location location = block.getLocation();

                LoadedBlockPluginProperties blockPluginProperties = LoadedBlockPluginProperties.getInstance(DecorHeadsPlugin.getInstance());
                OfflinePlayer offlinePlayer = InventoryBlockUtil.getInventoryOwner(blockPluginProperties, e.getBlock());
                Player player = (offlinePlayer.isOnline()) ? offlinePlayer.getPlayer() : null;

                if (player != null && player.hasPermission(Permissions.DROP_HEADS))
                {
                    ItemStack result = getResult(player.getUniqueId());
                    DropUtil.dropItems(location, result, 1);
                }
                else if (player == null && config.isEnvironmentalDropsEnabled())
                {
                    ItemStack result = getResult(null);
                    DropUtil.dropItems(location, result, 1);
                }
            }
        }
        catch (IllegalArgumentException ex)
        {
            Bukkit.getLogger().warning(String.format("[%s] %s drop event failed: %s", DecorHeadsPlugin.PLUGIN_NAME, getDropType().toString(), ex.getMessage()));
        }
    }
}
