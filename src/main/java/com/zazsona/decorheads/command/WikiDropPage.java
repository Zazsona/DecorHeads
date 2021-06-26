package com.zazsona.decorheads.command;

import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.headsources.DropHeadSource;
import com.zazsona.decorheads.headsources.HeadSource;
import com.zazsona.decorheads.headsources.dropfilters.BiomeDropFilter;
import com.zazsona.decorheads.headsources.dropfilters.DropSourceFilter;
import com.zazsona.decorheads.headsources.dropfilters.ToolDropFilter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.Collection;

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
        pageBuilder.append(borderChar).append(" Drop Rate: ").append(getScalar(headSource.getDropRate()+"%")).append("\n");
        for (DropSourceFilter filter : headSource.getDropFilters())
        {
            if (filter instanceof BiomeDropFilter)
                pageBuilder.append(borderChar).append(" Biomes: ").append(getEnumList(((BiomeDropFilter) filter).getBiomes()));
            else if (filter instanceof ToolDropFilter)
                pageBuilder.append(borderChar).append(" Tools: ").append(getEnumList(((ToolDropFilter) filter).getTools()));
            pageBuilder.append("\n");
        }
        return pageBuilder.toString();
    }

    private String getScalar(Object value)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.ITALIC);
        stringBuilder.append(DecorHeadsUtil.capitaliseName(value.toString()));
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
}
