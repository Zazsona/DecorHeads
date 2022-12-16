package com.zazsona.decorheads.drops.drops;

import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.headdata.TextureHead;
import com.zazsona.decorheads.drops.filters.IDropFilter;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * A class representing the logic that determines when an item should drop
 */
public abstract class Drop implements IDrop, Listener
{
    private String key;
    private double dropRate;

    private ItemStack itemResult;
    private IHead headResult;

    private List<IDropFilter> dropFilters;

    /**
     * Creates a new {@link Drop}
     * @param key a unique key to identify the drop
     * @param dropRate the percentage chance of this drop occurring
     * @param result the dropped item
     */
    public Drop(@NotNull String key, double dropRate, @NotNull ItemStack result)
    {
        this.key = key;
        this.dropRate = dropRate;
        this.itemResult = result;

        this.dropFilters = new ArrayList<>();
    }

    /**
     * Creates a new {@link Drop}
     * @param key a unique key to identify the drop
     * @param dropRate the percentage chance of this drop occurring
     * @param result the dropped item
     * @param dropFilters filters that must be passed for the drop to occur
     */
    public Drop(@NotNull String key, double dropRate, @NotNull ItemStack result, @NotNull List<IDropFilter> dropFilters)
    {
        this.key = key;
        this.dropRate = dropRate;
        this.itemResult = result;
        this.dropFilters = dropFilters;
    }

    /**
     * Creates a new {@link Drop}
     * @param key a unique key to identify the drop
     * @param dropRate the percentage chance of this drop occurring
     * @param result the dropped head
     */
    public Drop(@NotNull String key, double dropRate, @NotNull IHead result)
    {
        this.key = key;
        this.dropRate = dropRate;
        this.headResult = result;

        this.dropFilters = new ArrayList<>();
    }

    /**
     * Creates a new {@link Drop}
     * @param key a unique key to identify the drop
     * @param dropRate the percentage chance of this drop occurring
     * @param result the dropped head
     * @param dropFilters filters that must be passed for the drop to occur
     */
    public Drop(@NotNull String key, double dropRate, @NotNull IHead result, @NotNull List<IDropFilter> dropFilters)
    {
        this.key = key;
        this.dropRate = dropRate;
        this.headResult = result;
        this.dropFilters = dropFilters;
    }

    /**
     * Gets the unique key to identify the drop
     * @return the key
     */
    @Override
    public String getKey()
    {
        return key;
    }

    /**
     * Gets the percentage chance of this drop occurring
     * @return the key
     */
    @Override
    public double getDropRate()
    {
        return dropRate;
    }

    /**
     * Gets the drop result (as an item) for the provided player
     * @param playerUUID the player causing the drop. Can be null.
     * @return the drop
     */
    @Override
    public ItemStack getResult(@Nullable UUID playerUUID) throws IllegalArgumentException
    {
        if (itemResult != null)
            return itemResult.clone();

        if (headResult != null)
        {
            if (headResult instanceof TextureHead)
            {
                TextureHead head = (TextureHead) headResult;
                return head.createItem();
            }
            else if (headResult instanceof PlayerHead)
            {
                PlayerHead head = (PlayerHead) headResult;
                return head.createItem(playerUUID);
            }
        }
        return null;
    }

    /**
     * Gets the filters that must be passed for this drop to occur
     * @return
     */
    @Override
    public List<IDropFilter> getDropFilters()
    {
        return Collections.unmodifiableList(dropFilters);
    }

    /**
     * Adds a filter that will need to be passed for a head to drop
     * @param filter the filter to add
     * @return true on success
     */
    public boolean addDropFilter(IDropFilter filter)
    {
        return dropFilters.add(filter);
    }

    /**
     * Removes a filter that will no longer need to be passed for a head to drop
     * @param filter the filter to remove
     * @return true on success
     */
    public boolean removeDropFilter(IDropFilter filter)
    {
        return dropFilters.remove(filter);
    }

    /**
     * Checks if this drop has the specified filter
     * @param filter the filter to check
     * @return true if the filter is present, otherwise false
     */
    public boolean hasDropFilter(IDropFilter filter)
    {
        return dropFilters.contains(filter);
    }

    /**
     * Checks all drop filters are passed.
     * @param e the event to filter
     * @return true on all passed
     */
    protected boolean isFiltersPass(Event e)
    {
        for (IDropFilter filter : dropFilters)
        {
            if (!filter.isFilterPass(getDropType(), e))
                return false;
        }
        return true;
    }
}
