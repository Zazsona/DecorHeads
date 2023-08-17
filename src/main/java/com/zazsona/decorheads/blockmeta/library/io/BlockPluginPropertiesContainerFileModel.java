package com.zazsona.decorheads.blockmeta.library.io;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.blockmeta.library.container.IBlockPluginPropertiesContainer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.nio.file.Paths;

/**
 * Class for storing and overriding a multitude of filepaths used for storing various container levels of {@link IBlockPluginPropertiesContainer}
 */
public class BlockPluginPropertiesContainerFileModel implements IBlockPluginPropertiesContainerFileModel
{
    private String fileExtension;

    public BlockPluginPropertiesContainerFileModel(String fileExtension)
    {
        String extensionWithDot = fileExtension;
        if (!extensionWithDot.startsWith("."))
            extensionWithDot = "." + fileExtension;

        this.fileExtension = extensionWithDot;
    }

    @Override
    public String getRootDirectory()
    {
        return Bukkit.getWorldContainer().getAbsolutePath();
    }

    @Override
    public String getWorldDirectory(String worldName)
    {
        return Paths.get(getRootDirectory(), worldName).toString();
    }

    @Override
    public String getWorldDirectory(Location location)
    {
        if (location.getWorld() == null)
            throw new NullPointerException("World cannot be null.");

        return getWorldDirectory(location.getWorld().getName());
    }

    @Override
    public String getRegionDirectory(String worldName, int regionX, int regionZ)
    {
        String regionNameFormat = "r.%d.%d";
        String regionName = String.format(regionNameFormat, regionX, regionZ);
        return Paths.get(getWorldDirectory(worldName), regionName).toString();
    }

    @Override
    public String getRegionDirectory(Location location)
    {
        if (location.getWorld() == null)
            throw new NullPointerException("World cannot be null.");

        Chunk chunk = location.getChunk();
        int regionX = (int) Math.floor(chunk.getX() / 32.0);
        int regionZ = (int) Math.floor(chunk.getZ() / 32.0);
        return getRegionDirectory(location.getWorld().getName(), regionX, regionZ);
    }

    @Override
    public String getChunkDirectory(String worldName, int chunkX, int chunkZ)
    {
        String regionNameFormat = "c.%d.%d";
        String regionName = String.format(regionNameFormat, chunkX, chunkZ);
        return Paths.get(getWorldDirectory(worldName), regionName).toString();
    }

    @Override
    public String getChunkDirectory(Location location)
    {
        if (location.getWorld() == null)
            throw new NullPointerException("World cannot be null.");

        Chunk chunk = location.getChunk();
        return getChunkDirectory(location.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

    @Override
    public String getServerFile()
    {
        return Paths.get(getRootDirectory(), (DecorHeadsPlugin.PLUGIN_NAME + fileExtension)).toString();
    }

    @Override
    public String getWorldFile(String worldName)
    {
        return getWorldDirectory(worldName) + fileExtension;
    }

    @Override
    public String getWorldFile(Location location)
    {
        return getWorldDirectory(location) + fileExtension;
    }

    @Override
    public String getRegionFile(String worldName, int regionX, int regionZ)
    {
        return getRegionFile(worldName, regionX, regionZ) + fileExtension;
    }

    @Override
    public String getRegionFile(Location location)
    {
        return getRegionFile(location) + fileExtension;
    }

    @Override
    public String getChunkFile(String worldName, int chunkX, int chunkZ)
    {
        return getChunkFile(worldName, chunkX, chunkZ) + fileExtension;
    }

    @Override
    public String getChunkFile(Location location)
    {
        return getChunkFile(location) + fileExtension;
    }
}
