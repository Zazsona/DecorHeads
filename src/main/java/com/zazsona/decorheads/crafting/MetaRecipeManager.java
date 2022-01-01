package com.zazsona.decorheads.crafting;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class MetaRecipeManager
{
    private static boolean initialised = false;
    private static HashMap<NamespacedKey, IMetaRecipe> metaRecipes = new HashMap<>();
    private static Listener listener = new Listener()
    {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onPrepareItemCraft(PrepareItemCraftEvent e)
        {
            if (e.getRecipe() != null)
            {
                Recipe recipe = e.getRecipe();
                if (recipe instanceof Keyed)
                {
                    NamespacedKey recipeKey = ((Keyed) recipe).getKey();
                    if (metaRecipes.containsKey(recipeKey))
                    {
                        Recipe metaRecipe = metaRecipes.get(recipeKey);
                        boolean passed = false;
                        if (metaRecipe instanceof ShapedMetaRecipe)
                            passed = passShapedMetaRecipe(e.getInventory(), (ShapedMetaRecipe) metaRecipe);

                        if (metaRecipe instanceof ShapelessMetaRecipe)
                            passed = passShapelessMetaRecipe(e.getInventory(), (ShapelessMetaRecipe) metaRecipe);

                        if (!passed)
                            e.getInventory().setResult(null);
                    }
                }
            }
        }

        /**
         * Gets if the ingredients placed in the inventory are valid for the provided recipe
         * @param inventory the inventory with the ingredients
         * @param recipe the recipe to check against
         * @return boolean on recipe matched
         */
        private boolean passShapedMetaRecipe(CraftingInventory inventory, ShapedMetaRecipe recipe)
        {
            String[] rows = recipe.getShape();
            HashMap<Character, MetaIngredient> mIngredients = recipe.getIngredientMap();
            ItemStack[] matrix = inventory.getMatrix();
            for (int i = 0; i < rows.length; i++)
            {
                char[] rowElements = rows[i].toCharArray();
                for (int j = 0; j < rowElements.length; j++)
                {
                    char key = rowElements[j];
                    int matrixIndex = (i * rowElements.length) + j; // Matrix is single-dimensional. To get the index it's (Row * Elements per Row) + Current Row Index
                    ItemStack ingredient = matrix[matrixIndex];
                    ingredient = (ingredient != null) ? ingredient : new ItemStack(Material.AIR);
                    MetaIngredient mIngredient = mIngredients.get(key);

                    if (!(mIngredient == null || mIngredient.getMaterial().isAir()) && !mIngredient.isItemStackIngredientMatch(ingredient))
                        return false;
                    else if ((mIngredient == null || mIngredient.getMaterial().isAir()) && !(ingredient == null || ingredient.getType().isAir()))
                        return false;
                }
            }
            // All ingredients matched...
            return true;
        }

        /**
         * Gets if the ingredients placed in the inventory are valid for the provided recipe
         * @param inventory the inventory with the ingredients
         * @param recipe the recipe to check against
         * @return boolean on recipe matched
         */
        private boolean passShapelessMetaRecipe(CraftingInventory inventory, ShapelessMetaRecipe recipe)
        {
            ItemStack[] ingredientsMatrix = inventory.getMatrix();
            List<ItemStack> ingredients = new LinkedList<>();
            for (ItemStack ingredient : ingredientsMatrix)
            {
                if (ingredient != null && !ingredient.getType().isAir())
                    ingredients.add(ingredient);
            }

            if (ingredients.size() == recipe.getIngredientList().size()) // Calling .length on ingredientsMatrix returns the slots in the matrix NOT the number of ingredients entered.
            {
                List<MetaIngredient> requirements = new LinkedList<>(recipe.getIngredientList());
                Comparator<MetaIngredient> requirementsComparator = Comparator.comparing(req -> req.hashCode());
                requirements.sort(requirementsComparator);
                Comparator<ItemStack> ingredientsComparator = Comparator.comparing(ing -> ing.hashCode());
                ingredients.sort(ingredientsComparator);

                // Map out passes and failures on a table.
                // If a test has no passes, cancel early.
                int[][] resultMatrix = new int[requirements.size()][requirements.size()];
                for (MetaIngredient requirement : requirements)
                {
                    boolean minimumPassesFound = false;
                    for (ItemStack ingredient : ingredients)
                    {
                        boolean valid = requirement.isItemStackIngredientMatch(ingredient);
                        if (valid)
                        {
                            int requirementIndex = Collections.binarySearch(requirements, requirement, requirementsComparator);
                            int ingredientIndex = Collections.binarySearch(ingredients, ingredient, ingredientsComparator);
                            resultMatrix[requirementIndex][ingredientIndex] = 1; // 1 = true, 0 (default) = false
                            minimumPassesFound = true;
                        }
                    }
                    if (!minimumPassesFound)
                        return false;
                }

                //  Place requirements with the fewest passing values first for checking
                //  Place ingredients with the fewest passes first for checking
                List<MetaIngredient> prioritisedRequirements = new ArrayList<>(requirements);
                prioritisedRequirements.sort((req1, req2) ->
                                        {
                                            int req1Index = Collections.binarySearch(requirements, req1, requirementsComparator);
                                            int req1Score = 0;
                                            int req2Index = Collections.binarySearch(requirements, req2, requirementsComparator);
                                            int req2Score = 0;
                                            for (int i = 0; i < resultMatrix[req1Index].length; i++)
                                                req1Score += resultMatrix[req1Index][i];
                                            for (int i = 0; i < resultMatrix[req2Index].length; i++)
                                                req2Score += resultMatrix[req2Index][i];
                                            return Integer.compare(req1Score, req2Score);
                                        });
                List<ItemStack> prioritisedIngredients = new ArrayList<>(ingredients);
                prioritisedIngredients.sort((ing1, ing2) ->
                                       {
                                           int ing1Index = Collections.binarySearch(ingredients, ing1, ingredientsComparator);
                                           int ing1Score = 0;
                                           int ing2Index = Collections.binarySearch(ingredients, ing2, ingredientsComparator);
                                           int ing2Score = 0;
                                           for (int i = 0; i < resultMatrix.length; i++)
                                               ing1Score += resultMatrix[i][ing1Index];
                                           for (int i = 0; i < resultMatrix.length; i++)
                                               ing2Score += resultMatrix[i][ing2Index];
                                           return Integer.compare(ing1Score, ing2Score);
                                       });

                /*
                    #1. Start with the requirement that has the fewest passing ingredients
                    #2. Iterate over the ingredients, from ones that pass the least requirements to those that pass the most
                           This means that requirements with only one passing ingredient take that ingredient first.
                           Requirements will also take ingredients that satisfy the minimum number of other requirements while meeting their own
                           Thus, ingredients are used as efficiently as possible. If a requirement cannot take any ingredients because all passing ingredients are reserved, the operation is impossible.
                    #3a. If an ingredient is taken or fails the requirement, check the next.
                        #3a-1. If no ingredients pass, the provided ingredients are invalid for this recipe. Fail the check.
                    #3b. If an ingredient is not taken and passes, add it to the map and flag a passing value is found
                    #4. If every requirement has a mapped ingredient, pass the check!
                 */
                List<ItemStack> remainingIngredients = new ArrayList<>(prioritisedIngredients);
                for (MetaIngredient requirement : prioritisedRequirements)
                {
                    boolean mapped = false;
                    int requirementIndex = Collections.binarySearch(requirements, requirement, requirementsComparator);
                    Iterator<ItemStack> ingredientIterator = remainingIngredients.listIterator();
                    while (ingredientIterator.hasNext())
                    {
                        ItemStack ingredient = ingredientIterator.next();
                        int ingredientIndex = Collections.binarySearch(ingredients, ingredient, ingredientsComparator);
                        if (resultMatrix[requirementIndex][ingredientIndex] > 0)
                        {
                            ingredientIterator.remove();
                            mapped = true;
                            break;
                        }
                    }

                    if (!mapped)
                        return false;
                }

                if (remainingIngredients.size() == 0) // i.e, if every ingredient is paired to a requirement...
                    return true;
            }
            return false;
        }
    };

    /**
     * Initialises the RecipeManager
     * @param plugin the plugin to register against
     */
    public static void initialise(Plugin plugin)
    {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        initialised = true;
    }

    /**
     * Checks the RecipeManager is initialised.
     */
    private static void checkInitialisation()
    {
        if (!initialised)
            throw new RuntimeException("RecipeManager has not yet been initialised. Call RecipeManager#initialise(Plugin) in onEnable().");
    }

    /**
     * See: {@link Bukkit#recipeIterator()}
     * @return
     */
    public static Iterator<Recipe> recipeIterator()
    {
        checkInitialisation();
        LinkedList<Recipe> recipes = new LinkedList<>();
        Iterator<Recipe> recipesIterator = Bukkit.recipeIterator();
        while (recipesIterator.hasNext())
        {
            Recipe recipe = recipesIterator.next();
            if (recipe instanceof Keyed)
            {
                NamespacedKey recipeKey = ((Keyed) recipe).getKey();
                if (metaRecipes.containsKey(recipeKey))
                {
                    recipes.add(metaRecipes.get(recipeKey));
                    continue;
                }
            }
            recipes.add(recipe);
        }
        return recipes.iterator();
    }

    /**
     * See: {@link Bukkit#addRecipe(Recipe)}
     * @return
     */
    public static boolean addRecipe(Recipe recipe)
    {
        checkInitialisation();
        if (recipe instanceof IMetaRecipe)
        {
            IMetaRecipe metaRecipe = (IMetaRecipe) recipe;
            boolean success = Bukkit.addRecipe(metaRecipe.getRawRecipe());
            if (success)
                metaRecipes.put(metaRecipe.getKey(), metaRecipe);
            return success;
        }
        else
            return Bukkit.addRecipe(recipe);
    }

    /**
     * See: {@link Bukkit#removeRecipe(NamespacedKey)}
     * @return
     */
    public static void removeRecipe(NamespacedKey key)
    {
        checkInitialisation();
        boolean removeSuccess = Bukkit.removeRecipe(key);
        if (removeSuccess)
            metaRecipes.remove(key);
    }

    /**
     * See: {@link Bukkit#getRecipe(NamespacedKey)}
     * @return
     */
    public static Recipe getRecipe(NamespacedKey key)
    {
        checkInitialisation();
        if (metaRecipes.containsKey(key))
            return metaRecipes.get(key);
        return Bukkit.getRecipe(key);
    }

    /**
     * See: {@link Bukkit#getRecipesFor(ItemStack)}
     * @return
     */
    public static List<Recipe> getRecipesFor(ItemStack result)
    {
        checkInitialisation();
        List<Recipe> recipes = Bukkit.getRecipesFor(result);
        List<NamespacedKey> metaKeys = new ArrayList<>();
        Iterator<Recipe> recipeIterator = recipes.iterator();
        while (recipeIterator.hasNext())
        {
            Recipe recipe = recipeIterator.next();
            if (recipe instanceof Keyed)
            {
                NamespacedKey recipeKey = ((Keyed) recipe).getKey();
                if (metaRecipes.containsKey(recipeKey))
                {
                    metaKeys.add(recipeKey);
                    recipeIterator.remove();
                }
            }
        }
        for (NamespacedKey key : metaKeys)
            recipes.add(metaRecipes.get(key));
        return recipes;
    }

    /**
     * See: {@link Bukkit#resetRecipes()}
     * @return
     */
    public static void resetRecipes()
    {
        checkInitialisation();
        Set<NamespacedKey> keys = new HashSet<>(metaRecipes.keySet());
        for (NamespacedKey key : keys)
            removeRecipe(key);
        Bukkit.resetRecipes();
    }

    /**
     * See: {@link Bukkit#clearRecipes()}
     * @return
     */
    public static void clearRecipes()
    {
        checkInitialisation();
        Set<NamespacedKey> keys = new HashSet<>(metaRecipes.keySet());
        for (NamespacedKey key : keys)
            removeRecipe(key);
        Bukkit.clearRecipes();
    }
}
