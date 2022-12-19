package com.zazsona.decorheads;

public enum RecipeType
{
    CRAFT;

    /**
     * Attempts to match the {@link RecipeType} from the provided string by matching the enum value name format
     * @param recipeType the name of the recipe type
     * @return the matching {@link RecipeType}, or null if no matches found.
     */
    public static RecipeType matchRecipeType(String recipeType)
    {
        try
        {
            String formattedName = recipeType.toUpperCase().replace("-", "_");
            return RecipeType.valueOf(formattedName);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }
}
