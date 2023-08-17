package com.zazsona.decorheads.blockmeta.library.container;

import com.google.gson.annotations.SerializedName;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class ChunkBlockPluginProperties implements IMutableBlockPluginPropertiesContainer
{
    private static final String BLOCK_LOCATION_KEY_FORMAT = "{x}.{y}.{z}";
    protected static final String SERIALIZED_NAME_KEY = "blocks";

    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<String, HashMap<String, String>> locationToBlockProperties;

    public ChunkBlockPluginProperties()
    {
        this.locationToBlockProperties = new HashMap<>();
    }

    public ChunkBlockPluginProperties(HashMap<String, HashMap<String, String>> locationToBlockProperties)
    {
        this.locationToBlockProperties = locationToBlockProperties;
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
        String blockKey = getBlockKey(location);
        HashMap<String, String> blockMeta = getBlockPluginProperties(location);
        blockMeta.put(key, value);
        locationToBlockProperties.put(blockKey, blockMeta);
    }

    /**
     * Removes a block meta entry
     * @param location the block location
     * @param key an identifying key for the data field
     */
    @Override
    public void removeBlockPluginProperty(Location location, String key)
    {
        String blockKey = getBlockKey(location);
        HashMap<String, String> blockMeta = getBlockPluginProperties(location);
        blockMeta.remove(key);
        locationToBlockProperties.put(blockKey, blockMeta);

        if (locationToBlockProperties.get(blockKey).size() == 0)
            locationToBlockProperties.remove(blockKey);
    }

    /**
     * Gets a block meta entry, consisting of key:value pairs
     * @param location the block location
     */
    @Override
    public HashMap<String, String> getBlockPluginProperties(Location location)
    {
        String blockKey = getBlockKey(location);
        HashMap<String, String> blockMetaEntries = locationToBlockProperties.get(blockKey);
        return (blockMetaEntries != null) ? blockMetaEntries : new HashMap<>();
    }

    /**
     * Gets the locations of blocks with plugin properties
     * @return locations of the blocks, with world as "null"
     */
    public List<Location> getBlockLocations()
    {
        Set<String> keys = locationToBlockProperties.keySet();
        List<Location> locations = new ArrayList<>();
        for (String key : keys)
            locations.add(getBlockLocation(key, null));

        return locations;
    }

    /**
     * Gets the number of blocks registered to this container
     * @return the number of blocks with properties in this container
     */
    public int getBlocksCount()
    {
        return locationToBlockProperties.size();
    }

    private String getBlockKey(Location location)
    {
        String blockX = String.valueOf(location.getBlockX());
        String blockY = String.valueOf(location.getBlockY());
        String blockZ = String.valueOf(location.getBlockZ());
        return BLOCK_LOCATION_KEY_FORMAT.replace("{x}", blockX).replace("{y}", blockY).replace("{z}", blockZ);
    }

    private Location getBlockLocation(String blockKey)
    {
        return getBlockLocation(blockKey, null);
    }

    private Location getBlockLocation(String blockKey, World world)
    {
        String[] coordinates = blockKey.split("\\.");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        int z = Integer.parseInt(coordinates[2]);

        Location location = new Location(world, x, y, z);
        return location;
    }
}
