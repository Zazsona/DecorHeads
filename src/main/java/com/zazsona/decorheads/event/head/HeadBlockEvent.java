package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HeadBlockEvent extends HeadEvent
{
    private Block block;

    public HeadBlockEvent(@NotNull Block block, @NotNull IHead head)
    {
        super(head);
        this.block = block;
    }

    /**
     * Gets the block involved in this event
     * @return the block
     */
    public Block getBlock()
    {
        return block;
    }
}
