package com.zazsona.decorheads.blockmeta.library.container;

import org.bukkit.Location;
import org.bukkit.World;

public interface IMutableWorldBlockPluginPropertiesContainer extends IWorldBlockPluginPropertiesContainer, IMutableBlockPluginPropertiesContainer
{
    /**
     * Sets the plugin block properties container for the world that the provided location falls in
     *
     * @param location        a location to set the world for
     * @param worldProperties the properties to map to the world
     */
    void setWorld(Location location, IMutableRegionBlockPluginPropertiesContainer worldProperties);

    /**
     * Sets the plugin block properties container for the world corresponding to the provided id (See: {@link World#getUID()}
     *
     * @param worldId         the ID of the world
     * @param worldProperties the properties to map to the world
     */
    void setWorld(String worldId, IMutableRegionBlockPluginPropertiesContainer worldProperties);

    /**
     * Sets the plugin block properties container for the world
     *
     * @param world           the world
     * @param worldProperties the properties to map to the world
     */
    void setWorld(World world, IMutableRegionBlockPluginPropertiesContainer worldProperties);

    /**
     * Removes the plugin block properties container for the world that the provided location falls in
     *
     * @param location a location to remove the world for
     * @return the removed properties container, or null if there were no properties mapped to the world
     */
    IRegionBlockPluginPropertiesContainer removeWorld(Location location);

    /**
     * Removes the plugin block properties container for the world corresponding to the provided id (See: {@link World#getUID()}
     *
     * @param worldId the ID of the world
     * @return the removed properties container, or null if there were no properties mapped to the world
     */
    IRegionBlockPluginPropertiesContainer removeWorld(String worldId);

    /**
     * Removes the plugin block properties container for the world
     *
     * @param world the world
     * @return the removed properties container, or null if there were no properties mapped to the world
     */
    IRegionBlockPluginPropertiesContainer removeWorld(World world);
}
