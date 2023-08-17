package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Chunk;
import org.bukkit.Location;

public interface IRegionNodeParent extends INode
{
    /**
     * Gets the {@link RegionNode} for the region at the provided location.
     * @param location a location contained within the region
     * @return the RegionNode
     */
    RegionNode getRegionNode(Location location);
}
