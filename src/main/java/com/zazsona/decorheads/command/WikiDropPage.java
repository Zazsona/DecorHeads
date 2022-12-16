package com.zazsona.decorheads.command;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.config.DropType;
import com.zazsona.decorheads.drops.drops.IDrop;
import com.zazsona.decorheads.drops.filters.BlockFilter;
import com.zazsona.decorheads.drops.filters.IDropFilter;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class WikiDropPage implements IWikiPage
{
    private final String borderChar = "\u2B1B";
    private String page;

    public WikiDropPage(IDrop drop)
    {
        page = buildPage(drop);
    }

    @Override
    public String getPage()
    {
        return page;
    }

    private String buildPage(IDrop drop)
    {
        StringBuilder pageBuilder = new StringBuilder();
        pageBuilder.append(borderChar).append(" Drop Type: ").append(formatScalar(DecorHeadsUtil.capitaliseName(drop.getDropType().toString()))).append("\n");
        pageBuilder.append(borderChar).append(" Drop Rate: ").append(formatScalar(drop.getDropRate()+"%")).append("\n");
        pageBuilder.append(CommandUtil.addHeader("How to Drop", ""));
        List<IDropFilter> dropFilters = drop.getDropFilters();
        if (dropFilters.size() > 0)
        {
            for (IDropFilter filter : dropFilters)
            {
                if (filter instanceof BlockFilter)
                    pageBuilder.append(borderChar).append(" Break Blocks: ").append(formatKeyList(((BlockFilter) filter).getBlockKeys()));
                // TODO: Restore as filters are reimplemented
                /*else if (filter instanceof EntityDropFilter)
                    pageBuilder.append(borderChar).append(" Kill Entities: ").append(getEnumList(((EntityDropFilter) filter).getEntities()));
                else if (filter instanceof PlayerDeathIdDropFilter)
                    pageBuilder.append(borderChar).append(" Kill Players: ").append(getPlayerList(((PlayerDeathIdDropFilter) filter).getUuids()));
                else if (filter instanceof ToolDropFilter)
                    pageBuilder.append(borderChar).append(" Using Tools: ").append(getEnumList(((ToolDropFilter) filter).getTools()));
                else if (filter instanceof RecipeResultDropFilter)
                    pageBuilder.append(borderChar).append(String.format(" %s to Make: ", getSourceTypeActionName(drop.getDropType()))).append(getEnumList(((RecipeResultDropFilter) filter).getResults()));
                else if (filter instanceof RecipeIngredientsDropFilter)
                    pageBuilder.append(borderChar).append(String.format(" %s using Ingredients: ", getSourceTypeActionName(drop.getDropType()))).append(getNestedEnumList(((RecipeIngredientsDropFilter) filter).getIngredientWhitelists()));
                else if (filter instanceof WorldDropFilter)
                    pageBuilder.append(borderChar).append(" In Worlds: ").append(getObjectList(((WorldDropFilter) filter).getWorldNames()));
                else if (filter instanceof BiomeDropFilter)
                    pageBuilder.append(borderChar).append(" In Biomes: ").append(getEnumList(((BiomeDropFilter) filter).getBiomes()));
                else if (filter instanceof WeatherDropFilter)
                    pageBuilder.append(borderChar).append(" During Weather: ").append(getEnumList(((WeatherDropFilter) filter).getWeatherTypes()));
                else if (filter instanceof EventInvokerFilter)
                    pageBuilder.append(borderChar).append(" Performed By: ").append(getScalar(((EventInvokerFilter) filter).getInvoker()));*/
                pageBuilder.append("\n");
            }
        }
        else
        {
            pageBuilder.append(borderChar).append(" ").append(getDropTypeActionName(drop.getDropType()));
        }
        return pageBuilder.toString().trim();
    }

    private String getDropTypeActionName(DropType dropType)
    {
        switch (dropType)
        {
            case BLOCK_BREAK:
                return "Mine blocks";
            case BREW:
                return "Brew";
            case CRAFT:
                return "Craft";
            case SMELT:
                return "Cook";
            case ENTITY_DEATH:
                return "Kill entities";
            case PLAYER_DEATH:
                return "Kill players";
            default:
                return "Unknown";
        }
    }

    private String formatScalar(Object value)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.ITALIC);
        stringBuilder.append(DecorHeadsUtil.capitaliseName(value.toString()));
        stringBuilder.append(ChatColor.RESET);
        return stringBuilder.toString();
    }

    private String formatObjectList(Collection<? extends Object> objects)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.ITALIC);
        for (Object object : objects)
            stringBuilder.append(object.toString()).append(", ");
        stringBuilder.setLength(stringBuilder.length()-2); //Remove final ", "
        stringBuilder.append(ChatColor.RESET);
        return stringBuilder.toString();
    }

    private String formatEnumList(Collection<? extends Enum> enums)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.ITALIC);
        for (Enum enumEntry : enums)
            stringBuilder.append(DecorHeadsUtil.capitaliseName(enumEntry.toString())).append(", ");
        stringBuilder.setLength(stringBuilder.length()-2); //Remove final ", "
        stringBuilder.append(ChatColor.RESET);
        return stringBuilder.toString();
    }

    private String formatKeyList(Collection<NamespacedKey> keys)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.ITALIC);
        for (NamespacedKey keyEntry : keys)
        {
            String suffix = (keyEntry.getNamespace().equalsIgnoreCase(Core.PLUGIN_NAME)) ? " (Head)" : "";
            stringBuilder.append(DecorHeadsUtil.capitaliseName(keyEntry.getKey())).append(suffix).append(", ");
        }
        stringBuilder.setLength(stringBuilder.length()-2); //Remove final ", "
        stringBuilder.append(ChatColor.RESET);
        return stringBuilder.toString();
    }

    private String formatNestedEnumList(Collection<? extends Collection<? extends Enum>> nestedEnums)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (Collection<? extends Enum> enums : nestedEnums)
        {
            if (nestedEnums.size() > 1)
                stringBuilder.append("\n").append(borderChar).append(" ");
            stringBuilder.append(formatEnumList(enums));
        }
        return stringBuilder.toString();
    }

    private String formatPlayerList(Collection<String> uuids)
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
