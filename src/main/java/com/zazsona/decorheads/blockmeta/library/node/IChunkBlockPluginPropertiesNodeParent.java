package com.zazsona.decorheads.blockmeta.library.node;

public interface IChunkBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link IBlockPluginPropertiesNodeParent} for the chunk at the provided co-ordinates.
     * @param chunkX the chunk's X co-ordinate
     * @param chunkZ the chunk's Z co-ordinate
     * @return the chunk node
     */
    IBlockPluginPropertiesNodeParent getChunkNode(int chunkX, int chunkZ);
}
