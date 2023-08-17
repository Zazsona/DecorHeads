package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Chunk;
import org.bukkit.Location;

public interface IMutableChunkNodeParent extends IChunkNodeParent, IMutableNode
{
    /**
     * Sets the {@link ChunkNode}, including all its children, for the provided {@link Location}, overwriting any
     * previously set values.
     *
     * @param location a location in the Chunk
     * @param chunkNode the ChunkNode to set
     */
    ChunkNode putChunkNode(Location location, ChunkNode chunkNode);

    /**
     * Removes the {@link ChunkNode}, including all its children, for the provided {@link Location}.
     *
     * @param location a location in the Chunk
     */
    ChunkNode removeChunkNode(Location location);
}
