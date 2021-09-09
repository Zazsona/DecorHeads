package com.zazsona.decorheads.headsources.dropfilters;

import com.zazsona.decorheads.headsources.HeadSourceType;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class WeatherDropFilter extends DropSourceFilter
{
    private Set<RegionalWeatherType> weatherTypes = new HashSet<>();

    public WeatherDropFilter(Collection<RegionalWeatherType> weatherTypes)
    {
        if (weatherTypes != null)
            this.weatherTypes.addAll(weatherTypes);
    }

    public Set<RegionalWeatherType> getWeatherTypes()
    {
        return weatherTypes;
    }

    private boolean checkPass(Block block)
    {
        World world = block.getWorld();
        Location location = block.getLocation();
        double biomeTemperature = world.getTemperature(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        double blockTemperature = block.getTemperature();
        if (weatherTypes.size() == 0)
        {
            return true;
        }
        else if (!world.hasStorm())
        {
            return weatherTypes.contains(RegionalWeatherType.CLEAR);
        }
        else if (world.hasStorm())
        {
            if (biomeTemperature > 0.95) //Dry biome, hard-coded to not rain.
            {
                return weatherTypes.contains(RegionalWeatherType.CLEAR);
            }
            else if (blockTemperature <= 0.15)
            {
                return weatherTypes.contains(RegionalWeatherType.SNOW);
            }
            else if (blockTemperature > 0.15)
            {
                return (weatherTypes.contains(RegionalWeatherType.RAIN) || (weatherTypes.contains(RegionalWeatherType.THUNDER) && world.isThundering()));
            }
        }
        return false;
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, BlockBreakEvent e)
    {
        return checkPass(e.getBlock());
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, EntityDeathEvent e)
    {
        Location location = e.getEntity().getLocation();
        Block block = e.getEntity().getWorld().getBlockAt(location);
        return checkPass(block);
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, CraftItemEvent e)
    {
        Location location = e.getInventory().getLocation();
        Block block = e.getWhoClicked().getWorld().getBlockAt(location);
        return checkPass(block);
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, BrewEvent e)
    {
        return checkPass(e.getBlock());
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, FurnaceSmeltEvent e)
    {
        return checkPass(e.getBlock());
    }

    public enum RegionalWeatherType
    {
        RAIN,
        THUNDER,
        SNOW,
        CLEAR
    }
}
