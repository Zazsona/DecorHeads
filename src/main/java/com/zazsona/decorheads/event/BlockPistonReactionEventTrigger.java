package com.zazsona.decorheads.event;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import java.util.Iterator;

public class BlockPistonReactionEventTrigger implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e)
    {
        Iterator<Block> blocks = e.getBlocks().iterator();
        while (blocks.hasNext())
        {
            Block movingBlock = blocks.next();
            BlockPistonReactionEvent reactionEvent = new BlockPistonReactionEvent(movingBlock, movingBlock.getPistonMoveReaction());
            Bukkit.getPluginManager().callEvent(reactionEvent);

            if (reactionEvent.isCancelled())
                e.setCancelled(true);
        }
    }
}
