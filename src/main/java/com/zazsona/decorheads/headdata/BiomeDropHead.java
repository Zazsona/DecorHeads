package com.zazsona.decorheads.headdata;

import org.bukkit.Location;
import org.bukkit.Material;
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

import java.util.*;

public class BiomeDropHead extends DropHeadDecorator
{
    private Set<Biome> biomes = new HashSet<>();

    public BiomeDropHead(IDropHead head, Collection<Biome> biomes)
    {
        super(head);
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
            return ((IDropHead) head).onBlockBreak(e);
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
            return ((IDropHead) head).onEntityDeath(e);
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
            return ((IDropHead) head).onCraftItem(e);
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
            return ((IDropHead) head).onBrew(e);
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
            return ((IDropHead) head).onSmelt(e);
        else
            return null;
    }
}
