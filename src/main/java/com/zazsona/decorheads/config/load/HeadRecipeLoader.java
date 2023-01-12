package com.zazsona.decorheads.config.load;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.config.HeadRecipeConfig;
import com.zazsona.decorheads.RecipeType;
import com.zazsona.decorheads.crafting.IMetaRecipe;
import com.zazsona.decorheads.crafting.ShapedMetaRecipe;
import com.zazsona.decorheads.crafting.ShapelessMetaRecipe;
import com.zazsona.decorheads.exceptions.MissingFieldsException;
import com.zazsona.decorheads.headdata.Head;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.*;

import static com.zazsona.decorheads.config.HeadRecipeConfig.*;

public class HeadRecipeLoader
{
    /**
     * Loads all the recipes in the provided data, provided it complies with the expected format.
     * @param recipeConfig the recipe config
     * @param heads a key:head map of registered heads
     * @return a key:recipe map of loaded recipes
     */
    public HashMap<String, IMetaRecipe> loadRecipes(HeadRecipeConfig recipeConfig, HashMap<String, IHead> heads)
    {
        ConfigurationSection recipes = recipeConfig.getRecipes();
        Set<String> recipeKeys = recipeConfig.getRecipeKeys();

        HashMap<String, IMetaRecipe> loadedRecipes = new HashMap<>();
        for (String recipeKey : recipeKeys)
        {
            try
            {
                ConfigurationSection recipeData = recipes.getConfigurationSection(recipeKey);
                String recipeKeyLower = recipeKey.toLowerCase();
                IMetaRecipe recipe = loadRecipe(recipeKeyLower, recipeData, heads);
                if (loadedRecipes.containsKey(recipeKeyLower))
                    throw new IllegalArgumentException(String.format("Duplicate Recipe Key: %s", recipeKeyLower));
                else
                    loadedRecipes.put(recipeKeyLower, recipe);
            }
            catch (Exception e)
            {
                Bukkit.getLogger().warning(String.format("[%s] %s", DecorHeadsPlugin.PLUGIN_NAME, e.getMessage()));
            }
        }
        return loadedRecipes;
    }

    /**
     * Loads a specific recipe.
     * @param key the key for the recipe
     * @param recipeYaml the recipe data
     * @param heads a key:head map of registered heads
     * @return the loaded {@link IMetaRecipe}
     * @throws IllegalArgumentException recipe failed to load
     */
    public IMetaRecipe loadRecipe(String key, ConfigurationSection recipeYaml, HashMap<String, IHead> heads) throws IllegalArgumentException
    {
        try
        {
            IMetaRecipe recipe = parseRecipe(key, recipeYaml, heads);
            return recipe;
        }
        catch (Exception e)
        {
            String name = ((key != null) ? key : "[UNKNOWN]");
            throw new IllegalArgumentException(String.format("Unable to load recipe \"%s\": %s", name, e.getMessage()));
        }
    }

    /**
     * Parses recipe YAML data and converts it into an IMetaRecipe implementation
     * @param key the key for the recipe
     * @param recipeYaml the recipe to parse
     * @param heads a key:head map of registered heads
     * @return an {@link IMetaRecipe}
     * @throws MissingFieldsException invalid recipe data
     * @throws IllegalArgumentException invalid recipe data
     */
    private IMetaRecipe parseRecipe(String key, ConfigurationSection recipeYaml, HashMap<String, IHead> heads) throws MissingFieldsException, IllegalArgumentException, IOException
    {
        if (!recipeYaml.contains(RECIPE_TYPE_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", RECIPE_TYPE_KEY), RECIPE_TYPE_KEY);

        String recipeTypeName = recipeYaml.getString(RECIPE_TYPE_KEY);
        RecipeType recipeType = RecipeType.matchRecipeType(recipeTypeName);

        if (recipeType == RecipeType.CRAFT && !recipeYaml.contains(RECIPE_GRID_KEY))
            return parseShapelessMetaRecipe(key, recipeYaml, heads);
        else if (recipeType == RecipeType.CRAFT && recipeYaml.contains(RECIPE_GRID_KEY))
            return parseShapedMetaRecipe(key, recipeYaml, heads);
        else
            throw new IllegalArgumentException(String.format("Invalid recipe type: %s", recipeTypeName));
    }

    /**
     * Parses recipe YAML data and converts it into a {@link ShapelessMetaRecipe}
     * @param key the key for the recipe
     * @param recipeYaml the recipe yaml
     * @param heads a key:head map of registered heads
     * @return the recipe
     * @throws MissingFieldsException invalid recipe data
     * @throws IllegalArgumentException invalid recipe data
     */
    private ShapelessMetaRecipe parseShapelessMetaRecipe(String key, ConfigurationSection recipeYaml, HashMap<String, IHead> heads) throws MissingFieldsException, IllegalArgumentException, IOException
    {
        // Check Keys
        if (key == null)
            throw new MissingFieldsException("Missing Key: Recipe Key");
        if (!recipeYaml.contains(RECIPE_TYPE_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", RECIPE_TYPE_KEY), RECIPE_TYPE_KEY);
        if (!recipeYaml.contains(RECIPE_INGREDIENTS_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", RECIPE_INGREDIENTS_KEY), RECIPE_INGREDIENTS_KEY);
        if (!recipeYaml.contains(RECIPE_RESULT_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", RECIPE_RESULT_KEY), RECIPE_RESULT_KEY);

        // Load from YAML
        String resultName = recipeYaml.getString(RECIPE_RESULT_KEY);
        ConfigurationSection ingredientsMap = recipeYaml.getConfigurationSection(RECIPE_INGREDIENTS_KEY);

        // Transform Data
        NamespacedKey recipeNamespacedKey = new NamespacedKey(DecorHeadsPlugin.getInstance(), key);
        ItemStack result = ItemLoader.loadItem(resultName, heads);
        HashMap<Character, ItemStack> ingredientStackMap = parseIngredientsMap(ingredientsMap, heads);

        // Create Recipe
        ShapelessMetaRecipe recipe = new ShapelessMetaRecipe(recipeNamespacedKey, result);
        recipe.setGroup(resultName);
        for (Map.Entry<Character, ItemStack> ingredientEntry : ingredientStackMap.entrySet())
        {
            String headKey = ingredientEntry.getValue().getItemMeta().getPersistentDataContainer().get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
            if (headKey == null)
                recipe.addIngredient(ingredientEntry.getValue());
            else
                recipe.addIngredient(ingredientEntry.getValue(), (ingredient, refIngredient) -> compareHeads(ingredient, refIngredient));
        }

        return recipe;
    }

    /**
     * Parses recipe YAML data and converts it into a {@link ShapedMetaRecipe}
     * @param key the key for the recipe
     * @param recipeYaml the recipe yaml
     * @param heads a key:head map of registered heads
     * @return the recipe
     * @throws MissingFieldsException invalid recipe data
     * @throws IllegalArgumentException invalid recipe data
     */
    private ShapedMetaRecipe parseShapedMetaRecipe(String key, ConfigurationSection recipeYaml, HashMap<String, IHead> heads) throws MissingFieldsException, IllegalArgumentException, IOException
    {
        // Check Keys
        if (key == null)
            throw new MissingFieldsException("Missing Key: Recipe Key");
        if (!recipeYaml.contains(RECIPE_TYPE_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", RECIPE_TYPE_KEY), RECIPE_TYPE_KEY);
        if (!recipeYaml.contains(RECIPE_INGREDIENTS_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", RECIPE_INGREDIENTS_KEY), RECIPE_INGREDIENTS_KEY);
        if (!recipeYaml.contains(RECIPE_RESULT_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", RECIPE_RESULT_KEY), RECIPE_RESULT_KEY);
        if (!recipeYaml.contains(RECIPE_GRID_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", RECIPE_GRID_KEY), RECIPE_GRID_KEY);

        // Load from YAML
        String resultName = recipeYaml.getString(RECIPE_RESULT_KEY);
        ConfigurationSection ingredientsMap = recipeYaml.getConfigurationSection(RECIPE_INGREDIENTS_KEY);

        // Transform Data
        NamespacedKey recipeNamespacedKey = new NamespacedKey(DecorHeadsPlugin.getInstance(), key);
        ItemStack result = ItemLoader.loadItem(resultName, heads);
        HashMap<Character, ItemStack> ingredientStackMap = parseIngredientsMap(ingredientsMap, heads);

        // Create Recipe
        ShapedMetaRecipe recipe = new ShapedMetaRecipe(recipeNamespacedKey, result);
        recipe.setGroup(resultName);

        // Create Shape
        List<String> recipeGrid = recipeYaml.getStringList(RECIPE_GRID_KEY);
        List<String> rows = new ArrayList<>();
        for (int i = 0; i < recipeGrid.size(); i++)
        {
            String row = recipeGrid.get(i).replace(" ", "").replace("/", " ");  // Remove formatting spaces, and convert "/" to " " for compatibility with Recipe.
            rows.add(row);
        }
        recipe.shape(rows.toArray(new String[0]));

        // Set Ingredients
        for (Map.Entry<Character, ItemStack> ingredientEntry : ingredientStackMap.entrySet())
        {
            String headKey = ingredientEntry.getValue().getItemMeta().getPersistentDataContainer().get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
            if (headKey == null)
                recipe.setIngredient(ingredientEntry.getKey(), ingredientEntry.getValue());
            else
                recipe.setIngredient(ingredientEntry.getKey(), ingredientEntry.getValue(), (ingredient, refIngredient) -> compareHeads(ingredient, refIngredient));
        }

        return recipe;
    }

    /**
     * Creates a mapping of crafting grid key to item stack from YAML data
     * @param ingredientsYaml the ingredients yaml
     * @param headMap a key:head map of registered heads
     * @return a mapping of crafting grid keys and the item stacks they represent
     * @throws IllegalArgumentException invalid ingredients or key
     */
    private HashMap<Character, ItemStack> parseIngredientsMap(ConfigurationSection ingredientsYaml, HashMap<String, IHead> headMap) throws IllegalArgumentException, IOException
    {
        HashMap<Character, ItemStack> ingredientStackMap = new HashMap<>();
        for (String ingredientKey : ingredientsYaml.getKeys(false))
        {
            if (ingredientKey.length() > 1)
                throw new IllegalArgumentException(String.format("Invalid ingredient key \"%s\" - Keys may only be one character.", ingredientKey));

            String ingredientItemName = ingredientsYaml.getString(ingredientKey);
            ItemStack ingredientStack = ItemLoader.loadItem(ingredientItemName, headMap);
            char gridKey = ingredientKey.toUpperCase().charAt(0);
            ingredientStackMap.put(gridKey, ingredientStack);
        }
        return ingredientStackMap;
    }

    /**
     * Compares two item stacks, to see if they are the same type of DecorHead
     * @param ingredient the provided item stack
     * @param refIngredient the expected item stack
     * @return true if head type matches
     */
    private boolean compareHeads(ItemStack ingredient, ItemStack refIngredient)
    {
        if (refIngredient.getType() == Material.PLAYER_HEAD && ingredient.getType() == Material.PLAYER_HEAD)
        {
            String refIngredientSkullKey = refIngredient.getItemMeta().getPersistentDataContainer().get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
            String ingredientSkullKey = ingredient.getItemMeta().getPersistentDataContainer().get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
            return refIngredientSkullKey.equalsIgnoreCase(ingredientSkullKey);
        }
        return false;
    }
}
