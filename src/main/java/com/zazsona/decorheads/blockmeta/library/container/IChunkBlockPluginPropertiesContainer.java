package com.zazsona.decorheads.blockmeta.library.container;

import org.bukkit.Chunk;
import org.bukkit.Location;

public interface IChunkBlockPluginPropertiesContainer extends IBlockPluginPropertiesContainer
{
    /**
     * Gets the plugin block properties container for the chunk that the provided location falls in
     * @param location a location to get the chunk for
     * @return a container that holds block properties within the chunk, or null if no mapping exists.
     */
    IBlockPluginPropertiesContainer getChunk(Location location);

    /**
     * Gets the plugin block properties container for the chunk
     * @param chunk the chunk
     * @return a container that holds block properties within the chunk, or null if no mapping exists.
     */
    IBlockPluginPropertiesContainer getChunk(Chunk chunk);

    /**
     * Gets the plugin block properties container for the chunk that the provided location falls in, or if no chunk
     * is registered, the provided default value.
     * @param location a location to get the chunk for
     * @param defaultValue the default value to return if no mapping is found
     * @return the registered container that holds block properties within the chunk, or the provided default value if no
     * chunk is registered.
     */
    IBlockPluginPropertiesContainer getOrDefaultChunk(Location location, IBlockPluginPropertiesContainer defaultValue);

    /**
     * Gets the plugin block properties container for the chunk that the provided location falls in, or if no chunk
     * is registered, the provided default value.
     * @param chunk the chunk
     * @param defaultValue the default value to return if no mapping is found
     * @return the registered container that holds block properties within the chunk, or the provided default value if no
     * chunk is registered.
     */
    IBlockPluginPropertiesContainer getOrDefaultChunk(Chunk chunk, IBlockPluginPropertiesContainer defaultValue);

}
