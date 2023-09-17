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

    public String putBlockProperty(int blockX, int blockY, int blockZ, String key, String value)
    {
        return putBlockProperty(new Vector(blockX, blockY, blockZ), key, value);
    }

    @Override
    public String putBlockProperty(Location blockLocation, String key, String value)
    {
        return putBlockProperty(blockLocation.toVector(), key, value);
    }

    public void putBlockProperties(int blockX, int blockY, int blockZ, Map<String, String> keyValueMap)
    {
        putBlockProperties(new Vector(blockX, blockY, blockZ), keyValueMap);
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

    public String removeBlockProperty(int blockX, int blockY, int blockZ, String key)
    {
        return removeBlockProperty(new Vector(blockX, blockY, blockZ), key);
    }

    @Override
    public String removeBlockProperty(Location blockLocation, String key)
    {
        return removeBlockProperty(blockLocation.toVector(), key);
    }

    public void removeBlockProperties(int blockX, int blockY, int blockZ, String... keys)
    {
        removeBlockProperties(new Vector(blockX, blockY, blockZ), keys);
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

    public String getBlockProperty(int blockX, int blockY, int blockZ, String key)
    {
        return getBlockProperty(new Vector(blockX, blockY, blockZ), key);
    }

    @Override
    public String getBlockProperty(Location blockLocation, String key)
    {
        return getBlockProperty(blockLocation.toVector(), key);
    }

    public Map<String, String> getBlockProperties(int blockX, int blockY, int blockZ, String... keys)
    {
        return getBlockProperties(new Vector(blockX, blockY, blockZ), keys);
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
    public RegionBlockPluginPropertiesNode putRegionNode(int regionX, int regionZ, RegionBlockPluginPropertiesNode regionNode)
    {
        return putRegionNode(getRegionVector(regionX, regionZ), regionNode);
    }

    public RegionBlockPluginPropertiesNode putRegionNode(Location blockLocation, RegionBlockPluginPropertiesNode regionNode)
    {
        if (blockLocation == null)
            throw new NullArgumentException("location");

        return putRegionNode(getRegionVector(blockLocation.toVector()), regionNode);
    }

    private RegionBlockPluginPropertiesNode putRegionNode(Vector regionVector, RegionBlockPluginPropertiesNode regionNode)
    {
        if (regionVector == null)
            throw new NullArgumentException("regionVector");
        if (regionNode == null)
            throw new NullArgumentException("regionNode");

        regionNode.setParent(this);
        return children.put(regionVector, regionNode);
    }

    @Override
    public RegionBlockPluginPropertiesNode removeRegionNode(int regionX, int regionZ)
    {
        return removeRegionNode(getRegionVector(regionX, regionZ));
    }

    public RegionBlockPluginPropertiesNode removeRegionNode(Location blockLocation)
    {
        return removeRegionNode(getRegionVector(blockLocation.toVector()));
    }

    private RegionBlockPluginPropertiesNode removeRegionNode(Vector regionVector)
    {
        RegionBlockPluginPropertiesNode regionNode = children.remove(regionVector);
        if (regionNode != null)
            regionNode.setParent(null);
        return regionNode;
    }

    @Override
    public RegionBlockPluginPropertiesNode getRegionNode(int regionX, int regionZ)
    {
        return getRegionNode(getRegionVector(regionX, regionZ));
    }

    public RegionBlockPluginPropertiesNode getRegionNode(Location blockLocation)
    {
        return getRegionNode(getRegionVector(blockLocation.toVector()));
    }

    public RegionBlockPluginPropertiesNode getRegionNode(Vector regionVector)
    {
        return getRegionNode(regionVector, false);
    }

    private RegionBlockPluginPropertiesNode getRegionNode(Vector regionVector, boolean createIfNotExists)
    {
        if (!children.containsKey(regionVector) && createIfNotExists)
            children.put(regionVector, new RegionBlockPluginPropertiesNode(this));

        return children.get(regionVector);
    }

    public boolean isRegionInChildren(int regionX, int regionZ)
    {
        return isRegionInChildren(getRegionVector(regionX, regionZ));
    }

    public boolean isRegionInChildren(Location blockLocation)
    {
        return isRegionInChildren(getRegionVector(blockLocation.toVector()));
    }

    private boolean isRegionInChildren(Vector regionVector)
    {
        return getRegionNode(regionVector) != null;
    }

    /**
     * Returns a new list of contained region co-ordinate vectors
     * @return a list of contained region co-ordinate keys
     */
    @Override
    public List<Vector> getRegionVectors()
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

    private Vector getRegionVector(Vector blockVector)
    {
        // Get Chunk Co-ordinates
        int chunkX = (int) Math.floor(blockVector.getBlockX() / 16);
        int chunkZ = (int) Math.floor(blockVector.getBlockZ() / 16);

        // Get Region Co-ordinates
        int regionX = (int) Math.floor(chunkX / 32);
        int regionZ = (int) Math.floor(chunkZ / 32);

        return getRegionVector(regionX, regionZ);
    }

    private Vector getRegionVector(int regionX, int regionZ)
    {
        return new Vector(regionX, 0, regionZ);
    }
}
