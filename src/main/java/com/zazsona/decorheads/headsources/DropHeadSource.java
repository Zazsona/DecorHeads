package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.Permissions;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.headsources.dropfilters.DropSourceFilter;
import org.bukkit.Location;
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

    public double getBaseDropRate()
    {
        return dropRate;
    }

    public List<DropSourceFilter> getDropFilters()
    {
        return dropFilters;
    }

    /**
     * Drops heads in the world at the location based on how many of the specified rolls pass the provided drop rate.
     * @param world the world to drop a head in
     * @param location the location of the world to drop in
     * @param player the player who the head is dropping for. Pass null for a "playerless head drop"
     * @param headSkinTarget the UUID of a player whose skin should be used as the texture. Pass null for a decor head.
     * @param dropRate the chance for a head to drop
     * @param rolls the number of rolls to perform against the chance
     * @return the dropped ItemStack
     */
    public ItemStack dropHead(World world, Location location, @Nullable Player player, @Nullable UUID headSkinTarget, double dropRate, int rolls)
    {
        int totalToDrop = 0;
        for (int i = 0; i < rolls; i++)
        {
            boolean dropHead = rollDrop(dropRate);
            if (dropHead)
                totalToDrop++;
        }
        return dropHead(world, location, player, headSkinTarget, totalToDrop);
    }

    /**
     * Drops heads in the world at the location
     * @param world the world to drop a head in
     * @param location the location of the world to drop in
     * @param player the player who the head is dropping for. Pass null for a "playerless head drop"
     * @param headSkinTarget the UUID of a player whose skin should be used as the texture. Pass null for a decor head.
     * @return the dropped ItemStack
     */
    public ItemStack dropHead(World world, Location location, @Nullable Player player, @Nullable UUID headSkinTarget)
    {
        return dropHead(world, location, player, headSkinTarget, 1);
    }

    /**
     * Drops heads in the world at the location
     * @param world the world to drop a head in
     * @param location the location of the world to drop in
     * @param player the player who the head is dropping for. Pass null for a "playerless head drop"
     * @param headSkinTarget the UUID of a player whose skin should be used as the texture. Pass null for a decor head.
     * @param stackSize the amount of heads in the stack to drop
     * @return the dropped ItemStack
     */
    public ItemStack dropHead(World world, Location location, @Nullable Player player, @Nullable UUID headSkinTarget, int stackSize)
    {
        if (PluginConfig.isPluginEnabled() && PluginConfig.isDropsEnabled() && stackSize > 0 && ((player == null && PluginConfig.isPlayerlessDropEventsEnabled()) || (player != null && player.hasPermission(Permissions.DROP_HEADS))))
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

            if (stackSize <= headStack.getMaxStackSize())
            {
                headStack.setAmount(stackSize);
                world.dropItemNaturally(location, headStack);
                return headStack;
            }
            else
                throw new IllegalArgumentException(String.format("Invalid stack size: %d. Values must be between 0 and %d", stackSize, headStack.getMaxStackSize()));
        }
        return null;
    }

    /**
     * Performs a roll check against the chance to drop
     * @return true/false on head roll passed.
     */
    protected boolean rollDrop(double dropRate)
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
            if (!filter.passFilter(getSourceType(), e))
                return false;
        }
        return true;
    }
}
