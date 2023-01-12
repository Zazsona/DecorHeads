package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.MaterialUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Iterator;

public class BlockInventoryOwnerListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        try
        {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null)
            {
                Player player = e.getPlayer();
                Block clickedBlock = e.getClickedBlock();
                if (clickedBlock.getState() instanceof Campfire)
                {
                    ItemStack usedItem = e.getItem();
                    if (usedItem != null && MaterialUtil.isCookableFood(usedItem.getType()))
                        BlockMetaRepository.getInstance()
                                .getRegionData(clickedBlock.getChunk())
                                .addBlockMeta(clickedBlock.getLocation(), BlockMetaKeys.INVENTORY_OWNER_ID_KEY, player.getUniqueId().toString());
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryUsed(InventoryClickEvent e)
    {
        try
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
                BlockMetaRepository.getInstance()
                        .getRegionData(block.getChunk())
                        .addBlockMeta(location, BlockMetaKeys.INVENTORY_OWNER_ID_KEY, player.getUniqueId().toString());
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        try
        {
            clearInventoryOwnerMeta(e.getBlock().getLocation());
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        try
        {
            Iterator blockIterator = e.blockList().iterator();
            while (blockIterator.hasNext())
            {
                Block block = (Block) blockIterator.next();
                clearInventoryOwnerMeta(block.getLocation());
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e)
    {
        try
        {
            Iterator blockIterator = e.blockList().iterator();
            while (blockIterator.hasNext())
            {
                Block block = (Block) blockIterator.next();
                clearInventoryOwnerMeta(block.getLocation());
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
                if ((movingBlock.getPistonMoveReaction() != PistonMoveReaction.BLOCK || movingBlock.getPistonMoveReaction() != PistonMoveReaction.IGNORE))
                {
                    clearInventoryOwnerMeta(movingBlock.getLocation());
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent e)
    {
        try
        {
            clearInventoryOwnerMeta(e.getBlock().getLocation());
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    /**
     * Removes all inventory owner metadata from this location
     * @param location the location to purge
     * @throws IOException unable to get metadata file
     */
    private void clearInventoryOwnerMeta(Location location) throws IOException
    {
        BlockMetaRepository repository = BlockMetaRepository.getInstance();
        BlockMetaRegionData regionData = repository.getRegionData(location.getChunk());
        regionData.removeBlockMeta(location, BlockMetaKeys.INVENTORY_OWNER_ID_KEY);
    }
}
