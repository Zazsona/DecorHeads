package com.zazsona.decorheads.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeadRecipeConfig extends VersionedYamlConfigWrapper
{
    public static final String VERSION_KEY = "version";
    public static final String RECIPES_KEY = "recipes";
    public static final String RECIPE_TYPE_KEY = "recipe-type";
    public static final String RECIPE_RESULT_KEY = "result";
    public static final String RECIPE_INGREDIENTS_KEY = "ingredients";
    public static final String RECIPE_GRID_KEY = "grid";

    public HeadRecipeConfig(FileConfiguration config, File saveLocation)
    {
        super(config, saveLocation);
    }

    public HeadRecipeConfig(File configFile)
    {
        super(configFile);
    }

    public static String getMinConfigVersion()
    {
        return "3.0.0";
    }

    /**
     * Gets the path to the value where the recipes are stored
     * @return the path
     */
    protected String getRecipesPath()
    {
        return RECIPES_KEY;
    }

    /**
     * Gets the path to the value where a recipe is stored
     * @return the path
     */
    protected String getRecipePath(String recipeKey)
    {
        return String.format("%s.%s", getRecipesPath(), recipeKey);
    }

    /**
     * Gets the path to the value where a recipe's type is stored
     * @return the path
     */
    protected String getRecipeTypePath(String recipeKey)
    {
        return String.format("%s.%s", getRecipePath(recipeKey), RECIPE_TYPE_KEY);
    }

    /**
     * Gets the path to the value where a recipe's result is stored
     * @return the path
     */
    protected String getRecipeResultPath(String recipeKey)
    {
        return String.format("%s.%s", getRecipePath(recipeKey), RECIPE_RESULT_KEY);
    }

    /**
     * Gets the path to the value where a recipe's ingredients are stored
     * @return the path
     */
    protected String getRecipeIngredientsPath(String recipeKey)
    {
        return String.format("%s.%s", getRecipePath(recipeKey), RECIPE_INGREDIENTS_KEY);
    }

    /**
     * Gets the path to the value where a recipe's grid is stored
     * @return the path
     */
    protected String getRecipeGridPath(String recipeKey)
    {
        return String.format("%s.%s", getRecipePath(recipeKey), RECIPE_GRID_KEY);
    }

    public ConfigurationSection getRecipes()
    {
        return config.getConfigurationSection(RECIPES_KEY);
    }

    public void setRecipes(ConfigurationSection recipes)
    {
        if (!config.isConfigurationSection(RECIPES_KEY) || recipes == null)
            config.createSection(RECIPES_KEY);

        if (recipes != null)
            config.set(RECIPES_KEY, recipes);
    }

    public Set<String> getRecipeKeys()
    {
        return getRecipes().getKeys(false);
    }

    public ConfigurationSection getRecipe(String recipeKey)
    {
        return getRecipes().getConfigurationSection(getRecipePath(recipeKey));
    }

    public void addRecipe(String recipeKey, String recipeType, String recipeResult, Map<String, String> recipeIngredients)
    {
        String path = getRecipePath(recipeKey);
        if (config.getConfigurationSection(path) != null)
            throw new IllegalArgumentException(String.format("Recipe with key \"%s\" already exists.", recipeKey));

        config.createSection(path);
        setRecipeType(recipeKey, recipeType);
        setRecipeResult(recipeKey, recipeResult);
        setRecipeIngredients(recipeKey, recipeIngredients);
    }

    public void removeRecipe(String recipeKey)
    {
        config.set(getRecipePath(recipeKey), null);
    }

    public String getRecipeType(String recipeKey)
    {
        return config.getString(getRecipeTypePath(recipeKey));
    }

    public void setRecipeType(String recipeKey, String recipeType)
    {
        config.set(getRecipeTypePath(recipeKey), recipeType);
    }

    public String getRecipeResult(String recipeKey)
    {
        return config.getString(getRecipeResultPath(recipeKey));
    }

    public void setRecipeResult(String recipeKey, String recipeResult)
    {
        config.set(getRecipeResultPath(recipeKey), recipeResult);
    }

    public ConfigurationSection getRecipeIngredientsRaw(String recipeKey)
    {
        return config.getConfigurationSection(getRecipeIngredientsPath(recipeKey));
    }

    public Map<String, String> getRecipeIngredients(String recipeKey)
    {
        ConfigurationSection configSection = getRecipeIngredientsRaw(recipeKey);
        if (configSection == null)
            return null;

        HashMap<String, String> ingredientsMap = new HashMap<>();
        for (String key : configSection.getKeys(false))
            ingredientsMap.put(key, configSection.getString(key));
        return ingredientsMap;
    }

    public void setRecipeIngredients(String recipeKey, ConfigurationSection recipeIngredients)
    {
        config.set(getRecipeIngredientsPath(recipeKey), recipeIngredients);
    }

    public void setRecipeIngredients(String recipeKey, Map<String, String> recipeIngredients)
    {
        String path = getRecipeIngredientsPath(recipeKey);
        if (config.getConfigurationSection(path) == null)
            config.createSection(path);

        ConfigurationSection recipeIngredientsSection = config.getConfigurationSection(path);
        for (Map.Entry<String, String> entry : recipeIngredients.entrySet())
            recipeIngredientsSection.set(entry.getKey(), entry.getValue());
    }

    public List<String> getRecipeGrid(String recipeKey)
    {
        return config.getStringList(getRecipeGridPath(recipeKey));
    }

    public void setRecipeGrid(String recipeKey, List<String> recipeGrid)
    {
        config.set(getRecipeGridPath(recipeKey), recipeGrid);
    }

    @Override
    protected FileConfiguration getEmptyConfigData()
    {
        FileConfiguration emptyConfig = super.getEmptyConfigData();
        emptyConfig.createSection(RECIPES_KEY);
        return emptyConfig;
    }

    @Override
    protected boolean validateConfigData(FileConfiguration configData)
    {
        if (!super.validateConfigData(configData))
            return false;

        boolean hasRecipesSection = configData.getConfigurationSection(RECIPES_KEY) != null;
        return hasRecipesSection;
    }
}
