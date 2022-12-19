package com.zazsona.decorheads.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.*;

public class HeadConfig extends VersionedYamlConfigWrapper
{
    public static final String VERSION_KEY = "version";
    public static final String HEADS_KEY = "heads";
    public static final String HEAD_TYPE_KEY = "head-type";
    public static final String HEAD_NAME_KEY = "name";
    public static final String HEAD_TEXTURE_KEY = "texture";

    public HeadConfig(FileConfiguration config, File saveLocation)
    {
        super(config, saveLocation, VERSION_KEY);
    }

    public HeadConfig(File configFile)
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
        FileConfiguration emptyConfig = super.getEmptyConfigData();
        emptyConfig.createSection(HEADS_KEY);
        return emptyConfig;
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
