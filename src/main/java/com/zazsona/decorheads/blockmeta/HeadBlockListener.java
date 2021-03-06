package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.config.HeadLoader;
import com.zazsona.decorheads.headdata.Head;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Iterator;
import java.util.UUID;

/**
 * Class to ensure heads are retained after placing.
 */
public class HeadBlockListener implements Listener
{
    public static String ID_KEY = "HeadId";
    public static String PLAYER_ID_KEY = "HeadPlayerId";

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE && e.getBlock().getType() == Material.PLAYER_HEAD)
        {
            e.setDropItems(false);
            dropHeadFromBlock(e.getBlock());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        Iterator blockIterator = e.blockList().iterator();
        while (blockIterator.hasNext())
        {
            Block block = (Block) blockIterator.next();
            if (BlockMetaLogger.getInstance().isMetadataSet(block.getLocation(), ID_KEY))
            {
                blockIterator.remove();
                dropHeadFromBlock(block);
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e)
    {
        Iterator blockIterator = e.blockList().iterator();
        while (blockIterator.hasNext())
        {
            Block block = (Block) blockIterator.next();
            if (BlockMetaLogger.getInstance().isMetadataSet(block.getLocation(), ID_KEY))
            {
                blockIterator.remove();
                dropHeadFromBlock(block);
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e)
    {
        for (Block movingBlock : e.getBlocks())
        {
            if (movingBlock.getType() == Material.PLAYER_HEAD && BlockMetaLogger.getInstance().isMetadataSet(movingBlock.getLocation(), ID_KEY))
            {
                dropHeadFromBlock(movingBlock);
                movingBlock.setType(Material.AIR);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent e)
    {
        if (e.getBlock().getType() == Material.PLAYER_HEAD)
        {
            Block block = e.getBlock();
            if (BlockMetaLogger.getInstance().isMetadataSet(block.getLocation(), ID_KEY))
            {
                e.setCancelled(true);
                dropHeadFromBlock(block);
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFromTo(BlockFromToEvent e)
    {
        Block block = e.getToBlock();
        if (block.getType() == Material.PLAYER_HEAD)
        {
            if (BlockMetaLogger.getInstance().isMetadataSet(block.getLocation(), ID_KEY))
            {
                e.setCancelled(true);
                dropHeadFromBlock(block);
                block.setType(Material.AIR);
            }
        }
    }

    private boolean dropHeadFromBlock(Block block)
    {
        boolean headDropped = false;
        BlockMetaLogger metaLogger = BlockMetaLogger.getInstance();
        Location location = block.getLocation();
        if (metaLogger.isMetadataSet(location, ID_KEY))
        {
            String headKey = metaLogger.getMetadata(location, ID_KEY);
            IHead head = HeadLoader.getInstance().getLoadedHeads().get(headKey);
            if (head != null)
            {
                ItemStack item;
                if (head instanceof PlayerHead)
                {
                    PlayerHead playerHead = (PlayerHead) head;
                    String uuid = metaLogger.getMetadata(location, PLAYER_ID_KEY);
                    item = playerHead.createItem(UUID.fromString(uuid));
                }
                else
                    item = head.createItem();
                block.getWorld().dropItemNaturally(block.getLocation(), item);
                headDropped = true;
            }
            metaLogger.removeMetadata(location, ID_KEY);
            metaLogger.removeMetadata(location, PLAYER_ID_KEY);
        }
        return headDropped;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlaced(BlockPlaceEvent e)
    {
        BlockMetaLogger metaLogger = BlockMetaLogger.getInstance();
        ItemStack placedItem = e.getItemInHand();
        if (placedItem != null && placedItem.getType() == Material.PLAYER_HEAD)
        {
            PersistentDataContainer dataContainer = placedItem.getItemMeta().getPersistentDataContainer();
            if (dataContainer.has(Head.getSkullHeadKeyKey(), PersistentDataType.STRING))
            {
                String headKey = dataContainer.get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
                Block block = e.getBlock();
                Location location = block.getLocation();
                metaLogger.setMetadata(location, ID_KEY, headKey);
                if (dataContainer.has(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING))
                {
                    String uuid = dataContainer.get(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING);
                    metaLogger.setMetadata(location, PLAYER_ID_KEY, uuid);
                }
            }
        }
    }
}
