package com.zazsona.decorheads.command;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.DropType;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.UpdateNotificationLevel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

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
        try
        {
            PluginConfig config = DecorHeadsPlugin.getInstanceConfig();
            if (args.length >= 1 && args[0].equalsIgnoreCase(PluginConfig.DROP_TYPES_KEY))
                manageDropTypesConfig(config, sender, args);
            else
                manageRootConfig(config, sender, args);
        }
        catch (IllegalArgumentException iaEx)
        {
            sender.sendMessage(ChatColor.RED+iaEx.getMessage());
        }
        catch (IOException ioEx)
        {
            sender.sendMessage(ChatColor.RED+"Unable to save config. See log for details.");
            ioEx.printStackTrace();
        }
        finally
        {
            return true;
        }
    }

    private void manageRootConfig(PluginConfig config, CommandSender sender, String[] args) throws IOException
    {
        if (args.length >= 2)
        {
            String settingName = args[0];
            setState(config, settingName, args[1]);
        }
        sender.sendMessage(getCurrentStates(config));
    }

    private void manageDropTypesConfig(PluginConfig config, CommandSender sender, String[] args) throws IOException
    {
        if (args.length >= 3 && (args[2].equalsIgnoreCase(onText) || args[2].equalsIgnoreCase(offText)))
        {
            String typeName = args[1];
            boolean enable = args[2].equalsIgnoreCase(onText);
            String updatedType = setDropTypeState(config, typeName, enable);
            sender.sendMessage(getCurrentDropTypeStates(config));
            if (updatedType == null)
                throw new IllegalArgumentException(String.format("Unknown drop type: %s", typeName));
        }
        else
            sender.sendMessage(getCurrentDropTypeStates(config));
    }

    private String setState(PluginConfig config, String setting, String value) throws IllegalArgumentException, IOException
    {
        switch (setting.toLowerCase())
        {
            case PluginConfig.PLUGIN_ENABLED_KEY:
                config.setPluginEnabled(value.equalsIgnoreCase(onText));
                break;
            case PluginConfig.CRAFTING_KEY:
                config.setCraftingEnabled(value.equalsIgnoreCase(onText));
                break;
            case PluginConfig.DROPS_KEY:
                config.setDropsEnabled(value.equalsIgnoreCase(onText));
                break;
            case PluginConfig.ENVIRONMENTAL_DROPS_KEY:
                config.setEnvironmentalDropsEnabled(value.equalsIgnoreCase(onText));
                break;
            case PluginConfig.HEAD_META_PATCHER_KEY:
                config.setHeadMetaPatcherEnabled(value.equalsIgnoreCase(onText));
                break;
            case PluginConfig.UPDATE_NOTIFICATIONS_KEY:
                try
                {
                    UpdateNotificationLevel level = UpdateNotificationLevel.valueOf(value.toUpperCase());
                    config.setUpdateNotificationsLevel(level);
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

        config.save();
        return setting; //If it passes the switch, setting must be equal to a constant in PluginConfig, so works for displaying the updated setting.
    }

    private String setDropTypeState(PluginConfig config, String typeName, boolean value) throws IOException
    {
        try
        {
            DropType dropType = PluginConfig.convertConfigKeyToDropType(typeName);
            config.setDropTypeEnabled(dropType, value);
            config.save();
            return typeName;
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }

    private String getCurrentStates(PluginConfig config)
    {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(ChatColor.DARK_GRAY).append(String.format("Use /%s %s to get drop types config.", COMMAND_KEY, PluginConfig.DROP_TYPES_KEY)).append("\n");
        contentBuilder.append(ChatColor.GRAY).append(String.format("Use /%s [Name] [%s|%s] to change setting.", COMMAND_KEY, onText, offText)).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.PLUGIN_ENABLED_KEY)).append((config.isPluginEnabled()) ? onColour : offColour).append((config.isPluginEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.CRAFTING_KEY)).append((config.isCraftingEnabled()) ? onColour : offColour).append((config.isCraftingEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.DROPS_KEY)).append((config.isDropsEnabled()) ? onColour : offColour).append((config.isDropsEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.ENVIRONMENTAL_DROPS_KEY)).append((config.isEnvironmentalDropsEnabled()) ? onColour : offColour).append((config.isEnvironmentalDropsEnabled()) ? onText : offText).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.UPDATE_NOTIFICATIONS_KEY)).append((config.getUpdateNotificationsLevel() != UpdateNotificationLevel.DISABLED) ? onColour : offColour).append(DecorHeadsUtil.capitaliseName(config.getUpdateNotificationsLevel().toString())).append("\n");
        contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", PluginConfig.HEAD_META_PATCHER_KEY)).append((config.isHeadMetaPatcherEnabled()) ? onColour : offColour).append((config.isHeadMetaPatcherEnabled()) ? onText : offText);
        String contentWithHeader = CommandUtil.addHeader(String.format("%s Config", DecorHeadsPlugin.PLUGIN_NAME), contentBuilder.toString());
        return contentWithHeader;
    }

    private String getCurrentDropTypeStates(PluginConfig config)
    {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(ChatColor.DARK_GRAY).append(String.format("Use /%s to get standard config.", COMMAND_KEY)).append("\n");
        contentBuilder.append(ChatColor.GRAY).append(String.format("Use /%s %s [Name] [Value] to change setting.", COMMAND_KEY, PluginConfig.DROP_TYPES_KEY)).append("\n");
        for (DropType dropType : DropType.values())
        {
            String[] configTree = PluginConfig.convertDropTypeToConfigKey(dropType).split("[.]");
            String configKey = configTree[configTree.length-1];
            contentBuilder.append(ChatColor.WHITE).append(String.format("%s: ", configKey)).append((config.isDropTypeEnabled(dropType)) ? onColour : offColour).append((config.isDropTypeEnabled(dropType)) ? onText : offText).append("\n");
        }
        String contentWithHeader = CommandUtil.addHeader(String.format("%s Config", DecorHeadsPlugin.PLUGIN_NAME), contentBuilder.toString().trim());
        return contentWithHeader;
    }
}
