package com.zazsona.decorheads.headsources.dropfilters;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class EntityDropFilter extends DropSourceFilter
{
    private Set<EntityType> entities = new HashSet<>();

    public EntityDropFilter(Collection<EntityType> entities)
    {
        if (this.entities != null)
            this.entities.addAll(entities);
    }

    public Set<EntityType> getEntities()
    {
        return entities;
    }

    private boolean checkPass(EntityType entity)
    {
        return (entities.size() == 0 || entities.contains(entity));
    }

    @Override
    protected boolean passFilter(EntityDeathEvent e)
    {
        return checkPass(e.getEntityType());
    }
}
