package com.zazsona.decorheads.blockmeta.library.node;

import com.zazsona.decorheads.blockmeta.library.event.IBlockPluginPropertiesLoadHandler;
import com.zazsona.decorheads.blockmeta.library.event.IBlockPluginPropertiesUnloadHandler;
import com.zazsona.decorheads.blockmeta.library.io.BlockPluginPropertiesFileManager;
import com.zazsona.decorheads.blockmeta.library.io.IRegionBlockPluginPropertiesLoader;
import com.zazsona.decorheads.blockmeta.library.io.IWorldBlockPluginPropertiesSaver;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.*;

/**
 * Stores the Block Plugin Properties of all blocks that are currently loaded by the server.
 * (i.e, those in loaded chunks visible to players)
 */
public class LoadedBlockPluginProperties implements Listener, IMutableBlockPluginPropertiesNode
{
    private static HashMap<Plugin, LoadedBlockPluginProperties> instances = new HashMap<>();

    private Plugin plugin;
    private ServerBlockPluginPropertiesNode propertiesTree;
    private IWorldBlockPluginPropertiesSaver worldSaver;
    private IRegionBlockPluginPropertiesLoader regionLoader;
    private List<IBlockPluginPropertiesLoadHandler> loadListeners;
    private List<IBlockPluginPropertiesUnloadHandler> unloadListeners;

    public static LoadedBlockPluginProperties getInstance(Plugin plugin)
    {
        if (instances.containsKey(plugin))
            return instances.get(plugin);

        BlockPluginPropertiesFileManager fileManager = new BlockPluginPropertiesFileManager(plugin);
        LoadedBlockPluginProperties newInstance = new LoadedBlockPluginProperties(plugin, fileManager, fileManager);
        instances.put(plugin, newInstance);
        return newInstance;
    }

    private LoadedBlockPluginProperties(Plugin plugin, IWorldBlockPluginPropertiesSaver worldSaver, IRegionBlockPluginPropertiesLoader regionLoader)
    {
        this.plugin = plugin;
        this.propertiesTree = new ServerBlockPluginPropertiesNode();
        this.worldSaver = worldSaver;
        this.regionLoader = regionLoader;

        this.loadListeners = new ArrayList<>();
        this.unloadListeners = new ArrayList<>();

        instances.put(plugin, this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent e)
    {
        try
        {
            // Get region...
            Chunk chunk = e.getChunk();
            World world = chunk.getWorld();
            int regionX = (int) Math.floor(chunk.getX() / 32);
            int regionZ = (int) Math.floor(chunk.getZ() / 32);

            // Get world node...
            WorldBlockPluginPropertiesNode worldNode = propertiesTree.getWorldNode(world);
            if (worldNode == null)
                propertiesTree.putWorldNode(world, new WorldBlockPluginPropertiesNode(propertiesTree));

            // Do nothing if region already loaded; otherwise load & cache
            if (worldNode.isRegionInChildren(regionX, regionZ))
                return; // Data present in cache; nothing for us to do.

            RegionBlockPluginPropertiesNode regionNode = regionLoader.loadRegion(world, regionX, regionZ);
            if (regionNode == null)
                regionNode = new RegionBlockPluginPropertiesNode(worldNode);

            worldNode.putRegionNode(regionX, regionZ, regionNode);
        }
        catch (IOException ex)
        {
            plugin.getLogger().severe("Failed to load BlockPluginProperties for Chunk X:" + e.getChunk().getX() + " Z:" + e.getChunk().getZ() + " in World \"" + e.getWorld().getName() + "\": " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldSave(WorldSaveEvent e)
    {
        saveWorld(e.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPluginDisable(PluginDisableEvent e)
    {
        if (e.getPlugin() == plugin)
        {
            List<World> worlds = Bukkit.getWorlds();
            for (World world : worlds)
                saveWorld(world);
        }
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

    public String putBlockProperty(int blockX, int blockY, int blockZ, World world, String key, String value)
    {
        return putBlockProperty(new Location(world, blockX, blockY, blockZ), key, value);
    }

    public void putBlockProperties(int blockX, int blockY, int blockZ, World world, Map<String, String> keyValueMap)
    {
        putBlockProperties(new Location(world, blockX, blockY, blockZ), keyValueMap);
    }

    @Override
    public String putBlockProperty(Location blockLocation, String key, String value)
    {
        if (blockLocation.getChunk().isLoaded())
            return propertiesTree.putBlockProperty(blockLocation, key, value);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + blockLocation);
    }

    @Override
    public void putBlockProperties(Location blockLocation, Map<String, String> keyValueMap)
    {
        if (blockLocation.getChunk().isLoaded())
            propertiesTree.putBlockProperties(blockLocation, keyValueMap);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + blockLocation);
    }

    public String putBlockProperty(Vector blockVector, World world, String key, String value)
    {
        return putBlockProperty(blockVector.toLocation(world), key, value);
    }

    public void putBlockProperties(Vector blockVector, World world, Map<String, String> keyValueMap)
    {
        putBlockProperties(blockVector.toLocation(world), keyValueMap);
    }

    public String removeBlockProperty(int blockX, int blockY, int blockZ, World world, String key)
    {
        return removeBlockProperty(new Location(world, blockX, blockY, blockZ), key);
    }

    public void removeBlockProperties(int blockX, int blockY, int blockZ, World world, String... keys)
    {
        removeBlockProperties(new Location(world, blockX, blockY, blockZ), keys);
    }

    @Override
    public String removeBlockProperty(Location blockLocation, String key)
    {
        if (blockLocation.getChunk().isLoaded())
            return propertiesTree.removeBlockProperty(blockLocation, key);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + blockLocation);
    }

    @Override
    public void removeBlockProperties(Location blockLocation, String... keys)
    {
        if (blockLocation.getChunk().isLoaded())
            propertiesTree.removeBlockProperties(blockLocation, keys);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + blockLocation);
    }

    public String removeBlockProperty(Vector blockVector, World world, String key)
    {
        return removeBlockProperty(blockVector.toLocation(world), key);
    }

    public void removeBlockProperties(Vector blockVector, World world, String... keys)
    {
        removeBlockProperties(blockVector.toLocation(world), keys);
    }

    public String getBlockProperty(int blockX, int blockY, int blockZ, World world, String key)
    {
        return getBlockProperty(new Location(world, blockX, blockY, blockZ), key);
    }

    public Map<String, String> getBlockProperties(int blockX, int blockY, int blockZ, World world, String... keys)
    {
        return getBlockProperties(new Location(world, blockX, blockY, blockZ), keys);
    }

    @Override
    public String getBlockProperty(Location blockLocation, String key)
    {
        if (blockLocation.getChunk().isLoaded())
            return propertiesTree.getBlockProperty(blockLocation, key);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + blockLocation);
    }

    @Override
    public Map<String, String> getBlockProperties(Location blockLocation, String... keys)
    {
        if (blockLocation.getChunk().isLoaded())
            return propertiesTree.getBlockProperties(blockLocation, keys);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + blockLocation);
    }

    public String getBlockProperty(Vector blockVector, World world, String key)
    {
        return getBlockProperty(blockVector.toLocation(world), key);
    }

    public Map<String, String> getBlockProperties(Vector blockVector, World world, String... keys)
    {
        return getBlockProperties(blockVector.toLocation(world), keys);
    }

    private void saveWorld(World world)
    {
        WorldBlockPluginPropertiesNode worldNode = propertiesTree.getWorldNode(world);
        int initialCachedRegions = worldNode.getRegionValues().size();
        plugin.getLogger().fine("Saving Block Plugin Properties for " + world.getName() + ". (Regions to save: " + initialCachedRegions + ")");

        try
        {
            worldSaver.saveWorld(worldNode, world);

            Chunk[] loadedChunks = world.getLoadedChunks();
            for (Map.Entry<Vector, RegionBlockPluginPropertiesNode> regionEntry : worldNode.getRegionEntries())
            {
                Vector regionVector = regionEntry.getKey();
                int regionX = regionVector.getBlockX();
                int regionZ = regionVector.getBlockZ();
                RegionBlockPluginPropertiesNode regionNode = regionEntry.getValue();

                // Remove region from cache if it's no longer loaded on the server
                boolean isRegionActive = false;
                for (Chunk chunk : loadedChunks)
                {
                    isRegionActive = regionNode.isChunkInChildren(chunk.getX(), chunk.getZ());
                    if (isRegionActive)
                        break;
                }
                if (!isRegionActive)
                    worldNode.removeRegionNode(regionX, regionZ);
            }
            int remainingCachedRegions = worldNode.getRegionValues().size();
            plugin.getLogger().fine("Successfully saved Block Plugin Properties for " + world.getName() + ". (Cached region count: " + initialCachedRegions + " => " + remainingCachedRegions + ")");
        }
        catch (IOException ex)
        {
            plugin.getLogger().severe("Failed to save Block Plugin Properties for World \"" + world.getName() + "\": " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
