package com.zazsona.decorheads.blockmeta;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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

import java.util.UUID;

public class BlockInventoryOwnerListener implements Listener
{
    private final String OWNER_ID_KEY = "BlockInventoryOwnerId";
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
        BlockMetaLogger metaLogger = BlockMetaLogger.getInstance();
        Location location = block.getLocation();
        if (metaLogger.isMetadataSet(location, OWNER_ID_KEY))
        {
            String uuidValue = metaLogger.getMetadata(location, OWNER_ID_KEY);
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
            Location location = block.getLocation();
            Player player = (Player) e.getWhoClicked();
            BlockMetaLogger.getInstance().setMetadata(location, OWNER_ID_KEY, player.getUniqueId().toString());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        BlockMetaLogger.getInstance().removeMetadata(e.getBlock().getLocation(), OWNER_ID_KEY);
    }
}
