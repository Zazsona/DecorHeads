package com.zazsona.decorheads.command;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.config.HeadLoader;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.TextureHead;
import com.zazsona.decorheads.headsources.*;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

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

            int pageNo = getPageNo(args[args.length-1], 1, sourceKeys.size());
            String sourceKey = sourceKeys.get((pageNo - 1));
            HeadSource headSource = headLoader.getLoadedHeadSources().get(head.getKey()).get(sourceKey);
            if (headSource != null)
            {
                IWikiPage page = getHeadSourcePage(headSource);
                String headerText = String.format("Head Sources (%d/%d)", pageNo, sourceKeys.size());
                String disabledNotice = getSourceTypeDisabledNotice(headSource.getSourceType());
                String pageContent = (disabledNotice == null) ? page.getPage() : disabledNotice + "\n" + page.getPage();
                sender.sendMessage(CommandUtil.addHeader(headerText, pageContent));

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
            sender.sendMessage(ChatColor.RED+String.format("Head \"%s\" has no sources.", headName));
    }

    private String getSourceTypeDisabledNotice(HeadSourceType sourceType)
    {
        String lineTemplate = ChatColor.GRAY+"Note: %s is currently disabled."+ChatColor.RESET;
        if (!PluginConfig.isPluginEnabled())
            return String.format(lineTemplate, Core.PLUGIN_NAME);
        else if ((sourceType == HeadSourceType.SHAPED_CRAFT || sourceType == HeadSourceType.SHAPELESS_CRAFT) && !PluginConfig.isCraftingEnabled())
            return String.format(lineTemplate, "Head crafting");
        else if (!PluginConfig.isDropSourceEnabled(sourceType) || !PluginConfig.isDropsEnabled())
            return String.format(lineTemplate, DecorHeadsUtil.capitaliseName(sourceType.name()));
        else
            return null;
    }

    private void sendHeadsList(CommandSender sender, String[] args)
    {
        HeadLoader headLoader = HeadLoader.getInstance();
        ArrayList<IHead> heads = new ArrayList<>(headLoader.getLoadedHeads().values());
        heads.sort((o1, o2) ->
                   {
                       String head1Name = (o1 instanceof TextureHead) ? ((TextureHead) o1).getName() : DecorHeadsUtil.capitaliseName(o1.getKey());
                       String head2Name = (o2 instanceof TextureHead) ? ((TextureHead) o2).getName() : DecorHeadsUtil.capitaliseName(o2.getKey());
                       return head1Name.compareTo(head2Name);
                   });
        int headsPerPage = 8;
        int pages = (int) (Math.ceil(heads.size() / headsPerPage) + 1);
        int pageNo = getPageNo(args[args.length-1], 1, pages);
        StringBuilder sb = new StringBuilder();
        int startingHeadIndex = headsPerPage*(pageNo - 1);
        int endingHeadIndex = Math.min(startingHeadIndex+headsPerPage, heads.size());
        for (int i = startingHeadIndex; i<endingHeadIndex; i++)
        {
            IHead head = heads.get(i);
            String headName = (head instanceof TextureHead) ? ((TextureHead) head).getName() : DecorHeadsUtil.capitaliseName(head.getKey());
            sb.append(headName).append("\n");
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
        IHead head = getHead(headName);
        if (head != null && head instanceof TextureHead && sender instanceof Player)
        {
            PreviewInventory previewInventory = getPreviewInventory((TextureHead) head);
            Player player = (Player) sender;
            previewInventory.showInventory(player);
        }
        else if (headName == null || headName.equals(""))
            sender.sendMessage(ChatColor.RED+String.format("Usage: /%s %s", COMMAND_KEY, PREVIEW_USAGE));
        else if (head == null)
            sender.sendMessage(ChatColor.RED+String.format("Unrecognised head: \"%s\"", headName));
        else if (!(head instanceof TextureHead))
            sender.sendMessage(ChatColor.RED+String.format("Sorry, player head previews are not supported."));
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

    private PreviewInventory getPreviewInventory(TextureHead head)
    {
        if (!previewInventories.containsKey(head.getKey()))
        {
            ItemStack headStack = head.createItem();
            PreviewInventory previewInventory = new PreviewInventory(headStack);
            Core.getSelfPlugin().getServer().getPluginManager().registerEvents(previewInventory, Core.getSelfPlugin());
            previewInventories.put(head.getKey(), previewInventory);    //Cache preview heads for re-use so there's no memory leak, as the listener retains a reference stopping GC.
        }
        return previewInventories.get(head.getKey());
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
