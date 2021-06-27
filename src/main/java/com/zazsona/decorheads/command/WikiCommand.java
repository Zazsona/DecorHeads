package com.zazsona.decorheads.command;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.config.HeadLoader;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.headdata.TextureHead;
import com.zazsona.decorheads.headsources.*;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class WikiCommand implements CommandExecutor
{
    public static final String COMMAND_KEY = "decorheads";
    public static final String SOURCES_KEY = "sources";

    private static final String SOURCES_USAGE = "sources [Head Name] (Page #)";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        try
        {
            String wikiCategoryKey = args[0];
            if (wikiCategoryKey.equalsIgnoreCase(SOURCES_KEY))
            {
                sendHeadSource(sender, args);
            }
            else
            {

            }
        }
        catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e)
        {
            String usage = (String) Core.getSelfPlugin().getDescription().getCommands().get(COMMAND_KEY).get("usage");
            sender.sendMessage(ChatColor.RED+String.format("Invalid command arguments. Usage:\n%s", usage));
        }
        finally
        {
            return true;
        }
    }

    private void sendHeadSource(CommandSender sender, String[] args)    //This is a pretty long-winded method, but honestly, everything with user input seems to wind up this way. Can't see any approaches to minimising it.
    {
        boolean pageSpecified = args[args.length-1].matches("[0-9]+");
        int headNameEnd = (pageSpecified) ? args.length-1 : args.length;
        StringBuilder headNameBuilder = new StringBuilder();
        for (int i = 1; i < headNameEnd; i++)
            headNameBuilder.append(args[i]).append(" ");

        HeadLoader headLoader = HeadLoader.getInstance();
        String headName = headNameBuilder.toString().trim();
        IHead head = getHead(headName);
        if (head != null && headLoader.getLoadedHeadSources().get(head.getKey()) != null)
        {
            ArrayList<String> sourceKeys = new ArrayList<>();
            sourceKeys.addAll(headLoader.getLoadedHeadSources().get(head.getKey()).keySet());
            sourceKeys.sort(Comparator.naturalOrder());

            int pageNo = (pageSpecified) ? Integer.parseInt(args[args.length-1]) : 1;
            pageNo = (pageNo > sourceKeys.size()) ? sourceKeys.size() : pageNo;
            String sourceKey = sourceKeys.get((pageNo - 1));
            HeadSource headSource = headLoader.getLoadedHeadSources().get(head.getKey()).get(sourceKey);
            if (headSource != null)
            {
                IWikiPage page = getHeadSourcePage(headSource);
                String headerText = String.format("Head Sources (%d/%d)", pageNo, sourceKeys.size());
                sender.sendMessage(addHeader(headerText, page.getPage()));

                if (headSource instanceof CraftHeadSource && sender instanceof Player) //TODO: Make configurable?
                {
                    CraftHeadSource craftHeadSource = (CraftHeadSource) headSource;
                    Player player = (Player) sender;                                        //They've got Wiki page access, may as well give the recipe in the prettier UI.
                    NamespacedKey recipeKey = ((Keyed) craftHeadSource.getRecipe()).getKey();
                    player.discoverRecipe(recipeKey);
                }
            }
            else
                sender.sendMessage(ChatColor.RED+String.format("Could not load page for head source: \"%s\"", sourceKey));
        }
        else if (headName == null || headName.equals(""))
            sender.sendMessage(ChatColor.RED+String.format("Usage: /%s %s", COMMAND_KEY, SOURCES_USAGE));
        else if (head == null)
            sender.sendMessage(ChatColor.RED+String.format("Unrecognised head: \"%s\"", headName));
        else if (headLoader.getLoadedHeadSources().get(head.getKey()) == null)
            sender.sendMessage(ChatColor.RED+String.format("Head \"%s\"has no sources.", headName));
    }

    private String addHeader(String headerText, String content)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.YELLOW).append("---------");
        sb.append(ChatColor.WHITE).append(" ").append(headerText).append(" ");
        sb.append(ChatColor.YELLOW).append("--------------------");
        sb.append(ChatColor.WHITE).append("\n");
        sb.append(content);
        return sb.toString();
    }

    private IWikiPage getHeadSourcePage(HeadSource headSource)
    {
        IWikiPage wikiPage = null;
        if (headSource instanceof DropHeadSource)
            wikiPage = new WikiDropPage((DropHeadSource) headSource);
        else if (headSource instanceof CraftHeadSource)
        {
            CraftHeadSource craftHeadSource = (CraftHeadSource) headSource;
            Recipe recipe = craftHeadSource.getRecipe();
            if (recipe instanceof ShapedRecipe)
                wikiPage = new WikiCraftRecipePage((ShapedRecipe) recipe);
            else if (recipe instanceof ShapelessRecipe)
                wikiPage = new WikiCraftRecipePage((ShapelessRecipe) recipe);
        }
        return wikiPage;
    }

    private IHead getHead(String identifier)
    {
        HeadLoader headLoader = HeadLoader.getInstance();
        if (!headLoader.getLoadedHeads().containsKey(identifier))
            identifier = getHeadKey(identifier);
        return headLoader.getLoadedHeads().get(identifier);
    }

    private String getHeadKey(String headName)
    {
        HeadLoader headLoader = HeadLoader.getInstance();
        for (String loadedHeadKey : headLoader.getLoadedHeads().keySet())
        {
            IHead head = headLoader.getLoadedHeads().get(loadedHeadKey);
            if (head != null && head instanceof TextureHead)
            {
                TextureHead textureHead = (TextureHead) head;
                if (textureHead.getName().equalsIgnoreCase(headName))
                    return textureHead.getKey();
            }
        }
        return null;
    }
}
