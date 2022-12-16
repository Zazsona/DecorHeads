package com.zazsona.decorheads.config;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.exceptions.MissingFieldsException;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.drops.filters.BlockFilter;
import com.zazsona.decorheads.drops.filters.IDropFilter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;

import javax.management.openmbean.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class DropFilterLoader
{
    public static final String FILTER_BLOCKS_KEY = "blocks";
    public static final String FILTER_TOOLS_KEY = "tools";
    // TODO: Add all existing filters

    /**
     * Loads all the filters in the provided data, provided it complies with the expected format.
     * @param dropYaml the drop data
     * @param heads a key:head map of registered heads
     * @return a list of all loaded filters
     * @throws MissingFieldsException invalid YAML format
     */
    public ArrayList<IDropFilter> loadDropFilters(ConfigurationSection dropYaml, HashMap<String, IHead> heads) throws MissingFieldsException
    {
        ArrayList<IDropFilter> loadedFilters = new ArrayList<>();
        for (String yamlKey : dropYaml.getKeys(false))
        {
            try
            {
                switch (yamlKey.toLowerCase())
                {
                    case FILTER_BLOCKS_KEY:
                        loadedFilters.add(loadBlocksFilter(dropYaml, heads));
                        break;
                }
            }
            catch (Exception e)
            {
                Bukkit.getLogger().warning(String.format("[%s] %s", Core.PLUGIN_NAME, e.getMessage()));
            }
        }
        return loadedFilters;
    }

    /**
     * Loads block filter YAML field and converts it into a {@link IDropFilter}
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the filter
     */
    private IDropFilter loadBlocksFilter(ConfigurationSection dropYaml, HashMap<String, IHead> heads)
    {
        List<NamespacedKey> blockKeys = getBlockKeys(FILTER_BLOCKS_KEY, dropYaml, heads);
        return new BlockFilter(blockKeys);
    }

    /**
     * Converts a list of block names to NamespacedKeys
     * @param filterKey the filter's key
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return a list of block and head NamespacedKeys
     */
    private List<NamespacedKey> getBlockKeys(String filterKey, ConfigurationSection dropYaml, HashMap<String, IHead> heads)
    {
        List<NamespacedKey> blockKeys = new ArrayList<>();
        if (dropYaml.contains(filterKey))
        {
            List<String> blockNames = dropYaml.getStringList(filterKey);
            for (String blockName : blockNames)
            {
                NamespacedKey blockKey = NamespacedKey.fromString(blockName);
                String namespace = blockKey.getNamespace();
                String key = blockKey.getKey();
                if (namespace.equalsIgnoreCase(NamespacedKey.MINECRAFT) || namespace.equalsIgnoreCase(NamespacedKey.BUKKIT))
                {
                    Material material = Material.matchMaterial(key);
                    if (material == null)
                        throw new InvalidKeyException(String.format("Minecraft item key \"%s\" does not exist.", key));
                }
                else if (namespace.equalsIgnoreCase(Core.PLUGIN_NAME))
                {
                    IHead head = heads.get(key);
                    if (head == null)
                        throw new InvalidKeyException(String.format("%s item key \"%s\" does not exist.", Core.PLUGIN_NAME, key));
                }
                blockKeys.add(blockKey);
            }
        }
        return blockKeys;
    }
}
