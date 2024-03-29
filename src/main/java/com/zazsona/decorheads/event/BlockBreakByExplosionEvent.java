package com.zazsona.decorheads.event;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.metadata.Metadatable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockBreakByExplosionEvent extends BlockEvent implements Cancellable, IBlockBreakByExplosionEvent
{
    private static final HandlerList HANDLERS = new HandlerList();

    private Metadatable explosionSource;
    private List<Block> blocks;
    private float yield;
    private boolean isCancelled = false;

    public BlockBreakByExplosionEvent(@NotNull Block theBlock, Metadatable explosionSource, List<Block> blocks, float yield)
    {
        super(theBlock);
        this.explosionSource = explosionSource;
        this.blocks = blocks;
        this.yield = yield;
    }

    /**
     * Gets the object that exploded, resulting in this block being broken
     * @return
     */
    @Override
    public Metadatable getExplosionSource()
    {
        return explosionSource;
    }

    /**
     * Gets all blocks destroyed by the same explosion
     * @return blocks
     */
    public List<Block> getBlocks()
    {
        return blocks;
    }

    /**
     * Gets the yield rate of the explosion
     * @return the yield
     */
    public float getExplosionYield()
    {
        return yield;
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
