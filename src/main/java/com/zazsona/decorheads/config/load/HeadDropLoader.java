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
                IDrop drop = loadDrop(dropData, heads, filterLoader);
                if (loadedDrops.containsKey(drop.getKey()))
                    throw new IllegalArgumentException(String.format("Duplicate Drop Key: %s", drop.getKey()));
                else
                    loadedDrops.put(drop.getKey(), drop);
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
    public IDrop loadDrop(ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws IllegalArgumentException
    {
        try
        {
            IDrop drop = parseDrop(dropYaml, heads, filterLoader);
            return drop;
        }
        catch (Exception e)
        {
            String name = (dropYaml.getName() != null ? dropYaml.getString(dropYaml.getName()) : "[UNKNOWN]");
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
    private IDrop parseDrop(ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException
    {
        if (!dropYaml.contains(DROP_TYPE_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", DROP_TYPE_KEY), DROP_TYPE_KEY);

        String dropTypeName = dropYaml.getString(DROP_TYPE_KEY);
        DropType dropType = DropType.matchDropType(dropTypeName);

        IDrop drop;
        if (dropType == DropType.BLOCK_BREAK)
            drop = parseBreakDrop(dropYaml, heads, filterLoader);
        else if (dropType == DropType.BREW)
            drop = parseBrewDrop(dropYaml, heads, filterLoader);
        else if (dropType == DropType.CRAFT)
            drop = parseCraftDrop(dropYaml, heads, filterLoader);
        else if (dropType == DropType.SMELT)
            drop = parseSmeltDrop(dropYaml, heads, filterLoader);
        else if (dropType == DropType.ENTITY_DEATH)
            drop = parseEntityDeathDrop(dropYaml, heads, filterLoader);
        else if (dropType == DropType.PLAYER_DEATH)
            drop = parsePlayerDeathDrop(dropYaml, heads, filterLoader);
        else
            throw new IllegalArgumentException(String.format("Invalid drop type: %s", dropTypeName));

        return drop;
    }

    /**
     * Parses drop YAML data and converts it into a {@link BlockBreakDrop}
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the drop
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parseBreakDrop(ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException
    {
        // Get parameters
        DropProperties dropProperties = parseBaseDropProperties(dropYaml, heads);
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
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the drop
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parseBrewDrop(ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException
    {
        // Get parameters
        DropProperties dropProperties = parseBaseDropProperties(dropYaml, heads);
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
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the drop
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parseCraftDrop(ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException
    {
        // Get parameters
        DropProperties dropProperties = parseBaseDropProperties(dropYaml, heads);
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
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the drop
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parseSmeltDrop(ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException
    {
        // Get parameters
        DropProperties dropProperties = parseBaseDropProperties(dropYaml, heads);
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
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the drop
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parseEntityDeathDrop(ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException
    {
        // Get parameters
        DropProperties dropProperties = parseBaseDropProperties(dropYaml, heads);
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
     * Parses drop YAML data and converts it into a {@link PlayerDeathDrop}
     * @param dropYaml the drop yaml
     * @param heads a key:head map of registered heads
     * @return the drop
     * @throws MissingFieldsException invalid drop data
     * @throws IllegalArgumentException invalid drop data
     */
    private IDrop parsePlayerDeathDrop(ConfigurationSection dropYaml, HashMap<String, IHead> heads, DropFilterLoader filterLoader) throws MissingFieldsException, IllegalArgumentException, InvalidKeyException
    {
        // Get parameters
        DropProperties dropProperties = parseBaseDropProperties(dropYaml, heads);
        List<IDropFilter> filters = filterLoader.loadDropFilters(dropYaml, heads);

        // Create the Drop
        PlayerDeathDrop drop;
        if (dropProperties.headResult != null)
            drop = new PlayerDeathDrop(dropProperties.key, dropProperties.dropRate, dropProperties.headResult, filters);
        else
            drop = new PlayerDeathDrop(dropProperties.key, dropProperties.dropRate, dropProperties.itemResult, filters);
        return drop;
    }

    /**
     * Build the common parameters for all {@link Drop} instances.
     * @param dropYaml the drop to parse
     * @param heads a key:head map of registered heads
     * @return a record containing parameter data, used for instantiating subclasses of {@link Drop}
     */
    private DropProperties parseBaseDropProperties(ConfigurationSection dropYaml, HashMap<String, IHead> heads) throws InvalidKeyException
    {
        // Check Keys
        if (dropYaml.getName() == null)
            throw new MissingFieldsException("Missing Key: Drop Key");
        if (!dropYaml.contains(DROP_RESULT_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", DROP_RESULT_KEY), DROP_RESULT_KEY);
        if (!dropYaml.contains(DROP_RATE_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", DROP_RATE_KEY), DROP_RATE_KEY);

        // Load from YAML
        String key = dropYaml.getName().toLowerCase();
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
