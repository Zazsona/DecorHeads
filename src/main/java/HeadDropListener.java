import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class HeadDropListener implements Listener
{
    @EventHandler (priority = EventPriority.LOWEST)
    public void onBrewComplete(BrewEvent e)
    {
        Location loc = e.getBlock().getLocation();
        ItemStack item = new ItemStack(Material.DIAMOND); //Beer
        e.getBlock().getWorld().dropItemNaturally(loc, item);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent e)
    {
        Block block = e.getBlock();
        if (block.getType() == Material.BOOKSHELF)
        {
            ItemStack item = new ItemStack(Material.DIAMOND); //Books
            block.getWorld().dropItemNaturally(block.getLocation(), item);
        }
        else if (block.getType() == Material.SNOW_BLOCK)
        {
            ItemStack item = new ItemStack(Material.DIAMOND); //Snowman head
            block.getWorld().dropItemNaturally(block.getLocation(), item);
        }
        else if (block.getType() == Material.ACACIA_LEAVES || block.getType() == Material.BIRCH_LEAVES || block.getType() == Material.DARK_OAK_LEAVES || block.getType() == Material.JUNGLE_LEAVES || block.getType() == Material.OAK_LEAVES || block.getType() == Material.SPRUCE_LEAVES)
        {
            ItemStack item = new ItemStack(Material.DIAMOND); //Apple, blueberry, lemon head
            block.getWorld().dropItemNaturally(block.getLocation(), item);
        }
        else if (block.getType() == Material.JUNGLE_LOG)
        {
            ItemStack item = new ItemStack(Material.DIAMOND); //Coconut head
            block.getWorld().dropItemNaturally(block.getLocation(), item);
        }
        else if (block.getType() == Material.OAK_LOG)
        {
            ItemStack item = new ItemStack(Material.DIAMOND); //log head
            block.getWorld().dropItemNaturally(block.getLocation(), item);
        }
        else if (block.getType() == Material.DIRT)
        {
            ItemStack item = new ItemStack(Material.DIAMOND); //dirt head
            block.getWorld().dropItemNaturally(block.getLocation(), item);
        }
        else if (block.getType() == Material.OAK_LEAVES)
        {
            ItemStack item = new ItemStack(Material.DIAMOND); //leaves head
            block.getWorld().dropItemNaturally(block.getLocation(), item);
        }
        else if (block.getType() == Material.MELON)
        {
            ItemStack item = new ItemStack(Material.DIAMOND); //melon head
            block.getWorld().dropItemNaturally(block.getLocation(), item);
        }

    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onEntityKill(EntityDeathEvent e)
    {
        LivingEntity entity = e.getEntity();
        if (entity.getType() == EntityType.PUFFERFISH || entity.getType() == EntityType.SALMON || entity.getType() == EntityType.COD || entity.getType() == EntityType.TROPICAL_FISH)
        {
            ItemStack item = new ItemStack(Material.DIAMOND); //Sushi
            entity.getWorld().dropItemNaturally(entity.getLocation(), item);
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onItemCrafted(CraftItemEvent e)
    {
        Material resultingItem = e.getInventory().getResult().getType();
        int minimumAmount = getCraftedAmount(e);
        for (int i = 0; i<minimumAmount; i++)
        {
            if (resultingItem == Material.BREAD)
            {
                ItemStack item = new ItemStack(Material.DIAMOND); //bread head
                e.getWhoClicked().getWorld().dropItemNaturally(e.getWhoClicked().getLocation(), item);
            }
            else if (resultingItem == Material.COOKIE)
            {
                ItemStack item = new ItemStack(Material.DIAMOND); //cookie head
                e.getWhoClicked().getWorld().dropItemNaturally(e.getWhoClicked().getLocation(), item);
            }
            else if (resultingItem == Material.CAKE)
            {
                ItemStack item = new ItemStack(Material.DIAMOND); //cake head
                e.getWhoClicked().getWorld().dropItemNaturally(e.getWhoClicked().getLocation(), item);
            }
        }

    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onItemSmelted(FurnaceSmeltEvent e)
    {
        ItemStack smeltResult = e.getResult();
        if (smeltResult.getType() == Material.COOKED_BEEF)
        {
            ItemStack item = new ItemStack(Material.DIAMOND); //Burger
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
        }
        else if (smeltResult.getType() == Material.COOKED_CHICKEN)
        {
            ItemStack item = new ItemStack(Material.DIAMOND); //Turkey dinner
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
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

}
