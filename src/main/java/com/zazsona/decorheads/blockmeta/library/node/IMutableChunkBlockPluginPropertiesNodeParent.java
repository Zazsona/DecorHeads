package com.zazsona.decorheads.blockmeta.library.node;

public interface IMutableChunkBlockPluginPropertiesNodeParent extends IChunkBlockPluginPropertiesNodeParent, IMutableBlockPluginPropertiesNode
{
    /**
     * Sets the {@link ChunkBlockPluginPropertiesNode}, including all its children, for the provided co-ordinates,
     * overwriting any previously set values.
     *
     * @param chunkX the chunk's X co-ordinate
     * @param chunkZ the chunk's Z co-ordinate
     * @param chunkNode the chunk node
     * @return the replaced chunk node, if any.
     */
    ChunkBlockPluginPropertiesNode putChunkNode(int chunkX, int chunkZ, ChunkBlockPluginPropertiesNode chunkNode);

    /**
     * Removes the {@link ChunkBlockPluginPropertiesNode}, including all its children, for the provided co-ordinates.
     *
     * @param chunkX the chunk's X co-ordinate
     * @param chunkZ the chunk's Z co-ordinate
     * @return the removed chunk node
     */
    ChunkBlockPluginPropertiesNode removeChunkNode(int chunkX, int chunkZ);
}
