package com.zazsona.decorheads.drops.filters;

import com.zazsona.decorheads.DropType;
import com.zazsona.decorheads.event.block.BlockBreakByExplosionEvent;
import com.zazsona.decorheads.event.block.BlockPistonReactionEvent;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;

public abstract class DropFilter implements IDropFilter
{
    /**
     * Checks to see if the filter is passed
     * @param dropType the type of drop that defines the action the player took
     * @param e the event associated with the drop type
     * @return boolean on filter passed
     */
    public boolean isFilterPass(DropType dropType, Event e)
    {
        if (e instanceof BlockBreakEvent)
            return isFilterPass(dropType, (BlockBreakEvent) e);
        else if (e instanceof EntityDeathEvent)
            return isFilterPass(dropType, (EntityDeathEvent) e);
        else if (e instanceof CraftItemEvent)
            return isFilterPass(dropType, (CraftItemEvent) e);
        else if (e instanceof BrewEvent)
            return isFilterPass(dropType, (BrewEvent) e);
        else if (e instanceof BlockCookEvent)
            return isFilterPass(dropType, (BlockCookEvent) e);
        else if (e instanceof BlockBreakByExplosionEvent)
            return isFilterPass(dropType, (BlockBreakByExplosionEvent) e);
        else if (e instanceof BlockPistonReactionEvent)
            return isFilterPass(dropType, (BlockPistonReactionEvent) e);
        else if (e instanceof BlockExplodeEvent)
            return isFilterPass(dropType, (BlockExplodeEvent) e);
        else if (e instanceof BlockPistonExtendEvent)
            return isFilterPass(dropType, (BlockPistonExtendEvent) e);
        else if (e instanceof BlockBurnEvent)
            return isFilterPass(dropType, (BlockBurnEvent) e);
        else if (e instanceof BlockFromToEvent)
            return isFilterPass(dropType, (BlockFromToEvent) e);
        else
            return true;
    }

    protected boolean isFilterPass(DropType dropType, BlockBreakEvent e)
    {
        return true;
    }

    protected boolean isFilterPass(DropType dropType, EntityDeathEvent e)
    {
        return true;
    }

    protected boolean isFilterPass(DropType dropType, CraftItemEvent e)
    {
        return true;
    }

    protected boolean isFilterPass(DropType dropType, BrewEvent e)
    {
        return true;
    }

    protected boolean isFilterPass(DropType dropType, BlockCookEvent e)
    {
        return true;
    }


    /*
            Alternative block break events
     */
    protected boolean isFilterPass(DropType dropType, BlockBreakByExplosionEvent e)
    {
        return true;
    }

    protected boolean isFilterPass(DropType dropType, BlockPistonReactionEvent e)
    {
        return true;
    }

    protected boolean isFilterPass(DropType dropType, BlockExplodeEvent e)
    {
        return true;
    }

    protected boolean isFilterPass(DropType dropType, BlockPistonExtendEvent e)
    {
        return true;
    }

    protected boolean isFilterPass(DropType dropType, BlockBurnEvent e)
    {
        return true;
    }

    protected boolean isFilterPass(DropType dropType, BlockFromToEvent e)
    {
        return true;
    }
}
