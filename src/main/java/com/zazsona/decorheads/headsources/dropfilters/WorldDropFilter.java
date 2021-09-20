package com.zazsona.decorheads.headsources.dropfilters;

import com.zazsona.decorheads.headsources.HeadSourceType;
import org.bukkit.World;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class WorldDropFilter extends DropSourceFilter
{
    private Set<String> worldNames = new HashSet<>();

    public WorldDropFilter(Collection<String> worldNames)
    {
        if (worldNames != null)
            this.worldNames.addAll(worldNames);
    }

    public Set<String> getWorldNames()
    {
        return worldNames;
    }

    private boolean checkPass(String worldName)
    {
        return (worldNames.size() == 0 || worldNames.contains(worldName));
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, BlockBreakEvent e)
    {
        World world = e.getBlock().getWorld();
        return checkPass(world.getName());
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, EntityDeathEvent e)
    {
        World world = e.getEntity().getWorld();
        return checkPass(world.getName());
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, CraftItemEvent e)
    {
        World world = e.getWhoClicked().getWorld();
        return checkPass(world.getName());
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, BrewEvent e)
    {
        World world = e.getBlock().getWorld();
        return checkPass(world.getName());
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, BlockCookEvent e)
    {
        World world = e.getBlock().getWorld();
        return checkPass(world.getName());
    }
}
