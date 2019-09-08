import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

import java.util.Random;

public class HeadDropListener implements Listener
{
    private Random r = new Random();

    @EventHandler (priority = EventPriority.LOWEST)
    public void onBrewComplete(BrewEvent e)
    {
        if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.BeerChance))
        {
            Location loc = e.getBlock().getLocation();
            ItemStack item = createSkull("Thanauser", "Beer");
            e.getBlock().getWorld().dropItemNaturally(loc, item);
        }

    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent e)
    {
        Block block = e.getBlock();
        if (block.getType() == Material.BOOKSHELF)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.BooksChance))
            {
                ItemStack item = createSkull("GoodBook1", "Books");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.SNOW_BLOCK)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.SnowmanChance))
            {
                ItemStack item = createSkull("Snowman_7", "Snowman Head");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.ACACIA_LEAVES || block.getType() == Material.BIRCH_LEAVES || block.getType() == Material.DARK_OAK_LEAVES || block.getType() == Material.JUNGLE_LEAVES || block.getType() == Material.OAK_LEAVES || block.getType() == Material.SPRUCE_LEAVES)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.TreeFruitChance))
            {
                ItemStack item;
                if (r.nextBoolean())
                    item = createSkull("TheReapHorn", "Blueberry");
                else
                    item = createSkull("MHF_Apple", "Apple");

                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.JUNGLE_LOG)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.CoconutChance))
            {
                ItemStack item = createSkull("MHF_CoconutB", "Coconut");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.OAK_LOG)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.OakLogChance))
            {
                ItemStack item = createSkull("Log", "Oak Log");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.DIRT || block.getType() == Material.GRASS_BLOCK)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.DirtChance))
            {
                ItemStack item = createSkull("Zyne", "Dirt");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.OAK_LEAVES)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.LeavesChance))
            {
                ItemStack item = createSkull("Plant", "Leaves");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.MELON)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.MelonChance))
            {
                ItemStack item = createSkull("MHF_Melon", "Melon");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.PUMPKIN)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.PumpkinChance))
            {
                ItemStack item = createSkull("MHF_Pumpkin", "Melon");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.CACTUS)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.CactusChance))
            {
                ItemStack item = createSkull("MHF_Cactus", "Cactus");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.REDSTONE_ORE)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.RedstoneBlockChance))
            {
                ItemStack item = createSkull("TheNewTsar", "Redstone Mini-Block");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.GRAVEL)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.GravelChance))
            {
                ItemStack item = createSkull("MushTurf", "Gravel Mini-Block");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.COBBLESTONE)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.CobblestoneChance))
            {
                ItemStack item = createSkull("JuanFco", "Cobblestone Mini-Block");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }
        else if (block.getType() == Material.OAK_PLANKS)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.OakCharactersChance))
            {
                String[] signs = {"ArrowRight", "ArrowLeft", "ArrowUp", "ArrowDown", "Exclamation", "Question"};
                int selection = r.nextInt(signs.length);
                ItemStack item = createSkull("MHF_"+signs[selection], signs[selection]+" Sign");
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
        }

    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onEntityKill(EntityDeathEvent e)
    {
        LivingEntity entity = e.getEntity();
        if (entity.getType() == EntityType.PUFFERFISH || entity.getType() == EntityType.SALMON || entity.getType() == EntityType.COD || entity.getType() == EntityType.TROPICAL_FISH)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.SushiChance))
            {
                ItemStack item = createSkull("lmaoki", "Sushi");
                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            }
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
                if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.BreadChance))
                {
                    ItemStack item = createSkull("BedHeadBread", "Bread");
                    e.getWhoClicked().getWorld().dropItemNaturally(e.getWhoClicked().getLocation(), item);
                }
            }
            else if (resultingItem == Material.COOKIE)
            {
                if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.CookieChance))
                {
                    ItemStack item = createSkull("QuadratCookie", "Cookie");
                    e.getWhoClicked().getWorld().dropItemNaturally(e.getWhoClicked().getLocation(), item);
                }
            }
            else if (resultingItem == Material.CAKE)
            {
                if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.CakeChance))
                {
                    ItemStack item = createSkull("MHF_Cake", "Cake");
                    e.getWhoClicked().getWorld().dropItemNaturally(e.getWhoClicked().getLocation(), item);
                }
            }
        }

    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onItemSmelted(FurnaceSmeltEvent e)
    {
        ItemStack smeltResult = e.getResult();
        if (smeltResult.getType() == Material.COOKED_BEEF)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.BurgerChance))
            {
                ItemStack item = createSkull("Pesquiburguer", "Burger");
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
            }
        }
        else if (smeltResult.getType() == Material.COOKED_CHICKEN)
        {
            if (roll() <= Settings.getDropChance(Settings.DropChanceCategory.ChickenChance))
            {
                ItemStack item = createSkull("Ernie77", "Chicken Dinner");
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
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

    private ItemStack createSkull(String playerName, String skullName)
    {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        Bukkit.getUnsafe().modifyItemStack(skull, "{display:{Name:\"{\\\"text\\\":\\\""+skullName+"\\\"}\"},SkullOwner:\""+playerName+"\"}");
        return skull;
    }

    private int roll()
    {
        return r.nextInt(99)+1;
    }

}
