package com.zazsona.decorheads.command;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.config.HeadRepository;
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
        if (HeadRepository.getLoadedHeads().containsKey(SPAWN_COMMAND_PLAYER_HEAD_ID))
            return;

        IHead playerHead = new PlayerHead(SPAWN_COMMAND_PLAYER_HEAD_ID, String.format("%s's Head", PlayerHead.PLAYER_NAME_PLACEHOLDER));
        HeadRepository.loadHead(playerHead);
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

            if (args.length == 1 && args[0].equalsIgnoreCase("all"))
            {
                for (IHead head : HeadRepository.getLoadedHeads().values())
                {
                    ItemStack headStack = getHeadItem(head, null);
                    spawnHead((Player) sender, headStack);
                }
            }
            else if (args.length == 1)
            {
                IHead head = HeadRepository.matchLoadedHead(args[0].trim());
                String playerName = null;
                if (head == null)
                {
                    // getLoadedHead() is more performant. As the key is hard-coded, there's no room input uncertainty.
                    head = HeadRepository.getLoadedHead(SPAWN_COMMAND_PLAYER_HEAD_ID.trim());
                    playerName = args[0].trim();
                }
                ItemStack headStack = getHeadItem(head, playerName);
                spawnHead((Player) sender, headStack);
            }
            else if (args.length > 1)
            {
                String headIdentifier = StringUtils.join(args, " ", 0, args.length);
                IHead head = HeadRepository.matchLoadedHead(headIdentifier.trim());
                String playerName = DecorHeadsUtil.extractPlayerNameFromHead(headIdentifier, head.getName());
                ItemStack headStack = getHeadItem(head, playerName);
                spawnHead((Player) sender, headStack);
            }
            else
                throw new IllegalArgumentException();
        }
        catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException e)
        {
            String argString = StringUtils.join(args, " ", 0, args.length);
            String usage = (String) DecorHeadsPlugin.getInstance().getDescription().getCommands().get(COMMAND_KEY).get("usage");
            sender.sendMessage(ChatColor.RED + String.format("Unable to find head \"%s\".\nPlease check the head name and any included player names are correct.\nCommand Usage: %s", argString, usage));
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

    private ItemStack getHeadItem(IHead head, @Nullable String playerName) throws InvalidHeadException
    {
        if (head != null)
        {
            if (head instanceof PlayerHead && playerName != null)
                return ((PlayerHead) head).createItem(playerName);
            else
                return head.createItem();
        }
        throw new NullPointerException(String.format("Cannot create item as Head is null."));
    }

    private void spawnHead(Player player, ItemStack headStack)
    {
        if (player.getInventory().firstEmpty() != -1)
            player.getInventory().addItem(headStack);
        else
            player.getWorld().dropItem(player.getLocation(), headStack);
    }
}
