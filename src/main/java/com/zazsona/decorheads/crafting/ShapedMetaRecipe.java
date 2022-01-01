package com.zazsona.decorheads.crafting;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.*;

/**
 * NOTE: INSTANCES OF THIS CLASS MUST BE REGISTERED AGAINST {@link MetaRecipeManager}. USING {@link Bukkit#addRecipe(Recipe)} WILL DO NOTHING.<br><br>
 * Represents a shaped recipe where custom match requirements can be defined, and the arrangement of the ingredients on the crafting grid does matter.
 */
public class ShapedMetaRecipe implements IMetaRecipe, Listener
{
    protected NamespacedKey key;
    protected ItemStack result;
    protected String[] rows;
    private HashMap<Character, MetaIngredient> ingredientsMap;
    private String group;

    public ShapedMetaRecipe(NamespacedKey key, ItemStack result)
    {
        Preconditions.checkArgument(key != null, "key");
        Preconditions.checkArgument(!result.getType().isAir(), "Recipe must have non-AIR result.");
        this.key = key;
        this.result = new ItemStack(result);
        this.ingredientsMap = new HashMap<>();
        this.group = "";
    }

    /**
     * Set the shape of this recipe to the specified rows. Each character
     * represents a different ingredient; exactly what each character
     * represents is set separately. The first row supplied corresponds with
     * the upper most part of the recipe on the workbench e.g. if all three
     * rows are supplied the first string represents the top row on the
     * workbench.
     *
     * @param shape The rows of the recipe (up to 3 rows).
     * @return The shape
     */
    public String[] shape(final String... shape) {
        Validate.notNull(shape, "Must provide a shape");
        Validate.isTrue(shape.length > 0 && shape.length < 4, "Crafting recipes should be 1, 2 or 3 rows, not ", shape.length);

        int lastLen = -1;
        for (String row : shape) {
            Validate.notNull(row, "Shape cannot have null rows");
            Validate.isTrue(row.length() > 0 && row.length() < 4, "Crafting rows should be 1, 2, or 3 characters, not ", row.length());

            Validate.isTrue(lastLen == -1 || lastLen == row.length(), "Crafting recipes must be rectangular");
            lastLen = row.length();
        }
        this.rows = new String[shape.length];
        for (int i = 0; i < shape.length; i++) {
            this.rows[i] = shape[i];
        }

        // Remove character mappings for characters that no longer exist in the shape
        HashMap<Character, MetaIngredient> newIngredients = new HashMap<>();
        for (String row : shape) {
            for (Character c : row.toCharArray()) {
                newIngredients.put(c, ingredientsMap.get(c));
            }
        }
        this.ingredientsMap = newIngredients;
        return shape;
    }

    /**
     * Adds an ingredient without any metadata requirements. i.e, any item will work as long as it has the same material.
     * @param key the shape key
     * @param material the material to match.
     * @return the added {@link MetaIngredient}
     */
    public MetaIngredient setIngredient(char key, Material material)
    {
        ItemStack itemStack = new ItemStack(material);
        IngredientValidityComparator comparator = (ingredient, refIngredient) -> refIngredient.getType() == ingredient.getType();
        MetaIngredient metaIngredient = new MetaIngredient(itemStack, comparator);
        return setIngredient(key, metaIngredient);
    }

    /**
     * Adds an ingredient where the material type and metadata must match.
     * @param key the shape key
     * @param itemStack the reference stack, representing the material type and metadata to match.
     * @return the added {@link MetaIngredient}
     */
    public MetaIngredient setIngredient(char key, ItemStack itemStack)
    {
        MetaIngredient ingredient = new MetaIngredient(itemStack, (item, refItem) -> refItem.getType() == item.getType() && refItem.getItemMeta().equals(item.getItemMeta()));
        return setIngredient(key, ingredient);
    }

    /**
     * Adds an ingredient where the conditions for a match are defined by the passed comparator.
     * @param key the shape key
     * @param itemStack the reference stack
     * @param comparator the logic for dictating a match
     * @return the added {@link MetaIngredient}
     */
    public MetaIngredient setIngredient(char key, ItemStack itemStack, IngredientValidityComparator comparator)
    {
        MetaIngredient ingredient = new MetaIngredient(itemStack, comparator);
        return setIngredient(key, ingredient);
    }

    /**
     * Adds an ingredient.
     * @param key the shape key
     * @param ingredient the {@link MetaIngredient} to add.
     * @return the added {@link MetaIngredient}.
     */
    public MetaIngredient setIngredient(char key, MetaIngredient ingredient)
    {
        Validate.isTrue(ingredientsMap.containsKey(key), "Symbol does not appear in the shape:", key);
        ingredientsMap.put(key, ingredient);
        return ingredient;
    }

    /**
     * See: {@link ShapelessRecipe#setGroup(String)}
     * @return
     */
    public void setGroup(String group)
    {
        if (group == null)
            throw new NullPointerException();
        this.group = group;
    }

    /**
     * See: {@link ShapelessRecipe#getGroup()}
     * @return
     */
    public String getGroup()
    {
        return group;
    }

    /**
     * Get the shape.
     * @return The recipe's shape.
     * @throws NullPointerException if shape not yet set
     */
    public String[] getShape()
    {
        return rows.clone();
    }

    /**
     * Gets the ingredients for this recipe, including their metadata.
     * @return
     */
    public HashMap<Character, MetaIngredient> getIngredientMap()
    {
        return ingredientsMap;
    }

    /**
     * Gets a recipe with matching materials and positioning, but without ingredient metadata requirements.
     * @return a metadata-less recipe.
     */
    @Override
    public Recipe getRawRecipe()
    {
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.setGroup(group);
        recipe.shape(rows);
        for (Map.Entry<Character, MetaIngredient> entry : ingredientsMap.entrySet())
            recipe.setIngredient(entry.getKey(), (entry.getValue() != null) ? entry.getValue().getMaterial() : Material.AIR);
        return recipe;
    }

    @Override
    public NamespacedKey getKey()
    {
        return key;
    }

    @Override
    public ItemStack getResult()
    {
        return result;
    }
}
