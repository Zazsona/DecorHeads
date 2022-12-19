package com.zazsona.decorheads.drops.drops;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.Permissions;
import com.zazsona.decorheads.blockmeta.BlockMetaChunkData;
import com.zazsona.decorheads.blockmeta.BlockMetaKeys;
import com.zazsona.decorheads.blockmeta.BlockMetaRepository;
import com.zazsona.decorheads.config.DropType;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.drops.filters.IDropFilter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                Location location = e.getBlock().getLocation();

                BlockMetaRepository repo = BlockMetaRepository.getInstance();
                BlockMetaChunkData chunk = repo.getChunk(e.getBlock().getChunk());
                Map<String, String> blockMeta = chunk.getBlockMeta(e.getBlock().getLocation());
                String playerId = blockMeta.get(BlockMetaKeys.INVENTORY_OWNER_ID_KEY);
                Player player = null;
                if (playerId != null)
                {
                    UUID playerUuid = UUID.fromString(playerId);
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUuid);
                    player = (offlinePlayer != null) ? offlinePlayer.getPlayer() : null;
                }

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
        catch (IllegalArgumentException | IOException ex)
        {
            Bukkit.getLogger().warning(String.format("[%s] %s drop event failed: %s", DecorHeadsPlugin.PLUGIN_NAME, getDropType().toString(), ex.getMessage()));
        }
    }
}