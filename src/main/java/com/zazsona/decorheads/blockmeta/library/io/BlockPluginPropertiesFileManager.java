package com.zazsona.decorheads.blockmeta.library.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zazsona.decorheads.blockmeta.library.node.*;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

class BlockPluginPropertiesFileManager
        implements
        IServerBlockPluginPropertiesSaver, IServerBlockPluginPropertiesLoader,
        IWorldBlockPluginPropertiesSaver, IWorldBlockPluginPropertiesLoader,
        IRegionBlockPluginPropertiesSaver, IRegionBlockPluginPropertiesLoader,
        IChunkBlockPluginPropertiesSaver, IChunkBlockPluginPropertiesLoader,
        IBlockPluginPropertiesSaver, IBlockPluginPropertiesLoader
{
    public static final String VERSION_KEY = "version";
    public static final String BLOCK_PROPERTIES_KEY = "blockProperties";
    public static final String VERSION = "1.0.0";

    private Plugin plugin;
    private Gson gson;

    public BlockPluginPropertiesFileManager(Plugin plugin)
    {
        this.plugin = plugin;
        this.gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Loads all worlds from file
     * @return the server node, or null if no data has been saved
     * @throws IOException error loading worlds
     */
    @Override
    public ServerBlockPluginPropertiesNode loadServer() throws IOException
    {
        ServerBlockPluginPropertiesNode serverNode = new ServerBlockPluginPropertiesNode();
        List<World> worlds = Bukkit.getServer().getWorlds();
        for (World world : worlds)
        {
            WorldBlockPluginPropertiesNode worldNode = loadWorld(world);
            serverNode.putWorldNode(world, worldNode);
        }
        return serverNode;
    }

    /**
     * Saves all worlds to file
     * @param blockPluginProperties the server node
     * @throws IOException error writing worlds
     */
    @Override
    public void saveServer(ServerBlockPluginPropertiesNode blockPluginProperties) throws IOException
    {
        List<UUID> worldIds = blockPluginProperties.getWorldIds();
        for (UUID worldId : worldIds)
        {
            World world = Bukkit.getWorld(worldId);
            if (world == null)
                continue;

            WorldBlockPluginPropertiesNode worldNode = blockPluginProperties.getWorldNode(world);
            saveWorld(worldNode, world);
        }
    }

    /**
     * Loads the world from file
     * @param world the world
     * @return the world node, or null if none found for the world
     * @throws IOException error loading world
     */
    @Override
    public WorldBlockPluginPropertiesNode loadWorld(World world) throws IOException
    {
        File worldDirectory = getWorldDirectory(plugin, world);
        File[] regionFiles = worldDirectory.listFiles((dir, name) -> Pattern.matches("r[.]-*\\d+[.]-*\\d+[.]json", name));
        WorldBlockPluginPropertiesNode worldNode = new WorldBlockPluginPropertiesNode();
        if (regionFiles == null)
            return worldNode;

        for (File regionFile : regionFiles)
        {
            String[] fileNameComponents = regionFile.getName().split("[.]"); // "r", x, z, "json"
            int regionX = Integer.parseInt(fileNameComponents[1]);
            int regionZ = Integer.parseInt(fileNameComponents[2]);
            RegionBlockPluginPropertiesNode regionNode = loadRegionFromFile(regionFile);
            worldNode.putRegionNode(regionX, regionZ, regionNode);
        }
        return worldNode;
    }

    /**
     * Saves the world to file
     * @param blockPluginProperties the world node
     * @param world the world
     * @throws IOException error writing world
     */
    @Override
    public void saveWorld(WorldBlockPluginPropertiesNode blockPluginProperties, World world) throws IOException
    {
        for (Vector regionVector : blockPluginProperties.getRegionVectors())
        {
            RegionBlockPluginPropertiesNode regionNode = blockPluginProperties.getRegionNode(regionVector.getBlockX(), regionVector.getBlockZ());
            saveRegion(regionNode, world, regionVector.getBlockX(), regionVector.getBlockZ());
        }
    }

    /**
     * Loads the region from file
     * @param world the world of the region
     * @param regionX the x co-ordinate of the region
     * @param regionZ the z co-ordinate of the region
     * @return the region node, or null if none found for the region
     * @throws IOException error loading region
     */
    @Override
    public RegionBlockPluginPropertiesNode loadRegion(World world, int regionX, int regionZ) throws IOException
    {
        File regionFile = getRegionFile(plugin, world, regionX, regionZ);
        return loadRegionFromFile(regionFile);
    }

    /**
     * Saves the region to file.
     * @param blockPluginProperties the region node
     * @param world the world of the region
     * @param regionX the x co-ordinate of the region
     * @param regionZ the z co-ordinate of the region
     * @throws IOException error writing region
     */
    @Override
    public void saveRegion(RegionBlockPluginPropertiesNode blockPluginProperties, World world, int regionX, int regionZ) throws IOException
    {
        File regionFile = getRegionFile(plugin, world, regionX, regionZ);
        saveRegionToFile(blockPluginProperties, regionFile);
    }

    /**
     * Loads the chunk's properties from file.
     * If loading multiple chunks in the same region, prefer loadRegion for performance.
     * @param world the world of the chunk
     * @param chunkX the x co-ordinate of the chunk
     * @param chunkZ the z co-ordinate of the chunk
     * @return the chunk node, or null if none found for the chunk
     * @throws IOException error loading chunk
     */
    @Override
    public ChunkBlockPluginPropertiesNode loadChunk(World world, int chunkX, int chunkZ) throws IOException
    {
        // Get Region Co-ordinates
        int regionX = (int) Math.floor(chunkX / 32);
        int regionZ = (int) Math.floor(chunkZ / 32);

        RegionBlockPluginPropertiesNode regionNode = loadRegion(world, regionX, regionZ);
        return regionNode.getChunkNode(chunkX, chunkZ);
    }

    /**
     * Writes the chunk's properties to file.
     * If writing multiple chunk in the same region, prefer loadRegion then saveRegion for performance.
     * @param blockPluginProperties the chunk node
     * @param world the world of the chunk
     * @param chunkX the x co-ordinate of the chunk
     * @param chunkZ the z co-ordinate of the chunk
     * @throws IOException error writing chunk
     */
    @Override
    public void saveChunk(ChunkBlockPluginPropertiesNode blockPluginProperties, World world, int chunkX, int chunkZ) throws IOException
    {
        // Get Region Co-ordinates
        int regionX = (int) Math.floor(chunkX / 32);
        int regionZ = (int) Math.floor(chunkZ / 32);

        RegionBlockPluginPropertiesNode regionNode = loadRegion(world, regionX, regionZ);
        regionNode.putChunkNode(chunkX, chunkZ, blockPluginProperties);
        saveRegion(regionNode, world, regionX, regionZ);
    }

    /**
     * Loads the block's properties from file.
     * If loading multiple blocks in the same region, prefer loadRegion for performance.
     * @param world the world of the block
     * @param blockX the x co-ordinate of the block
     * @param blockY the y co-ordinate of the block
     * @param blockZ the z co-ordinate of the block
     * @return the block node, or null if none found for the block
     * @throws IOException error loading block
     */
    @Override
    public BlockPluginPropertiesNode loadBlock(World world, int blockX, int blockY, int blockZ) throws IOException
    {
        // Get Chunk Co-ordinates
        int chunkX = (int) Math.floor(blockX / 16);
        int chunkZ = (int) Math.floor(blockZ / 16);

        ChunkBlockPluginPropertiesNode chunkNode = loadChunk(world, chunkX, chunkZ);
        return chunkNode.getBlockNode(blockX, blockY, blockZ);
    }

    /**
     * Writes the block's properties to file.
     * If writing multiple blocks in the same region, prefer loadRegion then saveRegion for performance.
     * @param blockPluginProperties the block to write
     * @param world the world of the block
     * @param blockX the x co-ordinate of the block
     * @param blockY the y co-ordinate of the block
     * @param blockZ the z co-ordinate of the block
     * @throws IOException error saving block
     */
    @Override
    public void saveBlock(BlockPluginPropertiesNode blockPluginProperties, World world, int blockX, int blockY, int blockZ) throws IOException
    {
        // Get Chunk Co-ordinates
        int chunkX = (int) Math.floor(blockX / 16);
        int chunkZ = (int) Math.floor(blockZ / 16);

        ChunkBlockPluginPropertiesNode chunkNode = loadChunk(world, chunkX, chunkZ);
        chunkNode.putBlockNode(blockX, blockY, blockZ, blockPluginProperties);
        saveChunk(chunkNode, world, chunkX, chunkZ);
    }

    @Nullable
    private RegionBlockPluginPropertiesNode loadRegionFromFile(File regionFile) throws IOException
    {
        if (!regionFile.exists())
            return null;

        String jsonText = new String(Files.readAllBytes(regionFile.toPath()));
        JsonObject json = gson.fromJson(jsonText, JsonObject.class);
        JsonObject blockPropertiesJson = json.get(BLOCK_PROPERTIES_KEY).getAsJsonObject();
        RegionBlockPluginPropertiesNode regionNodeFromFile = gson.fromJson(blockPropertiesJson, RegionBlockPluginPropertiesNode.class);
        return regionNodeFromFile;
    }

    private void saveRegionToFile(IChunkBlockPluginPropertiesNodeParent blockPluginProperties, File regionFile) throws IOException
    {
        JsonObject json = new JsonObject();
        json.addProperty(VERSION_KEY, VERSION);
        JsonElement regionPropertiesElement = gson.toJsonTree(blockPluginProperties);
        json.add(BLOCK_PROPERTIES_KEY, regionPropertiesElement);
        String jsonText = gson.toJson(json);

        // Write to file
        BufferedWriter writer = Files.newBufferedWriter(regionFile.toPath(), StandardOpenOption.WRITE);
        writer.write(jsonText);
        writer.flush();
        writer.close();
    }

    private File getWorldDirectory(Plugin plugin, World world)
    {
        if (plugin == null)
            throw new NullArgumentException("plugin");
        if (world == null)
            throw new NullArgumentException("world");

        String worldFolderPath = world.getWorldFolder().getAbsolutePath();
        String pluginFolderName = plugin.getName();

        File worldDirectory = Paths.get(worldFolderPath, pluginFolderName).toFile();
        return worldDirectory;
    }

    private File getRegionFile(Plugin plugin, World world, int regionX, int regionZ)
    {
        String worldDirectory = getWorldDirectory(plugin, world).getAbsolutePath();
        String regionFileName = String.format("r.%d.%d.json", regionX, regionZ);

        File regionFile = Paths.get(worldDirectory, regionFileName).toFile();
        return regionFile;
    }

    private File getRegionFile(Plugin plugin, Location location)
    {
        if (plugin == null)
            throw new NullArgumentException("plugin");
        if (location == null)
            throw new NullArgumentException("location");
        if (location.getWorld() == null)
            throw new IllegalArgumentException("The Location's world cannot be null.");

        int regionXVal = (int) Math.floor(location.getChunk().getX() / 32.0);
        int regionZVal = (int) Math.floor(location.getChunk().getZ() / 32.0);

        return getRegionFile(plugin, location.getWorld(), regionXVal, regionZVal);
    }
}