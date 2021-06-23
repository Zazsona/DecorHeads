package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headsources.IHeadSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.*;

public class HeadUpdater extends HeadConfigAccessor
{
    private static HeadUpdater instance;

    public static HeadUpdater getInstance()
    {
        if (instance == null)
            instance = new HeadUpdater();
        return instance;
    }

    private HeadUpdater()
    {
        //private constructor
    }

    public boolean updateHeadsFile() throws IOException
    {
        try
        {
            File headsFile = getHeadsFile();
            if (!headsFile.exists())
                createHeadsFile(headsFile);

            YamlConfiguration headsYaml = YamlConfiguration.loadConfiguration(headsFile);
            String fileVersion = getFileVersion(headsYaml);
            String pluginVersion = Core.getPlugin(Core.class).getDescription().getVersion();

            String[] updates = getUpdates(fileVersion, pluginVersion);
            if (updates.length > 0)
            {
                Bukkit.getLogger().info(String.format("[%s] Heads config for %s found. Updating to %s...", Core.PLUGIN_NAME, fileVersion, pluginVersion));
                YamlConfiguration defaultYaml = buildDefaultHeadsConfig(fileVersion);
                for (int i = 0; i < updates.length; i++)
                {
                    YamlConfiguration updateYaml = YamlConfiguration.loadConfiguration(new InputStreamReader(getChangelog(updates[i])));
                    for (String key : updateYaml.getKeys(false))
                        updateKeyValue(key, updateYaml, headsYaml, defaultYaml);
                    for (String key : updateYaml.getKeys(false))
                        updateKeyValue(key, updateYaml, defaultYaml, null); //Set default to current update
                }
                headsYaml.save(headsFile);
                Bukkit.getLogger().info(String.format("[%s] Heads config successfully updated to %s!", Core.PLUGIN_NAME, pluginVersion));
                return true;
            }
            else
                return false;
        }
        catch (IOException e)
        {
            Bukkit.getLogger().severe(String.format("[%s] Unable to access heads config: ", Core.PLUGIN_NAME));
            e.printStackTrace();
            throw e;
        }
    }

    private String getFileVersion(ConfigurationSection headsYaml)
    {
        if (headsYaml.contains(versionKey))
        {
            String version = headsYaml.getString(versionKey);
            return version;
        }
        return null;
    }

    private String[] getUpdates(String currentVersion, String targetVersion) throws IOException
    {
        String[] changelogs = getChangelogs();
        ArrayList<String> changelogsToApply = new ArrayList<>();
        for (int i = 0; i < changelogs.length; i++)
        {
            String changelogVersion = changelogs[i];
            if (changelogVersion.compareTo(currentVersion) >= 1 && changelogVersion.compareTo(targetVersion) <= 0)
                changelogsToApply.add(changelogVersion);
        }
        return changelogsToApply.toArray(new String[0]);
    }

    private String[] getChangelogs() throws IOException
    {
        try
        {
            ArrayList<String> changelogs = new ArrayList<>();
            final URI uri = this.getClass().getClassLoader().getResource(headsChangelogDirName).toURI();
            final FileSystem changelogsFileSys = FileSystems.newFileSystem(uri, Collections.emptyMap());
            Files.walk(changelogsFileSys.getPath("/"+ headsChangelogDirName+"/")).forEach(path ->
                                                                              {
                                                                                  String fileNameWithExtension = path.getFileName().toString();
                                                                                  if (!Files.isDirectory(path) && !fileNameWithExtension.equals(baseHeadsFileName))
                                                                                  {
                                                                                      String extension = fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf("."));
                                                                                      String fileName = fileNameWithExtension.replace(extension, "");
                                                                                      changelogs.add(fileName);
                                                                                  }
                                                                              });
            changelogsFileSys.close();
            changelogs.sort(Comparator.naturalOrder());
            return changelogs.toArray(new String[0]);
        }
        catch (Exception e)
        {
            throw new IOException("Unable to get changelogs", e);
        }
    }

    private YamlConfiguration buildDefaultHeadsConfig(String targetVersion) throws IOException
    {
        String[] updates = getUpdates("0", targetVersion);
        YamlConfiguration headsYaml = YamlConfiguration.loadConfiguration(new InputStreamReader(getBaseHeadsStream()));
        for (int i = 0; i < updates.length; i++)
        {
            YamlConfiguration updateYaml = YamlConfiguration.loadConfiguration(new InputStreamReader(getChangelog(updates[i])));
            updateKeyValue(updateYaml.getRoot().getCurrentPath(), updateYaml, headsYaml, null); //Recursively updates entries
        }
        return headsYaml;
    }

    private void updateKeyValue(String path, YamlConfiguration sourceYaml, YamlConfiguration targetYaml, @Nullable YamlConfiguration defaultYaml)
    {
        if (!targetYaml.contains(path))
        {
            if (sourceYaml.isConfigurationSection(path))
                targetYaml.createSection(path, sourceYaml.getConfigurationSection(path).getValues(true));
            else
                targetYaml.set(path, sourceYaml.get(path));
        }
        else if (targetYaml.contains(path) && !sourceYaml.isConfigurationSection(path)
                && ((defaultYaml == null) || (defaultYaml != null && targetYaml.get(path).equals(defaultYaml.get(path)))))
        {
            /* TL;DR if this is a terminating value AND the config already has an entry:
                1. If defaultYaml is null, adopt the update
                2. If defaultYaml exists, only adopt the update if the targetYaml value matches, thus retaining any custom user values
            */
            targetYaml.set(path, sourceYaml.get(path));
            //TODO: If multiple drop configs per-head are added, iterate through these to find default and custom list entries
        }
        else if (targetYaml.contains(path) && sourceYaml.isConfigurationSection(path))
        {
            ConfigurationSection configurationSection = sourceYaml.getConfigurationSection(path);
            Set<String> keys = configurationSection.getKeys(false);
            for (String key : keys)
            {
                String subPath = configurationSection.getCurrentPath()+"."+key;
                updateKeyValue(subPath, sourceYaml, targetYaml, defaultYaml);
            }
        }
    }

    private void createHeadsFile(File headsFile) throws IOException
    {
        Bukkit.getLogger().info(String.format("[%s] Creating new heads config...", Core.PLUGIN_NAME));
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(headsFileName);
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        headsFile.createNewFile();
        Files.write(headsFile.toPath(), buffer, StandardOpenOption.WRITE);
        Bukkit.getLogger().info(String.format("[%s] Created heads config!", Core.PLUGIN_NAME));
    }
}
