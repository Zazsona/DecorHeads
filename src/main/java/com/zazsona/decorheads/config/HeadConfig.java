package com.zazsona.decorheads.config;

import com.zazsona.decorheads.HeadType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nullable;
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
        super(config, saveLocation);
    }

    public HeadConfig(File configFile)
    {
        super(configFile);
    }

    public static String getMinConfigVersion()
    {
        return "3.0.0";
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
     * Gets the path to the value where a head's type is stored
     * @return the path
     */
    protected String getHeadTypePath(String headKey)
    {
        return String.format("%s.%s", getHeadPath(headKey), HEAD_TYPE_KEY);
    }

    /**
     * Gets the path to the value where a head's name is stored
     * @return the path
     */
    protected String getHeadNamePath(String headKey)
    {
        return String.format("%s.%s", getHeadPath(headKey), HEAD_NAME_KEY);
    }

    /**
     * Gets the path to the value where a head's texture is stored
     * @return the path
     */
    protected String getHeadTexturePath(String headKey)
    {
        return String.format("%s.%s", getHeadPath(headKey), HEAD_TEXTURE_KEY);
    }

    public ConfigurationSection getHeads()
    {
        return config.getConfigurationSection(getHeadsPath());
    }

    public void setHeads(ConfigurationSection heads)
    {
        if (!config.isConfigurationSection(getHeadsPath()) || heads == null)
            config.createSection(getHeadsPath());

        if (heads != null)
            config.set(getHeadsPath(), heads);
    }

    public Set<String> getHeadKeys()
    {
        return getHeads().getKeys(false);
    }

    public ConfigurationSection getHead(String headKey)
    {
        return getHeads().getConfigurationSection(getHeadPath(headKey));
    }

    public void addHead(String headKey, String headType, String headName, @Nullable String headTexture)
    {
        String path = getHeadPath(headKey);
        if (config.getConfigurationSection(path) != null)
            throw new IllegalArgumentException(String.format("Head with key \"%s\" already exists.", headKey));
        if (HeadType.matchHeadType(headType) == HeadType.DECOR && headTexture == null)
            throw new IllegalArgumentException(String.format("Creating a head of type \"%s\" for \"%s\" requires headTexture to be set.", headType, headKey));

        config.createSection(path);
        setHeadType(headKey, headType);
        setHeadName(headKey, headName);
        if (HeadType.matchHeadType(headType) == HeadType.DECOR)
            setHeadTexture(headKey, headTexture);
    }

    public void removeHead(String headKey)
    {
        config.set(getHeadPath(headKey), null);
    }

    public String getHeadName(String headKey)
    {
        return config.getString(getHeadNamePath(headKey));
    }

    public void setHeadName(String headKey, String headName)
    {
        config.set(getHeadNamePath(headKey), headName);
    }

    public String getHeadType(String headKey)
    {
        return config.getString(getHeadTypePath(headKey));
    }

    public void setHeadType(String headKey, String headType)
    {
        config.set(getHeadTypePath(headKey), headType);
    }

    public String getHeadTexture(String headKey)
    {
        return config.getString(getHeadTexturePath(headKey));
    }

    public void setHeadTexture(String headKey, String headTexture)
    {
        config.set(getHeadTexturePath(headKey), headTexture);
    }

    @Override
    protected FileConfiguration getEmptyConfigData()
    {
        FileConfiguration emptyConfig = super.getEmptyConfigData();
        emptyConfig.createSection(getHeadsPath());
        return emptyConfig;
    }

    @Override
    protected boolean validateConfigData(FileConfiguration configData)
    {
        if (!super.validateConfigData(configData))
            return false;

        boolean hasHeadsSection = configData.getConfigurationSection(getHeadsPath()) != null;
        return hasHeadsSection;
    }
}
