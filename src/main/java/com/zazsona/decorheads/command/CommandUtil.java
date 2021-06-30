package com.zazsona.decorheads.command;

import org.bukkit.ChatColor;

public class CommandUtil
{
    public static String addHeader(String headerText, String content)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.YELLOW).append("---------");
        sb.append(ChatColor.WHITE).append(" ").append(headerText).append(" ");
        sb.append(ChatColor.YELLOW).append("--------------------");
        sb.append(ChatColor.WHITE).append("\n");
        sb.append(content);
        return sb.toString();
    }
}
