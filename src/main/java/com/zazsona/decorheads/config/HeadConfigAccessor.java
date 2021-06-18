package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class HeadConfigAccessor
{
    public static final String headsFileName = "heads.yml";
    public static final String baseHeadsFileName = "base-heads.yml";
    public static final String headsChangelogDirName = "heads-changelog";
    public static final String changelogFileFormat = ".yml";

    public static final String versionKey = "version";
    public static final String headsKey = "heads";
    public static final String nameKey = "name";
    public static final String textureKey = "texture";
    public static final String dropRateKey = "default-drop-rate";
    public static final String dropBlocksKey = "drop-blocks";
    public static final String dropBlocksToolsKey = "drop-blocks-tools";
    public static final String dropBlocksBiomesKey = "drop-blocks-biomes";
    public static final String dropEntitiesKey = "drop-entities";
    public static final String dropEntitiesToolsKey = "drop-entities-tools";
    public static final String dropEntitiesBiomesKey = "drop-entities-biomes";
    public static final String craftIngredientsKey = "craft-ingredients";
    public static final String craftGridKey = "craft-grid";

    public File getHeadsFile()
    {
        Plugin plugin = Core.getPlugin(Core.class);
        File headsFile = new File(plugin.getDataFolder().getPath()+"/"+ headsFileName);
        return headsFile;
    }

    public InputStream getBaseHeadsStream() throws IOException
    {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(headsChangelogDirName+"/"+baseHeadsFileName);
        return inputStream;
    }

    public InputStream getChangelog(String version) throws IOException
    {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(headsChangelogDirName+"/"+version+changelogFileFormat);
        return inputStream;
    }
}
