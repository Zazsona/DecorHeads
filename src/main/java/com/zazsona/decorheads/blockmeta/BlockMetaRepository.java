package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.event.RegionEvent;
import com.zazsona.decorheads.event.RegionLoadEvent;
import com.zazsona.decorheads.event.RegionUnloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BlockMetaRepository implements Listener
{
    public static final String REGION_FILE_NAME_FORMAT = "r.%s.json";
    private static BlockMetaRepository instance;

    private Map<String, Map<String, BlockMetaRegionData>> worldRegionDataMap = new HashMap<>();

    public static BlockMetaRepository getInstance()
    {
        if (instance == null)
            instance = new BlockMetaRepository();
        return instance;
    }

    /**
     * Gets the meta entries for a region
     * @param regionKey the region key
     * @param worldName the region's world
     * @return the region's meta data
     */
    public BlockMetaRegionData getRegionData(String regionKey, @NotNull String worldName) throws IOException
    {
        if (worldRegionDataMap.containsKey(worldName) && worldRegionDataMap.get(worldName).containsKey(regionKey))
            return worldRegionDataMap.get(worldName).get(regionKey);

        File regionFile = getRegionFile(regionKey, worldName);
        BlockMetaRegionData regionData = new BlockMetaRegionData(regionFile);
        return regionData;
    }

    /**
     * Gets the meta entries for a chunk's region
     * @param chunk the chunk residing in a region
     * @return the region's meta data
     */
    public BlockMetaRegionData getRegionData(Chunk chunk) throws IOException
    {
        return getRegionData(RegionEvent.getRegionKey(chunk), chunk.getWorld().getName());
    }

    /**
     * Loads the meta file for the provided chunk's region
     * @param regionKey the region key
     * @param worldName the region's world
     * @return the file
     */
    private File getRegionFile(String regionKey, String worldName)
    {
        String fileName = String.format(REGION_FILE_NAME_FORMAT, regionKey);
        String filePath = Paths.get(Bukkit.getServer().getWorldContainer().toString(), worldName, DecorHeadsPlugin.PLUGIN_NAME.toLowerCase(), fileName).toString();
        return new File(filePath);
    }

    /**
     * Loads the meta file for the provided chunk's region
     * @param chunk the chunk to load the region meta for
     * @return the file
     */
    private File getRegionFile(Chunk chunk)
    {
        String regionKey = RegionEvent.getRegionKey(chunk);
        String worldName = chunk.getWorld().getName();
        return getRegionFile(regionKey, worldName);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRegionLoad(RegionLoadEvent e)
    {
        try
        {
            String regionKey = e.getRegionKey();
            String worldName = e.getWorld().getName();

            BlockMetaRegionData loadedRegionMeta = getRegionData(e.getTriggeringChunk());
            if (!worldRegionDataMap.containsKey(worldName))
                worldRegionDataMap.put(worldName, new HashMap<>());
            worldRegionDataMap.get(worldName).put(regionKey, loadedRegionMeta);
        }
        catch (IOException ioEx)
        {
            DecorHeadsPlugin.getInstance().getLogger().warning(String.format("Unable to load region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRegionUnload(RegionUnloadEvent e)
    {
        String regionKey = e.getRegionKey();
        String worldName = e.getWorld().getName();
        if (!worldRegionDataMap.containsKey(worldName))
            return;
        worldRegionDataMap.get(worldName).remove(regionKey);
    }
}
