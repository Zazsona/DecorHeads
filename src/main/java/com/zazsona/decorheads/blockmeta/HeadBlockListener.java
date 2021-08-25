package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.config.HeadLoader;
import com.zazsona.decorheads.headdata.Head;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import org.bukkit.Bukkit;
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
    public static String TEXTURE_KEY = "HeadTexture";

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        Block block = e.getBlock();
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
        {
            boolean headDropped = dropHeadFromBlock(block);
            if (headDropped)
                e.setDropItems(false);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        Iterator blockIterator = e.blockList().iterator();
        while (blockIterator.hasNext())
        {
            Block block = (Block) blockIterator.next();
            boolean headDropped = dropHeadFromBlock(block);
            if (headDropped)
            {
                blockIterator.remove();         // Effectively, manually "explode" the block, so we call the shots on the drops.
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
            boolean headDropped = dropHeadFromBlock(block);
            if (headDropped)
            {
                blockIterator.remove();         // Effectively, manually "explode" the block, so we call the shots on the drops.
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e)
    {
        for (Block movingBlock : e.getBlocks())
        {
            boolean headDropped = dropHeadFromBlock(movingBlock);
            if (headDropped)
            {
                movingBlock.setType(Material.AIR);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent e)
    {
        Block block = e.getBlock();
        boolean headDropped = dropHeadFromBlock(block);
        if (headDropped)
        {
            e.setCancelled(true);
            block.setType(Material.AIR);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFromTo(BlockFromToEvent e)
    {
        Block block = e.getToBlock();
        boolean headDropped = dropHeadFromBlock(block);
        if (headDropped)
        {
            e.setCancelled(true);
            block.setType(Material.AIR);
        }
    }

    private boolean dropHeadFromBlock(Block block)
    {
        if (isDecorHeadsHead(block))
        {
            BlockMetaLogger metaLogger = BlockMetaLogger.getInstance();
            Location location = block.getLocation();
            String headKey = metaLogger.getMetadata(location, ID_KEY);
            IHead head = HeadLoader.getInstance().getLoadedHeads().get(headKey);
            ItemStack item;
            if (head instanceof PlayerHead)
            {
                PlayerHead playerHead = (PlayerHead) head;
                String uuid = metaLogger.getMetadata(location, PLAYER_ID_KEY);
                if (metaLogger.isMetadataSet(location, TEXTURE_KEY))
                {
                    String texture = metaLogger.getMetadata(location, TEXTURE_KEY);
                    item = playerHead.createItem(UUID.fromString(uuid), texture);
                }
                else
                    item = playerHead.createItem(UUID.fromString(uuid));
            }
            else
                item = head.createItem();
            block.getWorld().dropItemNaturally(block.getLocation(), item);
            clearHeadMeta(location);
            return true;
        }
        else
        {
            Location location = block.getLocation();
            clearHeadMeta(location);
            return false;
        }
    }

    private boolean isDecorHeadsHead(Block block)
    {
        if (block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD)
        {
            BlockMetaLogger metaLogger = BlockMetaLogger.getInstance();
            Location location = block.getLocation();
            if (metaLogger.isMetadataSet(location, ID_KEY))
            {
                String headKey = metaLogger.getMetadata(location, ID_KEY);
                IHead head = HeadLoader.getInstance().getLoadedHeads().get(headKey);
                return head != null;
            }
        }
        return false;
    }

    private void clearHeadMeta(Location location)
    {
        BlockMetaLogger metaLogger = BlockMetaLogger.getInstance();
        metaLogger.removeMetadata(location, ID_KEY);
        metaLogger.removeMetadata(location, PLAYER_ID_KEY);
        metaLogger.removeMetadata(location, TEXTURE_KEY);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlaced(BlockPlaceEvent e)
    {
        BlockMetaLogger metaLogger = BlockMetaLogger.getInstance();
        ItemStack placedItem = e.getItemInHand();
        if (placedItem != null && (placedItem.getType() == Material.PLAYER_HEAD || placedItem.getType() == Material.PLAYER_WALL_HEAD))
        {
            PersistentDataContainer dataContainer = placedItem.getItemMeta().getPersistentDataContainer();
            if (dataContainer.has(Head.getSkullHeadKeyKey(), PersistentDataType.STRING))
            {
                Block block = e.getBlock();
                Location location = block.getLocation();
                String headKey = dataContainer.get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
                metaLogger.setMetadata(location, ID_KEY, headKey);
                if (dataContainer.has(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING))
                {
                    String uuid = dataContainer.get(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING);
                    metaLogger.setMetadata(location, PLAYER_ID_KEY, uuid);
                    if (dataContainer.has(PlayerHead.getSkullTextureKey(), PersistentDataType.STRING))
                    {
                        String texture = dataContainer.get(PlayerHead.getSkullTextureKey(), PersistentDataType.STRING);
                        metaLogger.setMetadata(location, TEXTURE_KEY, texture);
                    }
                }
            }
        }
    }
}
