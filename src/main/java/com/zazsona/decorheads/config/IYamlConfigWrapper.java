package com.zazsona.decorheads.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public interface IYamlConfigWrapper
{
    /**
     * Gets the underlying configuration behind this wrapper
     * @return the wrapped {@link FileConfiguration}
     */
    FileConfiguration getConfigData();

    /**
     * Sets the underlying configuration behind this wrapper
     * @param config the {@link FileConfiguration} to wrap
     */
    void setConfigData(FileConfiguration config);

    /**
     * Gets the file this config is saved to by default
     * @return a file donating the save location
     */
    File getSaveLocation();

    /**
     * Sets the file this config is saved to by default
     * @param saveLocation a file donating the save location
     */
    void setSaveLocation(File saveLocation);

    /**
     * Saves this config to the default save location
     */
    void save() throws IOException;

    /**
     * Saves this config to the provided save location
     * @param file the location to save to
     */
    void save(File file) throws IOException;

    /**
     * Reloads the config from its last saved state, discarding working changes
     */
    void reloadConfig();
}
