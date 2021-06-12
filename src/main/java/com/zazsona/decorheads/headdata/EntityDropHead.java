package com.zazsona.decorheads.headdata;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EntityDropHead extends DropHead implements Listener
{
    private Set<EntityType> entities = new HashSet<EntityType>();
    private Set<Material> tools = new HashSet<Material>();

    public EntityDropHead(IHead head, Collection<EntityType> entities)
    {
        super(head);
        if (entities != null)
            this.entities.addAll(entities);
    }

    public EntityDropHead(IHead head, Collection<EntityType> entities, Collection<Material> requiredTools)
    {
        super(head);
        if (entities != null)
            this.entities.addAll(entities);
        if (requiredTools != null)
            this.tools.addAll(requiredTools);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent e)
    {
        if (entities.size() == 0 || entities.contains(e.getEntityType()) && rollDrop())
        {
            Player killer = e.getEntity().getKiller();
            ItemStack tool = (killer != null) ? killer.getInventory().getItemInMainHand() : null;
            if (tools.size() == 0 || tools.contains(tool.getType()))
            {
                World world = e.getEntity().getWorld();
                Location location = e.getEntity().getLocation();
                world.dropItemNaturally(location, head.createItem());
            }
        }
    }
}
