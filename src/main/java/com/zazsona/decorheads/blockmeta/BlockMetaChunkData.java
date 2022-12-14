package com.zazsona.decorheads.blockmeta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class BlockMetaChunkData
{
    private static final String LOCATION_KEY_FORMAT = "{x}.{y}.{z}";
    private static final TypeToken CHUNK_FILE_TYPE_TOKEN = new TypeToken<HashMap<String, HashMap<String, String>>>(){};

    private File chunkFile;
    private HashMap<String, HashMap<String, String>> chunkData;

    public BlockMetaChunkData(File chunkFile) throws IOException
    {
        this.chunkFile = chunkFile;
        this.chunkData = loadChunkData(chunkFile);
    }

    /**
     * Writes modifications to world data
     * @throws IOException error writing to file
     */
    public void save() throws IOException
    {
        saveChunkData(chunkFile, chunkData);
    }

    /**
     * Creates a new block meta entry
     * @param location the block location
     * @param key an identifying key for the data field
     * @param value the data value
     */
    public void addBlockMeta(Location location, String key, String value)
    {
        String blockKey = getBlockKey(location);
        HashMap<String, String> blockMeta = getBlockMeta(location);
        blockMeta.put(key, value);
        chunkData.put(blockKey, blockMeta);
    }

    /**
     * Removes a block meta entry
     * @param location the block location
     * @param key an identifying key for the data field
     */
    public void removeBlockMeta(Location location, String key)
    {
        String blockKey = getBlockKey(location);
        HashMap<String, String> blockMeta = getBlockMeta(location);
        blockMeta.remove(key);
        chunkData.put(blockKey, blockMeta);
    }

    /**
     * Gets a block meta entry, consisting of key:value pairs
     * @param location the block location
     */
    public HashMap<String, String> getBlockMeta(Location location)
    {
        String blockKey = getBlockKey(location);
        HashMap<String, String> blockMetaEntries = chunkData.get(blockKey);
        return (blockMetaEntries != null) ? blockMetaEntries : new HashMap<>();
    }

    private String getBlockKey(Location location)
    {
        String blockX = String.valueOf(location.getBlockX());
        String blockY = String.valueOf(location.getBlockY());
        String blockZ = String.valueOf(location.getBlockZ());
        return LOCATION_KEY_FORMAT.replace("{x}", blockX).replace("{y}", blockY).replace("{z}", blockZ);
    }

    private HashMap<String, HashMap<String, String>> loadChunkData(File chunkFile) throws IOException
    {
        if (chunkFile.exists())
        {
            FileReader fileReader = new FileReader(chunkFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                jsonBuilder.append(line).append("\n");
            }
            String json = jsonBuilder.toString();

            Gson gson = new Gson();
            Type type = CHUNK_FILE_TYPE_TOKEN.getType();
            HashMap<String, HashMap<String, String>> chunkEntries = gson.fromJson(json, type);
            return chunkEntries;
        }
        else
        {
            return new HashMap<>();
        }
    }

    private void saveChunkData(File chunkFile, HashMap<String, HashMap<String, String>> metadata) throws IOException
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
        Type type = CHUNK_FILE_TYPE_TOKEN.getType();
        String json = gson.toJson(metadata, type);

        if (!chunkFile.exists())
            chunkFile.createNewFile();

        FileWriter fileWriter = new FileWriter(chunkFile, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(json);
        printWriter.close();
        fileWriter.close();
    }
}
