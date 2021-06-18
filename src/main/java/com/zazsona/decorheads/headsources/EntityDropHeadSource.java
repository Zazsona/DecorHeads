package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EntityDropHeadSource extends DropHeadSource
{
    private Set<EntityType> entities = new HashSet<EntityType>();

    public EntityDropHeadSource(IHead head, Collection<EntityType> entities)
    {
        super(head);
        if (entities != null)
            this.entities.addAll(entities);
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onEntityDeath(EntityDeathEvent e)
    {
        if (entities.size() == 0 || entities.contains(e.getEntityType()))
        {
            World world = e.getEntity().getWorld();
            Location location = e.getEntity().getLocation();
            return super.dropHead(world, location);
        }
        return null;
    }
}
