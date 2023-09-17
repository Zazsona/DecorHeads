package com.zazsona.decorheads.blockmeta;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.blockmeta.library.io.BlockPluginPropertiesFileManager;
import com.zazsona.decorheads.blockmeta.library.node.ServerBlockPluginPropertiesNode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BlockPluginPropertiesUpdater
{
    public static void migrateBlockMetadataFileToBlockPluginProperties() throws IOException
    {
        Plugin plugin = DecorHeadsPlugin.getInstance();

        // Load file...
        File blockMetadataFile = new File(plugin.getDataFolder().getPath()+"/blockMetadata.json");
        if (!blockMetadataFile.exists())
            return;

        plugin.getLogger().info("Legacy blockMetadata.json file found. Migrating to 3.0.0+ format...");
        List<String> lines = Files.readAllLines(blockMetadataFile.toPath());
        StringBuilder jsonBuilder = new StringBuilder();
        lines.forEach(s -> jsonBuilder.append(s).append("\n"));
        String json = jsonBuilder.toString().trim();

        // Establish conversions
        HashMap<String, String> propertyKeyTranslations = new HashMap<>();
        propertyKeyTranslations.put("HeadId", HeadBlockUtil.HEAD_ID_KEY);
        propertyKeyTranslations.put("HeadPlayerId", HeadBlockUtil.PLAYER_ID_KEY);
        propertyKeyTranslations.put("HeadTexture", HeadBlockUtil.HEAD_TEXTURE_KEY);
        propertyKeyTranslations.put("BlockInventoryOwnerId", InventoryBlockUtil.INVENTORY_OWNER_PLAYER_ID_KEY);

        // Extract & translate data
        Gson gson = new Gson();
        ServerBlockPluginPropertiesNode blockProperties = new ServerBlockPluginPropertiesNode();
        JsonArray blockMetadata = gson.fromJson(json, JsonArray.class);
        List<JsonElement> blockEntries = blockMetadata.asList();
        for (JsonElement entry : blockEntries)
        {
            JsonArray blockEntry = entry.getAsJsonArray();
            JsonObject keyProperties = blockEntry.get(0).getAsJsonObject();
            JsonObject valueProperties = blockEntry.get(1).getAsJsonObject();

            String worldId = keyProperties.get("worldId").getAsString();
            int blockX = keyProperties.get("x").getAsInt();
            int blockY = keyProperties.get("y").getAsInt();
            int blockZ = keyProperties.get("z").getAsInt();

            UUID worldUuid = UUID.fromString(worldId);
            World world = Bukkit.getWorld(worldUuid);
            Location blockLocation = new Location(world, blockX, blockY, blockZ);

            HashMap<String, String> migratedBlockProperties = new HashMap<>();
            for (Map.Entry<String, String> keyTranslation : propertyKeyTranslations.entrySet())
            {
                String legacyKey = keyTranslation.getKey();
                String newKey = keyTranslation.getValue();
                if (valueProperties.has(legacyKey))
                    migratedBlockProperties.put(newKey, valueProperties.get(legacyKey).getAsString());
            }
            blockProperties.putBlockProperties(blockLocation, migratedBlockProperties);
        }

        BlockPluginPropertiesFileManager fileManager = new BlockPluginPropertiesFileManager(plugin);
        fileManager.saveServer(blockProperties);

        Path newBlockMetadataPath = Paths.get(blockMetadataFile.getParent(), "deprecated_blockMetadata.json");
        Files.move(blockMetadataFile.toPath(), newBlockMetadataPath);
        plugin.getLogger().info("blockMetadata.json migration complete. The file has been retained as \"deprecated_blockMetadata.json");
    }
}
