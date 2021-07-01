package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BlockDropHeadSource extends DropHeadSource
{
    public BlockDropHeadSource(IHead head, double dropRate)
    {
        super(head, HeadSourceType.MINE_DROP, dropRate);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onBlockBreak(BlockBreakEvent e)
    {
        if (passFilters(e))
        {
            Player blockBreaker = e.getPlayer();
            World world = e.getBlock().getWorld();
            Location location = e.getBlock().getLocation();
            return super.dropHead(world, location, blockBreaker, null);
        }
        return null;
    }
}
