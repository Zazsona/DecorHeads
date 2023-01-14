package com.zazsona.decorheads.config.update;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.DropType;
import com.zazsona.decorheads.HeadType;
import com.zazsona.decorheads.RecipeType;
import com.zazsona.decorheads.config.*;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Migrates a heads v2.3.0 config into heads, drops, and recipes v3.0.0
 */
public class LegacyHeadConfigMigrator
{
    public static final String SOURCE_VERSION = "2.3.0";
    public static final String TARGET_VERSION = "3.0.0";
    private static final String[] craftGridIngredientKeys = { "A", "B", "C", "D", "E", "F", "G", "H", "I" };

    public Map<Class<? extends VersionedYamlConfigWrapper>, VersionedYamlConfigWrapper> migrate(LegacyHeadConfig legacyConfig, File headsFile, File dropsFile, File recipesFile)
    {
        // Check if config is on 2.3.0
        String configVersion = legacyConfig.getVersion();
        if (configVersion.compareTo(SOURCE_VERSION) < 0)
            throw new IllegalArgumentException("LegacyHeadConfig must be on v2.3.0 to be migrated. Use LegacyHeadConfigUpdater.");

        HeadConfig headConfig = migrateHeads(legacyConfig, headsFile);
        HeadRecipeConfig recipeConfig = migrateRecipes(legacyConfig, recipesFile);
        HeadDropConfig dropConfig = migrateDrops(legacyConfig, dropsFile);

        HashMap<Class<? extends VersionedYamlConfigWrapper>, VersionedYamlConfigWrapper> configMap = new HashMap<>();
        configMap.put(headConfig.getClass(), headConfig);
        configMap.put(recipeConfig.getClass(), recipeConfig);
        configMap.put(dropConfig.getClass(), dropConfig);
        return configMap;
    }

    private HeadConfig migrateHeads(LegacyHeadConfig legacyConfig, File headsFile)
    {
        Set<String> headKeys = legacyConfig.getHeadKeys();
        HeadConfig headConfig = new HeadConfig(null, headsFile);

        for (String key : headKeys)
            migrateHead(key, legacyConfig, key, headConfig);

        headConfig.setVersion(TARGET_VERSION);
        return headConfig;
    }

    private HeadConfig migrateHead(String headKey, LegacyHeadConfig legacyConfig, String newHeadKey, HeadConfig headConfig)
    {
        // v2.3.0 only allowed one special "player" head, which had a hard-coded name and no texture.
        HeadType newHeadType = (headKey.equalsIgnoreCase("player") ? HeadType.PLAYER : HeadType.DECOR);

        String newName;
        String newTexture;
        if (newHeadType == HeadType.DECOR)
        {
            newName = legacyConfig.getHeadName(headKey);
            newTexture = legacyConfig.getHeadTexture(headKey);
        }
        else
        {
            newName = "%PlayerName%'s Head";
            newTexture = null;
        }

        headConfig.addHead(newHeadKey, newHeadType.toString(), newName, newTexture);
        return headConfig;
    }

    private HeadDropConfig migrateDrops(LegacyHeadConfig legacyConfig, File dropsFile)
    {
        Set<String> headKeys = legacyConfig.getHeadKeys();
        HeadDropConfig dropConfig = new HeadDropConfig(null, dropsFile);
        for (String headKey : headKeys)
        {
            ConfigurationSection legacySources = legacyConfig.getSources(headKey);
            if (legacySources == null)
                continue;

            Set<String> sourceKeys = legacyConfig.getSourceKeys(headKey);
            for (String sourceKey : sourceKeys)
            {
                String legacySourceType = legacyConfig.getSourceType(headKey, sourceKey);
                if (!Pattern.matches("(MINE|BREW|SMELT|CRAFT|ENTITY_DEATH|PLAYER_DEATH)_DROP", legacySourceType.toUpperCase()))
                    continue;

                String dropKey = String.format("%s-%s", headKey, sourceKey); // E.g aquarium-default-craft-0
                migrateDrop(headKey, sourceKey, legacyConfig, dropKey, dropConfig);
            }
        }

        dropConfig.setVersion(TARGET_VERSION);
        return dropConfig;
    }

    private HeadDropConfig migrateDrop(String legacyHeadKey, String legacyDropKey, LegacyHeadConfig legacyConfig, String newDropKey, HeadDropConfig dropConfig)
    {
        String legacySourceType = legacyConfig.getSourceType(legacyHeadKey, legacyDropKey);
        double legacyDropRate = legacyConfig.getDropRate(legacyHeadKey, legacyDropKey);

        String newResultItem = String.format("%s:%s", DecorHeadsPlugin.PLUGIN_NAME.toLowerCase(), legacyHeadKey); // v2.3.0 only allowed heads to be dropped, so we know it's a head.
        DropType newDropType = DropType.matchDropType(legacySourceType.toUpperCase().replace("MINE_DROP", "BLOCK_BREAK").replace("PLAYER_DEATH_DROP", "ENTITY_DEATH").replace("_DROP", ""));
        double newDropRate = legacyDropRate / 100.0f; // Percentage -> Decimal

        dropConfig.addDrop(newDropKey, newDropType.toString(), newResultItem, newDropRate);
        migrateDropFilters(legacyHeadKey, legacyDropKey, legacyConfig, newDropKey, dropConfig);
        return dropConfig;
    }

    private HeadDropConfig migrateDropFilters(String legacyHeadKey, String legacyDropKey, LegacyHeadConfig legacyConfig, String newDropKey, HeadDropConfig dropConfig)
    {
        Set<String> dropChildKeys = legacyConfig.getSource(legacyHeadKey, legacyDropKey).getKeys(false);
        for (String dropChildKey : dropChildKeys)
        {
            if (!Pattern.matches("(BIOMES|BLOCKS|ENTITIES|EVENT-INVOKER|KILLED-PLAYER-IDS|RECIPE-INGREDIENTS|RECIPE-RESULTS|TOOLS|WEATHER|WORLDS)", dropChildKey.toUpperCase()))
                continue;

            migrateDropFilter(legacyHeadKey, legacyDropKey, dropChildKey, legacyConfig, newDropKey, dropConfig);
        }

        return dropConfig;
    }

    private HeadDropConfig migrateDropFilter(String legacyHeadKey, String legacyDropKey, String legacyFilterKey, LegacyHeadConfig legacyConfig, String newDropKey, HeadDropConfig dropConfig)
    {
        // TODO: As below, make sure loader supports the changed structure
        if (legacyFilterKey.equalsIgnoreCase(LegacyHeadConfig.DROP_EVENT_INVOKER_KEY))
        {
            String legacyEvoker = legacyConfig.getEventInvokerFilter(legacyHeadKey, legacyDropKey);
            List<String> newEvoker = new ArrayList<>();
            newEvoker.add(legacyEvoker.toUpperCase());
            dropConfig.setEventEvokerFilter(newDropKey, newEvoker);
        }
        // TODO: Since this is being removed, make sure the filter & loader supports entities like player{id:chfehdg68hggdhh67gdh8}
        else if (legacyFilterKey.equalsIgnoreCase(LegacyHeadConfig.DROP_KILLED_PLAYER_IDS_KEY))
        {
            List<String> playerIds = legacyConfig.getKilledPlayerIdsFilter(legacyHeadKey, legacyDropKey);
            List<String> playerEntities = new ArrayList<>();
            playerIds.forEach(id -> playerEntities.add("player{id:" + id + "}"));

            List<String> entities = legacyConfig.getEntityFilter(legacyHeadKey, legacyDropKey);
            entities.removeIf(s -> s.equalsIgnoreCase("player"));
            entities.addAll(playerEntities);

            dropConfig.setEntityFilter(newDropKey, entities);
        }
        else if (legacyFilterKey.equalsIgnoreCase(LegacyHeadConfig.DROP_ENTITIES_KEY) && legacyConfig.getKilledPlayerIdsFilter(legacyHeadKey, legacyDropKey).size() == 0)
            dropConfig.setEntityFilter(newDropKey, legacyConfig.getEntityFilter(legacyHeadKey, legacyDropKey));
        else
        {
            switch (legacyFilterKey.toUpperCase())
            {
                case "BIOMES":
                    dropConfig.setBiomeFilter(newDropKey, legacyConfig.getBiomeFilter(legacyHeadKey, legacyDropKey));
                    break;
                case "BLOCKS":
                    dropConfig.setBlockFilter(newDropKey, legacyConfig.getBlockFilter(legacyHeadKey, legacyDropKey));
                    break;
                case "RECIPE-INGREDIENTS":
                    dropConfig.setRecipeIngredientsFilter(newDropKey, legacyConfig.getRecipeIngredientsFilter(legacyHeadKey, legacyDropKey));
                    break;
                case "RECIPE-RESULTS":
                    dropConfig.setRecipeResultFilter(newDropKey, legacyConfig.getRecipeResultFilter(legacyHeadKey, legacyDropKey));
                    break;
                case "TOOLS":
                    dropConfig.setToolFilter(newDropKey, legacyConfig.getToolFilter(legacyHeadKey, legacyDropKey));
                    break;
                case "WEATHER":
                    dropConfig.setWeatherFilter(newDropKey, legacyConfig.getWeatherFilter(legacyHeadKey, legacyDropKey));
                    break;
                case "WORLDS":
                    dropConfig.setWorldFilter(newDropKey, legacyConfig.getWorldFilter(legacyHeadKey, legacyDropKey));
                    break;
            }
        }

        return dropConfig;
    }

    private HeadRecipeConfig migrateRecipes(LegacyHeadConfig legacyConfig, File recipesFile)
    {
        Set<String> headKeys = legacyConfig.getHeadKeys();
        HeadRecipeConfig recipeConfig = new HeadRecipeConfig(null, recipesFile);
        for (String headKey : headKeys)
        {
            ConfigurationSection legacySources = legacyConfig.getSources(headKey);
            if (legacySources == null)
                continue;

            Set<String> sourceKeys = legacyConfig.getSourceKeys(headKey);
            for (String sourceKey : sourceKeys)
            {
                String legacySourceType = legacyConfig.getSourceType(headKey, sourceKey);
                if (!Pattern.matches("(SHAPED|SHAPELESS)_CRAFT", legacySourceType.toUpperCase()))
                    continue;

                String recipeKey = String.format("%s-%s", headKey, sourceKey); // E.g aquarium-default-craft-0
                migrateRecipe(headKey, sourceKey, legacyConfig, recipeKey, recipeConfig);
            }
        }

        recipeConfig.setVersion(TARGET_VERSION);
        return recipeConfig;
    }

    private HeadRecipeConfig migrateRecipe(String legacyHeadKey, String legacyRecipeKey, LegacyHeadConfig legacyConfig, String newRecipeKey, HeadRecipeConfig recipeConfig)
    {
        List<String> legacyIngredients = legacyConfig.getRecipeIngredients(legacyHeadKey, legacyRecipeKey);
        List<String> legacyGrid = legacyConfig.getRecipeGrid(legacyHeadKey, legacyRecipeKey);

        RecipeType newRecipeType = RecipeType.CRAFT; // v2.3.0 only allowed crafting recipes, so we know it's a CRAFT recipe
        String newResultItem = String.format("%s:%s", DecorHeadsPlugin.PLUGIN_NAME.toLowerCase(), legacyHeadKey); // v2.3.0 only allowed heads to be crafted, so we know it's a head.
        Map<String, String> newIngredients = new HashMap<>();
        for (int i = 0; i < legacyIngredients.size(); i++)
            newIngredients.put(craftGridIngredientKeys[i], legacyIngredients.get(i));
        List<String> newGrid = null;
        if (legacyGrid != null)
        {
            newGrid = new ArrayList<>();
            for (String row : legacyGrid)
            {
                String[] elements = row.split(" ");
                for (int i = 0; i < elements.length; i++)
                    elements[i] = (elements[i].equals("/") ? "/" : craftGridIngredientKeys[Integer.parseInt(elements[i])]);
                newGrid.add(String.join(" ", elements));
            }
        }

        recipeConfig.addRecipe(newRecipeKey, newRecipeType.toString(), newResultItem, newIngredients);
        if (newGrid.size() > 0)
            recipeConfig.setRecipeGrid(newRecipeKey, newGrid);

        return recipeConfig;
    }
}
