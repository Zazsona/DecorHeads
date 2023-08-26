package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;

public class HeadEvent extends Event
{
    private static final HandlerList HANDLERS = new HandlerList();

    private IHead head;

    public HeadEvent(@NotNull IHead head)
    {
        super();
        this.head = head;
    }

    /**
     * Gets the definition of the head
     * @return the head
     */
    public IHead getHead()
    {
        return head;
    }

    @NotNull
    @Override
    public HandlerList getHandlers()
    {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
