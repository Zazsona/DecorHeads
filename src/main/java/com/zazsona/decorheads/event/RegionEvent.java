package com.zazsona.decorheads.event;

import org.bukkit.Chunk;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;
import org.jetbrains.annotations.NotNull;

public class RegionEvent extends WorldEvent implements Cancellable
{
    private static final String REGION_KEY_FORMAT = "%d.%d";
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled = false;
    private Chunk triggeringChunk;

    public RegionEvent(@NotNull Chunk triggeringChunk)
    {
        super(triggeringChunk.getWorld());
        this.triggeringChunk = triggeringChunk;
    }

    /**
     * Gets the chunk that triggered this region event
     * @return the chunk
     */
    public Chunk getTriggeringChunk()
    {
        return triggeringChunk;
    }

    /**
     * Gets the region key for the triggering chunk
     * @return the region key, in the format X.Z
     */
    public String getRegionKey()
    {
        return getRegionKey(getTriggeringChunk());
    }

    /**
     * Gets the region key for the provided chunk
     * @param chunk the chunk
     * @return the region key, in the format X.Z
     */
    public static String getRegionKey(Chunk chunk)
    {
        return getRegionKey(chunk.getX(), chunk.getZ());
    }

    /**
     * Gets the region key for the provided chunk coordinates
     * @param chunkX the chunk's X coordinate
     * @param chunkZ the chunk's Z coordinate
     * @return the region key, in the format X.Z
     */
    public static String getRegionKey(int chunkX, int chunkZ)
    {
        int regionXVal = (int) Math.floor(chunkX / 32.0);
        int regionZVal = (int) Math.floor(chunkZ / 32.0);
        return String.format(REGION_KEY_FORMAT, regionXVal, regionZVal);
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
