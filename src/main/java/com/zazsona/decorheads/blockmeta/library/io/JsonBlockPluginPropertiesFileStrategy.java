package com.zazsona.decorheads.blockmeta.library.io;

import com.google.gson.*;
import com.zazsona.decorheads.blockmeta.library.container.IBlockPluginPropertiesContainer;

import java.io.*;

public class JsonBlockPluginPropertiesFileStrategy implements IBlockPluginPropertiesFileStrategy
{
    private static final String VERSION_KEY = "version";
    private static final String CONTAINER_KEY = "blockPluginProperties";
    private static String MAX_VERSION = "1.0.0";
    @Override
    public <T extends IBlockPluginPropertiesContainer> T load(File file, Class<T> typeOfContainer) throws IOException
    {
        if (file.exists())
        {
            // Load raw file content
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null)
                jsonBuilder.append(line).append("\n");

            // Convert to JSON
            String json = jsonBuilder.toString();
            Gson gson = new Gson();
            JsonObject fileData = gson.fromJson(json, JsonObject.class);

            // Extract values
            String version = fileData.get(VERSION_KEY).getAsString();
            T blockPropertiesContainer = gson.fromJson(fileData.get(CONTAINER_KEY), typeOfContainer);
            return blockPropertiesContainer;
        }
        else
        {
            throw new FileNotFoundException();
        }
    }

    @Override
    public File save(File file, IBlockPluginPropertiesContainer blockPluginPropertiesContainer) throws IOException
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject fileData = new JsonObject();

        // Save metadata
        JsonPrimitive versionElement = new JsonPrimitive(MAX_VERSION);
        fileData.add(VERSION_KEY, versionElement);

        // Save block property data
        JsonElement blockPropertiesContainerElement = gson.toJsonTree(blockPluginPropertiesContainer);
        fileData.add(CONTAINER_KEY, blockPropertiesContainerElement);

        // Create file if not exists
        if (!file.exists())
        {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        // Write JSON to file
        FileWriter fileWriter = new FileWriter(file, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(fileData);
        printWriter.close();
        fileWriter.close();

        return file;
    }
}
