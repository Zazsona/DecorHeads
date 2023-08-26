package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IMutableChunkBlockPluginPropertiesNodeParent extends IChunkBlockPluginPropertiesNodeParent, IMutableBlockPluginPropertiesNode
{
    /**
     * Sets the {@link ChunkBlockPluginPropertiesNode}, including all its children, for the provided {@link Location}, overwriting any
     * previously set values.
     *
     * @param location a location in the Chunk
     * @param chunkNode the ChunkNode to set
     */
    ChunkBlockPluginPropertiesNode putChunkNode(Location location, ChunkBlockPluginPropertiesNode chunkNode);

    /**
     * Removes the {@link ChunkBlockPluginPropertiesNode}, including all its children, for the provided {@link Location}.
     *
     * @param location a location in the Chunk
     */
    ChunkBlockPluginPropertiesNode removeChunkNode(Location location);
}
