package com.zazsona.decorheads.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.regex.Pattern;

public abstract class VersionedYamlConfigWrapper extends YamlConfigWrapper
{
    public static final String VERSION_PATTERN = "[0-9]+[.][0-9]+[.][0-9]+";
    public static final String VERSION_KEY = "version";

    public VersionedYamlConfigWrapper(FileConfiguration config, File saveLocation)
    {
        super(config, saveLocation);
    }

    public VersionedYamlConfigWrapper(File configFile)
    {
        super(configFile);
    }

    /**
     * Gets the minimum valid version for this config
     * @return the minimum valid version
     */
    public static String getMinConfigVersion()
    {
        return "0.0.0";
    }

    /**
     * Gets the maximum valid version for this config (though this may not be released)
     * @return the maximum valid version
     */
    public static String getMaxConfigVersion()
    {
        return "9999.9999.9999";
    }

    /**
     * Gets the path to the value where the version is stored
     * @return the path
     */
    protected String getVersionPath()
    {
        return VERSION_KEY;
    }

    /**
     * Gets the version of the config
     * @return the version, following semantic versioning.
     */
    public String getVersion()
    {
        return config.getString(getVersionPath());
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
        config.set(getVersionPath(), version);
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

        config.set(getVersionPath(), version);
    }

    @Override
    protected FileConfiguration getEmptyConfigData()
    {
        FileConfiguration emptyConfig = super.getEmptyConfigData();
        emptyConfig.set(getVersionPath(), getMinConfigVersion());
        return emptyConfig;
    }

    @Override
    protected boolean validateConfigData(FileConfiguration configData)
    {
        if (!super.validateConfigData(configData))
            return false;

        String version = configData.getString(getVersionPath());
        boolean hasValidVersion = (version != null) && Pattern.matches(VERSION_PATTERN, version);
        return hasValidVersion;
    }
}
