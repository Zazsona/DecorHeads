package com.zazsona.decorheads.command;

import com.zazsona.decorheads.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MasterCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        return Bukkit.dispatchCommand(sender, String.format("%s %s", "help", Core.PLUGIN_NAME));
    }
}
