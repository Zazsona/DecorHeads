package com.zazsona.decorheads.drops.drops;

import com.zazsona.decorheads.DropType;
import com.zazsona.decorheads.drops.filters.IDropFilter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface IDrop
{
    String getKey();

    double getDropRate();

    DropType getDropType();

    List<IDropFilter> getDropFilters();

    ItemStack getResult(@Nullable UUID playerId);
}
