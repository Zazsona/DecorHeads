package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class BlockDropHeadSource extends DropHeadSource
{
    public BlockDropHeadSource(IHead head, double dropRate)
    {
        super(head, HeadSourceType.MINE_DROP, dropRate);
    }

    protected BlockDropHeadSource(IHead head, HeadSourceType sourceType, double dropRate)
    {
        super(head, sourceType, dropRate);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onBlockBreak(BlockBreakEvent e)
    {
        if (PluginConfig.isDropSourceEnabled(getSourceType()) && passFilters(e))
        {
            Player blockBreaker = e.getPlayer();
            if (blockBreaker == null || blockBreaker.getGameMode() != GameMode.CREATIVE)
            {
                World world = e.getBlock().getWorld();
                Location location = e.getBlock().getLocation();
                ItemStack droppedStack = null;
                boolean dropHead = super.rollDrop(getBaseDropRate());
                if (dropHead)
                {
                    int quantity = 1;
                    quantity += rollFortuneEnchantmentDropCount(blockBreaker);
                    droppedStack = super.dropHead(world, location, blockBreaker, null, quantity);
                }
                return droppedStack;
            }
        }
        return null;
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
                    if (value < 25)
                        dropCount = 1;
                    else if (value >= 25 && value < 50)
                        dropCount = 2;
                }
                else if (fortuneLevel == 3)
                {
                    double value = r.nextDouble();
                    if (value < 20)
                        dropCount = 1;
                    else if (value >= 20 && value < 40)
                        dropCount = 2;
                    else if (value >= 40 && value < 60)
                        dropCount = 3;
                }
                return dropCount;
            }
        }
        return 0;
    }
}
