package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PlayerEntityDropHeadSource extends EntityDropHeadSource
{
    public PlayerEntityDropHeadSource(IHead head, double dropRate)
    {
        super(head, HeadSourceType.PLAYER_DEATH_DROP, dropRate);
    }

    protected PlayerEntityDropHeadSource(IHead head, HeadSourceType sourceType, double dropRate)
    {
        super(head, sourceType, dropRate);
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onEntityDeath(EntityDeathEvent e)
    {
        if (e.getEntityType() == EntityType.PLAYER)
            return super.onEntityDeath(e);
        else
            return null;
    }
}
