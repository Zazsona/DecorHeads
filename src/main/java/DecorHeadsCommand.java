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
                                       "/DecorHeads DropRate [Head] [Percentage] - Set drop rates\n" +
                                       "/DecorHeads Head [Head] - Get a head\n" +
                                       "/DecorHeads AllHeads - Get all heads\n" +
                                       "/DecorHeads List - List all heads");
        }
        else
        {
            if (args[0].equalsIgnoreCase("Toggle"))
            {
                toggleHeads(sender);
            }
            else if (args[0].equalsIgnoreCase("AllHeads"))
            {
                giveAllHeads(sender);
            }
            else if (args[0].equalsIgnoreCase("DropRate"))
            {
                editDropRate(sender, args);
            }
            else if (args[0].equalsIgnoreCase("Head"))
            {
                giveHead(sender, args);
            }
            else if (args[0].equalsIgnoreCase("list"))
            {
                listHeads(sender);
            }
        }
        return true;
    }

    private void editDropRate(CommandSender sender, String[] args)
    {
        if (args.length >= 3)
        {
            HeadManager.HeadType head = HeadManager.getHeadByName(args[1]);
            if (head != null)
            {
                if (args[2].matches("[0-9]+"))
                {
                    int dropRate = Integer.parseInt(args[2]);
                    if (dropRate >= 0 && dropRate <= 100)
                    {
                        Settings.setDropChance(head, dropRate);
                        sender.sendMessage("Drop rate for "+head.name()+" has been set to "+dropRate+"%!");
                    }
                    else
                    {
                        sender.sendMessage("The drop rate must be between 0 and 100.");
                    }

                }
                else
                {
                    sender.sendMessage("Invalid drop rate.");
                }
            }
            else
            {
                sender.sendMessage("Invalid head.");
            }
        }
        else
        {
            sender.sendMessage("Invalid parameters.");
        }
    }

    private void toggleHeads(CommandSender sender)
    {
        boolean isEnabled = Settings.getEnabled();
        Settings.setEnabled(!isEnabled);
        isEnabled = Settings.getEnabled();
        sender.sendMessage("DecorHeads is now "+((isEnabled) ? "enabled." : "disabled."));
    }

    private void giveAllHeads(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            for (HeadManager.HeadType head : HeadManager.HeadType.values())
            {
                player.getInventory().addItem(HeadManager.getSkull(head));
            }
        }
        else
        {
            sender.sendMessage("You are not a player.");
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
                    sender.sendMessage("Invalid head.");
                }
            }
            else
            {
                sender.sendMessage("You are not a player.");
            }
        }
        else
        {
            sender.sendMessage("Invalid parameters.");
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
