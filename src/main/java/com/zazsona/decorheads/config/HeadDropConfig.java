package com.zazsona.decorheads.config;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeadDropConfig extends VersionedYamlConfigWrapper
{
    public static final String VERSION_KEY = "version";
    public static final String DROPS_KEY = "drops";
    public static final String DROP_TYPE_KEY = "drop-type";
    public static final String DROP_RESULT_KEY = "result";
    public static final String DROP_RATE_KEY = "drop-rate";

    public static final String FILTER_BIOME_KEY = "biome";
    public static final String FILTER_BLOCK_KEY = "block";
    public static final String FILTER_ENTITY_KEY = "entity";
    public static final String FILTER_EVENT_EVOKER_KEY = "event-evoker";
    public static final String FILTER_RECIPE_INGREDIENTS_KEY = "recipe-ingredients";
    public static final String FILTER_RECIPE_RESULT_KEY = "recipe-result";
    public static final String FILTER_TOOL_KEY = "tool";
    public static final String FILTER_WEATHER_KEY = "weather";
    public static final String FILTER_WORLD_KEY = "world";
    public static final String FILTER_AND_OPERATOR = "&&";
    public static final String FILTER_OR_OPERATOR = "||";

    public HeadDropConfig(FileConfiguration config, File saveLocation)
    {
        super(config, saveLocation);
    }

    public HeadDropConfig(File configFile)
    {
        super(configFile);
    }

    public static String getMinConfigVersion()
    {
        return "3.0.0";
    }

    /**
     * Gets the path to the value where the drops are stored
     * @return the path
     */
    protected String getDropsPath()
    {
        return DROPS_KEY;
    }

    /**
     * Gets the path to the value where a drop is stored
     * @return the path
     */
    protected String getDropPath(String dropKey)
    {
        return String.format("%s.%s", getDropsPath(), dropKey);
    }

    /**
     * Gets the path to the value where the drop's type is stored
     * @return the path
     */
    protected String getDropTypePath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), DROP_TYPE_KEY);
    }

    /**
     * Gets the path to the value where a drop's result is stored
     * @return the path
     */
    protected String getDropResultPath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), DROP_RESULT_KEY);
    }

    /**
     * Gets the path to the value where a drop's drop rate is stored
     * @return the path
     */
    protected String getDropRatePath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), DROP_RATE_KEY);
    }

    /**
     * Gets the path to the value where a drop's biome filter data is stored
     * @return the path
     */
    protected String getHeadSourceBiomeFilterPath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), FILTER_BIOME_KEY);
    }

    /**
     * Gets the path to the value where a drop's block filter data is stored
     * @return the path
     */
    protected String getHeadSourceBlockFilterPath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), FILTER_BLOCK_KEY);
    }

    /**
     * Gets the path to the value where a drop's entity filter data is stored
     * @return the path
     */
    protected String getHeadSourceEntityFilterPath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), FILTER_ENTITY_KEY);
    }

    /**
     * Gets the path to the value where a drop's Event Evoker filter data is stored
     * @return the path
     */
    protected String getHeadSourceEventEvokerFilterPath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), FILTER_EVENT_EVOKER_KEY);
    }

    /**
     * Gets the path to the value where a drop's recipe ingredients filter data is stored
     * @return the path
     */
    protected String getHeadSourceRecipeIngredientsFilterPath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), FILTER_RECIPE_INGREDIENTS_KEY);
    }

    /**
     * Gets the path to the value where a drop's recipe result filter data is stored
     * @return the path
     */
    protected String getHeadSourceRecipeResultFilterPath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), FILTER_RECIPE_RESULT_KEY);
    }

    /**
     * Gets the path to the value where a drop's tools filter data is stored
     * @return the path
     */
    protected String getHeadSourceToolFilterPath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), FILTER_TOOL_KEY);
    }

    /**
     * Gets the path to the value where a drop's weather filter data is stored
     * @return the path
     */
    protected String getHeadSourceWeatherFilterPath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), FILTER_WEATHER_KEY);
    }

    /**
     * Gets the path to the value where a drop's world filter data is stored
     * @return the path
     */
    protected String getHeadSourceWorldFilterPath(String dropKey)
    {
        return String.format("%s.%s", getDropPath(dropKey), FILTER_WORLD_KEY);
    }

    public ConfigurationSection getDrops()
    {
        return config.getConfigurationSection(getDropsPath());
    }

    public Set<String> getDropKeys()
    {
        return getDrops().getKeys(false);
    }

    public ConfigurationSection getDrop(String dropKey)
    {
        return getDrops().getConfigurationSection(getDropPath(dropKey));
    }

    public void addDrop(String dropKey, String dropType, String dropResult, double dropRate)
    {
        String path = getDropPath(dropKey);
        if (config.getConfigurationSection(path) != null)
            throw new IllegalArgumentException(String.format("Drop with key \"%s\" already exists.", dropKey));

        config.createSection(path);
        setDropType(dropKey, dropType);
        setDropResult(dropKey, dropResult);
        setDropRate(dropKey, dropRate);
    }

    public void removeDrop(String dropKey)
    {
        config.set(getDropPath(dropKey), null);
    }

    public String getDropType(String dropKey)
    {
        return config.getString(getDropTypePath(dropKey));
    }

    public void setDropType(String dropKey, String dropType)
    {
        config.set(getDropTypePath(dropKey), dropType);
    }

    public String getDropResult(String dropKey)
    {
        return config.getString(getDropResultPath(dropKey));
    }

    public void setDropResult(String dropKey, String dropResult)
    {
        config.set(getDropResultPath(dropKey), dropResult);
    }

    public double getDropRate(String dropKey)
    {
        return config.getDouble(getDropRatePath(dropKey));
    }

    public void setDropRate(String dropKey, double dropRate)
    {
        config.set(getDropRatePath(dropKey), dropRate);
    }

    public List<String> getBiomeFilter(String dropKey)
    {
        return config.getStringList(getHeadSourceBiomeFilterPath(dropKey));
    }

    public void setBiomeFilter(String dropKey, List<String> filterValues)
    {
        config.set(getHeadSourceBiomeFilterPath(dropKey), filterValues);
    }

    public List<String> getBlockFilter(String dropKey)
    {
        return config.getStringList(getHeadSourceBlockFilterPath(dropKey));
    }

    public void setBlockFilter(String dropKey, List<String> filterValues)
    {
        config.set(getHeadSourceBlockFilterPath(dropKey), filterValues);
    }

    public List<String> getEntityFilter(String dropKey)
    {
        return config.getStringList(getHeadSourceEntityFilterPath(dropKey));
    }

    public void setEntityFilter(String dropKey, List<String> filterValues)
    {
        config.set(getHeadSourceEntityFilterPath(dropKey), filterValues);
    }

    public List<String> getEventEvokerFilter(String dropKey)
    {
        return config.getStringList(getHeadSourceEventEvokerFilterPath(dropKey));
    }

    public void setEventEvokerFilter(String dropKey, List<String> filterValues)
    {
        config.set(getHeadSourceEventEvokerFilterPath(dropKey), filterValues);
    }

    /**
     * Gets the recipe ingredient sets for the filter.<br>
     * Note: each element in the list is an OR. i.e, Element 1 or Element 2 will pass the filter.
     * However, each element in the ARRAYs are an AND. i.e, (Element 1[0] and Element 1[1]) or (Element 2[0]) must be present to pass the filter.
     * @param dropKey the key for the drop that owns this filter
     * @return the filter values; or null if the filter is not present for this drop.
     */
    public List<String[]> getRecipeIngredientsFilter(String dropKey)
    {
        String path = getHeadSourceRecipeIngredientsFilterPath(dropKey);
        List<String> ingredientStrings = config.getStringList(path);
        List<String[]> ingredients = new ArrayList<>();
        for (String ingredientString : ingredientStrings)
            ingredients.add(ingredientString.replace(" ", "").split(FILTER_AND_OPERATOR));
        return ingredients;
    }

    /**
     * Sets the recipe ingredient sets for the filter.<br>
     * Note: each element in the list is an OR. i.e, Element 1 or Element 2 will pass the filter.
     * However, each element in the ARRAYs are an AND. i.e, (Element 1[0] and Element 1[1]) or (Element 2[0]) must be present to pass the filter.
     * @param dropKey the key for the drop that owns this filter
     * @param recipeIngredientGroups the filter values; or null to remove.
     */
    public void setRecipeIngredientsFilter(String dropKey, List<String[]> recipeIngredientGroups)
    {
        String path = getHeadSourceRecipeIngredientsFilterPath(dropKey);
        if (recipeIngredientGroups == null)
            config.set(path, null);

        List<String> ingredientStrings = new ArrayList<>();
        for (String[] ingredientSet : recipeIngredientGroups)
        {
            StringBuilder ingredientStringBuilder = new StringBuilder();
            for (String ingredient : ingredientSet)
                ingredientStringBuilder.append(ingredient).append(" ").append(FILTER_AND_OPERATOR).append(" ");
            ingredientStrings.add(ingredientStringBuilder.substring(0, ingredientStringBuilder.lastIndexOf(FILTER_AND_OPERATOR)).trim());
        }
        config.set(path, ingredientStrings);
    }

    public List<String> getRecipeResultFilter(String dropKey)
    {
        return config.getStringList(getHeadSourceRecipeResultFilterPath(dropKey));
    }

    public void setRecipeResultFilter(String dropKey, List<String> filterValues)
    {
        config.set(getHeadSourceRecipeResultFilterPath(dropKey), filterValues);
    }

    public List<String> getToolFilter(String dropKey)
    {
        return config.getStringList(getHeadSourceToolFilterPath(dropKey));
    }

    public void setToolFilter(String dropKey, List<String> filterValues)
    {
        config.set(getHeadSourceToolFilterPath(dropKey), filterValues);
    }

    public List<String> getWeatherFilter(String dropKey)
    {
        return config.getStringList(getHeadSourceWeatherFilterPath(dropKey));
    }

    public void setWeatherFilter(String dropKey, List<String> filterValues)
    {
        config.set(getHeadSourceWeatherFilterPath(dropKey), filterValues);
    }

    public List<String> getWorldFilter(String dropKey)
    {
        return config.getStringList(getHeadSourceWorldFilterPath(dropKey));
    }

    public void setWorldFilter(String dropKey, List<String> filterValues)
    {
        config.set(getHeadSourceWorldFilterPath(dropKey), filterValues);
    }

    @Override
    public FileConfiguration getEmptyConfigData()
    {
        FileConfiguration emptyConfig = super.getEmptyConfigData();
        emptyConfig.createSection(DROPS_KEY);
        return emptyConfig;
    }

    @Override
    protected boolean validateConfigData(FileConfiguration configData)
    {
        if (!super.validateConfigData(configData))
            return false;

        boolean hasDropsSection = configData.getConfigurationSection(DROPS_KEY) != null;
        return hasDropsSection;
    }
}
