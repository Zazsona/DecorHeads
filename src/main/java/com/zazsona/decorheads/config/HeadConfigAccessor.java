package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class HeadConfigAccessor
{
    protected final String headsFileName = "heads.yml";

    protected final String versionKey = "version";
    protected final String nameKey = "name";
    protected final String textureKey = "texture";
    protected final String dropRateKey = "default-drop-rate";
    protected final String dropBlocksKey = "drop-blocks";
    protected final String dropBlocksToolsKey = "drop-blocks-tools";
    protected final String dropBlocksBiomesKey = "drop-blocks-biomes";
    protected final String dropEntitiesKey = "drop-entities";
    protected final String dropEntitiesToolsKey = "drop-entities-tools";
    protected final String dropEntitiesBiomesKey = "drop-entities-biomes";
    protected final String craftIngredientsKey = "craft-ingredients";
    protected final String craftGridKey = "craft-grid";

    public File getHeadsFile()
    {
        Plugin plugin = Core.getPlugin(Core.class);
        File headsFile = new File(plugin.getDataFolder().getPath()+"/"+ headsFileName);
        return headsFile;
    }
}
