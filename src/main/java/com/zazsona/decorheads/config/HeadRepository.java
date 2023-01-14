package com.zazsona.decorheads.config;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.config.load.DropFilterLoader;
import com.zazsona.decorheads.config.load.HeadDropLoader;
import com.zazsona.decorheads.config.load.HeadLoader;
import com.zazsona.decorheads.config.load.HeadRecipeLoader;
import com.zazsona.decorheads.crafting.IMetaRecipe;
import com.zazsona.decorheads.crafting.MetaRecipeManager;
import com.zazsona.decorheads.headdata.Head;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.drops.drops.IDrop;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.regex.Pattern;

public class HeadRepository
{
    private static HeadLoader headLoader;
    private static HeadDropLoader dropLoader;
    private static DropFilterLoader filterLoader;
    private static HeadRecipeLoader recipeLoader;

    private static HashMap<String, IHead> loadedHeads = new HashMap<>();
    private static HashMap<String, IDrop> loadedDrops = new HashMap<>();
    private static HashMap<String, IMetaRecipe> loadedRecipes = new HashMap<>();

    /**
     * Additively loads heads, drops, and recipes from the config files
     * @param plugin the plugin context
     */
    public static void loadConfig(DecorHeadsPlugin plugin)
    {
        HeadConfig headConfig = plugin.getHeadConfig();
        HeadDropConfig dropConfig = plugin.getHeadDropConfig();
        HeadRecipeConfig recipeConfig = plugin.getHeadRecipeConfig();

        loadGroup(plugin, headConfig, dropConfig, recipeConfig);
    }

    /**
     * Additively loads heads, drops, and recipes from the provided yaml
     * @param plugin the plugin context
     * @param headConfig the heads to load
     * @param dropConfig the drops to load
     * @param recipeConfig the recipes to load
     */
    public static void loadGroup(Plugin plugin, HeadConfig headConfig, HeadDropConfig dropConfig, HeadRecipeConfig recipeConfig)
    {
        boolean headSuccess = false;
        boolean dropSuccess = false;
        boolean recipeSuccess = false;

        headSuccess = loadHeads(headConfig);
        if (headSuccess)
            dropSuccess = loadDrops(plugin, dropConfig);
        if (dropSuccess)
            recipeSuccess = loadRecipes(recipeConfig);
    }

    /**
     * Additively loads heads from the provided config
     * @param headConfig the heads to load
     */
    public static boolean loadHeads(HeadConfig headConfig)
    {
        try
        {
            if (headLoader == null)
                headLoader = new HeadLoader();

            HashMap<String, IHead> heads = headLoader.loadHeads(headConfig);
            loadedHeads.putAll(heads);
            return true;
        }
        catch (Exception e)
        {
            Bukkit.getLogger().severe(String.format("[%s] Error when loading heads: %s", DecorHeadsPlugin.PLUGIN_NAME, e.getMessage()));
            return false;
        }
    }

    /**
     * Additively loads drops from the provided yaml
     * @param plugin the plugin context for listeners
     * @param dropConfig the drops to load
     */
    public static boolean loadDrops(Plugin plugin, HeadDropConfig dropConfig)
    {
        try
        {
            if (dropLoader == null)
                dropLoader = new HeadDropLoader();
            if (filterLoader == null)
                filterLoader = new DropFilterLoader();

            HashMap<String, IDrop> drops = dropLoader.loadDrops(dropConfig, loadedHeads, filterLoader);

            for (IDrop drop : drops.values())
            {
                if (drop instanceof Listener)
                {
                    Listener dropListener = (Listener) drop;
                    plugin.getServer().getPluginManager().registerEvents(dropListener, plugin);
                }
            }

            loadedDrops.putAll(drops);
            return true;
        }
        catch (Exception e)
        {
            Bukkit.getLogger().severe(String.format("[%s] Error when loading drops: %s", DecorHeadsPlugin.PLUGIN_NAME, e.getMessage()));
            return false;
        }
    }

    /**
     * Additively loads recipes from the provided yaml
     * @param recipeConfig the recipes to load
     */
    public static boolean loadRecipes(HeadRecipeConfig recipeConfig)
    {
        try
        {
            if (recipeLoader == null)
                recipeLoader = new HeadRecipeLoader();

            HashMap<String, IMetaRecipe> recipes = recipeLoader.loadRecipes(recipeConfig, loadedHeads);

            for (IMetaRecipe recipe : recipes.values())
                MetaRecipeManager.addRecipe(recipe);

            loadedRecipes.putAll(recipes);
            return true;
        }
        catch (Exception e)
        {
            Bukkit.getLogger().severe(String.format("[%s] Error when loading recipes: %s", DecorHeadsPlugin.PLUGIN_NAME, e.getMessage()));
            return false;
        }
    }

    /**
     * Additively loads a head from the provided yaml
     * @param headYaml the head to load
     */
    public static boolean loadHead(String key, ConfigurationSection headYaml)
    {
        try
        {
            if (headLoader == null)
                headLoader = new HeadLoader();

            IHead head = headLoader.loadHead(key, headYaml);
            loadedHeads.put(head.getKey(), head);
            return true;
        }
        catch (Exception e)
        {
            Bukkit.getLogger().severe(String.format("[%s] Error when loading head: %s", DecorHeadsPlugin.PLUGIN_NAME, e.getMessage()));
            return false;
        }
    }

    /**
     * Additively loads a drop from the provided yaml
     * @param plugin the plugin context for listeners
     * @param dropYaml the drop to load
     */
    public static boolean loadDrop(Plugin plugin, String key, ConfigurationSection dropYaml)
    {
        try
        {
            if (dropLoader == null)
                dropLoader = new HeadDropLoader();
            if (filterLoader == null)
                filterLoader = new DropFilterLoader();

            IDrop drop = dropLoader.loadDrop(key, dropYaml, loadedHeads, filterLoader);
            if (drop instanceof Listener)
            {
                Listener dropListener = (Listener) drop;
                plugin.getServer().getPluginManager().registerEvents(dropListener, plugin);
            }
            loadedDrops.put(drop.getKey(), drop);
            return true;
        }
        catch (Exception e)
        {
            Bukkit.getLogger().severe(String.format("[%s] Error when loading drop: %s", DecorHeadsPlugin.PLUGIN_NAME, e.getMessage()));
            return false;
        }
    }

    /**
     * Additively loads a recipe from the provided yaml
     * @param recipeYaml the recipe to load
     */
    public static boolean loadRecipe(String key, ConfigurationSection recipeYaml)
    {
        try
        {
            if (recipeLoader == null)
                recipeLoader = new HeadRecipeLoader();

            IMetaRecipe recipe = recipeLoader.loadRecipe(key, recipeYaml, loadedHeads);
            MetaRecipeManager.addRecipe(recipe);
            loadedRecipes.put(recipe.getKey().getKey(), recipe);
            return true;
        }
        catch (Exception e)
        {
            Bukkit.getLogger().severe(String.format("[%s] Error when loading recipes: %s", DecorHeadsPlugin.PLUGIN_NAME, e.getMessage()));
            return false;
        }
    }

    /**
     * Additively loads a head
     * @param head the head to load
     */
    public static boolean loadHead(IHead head)
    {
        loadedHeads.put(head.getKey(), head);
        return true;
    }

    /**
     * Additively loads a drop
     * @param plugin the plugin context for listeners
     * @param drop the drop to load
     */
    public static boolean loadDrop(Plugin plugin, IDrop drop)
    {
        if (drop instanceof Listener)
        {
            Listener dropListener = (Listener) drop;
            plugin.getServer().getPluginManager().registerEvents(dropListener, plugin);
        }
        loadedDrops.put(drop.getKey(), drop);
        return true;
    }

    /**
     * Additively loads a recipe
     * @param recipe the recipe to load
     */
    public static boolean loadRecipe(IMetaRecipe recipe)
    {
        MetaRecipeManager.addRecipe(recipe);
        loadedRecipes.put(recipe.getKey().getKey(), recipe);
        return true;
    }

    /**
     * Unloads all heads.
     */
    public static void unloadHeads()
    {
        List<String> headKeys = new ArrayList<>(loadedHeads.keySet());
        for (String key : headKeys)
            unloadHead(key);
    }

    /**
     * Unloads all drops.
     */
    public static void unloadDrops()
    {
        List<String> dropKeys = new ArrayList<>(loadedDrops.keySet());
        for (String key : dropKeys)
            unloadDrop(key);
    }

    /**
     * Unloads all recipes.
     */
    public static void unloadRecipes()
    {
        List<String> recipeKeys = new ArrayList<>(loadedRecipes.keySet());
        for (String key : recipeKeys)
            unloadRecipe(key);
    }

    /**
     * Unloads a head.
     * @param headKey the key of the head to unload
     * @return the unloaded head, or null
     */
    public static IHead unloadHead(String headKey)
    {
        IHead head = loadedHeads.remove(headKey);
        return head;
    }

    /**
     * Unloads a drop and unsubscribes its listeners.
     * @param dropKey the key of the drop to unload
     * @return the unloaded drop, or null
     */
    public static IDrop unloadDrop(String dropKey)
    {
        IDrop drop = loadedDrops.remove(dropKey);
        if (drop != null && drop instanceof Listener)
        {
            Listener listener = (Listener) drop;
            HandlerList.unregisterAll(listener);
        }
        return drop;
    }

    /**
     * Unloads a recipe and makes it uncraftable.
     * @param recipeKey the key of the recipe to unload
     * @return the unloaded recipe, or null
     */
    public static IMetaRecipe unloadRecipe(String recipeKey)
    {
        IMetaRecipe recipe = loadedRecipes.remove(recipeKey);
        if (recipe != null)
            MetaRecipeManager.removeRecipe(recipe.getKey());
        return recipe;
    }

    /**
     * Gets a map of loaded heads and their keys
     * @return a key:IHead map
     */
    public static Map<String, IHead> getLoadedHeads()
    {
        Map<String, IHead> heads = Collections.unmodifiableMap(loadedHeads);
        return heads;
    }

    /**
     * Gets a map of loaded drops and their keys
     * @return a key:IDrop map
     */
    public static Map<String, IDrop> getLoadedDrops()
    {
        Map<String, IDrop> drops = Collections.unmodifiableMap(loadedDrops);
        return drops;
    }

    /**
     * Gets a map of loaded recipes and their keys
     * @return a key:IMetaRecipe map
     */
    public static Map<String, IMetaRecipe> getLoadedRecipes()
    {
        Map<String, IMetaRecipe> recipes = Collections.unmodifiableMap(loadedRecipes);
        return recipes;
    }

    /**
     * Gets a loaded head
     * @param key the head's key (case-sensitive)
     * @return the head, or null if no match is found.
     */
    public static IHead getLoadedHead(String key)
    {
        return loadedHeads.get(key);
    }

    /**
     * Gets a loaded head.<br>
     * This first attempts to match by the head key (case-insensitive)<br>
     * If that fails, then a match is attempting the head's name (case-insensitive).
     * For heads that utilise the %PlayerName% variable in their names, matches will be made provided that the stand-in
     * value satisfies the following RegExp: [a-zA-Z0-9_]{2,16}
     * @param headIdentifier the key/name of the head
     * @return the matched head, or null if no match is found.
     */
    public static IHead matchLoadedHead(String headIdentifier)
    {
        // Direct key match...
        if (getLoadedHead(headIdentifier) != null)
            return getLoadedHead(headIdentifier);

        // Case-insensitive key match...
        for (String headKey : loadedHeads.keySet())
        {
            if (headKey.equalsIgnoreCase(headIdentifier))
                return getLoadedHead(headKey);
        }

        // Case-insensitive name match...
        String headIdLwr = headIdentifier.toLowerCase();
        String playerNamePlaceholderLwr = PlayerHead.PLAYER_NAME_PLACEHOLDER.toLowerCase();
        for (Map.Entry<String, IHead> loadedHead : loadedHeads.entrySet())
        {
            IHead head = loadedHead.getValue();
            String headNameLwr = head.getName().toLowerCase();

            if (headNameLwr.equals(headIdLwr))
                return head;
            if (headNameLwr.contains(playerNamePlaceholderLwr))
            {
                String matchPattern = "\\Q" + headNameLwr.replace(playerNamePlaceholderLwr, "\\E[a-z0-9_]{2,16}\\Q") + "\\E";
                matchPattern = matchPattern.replace("\\Q\\E", "");
                if (Pattern.matches(matchPattern, headIdLwr))
                    return head;
            }
        }
        return null;
    }

    /**
     * Gets a loaded drop
     * @param key the drop's key (case-sensitive)
     * @return the drop, or null if no match is found.
     */
    public static IDrop getLoadedDrop(String key)
    {
        return loadedDrops.get(key);
    }

    /**
     * Gets a loaded recipe
     * @param key the recipe's key (case-sensitive)
     * @return the recipe, or null if no match is found.
     */
    public static IMetaRecipe getLoadedRecipe(String key)
    {
        return loadedRecipes.get(key);
    }

    /**
     * Gets all loaded drops that result in the provided head
     * @param headKey the key of the head to get drops for
     * @return a list of drops for finding the head
     */
    public static List<IDrop> getDropsFor(String headKey)
    {
        List<IDrop> headDrops = new ArrayList<>();
        for (IDrop drop : loadedDrops.values())
        {
            ItemStack result = drop.getResult(null);
            String resultHeadId = result.getItemMeta().getPersistentDataContainer().get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
            if (resultHeadId != null && resultHeadId.equals(headKey))
                headDrops.add(drop);
        }
        return headDrops;
    }

    /**
     * Gets all loaded recipes that result in the provided head
     * @param headKey the key of the head to get recipes for
     * @return a list of recipes for crafting the head
     */
    public static List<IMetaRecipe> getRecipesFor(String headKey)
    {
        List<IMetaRecipe> headRecipes = new ArrayList<>();
        for (IMetaRecipe recipe : loadedRecipes.values())
        {
            ItemStack result = recipe.getResult();
            String resultHeadId = result.getItemMeta().getPersistentDataContainer().get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
            if (resultHeadId != null && resultHeadId.equals(headKey))
                headRecipes.add(recipe);
        }
        return headRecipes;
    }
}
