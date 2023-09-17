package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.util.Vector;

import java.util.List;

public interface IRegionBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link RegionBlockPluginPropertiesNode} for the region at the provided co-ordinates.
     * @param regionX the region's X co-ordinate
     * @param regionZ the region's Z co-ordinate
     * @return the region node
     */
    RegionBlockPluginPropertiesNode getRegionNode(int regionX, int regionZ);

    /**
     * Returns a new list of contained region co-ordinate vectors
     * @return a list of contained region co-ordinates
     */
    List<Vector> getRegionVectors();
}
