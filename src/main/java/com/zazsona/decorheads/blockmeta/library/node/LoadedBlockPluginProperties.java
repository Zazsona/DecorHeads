package com.zazsona.decorheads.blockmeta.library.node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zazsona.decorheads.blockmeta.library.event.IBlockPluginPropertiesLoadHandler;
import com.zazsona.decorheads.blockmeta.library.event.IBlockPluginPropertiesUnloadHandler;
import org.apache.commons.lang.NullArgumentException;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * Stores the Block Plugin Properties of all blocks that are currently loaded by the server.
 * (i.e, those in loaded chunks visible to players)
 */
public class LoadedBlockPluginProperties implements Listener, IMutableBlockPluginPropertiesNode
{
    private static HashMap<Plugin, LoadedBlockPluginProperties> instances;

    private Plugin plugin;
    private ServerBlockPluginPropertiesNode propertiesTree;
    private Gson gson;
    private List<IBlockPluginPropertiesLoadHandler> loadListeners;
    private List<IBlockPluginPropertiesUnloadHandler> unloadListeners;

    public static LoadedBlockPluginProperties getInstance(Plugin plugin)
    {
        if (instances.containsKey(plugin))
            return instances.get(plugin);

        LoadedBlockPluginProperties newInstance = new LoadedBlockPluginProperties(plugin);
        instances.put(plugin, newInstance);
        return newInstance;
    }

    private LoadedBlockPluginProperties(Plugin plugin)
    {
        this.plugin = plugin;
        this.propertiesTree = new ServerBlockPluginPropertiesNode();
        this.gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

        this.loadListeners = new ArrayList<>();
        this.unloadListeners = new ArrayList<>();

        instances.put(plugin, this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent e)
    {
        try
        {
            Chunk chunk = e.getChunk();
            Location location = chunk.getBlock(0, 0, 0).getLocation();

            WorldBlockPluginPropertiesNode worldNode = propertiesTree.getWorldNode(location);
            if (worldNode == null)
                propertiesTree.putWorldNode(location, new WorldBlockPluginPropertiesNode(propertiesTree));

            if (worldNode.isRegionInChildren(location))
                return; // Data present in cache; nothing for us to do.

            File regionFile = getRegionFile(plugin, location);
            if (regionFile.exists())
            {
                String jsonText = new String(Files.readAllBytes(regionFile.toPath()));
                RegionBlockPluginPropertiesNode regionNodeFromFile = gson.fromJson(jsonText, RegionBlockPluginPropertiesNode.class);
                worldNode.putRegionNode(location, regionNodeFromFile);
            }
            else
                worldNode.putRegionNode(location, new RegionBlockPluginPropertiesNode(worldNode));
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

    @Override
    public String putBlockProperty(Location location, String key, String value)
    {
        if (location.getChunk().isLoaded())
            return propertiesTree.putBlockProperty(location, key, value);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + location);
    }

    @Override
    public void putBlockProperties(Location location, Map<String, String> keyValueMap)
    {
        if (location.getChunk().isLoaded())
            propertiesTree.putBlockProperties(location, keyValueMap);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + location);
    }

    @Override
    public String removeBlockProperty(Location location, String key)
    {
        if (location.getChunk().isLoaded())
            return propertiesTree.removeBlockProperty(location, key);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + location);
    }

    @Override
    public void removeBlockProperties(Location location, String... keys)
    {
        if (location.getChunk().isLoaded())
            propertiesTree.removeBlockProperties(location, keys);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + location);
    }

    @Override
    public String getBlockProperty(Location location, String key)
    {
        if (location.getChunk().isLoaded())
            return propertiesTree.getBlockProperty(location, key);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + location);
    }

    @Override
    public Map<String, String> getBlockProperties(Location location, String... keys)
    {
        if (location.getChunk().isLoaded())
            return propertiesTree.getBlockProperties(location, keys);
        else
            throw new IllegalArgumentException("Location is not loaded by the server: " + location);
    }

    private void saveWorld(World world)
    {
        WorldBlockPluginPropertiesNode worldNode = propertiesTree.getWorldNode(world);
        int initialCachedRegions = worldNode.getRegionValues().size();
        plugin.getLogger().fine("Saving Block Plugin Properties for " + world.getName() + ". (Regions to save: " + initialCachedRegions + ")");

        try
        {
            Chunk[] loadedChunks = world.getLoadedChunks();
            for (Map.Entry<Vector, RegionBlockPluginPropertiesNode> regionEntry : worldNode.getRegionEntries())
            {
                // Get the Region Co-ordinates
                int regionX = regionEntry.getKey().getBlockX();
                int regionZ = regionEntry.getKey().getBlockZ();

                // Write the Region Node to file
                File regionFile = getRegionFile(plugin, world, regionX, regionZ);
                RegionBlockPluginPropertiesNode regionNode = regionEntry.getValue();
                String jsonText = gson.toJson(regionNode);
                BufferedWriter writer = Files.newBufferedWriter(regionFile.toPath(), StandardOpenOption.WRITE);
                writer.write(jsonText);
                writer.flush();
                writer.close();

                // Remove region from cache if it's no longer loaded on the server
                boolean isRegionActive = false;
                for (Chunk chunk : loadedChunks)
                {
                    Vector chunkVector = chunk.getBlock(0, 0, 0).getLocation().toVector();
                    isRegionActive = regionNode.isChunkInChildren(chunkVector);
                    if (isRegionActive)
                        break;
                }
                if (!isRegionActive)
                    worldNode.removeRegionNode(regionEntry.getKey());
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

    private File getRegionFile(Plugin plugin, World world, int regionX, int regionZ)
    {
        if (plugin == null)
            throw new NullArgumentException("plugin");
        if (world == null)
            throw new NullArgumentException("world");

        String worldFolderPath = world.getWorldFolder().getAbsolutePath();
        String pluginFolderName = plugin.getName();
        String regionFileName = String.format("r.{x}.{z}.json", regionX, regionZ);

        File regionFile = Paths.get(worldFolderPath, pluginFolderName, regionFileName).toFile();
        return regionFile;
    }

    private File getRegionFile(Plugin plugin, Location location)
    {
        if (plugin == null)
            throw new NullArgumentException("plugin");
        if (location == null)
            throw new NullArgumentException("location");
        if (location.getWorld() == null)
            throw new IllegalArgumentException("The Location's world cannot be null.");

        int regionXVal = (int) Math.floor(location.getChunk().getX() / 32.0);
        int regionZVal = (int) Math.floor(location.getChunk().getZ() / 32.0);

        return getRegionFile(plugin, location.getWorld(), regionXVal, regionZVal);
    }
}
