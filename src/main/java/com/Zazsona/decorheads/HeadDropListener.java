package com.zazsona.decorheads;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
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
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.SnowmanHead))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.SnowmanHead);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.ACACIA_LEAVES || block.getType() == Material.BIRCH_LEAVES || block.getType() == Material.DARK_OAK_LEAVES || block.getType() == Material.JUNGLE_LEAVES || block.getType() == Material.OAK_LEAVES || block.getType() == Material.SPRUCE_LEAVES)
            {
                HeadManager.HeadType[] headTypes = getMultiOptionSelections(HeadManager.HeadType.Apple, HeadManager.HeadType.Blueberry, HeadManager.HeadType.FruitBasket);
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
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.MiniOakLog))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.MiniOakLog);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.DIRT || block.getType() == Material.GRASS_BLOCK)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.MiniDirt))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.MiniDirt);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.OAK_LEAVES)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.MiniLeaves))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.MiniLeaves);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.MELON)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.MiniMelon))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.MiniMelon);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.PUMPKIN)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.MiniPumpkin))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.MiniPumpkin);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.CACTUS)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.MiniCactus))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.MiniCactus);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.REDSTONE_ORE)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.MiniRedstoneBlock))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.MiniRedstoneBlock);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.GRAVEL)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.MiniGravel))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.MiniGravel);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.COBBLESTONE)
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.MiniCobblestone))
                {
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.MiniCobblestone);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }
            else if (block.getType() == Material.OAK_PLANKS)
            {
                HeadManager.HeadType[] headTypes = getMultiOptionSelections(HeadManager.HeadType.OakCharacterExclamation, HeadManager.HeadType.OakCharacterQuestion, HeadManager.HeadType.OakCharacterUpArrow, HeadManager.HeadType.OakCharacterDownArrow, HeadManager.HeadType.OakCharacterLeftArrow, HeadManager.HeadType.OakCharacterRightArrow);
                for (HeadManager.HeadType head : headTypes)
                {
                    ItemStack item = HeadManager.getSkull(head);
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }

            }
            else if (block.getType() == Material.SAND && isPlayerInBiome(e.getPlayer(), Biome.BEACH))
            {
                HeadManager.HeadType[] headTypes = getMultiOptionSelections(HeadManager.HeadType.Sandcastle, HeadManager.HeadType.SandBucket);
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
            if (e.getEntity().getType() == EntityType.PLAYER && roll() <= Settings.getDropChance(HeadManager.HeadType.Player))
            {
                Player player = (Player) e.getEntity();
                ItemStack item = HeadManager.getPlayerSkull(player.getUniqueId());
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
            else
            {
                dropMobHead(entity);
                if (entity.getType() == EntityType.PUFFERFISH || entity.getType() == EntityType.SALMON || entity.getType() == EntityType.COD || entity.getType() == EntityType.TROPICAL_FISH)
                {
                    if (roll() <= Settings.getDropChance(HeadManager.HeadType.Sushi))
                    {
                        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Sushi);
                        entity.getWorld().dropItemNaturally(entity.getLocation(), item);
                    }
                }
                else if (entity.getType() == EntityType.SLIME)
                {
                    if (roll() <= Settings.getDropChance(HeadManager.HeadType.Slimeball))
                    {
                        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Slimeball);
                        entity.getWorld().dropItemNaturally(entity.getLocation(), item);
                    }
                }
            }
        }
    }

    private void dropMobHead(LivingEntity entity)
    {
        if (!Settings.isMobDropsEnabled())
            return;

        if (entity.getType() == EntityType.CHICKEN)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Chicken))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Chicken);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.COW)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Cow))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Cow);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.FOX)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Fox))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Fox);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.MUSHROOM_COW)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Mooshroom))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Mooshroom);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.OCELOT)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Ocelot))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Ocelot);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.PIG)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Pig))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Pig);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.SHEEP) //TODO: Get colour
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Sheep))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Sheep);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.SQUID)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Squid))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Squid);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.VILLAGER)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Villager))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Villager);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.WANDERING_TRADER)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.WanderingTrader))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.WanderingTrader);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.SPIDER)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Spider))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Spider);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.CAVE_SPIDER)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.CaveSpider))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.CaveSpider);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.ENDERMAN)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Enderman))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Enderman);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.PIG_ZOMBIE)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.ZombiePigman))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.ZombiePigman);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.IRON_GOLEM)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.IronGolem))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.IronGolem);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.DOLPHIN)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Dolphin))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Dolphin);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.BLAZE)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Blaze))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Blaze);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.CREEPER)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Creeper))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Creeper);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.DROWNED)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Drowned))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Drowned);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.ELDER_GUARDIAN)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.ElderGuardian))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.ElderGuardian);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.GHAST)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Ghast))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Ghast);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.GUARDIAN)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Guardian))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Guardian);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.HUSK)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Husk))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Husk);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.MAGMA_CUBE)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.MagmaCube))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.MagmaCube);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.PILLAGER || entity.getType() == EntityType.EVOKER)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Pillager))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Pillager);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.SKELETON)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Skeleton))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Skeleton);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.SLIME)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Slime))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Slime);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.STRAY)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Stray))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Stray);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.VEX)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Vex))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Vex);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.WITCH)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Witch))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Witch);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.WITHER_SKELETON)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.WitherSkeleton))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.WitherSkeleton);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.ZOMBIE)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Zombie))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Zombie);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
        else if (entity.getType() == EntityType.ZOMBIE_VILLAGER)
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.ZombieVillager))
            {
                ItemStack item = HeadManager.getSkull(HeadManager.HeadType.ZombieVillager);
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
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
                        if (roll() <= Settings.getDropChance(HeadManager.HeadType.Cupcake))
                        {
                            ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Cupcake);
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

    private boolean isPlayerInBiome(Player player, Biome biome)
    {
        World world = player.getWorld();
        Biome currentBiome = world.getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
        return (currentBiome == biome);
    }

}
