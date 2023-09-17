package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.util.Vector;

import java.util.List;

public interface IChunkBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link IBlockPluginPropertiesNodeParent} for the chunk at the provided co-ordinates.
     * @param chunkX the chunk's X co-ordinate
     * @param chunkZ the chunk's Z co-ordinate
     * @return the chunk node
     */
    IBlockPluginPropertiesNodeParent getChunkNode(int chunkX, int chunkZ);

    /**
     * Returns a new list of contained chunk co-ordinates
     * @return a list of contained chunk co-ordinates
     */
    List<Vector> getChunkVectors();
}
