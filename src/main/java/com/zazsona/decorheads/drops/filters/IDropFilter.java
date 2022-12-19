package com.zazsona.decorheads.drops.filters;

import com.zazsona.decorheads.DropType;
import org.bukkit.event.Event;

public interface IDropFilter
{
    /**
     * Checks if the provided event passes the filter, in the context o the {@link DropType}
     * @param dropType the {@link DropType} being filtered
     * @param e the event to check
     * @return true is the filter passes, otherwise false.
     */
    boolean isFilterPass(DropType dropType, Event e);
}
