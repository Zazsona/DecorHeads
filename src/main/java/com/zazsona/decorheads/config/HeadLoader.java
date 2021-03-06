package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.exceptions.InvalidHeadException;
import com.zazsona.decorheads.exceptions.InvalidHeadSourceException;
import com.zazsona.decorheads.headdata.*;
import com.zazsona.decorheads.headsources.*;
import com.zazsona.decorheads.headsources.dropfilters.*;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class HeadLoader extends HeadConfigAccessor
{
    private static HeadLoader instance;
    private Plugin plugin = Core.getSelfPlugin();
    private HashMap<String, IHead> loadedHeads = new HashMap<>();
    private HashMap<String, HashMap<String, HeadSource>> loadedHeadSources = new HashMap<>();

    public static HeadLoader getInstance()
    {
        if (instance == null)
            instance = new HeadLoader();
        return instance;
    }

    private HeadLoader()
    {
        //private constructor
    }

    public HashMap<String, IHead> getLoadedHeads()
    {
        return loadedHeads;
    }

    public HashMap<String, HashMap<String, HeadSource>> getLoadedHeadSources()
    {
        return loadedHeadSources;
    }


    public void loadHeads()
    {
        File headsFile = getHeadsFile();
        ConfigurationSection headsFileYaml = YamlConfiguration.loadConfiguration(headsFile);
        ConfigurationSection headsYaml = headsFileYaml.getConfigurationSection(headsKey);
        loadHeads(headsYaml);
    }

    public void loadHeads(ConfigurationSection headsYaml)
    {
        Set<String> headKeys = headsYaml.getKeys(false);
        for (String headKey : headKeys)
        {
            try
            {
                ConfigurationSection headYaml = headsYaml.getConfigurationSection(headKey);
                loadHead(headYaml);
            }
            catch (InvalidHeadException e)
            {
                Bukkit.getLogger().severe(String.format("[%s] %s", Core.PLUGIN_NAME, e.getMessage()));
            }
        }
    }

    public IHead loadHead(String key) throws InvalidHeadException
    {
        File headsFile = getHeadsFile();
        ConfigurationSection headsFileYaml = YamlConfiguration.loadConfiguration(headsFile);
        ConfigurationSection headsYaml = headsFileYaml.getConfigurationSection(headsKey);
        ConfigurationSection headYaml = headsYaml.getConfigurationSection(key);
        return loadHead(headYaml);
    }

    public IHead loadHead(ConfigurationSection headYaml) throws InvalidHeadException
    {
        IHead head = parseHead(headYaml);
        if (loadedHeads.containsKey(head.getKey()))
            throw new InvalidHeadException(String.format("A head already exists with key \"%s\"!", head.getKey()));
        else
            loadedHeads.put(head.getKey(), head);

        if (headYaml.contains(sourcesKey))
        {
            ConfigurationSection sourcesYaml = headYaml.getConfigurationSection(sourcesKey);
            for (String sourceKey : sourcesYaml.getKeys(false))
            {
                try
                {
                    ConfigurationSection sourceYaml = sourcesYaml.getConfigurationSection(sourceKey);
                    loadHeadSource(head, sourceYaml);
                }
                catch (Exception e)
                {
                    Bukkit.getLogger().severe(String.format("[%s] %s", Core.PLUGIN_NAME, e.getMessage()));
                }
            }
        }
        return head;
    }

    public HeadSource loadHeadSource(IHead head, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        if (!loadedHeads.containsKey(head.getKey()))
            throw new InvalidHeadSourceException(String.format("Attempted to load head source prior to loading head: %s", head.getKey()));

        HeadSource headSource = parseHeadSource(head, sourceYaml);
        if (headSource instanceof DropHeadSource)
        {
            DropHeadSource dropHeadSource = (DropHeadSource) headSource;
            plugin.getServer().getPluginManager().registerEvents(dropHeadSource, plugin);
        }
        else if (headSource instanceof CraftHeadSource)
        {
            CraftHeadSource craftHeadSource = (CraftHeadSource) headSource;
            plugin.getServer().addRecipe(craftHeadSource.getRecipe());
        }
        if (!loadedHeadSources.containsKey(head.getKey()))
            loadedHeadSources.put(head.getKey(), new HashMap<>());
        loadedHeadSources.get(head.getKey()).put(sourceYaml.getName(), headSource);
        return headSource;
    }

    public void unloadHeads()
    {
        HashSet<String> headKeys = new HashSet<>(loadedHeads.keySet());
        for (String headKey : headKeys)
            unloadHead(headKey);
    }

    public boolean unloadHead(String key)
    {
        IHead head = loadedHeads.get(key);
        if (head != null)
        {
            HashMap<String, HeadSource> headSources = loadedHeadSources.get(key);
            if (headSources != null)
            {
                HashSet<String> sourceKeys = new HashSet<>(headSources.keySet());
                for (String sourceKey : sourceKeys)
                    unloadHeadSource(key, sourceKey);
            }
            loadedHeadSources.remove(key);
            loadedHeads.remove(key);
            return true;
        }
        return false;
    }

    public void unloadHeadSource(String headKey, String sourceKey)
    {
        if (loadedHeadSources.get(headKey) != null && loadedHeadSources.get(headKey).get(sourceKey) != null)
        {
            Map<String, HeadSource> headSourceMap = loadedHeadSources.get(headKey);
            HeadSource headSource = headSourceMap.get(sourceKey);

            if (headSource instanceof Listener)
                HandlerList.unregisterAll((Listener) headSource);
            if (headSource instanceof CraftHeadSource)
            {
                Recipe recipe = ((CraftHeadSource) headSource).getRecipe();
                if (recipe instanceof Keyed)
                {
                    NamespacedKey nsk = ((Keyed) recipe).getKey();
                    plugin.getServer().removeRecipe(nsk);
                }
            }
            loadedHeadSources.get(headKey).remove(sourceKey);
        }
    }

    /**
     * Safely reloads all heads using the user's heads config. Unloading will not occur until ALL heads have been validated.
     * @throws InvalidHeadException head config is invalid
     * @throws InvalidHeadSourceException head source config is invalid
     */
    public void reloadHeads() throws InvalidHeadException, InvalidHeadSourceException
    {
        File headsFile = getHeadsFile();
        ConfigurationSection headsFileYaml = YamlConfiguration.loadConfiguration(headsFile);
        ConfigurationSection headsYaml = headsFileYaml.getConfigurationSection(headsKey);
        reloadHeads(headsYaml);
    }

    /**
     * Safely reloads all heads. Unloading will not occur until ALL heads have been validated.
     * @param headsYaml the new heads yaml to load
     * @throws InvalidHeadException head config is invalid
     * @throws InvalidHeadSourceException head source config is invalid
     */
    public void reloadHeads(ConfigurationSection headsYaml) throws InvalidHeadException, InvalidHeadSourceException
    {
        Set<String> oldHeadKeys = new HashSet<>(loadedHeads.keySet());
        Set<String> newHeadKeys = headsYaml.getKeys(false);
        for (String headKey : newHeadKeys)
            validateHeadConfig(headsYaml.getConfigurationSection(headKey));
        for (String headKey : oldHeadKeys)
            unloadHead(headKey);
        for (String headKey : newHeadKeys)
            loadHead(headsYaml.getConfigurationSection(headKey));
    }

    /**
     * Safely reloads the head. Unloading will not occur until the headYaml in the user's heads config has been validated to ensure it can be successfully loaded.<br>
     * This ensures any progression data, such as that associated with crafting, is not lost.
     * @param key
     * @throws InvalidHeadException head config is invalid
     * @throws InvalidHeadSourceException head source config is invalid
     */
    public void reloadHead(String key) throws InvalidHeadException, InvalidHeadSourceException
    {
        File headsFile = getHeadsFile();
        ConfigurationSection headsFileYaml = YamlConfiguration.loadConfiguration(headsFile);
        ConfigurationSection headsYaml = headsFileYaml.getConfigurationSection(headsKey);
        ConfigurationSection headYaml = headsYaml.getConfigurationSection(key);
        reloadHead(key, headYaml);
    }

    /**
     * Safely reloads the head. Unloading will not occur until the headYaml has been validated to ensure it can be successfully loaded.<br>
     * This ensures any progression data, such as that associated with crafting, is not lost.
     * @param existingKey the key to unload
     * @param headYaml the data to load
     * @throws InvalidHeadException head config is invalid
     * @throws InvalidHeadSourceException head source config is invalid
     */
    public void reloadHead(String existingKey, ConfigurationSection headYaml) throws InvalidHeadException, InvalidHeadSourceException
    {
        validateHeadConfig(headYaml);
        unloadHead(existingKey);
        loadHead(headYaml);
    }

    /**
     * Parses the provided yaml to build a representation of the head without registering it to the game logic
     * @param headYaml the head to validate
     * @throws InvalidHeadException invalid head
     * @throws InvalidHeadSourceException invalid head source
     */
    public void validateHeadConfig(ConfigurationSection headYaml) throws InvalidHeadException, InvalidHeadSourceException
    {
        if (headYaml == null)
            throw new InvalidHeadException("Head Yaml is null.");
        Bukkit.getLogger().info(String.format("[%s] Validating head: %s...", Core.PLUGIN_NAME, headYaml.getName()));
        IHead head = parseHead(headYaml);
        ConfigurationSection sourcesYaml = headYaml.getConfigurationSection(sourcesKey);
        for (String sourceKey : sourcesYaml.getKeys(false))
            parseHeadSource(head, sourcesYaml.getConfigurationSection(sourceKey));
    }

    private IHead parseHead(ConfigurationSection headYaml) throws InvalidHeadException
    {
        if (headYaml.getName().equals(playerHeadKey))
        {
            IHead head = new PlayerHead(headYaml.getName());
            return head;
        }
        else if (headYaml.contains(nameKey) && headYaml.contains(textureKey))
        {
            String name = headYaml.getString(nameKey);
            String texture = headYaml.getString(textureKey);
            IHead head = new TextureHead(headYaml.getName(), name, texture);
            return head;
        }
        else
            throw new InvalidHeadException(String.format("Head %s is missing the required name &/or texture fields. It will not be loaded.", headYaml.getName()));
    }

    private HeadSource parseHeadSource(IHead head, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        if (sourceYaml.contains(sourceTypeKey))
        {
            String sourceTypeText = sourceYaml.getString(sourceTypeKey);
            HeadSourceType sourceType = HeadSourceType.valueOf(sourceTypeText.toUpperCase());
            HeadSource headSource = buildBaseHeadSource(sourceType, head, sourceYaml);
            if (headSource instanceof DropHeadSource)
            {
                DropHeadSource dropHeadSource = (DropHeadSource) headSource;
                applyBlockDropFilter(dropBlocksKey, dropHeadSource, sourceYaml);
                applyEntityDropFilter(dropEntitiesKey, dropHeadSource, sourceYaml);
                applyPlayerDeathIdDropFilter(dropKilledPlayerIdsKey, dropHeadSource, sourceYaml);
                applyToolDropFilter(dropToolsKey, dropHeadSource, sourceYaml);
                applyBiomeDropFilter(dropBiomesKey, dropHeadSource, sourceYaml);
                applyRecipeResultDropFilter(dropRecipeResultsKey, dropHeadSource, sourceYaml);
                applyEventInvokerFilter(dropEventInvoker, dropHeadSource, sourceYaml);
                return dropHeadSource;
            }
            else if (headSource instanceof CraftHeadSource)
            {
                CraftHeadSource craftHeadSource = (CraftHeadSource) headSource;
                return craftHeadSource;
            }
            else
                return headSource;
        }
        else
            throw new InvalidHeadSourceException(String.format("Source \"%s\" for %s has no source type!", sourceYaml.getCurrentPath(), head.getKey()));
    }

    private HeadSource buildBaseHeadSource(HeadSourceType sourceType, IHead head, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        if (head instanceof PlayerHead && sourceType != HeadSourceType.PLAYER_DEATH_DROP)
            throw new InvalidHeadSourceException(String.format("Player heads only support %s source types. %s is %s.", HeadSourceType.PLAYER_DEATH_DROP.name(), sourceYaml.getName(), sourceType.name()));
        double dropRate = getDropRate(dropRateKey, sourceYaml);
        switch (sourceType)
        {
            case MINE_DROP:
                return new BlockDropHeadSource(head, dropRate);
            case BREW_DROP:
                return new BrewDropHeadSource(head, dropRate);
            case CRAFT_DROP:
                return new CraftDropHeadSource(head, dropRate);
            case SMELT_DROP:
                return new SmeltDropHeadSource(head, dropRate);
            case ENTITY_DEATH_DROP:
                return new EntityDropHeadSource(head, dropRate);
            case PLAYER_DEATH_DROP:
                return new PlayerEntityDropHeadSource(head, dropRate);
            case SHAPELESS_CRAFT:
                return buildShapelessCraftSource(head, sourceYaml);
            case SHAPED_CRAFT:
                return buildShapedCraftSource(head, sourceYaml);
            default:
                throw new InvalidHeadSourceException(String.format("%s sources have not been implemented.", sourceType.name()));
        }
    }

    private ShapelessCraftHeadSource buildShapelessCraftSource(IHead head, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        List<Material> ingredients = getCraftIngredients(craftIngredientsKey, sourceYaml);
        if (ingredients == null || ingredients.size() == 0)
            throw new InvalidHeadSourceException(String.format("%s for %s has no ingredients specified for crafting.", sourceYaml.getCurrentPath(), head.getKey()));

        NamespacedKey nsk = new NamespacedKey(plugin, head.getKey()+"/"+sourceYaml.getName());
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(nsk, head.createItem());
        shapelessRecipe.setGroup(head.getKey());
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

            NamespacedKey nsk = new NamespacedKey(plugin, head.getKey()+"/"+sourceYaml.getName());
            ShapedRecipe shapedRecipe = new ShapedRecipe(nsk, head.createItem());
            shapedRecipe.setGroup(head.getKey());
            List<String> recipeGrid = sourceYaml.getStringList(craftGridKey);
            List<String> rows = new ArrayList<>();
            for (int i = 0; i < recipeGrid.size(); i++)
            {
                String row = recipeGrid.get(i).replace(" ", "").replace("/", " ");  //Remove formatting spaces, and convert "/" to " " for compatibility with Recipe.
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

    private boolean applyBlockDropFilter(String blocksKey, DropHeadSource headSource, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        List<Material> blocks = getBlocks(blocksKey, headSource.getHead().getKey(), sourceYaml);
        if (blocks != null)
        {
            BlockDropFilter blockDropFilter = new BlockDropFilter(blocks);
            headSource.getDropFilters().add(blockDropFilter);
            return true;
        }
        return false;
    }

    private boolean applyEntityDropFilter(String entitiesKey, DropHeadSource headSource, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        List<EntityType> entities = getEntities(entitiesKey, headSource.getHead().getKey(), sourceYaml);
        if (entities != null)
        {
            EntityDropFilter entityDropFilter = new EntityDropFilter(entities);
            headSource.getDropFilters().add(entityDropFilter);
            return true;
        }
        return false;
    }

    private boolean applyPlayerDeathIdDropFilter(String uuidsKey, DropHeadSource headSource, ConfigurationSection sourceYaml)
    {
        List<String> uuids = getPlayerIds(uuidsKey, sourceYaml);
        if (uuids != null)
        {
            PlayerDeathIdDropFilter uuidDropFilter = new PlayerDeathIdDropFilter(uuids);
            headSource.getDropFilters().add(uuidDropFilter);
            return true;
        }
        return false;
    }

    private boolean applyBiomeDropFilter(String biomesKey, DropHeadSource headSource, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        List<Biome> biomes = getBiomes(biomesKey, headSource.getHead().getKey(), sourceYaml);
        if (biomes != null)
        {
            BiomeDropFilter biomeDropFilter = new BiomeDropFilter(biomes);
            headSource.getDropFilters().add(biomeDropFilter);
            return true;
        }
        return false;
    }

    private boolean applyToolDropFilter(String toolsKey, DropHeadSource headSource, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        List<Material> tools = getMaterials(toolsKey, headSource.getHead().getKey(), sourceYaml);
        if (tools != null)
        {
            ToolDropFilter toolDropFilter = new ToolDropFilter(tools);
            headSource.getDropFilters().add(toolDropFilter);
            return true;
        }
        return false;
    }

    private boolean applyRecipeResultDropFilter(String recipeResultKey, DropHeadSource headSource, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        List<Material> resultTypes = getMaterials(recipeResultKey, headSource.getHead().getKey(), sourceYaml);
        if (resultTypes != null)
        {
            RecipeResultDropFilter recipeResultDropFilter = new RecipeResultDropFilter(resultTypes);
            headSource.getDropFilters().add(recipeResultDropFilter);
            return true;
        }
        return false;
    }

    private boolean applyEventInvokerFilter(String eventInvokerKey, DropHeadSource headSource, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        String invokerName = sourceYaml.getString(eventInvokerKey);
        EventInvokerFilter.EventInvoker eventInvoker = EventInvokerFilter.matchInvoker(invokerName);
        if (eventInvoker != null)
        {
            EventInvokerFilter invokerDropFilter = new EventInvokerFilter(eventInvoker);
            headSource.getDropFilters().add(invokerDropFilter);
            return true;
        }
        else if (eventInvoker == null && sourceYaml.contains(eventInvokerKey))
            throw new InvalidHeadSourceException(String.format("Unrecognised event invoker: %s.", invokerName));
        return false;
    }

    private double getDropRate(String dropRateKey, ConfigurationSection sourceYaml)
    {
        if (sourceYaml.getKeys(false).contains(dropRateKey))
            return sourceYaml.getDouble(dropRateKey);
        else
            return 100;
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

    private List<Material> getMaterials(String materialsKey, String headKey, ConfigurationSection sourceYaml) throws InvalidHeadSourceException
    {
        if (sourceYaml.getKeys(false).contains(materialsKey))
        {
            List<String> materialNames = sourceYaml.getStringList(materialsKey);
            List<Material> materials = new ArrayList<>();
            for (String materialName : materialNames)
            {
                Material material = Material.matchMaterial(materialName);
                if (material == null)
                    throw new InvalidHeadSourceException(String.format("Unrecognised material \"%s\" for %s in %s.", materialName, headKey, materialsKey));
                else
                    materials.add(material);
            }
            return (materials.size() > 0) ? materials : null;
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

    private List<String> getPlayerIds(String uuidsKey, ConfigurationSection sourceYaml)
    {
        if (sourceYaml.getKeys(false).contains(uuidsKey))
        {
            List<String> uuids = sourceYaml.getStringList(uuidsKey);
            return (uuids.size() > 0) ? uuids : null;
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