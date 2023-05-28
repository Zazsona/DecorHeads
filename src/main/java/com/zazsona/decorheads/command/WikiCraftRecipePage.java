package com.zazsona.decorheads.command;

import com.zazsona.decorheads.crafting.MetaIngredient;
import com.zazsona.decorheads.crafting.ShapedMetaRecipe;
import com.zazsona.decorheads.crafting.ShapelessMetaRecipe;
import com.zazsona.decorheads.headdata.Head;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class WikiCraftRecipePage implements IWikiPage
{
    private final String borderChar = "\u2B1B";
    private final String shapedSlotChar = "\u2B1B";
    private final String shapelessSlotChar = "\u2B1C";
    private final int slotSize = 2;
    private final ChatColor[] ingredientColours = new ChatColor[] {ChatColor.RED, ChatColor.BLUE, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.LIGHT_PURPLE, ChatColor.DARK_RED, ChatColor.DARK_AQUA, ChatColor.DARK_GREEN, ChatColor.GOLD};
    private final ChatColor neutralColour = ChatColor.WHITE;

    private String page;

    public WikiCraftRecipePage(ShapedMetaRecipe shapedRecipe)
    {
        ArrayList<MetaIngredient> ingredients = new ArrayList<>(shapedRecipe.getIngredientMap().values());
        String craftGrid = buildGridDisplay(shapedSlotChar, shapedRecipe.getShape(), shapedRecipe.getIngredientMap(), ingredients);
        String[] gridRows = craftGrid.split("\n");
        String[] recipeKey = buildRecipeKey(gridRows.length, ingredients);
        this.page = buildPage(gridRows, recipeKey);
    }

    public WikiCraftRecipePage(ShapelessMetaRecipe shapelessRecipe)
    {
        String craftGrid = buildShapelessGridDisplay(shapelessRecipe.getIngredientList());
        String[] gridRows = craftGrid.split("\n");
        String[] recipeKey = buildRecipeKey(gridRows.length, shapelessRecipe.getIngredientList());
        this.page = buildPage(gridRows, recipeKey);
    }

    public String getPage()
    {
        return page;
    }

    private String buildShapelessGridDisplay(List<MetaIngredient> orderedIngredients)
    {
        int gridSize;
        if (orderedIngredients.size() == 1)
            gridSize = 1;
        else if (orderedIngredients.size() <= 4)
            gridSize = 2;
        else
            gridSize = 3;
        String[] shapeGrid = new String[gridSize];
        for (int y = 0; y < gridSize; y++)
        {
            StringBuilder rowBuilder = new StringBuilder();
            for (int x = 0; x < gridSize; x++)
            {
                int baseIndex = gridSize * y;
                int index = baseIndex + x;
                if (index < orderedIngredients.size())
                    rowBuilder.append(index);
                else
                    rowBuilder.append(" ");
            }
            shapeGrid[y] = rowBuilder.toString();
        }

        HashMap<Character, MetaIngredient> ingredientMap = new HashMap<>();
        for (int i = 0; i < orderedIngredients.size(); i++)
        {
            ingredientMap.put(String.valueOf(i).charAt(0), orderedIngredients.get(i));
        }
        if (ingredientMap.size() < 9)
            ingredientMap.put(' ', null);
        return buildGridDisplay(shapelessSlotChar, shapeGrid, ingredientMap, orderedIngredients);
    }

    private String buildGridDisplay(String slotChar, String[] shape, Map<Character, MetaIngredient> ingredientMap, List<MetaIngredient> orderedIngredients)
    {
        StringBuilder gridBuilder = new StringBuilder();
        int gridLength = (slotSize * 3) + 2;
        for (int i = 0; i < gridLength; i++)
            gridBuilder.append(borderChar);
        gridBuilder.append("\n");
        for (int y = 0; y < 3; y++)
        {
            String[] row = (y < shape.length) ? shape[y].split("") : new String[0];
            for (int slotY = 0; slotY < slotSize; slotY++)
            {
                gridBuilder.append(neutralColour).append(borderChar);
                for (int x = 0; x < 3; x++)
                {
                    Character ingredientChar = (x < row.length) ? row[x].charAt(0) : ' ';
                    MetaIngredient ingredient = (ingredientChar != ' ') ? ingredientMap.get(ingredientChar) : null;
                    int index = orderedIngredients.indexOf(ingredient);
                    ChatColor colour = (index > -1 && ingredientChar != ' ') ? ingredientColours[index] : neutralColour;
                    for (int slotX = 0; slotX < slotSize; slotX++)
                        gridBuilder.append(colour).append(slotChar);
                }
                gridBuilder.append(neutralColour).append(borderChar);
                gridBuilder.append("\n");
            }
        }
        for (int i = 0; i < gridLength; i++)
            gridBuilder.append(borderChar);
        return gridBuilder.toString();
    }

    private String[] buildRecipeKey(int linesCount, List<MetaIngredient> orderedIngredients)
    {
        String[] lines = new String[linesCount];
        int startLine = Math.max(0, ((lines.length / 2) - (orderedIngredients.size() / 2)));
        int endLine = Math.min(lines.length, startLine + orderedIngredients.size());
        int requiredMultiEntryLines = Math.max(0, orderedIngredients.size() - lines.length);
        for (int i = startLine; i < endLine; i++)
        {
            int ingredientIndex = (i - startLine);
            String entry = buildKeyEntry(orderedIngredients, ingredientIndex, true);
            if (requiredMultiEntryLines > 0)
            {
                int secondaryIngredientIndex = orderedIngredients.size()-requiredMultiEntryLines;
                String secondaryEntry = buildKeyEntry(orderedIngredients, secondaryIngredientIndex, false);
                entry += secondaryEntry;
                requiredMultiEntryLines--;
            }
            lines[i] = entry.trim();
        }
        return lines;
    }

    private String buildKeyEntry(List<MetaIngredient> orderedIngredients, int ingredientIndex, boolean firstInLine)
    {
        StringBuilder entryBuilder = new StringBuilder();
        MetaIngredient ingredient = orderedIngredients.get(ingredientIndex);
        ChatColor colour = ingredientColours[ingredientIndex];
        String ingredientName = (ingredient.getItemMeta().hasDisplayName()) ? ingredient.getItemMeta().getDisplayName() : ingredient.getItemMeta().getLocalizedName();
        if (ingredient.getItemMeta().getPersistentDataContainer().has(Head.getSkullHeadKeyKey(), PersistentDataType.STRING))
            ingredientName += " (Head)";

        if (!firstInLine)
            entryBuilder.append("     /     ");
        entryBuilder
                .append(colour)
                .append(borderChar)
                .append(neutralColour)
                .append(" ")
                .append(ingredientName);
        return entryBuilder.toString();
    }

    private String buildPage(String[] grid, String[] key)
    {
        StringBuilder pageBuilder = new StringBuilder();
        for (int i = 0; i < grid.length; i++)
        {
            pageBuilder
                    .append(grid[i])
                    .append("     ")
                    .append((i >= key.length || key[i] == null) ? "" : key[i])
                    .append("\n");
        }
        return pageBuilder.toString();
    }
}
