package com.zazsona.decorheads.blockmeta.library.container;

import org.bukkit.Location;
import org.bukkit.World;

public interface IWorldBlockPluginPropertiesContainer extends IBlockPluginPropertiesContainer
{

    /**
     * Gets the plugin block properties container for the world that the provided location falls in
     * @param location a location to get the world for
     * @return a container that holds block properties within the world, or null if no mapping exists.
     */
    IRegionBlockPluginPropertiesContainer getWorld(Location location);

    /**
     * Gets the plugin block properties container for the world corresponding to the provided id (See: {@link World#getUID()}
     * @param worldId the ID of the world
     * @return a container that holds block properties within the world, or null if no mapping exists.
     */
    IRegionBlockPluginPropertiesContainer getWorld(String worldId);

    /**
     * Gets the plugin block properties container for the world
     * @param world the world
     * @return a container that holds block properties within the world, or null if no mapping exists.
     */
    IRegionBlockPluginPropertiesContainer getWorld(World world);

    /**
     * Gets the plugin block properties container for the world that the provided location falls in, or if no world
     * is registered, the provided default value.
     * @param location a location to get the world for
     * @param defaultValue the default value to return if no mapping is found
     * @return a container that holds block properties within the world, or the provided default value if no
     * world is registered.
     */
    IRegionBlockPluginPropertiesContainer getOrDefaultWorld(Location location, IRegionBlockPluginPropertiesContainer defaultValue);

    /**
     * Gets the plugin block properties container for the world corresponding to the provided id (See: {@link World#getUID()}, or if no world
     * is registered, the provided default value.
     * @param worldId the ID of the world
     * @param defaultValue the default value to return if no mapping is found
     * @return a container that holds block properties within the world, or the provided default value if no
     * world is registered.
     */
    IRegionBlockPluginPropertiesContainer getOrDefaultWorld(String worldId, IRegionBlockPluginPropertiesContainer defaultValue);

    /**
     * Gets the plugin block properties container for the world, or if no world
     * is registered, the provided default value.
     * @param world the world
     * @param defaultValue the default value to return if no mapping is found
     * @return a container that holds block properties within the world, or the provided default value if no
     * world is registered.
     */
    IRegionBlockPluginPropertiesContainer getOrDefaultWorld(World world, IRegionBlockPluginPropertiesContainer defaultValue);

}
