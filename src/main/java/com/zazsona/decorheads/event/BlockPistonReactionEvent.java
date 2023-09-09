package com.zazsona.decorheads.event;

import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.metadata.Metadatable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockPistonReactionEvent extends BlockEvent implements Cancellable
{
    private static final HandlerList HANDLERS = new HandlerList();

    private PistonMoveReaction reaction;
    private boolean isCancelled = false;

    public BlockPistonReactionEvent(@NotNull Block theBlock, PistonMoveReaction reaction)
    {
        super(theBlock);
        this.reaction = reaction;
    }

    /**
     * Gets reaction
     * @return reaction
     */
    public PistonMoveReaction getReaction()
    {
        return reaction;
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
