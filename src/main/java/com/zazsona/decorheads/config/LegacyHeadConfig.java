package com.zazsona.decorheads.config;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.HeadType;
import com.zazsona.decorheads.config.update.LegacyHeadConfigMigrator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class LegacyHeadConfig extends VersionedYamlConfigWrapper
{
    public static final String VERSION_KEY = "version";
    public static final String HEADS_KEY = "heads";
    public static final String PLAYER_HEAD_KEY = "player";
    public static final String NAME_KEY = "name";
    public static final String TEXTURE_KEY = "texture";
    public static final String SOURCES_KEY = "sources";
    public static final String SOURCE_TYPE_KEY = "source-type";
    public static final String DROP_RATE_KEY = "drop-rate";
    public static final String DROP_BLOCKS_KEY = "blocks";
    public static final String DROP_TOOLS_KEY = "tools";
    public static final String DROP_BIOMES_KEY = "biomes";
    public static final String DROP_ENTITIES_KEY = "entities";
    public static final String DROP_KILLED_PLAYER_IDS_KEY = "killed-player-ids";
    public static final String DROP_RECIPE_RESULTS_KEY = "recipe-results";
    public static final String DROP_RECIPE_INGREDIENTS_KEY = "recipe-ingredients";
    public static final String DROP_EVENT_INVOKER_KEY = "event-invoker";
    public static final String WEATHER_KEY = "weather";
    public static final String WORLDS_KEY = "worlds";
    public static final String CRAFT_INGREDIENTS_KEY = "ingredients";
    public static final String CRAFT_GRID_KEY = "grid";

    private static final String FINAL_LEGACY_CONFIG_VERSION = "2.3.0";

    public LegacyHeadConfig(FileConfiguration config, File saveLocation)
    {
        super(config, saveLocation);
    }

    public LegacyHeadConfig(File configFile)
    {
        super(configFile);
    }

    public static String getMinConfigVersion()
    {
        return "2.0.0";
    }
    public static String getMaxConfigVersion()
    {
        return "2.3.0";
    }

    public static boolean isLegacyConfig(File file)
    {
        return isLegacyConfig(YamlConfiguration.loadConfiguration(file));
    }

    public static boolean isLegacyConfig(FileConfiguration configData)
    {
        if (configData != null || configData.contains(LegacyHeadConfig.VERSION_KEY))
        {
            String version = configData.getString(LegacyHeadConfig.VERSION_KEY);
            int compare = version.compareTo(getMaxConfigVersion());
            return compare <= 0;
        }
        throw new IllegalArgumentException(String.format("%s field is absent or incorrect.", LegacyHeadConfig.VERSION_KEY));
    }

    /**
     * Gets the path to the value where the heads are stored
     * @return the path
     */
    protected String getHeadsPath()
    {
        return HEADS_KEY;
    }

    /**
     * Gets the path to the value where a head is stored
     * @return the path
     */
    protected String getHeadPath(String headKey)
    {
        return String.format("%s.%s", getHeadsPath(), headKey);
    }

    /**
     * Gets the path to the value where a head's name is stored
     * @return the path
     */
    protected String getHeadNamePath(String headKey)
    {
        return String.format("%s.%s", getHeadPath(headKey), NAME_KEY);
    }

    /**
     * Gets the path to the value where a head's texture is stored
     * @return the path
     */
    protected String getHeadTexturePath(String headKey)
    {
        return String.format("%s.%s", getHeadPath(headKey), TEXTURE_KEY);
    }

    /**
     * Gets the path to the value where a head's sources are stored
     * @return the path
     */
    protected String getHeadSourcesPath(String headKey)
    {
        return String.format("%s.%s", getHeadPath(headKey), SOURCES_KEY);
    }

    /**
     * Gets the path to the value where a head's source is stored
     * @return the path
     */
    protected String getHeadSourcePath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcesPath(headKey), sourceKey);
    }

    /**
     * Gets the path to the value where a head source's type is stored
     * @return the path
     */
    protected String getHeadSourceTypePath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), SOURCE_TYPE_KEY);
    }

    /**
     * Gets the path to the value where a head source's drop rate is stored
     * @return the path
     */
    protected String getHeadSourceDropRatePath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), DROP_RATE_KEY);
    }

    /**
     * Gets the path to the value where a head source's biome filter data is stored
     * @return the path
     */
    protected String getHeadSourceBiomeFilterPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), DROP_BIOMES_KEY);
    }

    /**
     * Gets the path to the value where a head source's block filter data is stored
     * @return the path
     */
    protected String getHeadSourceBlockFilterPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), DROP_BLOCKS_KEY);
    }

    /**
     * Gets the path to the value where a head source's entity filter data is stored
     * @return the path
     */
    protected String getHeadSourceEntityFilterPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), DROP_ENTITIES_KEY);
    }

    /**
     * Gets the path to the value where a head source's Event Invoker filter data is stored
     * @return the path
     */
    protected String getHeadSourceEventInvokerFilterPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), DROP_EVENT_INVOKER_KEY);
    }

    /**
     * Gets the path to the value where a head source's Killed Player Ids filter data is stored
     * @return the path
     */
    protected String getHeadSourceKilledPlayerIdsFilterPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), DROP_KILLED_PLAYER_IDS_KEY);
    }

    /**
     * Gets the path to the value where a head source's recipe ingredients filter data is stored
     * @return the path
     */
    protected String getHeadSourceRecipeIngredientsFilterPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), DROP_RECIPE_INGREDIENTS_KEY);
    }

    /**
     * Gets the path to the value where a head source's recipe result filter data is stored
     * @return the path
     */
    protected String getHeadSourceRecipeResultFilterPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), DROP_RECIPE_RESULTS_KEY);
    }

    /**
     * Gets the path to the value where a head source's tools filter data is stored
     * @return the path
     */
    protected String getHeadSourceToolFilterPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), DROP_TOOLS_KEY);
    }

    /**
     * Gets the path to the value where a head source's weather filter data is stored
     * @return the path
     */
    protected String getHeadSourceWeatherFilterPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), WEATHER_KEY);
    }

    /**
     * Gets the path to the value where a head source's world filter data is stored
     * @return the path
     */
    protected String getHeadSourceWorldFilterPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), WORLDS_KEY);
    }

    /**
     * Gets the path to the value where a head source's ingredients are stored
     * @return the path
     */
    protected String getHeadSourceIngredientsPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), CRAFT_INGREDIENTS_KEY);
    }

    /**
     * Gets the path to the value where a head source's crafting grid is stored
     * @return the path
     */
    protected String getHeadSourceGridPath(String headKey, String sourceKey)
    {
        return String.format("%s.%s", getHeadSourcePath(headKey, sourceKey), CRAFT_GRID_KEY);
    }

    // ===========================
    // Heads
    // ===========================

    public ConfigurationSection getHeads()
    {
        return config.getConfigurationSection(HEADS_KEY);
    }

    public void setHeads(ConfigurationSection heads)
    {
        if (!config.isConfigurationSection(HEADS_KEY) || heads == null)
            config.createSection(HEADS_KEY);

        if (heads != null)
            config.set(HEADS_KEY, heads);
    }

    public Set<String> getHeadKeys()
    {
        return getHeads().getKeys(false);
    }

    public ConfigurationSection getHead(String headKey)
    {
        return getHeads().getConfigurationSection(headKey);
    }

    public void addHead(String headKey, String headName, String headTexture)
    {
        String path = getHeadPath(headKey);
        if (config.getConfigurationSection(path) != null)
            throw new IllegalArgumentException(String.format("Head with key \"%s\" already exists.", headKey));

        config.createSection(path);
        setHeadName(headKey, headName);
        setHeadTexture(headKey, headTexture);
    }

    public void removeHead(String headKey)
    {
        config.set(getHeadPath(headKey), null);
    }

    public String getHeadName(String headKey)
    {
        if (isPlayerHead(headKey))
            return "Player's Head";

        return config.getString(getHeadNamePath(headKey));
    }

    public void setHeadName(String headKey, String headName)
    {
        if (isPlayerHead(headKey))
            throw new IllegalArgumentException("Cannot set a name for the \"player\" head.");

        config.set(getHeadNamePath(headKey), headName);
    }

    public boolean isPlayerHead(String headKey)
    {
        return headKey.equalsIgnoreCase("player");
    }

    public String getHeadTexture(String headKey)
    {
        if (isPlayerHead(headKey))
            throw new IllegalArgumentException("Cannot get a texture for the \"player\" head.");
        return config.getString(getHeadTexturePath(headKey));
    }

    public void setHeadTexture(String headKey, String headTexture)
    {
        if (isPlayerHead(headKey))
            throw new IllegalArgumentException("Cannot set a texture for the \"player\" head.");

        config.set(getHeadTexturePath(headKey), headTexture);
    }

    // ===========================
    // Heads//Sources
    // ===========================

    public ConfigurationSection getSources(String headKey)
    {
        return config.getConfigurationSection(getHeadSourcesPath(headKey));
    }

    public Set<String> getSourceKeys(String headKey)
    {
        return config.getConfigurationSection(getHeadSourcesPath(headKey)).getKeys(false);
    }

    public ConfigurationSection getSource(String headKey, String sourceKey)
    {
        return config.getConfigurationSection(getHeadSourcePath(headKey, sourceKey));
    }

    protected void addSource(String headKey, String sourceKey, String sourceType)
    {
        String path = getHeadSourcePath(headKey, sourceKey);
        if (config.getConfigurationSection(path) != null)
            throw new IllegalArgumentException(String.format("Source with key \"%s\" already exists for head \"%s\".", sourceKey, headKey));

        config.createSection(path);
        setSourceType(headKey, sourceKey, sourceType);
    }

    protected void removeSource(String headKey, String sourceKey)
    {
        config.set(getHeadSourcePath(headKey, sourceKey), null);
    }

    public String getSourceType(String headKey, String sourceKey)
    {
        return config.getString(getHeadSourceTypePath(headKey, sourceKey));
    }

    public void setSourceType(String headKey, String sourceKey, String sourceType)
    {
        config.set(getHeadSourceTypePath(headKey, sourceKey), sourceType);
    }

    // ===========================
    // Heads//Sources//Drops
    // ===========================

    public void addDropSource(String headKey, String sourceKey, String sourceType, double dropRate)
    {
        addSource(headKey, sourceKey, sourceType);
        setDropRate(headKey, sourceKey, dropRate);
    }

    public double getDropRate(String headKey, String sourceKey)
    {
        return config.getDouble(getHeadSourceDropRatePath(headKey, sourceKey));
    }

    public void setDropRate(String headKey, String sourceKey, double dropRate)
    {
        config.set(getHeadSourceDropRatePath(headKey, sourceKey), dropRate);
    }

    // ===========================
    // Heads//Sources//Drops//Filters
    // ===========================

    public List<String> getBiomeFilter(String headKey, String sourceKey)
    {
        return config.getStringList(getHeadSourceBiomeFilterPath(headKey, sourceKey));
    }

    public void setBiomeFilter(String headKey, String sourceKey, List<String> filterValues)
    {
        config.set(getHeadSourceBiomeFilterPath(headKey, sourceKey), filterValues);
    }

    public List<String> getBlockFilter(String headKey, String sourceKey)
    {
        return config.getStringList(getHeadSourceBlockFilterPath(headKey, sourceKey));
    }

    public void setBlockFilter(String headKey, String sourceKey, List<String> filterValues)
    {
        config.set(getHeadSourceBlockFilterPath(headKey, sourceKey), filterValues);
    }

    public List<String> getEntityFilter(String headKey, String sourceKey)
    {
        return config.getStringList(getHeadSourceEntityFilterPath(headKey, sourceKey));
    }

    public void setEntityFilter(String headKey, String sourceKey, List<String> filterValues)
    {
        config.set(getHeadSourceEntityFilterPath(headKey, sourceKey), filterValues);
    }

    public String getEventInvokerFilter(String headKey, String sourceKey)
    {
        return config.getString(getHeadSourceEventInvokerFilterPath(headKey, sourceKey));
    }

    public void setEventInvokerFilter(String headKey, String sourceKey, String eventInvoker)
    {
        config.set(getHeadSourceEventInvokerFilterPath(headKey, sourceKey), eventInvoker);
    }

    public List<String> getKilledPlayerIdsFilter(String headKey, String sourceKey)
    {
        return config.getStringList(getHeadSourceKilledPlayerIdsFilterPath(headKey, sourceKey));
    }

    public void setKilledPlayerIdsFilter(String headKey, String sourceKey, List<String> filterValues)
    {
        config.set(getHeadSourceKilledPlayerIdsFilterPath(headKey, sourceKey), filterValues);
    }

    /**
     * Gets the recipe ingredient sets for the filter.<br>
     * Note: each element in the list is an OR. i.e, Element 1 or Element 2 will pass the filter.
     * However, each element in the ARRAYs are an AND. i.e, (Element 1[0] and Element 1[1]) or (Element 2[0]) must be present to pass the filter.
     * @param headKey the key for the head that owns the source that owns this filter
     * @param sourceKey the key for the source that owns this filter
     * @return the filter values; or null if the filter is not present for this drop.
     */
    public List<String[]> getRecipeIngredientsFilter(String headKey, String sourceKey)
    {
        List<?> recipeIngredientObjects = config.getList(getHeadSourceRecipeIngredientsFilterPath(headKey, sourceKey));
        List<String[]> recipeIngredientGroups = new ArrayList<>();
        for (Object childElement : recipeIngredientObjects)
        {
            if (childElement instanceof String)
                recipeIngredientGroups.add(new String[] { (String) childElement });
            else if (childElement instanceof List<?>)
                recipeIngredientGroups.add(((List<String>) childElement).toArray(new String[0]));
        }

        return recipeIngredientGroups;
    }

    /**
     * Sets the recipe ingredient sets for the filter.<br>
     * Note: each element in the list is an OR. i.e, Element 1 or Element 2 will pass the filter.
     * However, each element in the ARRAYs are an AND. i.e, (Element 1[0] and Element 1[1]) or (Element 2[0]) must be present to pass the filter.
     * @param headKey the key for the head that owns the source that owns this filter
     * @param sourceKey the key for the source that owns this filter
     * @param recipeIngredientGroups the filter values; or null to remove.
     */
    public void setRecipeIngredientsFilter(String headKey, String sourceKey, List<String[]> recipeIngredientGroups)
    {
        String path = getHeadSourceRecipeIngredientsFilterPath(headKey, sourceKey);
        if (recipeIngredientGroups == null)
            config.set(path, null);

        List<Object> recipeIngredientObjects = new ArrayList<>();
        for (String[] ingredientGroup : recipeIngredientGroups)
        {
            if (ingredientGroup.length == 0)
                recipeIngredientObjects.add(ingredientGroup[0]);
            else
                recipeIngredientObjects.add(Arrays.asList(ingredientGroup));
        }
        config.set(path, recipeIngredientObjects);
    }

    public List<String> getRecipeResultFilter(String headKey, String sourceKey)
    {
        return config.getStringList(getHeadSourceRecipeResultFilterPath(headKey, sourceKey));
    }

    public void setRecipeResultFilter(String headKey, String sourceKey, List<String> filterValues)
    {
        config.set(getHeadSourceRecipeResultFilterPath(headKey, sourceKey), filterValues);
    }

    public List<String> getToolFilter(String headKey, String sourceKey)
    {
        return config.getStringList(getHeadSourceToolFilterPath(headKey, sourceKey));
    }

    public void setToolFilter(String headKey, String sourceKey, List<String> filterValues)
    {
        config.set(getHeadSourceToolFilterPath(headKey, sourceKey), filterValues);
    }

    public List<String> getWeatherFilter(String headKey, String sourceKey)
    {
        return config.getStringList(getHeadSourceWeatherFilterPath(headKey, sourceKey));
    }

    public void setWeatherFilter(String headKey, String sourceKey, List<String> filterValues)
    {
        config.set(getHeadSourceWeatherFilterPath(headKey, sourceKey), filterValues);
    }

    public List<String> getWorldFilter(String headKey, String sourceKey)
    {
        return config.getStringList(getHeadSourceWorldFilterPath(headKey, sourceKey));
    }

    public void setWorldFilter(String headKey, String sourceKey, List<String> filterValues)
    {
        config.set(getHeadSourceWorldFilterPath(headKey, sourceKey), filterValues);
    }

    // ===========================
    // Heads//Sources//Recipes
    // ===========================

    public void addRecipeSource(String headKey, String sourceKey, String sourceType, List<String> recipeIngredients)
    {
        addSource(headKey, sourceKey, sourceType);
        setRecipeIngredients(headKey, sourceKey, recipeIngredients);
    }

    public List<String> getRecipeIngredients(String headKey, String sourceKey)
    {
        return config.getStringList(getHeadSourceIngredientsPath(headKey, sourceKey));
    }

    public void setRecipeIngredients(String headKey, String sourceKey, List<String> recipeIngredients)
    {
        String path = String.format("%s.%s.%s.%s", HEADS_KEY, headKey, sourceKey, DROP_RECIPE_INGREDIENTS_KEY);
        config.set(getHeadSourceIngredientsPath(headKey, sourceKey), recipeIngredients);
    }

    public List<String> getRecipeGrid(String headKey, String sourceKey)
    {
        return config.getStringList(getHeadSourceGridPath(headKey, sourceKey));
    }

    public void setRecipeGrid(String headKey, String sourceKey, List<String> recipeGrid)
    {
        config.set(getHeadSourceGridPath(headKey, sourceKey), recipeGrid);
    }


    @Override
    protected FileConfiguration getEmptyConfigData()
    {
        InputStream is = DecorHeadsPlugin.getInstance().getResource("updates/legacy/complete.yml");
        InputStreamReader isr = new InputStreamReader(is);
        return YamlConfiguration.loadConfiguration(isr);
    }

    @Override
    protected boolean validateConfigData(FileConfiguration configData)
    {
        if (!super.validateConfigData(configData))
            return false;

        boolean hasHeadsSection = configData.getConfigurationSection(HEADS_KEY) != null;
        return hasHeadsSection;
    }
}
