package com.zazsona.decorheads.config.load;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.DropType;
import com.zazsona.decorheads.config.HeadDropConfig;
import com.zazsona.decorheads.exceptions.MissingFieldsException;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.drops.drops.*;
import com.zazsona.decorheads.drops.drops.Drop;
import com.zazsona.decorheads.drops.drops.IDrop;
import com.zazsona.decorheads.drops.filters.IDropFilter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.zazsona.decorheads.config.HeadDropConfig.*;

public class HeadDropLoader
{
    /**
     * Loads all the drops in the provided data, provided it complies with the expected format.
     * @param dropConfig the drop config
     * @param heads a key:head map of registered heads
     * @return a key:drop map of loaded drops
     */
    public HashMap<String, IDrop> loadDrops(HeadDropConfig dropConfig, HashMap<String, IHead> heads, DropFilterLoader filterLoader)
    {
        ConfigurationSection drops = dropConfig.getDrops();
        Set<String> dropKeys = dropConfig.getDropKeys();

        HashMap<String, IDrop> loadedDrops = new HashMap<>();
        for (String dropKey : dropKeys)
        {
            try
            {
                ConfigurationSection dropData = drops.getConfigurationSection(dropKey);
                String dropKeyLower = dropKey.toLowerCase();
                IDrop drop = loadDrop(dropKeyLower, dropData, heads, filterLoader);
                if (loadedDrops.containsKey(dropKeyLower))
                    throw new IllegalArgumentException(String.format("Duplicate Drop Key: %s", dropKeyLower));
                else
                    loadedDrops.put(dropKeyLower, drop);
            }
            catch (Exception e)
            {
                Bukkit.getLogger().warning(String.format("[%s] %s", DecorHeadsPlugin.PLUGIN_NAME, e.getMessage()));
            }
        }
        return loadedDrops;
    }

    /**
     * Loads a specific drop.
     * @param dropYaml the drop data
     * @param heads a key:head map of registered heads
     * @return the loaded {@link IDrop}
     * @throws IllegalArgumentException drop failed to load
     */
    public IDrop loadDrop(String key, ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws IllegalArgumentException
    {
        try
        {
            IDrop drop = parseDrop(key, dropYaml, heads, filterLoader);
            return drop;
        }
        catch (Exception e)
        {
            String name = ((key != null) ? key : "[UNKNOWN]");
            throw new IllegalArgumentException(String.format("Unable to load drop \"%s\": %s", name, e.getMessage()));
        }
    }

    /**
     * Parses drop YAML data and converts it into a drop
     * @param dropYaml the drop to parse
     * @param heads a key:head map of registered heads
     * @return a {@link IDrop}
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parseDrop(String key, ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException, IOException
    {
        if (!dropYaml.contains(DROP_TYPE_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", DROP_TYPE_KEY), DROP_TYPE_KEY);

        String dropTypeName = dropYaml.getString(DROP_TYPE_KEY);
        DropType dropType = DropType.matchDropType(dropTypeName);

        IDrop drop;
        if (dropType == DropType.BLOCK_BREAK)
            drop = parseBreakDrop(key, dropYaml, heads, filterLoader);
        else if (dropType == DropType.BREW)
            drop = parseBrewDrop(key, dropYaml, heads, filterLoader);
        else if (dropType == DropType.CRAFT)
            drop = parseCraftDrop(key, dropYaml, heads, filterLoader);
        else if (dropType == DropType.SMELT)
            drop = parseSmeltDrop(key, dropYaml, heads, filterLoader);
        else if (dropType == DropType.ENTITY_DEATH)
            drop = parseEntityDeathDrop(key, dropYaml, heads, filterLoader);
        else
            throw new IllegalArgumentException(String.format("Invalid drop type: %s", dropTypeName));

        return drop;
    }

    /**
     * Parses drop YAML data and converts it into a {@link BlockBreakDrop}
     * @param key the key for the drop
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the drop
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parseBreakDrop(String key, ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException, IOException
    {
        // Get parameters
        DropProperties dropProperties = parseBaseDropProperties(key, dropYaml, heads);
        List<IDropFilter> filters = filterLoader.loadDropFilters(dropYaml, heads);

        // Create the Drop
        BlockBreakDrop drop;
        if (dropProperties.headResult != null)
            drop = new BlockBreakDrop(dropProperties.key, dropProperties.dropRate, dropProperties.headResult, filters);
        else
            drop = new BlockBreakDrop(dropProperties.key, dropProperties.dropRate, dropProperties.itemResult, filters);
        return drop;
    }

    /**
     * Parses drop YAML data and converts it into a {@link BrewDrop}
     * @param key the key for the drop
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the drop
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parseBrewDrop(String key, ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException, IOException
    {
        // Get parameters
        DropProperties dropProperties = parseBaseDropProperties(key, dropYaml, heads);
        List<IDropFilter> filters = filterLoader.loadDropFilters(dropYaml, heads);

        // Create the Drop
        BrewDrop drop;
        if (dropProperties.headResult != null)
            drop = new BrewDrop(dropProperties.key, dropProperties.dropRate, dropProperties.headResult, filters);
        else
            drop = new BrewDrop(dropProperties.key, dropProperties.dropRate, dropProperties.itemResult, filters);
        return drop;
    }

    /**
     * Parses drop YAML data and converts it into a {@link CraftDrop}
     * @param key the key for the drop
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the drop
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parseCraftDrop(String key, ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException, IOException
    {
        // Get parameters
        DropProperties dropProperties = parseBaseDropProperties(key, dropYaml, heads);
        List<IDropFilter> filters = filterLoader.loadDropFilters(dropYaml, heads);

        // Create the Drop
        CraftDrop drop;
        if (dropProperties.headResult != null)
            drop = new CraftDrop(dropProperties.key, dropProperties.dropRate, dropProperties.headResult, filters);
        else
            drop = new CraftDrop(dropProperties.key, dropProperties.dropRate, dropProperties.itemResult, filters);
        return drop;
    }

    /**
     * Parses drop YAML data and converts it into a {@link SmeltDrop}
     * @param key the key for the drop
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the drop
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parseSmeltDrop(String key, ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException, IOException
    {
        // Get parameters
        DropProperties dropProperties = parseBaseDropProperties(key, dropYaml, heads);
        List<IDropFilter> filters = filterLoader.loadDropFilters(dropYaml, heads);

        // Create the Drop
        SmeltDrop drop;
        if (dropProperties.headResult != null)
            drop = new SmeltDrop(dropProperties.key, dropProperties.dropRate, dropProperties.headResult, filters);
        else
            drop = new SmeltDrop(dropProperties.key, dropProperties.dropRate, dropProperties.itemResult, filters);
        return drop;
    }

    /**
     * Parses drop YAML data and converts it into a {@link EntityDeathDrop}
     * @param key the key for the drop
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the drop
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parseEntityDeathDrop(String key, ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException, IOException
    {
        // Get parameters
        DropProperties dropProperties = parseBaseDropProperties(key, dropYaml, heads);
        List<IDropFilter> filters = filterLoader.loadDropFilters(dropYaml, heads);

        // Create the Drop
        EntityDeathDrop drop;
        if (dropProperties.headResult != null)
            drop = new EntityDeathDrop(dropProperties.key, dropProperties.dropRate, dropProperties.headResult, filters);
        else
            drop = new EntityDeathDrop(dropProperties.key, dropProperties.dropRate, dropProperties.itemResult, filters);
        return drop;
    }

    /**
     * Build the common parameters for all {@link Drop} instances.
     * @param key the key for the drop
     * @param dropYaml the drop to parse
     * @param heads a key:head map of registered heads
     * @return a record containing parameter data, used for instantiating subclasses of {@link Drop}
     */
    private DropProperties parseBaseDropProperties(String key, ConfigurationSection dropYaml, HashMap<String, IHead> heads) throws InvalidKeyException, IOException
    {
        // Check Keys
        if (key == null)
            throw new MissingFieldsException("Missing Key: Drop Key");
        if (!dropYaml.contains(DROP_RESULT_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", DROP_RESULT_KEY), DROP_RESULT_KEY);
        if (!dropYaml.contains(DROP_RATE_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", DROP_RATE_KEY), DROP_RATE_KEY);

        // Load from YAML
        String resultName = dropYaml.getString(DROP_RESULT_KEY);
        double dropRate = dropYaml.getDouble(DROP_RATE_KEY);

        // Transform Data
        NamespacedKey resultKey = NamespacedKey.fromString(resultName);
        IHead headResult = null;
        ItemStack itemResult = null;
        if (resultKey.getNamespace().equalsIgnoreCase(DecorHeadsPlugin.PLUGIN_NAME))
            headResult = heads.get(resultKey.getKey());
        else
            itemResult = ItemLoader.loadItem(resultKey.getKey(), heads);

        if (headResult != null || itemResult != null)
            return new DropProperties(key, dropRate, headResult, itemResult);
        else
            throw new InvalidKeyException(String.format("\"%s\" does not map to a valid item.", resultKey));
    }

    private class DropProperties
    {
        public String key;
        public double dropRate;
        public IHead headResult;
        public ItemStack itemResult;

        public DropProperties(String key, double dropRate, IHead headResult, ItemStack itemResult)
        {
            this.key = key;
            this.dropRate = dropRate;
            this.headResult = headResult;
            this.itemResult = itemResult;
        }
    }
}
