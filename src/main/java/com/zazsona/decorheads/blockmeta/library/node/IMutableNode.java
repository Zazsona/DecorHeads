package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IMutableNode extends INode
{
    /**
     * Sets the plugin property identified by the key for the block at the given {@link Location}
     *
     * @param location the location of the block
     * @param key      the property key
     * @param value    the property value
     */
    String putBlockProperty(Location location, String key, String value);

    /**
     * Removes the plugin property identified by the key for the block at the given {@link Location}
     *
     * @param location the location of the block
     * @param key      the property key
     */
    String removeBlockProperty(Location location, String key);
}
