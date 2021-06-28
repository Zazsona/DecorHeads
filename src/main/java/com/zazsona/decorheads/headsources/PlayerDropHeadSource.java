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

public class PlayerDropHeadSource extends DropHeadSource
{
    public PlayerDropHeadSource(IHead head, double dropRate)
    {
        super(head, HeadSourceType.PLAYER_DEATH_DROP, dropRate);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onEntityDeath(EntityDeathEvent e)
    {
        if (e.getEntityType() == EntityType.PLAYER && passFilters(e))
        {
            Player player = (Player) e.getEntity();
            World world = e.getEntity().getWorld();
            Location location = e.getEntity().getLocation();
            return super.dropHead(world, location, player);
        }
        return null;
    }
}
