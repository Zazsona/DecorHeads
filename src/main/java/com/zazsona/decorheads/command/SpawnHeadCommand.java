package com.zazsona.decorheads.command;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.config.HeadConfigAccessor;
import com.zazsona.decorheads.config.HeadLoader;
import com.zazsona.decorheads.exceptions.InvalidHeadException;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.headdata.TextureHead;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class SpawnHeadCommand implements CommandExecutor
{
    public static final String COMMAND_KEY = "dhspawn";
    private static final String DECOR_TYPE_KEY = "decor";
    private static final String PLAYER_TYPE_KEY = "player";
    private static final String ALL_KEY = "all";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        try
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage(ChatColor.RED+"This command is not available on the console.");
                return true;
            }

            String typeKey = args[0];
            if (typeKey.equalsIgnoreCase(DECOR_TYPE_KEY))
            {
                String headIdentifier = StringUtils.join(args, " ", 1, args.length);
                ItemStack headStack = getDecorHead(headIdentifier);
                spawnHead((Player) sender, headStack);
            }
            else if (typeKey.equalsIgnoreCase(PLAYER_TYPE_KEY))
            {
                String username = args[1];
                ItemStack headStack = getPlayerHead(username);
                spawnHead((Player) sender, headStack);
            }
            else if (typeKey.equalsIgnoreCase(ALL_KEY))
            {
                Player targetPlayer = (Player) sender;
                Collection<IHead> loadedHeads = HeadLoader.getInstance().getLoadedHeads().values();
                for (IHead head : loadedHeads)
                    spawnHead(targetPlayer, head.createItem());
            }
            else
            {
                String headIdentifier = StringUtils.join(args, " ", 0, args.length);
                ItemStack headStack = getAnyHead(headIdentifier);
                spawnHead((Player) sender, headStack);
            }
        }
        catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e)
        {
            String usage = (String) Core.getSelfPlugin().getDescription().getCommands().get(COMMAND_KEY).get("usage");
            sender.sendMessage(ChatColor.RED+String.format("Invalid command arguments. Usage:\n%s", usage));
        }
        catch (InvalidHeadException e)
        {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        finally
        {
            return true;
        }
    }

    private ItemStack getDecorHead(String identifier) throws InvalidHeadException
    {
        HeadLoader headLoader = HeadLoader.getInstance();
        String key = identifier.trim();
        if (!headLoader.getLoadedHeads().containsKey(key))
            key = getDecorHeadKey(identifier);
        IHead head = headLoader.getLoadedHeads().get(key);
        if (head != null)
            return head.createItem();
        else
            throw new InvalidHeadException(String.format("Head \"%s\" is not a recognised head.", identifier));
    }

    private String getDecorHeadKey(String headName)
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

    private ItemStack getPlayerHead(String username) throws InvalidHeadException
    {
        try
        {
            // Don't get it from the HeadLoader, as the player head config may be removed
            // As we're forcing the item, we don't care about configuration details anyway.
            PlayerHead playerHead = new PlayerHead(HeadConfigAccessor.playerHeadKey);
            return playerHead.createItem(username);
        }
        catch (InvalidHeadException e)
        {
            throw new InvalidHeadException(String.format("Player \"%s\" does not exist.", username));
        }
    }

    private ItemStack getAnyHead(String headOrPlayerName) throws InvalidHeadException
    {
        ItemStack headStack;
        try
        {
            headStack = getDecorHead(headOrPlayerName);
            return headStack;
        }
        catch (InvalidHeadException e)
        {
            try
            {
                headStack = getPlayerHead(headOrPlayerName);
                return headStack;
            }
            catch (InvalidHeadException e1)
            {
                throw new InvalidHeadException(String.format("Could not find any head or player with name \"%s\".", headOrPlayerName));
            }
        }
    }

    private void spawnHead(Player player, ItemStack headStack)
    {
        if (player.getInventory().firstEmpty() != -1)
            player.getInventory().addItem(headStack);
        else
            player.getWorld().dropItem(player.getLocation(), headStack);
    }
}
