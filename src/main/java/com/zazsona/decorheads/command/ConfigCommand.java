package com.zazsona.decorheads.command;

import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headsources.HeadSourceType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ConfigCommand implements CommandExecutor
{
    public static final String COMMAND_KEY = "dhconfig";
    private static final String dropSourcesKey = "drop-sources";
    private static final ChatColor onColour = ChatColor.GREEN;
    private static final ChatColor offColour = ChatColor.RED;
    private static final String onText = "On";
    private static final String offText = "Off";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length >= 1 && args[0].equalsIgnoreCase(dropSourcesKey))
            manageDropSourcesConfig(sender, args);
        else
            manageRootConfig(sender, args);
        return true;

    }

    private void manageRootConfig(CommandSender sender, String[] args)
    {
        if (args.length >= 2 && (args[1].equalsIgnoreCase(onText) || args[1].equalsIgnoreCase(offText)))
        {
            String settingName = args[0];
            boolean enable = args[1].equalsIgnoreCase(onText);
            String updatedSetting = setState(settingName, enable);
            sender.sendMessage(getCurrentStates());
            if (updatedSetting == null)
            {
                sender.sendMessage(String.format(ChatColor.RED+"Unknown setting: %s", settingName));
            }
            else if (!enable && (updatedSetting.equalsIgnoreCase(PluginConfig.PLUGIN_ENABLED_KEY) || updatedSetting.equalsIgnoreCase(PluginConfig.CRAFTING_KEY)))
            {
                String craftDisabledWarning = ("" + ChatColor.RED + ChatColor.BOLD + "NOTE: " + ChatColor.RESET + ChatColor.RED + "To prevent progression data loss, crafting recipes will still be loaded and visible. If you wish to remove crafting permanently, remove all craft sources from heads.yml.");
                sender.sendMessage(craftDisabledWarning);
            }
        }
        else
            sender.sendMessage(getCurrentStates());
    }

    private void manageDropSourcesConfig(CommandSender sender, String[] args)
    {
        if (args.length >= 3 && (args[2].equalsIgnoreCase(onText) || args[2].equalsIgnoreCase(offText)))
        {
            String sourceName = args[1];
            boolean enable = args[2].equalsIgnoreCase(onText);
            String updatedSource = setDropSourceState(sourceName, enable);
            sender.sendMessage(getCurrentDropSourcesStates());
            if (updatedSource == null)
            {
                sender.sendMessage(String.format(ChatColor.RED+"Unknown drop source: %s", sourceName));
            }
        }
        else
            sender.sendMessage(getCurrentDropSourcesStates());
    }

    private String setState(String setting, boolean value)
    {
        switch (setting.toLowerCase())
        {
            case PluginConfig.PLUGIN_ENABLED_KEY:
                PluginConfig.setPluginEnabled(value);
                break;
            case PluginConfig.CRAFTING_KEY:
                PluginConfig.setCraftingEnabled(value);
                break;
            case PluginConfig.DROPS_KEY:
                PluginConfig.setDropsEnabled(value);
                break;
            case PluginConfig.PLAYERLESS_DROP_EVENTS_KEY:
                PluginConfig.setPlayerlessDropEventsEnabled(value);
                break;
            case PluginConfig.WIKI_RECIPE_LEARN_KEY:
                PluginConfig.setLearnRecipesFromWikiEnabled(value);
                break;
            case PluginConfig.UPDATE_NOTIFICATIONS_KEY:
                PluginConfig.setUpdateNotificationsEnabled(value);
                break;
            default:
                return null;
        }
        return setting; //If it passes the switch, setting must be equal to a constant in PluginConfig, so works for displaying the updated setting.
    }

    private String setDropSourceState(String sourceName, boolean value)
    {
        try
        {
            HeadSourceType headSourceType = PluginConfig.convertConfigKeyToSourceType(sourceName);
            PluginConfig.setDropSourceEnabled(headSourceType, value);
            return sourceName;
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }

    private String getCurrentStates()
    {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(ChatColor.DARK_GRAY).append(String.format("Use /%s %s to get drop sources config.", COMMAND_KEY, dropSourcesKey)).append("\n");
        contentBuilder.append(ChatColor.GRAY).append(String.format("Use /%s [Name] [%s|%s] to change setting.", COMMAND_KEY, onText, offText)).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.PLUGIN_ENABLED_KEY)).append((PluginConfig.isPluginEnabled()) ? onColour : offColour).append((PluginConfig.isPluginEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.CRAFTING_KEY)).append((PluginConfig.isCraftingEnabled()) ? onColour : offColour).append((PluginConfig.isCraftingEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.DROPS_KEY)).append((PluginConfig.isDropsEnabled()) ? onColour : offColour).append((PluginConfig.isDropsEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.PLAYERLESS_DROP_EVENTS_KEY)).append((PluginConfig.isPlayerlessDropEventsEnabled()) ? onColour : offColour).append((PluginConfig.isPlayerlessDropEventsEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.WIKI_RECIPE_LEARN_KEY)).append((PluginConfig.isLearnRecipesFromWikiEnabled()) ? onColour : offColour).append((PluginConfig.isLearnRecipesFromWikiEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.UPDATE_NOTIFICATIONS_KEY)).append((PluginConfig.isUpdateNotificationsEnabled()) ? onColour : offColour).append((PluginConfig.isUpdateNotificationsEnabled()) ? onText : offText);
        String contentWithHeader = CommandUtil.addHeader("DecorHeads Config", contentBuilder.toString());
        return contentWithHeader;
    }

    private String getCurrentDropSourcesStates()
    {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(ChatColor.DARK_GRAY).append(String.format("Use /%s to get standard config.", COMMAND_KEY)).append("\n");
        contentBuilder.append(ChatColor.GRAY).append(String.format("Use /%s %s [Name] [%s|%s] to change setting.", COMMAND_KEY, dropSourcesKey, onText, offText)).append("\n");
        for (HeadSourceType sourceType : HeadSourceType.values())
        {
            if (sourceType != HeadSourceType.SHAPELESS_CRAFT && sourceType != HeadSourceType.SHAPED_CRAFT)
            {
                String[] configTree = PluginConfig.convertSourceTypeToConfigKey(sourceType).split("[.]");
                String configKey = configTree[configTree.length-1];
                contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", configKey)).append((PluginConfig.isDropSourceEnabled(sourceType)) ? onColour : offColour).append((PluginConfig.isDropSourceEnabled(sourceType)) ? onText : offText).append("\n");
            }
        }
        String contentWithHeader = CommandUtil.addHeader("DecorHeads Config", contentBuilder.toString().trim());
        return contentWithHeader;
    }
}
