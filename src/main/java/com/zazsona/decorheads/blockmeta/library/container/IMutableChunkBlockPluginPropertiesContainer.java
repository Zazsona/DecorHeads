package com.zazsona.decorheads.blockmeta.library.container;

import org.bukkit.Chunk;
import org.bukkit.Location;

public interface IMutableChunkBlockPluginPropertiesContainer extends IChunkBlockPluginPropertiesContainer, IMutableBlockPluginPropertiesContainer
{
    /**
     * Sets the plugin block properties container for the chunk that the provided location falls in
     *
     * @param location        a location to set the chunk for
     * @param chunkProperties the properties to map to the chunk
     */
    void setChunk(Location location, IMutableBlockPluginPropertiesContainer chunkProperties);

    /**
     * Sets the plugin block properties container for the chunk
     *
     * @param chunk           the chunk
     * @param chunkProperties the properties to map to the chunk
     */
    void setChunk(Chunk chunk, IMutableBlockPluginPropertiesContainer chunkProperties);

    /**
     * Removes the plugin block properties container for the chunk that the provided location falls in
     *
     * @param location a location to remove the chunk for
     * @return the removed properties container, or null if there were no properties mapped to the chunk
     */
    IBlockPluginPropertiesContainer removeChunk(Location location);

    /**
     * Removes the plugin block properties container for the chunk
     *
     * @param chunk the chunk
     * @return the removed properties container, or null if there were no properties mapped to the chunk
     */
    IBlockPluginPropertiesContainer removeChunk(Chunk chunk);
}
