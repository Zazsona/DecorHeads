package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;
import org.bukkit.World;

public interface IWorldNodeParent extends INode
{
    /**
     * Gets the {@link WorldNode} for the world at the provided location.
     * @param location a location with world data
     * @return the WorldNode
     */
    WorldNode getWorldNode(Location location);
}
