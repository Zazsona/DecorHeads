package com.zazsona.decorheads.event.block;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headsources.drops.DropUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

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
            BlockPistonReactionEvent reactionEvent = new BlockPistonReactionEvent(movingBlock, movingBlock.getPistonMoveReaction(), e);
            Bukkit.getPluginManager().callEvent(reactionEvent);
        }
    }
}
