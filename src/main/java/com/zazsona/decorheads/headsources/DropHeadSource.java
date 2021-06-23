package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.Settings;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.Set;

public abstract class DropHeadSource extends HeadSource implements IDropHeadSource
{
    private static Random rand = new Random();
    private double dropRate;

    public DropHeadSource(IHead head, double dropRate)
    {
        super(head);
        this.dropRate = dropRate;
    }

    /**
     * Performs a roll check against the chance to drop
     * @return true/false on head roll passed.
     */
    public boolean rollDrop()
    {
        double roll = ((rand.nextInt(9999)+1.0)/100.0);
        return (roll <= dropRate);
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

    @Override
    public ItemStack onBlockBreak(BlockBreakEvent e)
    {
        return null;
    }

    @Override
    public ItemStack onEntityDeath(EntityDeathEvent e)
    {
        return null;
    }

    @Override
    public ItemStack onCraftItem(CraftItemEvent e)
    {
        return null;
    }

    @Override
    public ItemStack onBrew(BrewEvent e)
    {
        return null;
    }

    @Override
    public ItemStack onSmelt(FurnaceSmeltEvent e)
    {
        return null;
    }
}
