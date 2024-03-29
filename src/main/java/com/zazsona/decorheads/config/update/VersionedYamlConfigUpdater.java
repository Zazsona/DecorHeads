package com.zazsona.decorheads.config.update;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.config.VersionedYamlConfigWrapper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

public abstract class VersionedYamlConfigUpdater<T extends VersionedYamlConfigWrapper>
{
    protected String baseFileName;
    protected String updatesResourcesDirectory;

    /**
     * Creates an instance of an updater, utilising update files found in updatesResourcesDirectory,
     * and using baseFileName as a reference
     * @param updatesResourcesDirectory the directory where update files are found
     * @param baseFileName the name of the file which represents a version 0.0.0, from which all updates are based
     */
    public VersionedYamlConfigUpdater(String updatesResourcesDirectory, String baseFileName)
    {
        this.updatesResourcesDirectory = updatesResourcesDirectory;
        this.baseFileName = baseFileName;
    }

    protected T updateToVersion(T configWrapper, String targetVersion) throws IOException, InstantiationException
    {
        try
        {
            // Validate Inputs
            if (!Pattern.matches(VersionedYamlConfigWrapper.VERSION_PATTERN, targetVersion))
                throw new IllegalArgumentException("Version must follow semantic versioning pattern. (X.Y.Z)");

            // Check if config is already on targetVersion or greater
            FileConfiguration configYaml = configWrapper.getConfigData();
            String configVersion = configWrapper.getVersion();
            if (configVersion.compareTo(targetVersion) >= 0)
                return configWrapper;

            // Get Default Config and match the provided Config's version
            T baseConfigWrapper = getBaseConfig((Class<T>) configWrapper.getClass());
            FileConfiguration baseYaml = baseConfigWrapper.getConfigData();
            String baseVersion = baseConfigWrapper.getVersion();
            if (configVersion.compareTo(baseVersion) >= 0)
            {
                List<Path> changeFiles = getChangeFiles(baseVersion, configVersion);
                if (changeFiles.size() > 0)
                {
                    for (Path changeFile : changeFiles)
                    {
                        InputStreamReader changeStreamReader = new InputStreamReader(DecorHeadsPlugin.getInstance().getResource(changeFile.toString()));
                        FileConfiguration changeYaml = YamlConfiguration.loadConfiguration(changeStreamReader);
                        for (String key : changeYaml.getKeys(false))
                            updateYamlValue(key, changeYaml, baseYaml, null); // Set default to current update
                    }
                }
            }

            // Update the Config, making sure the Default Config always matches the version for referencing values
            List<Path> changeFiles = getChangeFiles(configVersion, targetVersion);
            if (changeFiles.size() > 0)
            {
                for (Path changeFile : changeFiles)
                {
                    InputStreamReader changeStreamReader = new InputStreamReader(DecorHeadsPlugin.getInstance().getResource(changeFile.toString()));
                    FileConfiguration changeYaml = YamlConfiguration.loadConfiguration(changeStreamReader);
                    for (String key : changeYaml.getKeys(false))
                        updateYamlValue(key, changeYaml, configYaml, baseYaml);
                    for (String key : changeYaml.getKeys(false))
                        updateYamlValue(key, changeYaml, baseYaml, null); // Set default to current update
                }
            }

            return configWrapper;
        }
        catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e)
        {
            throw new InstantiationException("Unable to create a wrapper around the Base Config, as no valid constructor was found.\nPlease override getBaseConfig() with a correct implementation.");
        }
    }

    protected T getBaseConfig(Class<T> configClass) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException
    {
        InputStream is = getClass().getClassLoader().getResourceAsStream(updatesResourcesDirectory+"/"+baseFileName);
        InputStreamReader isr = new InputStreamReader(is);
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(isr);
        Constructor<T> ctor = configClass.getConstructor(FileConfiguration.class, File.class);
        return ctor.newInstance(fileConfig, null);
    }

    /**
     * Gets the change files for updating the default configuration for heads, from minVersion to maxVersion
     * @param startVersion the minimum update version to get change files for. If trying to update a file, use the current version of that file
     * @param targetVersion the highest update version to get change files for.
     * @return the update change files resource path(s), in version ascending order
     * @throws IOException unable to find resources
     */
    protected List<Path> getChangeFiles(String startVersion, String targetVersion) throws IOException
    {
        try
        {
            ArrayList<Path> updateFilePaths = new ArrayList<>();
            final URI uri = this.getClass().getClassLoader().getResource(updatesResourcesDirectory).toURI();
            final FileSystem changelogsFileSys = FileSystems.newFileSystem(uri, Collections.emptyMap());
            Files.walk(changelogsFileSys.getPath("/"+ updatesResourcesDirectory +"/"))
                    .filter(path -> !Files.isDirectory(path))
                    .filter(path -> Pattern.matches(VersionedYamlConfigWrapper.VERSION_PATTERN+"\\Q.yml\\E", path.getFileName().toString()))
                    .filter(path -> getChangeFileVersion(path).compareTo(startVersion) >= 1 && getChangeFileVersion(path).compareTo(targetVersion) <= 0)
                    .forEach(path -> updateFilePaths.add(path));
            changelogsFileSys.close();
            updateFilePaths.sort(Comparator.naturalOrder());
            return updateFilePaths;
        }
        catch (Exception e)
        {
            throw new IOException(String.format("Unable to process update(s) in \"%s\". ", updatesResourcesDirectory), e);
        }
    }

    /**
     * Gets ALL the change files for updating the default configuration for heads
     * @return the update change files resource path(s), in version ascending order
     * @throws IOException unable to find resources
     */
    protected List<Path> getChangeFiles() throws IOException
    {
        return getChangeFiles(VersionedYamlConfigWrapper.getMinConfigVersion(), VersionedYamlConfigWrapper.getMaxConfigVersion());
    }

    /**
     * Extracts the version from the name of a change file.
     * @param changeFile a change file, with a name matching the pattern X.Y.Z.abc
     * @return the version
     */
    protected String getChangeFileVersion(Path changeFile)
    {
        String changeFileName = changeFile.getFileName().toString();
        String changeVersion = changeFileName.substring(0, changeFileName.lastIndexOf("."));
        return changeVersion;
    }

    protected void updateYamlValue(String path, FileConfiguration updateYaml, FileConfiguration updateTargetYaml, @Nullable FileConfiguration referenceYaml)
    {
        if (!updateTargetYaml.contains(path))
        {
            if (updateYaml.isConfigurationSection(path))
                updateTargetYaml.createSection(path, updateYaml.getConfigurationSection(path).getValues(true));
            else
                updateTargetYaml.set(path, updateYaml.get(path));
            return;
        }

        if (updateTargetYaml.contains(path) && !updateYaml.isConfigurationSection(path))
        {
            if (referenceYaml != null && !updateTargetYaml.get(path).equals(referenceYaml.get(path)))
                return;

            updateTargetYaml.set(path, updateYaml.get(path));
            return;
        }

        if (updateTargetYaml.contains(path) && updateYaml.isConfigurationSection(path))
        {
            ConfigurationSection configurationSection = updateYaml.getConfigurationSection(path);
            Set<String> keys = configurationSection.getKeys(false);
            for (String key : keys)
            {
                String subPath = configurationSection.getCurrentPath()+"."+key;
                updateYamlValue(subPath, updateYaml, updateTargetYaml, referenceYaml);
            }
            return;
        }
    }
}
