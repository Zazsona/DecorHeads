package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.event.head.*;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.drops.drops.DropUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.Iterator;

public class HeadBlockModificationListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadPlaced(HeadPlaceEvent e)
    {
        try
        {
            // Initialise variables
            Block block = e.getBlockPlaced();
            Location location = block.getLocation();
            ItemStack placedItem = e.getItemInHand();
            PersistentDataContainer dataContainer = placedItem.getItemMeta().getPersistentDataContainer();
            BlockMetaRepository metaRepository = BlockMetaRepository.getInstance();
            BlockMetaRegionData regionData = metaRepository.getRegionData(block.getChunk());

            // Write head id
            regionData.addBlockMeta(location, BlockMetaKeys.HEAD_ID_KEY, e.getHead().getKey());

            // Write player id & texture
            if (dataContainer.has(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING))
            {
                String uuid = dataContainer.get(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING);
                regionData.addBlockMeta(location, BlockMetaKeys.PLAYER_ID_KEY, uuid);
                if (dataContainer.has(PlayerHead.getSkullTextureKey(), PersistentDataType.STRING))
                {
                    String texture = dataContainer.get(PlayerHead.getSkullTextureKey(), PersistentDataType.STRING);
                    regionData.addBlockMeta(location, BlockMetaKeys.HEAD_TEXTURE_KEY, texture);
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadBreak(HeadBreakEvent e)
    {
        try
        {
            Block block = e.getBlock();
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
            {
                ItemStack drop = DecorHeadsUtil.getHeadDrop(block);
                DropUtil.dropItem(block.getLocation(), drop);
                e.setDropItems(false);
            }
            DecorHeadsUtil.clearBlockHeadMeta(block.getLocation());
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadBreakByExplosion(HeadBreakByExplosionEvent e)
    {
        try
        {
            Block block = e.getBlock();
            if (DropUtil.rollDrop(e.getExplosionYield()))
            {
                ItemStack drop = DecorHeadsUtil.getHeadDrop(block);
                DropUtil.dropItem(block.getLocation(), drop);
            }
            DecorHeadsUtil.clearBlockHeadMeta(block.getLocation());

            Iterator<Block> iterator = e.getBlocks().iterator();
            while (iterator.hasNext())
            {
                Block iteratedBlock = iterator.next();
                if (iteratedBlock == block)
                {
                    iterator.remove();
                    block.setType(Material.AIR);    // "Blow up" the block.
                    return;
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e)
    {
        try
        {
            for (Block movingBlock : e.getBlocks())
            {
                ItemStack dropStack = DecorHeadsUtil.getHeadDrop(movingBlock);
                if (dropStack != null)
                {
                    DropUtil.dropItem(movingBlock.getLocation(), dropStack);
                    DecorHeadsUtil.clearBlockHeadMeta(movingBlock.getLocation());
                    movingBlock.setType(Material.AIR);
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadBurn(HeadBurnEvent e)
    {
        try
        {
            Block block = e.getBlock();
            ItemStack dropStack = DecorHeadsUtil.getHeadDrop(block);
            if (dropStack != null)
            {
                DropUtil.dropItem(block.getLocation(), dropStack);
                DecorHeadsUtil.clearBlockHeadMeta(block.getLocation());
                e.setCancelled(true);
                block.setType(Material.AIR);
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFromTo(BlockFromToEvent e)
    {
        try
        {
            Block block = e.getBlock();
            ItemStack dropStack = DecorHeadsUtil.getHeadDrop(block);
            if (dropStack != null)
            {
                DropUtil.dropItem(block.getLocation(), dropStack);
                DecorHeadsUtil.clearBlockHeadMeta(block.getLocation());
                e.setCancelled(true);
                block.setType(Material.AIR);
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }
}
