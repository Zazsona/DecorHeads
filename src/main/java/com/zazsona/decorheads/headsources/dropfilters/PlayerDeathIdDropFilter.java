package com.zazsona.decorheads.headsources.dropfilters;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class PlayerDeathIdDropFilter extends DropSourceFilter
{
    private Set<String> uuids = new HashSet<>();

    public PlayerDeathIdDropFilter(Collection<String> uuids)
    {
        if (this.uuids != null)
            this.uuids.addAll(uuids);
    }

    public Set<String> getUuids()
    {
        return uuids;
    }

    private boolean checkPass(String uuid)
    {
        return (uuids.size() == 0 || uuids.contains(uuid));
    }

    @Override
    protected boolean passFilter(EntityDeathEvent e)
    {
        if (e.getEntityType() == EntityType.PLAYER)
            return checkPass(e.getEntity().getUniqueId().toString());
        else
            return false;
    }
}
