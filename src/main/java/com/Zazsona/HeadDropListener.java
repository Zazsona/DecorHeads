package com.Zazsona;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.LinkedList;
import java.util.Random;

public class HeadDropListener implements Listener
{
    private Random r = new Random();
    private static Plugin plugin = Core.getPlugin(Core.class);

    @EventHandler (priority = EventPriority.LOWEST)
    public void onBrewComplete(BrewEvent e)
    {
        if (canGetHeadDrop(null))
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Beer)) //TODO: There is no way to tie this to a player's permissions
            {
                Location loc = e.getBlock().getLocation();
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Beer);
                e.getBlock().getWorld().dropItemNaturally(loc, item);
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (canGetHeadDrop(e.getPlayer()))
        {
            Block block = e.getBlock();
            if (block.getType() == Material.BOOKSHELF)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Books))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Books);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.SNOW_BLOCK)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Snowman))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Snowman);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.ACACIA_LEAVES || block.getType() == Material.BIRCH_LEAVES || block.getType() == Material.DARK_OAK_LEAVES || block.getType() == Material.JUNGLE_LEAVES || block.getType() == Material.OAK_LEAVES || block.getType() == Material.SPRUCE_LEAVES)
            {
                HeadManager.HeadType[] headTypes = getMultiOptionSelections(HeadManager.HeadType.Apple, HeadManager.HeadType.Blueberry);
                for (HeadManager.HeadType head : headTypes)
                {
                    ItemStack item = HeadManager.getSkull(head);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.JUNGLE_LOG)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Coconut))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Coconut);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.OAK_LOG)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.OakLog))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.OakLog);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.DIRT || block.getType() == Material.GRASS_BLOCK)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Dirt))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Dirt);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.OAK_LEAVES)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Leaves))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Leaves);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.MELON)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Melon))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Melon);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.PUMPKIN)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Pumpkin))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Pumpkin);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.CACTUS)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Cactus))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Cactus);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.REDSTONE_ORE)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.RedstoneBlock))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.RedstoneBlock);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.GRAVEL)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Gravel))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Gravel);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.COBBLESTONE)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Cobblestone))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Cobblestone);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.OAK_PLANKS)
            {
                HeadManager.HeadType[] headTypes = getMultiOptionSelections(HeadManager.HeadType.OakCharacterExclamation, HeadManager.HeadType.OakCharacterQuestion, HeadManager.HeadType.OakCharacterUp, HeadManager.HeadType.OakCharacterDown, HeadManager.HeadType.OakCharacterLeft, HeadManager.HeadType.OakCharacterRight);
                for (HeadManager.HeadType head : headTypes)
                {
                    ItemStack item = HeadManager.getSkull(head);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }

            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onEntityKill(EntityDeathEvent e)
    {
        LivingEntity entity = e.getEntity();
        if (canGetHeadDrop(entity.getKiller()))
        {
            if (entity.getType() == EntityType.PUFFERFISH || entity.getType() == EntityType.SALMON || entity.getType() == EntityType.COD || entity.getType() == EntityType.TROPICAL_FISH)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Sushi))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Sushi);
                    entity.getWorld().dropItemNaturally(entity.getLocation(), item);
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onItemCrafted(CraftItemEvent e)
    {
        if (e.getWhoClicked() instanceof Player)
        {
            Material resultingItem = e.getInventory().getResult().getType();
            Player player = (Player) e.getWhoClicked();
            if (canGetHeadDrop(player))
            {
                int minimumAmount = getCraftedAmount(e);
                for (int i = 0; i<minimumAmount; i++)
                {
                    if (resultingItem == Material.BREAD)
                    {
                        if (roll() <= Settings.getDropChance(HeadManager.HeadType.Bread))
                        {
                            ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Bread);
                            player.getWorld().dropItemNaturally(player.getLocation(), item);
                        }
                    }
                    else if (resultingItem == Material.COOKIE)
                    {
                        if (roll() <= Settings.getDropChance(HeadManager.HeadType.Cookie))
                        {
                            ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Cookie);
                            player.getWorld().dropItemNaturally(player.getLocation(), item);
                        }
                    }
                    else if (resultingItem == Material.CAKE)
                    {
                        if (roll() <= Settings.getDropChance(HeadManager.HeadType.Cake))
                        {
                            ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Cake);
                            player.getWorld().dropItemNaturally(player.getLocation(), item);
                        }
                    }
                }
            }
            if (resultingItem == Material.PLAYER_HEAD && !canCraftHead(player))
            {
                player.sendMessage(ChatColor.RED+"You do not have permission to craft heads.");
                e.setCancelled(true);

            }
        }

    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onItemSmelted(FurnaceSmeltEvent e)
    {
        if (canGetHeadDrop(null)) //TODO: No way to detect player for permissions
        {
            ItemStack smeltResult = e.getResult();
            if (smeltResult.getType() == Material.COOKED_BEEF)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Burger))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Burger);
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
                }
            }
            else if (smeltResult.getType() == Material.COOKED_CHICKEN)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.ChickenDinner))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.ChickenDinner);
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
                }
            }
        }
    }

    private int getCraftedAmount(CraftItemEvent e)
    {
        int minimumAmount = Integer.MAX_VALUE;
        ItemStack[] matrix = e.getInventory().getMatrix();
        for (int i = 0; i<matrix.length; i++)
        {
            if (matrix[i] != null)
            {
                if (matrix[i].getAmount() < minimumAmount && matrix[i].getAmount() > 0)
                {
                    minimumAmount = matrix[i].getAmount();
                }
            }
        }
        return minimumAmount;
    }

    private HeadManager.HeadType[] getMultiOptionSelections(HeadManager.HeadType... possibleHeads)
    {
        LinkedList<HeadManager.HeadType> headsToSpawn = new LinkedList<>();
        for (HeadManager.HeadType head : possibleHeads)
        {
            if (roll() <= Settings.getDropChance(head))
            {
                headsToSpawn.add(head);
            }
        }
        return headsToSpawn.toArray(new HeadManager.HeadType[0]);
    }

    private double roll()
    {
        double roll = ((r.nextInt(9999)+1.0)/100.0);
        System.out.println(roll);
        return roll;
    }

    private boolean canGetHeadDrop(Player player)
    {
        if (Settings.isEnabled() && Settings.isDropsEnabled())
        {
            if (player != null)
            {
                return player.hasPermission("DecorHeads.HeadDrop");
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    private boolean canCraftHead(Player player)
    {
        if (Settings.isEnabled() && Settings.isCraftingEnabled())
        {
            if (player != null)
            {
                return player.hasPermission("DecorHeads.CraftHead");
            }
            else
            {
                return true;
            }
        }
        return false;
    }

}
