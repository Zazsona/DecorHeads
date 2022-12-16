package com.zazsona.decorheads.command;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.config.HeadConfig;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.crafting.IMetaRecipe;
import com.zazsona.decorheads.crafting.MetaRecipeManager;
import com.zazsona.decorheads.crafting.ShapedMetaRecipe;
import com.zazsona.decorheads.crafting.ShapelessMetaRecipe;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.drops.drops.IDrop;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WikiCommand implements CommandExecutor
{
    public static final String COMMAND_KEY = "dhwiki";
    public static final String SOURCES_KEY = "sources";
    public static final String LIST_KEY = "list";
    public static final String PREVIEW_KEY = "preview";

    private static final String SOURCES_USAGE = "sources [Head Name] (Page #)";
    private static final String LIST_USAGE = "list (Page #)";
    private static final String PREVIEW_USAGE = "preview [Head Name]";

    private HashMap<String, PreviewInventory> previewInventories = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        try
        {
            String wikiCategoryKey = args[0];
            if (wikiCategoryKey.equalsIgnoreCase(LIST_KEY))
                sendHeadsList(sender, args);
            else if (wikiCategoryKey.equalsIgnoreCase(SOURCES_KEY))
                sendHeadSource(sender, args);
            else if (wikiCategoryKey.equalsIgnoreCase(PREVIEW_KEY))
                sendHeadPreview(sender, args);
            else
                throw new IllegalArgumentException(String.format("Unknown submenu: %s", wikiCategoryKey));
        }
        catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e)
        {
            StringBuilder helpBuilder = new StringBuilder();
            helpBuilder.append(ChatColor.GOLD).append("/").append(COMMAND_KEY).append(" ").append(LIST_USAGE).append(ChatColor.WHITE).append(": List all heads").append("\n");
            helpBuilder.append(ChatColor.GOLD).append("/").append(COMMAND_KEY).append(" ").append(PREVIEW_USAGE).append(ChatColor.WHITE).append(": Preview head appearance").append("\n");
            helpBuilder.append(ChatColor.GOLD).append("/").append(COMMAND_KEY).append(" ").append(SOURCES_USAGE).append(ChatColor.WHITE).append(": Craft/Drop Info").append("\n");
            String page = CommandUtil.addHeader("DecorHeads Wiki", helpBuilder.toString().trim());
            sender.sendMessage(page);
        }
        finally
        {
            return true;
        }
    }

    private void sendHeadSource(CommandSender sender, String[] args)    // This is a pretty long-winded method, but honestly, everything with user input seems to wind up this way. Can't see any approaches to minimising it.
    {
        boolean pageSpecified = args[args.length-1].matches("[0-9]+");
        int headNameEnd = (pageSpecified) ? args.length-1 : args.length;
        StringBuilder headNameBuilder = new StringBuilder();
        for (int i = 1; i < headNameEnd; i++)
            headNameBuilder.append(args[i]).append(" ");

        String headName = headNameBuilder.toString().trim();
        IHead head = HeadConfig.matchLoadedHead(headName);
        if (head != null)
        {
            List<IDrop> drops = HeadConfig.getDropsFor(head.getKey());
            List<IMetaRecipe> recipes = HeadConfig.getRecipesFor(head.getKey());
            drops.sort(Comparator.comparing(IDrop::getKey));
            recipes.sort(Comparator.comparing(recipe -> recipe.getKey().getKey()));
            int totalSources = drops.size() + recipes.size();

            if (totalSources == 0)
            {
                sender.sendMessage(ChatColor.RED+String.format("Head \"%s\" has no sources.", headName));
                return;
            }

            int pageNo = getPageNo(args[args.length-1], 1, totalSources);
            IDrop drop = ((pageNo - 1) < drops.size()) ? drops.get(pageNo - 1) : null;
            IMetaRecipe recipe = (((pageNo - 1) - drops.size()) >= 0) ? recipes.get((pageNo - 1) - drops.size()) : null;

            IWikiPage wikiPage = null;
            if (drop != null)
                wikiPage = new WikiDropPage(drop);
            if (recipe != null && recipe instanceof ShapedMetaRecipe)
                wikiPage = new WikiCraftRecipePage((ShapedMetaRecipe) recipe);
            if (recipe != null && recipe instanceof ShapelessMetaRecipe)
                wikiPage = new WikiCraftRecipePage((ShapelessMetaRecipe) recipe);

            String headerText = String.format("Head Sources (%d/%d)", pageNo, totalSources);
            boolean isDisabled = (drop != null) ? isDropEnabled(drop) : isRecipeEnabled(recipe);
            String disabledNotice = (isDisabled) ? "Note: This source is currently disabled." : null;
            String pageContent = (isDisabled) ? disabledNotice + "\n" + wikiPage.getPage() : wikiPage.getPage();
            String message = CommandUtil.addHeader(headerText, pageContent);
            sender.sendMessage("\n"+message); // New line to make it easier to read against previously opened pages.

            if (PluginConfig.isLearnRecipesFromWikiEnabled() && recipe != null && sender instanceof Player)
            {
                Player player = (Player) sender;                 // They've got Wiki page access, may as well give the recipe in the prettier UI
                player.discoverRecipe(recipe.getKey());
            }
        }
        else if (headName == null || headName.equals(""))
            sender.sendMessage(ChatColor.RED+String.format("Usage: /%s %s", COMMAND_KEY, SOURCES_USAGE));
        else if (head == null)
            sender.sendMessage(ChatColor.RED+String.format("Unrecognised head: \"%s\"", headName));
    }

    private boolean isRecipeEnabled(IMetaRecipe recipe)
    {
        return (PluginConfig.isPluginEnabled() && PluginConfig.isCraftingEnabled() && MetaRecipeManager.getRecipe(recipe.getKey()) != null);
    }

    private boolean isDropEnabled(IDrop drop)
    {
        return (PluginConfig.isPluginEnabled() && PluginConfig.isDropsEnabled() && PluginConfig.isDropTypeEnabled(drop.getDropType()));
    }

    private void sendHeadsList(CommandSender sender, String[] args)
    {
        ArrayList<IHead> heads = new ArrayList<>(HeadConfig.getLoadedHeads().values());
        heads.sort(Comparator.comparing(IHead::getPrettyName));
        int headsPerPage = 8;
        int pages = (int) (Math.ceil(heads.size() / headsPerPage) + 1);
        int pageNo = getPageNo(args[args.length-1], 1, pages);
        StringBuilder sb = new StringBuilder();
        int startingHeadIndex = headsPerPage*(pageNo - 1);
        int endingHeadIndex = Math.min(startingHeadIndex+headsPerPage, heads.size());
        for (int i = startingHeadIndex; i<endingHeadIndex; i++)
        {
            IHead head = heads.get(i);
            sb.append(head.getPrettyName()).append("\n");
        }
        String headerText = String.format("Heads (%d/%d)", pageNo, pages);
        String page = CommandUtil.addHeader(headerText, sb.toString().trim());
        sender.sendMessage(page);
    }

    private void sendHeadPreview(CommandSender sender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED+"This command is not available on the console.");
            return;
        }

        StringBuilder headNameBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++)
            headNameBuilder.append(args[i]).append(" ");
        String headName = headNameBuilder.toString().trim();
        if (headName == null || headName.equals(""))
        {
            sender.sendMessage(ChatColor.RED+String.format("Usage: /%s %s", COMMAND_KEY, PREVIEW_USAGE));
            return;
        }

        IHead head = HeadConfig.matchLoadedHead(headName);
        if (head == null)
        {
            sender.sendMessage(ChatColor.RED+String.format("Unrecognised head: \"%s\"", headName));
            return;
        }

        String playerName = DecorHeadsUtil.extractPlayerNameFromHead(headName, head.getName());
        PreviewInventory previewInventory = getPreviewInventory(head, playerName);
        Player player = (Player) sender;
        previewInventory.showInventory(player);
    }

    private int getPageNo(String pageParam, int minPage, int maxPage)
    {
        int page = minPage;
        if (pageParam.matches("[0-9]+"))
        {

            page = Integer.parseInt(pageParam);
            if (page < minPage)
                page = minPage;
            if (page > maxPage)
                page = maxPage;
        }
        return page;
    }

    private PreviewInventory getPreviewInventory(IHead head, @Nullable String playerName)
    {
        String inventoryKey = head.getKey();
        if (head instanceof PlayerHead && playerName != null)
            inventoryKey += ":" + playerName;

        if (previewInventories.containsKey(inventoryKey))
            return previewInventories.get(inventoryKey);

        ItemStack headStack = (head instanceof PlayerHead && playerName != null) ? ((PlayerHead) head).createItem(playerName) : head.createItem();
        PreviewInventory previewInventory = new PreviewInventory(headStack);
        Core.getSelfPlugin().getServer().getPluginManager().registerEvents(previewInventory, Core.getSelfPlugin());
        previewInventories.put(inventoryKey, previewInventory);
        return previewInventory;
    }
}
