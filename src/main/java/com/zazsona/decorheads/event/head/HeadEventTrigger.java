package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.config.HeadRepository;
import com.zazsona.decorheads.drops.drops.DropUtil;
import com.zazsona.decorheads.event.BlockBreakByExplosionEvent;
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
        // Dependency checks
        ItemStack placedItem = e.getItemInHand();
        if (placedItem.getType() != Material.PLAYER_HEAD && placedItem.getType() != Material.PLAYER_WALL_HEAD)
            return;

        PersistentDataContainer dataContainer = placedItem.getItemMeta().getPersistentDataContainer();
        if (!dataContainer.has(Head.getSkullHeadKeyKey(), PersistentDataType.STRING))
            return;

        String headKey = dataContainer.get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
        IHead head = HeadRepository.getLoadedHeads().get(headKey);

        HeadPlaceEvent event = new HeadPlaceEvent(e.getBlockPlaced(), head, e);
        Bukkit.getPluginManager().callEvent(event);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        Block block = e.getBlock();
        IHead head = DecorHeadsUtil.getBlockHeadData(block);
        if (head == null)
            return;

        HeadDestroyEvent event = new HeadDestroyEvent(block, head, e);
        Bukkit.getPluginManager().callEvent(event);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        Block block = e.getBlock();
        IHead head = DecorHeadsUtil.getBlockHeadData(block);
        if (head == null)
            return;

        HeadDestroyEvent event = new HeadDestroyEvent(block, head, e);
        Bukkit.getPluginManager().callEvent(event);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreakByExplosion(BlockBreakByExplosionEvent e)
    {
        Block block = e.getBlock();
        IHead head = DecorHeadsUtil.getBlockHeadData(block);
        if (head == null)
            return;

        HeadDestroyEvent event = new HeadDestroyEvent(block, head, e);
        Bukkit.getPluginManager().callEvent(event);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent e)
    {
        Block block = e.getBlock();
        IHead head = DecorHeadsUtil.getBlockHeadData(block);
        if (head == null)
            return;

        HeadDestroyEvent event = new HeadDestroyEvent(block, head, e);
        Bukkit.getPluginManager().callEvent(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e)
    {
        for (Block movingBlock : e.getBlocks())
        {
            IHead head = DecorHeadsUtil.getBlockHeadData(movingBlock);
            if (head == null)
                continue;
            HeadDestroyEvent event = new HeadDestroyEvent(movingBlock, head, e);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFromTo(BlockFromToEvent e)
    {
        Block block = e.getBlock();
        IHead head = DecorHeadsUtil.getBlockHeadData(block);
        if (head == null)
            return;
        HeadDestroyEvent event = new HeadDestroyEvent(block, head, e);
        Bukkit.getPluginManager().callEvent(event);
    }
}
