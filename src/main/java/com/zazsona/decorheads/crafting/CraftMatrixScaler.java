package com.zazsona.decorheads.crafting;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CraftMatrixScaler
{
    /**
     * Scales the crafting matrix such that it is of the set size, anchoring current contents to the top left.
     * @param srcMatrix - the matrix to format
     * @param targetAxisLength - The axis length to target
     * @param isShaped - set to true if this matrix represents a {@link ShapedRecipe}, false for a {@link ShapelessRecipe}
     * @return the scaled matrix
     */
    @NotNull
    public static ItemStack[] scaleCraftMatrix(ItemStack[] srcMatrix, int targetAxisLength, boolean isShaped)
    {
        if (srcMatrix.length == (targetAxisLength * targetAxisLength))
            return srcMatrix;

        int srcAxisLength = (int) Math.ceil(Math.sqrt(srcMatrix.length));
        boolean isScaleUp = targetAxisLength > srcAxisLength;

        if (isScaleUp)
            return upscaleCraftMatrix(srcMatrix, targetAxisLength, isShaped);
        else
            return downscaleCraftMatrix(srcMatrix, targetAxisLength, isShaped);
    }

    /**
     * Scales up the crafting matrix such that it is of the set size, anchoring current contents to the top left.
     * @param srcMatrix - the matrix to format
     * @param targetAxisLength - The axis length to target
     * @param isShaped - set to true if this matrix represents a {@link ShapedRecipe}, false for a {@link ShapelessRecipe}
     * @return the scaled matrix
     */
    private static ItemStack[] upscaleCraftMatrix(ItemStack[] srcMatrix, int targetAxisLength, boolean isShaped)
    {
        if (srcMatrix.length == (targetAxisLength * targetAxisLength))
            return srcMatrix;

        int srcAxisLength = (int) Math.ceil(Math.sqrt(srcMatrix.length));
        if (srcAxisLength > targetAxisLength)
            throw new IllegalArgumentException("Target Axis Length must be >= the current matrix axis length.");

        ItemStack[] newMatrix;
        if (isShaped)
        {
            newMatrix = new ItemStack[targetAxisLength * targetAxisLength];
            for (int i = 0; i < newMatrix.length; i++)
            {
                // Map the recipe to the new size, such that it's in the top left of the grid.
                int targetColumn = i % targetAxisLength;
                int targetRow = (i - targetColumn) / targetAxisLength;
                if (targetColumn < srcAxisLength && targetRow < srcAxisLength)
                    newMatrix[i] = srcMatrix[targetRow * targetColumn];
                else
                    newMatrix[i] = new ItemStack(Material.AIR);
            }
        }
        else
        {
            newMatrix = Arrays.copyOf(srcMatrix, targetAxisLength * targetAxisLength);
            Arrays.fill(newMatrix, srcMatrix.length, newMatrix.length, new ItemStack(Material.AIR));
        }
        return newMatrix;
    }

    /**
     * Scales down the crafting matrix such that it is of the set size, anchoring current contents to the top left.
     * @param srcMatrix - the matrix to format
     * @param targetAxisLength - The axis length to target
     * @param isShaped - set to true if this matrix represents a {@link ShapedRecipe}, false for a {@link ShapelessRecipe}
     * @return the scaled matrix
     */
    private static ItemStack[] downscaleCraftMatrix(ItemStack[] srcMatrix, int targetAxisLength, boolean isShaped)
    {
        if (srcMatrix.length == (targetAxisLength * targetAxisLength))
            return srcMatrix;

        int srcAxisLength = (int) Math.ceil(Math.sqrt(srcMatrix.length));
        if (srcAxisLength < targetAxisLength)
            throw new IllegalArgumentException("Target Axis Length must be <= the current matrix axis length.");

        ItemStack[] srcMatrixNoAir = (ItemStack[]) Arrays.stream(srcMatrix).filter(itemStack -> !itemStack.getType().isAir()).toArray();
        if (srcMatrixNoAir.length > targetAxisLength * targetAxisLength)
            throw new IllegalArgumentException("The count of contents in the matrix exceed the new matrix size.");

        ItemStack[] newMatrix;
        if (isShaped)
        {
            newMatrix = new ItemStack[targetAxisLength * targetAxisLength];
            int contentsStartIndex = getInitialContentsIndex(srcMatrix);
            int contentsStartColumn = contentsStartIndex % srcAxisLength;
            int contentsStartRow = (contentsStartIndex - contentsStartColumn) / srcAxisLength;

            int contentsEndIndex = getFinalContentsIndex(srcMatrix);
            int contentsEndColumn = contentsEndIndex % srcAxisLength;
            int contentsEndRow = (contentsEndIndex - contentsEndColumn) / srcAxisLength;

            int width = (contentsEndColumn + 1 - contentsStartColumn);
            int height = (contentsEndRow + 1 - contentsStartRow);
            if ((width > targetAxisLength) || (height > targetAxisLength))
                throw new IllegalArgumentException("The shape of contents in the matrix exceed the new matrix width/height.");

            int targetMatrixIndex = 0;
            for (int i = contentsStartIndex; i <= contentsEndIndex; i++)
            {
                int srcColumn = i % srcAxisLength;
                int srcRow = (i - srcColumn) / srcAxisLength;
                if (srcColumn < contentsStartColumn || srcColumn > contentsEndColumn)
                    continue;
                if (srcRow < contentsStartRow || srcRow > contentsEndRow)
                    continue;

                newMatrix[targetMatrixIndex] = srcMatrix[i];
                targetMatrixIndex++;
            }
        }
        else
        {
            newMatrix = Arrays.copyOf(srcMatrixNoAir, targetAxisLength * targetAxisLength);
            Arrays.fill(newMatrix, srcMatrixNoAir.length, newMatrix.length, new ItemStack(Material.AIR));
        }
        return newMatrix;
    }

    /**
     * Gets the index of the first non-Air item in the matrix.
     * @param srcMatrix the matrix to search
     */
    private static int getInitialContentsIndex(ItemStack[] srcMatrix)
    {
        for (int i = 0; i < srcMatrix.length; i++)
        {
            if (!srcMatrix[i].getType().isAir())
                return i;
        }
        throw new IllegalArgumentException("Could not find contents index: Matrix is empty or air.");
    }

    /**
     * Gets the index of the final non-Air item in the matrix.
     * @param srcMatrix the matrix to search
     */
    private static int getFinalContentsIndex(ItemStack[] srcMatrix)
    {
        int lastNonAirIndex = -1;
        for (int i = 0; i < srcMatrix.length; i++)
        {
            if (!srcMatrix[i].getType().isAir())
                return lastNonAirIndex;
        }

        if (lastNonAirIndex == -1)
            throw new IllegalArgumentException("Could not find contents index: Matrix is empty or air.");
        else
            return lastNonAirIndex;
    }
}
