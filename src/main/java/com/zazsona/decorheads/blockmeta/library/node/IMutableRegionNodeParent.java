package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IMutableRegionNodeParent extends IRegionNodeParent, IMutableNode
{
    /**
     * Sets the {@link RegionNode}, including all its children, for the provided {@link Location}, overwriting any
     * previously set values.
     *
     * @param location a location in the region
     * @param regionNode the RegionNode to set
     */
    RegionNode putRegionNode(Location location, RegionNode regionNode);

    /**
     * Removes the {@link RegionNode}, including all its children, for the provided {@link Location}.
     *
     * @param location a location in the region
     */
    RegionNode removeRegionNode(Location location);
}
