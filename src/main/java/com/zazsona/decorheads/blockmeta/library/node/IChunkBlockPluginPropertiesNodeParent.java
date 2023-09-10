package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Chunk;
import org.bukkit.Location;

public interface IChunkBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link ChunkBlockPluginPropertiesNode} for the chunk at the provided blockLocation.
     * @param blockLocation a block location contained within the {@link Chunk}
     * @return the ChunkNode
     */
    IBlockPluginPropertiesNodeParent getChunkNode(Location blockLocation);
}
