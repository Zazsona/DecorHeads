package com.zazsona.decorheads;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

public class BlockInventoryOwnerListener implements Listener
{
    private final String OWNER_ID_KEY = "DecorHeadsBlockInventoryOwnerId";
    private static BlockInventoryOwnerListener instance;

    private BlockInventoryOwnerListener()
    {

    }

    public static BlockInventoryOwnerListener getInstance()
    {
        if (instance == null)
            instance = new BlockInventoryOwnerListener();
        return instance;
    }

    public OfflinePlayer getOwningPlayer(Block block)
    {
        if (block.hasMetadata(OWNER_ID_KEY))
        {
            String uuidValue = block.getMetadata(OWNER_ID_KEY).get(0).asString();
            UUID uuid = UUID.fromString(uuidValue);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            return offlinePlayer;
        }
        return null;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryUsed(InventoryClickEvent e)
    {
        if (e == null || e.getInventory() == null)
            return;
        InventoryHolder inventoryHolder = e.getInventory().getHolder();
        if (inventoryHolder instanceof BlockState)
        {
            BlockState blockState = (BlockState) inventoryHolder;
            Block block = blockState.getBlock();
            Player player = (Player) e.getWhoClicked();
            block.setMetadata(OWNER_ID_KEY, new FixedMetadataValue(Core.getSelfPlugin(), player.getUniqueId()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        e.getBlock().removeMetadata(OWNER_ID_KEY, Core.getSelfPlugin());
    }
}
