package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HeadPlaceEvent extends HeadBlockEvent
{
    private Event causingEvent;

    public HeadPlaceEvent(@NotNull Block block, @NotNull IHead head, Event causingEvent)
    {
        super(block, head);
        this.causingEvent = causingEvent;
    }

    /**
     * Gets the event causing the placement of the head.
     * @return the creative event
     */
    public Event getCausingEvent()
    {
        return causingEvent;
    }
}
