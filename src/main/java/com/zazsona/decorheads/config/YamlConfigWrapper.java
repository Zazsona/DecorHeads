package com.zazsona.decorheads.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class YamlConfigWrapper implements IYamlConfigWrapper
{
    protected FileConfiguration config;
    protected File saveLocation;

    public YamlConfigWrapper(FileConfiguration config, File saveLocation)
    {
        this.config = config;
        this.saveLocation = saveLocation;
    }

    public YamlConfigWrapper(File configFile)
    {
        FileConfiguration configContents = YamlConfiguration.loadConfiguration(configFile);

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
        FileConfiguration configContents = YamlConfiguration.loadConfiguration(saveLocation);
        config = configContents;
    }
}
