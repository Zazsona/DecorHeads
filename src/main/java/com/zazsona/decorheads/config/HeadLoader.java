package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.Settings;
import com.zazsona.decorheads.exceptions.InvalidHeadException;
import com.zazsona.decorheads.exceptions.InvalidHeadSourceException;
import com.zazsona.decorheads.headdata.*;
import com.zazsona.decorheads.headsources.*;
import com.zazsona.decorheads.headsources.dropdecorators.BiomeDropHeadSourceFilter;
import com.zazsona.decorheads.headsources.dropdecorators.ToolDropHeadSourceFilter;
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

        ConfigurationSection headsYaml = YamlConfiguration.loadConfiguration(headsFile);
        headsYaml = headsYaml.getConfigurationSection(headsKey);
        Set<String> headKeys = headsYaml.getKeys(false);
        for (String headKey : headKeys)
        {
            try
            {
                ConfigurationSection headYaml = headsYaml.getConfigurationSection(headKey);
                IHead head = loadHead(headKey, headYaml);
                List<IHeadSource> headSources = loadHeadSources(plugin, head, headYaml);
                if (head != null)
                    loadedHeads.put(headKey, head);
            }
            catch (InvalidHeadException e)
            {
                Bukkit.getLogger().severe(String.format("[%s] %s", Core.PLUGIN_NAME, e.getMessage()));
            }
        }
    }

    private IHead loadHead(String key, ConfigurationSection headYaml) throws InvalidHeadException
    {
        if (headYaml.contains(nameKey) && headYaml.contains(textureKey))
        {
            String name = headYaml.getString(nameKey);
            String texture = headYaml.getString(textureKey);
            IHead head = new Head(key, name, texture);
            return head;
        }
        else
            throw new InvalidHeadException(String.format("Head %s is missing the required name &/or texture fields. It will not be loaded.", key));
    }

    private List<IHeadSource> loadHeadSources(Plugin plugin, IHead head, ConfigurationSection headYaml)
    {
        ArrayList<IHeadSource> headSources = new ArrayList<>();
        ConfigurationSection sourcesYaml = headYaml.getConfigurationSection(sourcesKey);
        for (String sourceKey : sourcesYaml.getKeys(false))
        {
            try
            {
                ConfigurationSection sourceConfiguration = sourcesYaml.getConfigurationSection(sourceKey);
                if (sourceConfiguration.contains(sourceTypeKey))
                {
                    String sourceTypeText = sourceConfiguration.getString(sourceTypeKey);
                    HeadSourceType sourceType = HeadSourceType.valueOf(sourceTypeText.toUpperCase());
                    IHeadSource headSource = buildBaseHeadSource(sourceType, head, sourceConfiguration);
                    if (headSource instanceof IDropHeadSource && Settings.isDropsEnabled())
                    {
                        IDropHeadSource dropHeadSource = (IDropHeadSource) headSource;
                        dropHeadSource = applyToolDropFilter(dropToolsKey, dropHeadSource, sourceConfiguration);
                        dropHeadSource = applyBiomeDropFilter(dropBiomesKey, dropHeadSource, sourceConfiguration);
                        plugin.getServer().getPluginManager().registerEvents(dropHeadSource, plugin);
                        headSources.add(dropHeadSource);
                    }
                    else if (headSource instanceof ICraftHeadSource && Settings.isCraftingEnabled())
                    {
                        ICraftHeadSource craftHeadSource = (ICraftHeadSource) headSource;
                        plugin.getServer().addRecipe(craftHeadSource.getRecipe());
                        headSources.add(craftHeadSource);
                    }
                }
                else
                    throw new InvalidHeadSourceException(String.format("Source \"%s\" for %s has no source type!", sourceKey, head.getKey()));
            }
            catch (Exception e)
            {
                Bukkit.getLogger().severe(String.format("[%s] %s", Core.PLUGIN_NAME, e.getMessage()));
            }
        }
        return headSources;
    }

    private IHeadSource buildBaseHeadSource(HeadSourceType sourceType, IHead head, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        switch (sourceType)
        {
            case MINE_DROP:
                List<Material> blocks = getBlocks(dropBlocksKey, head.getKey(), sourceYaml);
                return new BlockDropHeadSource(head, blocks);
            case ENTITY_KILL_DROP:
                List<EntityType> entities = getEntities(dropEntitiesKey, head.getKey(), sourceYaml);
                return new EntityDropHeadSource(head, entities);
            case SHAPELESS_CRAFT:
                return buildShapelessCraftSource(head, sourceYaml);
            case SHAPED_CRAFT:
                return buildShapedCraftSource(head, sourceYaml);
            default:
                throw new InvalidHeadSourceException(String.format("%s sources have not been implemented.", sourceType.name()));
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

    private ShapelessCraftHeadSource buildShapelessCraftSource(IHead head, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        List<Material> ingredients = getCraftIngredients(craftIngredientsKey, sourceYaml);
        if (ingredients == null || ingredients.size() == 0)
            throw new InvalidHeadSourceException(String.format("%s for %s has no ingredients specified for crafting.", sourceYaml.getCurrentPath(), head.getKey()));

        NamespacedKey nsk = new NamespacedKey(Core.getPlugin(Core.class), sourceYaml.getCurrentPath());
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(nsk, head.createItem());
        for (Material ingredient : ingredients)
        {
            shapelessRecipe.addIngredient(ingredient);
        }
        return new ShapelessCraftHeadSource(head, shapelessRecipe);
    }

    private ShapedCraftHeadSource buildShapedCraftSource(IHead head, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        List<Material> ingredients = getCraftIngredients(craftIngredientsKey, sourceYaml);
        if (sourceYaml.getKeys(false).contains(craftGridKey))
        {
            String gridOutOfBoundsMessage = String.format("Shaped recipe %s for \"%s\" exceeds a 3x3 crafting grid.", sourceYaml.getCurrentPath(), head.getKey());
            if (ingredients == null || ingredients.size() == 0)
                throw new InvalidHeadSourceException(String.format("%s for %s has no ingredients specified for crafting.", sourceYaml.getCurrentPath(), head.getKey()));

            NamespacedKey nsk = new NamespacedKey(Core.getPlugin(Core.class), sourceYaml.getCurrentPath());
            ShapedRecipe shapedRecipe = new ShapedRecipe(nsk, head.createItem());
            List<String> recipeGrid = sourceYaml.getStringList(craftGridKey);
            List<String> rows = new ArrayList<>();
            for (int i = 0; i < recipeGrid.size(); i++)
            {
                String row = recipeGrid.get(i).replace(" ", "").replace("-", " ");  //Remove formatting spaces, and convert "-" to " " for compatibility with Recipe.
                if (row.length() > 3)
                    throw new InvalidHeadSourceException(gridOutOfBoundsMessage);
                rows.add(row);
            }
            if (rows.size() > 3)
                throw new InvalidHeadSourceException(gridOutOfBoundsMessage);
            shapedRecipe.shape(rows.toArray(new String[0]));
            for (int i = 0; i < ingredients.size(); i++)
            {
                try
                {
                    shapedRecipe.setIngredient(String.valueOf(i).charAt(0), ingredients.get(i));
                }
                catch (IllegalArgumentException e)
                {
                    Bukkit.getLogger().info(String.format("[%s] Ignoring %s for crafting %s via %s as it's not on the grid...", Core.PLUGIN_NAME, ingredients.get(i).name(), head.getKey(), sourceYaml.getCurrentPath()));
                }
            }
            return new ShapedCraftHeadSource(head, shapedRecipe);
        }
        else
            throw new InvalidHeadSourceException(String.format("%s in %s is a shaped craft, but has no grid!", sourceYaml.getCurrentPath(), head.getKey()));
    }

    private IDropHeadSource applyBiomeDropFilter(String biomesKey, IDropHeadSource headSource, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        List<Biome> biomes = getBiomes(biomesKey, headSource.getHead().getKey(), sourceYaml);
        if (biomes != null)
        {
            BiomeDropHeadSourceFilter biomeDropSourceFilter = new BiomeDropHeadSourceFilter(headSource, biomes);
            return biomeDropSourceFilter;
        }
        return headSource;
    }

    private IDropHeadSource applyToolDropFilter(String toolsKey, IDropHeadSource headSource, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        List<Material> tools = getTools(toolsKey, headSource.getHead().getKey(), sourceYaml);
        if (tools != null)
        {
            ToolDropHeadSourceFilter toolDropHeadFilter = new ToolDropHeadSourceFilter(headSource, tools);
            return toolDropHeadFilter;
        }
        return headSource;
    }

    private List<Material> getBlocks(String blocksKey, String headKey, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        if (sourceYaml.getKeys(false).contains(blocksKey))
        {
            List<String> blockNames = sourceYaml.getStringList(blocksKey);
            List<Material> blocks = new ArrayList<>();
            for (String blockName : blockNames)
            {
                Material block = Material.matchMaterial(blockName);
                if (block == null || !block.isBlock())
                    throw new InvalidHeadSourceException(String.format("Unrecognised block \"%s\" for %s in %s.", blockName, headKey, blocksKey));
                else
                    blocks.add(block); //Uses a map, so should be plenty performant.
            }
            return (blocks.size() > 0) ? blocks : null;
        }
        return null;
    }

    private List<EntityType> getEntities(String entitiesKey, String headKey, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        if (sourceYaml.getKeys(false).contains(entitiesKey))
        {
            List<String> entityNames = sourceYaml.getStringList(entitiesKey);
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
                    throw new InvalidHeadSourceException(String.format("Unrecognised entity \"%s\" for %s in %s.", entityName, headKey, entitiesKey), e);
                }
            }
            return (entities.size() > 0) ? entities : null;
        }
        return null;
    }

    private List<Material> getTools(String toolsKey, String headKey, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        if (sourceYaml.getKeys(false).contains(toolsKey))
        {
            List<String> toolNames = sourceYaml.getStringList(toolsKey);
            List<Material> tools = new ArrayList<>();
            for (String toolName : toolNames)
            {
                Material tool = Material.matchMaterial(toolName);
                if (tool == null)
                    throw new InvalidHeadSourceException(String.format("Unrecognised tool \"%s\" for %s in %s.", toolName, headKey, toolsKey));
                else
                    tools.add(tool);
            }
            return (tools.size() > 0) ? tools : null;
        }
        return null;
    }

    private List<Biome> getBiomes(String biomesKey, String headKey, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        if (sourceYaml.getKeys(false).contains(biomesKey))
        {
            List<String> biomeNames = sourceYaml.getStringList(biomesKey);
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
                    throw new InvalidHeadSourceException(String.format("Unrecognised biome \"%s\" for %s in %s.", biomeName, headKey, biomesKey), e);
                }
            }
            return (biomes.size() > 0) ? biomes : null;
        }
        return null;
    }

    private List<Material> getCraftIngredients(String key, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        if (sourceYaml.getKeys(false).contains(craftIngredientsKey))
        {
            List<String> ingredientsList = (List<String>) sourceYaml.getList(craftIngredientsKey);
            List<Material> ingredients = new ArrayList<>();
            for (int i = 0; i < ingredientsList.size(); i++)
            {
                String ingredientName = ingredientsList.get(i).trim();
                Material ingredient = Material.matchMaterial(ingredientName);
                if (ingredient != null)
                    ingredients.add(ingredient);
                else
                    throw new InvalidHeadSourceException(String.format("Unrecognised crafting ingredient \"%s\" for %s.", ingredientName, key));
            }
            return ingredients;
        }
        return null;
    }
}