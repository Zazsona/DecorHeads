package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.headsources.dropfilters.DropSourceFilter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

    public double getDropRate()
    {
        return dropRate;
    }

    public List<DropSourceFilter> getDropFilters()
    {
        return dropFilters;
    }

    public ItemStack dropHead(World world, Location location, @Nullable Player player, @Nullable UUID headSkinTarget)
    {
        if (rollDrop())
        {
            ItemStack headStack;
            IHead head = getHead();
            if (headSkinTarget != null && head instanceof PlayerHead)
            {
                PlayerHead playerHead = (PlayerHead) head;
                headStack = playerHead.createItem(headSkinTarget);
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
        double roll = rand.nextDouble() * 100.0f;
        return (roll < dropRate);
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
