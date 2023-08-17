package com.zazsona.decorheads.blockmeta.library.container;

import com.google.gson.annotations.SerializedName;
import com.zazsona.decorheads.blockmeta.library.BlockPluginPropertiesContainerFactory;
import com.zazsona.decorheads.blockmeta.library.IBlockPluginPropertiesContainerFactory;
import com.zazsona.decorheads.blockmeta.library.IMutableBlockPluginPropertiesContainerFactory;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ServerBlockPluginProperties implements IMutableBlockPluginPropertiesContainer, IMutableWorldBlockPluginPropertiesContainer, IBlockPluginPropertiesContainer
{
    protected static final String SERIALIZED_NAME_KEY = "worlds";

    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<String, IMutableRegionBlockPluginPropertiesContainer> worldIdToWorld;

    private transient IMutableBlockPluginPropertiesContainerFactory worldFactory;

    public ServerBlockPluginProperties()
    {
        this.worldIdToWorld = new HashMap<>();
        this.worldFactory = new BlockPluginPropertiesContainerFactory();
    }

    public ServerBlockPluginProperties(IMutableBlockPluginPropertiesContainerFactory subcontainerFactory)
    {
        this.worldIdToWorld = new HashMap<>();
        this.worldFactory = subcontainerFactory;
    }

    public ServerBlockPluginProperties(HashMap<String, IMutableRegionBlockPluginPropertiesContainer> worldIdToWorld)
    {
        this.worldIdToWorld = worldIdToWorld;
        this.worldFactory = new BlockPluginPropertiesContainerFactory();
    }

    public ServerBlockPluginProperties(HashMap<String, IMutableRegionBlockPluginPropertiesContainer> worldIdToWorld, IMutableBlockPluginPropertiesContainerFactory subcontainerFactory)
    {
        this.worldIdToWorld = worldIdToWorld;
        this.worldFactory = subcontainerFactory;
    }

    /**
     * Creates a new block meta entry
     * @param location the block location
     * @param key an identifying key for the data field
     * @param value the data value
     */
    @Override
    public void addBlockPluginProperty(Location location, String key, String value)
    {
        if (location.getWorld() == null)
            throw new IllegalArgumentException("World cannot be null.");

        String worldId = location.getWorld().getUID().toString();
        if (!worldIdToWorld.containsKey(worldId))
            worldIdToWorld.put(worldId, worldFactory.makeWorldContainer(location));

        worldIdToWorld.get(worldId).addBlockPluginProperty(location, key, value);
    }

    /**
     * Removes a block meta entry
     * @param location the block location
     * @param key an identifying key for the data field
     */
    @Override
    public void removeBlockPluginProperty(Location location, String key)
    {
        if (location.getWorld() == null)
            throw new IllegalArgumentException("World cannot be null.");

        String worldId = location.getWorld().getUID().toString();
        if (worldIdToWorld.containsKey(worldId))
        {
            IMutableRegionBlockPluginPropertiesContainer world = worldIdToWorld.get(worldId);
            world.removeBlockPluginProperty(location, key);
            if (world.getBlocksCount() == 0)
                worldIdToWorld.remove(worldId);
        }
    }

    /**
     * Gets a block meta entry, consisting of key:value pairs
     * @param location the block location
     */
    @Override
    public HashMap<String, String> getBlockPluginProperties(Location location)
    {
        if (location.getWorld() == null)
            throw new IllegalArgumentException("World cannot be null.");

        String worldId = location.getWorld().getUID().toString();
        if (!worldIdToWorld.containsKey(worldId))
        {
            IMutableRegionBlockPluginPropertiesContainer worldContainer = worldFactory.makeWorldContainer(location);
            worldIdToWorld.put(worldId, worldContainer);
        }
        return worldIdToWorld.get(worldId).getBlockPluginProperties(location);
    }

    public List<Location> getBlockLocations()
    {
        List<Location> locations = new ArrayList<>();
        for (Map.Entry<String, IMutableRegionBlockPluginPropertiesContainer> worldBlockPluginPropertiesEntry : worldIdToWorld.entrySet())
        {
            World world = Bukkit.getWorld(worldBlockPluginPropertiesEntry.getKey());
            List<Location> worldLocations = worldBlockPluginPropertiesEntry.getValue().getBlockLocations();
            worldLocations.forEach(location -> location.setWorld(world));
            locations.addAll(worldLocations);
        }
        return locations;
    }

    public int getBlocksCount()
    {
        int blocksCount = 0;
        for (IRegionBlockPluginPropertiesContainer worldBlockPluginProperties : worldIdToWorld.values())
            blocksCount += worldBlockPluginProperties.getBlocksCount();
        return blocksCount;
    }

    @Override
    public IRegionBlockPluginPropertiesContainer getWorld(Location location)
    {
        if (location.getWorld() == null)
            throw new IllegalArgumentException("World cannot be null.");

        return worldIdToWorld.get(location.getWorld().getUID().toString());
    }

    @Override
    public IRegionBlockPluginPropertiesContainer getWorld(String worldId)
    {
        return worldIdToWorld.get(worldId);
    }

    @Override
    public IRegionBlockPluginPropertiesContainer getWorld(World world)
    {
        return worldIdToWorld.get(world.getUID().toString());
    }

    @Override
    public IRegionBlockPluginPropertiesContainer getOrDefaultWorld(Location location, IRegionBlockPluginPropertiesContainer defaultValue)
    {
        IRegionBlockPluginPropertiesContainer storedProperties = getWorld(location);
        return (storedProperties == null) ? defaultValue : storedProperties;
    }

    @Override
    public IRegionBlockPluginPropertiesContainer getOrDefaultWorld(String worldId, IRegionBlockPluginPropertiesContainer defaultValue)
    {
        IRegionBlockPluginPropertiesContainer storedProperties = getWorld(worldId);
        return (storedProperties == null) ? defaultValue : storedProperties;
    }

    @Override
    public IRegionBlockPluginPropertiesContainer getOrDefaultWorld(World world, IRegionBlockPluginPropertiesContainer defaultValue)
    {
        IRegionBlockPluginPropertiesContainer storedProperties = getWorld(world);
        return (storedProperties == null) ? defaultValue : storedProperties;
    }


    @Override
    public void setWorld(Location location, IMutableRegionBlockPluginPropertiesContainer worldProperties)
    {
        if (location.getWorld() == null)
            throw new IllegalArgumentException("World cannot be null.");

        worldIdToWorld.put(location.getWorld().getUID().toString(), worldProperties);
    }

    @Override
    public void setWorld(String worldId, IMutableRegionBlockPluginPropertiesContainer worldProperties)
    {
        worldIdToWorld.put(worldId, worldProperties);
    }

    @Override
    public void setWorld(World world, IMutableRegionBlockPluginPropertiesContainer worldProperties)
    {
        worldIdToWorld.put(world.getUID().toString(), worldProperties);
    }

    @Override
    public IRegionBlockPluginPropertiesContainer removeWorld(Location location)
    {
        if (location.getWorld() == null)
            throw new IllegalArgumentException("World cannot be null.");

        return worldIdToWorld.remove(location.getWorld().getUID().toString());
    }

    @Override
    public IRegionBlockPluginPropertiesContainer removeWorld(String worldId)
    {
        return worldIdToWorld.remove(worldId);
    }

    @Override
    public IRegionBlockPluginPropertiesContainer removeWorld(World world)
    {
        return worldIdToWorld.remove(world.getUID().toString());
    }
}
