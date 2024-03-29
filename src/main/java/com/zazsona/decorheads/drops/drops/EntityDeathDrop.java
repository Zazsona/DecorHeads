package com.zazsona.decorheads.drops.drops;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.Permissions;
import com.zazsona.decorheads.DropType;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.drops.filters.IDropFilter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

// TODO: At the moment, an error is occasionally thrown stating UUID is null. This is due to the fact entity filter hasn't been implemented yet, so all mobs try to drop the head, even though only players with UUIDs can.
// Implementing the filter fixes this, but a null check should be added as server owners could cock up their configs and do this.
public class EntityDeathDrop extends Drop
{
    public EntityDeathDrop(@NotNull String key, double dropRate, @NotNull ItemStack result)
    {
        super(key, dropRate, result);
    }

    public EntityDeathDrop(@NotNull String key, double dropRate, @NotNull IHead result)
    {
        super(key, dropRate, result);
    }

    public EntityDeathDrop(@NotNull String key, double dropRate, @NotNull ItemStack result, @NotNull List<IDropFilter> filters)
    {
        super(key, dropRate, result, filters);
    }

    public EntityDeathDrop(@NotNull String key, double dropRate, @NotNull IHead result, @NotNull List<IDropFilter> filters)
    {
        super(key, dropRate, result, filters);
    }

    @Override
    public DropType getDropType()
    {
        return DropType.ENTITY_DEATH;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent e)
    {
        try
        {
            Player killer = e.getEntity().getKiller();
            PluginConfig config = DecorHeadsPlugin.getInstanceConfig();
            boolean enabledPass = (config.isPluginEnabled() && config.isDropsEnabled() && config.isDropTypeEnabled(getDropType()));
            boolean rollPass = DropUtil.rollDrop(getModifiedDropRate(e));
            boolean permissionsPass = (killer == null) ? config.isEnvironmentalDropsEnabled() : killer.hasPermission(Permissions.DROP_HEADS);

            if (enabledPass && rollPass && permissionsPass && isFiltersPass(e))
            {
                Location location = e.getEntity().getLocation();
                UUID playerUuid = (e.getEntity().getType() == EntityType.PLAYER) ? e.getEntity().getUniqueId() : null;
                ItemStack result = getResult(playerUuid);
                DropUtil.dropItem(location, result);
            }
        }
        catch (IllegalArgumentException ex)
        {
            Bukkit.getLogger().warning(String.format("[%s] %s drop event failed: %s", DecorHeadsPlugin.PLUGIN_NAME, getDropType().toString(), ex.getMessage()));
        }
    }

    /**
     * Gets the drop rate adjusted for the killer's Looting enchantment.
     * @param e the death event
     * @return the modified drop rate
     */
    private double getModifiedDropRate(EntityDeathEvent e)
    {
        double baseDropRate = getDropRate();
        double dropRate = baseDropRate;
        Player killer = e.getEntity().getKiller();
        if (killer != null && baseDropRate > 0) // Maintain 0% drop rate as this is a quick alternative to "off"
        {
            ItemStack murderWeapon = killer.getInventory().getItemInMainHand(); // Note: while a bow could be fired in the off-hand with an enchanted main hand, this is how Minecraft itself does things.
            if (murderWeapon.containsEnchantment(Enchantment.LOOT_BONUS_MOBS))
                dropRate += (murderWeapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) / 100.0f); // This mimics behaviour for "rare" loot drops.
        }
        return Math.min(1.0f, dropRate);
    }
}
