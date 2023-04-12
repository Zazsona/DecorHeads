package com.zazsona.decorheads.crafting;

/**
 * Represents a Recipe's priority in crafting.
 */
public enum RecipePriority
{
    /**
     * This recipe is of very low priority, and should be discarded if there are any other Recipes
     * with matching conditions. Ideal for recipes which are very broad in what ingredients are valid,
     * such as a wide range of MaterialType matches.
     */
    LOWEST,
    /**
     * This recipe is of low priority, and can be discarded if there are any other Recipes
     * with matching conditions. Ideal for recipes which are broad in what ingredients are valid,
     * such as a small range of MaterialType matches.
     */
    LOW,
    /**
     * This recipe is of normal priority, and could be discarded if there are any other Recipes
     * with matching conditions. Ideal for recipes which are selective in what ingredients are valid,
     * such as a specific MaterialType match.
     * <br>
     * Note that this is the default priority level for vanilla recipes.
     */
    NORMAL,
    /**
     * This recipe is of high priority, and shouldn't be discarded if there are any other Recipes
     * with matching conditions. Ideal for recipes which are specific in what ingredients are valid,
     * such as meeting a threshold or range of metadata values for a given MaterialType.
     */
    HIGH,
    /**
     * This recipe is of very high priority, and mustn't be discarded if there are any other Recipes
     * with matching conditions. Ideal for recipes which are specific in what ingredients are valid,
     * such as meeting a specific metadata value for a given MaterialType.
     * <br>
     * Note that there is no guarantee this recipe won't be discarded if another recipe of equal priority
     * is found.
     */
    HIGHEST
}
