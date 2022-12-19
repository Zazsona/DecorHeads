package com.zazsona.decorheads.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.regex.Pattern;

public abstract class VersionedYamlConfigWrapper extends YamlConfigWrapper
{
    public static final String VERSION_PATTERN = "[0-9]+[.][0-9]+[.][0-9]+";
    public static final String MIN_CONFIG_VERSION = "0.0.0";
    public static final String MAX_CONFIG_VERSION = "9999.9999.9999";
    private String versionPath;

    public VersionedYamlConfigWrapper(FileConfiguration config, File saveLocation, String versionPath)
    {
        super(config, saveLocation);
        this.versionPath = versionPath;
    }

    public VersionedYamlConfigWrapper(File configFile, String versionPath)
    {
        super(configFile);
        this.versionPath = versionPath;
    }

    /**
     * Gets the version of the config
     * @return the version, following semantic versioning.
     */
    public String getVersion()
    {
        return config.getString(versionPath);
    }

    /**
     * Sets the version of the config
     * @param major major version
     * @param minor minor version
     * @param patch patch version
     */
    public void setVersion(int major, int minor, int patch)
    {
        String version = String.format("%d.%d.%d", major, minor, patch);
        config.set(versionPath, version);
    }

    /**
     * Sets the version of the config
     * @param version version string following semantic versioning (X.Y.Z)
     * @throws IllegalArgumentException - Provided version does not follow the X.Y.Z convention
     */
    public void setVersion(String version)
    {
        if (!Pattern.matches(VERSION_PATTERN, version))
            throw new IllegalArgumentException("Version must follow semantic versioning pattern. (X.Y.Z)");

        config.set(versionPath, version);
    }

    @Override
    protected FileConfiguration getEmptyConfigData()
    {
        FileConfiguration emptyConfig = super.getEmptyConfigData();
        emptyConfig.set(versionPath, MIN_CONFIG_VERSION);
        return emptyConfig;
    }

    @Override
    protected boolean validateConfigData(FileConfiguration configData)
    {
        if (!super.validateConfigData(configData))
            return false;

        String version = configData.getString(versionPath);
        boolean hasValidVersion = (version != null) && Pattern.matches(VERSION_PATTERN, version);
        return hasValidVersion;
    }
}
