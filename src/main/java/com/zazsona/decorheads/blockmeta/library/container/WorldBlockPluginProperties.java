package com.zazsona.decorheads.blockmeta.library.container;

import com.google.gson.annotations.SerializedName;
import com.zazsona.decorheads.blockmeta.library.BlockPluginPropertiesContainerFactory;
import com.zazsona.decorheads.blockmeta.library.IBlockPluginPropertiesContainerFactory;
import com.zazsona.decorheads.blockmeta.library.IMutableBlockPluginPropertiesContainerFactory;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorldBlockPluginProperties implements IMutableBlockPluginPropertiesContainer, IMutableRegionBlockPluginPropertiesContainer, IBlockPluginPropertiesContainer
{
    private static final String REGION_LOCATION_KEY_FORMAT = "{x}.{z}";
    protected static final String SERIALIZED_NAME_KEY = "regions";

    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<String, IMutableChunkBlockPluginPropertiesContainer> locationToRegion;

    private transient IMutableBlockPluginPropertiesContainerFactory regionFactory;

    public WorldBlockPluginProperties()
    {
        this.locationToRegion = new HashMap<>();
        this.regionFactory = new BlockPluginPropertiesContainerFactory();
    }

    public WorldBlockPluginProperties(IMutableBlockPluginPropertiesContainerFactory subcontainerFactory)
    {
        this.locationToRegion = new HashMap<>();
        this.regionFactory = subcontainerFactory;
    }

    public WorldBlockPluginProperties(HashMap<String, IMutableChunkBlockPluginPropertiesContainer> locationToChunk)
    {
        this.locationToRegion = locationToChunk;
        this.regionFactory = new BlockPluginPropertiesContainerFactory();
    }

    public WorldBlockPluginProperties(HashMap<String, IMutableChunkBlockPluginPropertiesContainer> locationToChunk, IMutableBlockPluginPropertiesContainerFactory subcontainerFactory)
    {
        this.locationToRegion = locationToChunk;
        this.regionFactory = subcontainerFactory;
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
        String regionKey = location.getRegionKey();
        if (!locationToRegion.containsKey(regionKey))
            locationToRegion.put(regionKey, regionFactory.makeRegionContainer(location));

        locationToRegion.get(regionKey).addBlockPluginProperty(location, key, value);
    }

    /**
     * Removes a block meta entry
     * @param location the block location
     * @param key an identifying key for the data field
     */
    @Override
    public void removeBlockPluginProperty(Location location, String key)
    {
        String regionKey = location.getRegionKey();
        if (locationToRegion.containsKey(regionKey))
        {
            IMutableBlockPluginPropertiesContainer region = locationToRegion.get(regionKey);
            region.removeBlockPluginProperty(location, key);
            if (region.getBlocksCount() == 0)
                locationToRegion.remove(regionKey);
        }
    }

    /**
     * Gets a block meta entry, consisting of key:value pairs
     * @param location the block location
     */
    @Override
    public HashMap<String, String> getBlockPluginProperties(Location location)
    {
        String regionKey = location.getRegionKey();
        if (!locationToRegion.containsKey(regionKey))
        {
            IMutableChunkBlockPluginPropertiesContainer regionContainer = regionFactory.makeRegionContainer(location);
            locationToRegion.put(regionKey, regionContainer);
        }
        return locationToRegion.get(regionKey).getBlockPluginProperties(location);
    }

    public List<Location> getBlockLocations()
    {
        List<Location> locations = new ArrayList<>();
        for (IChunkBlockPluginPropertiesContainer regionBlockPluginProperties : locationToRegion.values())
            locations.addAll(regionBlockPluginProperties.getBlockLocations());
        return locations;
    }

    public int getBlocksCount()
    {
        int blocksCount = 0;
        for (IChunkBlockPluginPropertiesContainer regionBlockPluginProperties : locationToRegion.values())
            blocksCount += regionBlockPluginProperties.getBlocksCount();
        return blocksCount;
    }

    @Override
    public IChunkBlockPluginPropertiesContainer getRegion(Location location)
    {
        return locationToRegion.get(location.getRegionKey());
    }

    @Override
    public IChunkBlockPluginPropertiesContainer getOrDefaultRegion(Location location, IChunkBlockPluginPropertiesContainer defaultValue)
    {
        IChunkBlockPluginPropertiesContainer storedProperties = getRegion(location);
        return (storedProperties == null) ? defaultValue : storedProperties;
    }

    @Override
    public void setRegion(Location location, IMutableChunkBlockPluginPropertiesContainer regionProperties)
    {
        locationToRegion.put(location.getRegionKey(), regionProperties);
    }

    @Override
    public IChunkBlockPluginPropertiesContainer removeRegion(Location location)
    {
        return locationToRegion.remove(location.getRegionKey());
    }
}
