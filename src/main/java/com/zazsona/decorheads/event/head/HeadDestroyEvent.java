package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the destruction of a head by any means
 */
public class HeadDestroyEvent extends HeadBlockEvent
{
    private Event causingEvent;

    public HeadDestroyEvent(@NotNull Block block, @NotNull IHead head, Event causingEvent)
    {
        super(block, head);
        this.causingEvent = causingEvent;
    }

    /**
     * Gets the event causing the destruction of the head.
     * @return the destructive event
     */
    public Event getCausingEvent()
    {
        return causingEvent;
    }

    // TODO: Maybe add SetDrops() or SetDropRate()?
    // This will need to work across explosion events, block break events, etc. (Former float, latter boolean)
    // This will be needed to ensure heads drop 100% of the time.

    // TODO: Then create a class to... Y'know, drop the heads based on this event.

    //                ItemStack dropStack = DecorHeadsUtil.getHeadDrop(movingBlock);
    //                if (dropStack != null)
    //                    DropUtil.dropItem(movingBlock.getLocation(), dropStack);
}
