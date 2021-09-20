package com.zazsona.decorheads.headsources.dropfilters;

import com.zazsona.decorheads.headsources.HeadSourceType;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;

public abstract class DropSourceFilter
{
    public boolean passFilter(HeadSourceType sourceType, Event e)
    {
        if (e instanceof BlockBreakEvent)
            return passFilter(sourceType, (BlockBreakEvent) e);
        else if (e instanceof EntityDeathEvent)
            return passFilter(sourceType, (EntityDeathEvent) e);
        else if (e instanceof CraftItemEvent)
            return passFilter(sourceType, (CraftItemEvent) e);
        else if (e instanceof BrewEvent)
            return passFilter(sourceType, (BrewEvent) e);
        else if (e instanceof BlockCookEvent)
            return passFilter(sourceType, (BlockCookEvent) e);
        else
            return true;
    }

    protected boolean passFilter(HeadSourceType sourceType, BlockBreakEvent e)
    {
        return true;
    }

    protected boolean passFilter(HeadSourceType sourceType, EntityDeathEvent e)
    {
        return true;
    }

    protected boolean passFilter(HeadSourceType sourceType, CraftItemEvent e)
    {
        return true;
    }

    protected boolean passFilter(HeadSourceType sourceType, BrewEvent e)
    {
        return true;
    }

    protected boolean passFilter(HeadSourceType sourceType, BlockCookEvent e)
    {
        return true;
    }
}
