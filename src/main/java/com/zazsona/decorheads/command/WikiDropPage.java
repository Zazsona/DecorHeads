package com.zazsona.decorheads.command;

import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.headsources.DropHeadSource;
import com.zazsona.decorheads.headsources.HeadSourceType;
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
        pageBuilder.append(CommandUtil.addHeader("How to Drop", ""));
        if (headSource.getDropFilters().size() > 0)
        {
            for (DropSourceFilter filter : headSource.getDropFilters())
            {
                if (filter instanceof BlockDropFilter)
                    pageBuilder.append(borderChar).append(" Break Blocks: ").append(getEnumList(((BlockDropFilter) filter).getBlocks()));
                else if (filter instanceof EntityDropFilter)
                    pageBuilder.append(borderChar).append(" Kill Entities: ").append(getEnumList(((EntityDropFilter) filter).getEntities()));
                else if (filter instanceof PlayerDeathIdDropFilter)
                    pageBuilder.append(borderChar).append(" Kill Players: ").append(getPlayerList(((PlayerDeathIdDropFilter) filter).getUuids()));
                else if (filter instanceof ToolDropFilter)
                    pageBuilder.append(borderChar).append(" Using Tools: ").append(getEnumList(((ToolDropFilter) filter).getTools()));
                else if (filter instanceof RecipeResultDropFilter)
                    pageBuilder.append(borderChar).append(String.format(" %s to Make: ", getSourceTypeActionName(headSource.getSourceType()))).append(getEnumList(((RecipeResultDropFilter) filter).getResults()));
                else if (filter instanceof RecipeIngredientsDropFilter)
                    pageBuilder.append(borderChar).append(String.format(" %s using Ingredients: ", getSourceTypeActionName(headSource.getSourceType()))).append(getNestedEnumList(((RecipeIngredientsDropFilter) filter).getIngredientWhitelists()));
                else if (filter instanceof WorldDropFilter)
                    pageBuilder.append(borderChar).append(" In Worlds: ").append(getObjectList(((WorldDropFilter) filter).getWorldNames()));
                else if (filter instanceof BiomeDropFilter)
                    pageBuilder.append(borderChar).append(" In Biomes: ").append(getEnumList(((BiomeDropFilter) filter).getBiomes()));
                else if (filter instanceof WeatherDropFilter)
                    pageBuilder.append(borderChar).append(" During Weather: ").append(getEnumList(((WeatherDropFilter) filter).getWeatherTypes()));
                else if (filter instanceof EventInvokerFilter)
                    pageBuilder.append(borderChar).append(" Performed By: ").append(getScalar(((EventInvokerFilter) filter).getInvoker()));
                pageBuilder.append("\n");
            }
        }
        else
        {
            pageBuilder.append(borderChar).append(" ").append(getSourceTypeActionName(headSource.getSourceType()));
        }
        return pageBuilder.toString().trim();
    }

    private String getSourceTypeActionName(HeadSourceType sourceType)
    {
        switch (sourceType)
        {
            case MINE_DROP:
                return "Mine blocks";
            case BREW_DROP:
                return "Brew";
            case CRAFT_DROP:
                return "Craft";
            case SMELT_DROP:
                return "Smelt";
            case ENTITY_DEATH_DROP:
                return "Kill entities";
            case PLAYER_DEATH_DROP:
                return "Kill players";
            default:
                return "Unknown";
        }
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

    private String getNestedEnumList(Collection<? extends Collection<? extends Enum>> nestedEnums)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (Collection<? extends Enum> enums : nestedEnums)
        {
            if (nestedEnums.size() > 1)
                stringBuilder.append("\n").append(borderChar).append(" ");
            stringBuilder.append(getEnumList(enums));
        }
        return stringBuilder.toString();
    }

    private String getPlayerList(Collection<String> uuids)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.ITALIC);
        for (String uuid : uuids)
        {
            String playerName = DecorHeadsUtil.fetchPlayerName(UUID.fromString(uuid));
            if (playerName != null)
                stringBuilder.append(playerName).append(", ");
        }
        stringBuilder.setLength(stringBuilder.length()-2); //Remove final ", "
        stringBuilder.append(ChatColor.RESET);
        return stringBuilder.toString();
    }
}
