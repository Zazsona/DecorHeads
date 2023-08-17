package com.zazsona.decorheads.blockmeta.library.io;

import org.bukkit.Location;

public interface IBlockPluginPropertiesContainerFileModel
{
    /**
     * Get the root directory for where all files & folders will be stored.
     * @return the path to the root.
     */
    String getRootDirectory();

    /**
     * Gets the directory for the provided world
     * @param worldName the world
     * @return the world directory path
     */
    String getWorldDirectory(String worldName);

    /**
     * Gets the directory for the world specified in the Location
     * @param location the location, with world data
     * @return the world directory path
     */
    String getWorldDirectory(Location location);

    /**
     * Gets the directory for the region at the specified location
     * @param worldName the world
     * @param regionX the X coordinate of the region
     * @param regionZ the Z coordinate of the region
     * @return the region directory path
     */
    String getRegionDirectory(String worldName, int regionX, int regionZ);

    /**
     * Gets the directory for the region at the specified location
     * @param location the location, with world data
     * @return the region directory path
     */
    String getRegionDirectory(Location location);

    /**
     * Gets the directory for the chunk at the specified location
     * @param worldName the world
     * @param chunkX the X coordinate of the chunk
     * @param chunkZ the Z coordinate of the chunk
     * @return the chunk directory path
     */
    String getChunkDirectory(String worldName, int chunkX, int chunkZ);

    /**
     * Gets the directory for the chunk at the specified location
     * @param location the location, with world data
     * @return the chunk directory path
     */
    String getChunkDirectory(Location location);

    /**
     * Gets the path of the file for storing server-level data
     * @return the server file path
     */
    String getServerFile();

    /**
     * Gets the path of the file for storing world-level data
     * @param worldName the world
     * @return the world file path
     */
    String getWorldFile(String worldName);

    /**
     * Gets the path of the file for storing world-level data
     * @param location the location, with world data
     * @return the world file path
     */
    String getWorldFile(Location location);

    /**
     * Gets the path of the file for storing region-level data
     * @param worldName the world
     * @param regionX the X coordinate of the region
     * @param regionZ the Z coordinate of the region
     * @return the region file path
     */
    String getRegionFile(String worldName, int regionX, int regionZ);

    /**
     * Gets the path of the file for storing region-level data
     * @param location the location, with world data
     * @return the region file path
     */
    String getRegionFile(Location location);

    /**
     * Gets the path of the file for storing chunk-level data
     * @param worldName the world
     * @param chunkX the X coordinate of the chunk
     * @param chunkZ the Z coordinate of the chunk
     * @return the chunk file path
     */
    String getChunkFile(String worldName, int chunkX, int chunkZ);

    /**
     * Gets the path of the file for storing chunk-level data
     * @param location the location, with world data
     * @return the chunk file path
     */
    String getChunkFile(Location location);
}
