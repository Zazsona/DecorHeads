package com.zazsona.decorheads.event.block;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;

public class BlockBreakByExplosionEventTrigger implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        Iterator blockIterator = e.blockList().iterator();
        while (blockIterator.hasNext())
        {
            Block block = (Block) blockIterator.next();
            BlockBreakByExplosionEvent blockBrokenEvent = new BlockBreakByExplosionEvent(block, e.getBlock(), e.blockList(), e.getYield());
            Bukkit.getPluginManager().callEvent(blockBrokenEvent);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e)
    {
        Iterator blockIterator = e.blockList().iterator();
        while (blockIterator.hasNext())
        {
            Block block = (Block) blockIterator.next();
            BlockBreakByExplosionEvent blockBrokenEvent = new BlockBreakByExplosionEvent(block, e.getEntity(), e.blockList(), e.getYield());
            Bukkit.getPluginManager().callEvent(blockBrokenEvent);
        }
    }
}
