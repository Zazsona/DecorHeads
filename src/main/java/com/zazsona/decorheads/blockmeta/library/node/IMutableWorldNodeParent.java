package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;
import org.bukkit.World;

public interface IMutableWorldNodeParent extends IWorldNodeParent, IMutableNode
{
    /**
     * Sets the {@link WorldNode}, including all its children, for the provided {@link Location}, overwriting any
     * previously set values.
     *
     * @param location a location in the world
     * @param worldNode the WorldNode to set
     */
    WorldNode putWorldNode(Location location, WorldNode worldNode);

    /**
     * Removes the {@link WorldNode}, including all its children, for the provided {@link Location}.
     *
     * @param location a location in the world
     */
    WorldNode removeWorldNode(Location location);
}
