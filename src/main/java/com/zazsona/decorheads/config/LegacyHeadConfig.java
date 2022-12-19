package com.zazsona.decorheads.config;

import com.zazsona.decorheads.DecorHeadsPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

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

    public LegacyHeadConfig(FileConfiguration config, File saveLocation)
    {
        super(config, saveLocation, VERSION_KEY);
    }

    public LegacyHeadConfig(File configFile)
    {
        super(configFile, VERSION_KEY);
    }

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

    public void setHead(String headKey, ConfigurationSection head)
    {
        String path = String.format("%s.%s", HEADS_KEY, headKey);
        config.set(path, head);
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
