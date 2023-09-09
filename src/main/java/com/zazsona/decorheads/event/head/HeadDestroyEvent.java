package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the destruction of a head by any means
 */
public class HeadDestroyEvent extends HeadBlockEvent implements Cancellable
{
    private boolean cancelled;

    public HeadDestroyEvent(@NotNull Block block, @NotNull IHead head)
    {
        super(block, head);
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
    }
}
