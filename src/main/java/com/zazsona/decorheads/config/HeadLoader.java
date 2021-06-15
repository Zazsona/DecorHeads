package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.Settings;
import com.zazsona.decorheads.exceptions.InvalidHeadException;
import com.zazsona.decorheads.headdata.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class HeadLoader extends HeadConfigAccessor
{
    private HashMap<String, IHead> loadedHeads = new HashMap<>();

    public HashMap<String, IHead> getLoadedHeads()
    {
        return loadedHeads;
    }

    public void loadHeads()
    {
        Plugin plugin = Core.getPlugin(Core.class);
        loadedHeads.clear();
        File headsFile = getHeadsFile();
        YamlConfiguration headsYaml = YamlConfiguration.loadConfiguration(headsFile);
        Set<String> headKeys = headsYaml.getKeys(false);
        for (String headKey : headKeys)
        {
            try
            {
                IHead head = loadHead(plugin, headKey, headsYaml.getConfigurationSection(headKey));
                if (head != null)
                    loadedHeads.put(headKey, head);
            }
            catch (InvalidHeadException e)
            {
                Bukkit.getLogger().warning(String.format("[%s] %s", Core.PLUGIN_NAME, e.getMessage()));
            }

        }
    }

    private IHead loadHead(Plugin plugin, String key, ConfigurationSection headYaml) throws InvalidHeadException
    {
        if (headYaml.contains(nameKey) && headYaml.contains(textureKey))
        {
            String name = headYaml.getString(nameKey);
            String texture = headYaml.getString(textureKey);
            IHead head = new Head(key, name, texture);
            head = loadDrop(key, headYaml, head);
            head = loadBlockDrops(key, headYaml, head, plugin);
            head = loadEntityDrops(key, headYaml, head, plugin);
            head = loadCraftHead(key, headYaml, head, plugin);
            return head;
        }
        else
            throw new InvalidHeadException(String.format("Head %s is missing the required name &/or texture fields. It will not be loaded.", key));

    }

    private IHead loadDrop(String key, ConfigurationSection headYaml, IHead head)
    {
        if (headYaml.getKeys(false).contains(dropRateKey) && !Settings.isHeadDropRateInConfig(key))
        {
            Settings.setDropChance(key, headYaml.getDouble(dropRateKey));
        }
        return head;
    }

    private IHead loadBlockDrops(String key, ConfigurationSection headYaml, IHead head, Plugin plugin) throws InvalidHeadException
    {
        List<Material> blocks = getBlocks(dropBlocksKey, key, headYaml);
        List<Material> tools = getTools(dropBlocksToolsKey, key, headYaml);
        IDropHead blockDropHead = new BlockDropHead(head, blocks, tools);

        blockDropHead = loadBiomeDrops(dropBlocksBiomesKey, key, headYaml, blockDropHead, plugin);

        plugin.getServer().getPluginManager().registerEvents(blockDropHead, plugin);
        return blockDropHead;
    }

    private IDropHead loadBiomeDrops(String biomesKey, String key, ConfigurationSection headYaml, IDropHead head, Plugin plugin) throws InvalidHeadException
    {
        List<Biome> biomes = getBiomes(biomesKey, key, headYaml);
        if (biomes != null)
        {
            BiomeDropHead biomeDropHead = new BiomeDropHead(head, biomes);
            return biomeDropHead;
        }
        return head;
    }

    private IHead loadEntityDrops(String key, ConfigurationSection headYaml, IHead head, Plugin plugin) throws InvalidHeadException
    {
        List<EntityType> entities = getEntities(dropEntitiesKey, key, headYaml);
        List<Material> tools = getTools(dropEntitiesToolsKey, key, headYaml);
        IDropHead entityDropHead = new EntityDropHead(head, entities, tools);

        entityDropHead = loadBiomeDrops(dropEntitiesBiomesKey, key, headYaml, entityDropHead, plugin);

        plugin.getServer().getPluginManager().registerEvents(entityDropHead, plugin);
        return entityDropHead;
    }

    private IHead loadCraftHead(String key, ConfigurationSection headYaml, IHead head, Plugin plugin) throws InvalidHeadException
    {
        List<Material> craftingIngredients = getCraftIngredients(key, headYaml);
        if (craftingIngredients != null)
        {
            if (headYaml.getKeys(false).contains(craftGridKey))
                return loadShapedCraftHead(key, headYaml, head, plugin, craftingIngredients);
            else
                return loadShapelessCraftHead(key, headYaml, head, plugin, craftingIngredients);
        }
        return head;
    }

    private IHead loadShapelessCraftHead(String key, ConfigurationSection headYaml, IHead head, Plugin plugin, List<Material> ingredients) throws InvalidHeadException
    {
        if (ingredients == null || ingredients.size() == 0)
            throw new InvalidHeadException(String.format("%s has no ingredients specified for crafting.", key));

        NamespacedKey nsk = new NamespacedKey(Core.getPlugin(Core.class), key);
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(nsk, head.createItem());
        for (Material ingredient : ingredients)
        {
            shapelessRecipe.addIngredient(ingredient);
        }
        plugin.getServer().addRecipe(shapelessRecipe);
        ShapelessCraftHead shapelessCraftHead = new ShapelessCraftHead(head, shapelessRecipe);
        return shapelessCraftHead;
    }

    private IHead loadShapedCraftHead(String key, ConfigurationSection headYaml, IHead head, Plugin plugin, List<Material> ingredients) throws InvalidHeadException
    {
        if (headYaml.getKeys(false).contains(craftGridKey))
        {
            if (ingredients == null || ingredients.size() == 0)
                throw new InvalidHeadException(String.format("%s has no ingredients specified for crafting.", key));

            NamespacedKey nsk = new NamespacedKey(Core.getPlugin(Core.class), key);
            ShapedRecipe shapedRecipe = new ShapedRecipe(nsk, head.createItem());
            List<List<String>> recipeGrid = (List<List<String>>) headYaml.getList(craftGridKey);
            StringBuilder rowBuilder;
            List<String> rows = new ArrayList<>();
            for (int y = 0; y < recipeGrid.size(); y++)
            {
                rowBuilder = new StringBuilder();
                for (int x = 0; x < recipeGrid.get(y).size(); x++)
                {
                    if (y >= 3 || x >= 3)
                        throw new InvalidHeadException(String.format("Shaped recipe for \"%s\" exceeds a 3x3 crafting grid.", key));
                    else
                    {
                        String gridValue = String.valueOf(recipeGrid.get(y).get(x));
                        if (gridValue.equals("-"))
                            gridValue = " "; //YAML won't allow unquoted spaces in array
                        rowBuilder.append(gridValue);
                    }
                }
                rows.add(rowBuilder.toString());
            }
            shapedRecipe.shape(rows.toArray(new String[0]));
            for (int i = 0; i < ingredients.size(); i++)
            {
                shapedRecipe.setIngredient(String.valueOf(i).charAt(0), ingredients.get(i));
            }
            plugin.getServer().addRecipe(shapedRecipe);
            ShapedCraftHead shapedCraftHead = new ShapedCraftHead(head, shapedRecipe);
            return shapedCraftHead;
        }
        else
            return head;
    }

    private List<Material> getBlocks(String blocksKey, String headKey, ConfigurationSection headYaml) throws InvalidHeadException
    {
        if (headYaml.getKeys(false).contains(blocksKey))
        {
            List<String> blockNames = headYaml.getStringList(blocksKey);
            List<Material> blocks = new ArrayList<>();
            for (String blockName : blockNames)
            {
                Material block = Material.matchMaterial(blockName);
                if (block == null || !block.isBlock())
                    throw new InvalidHeadException(String.format("Unrecognised block \"%s\" for %s in %s.", blockName, headKey, blocksKey));
                else
                    blocks.add(block); //Uses a map, so should be plenty performant.
            }
            return (blocks.size() > 0) ? blocks : null;
        }
        return null;
    }

    private List<EntityType> getEntities(String entitiesKey, String headKey, ConfigurationSection headYaml) throws InvalidHeadException
    {
        if (headYaml.getKeys(false).contains(entitiesKey))
        {
            List<String> entityNames = headYaml.getStringList(entitiesKey);
            List<EntityType> entities = new ArrayList<>();
            for (String entityName : entityNames)
            {
                try
                {
                    EntityType entity = EntityType.valueOf(entityName.toUpperCase().trim());
                    entities.add(entity);
                }
                catch (IllegalArgumentException e)
                {
                    throw new InvalidHeadException(String.format("Unrecognised entity \"%s\" for %s in %s.", entityName, headKey, entitiesKey), e);
                }
            }
            return (entities.size() > 0) ? entities : null;
        }
        return null;
    }

    private List<Material> getTools(String toolsKey, String headKey, ConfigurationSection headYaml) throws InvalidHeadException
    {
        if (headYaml.getKeys(false).contains(toolsKey))
        {
            List<String> toolNames = headYaml.getStringList(toolsKey);
            List<Material> tools = new ArrayList<>();
            for (String toolName : toolNames)
            {
                Material tool = Material.matchMaterial(toolName);
                if (tool == null)
                    throw new InvalidHeadException(String.format("Unrecognised tool \"%s\" for %s in %s.", toolName, headKey, toolsKey));
                else
                    tools.add(tool);
            }
            return (tools.size() > 0) ? tools : null;
        }
        return null;
    }

    private List<Biome> getBiomes(String biomesKey, String headKey, ConfigurationSection headYaml) throws InvalidHeadException
    {
        if (headYaml.getKeys(false).contains(biomesKey))
        {
            List<String> biomeNames = headYaml.getStringList(biomesKey);
            List<Biome> biomes = new ArrayList<>();
            for (String biomeName : biomeNames)
            {
                try
                {
                    Biome biome = Biome.valueOf(biomeName.toUpperCase().trim());
                    biomes.add(biome);
                }
                catch (IllegalArgumentException e)
                {
                    throw new InvalidHeadException(String.format("Unrecognised biome \"%s\" for %s in %s.", biomeName, headKey, biomesKey), e);
                }
            }
            return (biomes.size() > 0) ? biomes : null;
        }
        return null;
    }

    private List<Material> getCraftIngredients(String key, ConfigurationSection headYaml) throws InvalidHeadException
    {
        if (headYaml.getKeys(false).contains(craftIngredientsKey))
        {
            List<String> ingredientsList = (List<String>) headYaml.getList(craftIngredientsKey);
            List<Material> ingredients = new ArrayList<>();
            for (int i = 0; i < ingredientsList.size(); i++)
            {
                String ingredientName = ingredientsList.get(i).trim();
                Material ingredient = Material.matchMaterial(ingredientName);
                if (ingredient != null)
                    ingredients.add(ingredient);
                else
                    throw new InvalidHeadException(String.format("Unrecognised crafting ingredient \"%s\" for %s.", ingredientName, key));
            }
            return ingredients;
        }
        return null;
    }
}
