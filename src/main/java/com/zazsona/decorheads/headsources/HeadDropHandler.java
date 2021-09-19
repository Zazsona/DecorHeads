package com.zazsona.decorheads.headsources;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public abstract class HeadDropHandler
{
    public abstract void onHeadDropped(DropHeadSource source, List<ItemStack> droppedStacks, @Nullable Player associatedPlayer);
}
