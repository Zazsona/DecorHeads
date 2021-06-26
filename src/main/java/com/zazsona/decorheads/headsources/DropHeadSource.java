package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.Settings;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.headsources.dropfilters.DropSourceFilter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class DropHeadSource extends HeadSource implements Listener
{
    private static Random rand = new Random();
    private double dropRate;
    private ArrayList<DropSourceFilter> dropFilters;

    public DropHeadSource(IHead head, HeadSourceType headSourceType, double dropRate)
    {
        super(head, headSourceType);
        this.dropRate = dropRate;
        this.dropFilters = new ArrayList<>();
    }

    public List<DropSourceFilter> getDropFilters()
    {
        return dropFilters;
    }

    public ItemStack dropHead(World world, Location location)
    {
        return dropHead(world, location, null);
    }

    public ItemStack dropHead(World world, Location location, Player player)
    {
        if (rollDrop())
        {
            ItemStack headStack;
            IHead head = getHead();
            if (player != null && head instanceof PlayerHead)
            {
                PlayerHead playerHead = (PlayerHead) head;
                headStack = playerHead.createItem(player.getUniqueId());
            }
            else
                headStack = head.createItem();
            world.dropItemNaturally(location, headStack);
            return headStack;
        }
        return null;
    }

    /**
     * Performs a roll check against the chance to drop
     * @return true/false on head roll passed.
     */
    protected boolean rollDrop()
    {
        double roll = ((rand.nextInt(9999)+1.0)/100.0);
        return (roll <= dropRate);
    }

    /**
     * Checks all drop filters are passed.
     * @param e the event to filter
     * @return true on all passed
     */
    protected boolean passFilters(Event e)
    {
        for (DropSourceFilter filter : dropFilters)
        {
            if (!filter.passFilter(e))
                return false;
        }
        return true;
    }
}
