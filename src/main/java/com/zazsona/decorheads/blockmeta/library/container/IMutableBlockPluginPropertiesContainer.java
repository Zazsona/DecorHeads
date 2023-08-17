package com.zazsona.decorheads.blockmeta.library.container;

import org.bukkit.Location;

public interface IMutableBlockPluginPropertiesContainer extends IBlockPluginPropertiesContainer
{
    /**
     * Sets the plugin property identified by the key for the block at the given {@link Location}
     *
     * @param location the location of the block
     * @param key      the property key
     * @param value    the property value
     */
    void addBlockPluginProperty(Location location, String key, String value);

    /**
     * Removes the plugin property identified by the key for the block at the given {@link Location}
     *
     * @param location the location of the block
     * @param key      the property key
     */
    void removeBlockPluginProperty(Location location, String key);
}
