package com.zazsona.decorheads.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
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
        super(config, saveLocation, VERSION_KEY);
    }

    public HeadRecipeConfig(File configFile)
    {
        super(configFile, VERSION_KEY);
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
        return getRecipes().getConfigurationSection(recipeKey);
    }

    public void setRecipe(String recipeKey, ConfigurationSection recipe)
    {
        String path = String.format("%s.%s", RECIPES_KEY, recipeKey);
        config.set(path, recipe);
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
