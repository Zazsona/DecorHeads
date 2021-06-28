package com.zazsona.decorheads.headsources.dropfilters;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class BlockDropFilter extends DropSourceFilter
{
    private Set<Material> blocks = new HashSet<Material>();

    public BlockDropFilter(Collection<Material> blocks)
    {
        if (this.blocks != null)
            this.blocks.addAll(blocks);
    }

    public Set<Material> getBlocks()
    {
        return blocks;
    }

    private boolean checkPass(Material block)
    {
        return (blocks.size() == 0 || blocks.contains(block));
    }

    @Override
    protected boolean passFilter(BlockBreakEvent e)
    {
        Material block = e.getBlock().getType();
        return checkPass(block);
    }

    @Override
    protected boolean passFilter(EntityDeathEvent e)
    {
        LivingEntity entity = e.getEntity();
        Location location = entity.getLocation();
        location.setY(location.getY() - 1.0f); //Entity location returns the Y position at the entity's feet. To get the block beneath, simply -1!
        Block block = entity.getWorld().getBlockAt(location);
        return checkPass(block.getType());
    }
}
