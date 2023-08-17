package com.zazsona.decorheads.blockmeta.library.container;

import com.zazsona.decorheads.blockmeta.library.IMutableBlockPluginPropertiesContainerFactory;
import com.zazsona.decorheads.blockmeta.library.event.BlockPluginPropertiesLoadEvent;
import com.zazsona.decorheads.blockmeta.library.event.BlockPluginPropertiesUnloadEvent;
import com.zazsona.decorheads.blockmeta.library.event.IBlockPluginPropertiesLoadHandler;
import com.zazsona.decorheads.blockmeta.library.event.IBlockPluginPropertiesUnloadHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO: Attach this so it gets listened to

/**
 * Stores the Block Plugin Properties of all blocks that are currently loaded by the server.
 * (i.e, those in loaded chunks visible to players)
 */
public class ServerLoadedBlockPluginProperties implements Listener, IMutableBlockPluginPropertiesContainer
{
    private IMutableWorldBlockPluginPropertiesContainer propertiesContainer;
    private IMutableBlockPluginPropertiesContainerFactory factory;

    private List<IBlockPluginPropertiesLoadHandler> loadListeners;
    private List<IBlockPluginPropertiesUnloadHandler> unloadListeners;

    public ServerLoadedBlockPluginProperties(IMutableWorldBlockPluginPropertiesContainer propertiesContainer, IMutableBlockPluginPropertiesContainerFactory factory)
    {
        this.propertiesContainer = propertiesContainer;
        this.factory = factory;
        this.loadListeners = new ArrayList<>();
        this.unloadListeners = new ArrayList<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent e)
    {
        Chunk chunk = e.getChunk();
        Location location = chunk.getBlock(0, 0, 0).getLocation();

        IMutableRegionBlockPluginPropertiesContainer worldProperties = (IMutableRegionBlockPluginPropertiesContainer) propertiesContainer.getWorld(location);
        if (worldProperties == null)
            propertiesContainer.setWorld(location, factory.makeWorldContainer(location));

        IMutableChunkBlockPluginPropertiesContainer regionProperties = (IMutableChunkBlockPluginPropertiesContainer) worldProperties.getRegion(location);
        if (regionProperties == null)
            worldProperties.setRegion(location, factory.makeRegionContainer(location));

        IMutableBlockPluginPropertiesContainer chunkProperties = (IMutableBlockPluginPropertiesContainer) regionProperties.getChunk(location);
        if (chunkProperties == null)
            regionProperties.setChunk(location, factory.makeChunkContainer(location));

        BlockPluginPropertiesLoadEvent loadEvent = new BlockPluginPropertiesLoadEvent(regionProperties.getChunk(location));
        for (IBlockPluginPropertiesLoadHandler loadListener : loadListeners)
            loadListener.onBlockPluginPropertiesLoad(loadEvent);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent e)
    {
        Chunk chunk = e.getChunk();
        Location location = chunk.getBlock(0, 0, 0).getLocation();

        IMutableRegionBlockPluginPropertiesContainer worldProperties = (IMutableRegionBlockPluginPropertiesContainer) propertiesContainer.getWorld(location);
        if (worldProperties == null)
            return;

        IMutableChunkBlockPluginPropertiesContainer regionProperties = (IMutableChunkBlockPluginPropertiesContainer) worldProperties.getRegion(location);
        if (regionProperties == null)
            return;

        IMutableBlockPluginPropertiesContainer chunkProperties = (IMutableBlockPluginPropertiesContainer) regionProperties.getChunk(location);
        BlockPluginPropertiesUnloadEvent unloadEvent = new BlockPluginPropertiesUnloadEvent(chunkProperties);
        for (IBlockPluginPropertiesUnloadHandler unloadListener : unloadListeners)
            unloadListener.onBlockPluginPropertiesUnload(unloadEvent);

        // TODO: Listen to this event and save to region file
        // Will somehow need to pass world data to the event, so we know the world directory...

        regionProperties.removeChunk(location);
        if (regionProperties.getBlocksCount() == 0)
            worldProperties.removeRegion(location);
        if (worldProperties.getBlocksCount() == 0)
            propertiesContainer.removeWorld(location);
    }

    public boolean addLoadListener(IBlockPluginPropertiesLoadHandler listener)
    {
        return loadListeners.add(listener);
    }

    public boolean removeLoadListener(IBlockPluginPropertiesLoadHandler listener)
    {
        return loadListeners.remove(listener);
    }

    public boolean addUnloadListener(IBlockPluginPropertiesUnloadHandler listener)
    {
        return unloadListeners.add(listener);
    }

    public boolean removeUnloadListener(IBlockPluginPropertiesUnloadHandler listener)
    {
        return unloadListeners.remove(listener);
    }

    public boolean isLocationLoaded(Location location)
    {
        IMutableRegionBlockPluginPropertiesContainer worldProperties = (IMutableRegionBlockPluginPropertiesContainer) propertiesContainer.getWorld(location);
        if (worldProperties == null)
            return false;
        IMutableChunkBlockPluginPropertiesContainer regionProperties = (IMutableChunkBlockPluginPropertiesContainer) worldProperties.getRegion(location);
        if (regionProperties == null)
            return false;
        IMutableBlockPluginPropertiesContainer chunkProperties = (IMutableBlockPluginPropertiesContainer) regionProperties.getChunk(location);
        if (chunkProperties == null)
            return false;

        return true;
    }

    @Override
    public int getBlocksCount()
    {
        return propertiesContainer.getBlocksCount();
    }

    @Override
    public List<Location> getBlockLocations()
    {
        return propertiesContainer.getBlockLocations();
    }

    @Override
    public HashMap<String, String> getBlockPluginProperties(Location location)
    {
        return propertiesContainer.getBlockPluginProperties(location);
    }

    @Override
    public void addBlockPluginProperty(Location location, String key, String value)
    {
        if (!isLocationLoaded(location))
            throw new IllegalStateException("Location not currently loaded by the server: " + location.toString());

        propertiesContainer.addBlockPluginProperty(location, key, value);
    }

    @Override
    public void removeBlockPluginProperty(Location location, String key)
    {
        if (!isLocationLoaded(location))
            throw new IllegalStateException("Location not currently loaded by the server: " + location.toString());

        propertiesContainer.removeBlockPluginProperty(location, key);
    }
}
