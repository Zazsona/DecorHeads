package com.Zazsona;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DecorHeadsCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage("DecorHeads adds custom heads in a natural way.\n" +
                                       "Commands:\n" +
                                       "/DecorHeads Toggle - Disable/Enable DecorHeads\n" +
                                       "/DecorHeads DropRate [Head] [Rate] - Set drop percentage\n" +
                                       "/DecorHeads DropRate [Head] - Get drop rate of a head\n" +
                                       "/DecorHeads Head [Head] - Get a head\n" +
                                       "/DecorHeads AllHeads - Get all heads\n" +
                                       "/DecorHeads List - List all heads");
        }
        else
        {
            if (args[0].equalsIgnoreCase("Toggle"))
            {
                if (sender.hasPermission("DecorHeads.Admin"))
                    toggleHeads(sender);
            }
            else if (args[0].equalsIgnoreCase("DropRate"))
            {
                if (sender.hasPermission("DecorHeads.Admin"))
                    editDropRate(sender, args);
            }
            else if (args[0].equalsIgnoreCase("Head"))
            {
                if (sender.hasPermission("DecorHeads.Admin") || sender.hasPermission("DecorHeads.SummonHead"))
                    giveHead(sender, args);
            }
            else if (args[0].equalsIgnoreCase("AllHeads"))
            {
                if (sender.hasPermission("DecorHeads.Admin") || sender.hasPermission("DecorHeads.SummonHead"))
                    giveAllHeads(sender);
            }
            else if (args[0].equalsIgnoreCase("list"))
            {
                if (sender.hasPermission("DecorHeads.Admin") || sender.hasPermission("DecorHeads.SummonHead"))
                    listHeads(sender);
            }
        }
        return true;
    }

    private void editDropRate(CommandSender sender, String[] args)
    {
        if (args.length >= 2)
        {
            HeadManager.HeadType head = HeadManager.getHeadByName(args[1]);
            if (head != null)
            {
                if (args.length >= 3)
                {
                    if (args[2].matches("[0-9]+") || args[2].matches("[0-9]+(\\.[0-9][0-9]?)?"))
                    {
                        double dropRate = Double.parseDouble(args[2]);
                        if (dropRate >= 0.0 && dropRate <= 100.0)
                        {
                            Settings.setDropChance(head, dropRate);
                            sender.sendMessage(ChatColor.GREEN+"Drop rate for "+head.name()+" has been set to "+dropRate+"%!");
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.RED+"The drop rate must be between 0.00 and 100.00.");
                        }

                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED+"Invalid drop rate. It must be a value between 0.00 and 100.00.");
                    }
                }
                else
                {
                    double dropChance = Settings.getDropChance(head);
                    sender.sendMessage(head.name()+" has a "+dropChance+"% chance of dropping.");
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED+"Unrecognised head. Use /DecorHeads list for a list of heads.");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED+"Invalid parameters. Use /DecorHeads for usage instructions.");
        }
    }

    private void toggleHeads(CommandSender sender)
    {
        boolean isEnabled = Settings.isEnabled();
        Settings.setEnabled(!isEnabled);
        isEnabled = Settings.isEnabled();
        sender.sendMessage(ChatColor.GREEN+"DecorHeads is now "+((isEnabled) ? ChatColor.BLUE+"enabled." : ChatColor.RED+"disabled."));
    }

    private void giveAllHeads(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            for (HeadManager.HeadType head : HeadManager.HeadType.values())
            {
                if (player.getInventory().firstEmpty() != -1)
                {
                    player.getInventory().addItem(HeadManager.getSkull(head));
                }
                else
                {
                    player.getWorld().dropItem(player.getLocation(), HeadManager.getSkull(head));
                }
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED+"You are not a player.");
        }
    }

    private void giveHead(CommandSender sender, String[] args)
    {
        if (args.length >= 2)
        {
            if (sender instanceof Player)
            {
                HeadManager.HeadType head = HeadManager.getHeadByName(args[1]);
                if (head != null)
                {
                    Player player = (Player) sender;
                    player.getInventory().addItem(HeadManager.getSkull(head));
                }
                else
                {
                    sender.sendMessage(ChatColor.RED+"Unrecognised head. Use /DecorHeads list for a list of heads.");
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED+"You are not a player.");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED+"Invalid parameters. Use /DecorHeads for usage instructions.");
        }
    }

    private void listHeads(CommandSender sender)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Heads:\n");
        for (HeadManager.HeadType head : HeadManager.HeadType.values())
        {
            sb.append(head.name()).append(", ");
        }
        sb.setLength(sb.length()-2);
        sender.sendMessage(sb.toString());
    }
}
