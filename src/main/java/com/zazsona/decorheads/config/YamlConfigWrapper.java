package com.zazsona.decorheads.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class YamlConfigWrapper implements IYamlConfigWrapper
{
    protected FileConfiguration config;
    protected File saveLocation;

    /**
     * Creates a new wrapper around the provided config. If the provided config is null, an empty config will be generated
     * @param config the config to wrap
     * @param saveLocation the default save location
     * @throws IllegalArgumentException config is invalid for this wrapper
     */
    public YamlConfigWrapper(FileConfiguration config, File saveLocation)
    {
        if (config == null)
            config = getEmptyConfigData();

        this.config = config;
        this.saveLocation = saveLocation;
    }

    /**
     * Creates a new wrapper around the config in the file. If the file is null or does not exist, an empty config will be generated
     * @param configFile the file containing config data
     * @throws IllegalArgumentException config is invalid for this wrapper
     */
    public YamlConfigWrapper(File configFile)
    {
        FileConfiguration configContents = loadConfigFromFile(configFile);
        this.config = configContents;
        this.saveLocation = configFile;
    }

    public FileConfiguration getConfigData()
    {
        return config;
    }

    public void setConfigData(FileConfiguration config)
    {
        this.config = config;
    }

    public File getSaveLocation()
    {
        return saveLocation;
    }

    public void setSaveLocation(File saveLocation)
    {
        this.saveLocation = saveLocation;
    }

    public void save() throws IOException
    {
        save(saveLocation);
    }

    public void save(File file) throws IOException
    {
        if (!file.exists())
        {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        config.save(file);
    }

    public void reloadConfig()
    {
        FileConfiguration configContents = loadConfigFromFile(saveLocation);
        config = configContents;
    }

    public boolean validateConfigData()
    {
        return validateConfigData(this.config);
    }

    /**
     * Gets an empty config supported by this wrapper
     * @return an empty, but valid, config
     */
    protected FileConfiguration getEmptyConfigData()
    {
        return new YamlConfiguration();
    }

    /**
     * Validates the config is compatible with this wrapper
     * @param configData the underlying config
     * @return boolean on validity
     */
    protected boolean validateConfigData(FileConfiguration configData)
    {
        return (configData != null);
    }

    /**
     * Attempts to load configuration data from the provided file.
     * If the file is null or does not exist, an empty config will be returned instead.
     * @param configFile the file to load from.
     * @return a configuration
     */
    protected FileConfiguration loadConfigFromFile(File configFile)
    {
        FileConfiguration configContents;
        if (configFile != null && configFile.exists())
            configContents = YamlConfiguration.loadConfiguration(configFile);
        else
            configContents = getEmptyConfigData();

        return configContents;
    }
}
