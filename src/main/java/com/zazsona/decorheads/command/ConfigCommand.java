package com.zazsona.decorheads.command;

import com.zazsona.decorheads.config.PluginConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ConfigCommand implements CommandExecutor
{
    public static final String COMMAND_KEY = "dhconfig";
    private static final String ENABLED_KEY = "plugin";
    private static final String CRAFTING_KEY = "crafting";
    private static final String DROPS_KEY = "drops";
    private static final String MOB_DROPS_KEY = "mob-drops";
    private static final String PLAYER_DROPS_KEY = "player-drops";
    private static final ChatColor onColour = ChatColor.GREEN;
    private static final ChatColor offColour = ChatColor.RED;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length >= 2)
        {
            if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("off"))
            {
                String message = setState(args[0], args[1].equalsIgnoreCase("on"));
                if (message != null)
                {
                    sender.sendMessage(message);
                    return true;
                }
            }
        }
        sender.sendMessage(getCurrentStates());
        return true;
    }

    private String setState(String setting, boolean value)
    {
        String enabledMessageTemplate = "%s "+ChatColor.GREEN+" enabled"+ChatColor.WHITE+".";
        String disabledMessageTemplate = "%s "+ChatColor.RED+" disabled"+ChatColor.WHITE+".";
        switch (setting.toLowerCase())
        {
            case ENABLED_KEY:
                PluginConfig.setEnabled(value);
                return String.format((PluginConfig.isEnabled() ? enabledMessageTemplate : disabledMessageTemplate), ENABLED_KEY);
            case CRAFTING_KEY:
                PluginConfig.setCraftingEnabled(value);
                return String.format((PluginConfig.isCraftingEnabled() ? enabledMessageTemplate : disabledMessageTemplate), CRAFTING_KEY);
            case DROPS_KEY:
                PluginConfig.setDropsEnabled(value);
                return String.format((PluginConfig.isDropsEnabled() ? enabledMessageTemplate : disabledMessageTemplate), DROPS_KEY);
            case MOB_DROPS_KEY:
                PluginConfig.setMobDropsEnabled(value);
                return String.format((PluginConfig.isMobDropsEnabled() ? enabledMessageTemplate : disabledMessageTemplate), MOB_DROPS_KEY);
            case PLAYER_DROPS_KEY:
                PluginConfig.setPlayerDropsEnabled(value);
                return String.format((PluginConfig.isPlayerDropsEnabled() ? enabledMessageTemplate : disabledMessageTemplate), PLAYER_DROPS_KEY);
            default:
                return null;
        }
    }

    private String getCurrentStates()
    {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(ChatColor.DARK_GRAY).append(String.format("Use /%s [Name] [On|Off] to change config.", COMMAND_KEY)).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", ENABLED_KEY)).append((PluginConfig.isEnabled()) ? onColour : offColour).append((PluginConfig.isEnabled()) ? "On" : "Off").append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", CRAFTING_KEY)).append((PluginConfig.isCraftingEnabled()) ? onColour : offColour).append((PluginConfig.isCraftingEnabled()) ? "On" : "Off").append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", DROPS_KEY)).append((PluginConfig.isDropsEnabled()) ? onColour : offColour).append((PluginConfig.isDropsEnabled()) ? "On" : "Off").append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", MOB_DROPS_KEY)).append((PluginConfig.isMobDropsEnabled()) ? onColour : offColour).append((PluginConfig.isMobDropsEnabled()) ? "On" : "Off").append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PLAYER_DROPS_KEY)).append((PluginConfig.isPlayerDropsEnabled()) ? onColour : offColour).append((PluginConfig.isPlayerDropsEnabled()) ? "On" : "Off");
        String contentWithHeader = CommandUtil.addHeader("DecorHeads Config", contentBuilder.toString());
        return contentWithHeader;
    }
}
