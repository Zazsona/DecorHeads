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

public class WorldNode extends Node implements IMutableNode, IMutableRegionNodeParent
{
    protected static final String SERIALIZED_NAME_KEY = "regions";

    @Expose
    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<Vector, RegionNode> children;

    public WorldNode()
    {
        super();
        children = new HashMap<>();
    }

    public WorldNode(Node parent)
    {
        super(parent);
        children = new HashMap<>();
    }

    @Override
    public String putBlockProperty(Location location, String key, String value)
    {
        return putBlockProperty(location.toVector(), key, value);
    }

    public String putBlockProperty(Vector vector, String key, String value)
    {
        return getRegionNode(vector, true).putBlockProperty(vector, key, value);
    }

    @Override
    public String removeBlockProperty(Location location, String key)
    {
        return removeBlockProperty(location.toVector(), key);
    }

    public String removeBlockProperty(Vector vector, String key)
    {
        RegionNode regionNode = getRegionNode(vector, false);
        if (regionNode != null)
            return regionNode.removeBlockProperty(vector, key);
        else
            return null;
    }

    @Override
    public String getBlockProperty(Location location, String key)
    {
        return getBlockProperty(location.toVector(), key);
    }

    public String getBlockProperty(Vector vector, String key)
    {
        RegionNode regionNode = getRegionNode(vector, false);
        if (regionNode != null)
            return regionNode.getBlockProperty(vector, key);
        else
            return null;
    }

    @Override
    public RegionNode putRegionNode(Location location, RegionNode regionNode)
    {
        if (location == null)
            throw new NullArgumentException("location");

        return putRegionNode(location.toVector(), regionNode);
    }

    public RegionNode putRegionNode(Vector vector, RegionNode regionNode)
    {
        if (vector == null)
            throw new NullArgumentException("vector");
        if (regionNode == null)
            throw new NullArgumentException("regionNode");

        Vector key = getRegionKey(vector);
        return children.put(key, regionNode);
    }

    @Override
    public RegionNode removeRegionNode(Location location)
    {
        return removeRegionNode(location.toVector());
    }

    public RegionNode removeRegionNode(Vector vector)
    {
        Vector key = getRegionKey(vector);
        return children.remove(key);
    }

    public boolean isRegionInChildren(Location location)
    {
        return isRegionInChildren(location.toVector());
    }

    public boolean isRegionInChildren(Vector vector)
    {
        return getRegionNode(vector, false) != null;
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
     * Returns a new list of contained {@link RegionNode} values
     * @return a list of contained {@link RegionNode} values
     */
    public List<RegionNode> getRegionValues()
    {
        return new ArrayList<>(children.values());
    }

    /**
     * Returns a new list of {@link RegionNode} key:value pairs
     * @return a list of property key:value pairs, with the key a representation of region location,
     * and the value a {@link RegionNode}
     */
    public List<Map.Entry<Vector, RegionNode>> getRegionEntries()
    {
        return new ArrayList<>(children.entrySet());
    }

    public RegionNode getRegionNode(Location location)
    {
        return getRegionNode(location.toVector());
    }

    public RegionNode getRegionNode(Vector vector)
    {
        return getRegionNode(vector, false);
    }

    private RegionNode getRegionNode(Vector vector, boolean createIfNotExists)
    {
        Vector key = getRegionKey(vector);
        if (!children.containsKey(key) && createIfNotExists)
            children.put(key, new RegionNode(this));

        return children.get(key);
    }

    private Vector getRegionKey(Vector vector)
    {
        // Get Chunk Co-ordinates
        int chunkX = (int) Math.floor(vector.getBlockX() / 16);
        int chunkZ = (int) Math.floor(vector.getBlockZ() / 16);

        // Get Region Co-ordinates
        int regionX = (int) Math.floor(chunkX / 32);
        int regionZ = (int) Math.floor(chunkZ / 32);

        return new Vector(regionX, 0, regionZ);
    }
}
