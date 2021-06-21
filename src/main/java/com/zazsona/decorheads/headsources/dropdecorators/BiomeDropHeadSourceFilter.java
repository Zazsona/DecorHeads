package com.zazsona.decorheads.headsources.dropdecorators;

import com.zazsona.decorheads.headsources.DropHeadSource;
import com.zazsona.decorheads.headsources.DropHeadSourceDecorator;
import com.zazsona.decorheads.headsources.IDropHeadSource;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BiomeDropHeadSourceFilter extends DropHeadSourceDecorator
{
    private Set<Biome> biomes = new HashSet<>();

    public BiomeDropHeadSourceFilter(IDropHeadSource dropHeadSource, Collection<Biome> biomes)
    {
        super(dropHeadSource);
        if (biomes != null)
            this.biomes.addAll(biomes);
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onBlockBreak(BlockBreakEvent e)
    {
        World world = e.getBlock().getWorld();
        Location loc = e.getBlock().getLocation();
        Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (biomes.contains(biome))
            return dropHeadSource.onBlockBreak(e);
        else
            return null;
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onEntityDeath(EntityDeathEvent e)
    {
        World world = e.getEntity().getWorld();
        Location loc = e.getEntity().getLocation();
        Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (biomes.size() == 0 || biomes.contains(biome))
            return dropHeadSource.onEntityDeath(e);
        else
            return null;
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onCraftItem(CraftItemEvent e)
    {
        World world = e.getWhoClicked().getWorld();
        Location loc = e.getInventory().getLocation();
        Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (biomes.size() == 0 || biomes.contains(biome))
            return dropHeadSource.onCraftItem(e);
        else
            return null;
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onBrew(BrewEvent e)
    {
        World world = e.getBlock().getWorld();
        Location loc = e.getBlock().getLocation();
        Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (biomes.size() == 0 || biomes.contains(biome))
            return dropHeadSource.onBrew(e);
        else
            return null;
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onSmelt(FurnaceSmeltEvent e)
    {
        World world = e.getBlock().getWorld();
        Location loc = e.getBlock().getLocation();
        Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (biomes.size() == 0 || biomes.contains(biome))
            return dropHeadSource.onSmelt(e);
        else
            return null;
    }
}
