package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.config.HeadRepository;
import com.zazsona.decorheads.headdata.Head;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Bukkit;
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

import java.io.IOException;
import java.util.Iterator;

public class HeadEventTrigger implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlaced(BlockPlaceEvent e)
    {
        // If this is already a HeadPlaceEvent, skip.
        if (e instanceof HeadPlaceEvent)
            return;

        // Dependency checks
        ItemStack placedItem = e.getItemInHand();
        if (placedItem.getType() != Material.PLAYER_HEAD && placedItem.getType() != Material.PLAYER_WALL_HEAD)
            return;

        PersistentDataContainer dataContainer = placedItem.getItemMeta().getPersistentDataContainer();
        if (!dataContainer.has(Head.getSkullHeadKeyKey(), PersistentDataType.STRING))
            return;

        String headKey = dataContainer.get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
        IHead head = HeadRepository.getLoadedHeads().get(headKey);

        HeadPlaceEvent headEvent = new HeadPlaceEvent(e.getBlockPlaced(), e.getBlockReplacedState(), e.getBlockAgainst(), e.getItemInHand(), e.getPlayer(), e.canBuild(), e.getHand(), head);
        Bukkit.getPluginManager().callEvent(headEvent);
        e.setBuild(headEvent.canBuild());
        e.setCancelled(headEvent.isCancelled());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        try
        {
            // If this is already a HeadBreakEvent, skip.
            if (e instanceof HeadBreakEvent)
                return;

            Block block = e.getBlock();
            IHead head = DecorHeadsUtil.getBlockHeadData(block);
            if (head != null)
            {
                HeadBreakEvent headEvent = new HeadBreakEvent(e.getBlock(), e.getPlayer(), head);
                Bukkit.getPluginManager().callEvent(headEvent);
                e.setDropItems(headEvent.isDropItems());
                e.setExpToDrop(headEvent.getExpToDrop());
                e.setCancelled(headEvent.isCancelled());
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        try
        {
            // If this is already a HeadExplodeEvent, skip.
            if (e instanceof HeadExplodeEvent)
                return;

            IHead explodedHead = DecorHeadsUtil.getBlockHeadData(e.getBlock());
            if (explodedHead != null)
            {
                HeadExplodeEvent headEvent = new HeadExplodeEvent(e.getBlock(), e.blockList(), e.getYield(), explodedHead);
                Bukkit.getPluginManager().callEvent(headEvent);
                e.setYield(headEvent.getYield());
                e.setCancelled(headEvent.isCancelled());
                if (e.isCancelled())
                    return;
            }

            Iterator blockIterator = e.blockList().iterator();
            while (blockIterator.hasNext())
            {
                Block block = (Block) blockIterator.next();
                IHead head = DecorHeadsUtil.getBlockHeadData(block);
                if (head != null)
                {
                    HeadBreakByExplosionEvent headEvent = new HeadBreakByExplosionEvent(block, e.getBlock(), e.blockList(), e.getYield(), head);
                    Bukkit.getPluginManager().callEvent(headEvent);
                    if (headEvent.isCancelled())
                        blockIterator.remove();
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e)
    {
        try
        {
            Iterator blockIterator = e.blockList().iterator();
            while (blockIterator.hasNext())
            {
                Block block = (Block) blockIterator.next();
                IHead head = DecorHeadsUtil.getBlockHeadData(block);
                if (head != null)
                {
                    HeadBreakByExplosionEvent headEvent = new HeadBreakByExplosionEvent(block, e.getEntity(), e.blockList(), e.getYield(), head);
                    Bukkit.getPluginManager().callEvent(headEvent);
                    if (headEvent.isCancelled())
                        blockIterator.remove();
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent e)
    {
        try
        {
            // If this is already a HeadBurnEvent, skip.
            if (e instanceof HeadBurnEvent)
                return;

            Block block = e.getBlock();
            IHead head = DecorHeadsUtil.getBlockHeadData(block);
            if (head != null)
            {
                HeadBurnEvent headEvent = new HeadBurnEvent(block, e.getIgnitingBlock(), head);
                Bukkit.getPluginManager().callEvent(headEvent);
                e.setCancelled(headEvent.isCancelled());
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }
}
