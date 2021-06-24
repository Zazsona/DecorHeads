package com.zazsona.decorheads.command;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.HeadManager;
import com.zazsona.decorheads.config.HeadConfigAccessor;
import com.zazsona.decorheads.config.HeadLoader;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.headdata.TextureHead;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public class SpawnHeadCommand implements CommandExecutor
{
    public static final String COMMAND_KEY = "spawnhead";
    private static final String DECOR_TYPE_KEY = "decor";
    private static final String PLAYER_TYPE_KEY = "player";
    private static final String ALL_KEY = "all";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        try
        {
            if (!(sender instanceof Player))
                sender.sendMessage(ChatColor.RED+"This command is not available on the console.");

            String typeKey = args[0];
            if (typeKey.equalsIgnoreCase(DECOR_TYPE_KEY))
            {
                String headKey = args[1];
                ItemStack headStack = getDecorHead(headKey);
                if (headStack == null)
                {
                    StringBuilder headNameBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++)
                        headNameBuilder.append(args[i]).append(" ");
                    String key = getDecorKey(headNameBuilder.toString().trim());
                    headStack = getDecorHead(key);
                }
                if (headStack == null)
                    sender.sendMessage(ChatColor.RED+String.format("Head \"%s\" not found.", headKey));
                else
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
                throw new IllegalArgumentException(String.format("Unknown argument: %s", typeKey));
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

    private String getDecorKey(String headName)
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

    private ItemStack getDecorHead(String key)
    {
        HeadLoader headLoader = HeadLoader.getInstance();
        IHead head = headLoader.getLoadedHeads().get(key);
        if (head != null)
            return head.createItem();
        else
            return null;
    }

    private ItemStack getPlayerHead(String username)
    {
        //Don't get it from the HeadLoader, as the player head config may be removed
        //As we're forcing the item, we don't care about configuration details anyway.
        PlayerHead playerHead = new PlayerHead(HeadConfigAccessor.playerHeadKey);
        return playerHead.createItem(username);
    }

    private void spawnHead(Player player, ItemStack headStack)
    {
        if (player.getInventory().firstEmpty() != -1)
            player.getInventory().addItem(headStack);
        else
            player.getWorld().dropItem(player.getLocation(), headStack);
    }
}
