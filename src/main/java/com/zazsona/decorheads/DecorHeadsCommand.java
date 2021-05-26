package com.zazsona.decorheads;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DecorHeadsCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(ChatColor.YELLOW + "---------" + ChatColor.WHITE + " DecorHeads ").append(ChatColor.YELLOW).append("--------------------").append(ChatColor.WHITE).append("\n");
            sb.append(ChatColor.GRAY+"Bringing custom heads into the natural Minecraft world!\n"+ChatColor.WHITE);
            sb.append(ChatColor.GOLD+"/DecorHeads Toggle - ").append(ChatColor.WHITE+"Disable/Enable DecorHeads\n");
            sb.append(ChatColor.GOLD+"/DecorHeads SetRate [Head] [Rate] - ").append(ChatColor.WHITE+"Set drop percentage\n");
            sb.append(ChatColor.GOLD+"/DecorHeads GetRate [Head] - ").append(ChatColor.WHITE+"Get drop rate of a head\n");
            sb.append(ChatColor.GOLD+"/DecorHeads List [Page] - ").append(ChatColor.WHITE+"List heads\n");
            sb.append(ChatColor.GOLD+"/DecorHeads Head [Head] - ").append(ChatColor.WHITE+"Get a head\n");
            sb.append(ChatColor.GOLD+"/DecorHeads AllHeads - ").append(ChatColor.WHITE+"Get all heads\n");
            sb.append(ChatColor.GOLD+"/DecorHeads PlayerHead [UUID/Name] - ").append(ChatColor.WHITE+"Get a player's head\n");
            sender.sendMessage(sb.toString());
        }
        else
        {
            if (args[0].equalsIgnoreCase("Toggle"))
            {
                if (sender.hasPermission("DecorHeads.Admin"))
                    toggleHeads(sender);
            }
            else if (args[0].equalsIgnoreCase("SetRate"))
            {
                if (sender.hasPermission("DecorHeads.Admin"))
                    editDropRate(sender, args);
            }
            else if (args[0].equalsIgnoreCase("GetRate"))
            {
                if (sender.hasPermission("DecorHeads.CheckHead"))
                    sendDropRate(sender, args);
            }
            else if (args[0].equalsIgnoreCase("Head"))
            {
                if (sender.hasPermission("DecorHeads.SummonHead"))
                    giveHead(sender, args);
            }
            else if (args[0].equalsIgnoreCase("PlayerHead"))
            {
                if (sender.hasPermission("DecorHeads.SummonHead"))
                    givePlayerHead(sender, args);
            }
            else if (args[0].equalsIgnoreCase("AllHeads"))
            {
                if (sender.hasPermission("DecorHeads.SummonHead"))
                    giveAllHeads(sender);
            }
            else if (args[0].equalsIgnoreCase("list"))
            {
                if (sender.hasPermission("DecorHeads.CheckHead"))
                    listHeads(sender, args);
            }
        }
        return true;
    }

    private void sendDropRate(CommandSender sender, String[] args)
    {
        if (args.length >= 2)
        {
            HeadManager.HeadType head = HeadManager.getHeadByName(args[1]);
            if (head != null)
            {
                double dropChance = Settings.getDropChance(head);
                sender.sendMessage(head.name()+" has a "+ChatColor.DARK_GREEN+dropChance+"%"+ChatColor.WHITE+" chance of dropping.");
            }
            else
                sender.sendMessage(ChatColor.RED+"Unrecognised head. Use /DecorHeads list for a list of heads.");
        }
        else
            sender.sendMessage(ChatColor.RED+"Invalid parameters. Use /DecorHeads for usage instructions.");
    }

    private void editDropRate(CommandSender sender, String[] args)
    {
        if (args.length >= 2)
        {
            HeadManager.HeadType head = HeadManager.getHeadByName(args[1]);
            if (head != null)
            {
                if (sender.hasPermission("DecorHeads.Admin") && args.length >= 3)
                {
                    if (args[2].matches("[0-9]+") || args[2].matches("[0-9]+(\\.[0-9][0-9]?)?"))
                    {
                        double dropRate = Double.parseDouble(args[2]);
                        if (dropRate >= 0.0 && dropRate <= 100.0)
                        {
                            Settings.setDropChance(head, dropRate);
                            sender.sendMessage(ChatColor.GREEN+"Drop rate for "+head.name()+" has been set to "+ChatColor.DARK_GREEN+dropRate+"%"+ChatColor.WHITE+"!");
                        }
                        else
                            sender.sendMessage(ChatColor.RED+"The drop rate must be between 0.00 and 100.00.");
                    }
                    else
                        sender.sendMessage(ChatColor.RED+"Invalid drop rate. It must be a value between 0.00 and 100.00.");
                }
                else
                    sendDropRate(sender, args);
            }
            else
                sender.sendMessage(ChatColor.RED+"Unrecognised head. Use /DecorHeads list for a list of heads.");
        }
        else
            sender.sendMessage(ChatColor.RED+"Invalid parameters. Use /DecorHeads for usage instructions.");
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
                if (head == HeadManager.HeadType.Player)
                    continue;
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

    private void givePlayerHead(CommandSender sender, String[] args)
    {
        if (args.length >= 2)
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                String identifier = args[1];
                if (identifier.length() == 32)
                    identifier = identifier.replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5");

                if (identifier.length() == 36) //UUID
                {
                    UUID uuid = UUID.fromString(identifier);
                    player.getInventory().addItem(HeadManager.getPlayerSkull(uuid));
                }
                else //Player Name
                {
                    player.getInventory().addItem(HeadManager.getPlayerSkull(identifier));
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

    private void listHeads(CommandSender sender, String[] args)
    {
        try
        {
            int headsPerPage = 8;
            int pages = (int) Math.ceil(HeadManager.HeadType.values().length/8);
            int pageNo = getHeadsPageNo(args, pages);
            StringBuilder sb = new StringBuilder();
            sb.append(ChatColor.YELLOW + "---------" + ChatColor.WHITE + " Heads (Page: ").append(pageNo + 1).append("/").append(pages + 1).append(") ").append(ChatColor.YELLOW).append("--------------------").append(ChatColor.WHITE).append("\n");
            sb.append(ChatColor.GRAY + "Use /Decorheads List [n] to get page n of heads.\n" + ChatColor.WHITE);
            int startingHeadIndex = headsPerPage*(pageNo);
            int endingHeadIndex = Math.min(startingHeadIndex+headsPerPage, HeadManager.HeadType.values().length);
            for (int i = startingHeadIndex; i<endingHeadIndex; i++)
            {
                sb.append(HeadManager.HeadType.values()[i].name()).append("\n");
            }
            sender.sendMessage(sb.toString());
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            sender.sendMessage(ChatColor.RED+"That page does not exist!");
        }
    }

    private int getHeadsPageNo(String[] args, int maxPages)
    {
        if (args.length >= 2 && args[1].matches("[0-9]+"))
        {
            return Math.min(maxPages, Integer.parseInt(args[1])-1); //-1 as pages start at 1, but arrays start at 0
        }
        return 0;

    }
}
