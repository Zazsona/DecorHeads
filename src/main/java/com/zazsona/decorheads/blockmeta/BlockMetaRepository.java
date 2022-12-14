package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.Core;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.io.*;
import java.util.HashMap;

public class BlockMetaRepository implements Listener
{
    public static final String CHUNK_FILE_NAME_FORMAT = "r.{x}.{z}.json";
    private static final String CHUNK_KEY_FORMAT = "{x}.{z}";
    private static BlockMetaRepository instance;

    private HashMap<String, BlockMetaChunkData> chunkCache = new HashMap<>();

    public static BlockMetaRepository getInstance()
    {
        if (instance == null)
            instance = new BlockMetaRepository();
        return instance;
    }

    /**
     * Gets the meta entries for a chunk.
     * @param chunk the chunk, must include world, x, and z.
     */
    public BlockMetaChunkData getChunk(Chunk chunk) throws IOException
    {
        if (chunk.getWorld() == null)
            throw new NullPointerException("Chunk must have a world defined.");

        String chunkCacheKey = getChunkCacheKey(chunk);
        if (chunkCache.containsKey(chunkCacheKey))
            return chunkCache.get(chunkCacheKey);

        File chunkFile = getChunkFile(chunk);
        BlockMetaChunkData chunkData = new BlockMetaChunkData(chunkFile);
        return chunkData;
    }

    /**
     * Loads the meta file for the provided chunk
     * @param chunk the chunk to load the meta for
     * @return the file
     */
    private File getChunkFile(Chunk chunk)
    {
        String worldName = chunk.getWorld().getName();
        String chunkX = String.valueOf(chunk.getX());
        String chunkZ = String.valueOf(chunk.getZ());

        String fileName = CHUNK_FILE_NAME_FORMAT.replace("{x}", chunkX).replace("{z}", chunkZ);
        String worldsPath = Bukkit.getServer().getWorldContainer().getAbsolutePath();
        String worldPath = worldsPath + "/" + worldName;
        String worldPluginPath = worldPath + "/" + Core.PLUGIN_NAME.toLowerCase();
        String filePath = worldPluginPath + "/" + fileName;
        return new File(filePath);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent e)
    {
        try
        {
            Chunk chunk = e.getChunk();
            String key = getChunkCacheKey(chunk);
            if (!chunkCache.containsKey(key))
            {
                BlockMetaChunkData loadedChunkMeta = getChunk(chunk);
                chunkCache.put(key, loadedChunkMeta);
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load chunk meta: %s", Core.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent e)
    {
        Chunk chunk = e.getChunk();
        String key = getChunkCacheKey(chunk);
        if (chunkCache.containsKey(key))
            chunkCache.remove(chunk);
    }

    /**
     * Converts the chunk into a key for use within the map
     * @param chunk the chunk to get the key for
     * @return the key
     */
    private String getChunkCacheKey(Chunk chunk)
    {
        String chunkX = String.valueOf(chunk.getX());
        String chunkZ = String.valueOf(chunk.getZ());
        return CHUNK_KEY_FORMAT.replace("{x}", chunkX).replace("{z}", chunkZ);
    }
}
