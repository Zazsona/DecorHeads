package com.zazsona.decorheads.headsources.dropfilters;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public abstract class DropSourceFilter
{
    public boolean passFilter(Event e)
    {
        if (e instanceof BlockBreakEvent)
            return passFilter((BlockBreakEvent) e);
        else if (e instanceof EntityDeathEvent)
            return passFilter((EntityDeathEvent) e);
        else if (e instanceof CraftItemEvent)
            return passFilter((CraftItemEvent) e);
        else if (e instanceof BrewEvent)
            return passFilter((BrewEvent) e);
        else if (e instanceof FurnaceSmeltEvent)
            return passFilter((FurnaceSmeltEvent) e);
        else
            return true;
    }

    protected boolean passFilter(BlockBreakEvent e)
    {
        return true;
    }

    protected boolean passFilter(EntityDeathEvent e)
    {
        return true;
    }

    protected boolean passFilter(CraftItemEvent e)
    {
        return true;
    }

    protected boolean passFilter(BrewEvent e)
    {
        return true;
    }

    protected boolean passFilter(FurnaceSmeltEvent e)
    {
        return true;
    }
}
