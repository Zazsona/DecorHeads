package com.zazsona.decorheads.command;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.config.HeadConfig;
import com.zazsona.decorheads.exceptions.InvalidHeadException;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SpawnHeadCommand implements CommandExecutor
{
    public static final String COMMAND_KEY = "dhspawn";
    public static final String SPAWN_COMMAND_PLAYER_HEAD_ID = "spawn-command-player-head";

    /**
     * Loads the heads required for usage of this command.
     */
    public void loadCommandHeads()
    {
        if (HeadConfig.getLoadedHeads().containsKey(SPAWN_COMMAND_PLAYER_HEAD_ID))
            return;

        IHead playerHead = new PlayerHead(SPAWN_COMMAND_PLAYER_HEAD_ID, String.format("%s's Head", PlayerHead.PLAYER_NAME_PLACEHOLDER));
        HeadConfig.loadHead(playerHead);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        try
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage(ChatColor.RED + "This command is not available on the console.");
                return true;
            }

            if (args.length == 2)
            {
                try
                {
                    ItemStack headStack = getHeadItem(args[1], null);
                    spawnHead((Player) sender, headStack);
                }
                catch (InvalidHeadException e)
                {
                    ItemStack headStack = getHeadItem(SPAWN_COMMAND_PLAYER_HEAD_ID, args[1]);
                    spawnHead((Player) sender, headStack);
                }
            }
            else if (args.length > 2)
            {
                try
                {
                    String headIdentifier = StringUtils.join(args, " ", 1, args.length);
                    ItemStack headStack = getHeadItem(headIdentifier, null);
                    spawnHead((Player) sender, headStack);
                }
                catch (InvalidHeadException e)
                {
                    String headIdentifier = StringUtils.join(args, " ", 1, args.length - 1);
                    String playerName = args[args.length - 1];
                    ItemStack headStack = getHeadItem(headIdentifier, playerName);
                    spawnHead((Player) sender, headStack);
                }
            }
            else
                throw new IllegalArgumentException();
        }
        catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException e)
        {
            String usage = (String) Core.getSelfPlugin().getDescription().getCommands().get(COMMAND_KEY).get("usage");
            sender.sendMessage(ChatColor.RED + String.format("Invalid command arguments. Usage:\n%s", usage));
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

    private ItemStack getHeadItem(String identifier, @Nullable String playerName) throws InvalidHeadException
    {
        IHead head = HeadConfig.matchLoadedHead(identifier.trim());
        if (head != null)
        {
            if (head instanceof PlayerHead && playerName != null)
                return ((PlayerHead) head).createItem(playerName);
            else
                return head.createItem();
        }
        throw new InvalidHeadException(String.format("Head \"%s\" is not a recognised head.", identifier));
    }

    private void spawnHead(Player player, ItemStack headStack)
    {
        if (player.getInventory().firstEmpty() != -1)
            player.getInventory().addItem(headStack);
        else
            player.getWorld().dropItem(player.getLocation(), headStack);
    }
}
