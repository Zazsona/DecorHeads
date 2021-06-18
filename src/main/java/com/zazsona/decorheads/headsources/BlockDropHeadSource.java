package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BlockDropHeadSource extends DropHeadSource
{
    private Set<Material> blocks = new HashSet<Material>();

    public BlockDropHeadSource(IHead head, Collection<Material> blocks)
    {
        super(head);
        if (blocks != null)
            this.blocks.addAll(blocks);
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onBlockBreak(BlockBreakEvent e)
    {
        if ((blocks.size() == 0 || blocks.contains(e.getBlock().getType())))
        {
            World world = e.getBlock().getWorld();
            Location location = e.getBlock().getLocation();
            return super.dropHead(world, location);
        }
        return null;
    }
}
