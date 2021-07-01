package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EntityDropHeadSource extends DropHeadSource
{
    public EntityDropHeadSource(IHead head, double dropRate)
    {
        super(head, HeadSourceType.ENTITY_DEATH_DROP, dropRate);
    }

    protected EntityDropHeadSource(IHead head, HeadSourceType sourceType, double dropRate)
    {
        super(head, sourceType, dropRate);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onEntityDeath(EntityDeathEvent e)
    {
        if (passFilters(e))
        {
            UUID killedPlayerId = (e.getEntity() instanceof Player) ? e.getEntity().getUniqueId() : null;
            Player killer = e.getEntity().getKiller();
            World world = e.getEntity().getWorld();
            Location location = e.getEntity().getLocation();
            return super.dropHead(world, location, killer, killedPlayerId);
        }
        return null;
    }
}
