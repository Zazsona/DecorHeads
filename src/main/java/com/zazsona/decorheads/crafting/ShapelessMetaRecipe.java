package com.zazsona.decorheads.crafting;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * NOTE: INSTANCES OF THIS CLASS MUST BE REGISTERED AGAINST {@link MetaRecipeManager}. USING {@link Bukkit#addRecipe(Recipe)} WILL DO NOTHING.<br><br>
 * Represents a shapeless recipe where custom match requirements can be defined, and the arrangement of the ingredients on the crafting grid does not matter.
 */
public class ShapelessMetaRecipe implements IMetaRecipe, Listener
{
    protected NamespacedKey key;
    protected ItemStack result;
    private List<MetaIngredient> ingredients;
    private String group;

    public ShapelessMetaRecipe(NamespacedKey key, ItemStack result)
    {
        Preconditions.checkArgument(key != null, "key");
        Preconditions.checkArgument(!result.getType().isAir(), "Recipe must have non-AIR result.");
        this.key = key;
        this.result = new ItemStack(result);
        this.ingredients = new LinkedList<>();
        this.group = "";
    }

    /**
     * Adds an ingredient without any metadata requirements. i.e, any item will work as long as it has the same material.
     * @param material the material to match.
     * @return the added {@link MetaIngredient}
     */
    public MetaIngredient addIngredient(Material material)
    {
        ItemStack itemStack = new ItemStack(material);
        IngredientValidityComparator comparator = (ingredient, refIngredient) -> refIngredient.getType() == ingredient.getType();
        MetaIngredient metaIngredient = new MetaIngredient(itemStack, comparator);
        return addIngredient(metaIngredient);
    }

    /**
     * Adds an ingredient where the material type and metadata must match.
     * @param itemStack the reference stack, representing the material type and metadata to match.
     * @return the added {@link MetaIngredient}
     */
    public MetaIngredient addIngredient(ItemStack itemStack)
    {
        MetaIngredient ingredient = new MetaIngredient(itemStack, (item, refItem) -> refItem.getType() == item.getType() && refItem.getItemMeta().equals(item.getItemMeta()));
        return addIngredient(ingredient);
    }

    /**
     * Adds an ingredient where the conditions for a match are defined by the passed comparator.
     * @param itemStack the reference stack
     * @param comparator the logic for dictating a match
     * @return the added {@link MetaIngredient}
     */
    public MetaIngredient addIngredient(ItemStack itemStack, IngredientValidityComparator comparator)
    {
        MetaIngredient ingredient = new MetaIngredient(itemStack, comparator);
        return addIngredient(ingredient);
    }

    /**
     * Adds an ingredient.
     * @param ingredient the {@link MetaIngredient} to add.
     * @return the added {@link MetaIngredient}.
     */
    public MetaIngredient addIngredient(MetaIngredient ingredient)
    {
        Validate.isTrue(ingredients.size() + 1 <= 9, "Shapeless recipes cannot have more than 9 ingredients");
        ingredients.add(ingredient);
        return ingredient;
    }

    /**
     * Removes an ingredient.
     * @param ingredient the ingredient to remove
     * @return true on removal
     */
    public boolean removeIngredient(ItemStack ingredient)
    {
        Iterator<MetaIngredient> iterator = ingredients.iterator();
        while (iterator.hasNext())
        {
            MetaIngredient metaIngredient = iterator.next();
            if (metaIngredient.getItemStack().equals(ingredient))
            {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Removes an ingredient.
     * @param ingredient the ingredient to remove
     * @return true on removal
     */
    public boolean removeIngredient(MetaIngredient ingredient)
    {
        return ingredients.remove(ingredient);
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
     * Gets the ingredients for this recipe, including their metadata.
     * @return
     */
    public List<MetaIngredient> getIngredientList()
    {
        return ingredients;
    }

    /**
     * Gets a recipe with matching materials, but without ingredient metadata requirements.
     * @return a metadata-less recipe.
     */
    @Override
    public Recipe getRawRecipe()
    {
        ShapelessRecipe recipe = new ShapelessRecipe(key, result);
        recipe.setGroup(group);
        for (MetaIngredient ingredient : ingredients)
            recipe.addIngredient((ingredient != null) ? ingredient.getMaterial() : Material.AIR);
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
