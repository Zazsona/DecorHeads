package com.zazsona.decorheads;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class HeadDropListener implements Listener
{
    private final Random r = new Random();
    private final String PROCESSOR_HEADROP_PERMISSION_METADATA_KEY = "DecorHeadsDropHeadOnOperationComplete";

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryUsed(InventoryClickEvent e)
    {
        if (e == null || e.getInventory() == null)
            return;
        InventoryHolder inventoryHolder = e.getInventory().getHolder();
        if (inventoryHolder instanceof BlockState)
        {
            BlockState blockState = (BlockState) inventoryHolder;
            Block block = blockState.getBlock();
            Player player = (Player) e.getWhoClicked();
            boolean isPlayerPermittedToDropHead = canGetHeadDrop(player);
            block.setMetadata(PROCESSOR_HEADROP_PERMISSION_METADATA_KEY, new FixedMetadataValue(Core.getPlugin(Core.class), isPlayerPermittedToDropHead));
            //We store the permission rather than the player id as the player may leave while processing occurs, & we cannot get permission data from OfflinePlayer.
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBrewComplete(BrewEvent e)
    {
        if (e.getBlock().hasMetadata(PROCESSOR_HEADROP_PERMISSION_METADATA_KEY))
        {
            boolean blockOwnerPermittedHeadDrops = (e.getBlock().getMetadata(PROCESSOR_HEADROP_PERMISSION_METADATA_KEY).get(0).asBoolean());
            if (blockOwnerPermittedHeadDrops && canGetHeadDrop(null))
            {
                if (roll() <= Settings.getDropChance(HeadManager.HeadType.Beer))
                {
                    Location loc = e.getBlock().getLocation();
                    ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Beer);
                    e.getBlock().getWorld().dropItemNaturally(loc, item);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (canGetHeadDrop(e.getPlayer()))
        {
            Block block = e.getBlock();
            checkGlobalBlockDrops(block);
            checkBiomeBlockDrops(e, block);
        }
    }

    private void checkGlobalBlockDrops(Block block)
    {
        if (block.getType() == Material.BOOKSHELF)
            dropHead(HeadManager.HeadType.Books, block.getWorld(), block.getLocation());
        if (block.getType() == Material.SNOW_BLOCK)
            dropHead(HeadManager.HeadType.SnowmanHead, block.getWorld(), block.getLocation());
        if (block.getType() == Material.ACACIA_LEAVES || block.getType() == Material.BIRCH_LEAVES || block.getType() == Material.DARK_OAK_LEAVES || block.getType() == Material.JUNGLE_LEAVES || block.getType() == Material.OAK_LEAVES || block.getType() == Material.SPRUCE_LEAVES)
        {
            dropHead(HeadManager.HeadType.Apple, block.getWorld(), block.getLocation());
            dropHead(HeadManager.HeadType.Blueberry, block.getWorld(), block.getLocation());
            dropHead(HeadManager.HeadType.FruitBasket, block.getWorld(), block.getLocation());
        }
        if (block.getType() == Material.OAK_LOG)
            dropHead(HeadManager.HeadType.MiniOakLog, block.getWorld(), block.getLocation());
        if (block.getType() == Material.BIRCH_LOG)
            dropHead(HeadManager.HeadType.MiniBirchLog, block.getWorld(), block.getLocation());
        if (block.getType() == Material.JUNGLE_LOG)
            dropHead(HeadManager.HeadType.MiniJungleLog, block.getWorld(), block.getLocation());
        if (block.getType() == Material.ACACIA_LOG)
            dropHead(HeadManager.HeadType.MiniAcaciaLog, block.getWorld(), block.getLocation());
        if (block.getType() == Material.DARK_OAK_LOG)
            dropHead(HeadManager.HeadType.MiniDarkOakLog, block.getWorld(), block.getLocation());
        if (block.getType() == Material.SPRUCE_LOG)
            dropHead(HeadManager.HeadType.MiniSpruceLog, block.getWorld(), block.getLocation());
        if (block.getType() == Material.CRIMSON_STEM)
            dropHead(HeadManager.HeadType.MiniCrimsonStem, block.getWorld(), block.getLocation());
        if (block.getType() == Material.WARPED_STEM)
            dropHead(HeadManager.HeadType.MiniWarpedStem, block.getWorld(), block.getLocation());
        if (block.getType() == Material.JUNGLE_LOG)
            dropHead(HeadManager.HeadType.Coconut, block.getWorld(), block.getLocation());
        if (block.getType() == Material.OAK_LEAVES || block.getType() == Material.DARK_OAK_LEAVES)
            dropHead(HeadManager.HeadType.MiniLeaves, block.getWorld(), block.getLocation());
        if (block.getType() == Material.BIRCH_LEAVES)
            dropHead(HeadManager.HeadType.MiniBirchLeaves, block.getWorld(), block.getLocation());
        if (block.getType() == Material.JUNGLE_LEAVES)
            dropHead(HeadManager.HeadType.MiniJungleLeaves, block.getWorld(), block.getLocation());
        if (block.getType() == Material.ACACIA_LEAVES)
            dropHead(HeadManager.HeadType.MiniAcaciaLeaves, block.getWorld(), block.getLocation());
        if (block.getType() == Material.SPRUCE_LEAVES)
            dropHead(HeadManager.HeadType.MiniSpruceLeaves, block.getWorld(), block.getLocation());
        if (block.getType() == Material.DIRT || block.getType() == Material.GRASS_BLOCK)
            dropHead(HeadManager.HeadType.MiniDirt, block.getWorld(), block.getLocation());
        if (block.getType() == Material.MELON)
            dropHead(HeadManager.HeadType.MiniMelon, block.getWorld(), block.getLocation());
        if (block.getType() == Material.PUMPKIN)
            dropHead(HeadManager.HeadType.MiniPumpkin, block.getWorld(), block.getLocation());
        if (block.getType() == Material.CACTUS)
            dropHead(HeadManager.HeadType.MiniCactus, block.getWorld(), block.getLocation());
        if (block.getType() == Material.REDSTONE_ORE)
            dropHead(HeadManager.HeadType.MiniRedstoneBlock, block.getWorld(), block.getLocation());
        if (block.getType() == Material.GRAVEL)
            dropHead(HeadManager.HeadType.MiniGravel, block.getWorld(), block.getLocation());
        if (block.getType() == Material.COBBLESTONE)
            dropHead(HeadManager.HeadType.MiniCobblestone, block.getWorld(), block.getLocation());
        if (block.getType() == Material.OAK_PLANKS)
        {
            dropHead(HeadManager.HeadType.OakCharacterExclamation, block.getWorld(), block.getLocation());
            dropHead(HeadManager.HeadType.OakCharacterQuestion, block.getWorld(), block.getLocation());
            dropHead(HeadManager.HeadType.OakCharacterUpArrow, block.getWorld(), block.getLocation());
            dropHead(HeadManager.HeadType.OakCharacterDownArrow, block.getWorld(), block.getLocation());
            dropHead(HeadManager.HeadType.OakCharacterLeftArrow, block.getWorld(), block.getLocation());
            dropHead(HeadManager.HeadType.OakCharacterRightArrow, block.getWorld(), block.getLocation());
            dropHead(HeadManager.HeadType.OakCharacterExclamation, block.getWorld(), block.getLocation());
        }
        if (block.getType() == Material.BROWN_MUSHROOM)
            dropHead(HeadManager.HeadType.MushroomBrown, block.getWorld(), block.getLocation());
        if (block.getType() == Material.RED_MUSHROOM)
            dropHead(HeadManager.HeadType.MushroomRed, block.getWorld(), block.getLocation());
        if (block.getType() == Material.HAY_BLOCK)
            dropHead(HeadManager.HeadType.MiniHayBale, block.getWorld(), block.getLocation());
        if (block.getType() == Material.BRICKS || block.getType() == Material.BRICK_SLAB || block.getType() == Material.BRICK_STAIRS)
            dropHead(HeadManager.HeadType.MiniBricks, block.getWorld(), block.getLocation());
        if (block.getType() == Material.COAL_ORE)
            dropHead(HeadManager.HeadType.MiniCoalBlock, block.getWorld(), block.getLocation());
        if (block.getType() == Material.LAPIS_ORE)
            dropHead(HeadManager.HeadType.MiniLapisBlock, block.getWorld(), block.getLocation());
    }

    private void checkBiomeBlockDrops(BlockBreakEvent e, Block block)
    {
        if (block.getType() == Material.SAND && isPlayerInBiome(e.getPlayer(), Biome.BEACH))
        {
            dropHead(HeadManager.HeadType.SandBucket, block.getWorld(), block.getLocation());
            dropHead(HeadManager.HeadType.Sandcastle, block.getWorld(), block.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityKill(EntityDeathEvent e)
    {
        LivingEntity entity = e.getEntity();
        if (canGetHeadDrop(entity.getKiller()))
        {
            checkPlayerHeadDrops(e, entity);
            checkMobItemDrops(entity);
            checkMobHeadDrops(entity); //This is not in an else, so that mob heads can be dropped with item heads (E.g, slime head AND slime ball)
        }
    }

    private void checkPlayerHeadDrops(EntityDeathEvent e, LivingEntity entity)
    {
        if (e.getEntity().getType() == EntityType.PLAYER && Settings.isPlayerDropsEnabled() && (!Settings.isPlayerHeadsPvPOnly() || (Settings.isPlayerHeadsPvPOnly() && entity.getKiller() != null && entity.getKiller() != e.getEntity())))
        {
            if (roll() <= Settings.getDropChance(HeadManager.HeadType.Player))
            {
                Player player = (Player) e.getEntity();
                ItemStack item = HeadManager.getPlayerSkull(player.getUniqueId());
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
        }
    }

    private void checkMobItemDrops(LivingEntity entity)
    {
        if (entity.getType() == EntityType.PUFFERFISH || entity.getType() == EntityType.SALMON || entity.getType() == EntityType.COD || entity.getType() == EntityType.TROPICAL_FISH)
            dropHead(HeadManager.HeadType.Sushi, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.SLIME)
            dropHead(HeadManager.HeadType.Slimeball, entity.getWorld(), entity.getLocation());
    }

    private void checkMobHeadDrops(LivingEntity entity)
    {
        if (!Settings.isMobDropsEnabled())
            return;

        if (entity.getType() == EntityType.CHICKEN)
            dropHead(HeadManager.HeadType.Chicken, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.COW)
            dropHead(HeadManager.HeadType.Cow, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.FOX)
            dropHead(HeadManager.HeadType.Fox, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.MUSHROOM_COW)
            dropHead(HeadManager.HeadType.Mooshroom, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.OCELOT)
            dropHead(HeadManager.HeadType.Ocelot, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.PIG)
            dropHead(HeadManager.HeadType.Pig, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.SHEEP) //TODO: Get colour
            dropHead(HeadManager.HeadType.Sheep, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.SQUID)
            dropHead(HeadManager.HeadType.Squid, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.VILLAGER)
            dropHead(HeadManager.HeadType.Villager, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.WANDERING_TRADER)
            dropHead(HeadManager.HeadType.WanderingTrader, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.SPIDER)
            dropHead(HeadManager.HeadType.Spider, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.CAVE_SPIDER)
            dropHead(HeadManager.HeadType.CaveSpider, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.ENDERMAN)
            dropHead(HeadManager.HeadType.Enderman, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.ZOMBIFIED_PIGLIN)
            dropHead(HeadManager.HeadType.ZombiePigman, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.IRON_GOLEM)
            dropHead(HeadManager.HeadType.IronGolem, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.DOLPHIN)
            dropHead(HeadManager.HeadType.Dolphin, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.BLAZE)
            dropHead(HeadManager.HeadType.Blaze, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.CREEPER)
            dropHead(HeadManager.HeadType.Creeper, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.DROWNED)
            dropHead(HeadManager.HeadType.Drowned, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.ELDER_GUARDIAN)
            dropHead(HeadManager.HeadType.ElderGuardian, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.GHAST)
            dropHead(HeadManager.HeadType.Ghast, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.GUARDIAN)
            dropHead(HeadManager.HeadType.Guardian, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.HUSK)
            dropHead(HeadManager.HeadType.Husk, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.MAGMA_CUBE)
            dropHead(HeadManager.HeadType.MagmaCube, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.PILLAGER || entity.getType() == EntityType.EVOKER)
            dropHead(HeadManager.HeadType.Pillager, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.SKELETON)
            dropHead(HeadManager.HeadType.Skeleton, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.SLIME)
            dropHead(HeadManager.HeadType.Slime, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.STRAY)
            dropHead(HeadManager.HeadType.Stray, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.VEX)
            dropHead(HeadManager.HeadType.Vex, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.WITCH)
            dropHead(HeadManager.HeadType.Witch, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.WITHER_SKELETON)
            dropHead(HeadManager.HeadType.WitherSkeleton, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.ZOMBIE)
            dropHead(HeadManager.HeadType.Zombie, entity.getWorld(), entity.getLocation());
        if (entity.getType() == EntityType.ZOMBIE_VILLAGER)
            dropHead(HeadManager.HeadType.ZombieVillager, entity.getWorld(), entity.getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemCrafted(CraftItemEvent e)
    {
        if (e.getWhoClicked() instanceof Player)
        {
            Material resultingItem = e.getInventory().getResult().getType();
            Player player = (Player) e.getWhoClicked();
            if (canGetHeadDrop(player))
            {
                int craftedAmount = getCraftedAmount(e);
                for (int i = 0; i<craftedAmount; i++)
                {
                    if (resultingItem == Material.BREAD)
                        dropHead(HeadManager.HeadType.Bread, player.getWorld(), player.getLocation());
                    if (resultingItem == Material.COOKIE)
                        dropHead(HeadManager.HeadType.Cookie, player.getWorld(), player.getLocation());
                    if (resultingItem == Material.CAKE)
                        dropHead(HeadManager.HeadType.Cupcake, player.getWorld(), player.getLocation());
                    if (resultingItem == Material.CAULDRON)
                        dropHead(HeadManager.HeadType.MiniCauldron, player.getWorld(), player.getLocation());
                }
            }
            if (resultingItem == Material.PLAYER_HEAD && !canCraftHead(player))
            {
                player.sendMessage(ChatColor.RED+"You do not have permission to craft heads.");
                e.setCancelled(true);
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemSmelted(FurnaceSmeltEvent e)
    {
        if (e.getBlock().hasMetadata(PROCESSOR_HEADROP_PERMISSION_METADATA_KEY))
        {
            boolean blockOwnerPermittedHeadDrops = (e.getBlock().getMetadata(PROCESSOR_HEADROP_PERMISSION_METADATA_KEY).get(0).asBoolean());
            if (blockOwnerPermittedHeadDrops && canGetHeadDrop(null))
            {
                ItemStack smeltResult = e.getResult();
                if (smeltResult.getType() == Material.COOKED_BEEF)
                    dropHead(HeadManager.HeadType.Burger, e.getBlock().getWorld(), e.getBlock().getLocation());
                if (smeltResult.getType() == Material.COOKED_CHICKEN)
                    dropHead(HeadManager.HeadType.ChickenDinner, e.getBlock().getWorld(), e.getBlock().getLocation());
            }
        }
    }

    /**
     * Performs a roll check against the chance to drop. If it succeeds, the head is dropped at the location provided.
     * @param headType the head to drop
     * @param world the world to drop in
     * @param location the point in the world to drop
     * @return true/false on head dropped.
     */
    private boolean dropHead(HeadManager.HeadType headType, World world, Location location)
    {
        if (roll() <= Settings.getDropChance(headType))
        {
            ItemStack item = HeadManager.getSkull(headType);
            world.dropItemNaturally(location, item);
            return true;
        }
        return false;
    }

    /**
     * Gets the amount of items crafted for a given crafting event
     * @param e the crafting event
     * @return # of items crafted
     */
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

    /**
     * Rolls a value to compare against the drop chance saved in settings. If this value is less then the drop chance, the head should be dropped.
     * @return the rolled value
     */
    private double roll()
    {
        return ((r.nextInt(9999)+1.0)/100.0);
    }

    /**
     * Confirms if head drops are enabled and this player has permission for heads to drop for them
     * @param player the player to check permissions for. Null to ignore permissions check.
     * @return boolean on whether heads can be dropped or not.
     */
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

    /**
     * Confirms if head crafting is enabled and this player has permission to craft them them
     * @param player the player to check permissions for. Null to ignore permissions check.
     * @return boolean on whether heads can be crafted or not.
     */
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

    /**
     * Checks if the player is in the specified biome.
     * @param player The player
     * @param biome The biome
     * @return true/false on player in biome
     */
    private boolean isPlayerInBiome(Player player, Biome biome)
    {
        World world = player.getWorld();
        Biome currentBiome = world.getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
        return (currentBiome == biome);
    }

}
