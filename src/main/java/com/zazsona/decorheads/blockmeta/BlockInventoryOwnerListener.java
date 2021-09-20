package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.MaterialUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Campfire;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
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
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null)
        {
            Player player = e.getPlayer();
            Block clickedBlock = e.getClickedBlock();
            if (clickedBlock.getState() instanceof Campfire)
            {
                ItemStack usedItem = e.getItem();
                if (usedItem != null && MaterialUtil.isCookableFood(usedItem.getType()))
                    BlockMetaLogger.getInstance().setMetadata(clickedBlock.getLocation(), OWNER_ID_KEY, player.getUniqueId().toString());
            }

        }
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        Iterator blockIterator = e.blockList().iterator();
        while (blockIterator.hasNext())
        {
            Block block = (Block) blockIterator.next();
            BlockMetaLogger.getInstance().removeMetadata(block.getLocation(), OWNER_ID_KEY);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e)
    {
        Iterator blockIterator = e.blockList().iterator();
        while (blockIterator.hasNext())
        {
            Block block = (Block) blockIterator.next();
            BlockMetaLogger.getInstance().removeMetadata(block.getLocation(), OWNER_ID_KEY);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e)
    {
        for (Block movingBlock : e.getBlocks())
        {
            if ((movingBlock.getPistonMoveReaction() != PistonMoveReaction.BLOCK || movingBlock.getPistonMoveReaction() != PistonMoveReaction.IGNORE) && BlockMetaLogger.getInstance().isMetadataSet(movingBlock.getLocation(), OWNER_ID_KEY))
            {
                BlockMetaLogger.getInstance().removeMetadata(movingBlock.getLocation(), OWNER_ID_KEY);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent e)
    {
        BlockMetaLogger.getInstance().removeMetadata(e.getBlock().getLocation(), OWNER_ID_KEY);
    }

    //@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    //public void onFromTo(BlockFromToEvent e)
    //{
    //             Unimplemented. No inventory-holding block is destroyed by liquids.
    //}
}
