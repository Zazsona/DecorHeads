package com.zazsona.decorheads.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Map;

public class WikiShapedCraftRecipePage
{
    private final String craftSlotChar = "\u2B1B";
    private final int slotSize = 2;
    private final ChatColor[] ingredientColours = new ChatColor[] {ChatColor.RED, ChatColor.BLUE, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.LIGHT_PURPLE, ChatColor.DARK_RED, ChatColor.DARK_AQUA, ChatColor.DARK_GREEN, ChatColor.GOLD};
    private final ChatColor neutralColour = ChatColor.WHITE;

    private ShapedRecipe shapedRecipe;
    private ArrayList<Character> ingredientKeys;

    public WikiShapedCraftRecipePage(ShapedRecipe shapedRecipe)
    {
        this.shapedRecipe = shapedRecipe;
        this.ingredientKeys = new ArrayList<>(shapedRecipe.getIngredientMap().keySet());
        this.ingredientKeys.remove(Character.valueOf(' '));
    }

    public String buildPage()
    {
        return buildCraftGridDisplay();
    }

    private String buildCraftGridDisplay()
    {
        String[] shape = shapedRecipe.getShape();
        StringBuilder gridBuilder = new StringBuilder();
        int gridLength = (slotSize * 3) + 2;
        for (int i = 0; i < gridLength; i++)
            gridBuilder.append(craftSlotChar);
        gridBuilder.append("\n");
        for (int y = 0; y < 3; y++)
        {
            String[] row = (y < shape.length) ? shape[y].split("") : new String[0];
            for (int slotY = 0; slotY < slotSize; slotY++)
            {
                gridBuilder.append(neutralColour).append(craftSlotChar);
                for (int x = 0; x < 3; x++)
                {
                    Character ingredientChar = (x < row.length) ? row[x].charAt(0) : ' ';
                    int charIndex = ingredientKeys.indexOf(ingredientChar);
                    ChatColor colour = (charIndex > -1 && ingredientChar != ' ') ? ingredientColours[charIndex] : neutralColour;
                    for (int slotX = 0; slotX < slotSize; slotX++)
                        gridBuilder.append(colour).append(craftSlotChar);
                }
                gridBuilder.append(neutralColour).append(craftSlotChar);
                gridBuilder.append("\n");
            }
        }
        for (int i = 0; i < gridLength; i++)
            gridBuilder.append(craftSlotChar);
        return buildGridRecipeKey(gridBuilder.toString());
    }

    private String buildGridRecipeKey(String grid)
    {
        String[] gridLines = grid.split("\n");
        int startLine = Math.max(0, ((gridLines.length / 2) - (ingredientKeys.size() / 2)));
        int endLine = Math.min(gridLines.length, startLine + ingredientKeys.size());
        int requiredMultiEntryLines = Math.max(0, ingredientKeys.size() - gridLines.length);
        for (int i = startLine; i < endLine; i++)
        {
            int ingredientIndex = (i - startLine);
            String entry = buildKeyEntry(ingredientIndex, true);
            if (requiredMultiEntryLines > 0)
            {
                int secondaryIngredientIndex = ingredientKeys.size()-requiredMultiEntryLines;
                String secondaryEntry = buildKeyEntry(secondaryIngredientIndex, false);
                entry += secondaryEntry;
                requiredMultiEntryLines--;
            }
            gridLines[i] += entry;
        }

        StringBuilder pageBuilder = new StringBuilder();
        for (String line : gridLines)
            pageBuilder.append(line).append("\n");
        return pageBuilder.toString();
    }

    private String buildKeyEntry(int ingredientIndex, boolean firstInLine)
    {
        StringBuilder entryBuilder = new StringBuilder();
        Character ingredientKey = ingredientKeys.get(ingredientIndex);
        ChatColor colour = ingredientColours[ingredientIndex];
        if (ingredientKey != ' ')
        {
            entryBuilder
                    .append((firstInLine) ? "     " : "     /     ")
                    .append(colour)
                    .append(craftSlotChar)
                    .append(neutralColour)
                    .append(" ")
                    .append(shapedRecipe.getIngredientMap().get(ingredientKey).getType().name().replace("_", " "));
        }
        return entryBuilder.toString();
    }
}
