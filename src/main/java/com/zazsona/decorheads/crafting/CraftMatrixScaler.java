package com.zazsona.decorheads.crafting;

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
     * Gets the smallest grid axis length the contents of the matrix can be placed in
     * @param srcMatrix the matrix to measure
     * @param isShaped  set to true if this matrix represents a {@link ShapedRecipe}, false for a {@link ShapelessRecipe}
     * @return the minimum axis length required to display the non-Null contents
     */
    public static int getMinimumAxisLength(ItemStack[] srcMatrix, boolean isShaped)
    {
        if (!isShaped)
        {
            ItemStack[] srcMatrixNoNulls = Arrays.stream(srcMatrix).filter(itemStack -> itemStack != null).toArray(ItemStack[]::new);
            return (int) Math.ceil(Math.sqrt(srcMatrixNoNulls.length));
        }
        else
        {
            return getMinimumSubmatrix(srcMatrix).getAxisLength();
        }
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

        if (isShaped)
            return upscaleShapedCraftMatrix(srcMatrix, targetAxisLength, srcAxisLength);
        else
            return upscaleShapelessCraftMatrix(srcMatrix, targetAxisLength);
    }

    @NotNull
    private static ItemStack[] upscaleShapelessCraftMatrix(ItemStack[] srcMatrix, int targetAxisLength)
    {
        ItemStack[] newMatrix = Arrays.copyOf(srcMatrix, targetAxisLength * targetAxisLength);
        Arrays.fill(newMatrix, srcMatrix.length, newMatrix.length, null);
        return newMatrix;
    }

    @NotNull
    private static ItemStack[] upscaleShapedCraftMatrix(ItemStack[] srcMatrix, int targetAxisLength, int srcAxisLength)
    {
        ItemStack[] newMatrix;
        newMatrix = new ItemStack[targetAxisLength * targetAxisLength];
        for (int i = 0; i < newMatrix.length; i++)
        {
            // Map the recipe to the new size, such that it's in the top left of the grid.
            int targetColumn = i % targetAxisLength;
            int targetRow = (i - targetColumn) / targetAxisLength;
            if (targetColumn < srcAxisLength && targetRow < srcAxisLength)
                newMatrix[i] = srcMatrix[targetRow * targetColumn];
            else
                newMatrix[i] = null;
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
            throw new IllegalArgumentException("Target Axis Length must be <= the source matrix axis length.");

        if (getMinimumAxisLength(srcMatrix, isShaped) > targetAxisLength)
            throw new IllegalArgumentException("The source matrix contents exceed the target matrix.");

        if (isShaped)
            return downscaleShapedCraftMatrix(srcMatrix, targetAxisLength, srcAxisLength);
        else
            return downscaleShapelessCraftMatrix(srcMatrix, targetAxisLength);
    }

    private static ItemStack[] downscaleShapelessCraftMatrix(ItemStack[] srcMatrix, int targetAxisLength)
    {
        ItemStack[] srcMatrixNoNulls = Arrays.stream(srcMatrix).filter(itemStack -> itemStack != null).toArray(ItemStack[]::new);
        ItemStack[] newMatrix = upscaleShapelessCraftMatrix(srcMatrixNoNulls, targetAxisLength);
        return newMatrix;
    }

    @NotNull
    private static ItemStack[] downscaleShapedCraftMatrix(ItemStack[] srcMatrix, int targetAxisLength, int srcAxisLength)
    {
        ItemStack[] newMatrix;
        newMatrix = new ItemStack[targetAxisLength * targetAxisLength];
        CraftSubmatrix submatrix = getMinimumSubmatrix(srcMatrix);

        int contentsStartColumn = submatrix.getStartColumn();
        int contentsStartRow = submatrix.getStartRow();

        int contentsEndColumn = contentsStartColumn + (submatrix.getAxisLength() - 1);
        int contentsEndRow = contentsStartRow + (submatrix.getAxisLength() - 1);

        for (int srcColumn = contentsStartColumn; srcColumn <= contentsEndColumn; srcColumn++)
        {
            int targetColumn = srcColumn - contentsStartColumn;
            for (int srcRow = contentsStartRow; srcRow <= contentsEndRow; srcRow++)
            {
                int targetRow = srcRow - contentsStartRow;

                int srcIndex = (srcRow * srcAxisLength) + srcColumn;
                int targetIndex = (targetRow * targetAxisLength) + targetColumn;
                newMatrix[targetIndex] = srcMatrix[srcIndex];
            }
        }
        return newMatrix;
    }

    private static CraftSubmatrix getMinimumSubmatrix(ItemStack[] srcMatrix)
    {
        int srcAxisLength = (int) Math.ceil(Math.sqrt(srcMatrix.length));
        int minRow = srcAxisLength;
        int minColumn = srcAxisLength;
        int maxRow = 0;
        int maxColumn = 0;
        for (int i = 0; i < srcMatrix.length; i++)
        {
            // Get the row & column this index maps to in the matrix...
            int column = i % srcAxisLength;
            int row = (i - column) / srcAxisLength;

            // If the index has an item, make sure the row & column are included.
            if (srcMatrix[i] != null)
            {
                minRow = Math.min(row, minRow);
                maxRow = Math.max(row, maxRow);
                minColumn = Math.min(column, minColumn);
                maxColumn = Math.max(column, maxColumn);
            }
        }
        int rowLength = (maxRow + 1) - minRow;
        int columnLength = (maxColumn + 1) - minColumn;
        int axisLength = Math.max(rowLength, columnLength);

        return new CraftSubmatrix(minRow, minColumn, axisLength);
    }
}
