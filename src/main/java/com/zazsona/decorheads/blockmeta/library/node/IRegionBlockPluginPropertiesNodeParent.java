package com.zazsona.decorheads.blockmeta.library.node;

public interface IRegionBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link IChunkBlockPluginPropertiesNodeParent} for the region at the provided co-ordinates.
     * @param regionX the region's X co-ordinate
     * @param regionZ the region's Z co-ordinate
     * @return the region node
     */
    IChunkBlockPluginPropertiesNodeParent getRegionNode(int regionX, int regionZ);
}
