package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
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
        if (PluginConfig.isDropSourceEnabled(getSourceType()) && passFilters(e))
        {
            UUID killedPlayerId = (e.getEntity() instanceof Player) ? e.getEntity().getUniqueId() : null;
            Player killer = e.getEntity().getKiller();
            World world = e.getEntity().getWorld();
            Location location = e.getEntity().getLocation();
            double dropRate = getModifiedDropRate(e);
            return super.dropHead(world, location, killer, killedPlayerId, dropRate, 1);
        }
        return null;
    }

    private double getModifiedDropRate(EntityDeathEvent e)
    {
        double baseDropRate = getBaseDropRate();
        double dropRate = baseDropRate;
        Player killer = e.getEntity().getKiller();
        if (killer != null && baseDropRate > 0) // Maintain 0% drop rate as this is a quick alternative to "off"
        {
            ItemStack murderWeapon = killer.getInventory().getItemInMainHand(); // Note: while a bow could be fired in the off-hand with an enchanted main hand, this is how Minecraft itself does things.
            if (murderWeapon.containsEnchantment(Enchantment.LOOT_BONUS_MOBS))
                dropRate += murderWeapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS); // This mimics behaviour for "rare" loot drops.
        }
        return Math.min(100.0f, dropRate);
    }
}
