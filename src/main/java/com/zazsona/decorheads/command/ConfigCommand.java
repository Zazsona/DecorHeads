package com.zazsona.decorheads.command;

import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.config.DropType;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.config.UpdateNotificationLevel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ConfigCommand implements CommandExecutor
{
    public static final String COMMAND_KEY = "dhconfig";
    private static final ChatColor onColour = ChatColor.GREEN;
    private static final ChatColor offColour = ChatColor.RED;
    private static final String onText = "On";
    private static final String offText = "Off";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length >= 1 && args[0].equalsIgnoreCase(PluginConfig.DROP_TYPES_KEY))
            manageDropTypesConfig(sender, args);
        else
            manageRootConfig(sender, args);
        return true;

    }

    private void manageRootConfig(CommandSender sender, String[] args)
    {
        try
        {
            if (args.length >= 2)
            {
                String settingName = args[0];
                setState(settingName, args[1]);
            }
            sender.sendMessage(getCurrentStates());
        }
        catch (IllegalArgumentException e)
        {
            sender.sendMessage(ChatColor.RED+e.getMessage());
        }
    }

    private void manageDropTypesConfig(CommandSender sender, String[] args)
    {
        if (args.length >= 3 && (args[2].equalsIgnoreCase(onText) || args[2].equalsIgnoreCase(offText)))
        {
            String typeName = args[1];
            boolean enable = args[2].equalsIgnoreCase(onText);
            String updatedType = setDropTypeState(typeName, enable);
            sender.sendMessage(getCurrentDropTypeStates());
            if (updatedType == null)
                sender.sendMessage(String.format(ChatColor.RED+"Unknown drop type: %s", typeName));
        }
        else
            sender.sendMessage(getCurrentDropTypeStates());
    }

    private String setState(String setting, String value) throws IllegalArgumentException
    {
        switch (setting.toLowerCase())
        {
            case PluginConfig.PLUGIN_ENABLED_KEY:
                PluginConfig.setPluginEnabled(value.equalsIgnoreCase(onText));
                break;
            case PluginConfig.CRAFTING_KEY:
                PluginConfig.setCraftingEnabled(value.equalsIgnoreCase(onText));
                break;
            case PluginConfig.DROPS_KEY:
                PluginConfig.setDropsEnabled(value.equalsIgnoreCase(onText));
                break;
            case PluginConfig.ENVIRONMENTAL_DROPS_KEY:
                PluginConfig.setEnvironmentalDropsEnabled(value.equalsIgnoreCase(onText));
                break;
            case PluginConfig.WIKI_RECIPE_LEARN_KEY:
                PluginConfig.setLearnRecipesFromWikiEnabled(value.equalsIgnoreCase(onText));
                break;
            case PluginConfig.HEAD_META_PATCHER_KEY:
                PluginConfig.setHeadMetaPatcherEnabled(value.equalsIgnoreCase(onText));
                break;
            case PluginConfig.UPDATE_NOTIFICATIONS_KEY:
                try
                {
                    UpdateNotificationLevel level = UpdateNotificationLevel.valueOf(value.toUpperCase());
                    PluginConfig.setUpdateNotificationsLevel(level);
                }
                catch (IllegalArgumentException e)
                {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid notification level. Supported levels:\n");
                    for (UpdateNotificationLevel level : UpdateNotificationLevel.values())
                        stringBuilder.append(DecorHeadsUtil.capitaliseName(level.toString())).append(", ");
                    stringBuilder.setLength(stringBuilder.length() - 2);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown setting: %s", setting));
        }
        return setting; //If it passes the switch, setting must be equal to a constant in PluginConfig, so works for displaying the updated setting.
    }

    private String setDropTypeState(String typeName, boolean value)
    {
        try
        {
            DropType dropType = PluginConfig.convertConfigKeyToDropType(typeName);
            PluginConfig.setDropTypeEnabled(dropType, value);
            return typeName;
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }

    private String getCurrentStates()
    {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(ChatColor.DARK_GRAY).append(String.format("Use /%s %s to get drop types config.", COMMAND_KEY, PluginConfig.DROP_TYPES_KEY)).append("\n");
        contentBuilder.append(ChatColor.GRAY).append(String.format("Use /%s [Name] [%s|%s] to change setting.", COMMAND_KEY, onText, offText)).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.PLUGIN_ENABLED_KEY)).append((PluginConfig.isPluginEnabled()) ? onColour : offColour).append((PluginConfig.isPluginEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.CRAFTING_KEY)).append((PluginConfig.isCraftingEnabled()) ? onColour : offColour).append((PluginConfig.isCraftingEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.DROPS_KEY)).append((PluginConfig.isDropsEnabled()) ? onColour : offColour).append((PluginConfig.isDropsEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.ENVIRONMENTAL_DROPS_KEY)).append((PluginConfig.isEnvironmentalDropsEnabled()) ? onColour : offColour).append((PluginConfig.isEnvironmentalDropsEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.WIKI_RECIPE_LEARN_KEY)).append((PluginConfig.isLearnRecipesFromWikiEnabled()) ? onColour : offColour).append((PluginConfig.isLearnRecipesFromWikiEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.UPDATE_NOTIFICATIONS_KEY)).append((PluginConfig.getUpdateNotificationsLevel() != UpdateNotificationLevel.DISABLED) ? onColour : offColour).append(DecorHeadsUtil.capitaliseName(PluginConfig.getUpdateNotificationsLevel().toString())).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.HEAD_META_PATCHER_KEY)).append((PluginConfig.isHeadMetaPatcherEnabled()) ? onColour : offColour).append((PluginConfig.isHeadMetaPatcherEnabled()) ? onText : offText);
        String contentWithHeader = CommandUtil.addHeader("DecorHeads Config", contentBuilder.toString());
        return contentWithHeader;
    }

    private String getCurrentDropTypeStates()
    {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(ChatColor.DARK_GRAY).append(String.format("Use /%s to get standard config.", COMMAND_KEY)).append("\n");
        contentBuilder.append(ChatColor.GRAY).append(String.format("Use /%s %s [Name] [Value] to change setting.", COMMAND_KEY, PluginConfig.DROP_TYPES_KEY)).append("\n");
        for (DropType dropType : DropType.values())
        {
            String[] configTree = PluginConfig.convertDropTypeToConfigKey(dropType).split("[.]");
            String configKey = configTree[configTree.length-1];
            contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", configKey)).append((PluginConfig.isDropTypeEnabled(dropType)) ? onColour : offColour).append((PluginConfig.isDropTypeEnabled(dropType)) ? onText : offText).append("\n");
        }
        String contentWithHeader = CommandUtil.addHeader("DecorHeads Config", contentBuilder.toString().trim());
        return contentWithHeader;
    }
}
