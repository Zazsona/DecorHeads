package com.zazsona.decorheads.blockmeta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zazsona.decorheads.Core;
import org.bukkit.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class BlockMetaLogger
{
    private static final String fileName = "blockMetadata.json";
    private static BlockMetaLogger instance;
    private HashMap<BlockLocator, HashMap<String, String>> blockMeta = new HashMap<>();
    private boolean dirty = false;
    private Object lock = new Object();

    private BlockMetaLogger()
    {

    }

    public static BlockMetaLogger getInstance()
    {
        if (instance == null)
            instance = new BlockMetaLogger();
        return instance;
    }

    public void setMetadata(BlockLocator blockLocator, String metaKey, String metaData)
    {
        synchronized (lock)
        {
            if (blockMeta.get(blockLocator) == null)
                blockMeta.put(blockLocator, new HashMap<>());

            blockMeta.get(blockLocator).put(metaKey, metaData);
            dirty = true;
        }
    }

    public void setMetadata(Location location, String metaKey, String metaData)
    {
        setMetadata(new BlockLocator(location), metaKey, metaData);
    }

    public void removeMetadata(BlockLocator blockLocator, String metaKey)
    {
        synchronized (lock)
        {
            if (blockMeta.get(blockLocator) != null)
            {
                blockMeta.get(blockLocator).remove(metaKey);
                if (blockMeta.get(blockLocator).size() == 0)
                    blockMeta.remove(blockLocator);
                dirty = true;
            }
        }
    }

    public void removeMetadata(Location location, String metaKey)
    {
        removeMetadata(new BlockLocator(location), metaKey);
    }

    public String getMetadata(BlockLocator blockLocator, String metaKey)
    {
        synchronized (lock)
        {
            if (blockMeta.get(blockLocator) != null)
                return blockMeta.get(blockLocator).get(metaKey);
            else
                return null;
        }
    }

    public String getMetadata(Location location, String metaKey)
    {
        return getMetadata(new BlockLocator(location), metaKey);
    }

    public boolean isMetadataSet(BlockLocator blockLocator)
    {
        synchronized (lock)
        {
            return blockMeta.get(blockLocator) != null;
        }
    }

    public boolean isMetadataSet(Location location)
    {
        return isMetadataSet(new BlockLocator(location));
    }

    public boolean isMetadataSet(BlockLocator blockLocator, String metaKey)
    {
        synchronized (lock)
        {
            return isMetadataSet(blockLocator) && blockMeta.get(blockLocator).containsKey(metaKey);
        }
    }

    public boolean isMetadataSet(Location location, String metaKey)
    {
        return isMetadataSet(new BlockLocator(location), metaKey);
    }

    public boolean isDirty()
    {
        return dirty;
    }

    public void saveToFile() throws IOException
    {
        synchronized (lock)
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
            Type type = new TypeToken<HashMap<BlockLocator, HashMap<String, String>>>(){}.getType();
            String json = gson.toJson(blockMeta, type);

            File blockDataFile = new File(Core.getSelfPlugin().getDataFolder().getPath()+"/"+fileName);
            if (!blockDataFile.exists())
                blockDataFile.createNewFile();

            FileWriter fileWriter = new FileWriter(blockDataFile, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(json);
            printWriter.close();
            fileWriter.close();
            dirty = false;
        }
    }

    public void loadFromFile() throws IOException
    {
        synchronized (lock)
        {
            File blockDataFile = new File(Core.getSelfPlugin().getDataFolder().getPath()+"/"+fileName);
            if (blockDataFile.exists())
            {
                FileReader fileReader = new FileReader(blockDataFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    jsonBuilder.append(line).append("\n");
                }
                String json = jsonBuilder.toString();
                Gson gson = new Gson();
                Type type = new TypeToken<HashMap<BlockLocator, HashMap<String, String>>>(){}.getType();
                blockMeta = gson.fromJson(json, type);
                dirty = false;
            }
        }
    }
}
