package com.zazsona.decorheads.drops.drops;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.Permissions;
import com.zazsona.decorheads.DropType;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.event.block.BlockBreakByExplosionEvent;
import com.zazsona.decorheads.event.block.BlockPistonReactionEvent;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.drops.filters.IDropFilter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class BlockBreakDrop extends Drop
{
    public BlockBreakDrop(@NotNull String key, double dropRate, @NotNull ItemStack result)
    {
        super(key, dropRate, result);
    }

    public BlockBreakDrop(@NotNull String key, double dropRate, @NotNull IHead result)
    {
        super(key, dropRate, result);
    }

    public BlockBreakDrop(@NotNull String key, double dropRate, @NotNull ItemStack result, @NotNull List<IDropFilter> filters)
    {
        super(key, dropRate, result, filters);
    }

    public BlockBreakDrop(@NotNull String key, double dropRate, @NotNull IHead result, @NotNull List<IDropFilter> filters)
    {
        super(key, dropRate, result, filters);
    }

    @Override
    public DropType getDropType()
    {
        return DropType.BLOCK_BREAK;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        try
        {
            PluginConfig config = DecorHeadsPlugin.getInstanceConfig();
            boolean enabledPass = (config.isPluginEnabled() && config.isDropsEnabled() && config.isDropTypeEnabled(getDropType()));
            boolean rollPass = DropUtil.rollDrop(getDropRate());

            if (enabledPass && rollPass && isFiltersPass(e))
            {
                Location location = e.getBlock().getLocation();
                Player player = e.getPlayer();
                if (player != null && player.hasPermission(Permissions.DROP_HEADS) && player.getGameMode() != GameMode.CREATIVE)
                {
                    int quantity = 1;
                    quantity += rollFortuneEnchantmentDropCount(player);
                    ItemStack result = getResult(player.getUniqueId());
                    DropUtil.dropItems(location, result, quantity);
                }
            }
        }
        catch (IllegalArgumentException ex)
        {
            Bukkit.getLogger().warning(String.format("[%s] %s drop event failed: %s", DecorHeadsPlugin.PLUGIN_NAME, getDropType().toString(), ex.getMessage()));
        }
    }

    /*@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        try
        {
            boolean enabledPass = (PluginConfig.isPluginEnabled() && PluginConfig.isDropsEnabled() && PluginConfig.isEnvironmentalDropsEnabled() && PluginConfig.isDropTypeEnabled(getDropType()));
            boolean rollPass = DropUtil.rollDrop(getDropRate());

            if (enabledPass && rollPass && passFilters(e))
            {
                Location location = e.getBlock().getLocation();
                ItemStack result = getResult(null);
                DropUtil.dropItems(location, result, 1);
            }
        }
        catch (IllegalArgumentException ex)
        {
            Bukkit.getLogger().warning(String.format("[%s] %s drop event failed: %s", Core.PLUGIN_NAME, getDropType().toString(), ex.getMessage()));
        }
    }*/

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreakByExplosion(BlockBreakByExplosionEvent e)
    {
        try
        {
            PluginConfig config = DecorHeadsPlugin.getInstanceConfig();
            boolean enabledPass = (config.isPluginEnabled() && config.isDropsEnabled() && config.isEnvironmentalDropsEnabled() && config.isDropTypeEnabled(getDropType()));
            boolean rollPass = DropUtil.rollDrop(getDropRate());

            if (enabledPass && rollPass && isFiltersPass(e))
            {
                Location location = e.getBlock().getLocation();
                ItemStack result = getResult(null);
                DropUtil.dropItems(location, result, 1);
            }
        }
        catch (IllegalArgumentException ex)
        {
            Bukkit.getLogger().warning(String.format("[%s] %s drop event failed: %s", DecorHeadsPlugin.PLUGIN_NAME, getDropType().toString(), ex.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonReaction(BlockPistonReactionEvent e)
    {
        try
        {
            PluginConfig config = DecorHeadsPlugin.getInstanceConfig();
            boolean isBreak = e.getReaction() == PistonMoveReaction.BREAK;
            boolean enabledPass = (config.isPluginEnabled() && config.isDropsEnabled() && config.isEnvironmentalDropsEnabled() && config.isDropTypeEnabled(getDropType()));
            boolean rollPass = DropUtil.rollDrop(getDropRate());

            if (enabledPass && rollPass && isBreak && isFiltersPass(e))
            {
                Location location = e.getBlock().getLocation();
                ItemStack result = getResult(null);
                DropUtil.dropItems(location, result, 1);
            }
        }
        catch (IllegalArgumentException ex)
        {
            Bukkit.getLogger().warning(String.format("[%s] %s drop event failed: %s", DecorHeadsPlugin.PLUGIN_NAME, getDropType().toString(), ex.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFromTo(BlockFromToEvent e)
    {
        try
        {
            PluginConfig config = DecorHeadsPlugin.getInstanceConfig();
            Block fromBlock = e.getBlock();
            Block toBlock = e.getToBlock();
            if (!fromBlock.isEmpty() && !fromBlock.isLiquid() && toBlock.isLiquid())
            {
                boolean enabledPass = (config.isPluginEnabled() && config.isDropsEnabled() && config.isEnvironmentalDropsEnabled() && config.isDropTypeEnabled(getDropType()));
                boolean rollPass = DropUtil.rollDrop(getDropRate());

                if (enabledPass && rollPass && isFiltersPass(e))
                {
                    Location location = e.getBlock().getLocation();
                    ItemStack result = getResult(null);
                    DropUtil.dropItems(location, result, 1);
                }
            }
        }
        catch (IllegalArgumentException ex)
        {
            Bukkit.getLogger().warning(String.format("[%s] %s drop event failed: %s", DecorHeadsPlugin.PLUGIN_NAME, getDropType().toString(), ex.getMessage()));
        }
    }

    private int rollFortuneEnchantmentDropCount(Player blockBreaker)
    {
        if (blockBreaker != null)
        {
            ItemStack miningTool = blockBreaker.getInventory().getItemInMainHand();
            if (miningTool.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS))
            {
                Random r = new Random();
                int dropCount = 0;
                int fortuneLevel = miningTool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                if (fortuneLevel == 1)
                {
                    if (r.nextDouble() < 0.33)
                        dropCount = 1;
                }
                else if (fortuneLevel == 2)
                {
                    double value = r.nextDouble();
                    if (value < 0.25)
                        dropCount = 1;
                    else if (value >= 0.25 && value < 0.5)
                        dropCount = 2;
                }
                else if (fortuneLevel == 3)
                {
                    double value = r.nextDouble();
                    if (value < 0.2)
                        dropCount = 1;
                    else if (value >= 0.2 && value < 0.4)
                        dropCount = 2;
                    else if (value >= 0.4 && value < 0.6)
                        dropCount = 3;
                }
                return dropCount;
            }
        }
        return 0;
    }
}
