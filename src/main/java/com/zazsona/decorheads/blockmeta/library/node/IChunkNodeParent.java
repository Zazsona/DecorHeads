package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Chunk;
import org.bukkit.Location;

public interface IChunkNodeParent extends INode
{
    /**
     * Gets the {@link ChunkNode} for the chunk at the provided location.
     * @param location a location contained within the {@link Chunk}
     * @return the ChunkNode
     */
    ChunkNode getChunkNode(Location location);
}
