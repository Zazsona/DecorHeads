package com.Zazsona;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class CraftingManager
{
    private static Plugin plugin = Core.getPlugin(Core.class);

    public static void addRecipes()
    {
        addDiceRecipe();
        addMonitorRecipe();
        addToasterRecipe();
        addClockRecipe();
        addWoodenClockRecipe();
        addAquariumRecipe();
        addLanternRecipe();
        addFootballRecipe();
        addPresentRecipe();
        addCookieJarRecipe();
        addCardBoxRecipe();
        addChimneyRecipe();
        addPottedPlants();
    }

    private static void addDiceRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Dice);
        NamespacedKey nsk = new NamespacedKey(plugin, "Dice");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XOO", "OXO", "OOX");
        recipe.setIngredient('X', Material.BLACK_CONCRETE);
        recipe.setIngredient('O', Material.WHITE_CONCRETE);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addMonitorRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.TV);
        NamespacedKey nsk = new NamespacedKey(plugin, "Monitor");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "XOX", "XXX");
        recipe.setIngredient('X', Material.BLACK_CONCRETE);
        recipe.setIngredient('O', Material.GLASS_PANE);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addToasterRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Toaster);
        NamespacedKey nsk = new NamespacedKey(plugin, "Toaster");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("   ", "XZX", "XXX");
        recipe.setIngredient('X', Material.IRON_INGOT);
        recipe.setIngredient('Z', Material.BREAD);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addClockRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Clock);
        NamespacedKey nsk = new NamespacedKey(plugin, "Clock");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "XOX", "XXX");
        recipe.setIngredient('X', Material.LIGHT_BLUE_CONCRETE);
        recipe.setIngredient('O', Material.CLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addWoodenClockRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.WoodenClock);
        NamespacedKey nsk = new NamespacedKey(plugin, "WoodenClock");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "XOX", "XXX");
        recipe.setIngredient('X', Material.SPRUCE_PLANKS);
        recipe.setIngredient('O', Material.CLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addAquariumRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Aquarium);
        NamespacedKey nsk = new NamespacedKey(plugin, "SalmonAquarium");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "XOX", "XXX");
        recipe.setIngredient('X', Material.GLASS_PANE);
        recipe.setIngredient('O', Material.SALMON);
        plugin.getServer().addRecipe(recipe);

        nsk = new NamespacedKey(plugin, "CodAquarium");
        recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "XOX", "XXX");
        recipe.setIngredient('X', Material.GLASS_PANE);
        recipe.setIngredient('O', Material.COD);
        plugin.getServer().addRecipe(recipe);

        nsk = new NamespacedKey(plugin, "TropicalAquarium");
        recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "XOX", "XXX");
        recipe.setIngredient('X', Material.GLASS_PANE);
        recipe.setIngredient('O', Material.TROPICAL_FISH);
        plugin.getServer().addRecipe(recipe);

        nsk = new NamespacedKey(plugin, "PufferAquarium");
        recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "XOX", "XXX");
        recipe.setIngredient('X', Material.GLASS_PANE);
        recipe.setIngredient('O', Material.PUFFERFISH);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addLanternRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Lantern);
        NamespacedKey nsk = new NamespacedKey(plugin, "Lantern");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "XOX", "XXX");
        recipe.setIngredient('X', Material.SPRUCE_PLANKS);
        recipe.setIngredient('O', Material.TORCH);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addFootballRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Football);
        NamespacedKey nsk = new NamespacedKey(plugin, "Football");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "XOX", "XXX");
        recipe.setIngredient('X', Material.LEATHER);
        recipe.setIngredient('O', Material.WHITE_DYE);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addPresentRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Present);
        NamespacedKey nsk = new NamespacedKey(plugin, "Present");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XOX", "OOO", "XOX");
        recipe.setIngredient('X', Material.RED_WOOL);
        recipe.setIngredient('O', Material.YELLOW_WOOL);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addCookieJarRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.CookieJar);
        NamespacedKey nsk = new NamespacedKey(plugin, "CookieJar");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "XOX", "XXX");
        recipe.setIngredient('X', Material.GLASS_PANE);
        recipe.setIngredient('O', Material.COOKIE);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addCardBoxRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.CardboardBox);
        NamespacedKey nsk = new NamespacedKey(plugin, "CardBox");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "X X", "XXX");
        recipe.setIngredient('X', Material.PAPER);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addChimneyRecipe()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Chimney);
        NamespacedKey nsk = new NamespacedKey(plugin, "Chimney");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("OOO", " X ", " X ");
        recipe.setIngredient('X', Material.COBBLESTONE);
        recipe.setIngredient('O', Material.SMOOTH_STONE_SLAB);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addPottedPlants()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.PottedPoppy);
        NamespacedKey nsk = new NamespacedKey(plugin, "PottedPoppy");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("   ", "ZOZ", "XXX");
        recipe.setIngredient('X', Material.OAK_PLANKS);
        recipe.setIngredient('O', Material.POPPY);
        recipe.setIngredient('Z', Material.OAK_LEAVES);
        plugin.getServer().addRecipe(recipe);

        item = HeadManager.getSkull(HeadManager.HeadType.PottedDandelion);
        nsk = new NamespacedKey(plugin, "PottedDandelion");
        recipe = new ShapedRecipe(nsk, item);
        recipe.shape("   ", "ZOZ", "XXX");
        recipe.setIngredient('X', Material.OAK_PLANKS);
        recipe.setIngredient('O', Material.DANDELION);
        recipe.setIngredient('Z', Material.OAK_LEAVES);
        plugin.getServer().addRecipe(recipe);

        item = HeadManager.getSkull(HeadManager.HeadType.PottedDaisy);
        nsk = new NamespacedKey(plugin, "PottedDaisy");
        recipe = new ShapedRecipe(nsk, item);
        recipe.shape("   ", "ZOZ", "XXX");
        recipe.setIngredient('X', Material.OAK_PLANKS);
        recipe.setIngredient('O', Material.OXEYE_DAISY);
        recipe.setIngredient('Z', Material.OAK_LEAVES);
        plugin.getServer().addRecipe(recipe);
    }
}
