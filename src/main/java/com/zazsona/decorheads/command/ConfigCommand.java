package com.zazsona.decorheads.command;

import com.zazsona.decorheads.config.PluginConfig;
import jdk.tools.jlink.plugin.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ConfigCommand implements CommandExecutor
{
    public static final String COMMAND_KEY = "dhconfig";
    private static final ChatColor onColour = ChatColor.GREEN;
    private static final ChatColor offColour = ChatColor.RED;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length >= 2 && (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("off")))
        {
            String settingName = args[0];
            boolean enable = args[1].equalsIgnoreCase("on");
            String updatedSetting = setState(settingName, enable);
            sender.sendMessage(getCurrentStates());

            if (!enable && (updatedSetting.equalsIgnoreCase(PluginConfig.ENABLED_KEY) || updatedSetting.equalsIgnoreCase(PluginConfig.CRAFTING_KEY)))
            {
                String craftDisabledWarning = ("" + ChatColor.RED + ChatColor.BOLD + "WARNING: " + ChatColor.RESET + ChatColor.RED + "If you reboot the server with crafting disabled, players' head crafting progression data may be lost.");
                sender.sendMessage(craftDisabledWarning);
            }
        }
        else
            sender.sendMessage(getCurrentStates());
        return true;

    }

    private String setState(String setting, boolean value)
    {
        switch (setting.toLowerCase())
        {
            case PluginConfig.ENABLED_KEY:
                PluginConfig.setEnabled(value);
                break;
            case PluginConfig.CRAFTING_KEY:
                PluginConfig.setCraftingEnabled(value);
                break;
            case PluginConfig.DROPS_KEY:
                PluginConfig.setDropsEnabled(value);
                break;
            case PluginConfig.BLOCK_DROPS_KEY:
                PluginConfig.setBlockDropsEnabled(value);
                break;
            case PluginConfig.ENTITY_DROPS_KEY:
                PluginConfig.setEntityDropsEnabled(value);
                break;
            case PluginConfig.PLAYER_DROPS_KEY:
                PluginConfig.setPlayerDropsEnabled(value);
                break;
            case PluginConfig.PLAYERLESS_DROP_EVENTS_KEY:
                PluginConfig.setPlayerlessDropEventsEnabled(value);
                break;
            case PluginConfig.WIKI_RECIPE_LEARN_KEY:
                PluginConfig.setLearnRecipesFromWikiEnabled(value);
                break;
            default:
                return null;
        }
        return setting; //If it passes the switch, setting must be equal to a constant in PluginConfig, so works for displaying the updated setting.
    }

    private String getCurrentStates()
    {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(ChatColor.GRAY).append(String.format("Use /%s [Name] [On|Off] to change config.", COMMAND_KEY)).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.ENABLED_KEY)).append((PluginConfig.isEnabled()) ? onColour : offColour).append((PluginConfig.isEnabled()) ? "On" : "Off").append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.CRAFTING_KEY)).append((PluginConfig.isCraftingEnabled()) ? onColour : offColour).append((PluginConfig.isCraftingEnabled()) ? "On" : "Off").append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.DROPS_KEY)).append((PluginConfig.isDropsEnabled()) ? onColour : offColour).append((PluginConfig.isDropsEnabled()) ? "On" : "Off").append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.BLOCK_DROPS_KEY)).append((PluginConfig.isBlockDropsEnabled()) ? onColour : offColour).append((PluginConfig.isBlockDropsEnabled()) ? "On" : "Off").append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.ENTITY_DROPS_KEY)).append((PluginConfig.isEntityDropsEnabled()) ? onColour : offColour).append((PluginConfig.isEntityDropsEnabled()) ? "On" : "Off").append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.PLAYER_DROPS_KEY)).append((PluginConfig.isPlayerDropsEnabled()) ? onColour : offColour).append((PluginConfig.isPlayerDropsEnabled()) ? "On" : "Off").append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.PLAYERLESS_DROP_EVENTS_KEY)).append((PluginConfig.isPlayerlessDropEventsEnabled()) ? onColour : offColour).append((PluginConfig.isPlayerlessDropEventsEnabled()) ? "On" : "Off").append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.WIKI_RECIPE_LEARN_KEY)).append((PluginConfig.isLearnRecipesFromWikiEnabled()) ? onColour : offColour).append((PluginConfig.isLearnRecipesFromWikiEnabled()) ? "On" : "Off");
        String contentWithHeader = CommandUtil.addHeader("DecorHeads Config", contentBuilder.toString());
        return contentWithHeader;
    }
}
