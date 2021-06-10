package com.zazsona.decorheads;

import com.zazsona.decorheads.headdata.BlockDropHead;
import com.zazsona.decorheads.headdata.Head;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HeadLoader
{
    private final String headsFileName = "heads.yml";

    private final String nameKey = "name";
    private final String textureKey = "texture";
    private final String dropRateKey = "default-drop-rate";
    private final String dropBlocksKey = "drop-blocks";
    private final String dropBlocksToolsKey = "drop-blocks-tools";

    private HashMap<String, IHead> loadedHeads = new HashMap<>();

    public HashMap<String, IHead> getLoadedHeads()
    {
        return loadedHeads;
    }

    public void loadHeads()
    {
        try
        {
            Plugin plugin = Core.getPlugin(Core.class);
            File headsFile = new File(plugin.getDataFolder().getPath()+"/"+ headsFileName);
            if (!headsFile.exists())
                createHeadsFile(headsFile);
            loadedHeads.clear();
            YamlConfiguration headsYaml = YamlConfiguration.loadConfiguration(headsFile);
            Set<String> headKeys = headsYaml.getKeys(false);
            for (String headKey : headKeys)
            {
                IHead head = loadHead(plugin, headKey, headsYaml.getConfigurationSection(headKey));
                if (head != null)
                    loadedHeads.put(headKey, head);
            }
        }
        catch (IOException e)
        {
            Bukkit.getLogger().severe("[DecorHeads] Unable to load heads: ");
            e.printStackTrace();
        }
    }

    private IHead loadHead(Plugin plugin, String key, ConfigurationSection headYaml)
    {
        if (headYaml.contains(nameKey) && headYaml.contains(textureKey))
        {
            String name = headYaml.getString(nameKey);
            String texture = headYaml.getString(textureKey);
            IHead head = new Head(key, name, texture);
            head = loadDrop(key, headYaml, head);
            head = loadBlockDrops(key, headYaml, head, plugin);
            return head;
        }
        else
        {
            Bukkit.getLogger().severe(String.format("[DecorHeads] Head %s is missing the required name &/or texture fields. It will not be loaded.", key));
            return null;
        }

    }

    private IHead loadDrop(String key, ConfigurationSection headYaml, IHead head)
    {
        if (headYaml.getKeys(false).contains(dropRateKey) && !Settings.isHeadDropRateInConfig(key))
        {
            Settings.setDropChance(key, headYaml.getDouble(dropRateKey));
        }
        return head;
    }

    private BlockDropHead loadBlockDrops(String key, ConfigurationSection headYaml, IHead head, Plugin plugin)
    {
        List<Material> blocks = getBlocks(dropBlocksKey, key, headYaml);
        List<Material> tools = getTools(dropBlocksToolsKey, key, headYaml);
        BlockDropHead blockDropHead = new BlockDropHead(head, blocks, tools);
        plugin.getServer().getPluginManager().registerEvents(blockDropHead, plugin);
        return blockDropHead;
    }

    private List<Material> getBlocks(String blocksKey, String headKey, ConfigurationSection headYaml)
    {
        if (headYaml.getKeys(false).contains(blocksKey))
        {
            List<String> blockNames = headYaml.getStringList(blocksKey);
            List<Material> blocks = new ArrayList<>();
            for (String blockName : blockNames)
            {
                Material block = Material.matchMaterial(blockName);
                if (block == null || !block.isBlock())
                    Bukkit.getLogger().warning(String.format("[DecorHeads] Unrecognised block \"%s\" for %s in %s.", blockName, headKey, blocksKey));
                else
                    blocks.add(block); //Uses a map, so should be plenty performant.
            }
            return (blocks.size() > 0) ? blocks : null;
        }
        return null;
    }

    private List<Material> getTools(String toolsKey, String headKey, ConfigurationSection headYaml)
    {
        if (headYaml.getKeys(false).contains(toolsKey))
        {
            List<String> toolNames = headYaml.getStringList(toolsKey);
            List<Material> tools = new ArrayList<>();
            for (String toolName : toolNames)
            {
                Material tool = Material.matchMaterial(toolName);
                if (tool == null)
                    Bukkit.getLogger().warning(String.format("[DecorHeads] Unrecognised tool \"%s\" for %s in %s.", toolName, headKey, toolsKey));
                else
                    tools.add(tool);
            }
            return (tools.size() > 0) ? tools : null;
        }
        return null;
    }

    private void createHeadsFile(File headsFile) throws IOException
    {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(headsFileName);
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        headsFile.createNewFile();
        Files.write(headsFile.toPath(), buffer, StandardOpenOption.WRITE);
    }
}
