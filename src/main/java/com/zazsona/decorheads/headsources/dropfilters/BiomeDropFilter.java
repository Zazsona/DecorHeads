package com.zazsona.decorheads.headsources.dropfilters;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class BiomeDropFilter extends DropSourceFilter
{
    private Set<Biome> biomes = new HashSet<>();

    public BiomeDropFilter(Collection<Biome> biomes)
    {
        if (biomes != null)
            this.biomes.addAll(biomes);
    }

    private boolean checkPass(Biome biome)
    {
        return (biomes.size() == 0 || biomes.contains(biome));
    }

    @Override
    protected boolean passFilter(BlockBreakEvent e)
    {
        World world = e.getBlock().getWorld();
        Location loc = e.getBlock().getLocation();
        Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        return checkPass(biome);
    }

    @Override
    protected boolean passFilter(EntityDeathEvent e)
    {
        World world = e.getEntity().getWorld();
        Location loc = e.getEntity().getLocation();
        Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        return checkPass(biome);
    }

    @Override
    protected boolean passFilter(CraftItemEvent e)
    {
        World world = e.getWhoClicked().getWorld();
        Location loc = e.getInventory().getLocation();
        Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        return checkPass(biome);
    }

    @Override
    protected boolean passFilter(BrewEvent e)
    {
        World world = e.getBlock().getWorld();
        Location loc = e.getBlock().getLocation();
        Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        return checkPass(biome);
    }

    @Override
    protected boolean passFilter(FurnaceSmeltEvent e)
    {
        World world = e.getBlock().getWorld();
        Location loc = e.getBlock().getLocation();
        Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        return checkPass(biome);
    }
}
