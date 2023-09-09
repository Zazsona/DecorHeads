package com.zazsona.decorheads.event;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BlockInventoryOwnerUpdateEvent extends BlockEvent implements Cancellable
{
    private static final HandlerList HANDLERS = new HandlerList();

    private UUID newOwnerId;
    private boolean isCancelled = false;

    public BlockInventoryOwnerUpdateEvent(@NotNull Block theBlock, UUID newOwnerId)
    {
        super(theBlock);
        this.newOwnerId = newOwnerId;
    }

    /**
     * Gets the ID of the player that now owns this block's inventory
     * @return the new owner's player ID, or null if there is no longer an owner
     */
    public UUID getNewOwnerPlayerId()
    {
        return newOwnerId;
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

    @Override
    public boolean isCancelled()
    {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel)
    {
        this.isCancelled = cancel;
    }
}
