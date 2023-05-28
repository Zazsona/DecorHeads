package com.zazsona.decorheads.crafting;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class MetaRecipeManager
{
    private static final int MIN_MATRIX_AXIS_LENGTH = 1; // Shortest axis in the game; single slot
    private static final int MAX_MATRIX_AXIS_LENGTH = 3; // Longest axis in the game; currently a Crafting Table
    private static MetaRecipeManager instance;

    private HashMap<NamespacedKey, IMetaRecipe> metaRecipeByKey;
    private HashMap<NamespacedKey, RecipePriority> priorityByMetaRecipeKey;
    private HashMap<Integer, MetaRecipeCraftTree> shapedCraftTreeByAxisLength;
    private MetaRecipeCraftTree shapelessCraftTree;
    private boolean debug;

    public static MetaRecipeManager getInstance(Plugin plugin)
    {
        if (instance == null)
            instance = new MetaRecipeManager(plugin);
        return instance;
    }

    private MetaRecipeManager(Plugin plugin)
    {
        metaRecipeByKey = new HashMap<>();
        priorityByMetaRecipeKey = new HashMap<>();
        shapedCraftTreeByAxisLength = new HashMap<>();
        for (int i = MIN_MATRIX_AXIS_LENGTH; i <= MAX_MATRIX_AXIS_LENGTH; i++)
            shapedCraftTreeByAxisLength.put(i, new MetaRecipeCraftTree());
        shapelessCraftTree = new MetaRecipeCraftTree();

        Bukkit.getPluginManager().registerEvents(listener, plugin);
        if (plugin.getConfig().contains("debug") && plugin.getConfig().isBoolean("debug"))
            debug = plugin.getConfig().getBoolean("debug");
    }

    private Listener listener = new Listener()
    {
        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onPrepareItemCraft(PrepareItemCraftEvent e)
        {
            // Get the highest priority recipe (vanilla & meta)...
            Recipe recipe = getCraftingRecipe(e.getInventory().getMatrix(), e.getInventory().getLocation().getWorld());

            // Recipes under crafting_special_* may return AIR for getResult(), whereas the Inventory will have the
            // correct result. As such, we should only set the result when we need to, so we only modify for registered recipes.
            if (recipe != null && metaRecipeByKey.containsKey(((Keyed) recipe).getKey()))
                e.getInventory().setResult(recipe.getResult());

            // Nothing we need to do if it's not a MetaRecipe; leave default behaviour.
        }
    };

    /**
     * Searches the provided {@link MetaRecipeCraftTree} for any recipes that are satisfied by the provided
     * matrix array.<br>
     * The tree is navigated such that each depth level denotes a position on the crafting grid, going from left to
     * right before descending a row, starting from the top-left. For example, given the below crafting grid:<br>
     * <br>
     *  A  B  C <br>
     *  D  E  F <br>
     *  G  H  I <br>
     * <br>
     * This function will first check the root's children for any nodes that are satisfied by ingredient A, then check
     * the children of that satisfied node for any child nodes satisfied by B, and so forth in the order A, B, C, D,
     * E, F, G, H, and I, where I will be the tree's leaf node.
     *
     * @param srcMatrix - The ingredients to find the recipe for, where each index corresponds to a depth level in craftTree
     * @return the recipes satisfied by the ingredients and positioning in srcMatrix.
     */
    private List<IMetaRecipe> searchShapedMetaRecipe(ItemStack[] srcMatrix)
    {
        // TODO: Test what happens if the recipe is in the bottom right. It should now work.
        if (debug)
            Bukkit.getLogger().info("Searching ShapedMetaRecipes...");

        ArrayList<IMetaRecipe> recipes = new ArrayList<>();
        if (srcMatrix.length == 0 || Arrays.stream(srcMatrix).allMatch(itemStack -> itemStack == null))
            return recipes;

        int axisLength = (int) Math.ceil(Math.sqrt(srcMatrix.length));
        for (int i = CraftMatrixScaler.getMinimumAxisLength(srcMatrix, true); i <= axisLength; i++)
        {
            if (debug)
                Bukkit.getLogger().info("Searching " + i + "x" + i + " matrix recipes.");
            MetaRecipeCraftTree craftTree = shapedCraftTreeByAxisLength.get(i);
            ItemStack[] scaledMatrix = CraftMatrixScaler.scaleCraftMatrix(srcMatrix, i, true);
            recipes.addAll(searchMetaRecipeTree(craftTree.getRoot(), scaledMatrix, 0));
        }
        return recipes;
    }

    /**
     * Searches the provided {@link MetaRecipeCraftTree} for any recipes that are satisfied by the provided
     * ingredients array.<br>
     * The tree is navigated such that each depth level denotes an ingredient of the recipe, ordered by ascending
     * value of the ingredient's {@link Material}, retrieved via {@link ItemStack#getType()}
     *
     * @param ingredients - The ingredients to find the recipe for, where each index corresponds to a depth level in craftTree
     * @return the recipes satisfied by the ingredients in ingredients.
     */
    private List<IMetaRecipe> searchShapelessMetaRecipe(ItemStack[] ingredients)
    {
        if (debug)
            Bukkit.getLogger().info("Searching ShapelessMetaRecipes...");

        ItemStack[] nonNullIngredients = Arrays.stream(ingredients)
                .filter(itemStack -> itemStack != null)
                .sorted(Comparator.comparing(o -> o.getType().getKey().toString()))
                .toArray(ItemStack[]::new);

        if (nonNullIngredients.length == 0)
            return new ArrayList<>();

        return searchMetaRecipeTree(shapelessCraftTree.getRoot(), nonNullIngredients, 0);
    }

    /**
     * Searches the tree, starting from craftTreeNode and ingredientIndex, for recipes where the ingredients provided in the
     * array match the recipe ingredient criteria.
     * @param craftTreeNode The node to search. Depth of this node in the tree must match the value in ingredientIndex
     * @param ingredients The array of ingredients to check against a recipe. The length of the array must match the depth of the tree
     * @param ingredientIndex The current ingredient index to compare against the children of craftTreeNode. Must craftTreeNode's depth.
     * @return a list of recipes for which the list of ingredients satisfy their conditions.
     */
    private List<IMetaRecipe> searchMetaRecipeTree(MetaRecipeCraftTreeNode craftTreeNode, ItemStack[] ingredients, int ingredientIndex)
    {
        ArrayList<IMetaRecipe> recipes = new ArrayList<>();
        if (craftTreeNode.getChildren().size() == 0 && (ingredientIndex >= ingredients.length || Arrays.stream(ingredients, ingredientIndex, ingredients.length).allMatch(itemStack -> itemStack == null)))
        {
            NamespacedKey recipeKey = craftTreeNode.getAssociatedRecipe();
            if (recipeKey != null)
            {
                recipes.add((IMetaRecipe) getRecipe(recipeKey));
                if (debug)
                    Bukkit.getLogger().info("Found recipe: " + recipeKey);
            }
        }
        else if ((craftTreeNode.getChildren().size() > 0) && (ingredientIndex < ingredients.length))
        {
            ItemStack ingredient = ingredients[ingredientIndex];
            if (debug)
                Bukkit.getLogger().info("Depth " + ingredientIndex + " | Slot " + (ingredientIndex + 1) + "/" + ingredients.length + ": " + ((ingredient == null) ? "EMPTY" : ingredient.getType()));

            List<MetaRecipeCraftTreeNode> nodes = craftTreeNode.getChildrenOfType(ingredient);
            if (nodes.size() > 0)
            {
                for (MetaRecipeCraftTreeNode node : nodes)
                {
                    MetaIngredient nodeIngredient = node.getIngredient();
                    if ((nodeIngredient == null && ingredient == null) || nodeIngredient.isItemStackIngredientMatch(ingredient))
                        recipes.addAll(searchMetaRecipeTree(node, ingredients, ++ingredientIndex));
                }
            }
        }
        return recipes;
    }

    /**
     * See: {@link Bukkit#recipeIterator()}.
     * Gets an iterator on all recipes - both Meta & Vanilla
     * @return a ReadOnlyIterator
     */
    public Iterator<Recipe> recipeIterator()
    {
        return recipeIterator(true, true);
    }

    /**
     * See: {@link Bukkit#recipeIterator()}
     * @param includeVanilla - whether to include vanilla registered recipes or not
     * @param includeMeta - whether to include {@link IMetaRecipe}s or not
     * @return a ReadOnlyIterator
     */
    public Iterator<Recipe> recipeIterator(boolean includeVanilla, boolean includeMeta)
    {
        LinkedList<Recipe> recipes = new LinkedList<>();

        if (includeMeta)
            recipes.addAll(metaRecipeByKey.values());

        if (includeVanilla)
        {
            Iterator<Recipe> vanillaRecipeIterator = Bukkit.recipeIterator();
            while (vanillaRecipeIterator.hasNext())
                recipes.add(vanillaRecipeIterator.next());
        }
        return Collections.unmodifiableList(recipes).iterator();
    }

    /**
     * See: {@link Bukkit#addRecipe(Recipe)}.
     * Adds a Recipe with "NORMAL" priority.
     * @return
     */
    public boolean addRecipe(Recipe recipe)
    {
        return addRecipe(recipe, RecipePriority.NORMAL);
    }

    public boolean addRecipe(Recipe recipe, RecipePriority priority)
    {
        if (!(recipe instanceof IMetaRecipe))
            return Bukkit.addRecipe(recipe);

        IMetaRecipe metaRecipe = (IMetaRecipe) recipe;
        boolean isMetaRegistered = metaRecipeByKey.containsKey(metaRecipe.getKey());
        if (isMetaRegistered)
            return false;

        if (debug)
            Bukkit.getLogger().info("Loading Meta Recipe: " + metaRecipe.getKey());
        metaRecipeByKey.put(metaRecipe.getKey(), metaRecipe);
        priorityByMetaRecipeKey.put(metaRecipe.getKey(), priority);

        List<MetaIngredient> ingredients = null;
        if (metaRecipe instanceof ShapelessMetaRecipe)
        {
            ShapelessMetaRecipe slMetaRecipe = (ShapelessMetaRecipe) metaRecipe;
            ingredients = slMetaRecipe.getIngredientList();
            ingredients.sort(Comparator.naturalOrder());
        }

        if (metaRecipe instanceof ShapedMetaRecipe)
        {
            ShapedMetaRecipe sMetaRecipe = (ShapedMetaRecipe) metaRecipe;
            ingredients = new LinkedList<>();
            String[] shape = sMetaRecipe.getShape();
            for (String row : shape)
            {
                char[] rowElements = row.toCharArray();
                for (char ingredientKey : rowElements)
                    ingredients.add(sMetaRecipe.getIngredientMap().get(ingredientKey));
            }
        }

        // I think this works? Nulls are included in the ingredients array, so there shouldn't be any worries such as
        // having four entries, which would normally fit in a 2x2 grid, but the shape requiring 3 on the top row & 1 on
        // the 2nd, which would require a 3x3 grid.
        int axisLength = (int) Math.ceil(Math.sqrt(ingredients.size()));
        MetaRecipeCraftTree craftTree = (metaRecipe instanceof ShapelessMetaRecipe) ? shapelessCraftTree : shapedCraftTreeByAxisLength.get(axisLength);
        MetaRecipeCraftTreeNode currNode = craftTree.getRoot();
        for (MetaIngredient ingredient : ingredients)
        {
            MetaRecipeCraftTreeNode node = new MetaRecipeCraftTreeNode(ingredient, metaRecipe.getKey());
            currNode.addChild(node);
            currNode = node;
        }

        return true;
    }

    /**
     * See: {@link Bukkit#removeRecipe(NamespacedKey)}
     * @return
     */
    public boolean removeRecipe(NamespacedKey key)
    {
        boolean isMetaRegistered = metaRecipeByKey.containsKey(key);
        if (isMetaRegistered)
        {
            metaRecipeByKey.remove(key);
            priorityByMetaRecipeKey.remove(key);
            // TODO: Remove from craftTree
            return true;
        }
        else
            return Bukkit.removeRecipe(key);
    }

    /**
     * See: {@link Bukkit#getRecipe(NamespacedKey)}
     * @return
     */
    public Recipe getRecipe(NamespacedKey key)
    {
        if (metaRecipeByKey.containsKey(key))
            return metaRecipeByKey.get(key);
        return Bukkit.getRecipe(key);
    }

    /**
     * Sets the priority of a registered {@link IMetaRecipe}. Note that non-Meta recipes cannot have their priorities
     * altered to keep the {@link EventPriority} levels relative.
     * @param key - The NamespacedKey of a registered MetaRecipe
     * @param priority - The priority to set
     * @return true if the priority is updated, false if the key does not denote a registered {@link IMetaRecipe}
     */
    public boolean setRecipePriority(NamespacedKey key, RecipePriority priority)
    {
        if (priorityByMetaRecipeKey.containsKey(key))
        {
            priorityByMetaRecipeKey.put(key, priority);
            return true;
        }
        return false;
    }

    /**
     * Gets the priority of the Recipe.
     * @return Gets the {@link RecipePriority} of the recipe identified with the NamespacedKey.
     * If the recipe is a registered {@link IMetaRecipe}, the set priority is returned.
     * If the recipe is valid, but is not registered as a {@link IMetaRecipe}, the "NORMAL" priority is returned.
     * If the recipe is invalid, or could not be found with the given key, null is returned.
     */
    public RecipePriority getRecipePriority(NamespacedKey key)
    {
        if (priorityByMetaRecipeKey.containsKey(key))
            return priorityByMetaRecipeKey.get(key);

        if (getRecipe(key) != null)
            return RecipePriority.NORMAL;
        else
            return null;
    }

    /**
     * See: {@link Bukkit#getCraftingRecipe(ItemStack[], World)}
     * @throws IllegalStateException - There is more than one recipe satisfied by the craftingMatrix
     * @return
     */
    public Recipe getCraftingRecipe(ItemStack[] craftingMatrix, World world) throws IllegalStateException
    {
        return getCraftingRecipe(craftingMatrix, world, true, true);
    }

    /**
     * See: {@link Bukkit#getCraftingRecipe(ItemStack[], World)}
     * @param includeVanilla - whether to include vanilla registered recipes or not
     * @param includeMeta - whether to include {@link IMetaRecipe}s or not
     * @return the recipe satisfied by the crafting matrix, or the highest priority one if multiple are satisfied.
     */
    public Recipe getCraftingRecipe(ItemStack[] craftingMatrix, World world, boolean includeVanilla, boolean includeMeta) throws IllegalStateException
    {
        List<Recipe> craftingRecipes = getCraftingRecipes(craftingMatrix, world, includeVanilla, includeMeta);

        // TODO: Test priorities work
        // Get the highest priority recipe...
        Recipe topRecipe = null;
        RecipePriority topPriority = null;
        for (Recipe recipe : craftingRecipes)
        {
            NamespacedKey key = ((Keyed) recipe).getKey();
            RecipePriority recipePriority = getRecipePriority(key);
            if ((topRecipe == null && topPriority == null) || recipePriority.compareTo(topPriority) >= 1)
            {
                topRecipe = recipe;
                topPriority = recipePriority;
            }
        }

        if (debug)
            Bukkit.getLogger().info(String.format("Highest Priority Recipe: %s (Priority: %s)",
                                                  ((topRecipe == null) ? "None" : ((Keyed) topRecipe).getKey()),
                                                  ((topPriority == null) ? "None" : topPriority)));
        return topRecipe;
    }

    /**
     * Returns ALL recipes satisfied by the provided craftingMatrix.
     * See: {@link Bukkit#getCraftingRecipe(ItemStack[], World)}
     * @return
     */
    public List<Recipe> getCraftingRecipes(ItemStack[] craftingMatrix, World world)
    {
        return getCraftingRecipes(craftingMatrix, world, true, true);
    }

    /**
     * Returns ALL recipes satisfied by the provided craftingMatrix.
     * See: {@link Bukkit#getCraftingRecipe(ItemStack[], World)}
     * @param includeVanilla - whether to include vanilla registered recipes or not
     * @param includeMeta - whether to include {@link IMetaRecipe}s or not
     * @return
     */
    public List<Recipe> getCraftingRecipes(ItemStack[] craftingMatrix, World world, boolean includeVanilla, boolean includeMeta)
    {
        // If there are MetaRecipe(s) for these materials, get the ones that apply.
        List<Recipe> recipes = new ArrayList<>();
        if (includeMeta)
        {
            List<IMetaRecipe> shapedRecipes = searchShapedMetaRecipe(craftingMatrix);
            List<IMetaRecipe> shapelessRecipes = searchShapelessMetaRecipe(craftingMatrix);
            recipes.addAll(shapedRecipes);
            recipes.addAll(shapelessRecipes);
        }
        if (includeVanilla)
        {
            // Vanilla getCraftingRecipe() demands a length of 9.
            ItemStack[] vanillaCraftingMatrix = Arrays.copyOf(craftingMatrix, MAX_MATRIX_AXIS_LENGTH * MAX_MATRIX_AXIS_LENGTH);

            Recipe vanillaRecipe = Bukkit.getCraftingRecipe(vanillaCraftingMatrix, world);
            if (vanillaRecipe != null)
                recipes.add(vanillaRecipe);
        }

        if (debug)
            Bukkit.getLogger().info("Found " + recipes.size() + " matching recipes.");
        return recipes;
    }

    /**
     * See: {@link Bukkit#getRecipesFor(ItemStack)}
     * @return
     */
    public List<Recipe> getRecipesFor(ItemStack result)
    {
        return getRecipesFor(result, true, true);
    }

    /**
     * See: {@link Bukkit#getRecipesFor(ItemStack)}
     * @param includeVanilla - whether to include vanilla registered recipes or not
     * @param includeMeta - whether to include {@link IMetaRecipe}s or not
     * @return
     */
    public List<Recipe> getRecipesFor(ItemStack result, boolean includeVanilla, boolean includeMeta)
    {
        List<Recipe> recipes = new LinkedList<>();

        if (includeVanilla)
            recipes.addAll(Bukkit.getRecipesFor(result));

        if (includeMeta)
        {
            Iterator<Recipe> iterator = recipeIterator(false, true);
            while (iterator.hasNext())
            {
                Recipe recipe = iterator.next();
                if (recipe.getResult().equals(result))
                    recipes.add(recipe);
            }
        }
        return recipes;
    }

    /**
     * See: {@link Bukkit#resetRecipes()}
     * @return
     */
    public void resetRecipes()
    {
        resetRecipes(true, true);
    }

    /**
     * See: {@link Bukkit#resetRecipes()}
     * @param includeVanilla - whether to include vanilla registered recipes or not
     * @param includeMeta - whether to include {@link IMetaRecipe}s or not
     * @return
     */
    public void resetRecipes(boolean includeVanilla, boolean includeMeta)
    {
        if (includeVanilla)
            Bukkit.resetRecipes();

        if (includeMeta)
        {
            Set<NamespacedKey> keys = new HashSet<>(metaRecipeByKey.keySet());
            for (NamespacedKey key : keys)
                removeRecipe(key);
        }
    }

    /**
     * See: {@link Bukkit#clearRecipes()}
     * @return
     */
    public void clearRecipes()
    {
        clearRecipes(true, true);
    }

    /**
     * See: {@link Bukkit#clearRecipes()}
     * @param includeVanilla - whether to include vanilla registered recipes or not
     * @param includeMeta - whether to include {@link IMetaRecipe}s or not
     * @return
     */
    public void clearRecipes(boolean includeVanilla, boolean includeMeta)
    {
        if (includeVanilla)
            Bukkit.clearRecipes();

        if (includeMeta)
        {
            Set<NamespacedKey> keys = new HashSet<>(metaRecipeByKey.keySet());
            for (NamespacedKey key : keys)
                removeRecipe(key);
        }
    }

    public boolean isDebuggingEnabled()
    {
        return debug;
    }

    public void setDebuggingEnabled(boolean enableDebugging)
    {
        this.debug = enableDebugging;
    }
}
