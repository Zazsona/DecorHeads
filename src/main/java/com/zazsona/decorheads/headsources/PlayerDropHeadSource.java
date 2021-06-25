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
    private Set<String> uuids = new HashSet<String>();

    public PlayerDropHeadSource(IHead head, double dropRate, Collection<String> uuids)
    {
        super(head, dropRate);
        if (uuids != null)
            this.uuids.addAll(uuids);
    }

    @Override
    public HeadSourceType getSourceType()
    {
        return HeadSourceType.PLAYER_DEATH_DROP;
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onEntityDeath(EntityDeathEvent e)
    {
        if (e.getEntityType() == EntityType.PLAYER)
        {
            Player player = (Player) e.getEntity();
            if (uuids.size() == 0 || uuids.contains(player.getUniqueId()))
            {
                World world = e.getEntity().getWorld();
                Location location = e.getEntity().getLocation();
                return super.dropHead(world, location, player);
            }
        }
        return null;
    }
}
