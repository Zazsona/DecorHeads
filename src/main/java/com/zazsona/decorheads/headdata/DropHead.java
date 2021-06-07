package com.zazsona.decorheads.headdata;

import com.zazsona.decorheads.HeadManager;
import com.zazsona.decorheads.Settings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public abstract class DropHead extends HeadDecorator
{
    private static Random rand = new Random();

    public DropHead(IHead head)
    {
        super(head);
    }

    @Override
    public boolean isObtainedThroughDrops()
    {
        return true;
    }

    /**
     * Performs a roll check against the chance to drop
     * @return true/false on head roll passed.
     */
    protected boolean rollDrop()
    {
        double roll = ((rand.nextInt(9999)+1.0)/100.0);
        return (roll <= Settings.getDropChance(head.getKey()));
    }
}
