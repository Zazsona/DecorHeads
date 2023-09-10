package com.zazsona.decorheads.blockmeta.library.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldBlockPluginPropertiesNode extends Node implements IMutableBlockPluginPropertiesNode, IMutableRegionBlockPluginPropertiesNodeParent
{
    protected static final String SERIALIZED_NAME_KEY = "regions";

    @Expose
    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<Vector, RegionBlockPluginPropertiesNode> children;

    public WorldBlockPluginPropertiesNode()
    {
        super();
        children = new HashMap<>();
    }

    WorldBlockPluginPropertiesNode(Node parent)
    {
        super(parent);
        children = new HashMap<>();
    }

    @Override
    public String putBlockProperty(Location blockLocation, String key, String value)
    {
        return putBlockProperty(blockLocation.toVector(), key, value);
    }

    @Override
    public void putBlockProperties(Location blockLocation, Map<String, String> keyValueMap)
    {
        putBlockProperties(blockLocation.toVector(), keyValueMap);
    }

    public String putBlockProperty(Vector blockVector, String key, String value)
    {
        return getRegionNode(blockVector, true).putBlockProperty(blockVector, key, value);
    }

    public void putBlockProperties(Vector blockVector, Map<String, String> keyValueMap)
    {
        getRegionNode(blockVector, true).putBlockProperties(blockVector, keyValueMap);
    }

    @Override
    public String removeBlockProperty(Location blockLocation, String key)
    {
        return removeBlockProperty(blockLocation.toVector(), key);
    }

    @Override
    public void removeBlockProperties(Location blockLocation, String... keys)
    {
        removeBlockProperties(blockLocation.toVector(), keys);
    }

    public String removeBlockProperty(Vector blockVector, String key)
    {
        RegionBlockPluginPropertiesNode regionNode = getRegionNode(blockVector, false);
        if (regionNode != null)
            return regionNode.removeBlockProperty(blockVector, key);
        else
            return null;
    }

    public void removeBlockProperties(Vector blockVector, String... keys)
    {
        RegionBlockPluginPropertiesNode regionNode = getRegionNode(blockVector, false);
        if (regionNode != null)
            regionNode.removeBlockProperties(blockVector, keys);
    }

    @Override
    public String getBlockProperty(Location blockLocation, String key)
    {
        return getBlockProperty(blockLocation.toVector(), key);
    }

    @Override
    public Map<String, String> getBlockProperties(Location blockLocation, String... keys)
    {
        return getBlockProperties(blockLocation.toVector(), keys);
    }

    public String getBlockProperty(Vector blockVector, String key)
    {
        RegionBlockPluginPropertiesNode regionNode = getRegionNode(blockVector, false);
        if (regionNode != null)
            return regionNode.getBlockProperty(blockVector, key);
        else
            return null;
    }

    public Map<String, String> getBlockProperties(Vector blockVector, String... keys)
    {
        RegionBlockPluginPropertiesNode regionNode = getRegionNode(blockVector, false);
        if (regionNode != null)
            return regionNode.getBlockProperties(blockVector, keys);
        else
            return new HashMap<>();
    }

    @Override
    public RegionBlockPluginPropertiesNode putRegionNode(Location blockLocation, RegionBlockPluginPropertiesNode regionNode)
    {
        if (blockLocation == null)
            throw new NullArgumentException("location");

        return putRegionNode(blockLocation.toVector(), regionNode);
    }

    public RegionBlockPluginPropertiesNode putRegionNode(Vector blockVector, RegionBlockPluginPropertiesNode regionNode)
    {
        if (blockVector == null)
            throw new NullArgumentException("blockVector");
        if (regionNode == null)
            throw new NullArgumentException("regionNode");

        Vector key = getRegionKey(blockVector);
        regionNode.setParent(this);
        return children.put(key, regionNode);
    }

    @Override
    public RegionBlockPluginPropertiesNode removeRegionNode(Location blockLocation)
    {
        return removeRegionNode(blockLocation.toVector());
    }

    public RegionBlockPluginPropertiesNode removeRegionNode(Vector blockVector)
    {
        Vector key = getRegionKey(blockVector);
        RegionBlockPluginPropertiesNode regionNode = children.remove(key);
        if (regionNode != null)
            regionNode.setParent(null);
        return regionNode;
    }

    public boolean isRegionInChildren(Location location)
    {
        return isRegionInChildren(location.toVector());
    }

    public boolean isRegionInChildren(Vector blockVector)
    {
        return getRegionNode(blockVector, false) != null;
    }

    /**
     * Returns a new list of contained region location keys
     * @return a list of contained region location keys
     */
    public List<Vector> getRegionKeys()
    {
        return new ArrayList<>(children.keySet());
    }

    /**
     * Returns a new list of contained {@link RegionBlockPluginPropertiesNode} values
     * @return a list of contained {@link RegionBlockPluginPropertiesNode} values
     */
    public List<RegionBlockPluginPropertiesNode> getRegionValues()
    {
        return new ArrayList<>(children.values());
    }

    /**
     * Returns a new list of {@link RegionBlockPluginPropertiesNode} key:value pairs
     * @return a list of property key:value pairs, with the key a representation of region location,
     * and the value a {@link RegionBlockPluginPropertiesNode}
     */
    public List<Map.Entry<Vector, RegionBlockPluginPropertiesNode>> getRegionEntries()
    {
        return new ArrayList<>(children.entrySet());
    }

    public RegionBlockPluginPropertiesNode getRegionNode(Location blockLocation)
    {
        return getRegionNode(blockLocation.toVector());
    }

    public RegionBlockPluginPropertiesNode getRegionNode(Vector blockVector)
    {
        return getRegionNode(blockVector, false);
    }

    private RegionBlockPluginPropertiesNode getRegionNode(Vector blockVector, boolean createIfNotExists)
    {
        Vector key = getRegionKey(blockVector);
        if (!children.containsKey(key) && createIfNotExists)
            children.put(key, new RegionBlockPluginPropertiesNode(this));

        return children.get(key);
    }

    private Vector getRegionKey(Vector blockVector)
    {
        // Get Chunk Co-ordinates
        int chunkX = (int) Math.floor(blockVector.getBlockX() / 16);
        int chunkZ = (int) Math.floor(blockVector.getBlockZ() / 16);

        // Get Region Co-ordinates
        int regionX = (int) Math.floor(chunkX / 32);
        int regionZ = (int) Math.floor(chunkZ / 32);

        return new Vector(regionX, 0, regionZ);
    }
}
