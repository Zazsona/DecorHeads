package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IMutableRegionBlockPluginPropertiesNodeParent extends IRegionBlockPluginPropertiesNodeParent, IMutableBlockPluginPropertiesNode
{
    /**
     * Sets the {@link RegionBlockPluginPropertiesNode}, including all its children, for the provided {@link Location}, overwriting any
     * previously set values.
     *
     * @param blockLocation a block location in the region
     * @param regionNode the RegionNode to set
     */
    RegionBlockPluginPropertiesNode putRegionNode(Location blockLocation, RegionBlockPluginPropertiesNode regionNode);

    /**
     * Removes the {@link RegionBlockPluginPropertiesNode}, including all its children, for the provided {@link Location}.
     *
     * @param blockLocation a block location in the region
     */
    RegionBlockPluginPropertiesNode removeRegionNode(Location blockLocation);
}
