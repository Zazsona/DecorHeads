package com.zazsona.decorheads.blockmeta.library.node;

public interface IMutableRegionBlockPluginPropertiesNodeParent extends IRegionBlockPluginPropertiesNodeParent, IMutableBlockPluginPropertiesNode
{
    /**
     * Sets the {@link IChunkBlockPluginPropertiesNodeParent}, including all its children, for the provided co-ordinates,
     * overwriting any previously set values.
     *
     * @param regionX the region's X co-ordinate
     * @param regionZ the region's Z co-ordinate
     * @param regionNode the region node
     * @return the replaced region node, if any.
     */
    IChunkBlockPluginPropertiesNodeParent putRegionNode(int regionX, int regionZ, RegionBlockPluginPropertiesNode regionNode);

    /**
     * Removes the {@link IChunkBlockPluginPropertiesNodeParent}, including all its children, for the provided co-ordinates.
     *
     * @param regionX the region's X co-ordinate
     * @param regionZ the region's Z co-ordinate
     * @return the removed region node
     */
    IChunkBlockPluginPropertiesNodeParent removeRegionNode(int regionX, int regionZ);
}
