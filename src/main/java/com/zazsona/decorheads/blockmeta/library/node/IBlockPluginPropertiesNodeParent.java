package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.List;

public interface IBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link BlockPluginPropertiesNode} for the block at the provided X, Y, and Z co-ordinates.
     * @param blockX the block's X co-ordinate
     * @param blockY the block's Y co-ordinate
     * @param blockZ the block's Z co-ordinate
     * @return the block node
     */
    BlockPluginPropertiesNode getBlockNode(int blockX, int blockY, int blockZ);

    /**
     * Returns a new list of contained block co-ordinates
     * @return a list of contained block co-ordinates
     */
    List<Vector> getBlockVectors();
}
