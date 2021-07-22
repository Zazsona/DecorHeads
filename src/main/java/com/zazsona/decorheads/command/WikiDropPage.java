package com.zazsona.decorheads.command;

import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.headsources.DropHeadSource;
import com.zazsona.decorheads.headsources.dropfilters.*;
import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.UUID;

public class WikiDropPage implements IWikiPage
{
    private final String borderChar = "\u2B1B";
    private String page;

    public WikiDropPage(DropHeadSource headSource)
    {
        page = buildPage(headSource);
    }

    @Override
    public String getPage()
    {
        return page;
    }

    private String buildPage(DropHeadSource headSource)
    {
        StringBuilder pageBuilder = new StringBuilder();
        pageBuilder.append(borderChar).append(" Drop Type: ").append(getScalar(headSource.getSourceType().name())).append("\n");
        pageBuilder.append(borderChar).append(" Drop Rate: ").append(getScalar(headSource.getBaseDropRate()+"%")).append("\n");
        for (DropSourceFilter filter : headSource.getDropFilters())
        {
            if (filter instanceof BlockDropFilter)
                pageBuilder.append(borderChar).append(" Blocks: ").append(getEnumList(((BlockDropFilter) filter).getBlocks()));
            else if (filter instanceof EntityDropFilter)
                pageBuilder.append(borderChar).append(" Entities: ").append(getEnumList(((EntityDropFilter) filter).getEntities()));
            else if (filter instanceof PlayerDeathIdDropFilter)
                pageBuilder.append(borderChar).append(" Target Players: ").append(getPlayerList(((PlayerDeathIdDropFilter) filter).getUuids()));
            else if (filter instanceof BiomeDropFilter)
                pageBuilder.append(borderChar).append(" Biomes: ").append(getEnumList(((BiomeDropFilter) filter).getBiomes()));
            else if (filter instanceof ToolDropFilter)
                pageBuilder.append(borderChar).append(" Tools: ").append(getEnumList(((ToolDropFilter) filter).getTools()));
            else if (filter instanceof RecipeResultDropFilter)
                pageBuilder.append(borderChar).append(" Recipe Results: ").append(getEnumList(((RecipeResultDropFilter) filter).getResults()));
            else if (filter instanceof EventInvokerFilter)
                pageBuilder.append(borderChar).append(" Event Invoker: ").append(getScalar(((EventInvokerFilter) filter).getInvoker()));
            pageBuilder.append("\n");
        }
        return pageBuilder.toString().trim();
    }

    private String getScalar(Object value)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.ITALIC);
        stringBuilder.append(DecorHeadsUtil.capitaliseName(value.toString()));
        stringBuilder.append(ChatColor.RESET);
        return stringBuilder.toString();
    }

    private String getObjectList(Collection<? extends Object> objects)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.ITALIC);
        for (Object object : objects)
            stringBuilder.append(object.toString()).append(", ");
        stringBuilder.setLength(stringBuilder.length()-2); //Remove final ", "
        stringBuilder.append(ChatColor.RESET);
        return stringBuilder.toString();
    }

    private String getEnumList(Collection<? extends Enum> enums)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.ITALIC);
        for (Enum enumEntry : enums)
            stringBuilder.append(DecorHeadsUtil.capitaliseName(enumEntry.name())).append(", ");
        stringBuilder.setLength(stringBuilder.length()-2); //Remove final ", "
        stringBuilder.append(ChatColor.RESET);
        return stringBuilder.toString();
    }

    private String getPlayerList(Collection<String> uuids)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.ITALIC);
        for (String uuid : uuids)
        {
            String playerName = DecorHeadsUtil.getPlayerName(UUID.fromString(uuid));
            if (playerName != null)
                stringBuilder.append(playerName).append(", ");
        }
        stringBuilder.setLength(stringBuilder.length()-2); //Remove final ", "
        stringBuilder.append(ChatColor.RESET);
        return stringBuilder.toString();
    }
}
