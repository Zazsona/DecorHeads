package com.zazsona.decorheads.drops.drops;

import com.zazsona.decorheads.config.DropType;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.drops.filters.IDropFilter;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerDeathDrop extends EntityDeathDrop
{
    public PlayerDeathDrop(@NotNull String key, double dropRate, @NotNull ItemStack result)
    {
        super(key, dropRate, result);
    }

    public PlayerDeathDrop(@NotNull String key, double dropRate, @NotNull IHead result)
    {
        super(key, dropRate, result);
    }

    public PlayerDeathDrop(@NotNull String key, double dropRate, @NotNull ItemStack result, @NotNull List<IDropFilter> filters)
    {
        super(key, dropRate, result, filters);
    }

    public PlayerDeathDrop(@NotNull String key, double dropRate, @NotNull IHead result, @NotNull List<IDropFilter> filters)
    {
        super(key, dropRate, result, filters);
    }

    @Override
    public DropType getDropType()
    {
        return DropType.PLAYER_DEATH;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent e)
    {
        if (PluginConfig.isDropTypeEnabled(getDropType()) && e.getEntityType() == EntityType.PLAYER)
            super.onEntityDeath(e);
    }
}
