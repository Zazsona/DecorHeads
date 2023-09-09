package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HeadPlaceEvent extends HeadBlockEvent implements Cancellable
{
    private boolean cancelled;
    private ItemStack placedItem;

    public HeadPlaceEvent(@NotNull Block block, @NotNull IHead head, ItemStack placedItem)
    {
        super(block, head);
        this.placedItem = placedItem;
    }

    /**
     * Gets the placed head in {@link ItemStack} form
     * @return the head's ItemStack
     */
    public ItemStack getPlacedItem()
    {
        return placedItem;
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
