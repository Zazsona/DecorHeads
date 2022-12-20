package com.zazsona.decorheads.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Set;

public class HeadDropConfig extends VersionedYamlConfigWrapper
{
    public static final String VERSION_KEY = "version";
    public static final String DROPS_KEY = "drops";
    public static final String DROP_TYPE_KEY = "drop-type";
    public static final String DROP_RESULT_KEY = "result";
    public static final String DROP_RATE_KEY = "drop-rate";

    public HeadDropConfig(FileConfiguration config, File saveLocation)
    {
        super(config, saveLocation, VERSION_KEY);
    }

    public HeadDropConfig(File configFile)
    {
        super(configFile, VERSION_KEY);
    }

    public ConfigurationSection getDrops()
    {
        return config.getConfigurationSection(DROPS_KEY);
    }

    public void setDrops(ConfigurationSection drops)
    {
        if (!config.isConfigurationSection(DROPS_KEY) || drops == null)
            config.createSection(DROPS_KEY);

        if (drops != null)
            config.set(DROPS_KEY, drops);
    }

    public Set<String> getDropKeys()
    {
        return getDrops().getKeys(false);
    }

    public ConfigurationSection getDrop(String dropKey)
    {
        return getDrops().getConfigurationSection(dropKey);
    }

    public void setDrop(String dropKey, ConfigurationSection drop)
    {
        String path = String.format("%s.%s", DROPS_KEY, drop);
        config.set(path, drop);
    }

    @Override
    protected FileConfiguration getEmptyConfigData()
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
