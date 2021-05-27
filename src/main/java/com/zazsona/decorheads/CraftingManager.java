package com.zazsona.decorheads;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
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
        addToiletRoll();
        addPotion();
        addInkJar();
        addNewspapers();
        addFruitBasket();
        addWoodenCrate();
        addBeachball();
        addPicnicBasket();
        addVase();
        addGoldMedal();
        addStevePlush();
        addCarrotStack();
        addStool();
        addLogStool();
        addCaveDiorama();
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

    private static void addToiletRoll()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.ToiletPaper);
        NamespacedKey nsk = new NamespacedKey(plugin, "ToiletPaper");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("OOO", "OXO", "OOO");
        recipe.setIngredient('X', Material.OAK_PLANKS);
        recipe.setIngredient('O', Material.PAPER);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addPotion()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Potion);
        NamespacedKey nsk = new NamespacedKey(plugin, "Potion");
        ShapelessRecipe recipe = new ShapelessRecipe(nsk, item);
        recipe.addIngredient(Material.POTION);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addInkJar()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.InkJar);
        NamespacedKey nsk = new NamespacedKey(plugin, "InkJar");
        ShapelessRecipe recipe = new ShapelessRecipe(nsk, item);
        recipe.addIngredient(Material.GLASS_BOTTLE);
        recipe.addIngredient(Material.INK_SAC);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addNewspapers()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Newspapers);
        NamespacedKey nsk = new NamespacedKey(plugin, "Newspapers");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape(" X ", "OOO", " X ");
        recipe.setIngredient('X', Material.STRING);
        recipe.setIngredient('O', Material.PAPER);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addFruitBasket()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.FruitBasket);
        NamespacedKey nsk = new NamespacedKey(plugin, "FruitBasket");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape(" I ", "ABC", "XXX");
        recipe.setIngredient('X', Material.OAK_PLANKS);
        recipe.setIngredient('I', Material.STICK);
        recipe.setIngredient('A', Material.APPLE);
        recipe.setIngredient('B', Material.MELON_SLICE);
        recipe.setIngredient('C', Material.SWEET_BERRIES);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addWoodenCrate()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.WoodenCrate);
        NamespacedKey nsk = new NamespacedKey(plugin, "WoodenCrate");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("XXX", "XXX", "XXX");
        recipe.setIngredient('X', Material.OAK_PLANKS);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addBeachball()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.BeachBall);
        NamespacedKey nsk = new NamespacedKey(plugin, "Beachball");
        ShapelessRecipe recipe = new ShapelessRecipe(nsk, item);
        recipe.addIngredient(Material.BLUE_DYE);
        recipe.addIngredient(Material.GREEN_DYE);
        recipe.addIngredient(Material.YELLOW_DYE);
        recipe.addIngredient(Material.ORANGE_DYE);
        recipe.addIngredient(Material.RED_DYE);
        recipe.addIngredient(Material.PURPLE_DYE);
        recipe.addIngredient(Material.WHITE_DYE);
        recipe.addIngredient(Material.LEATHER);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addPicnicBasket()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.PicnicBasket);
        NamespacedKey nsk = new NamespacedKey(plugin, "PicnicBasket");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape(" I ", "ABA", "XXX");
        recipe.setIngredient('X', Material.OAK_PLANKS);
        recipe.setIngredient('I', Material.STICK);
        recipe.setIngredient('A', Material.RED_CARPET);
        recipe.setIngredient('B', Material.WHITE_CARPET);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addVase()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.PorcelainVase);
        NamespacedKey nsk = new NamespacedKey(plugin, "PorcelainVase");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("A A", "B B", "AAA");
        recipe.setIngredient('A', Material.CLAY_BALL);
        recipe.setIngredient('B', Material.BLUE_DYE);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addGoldMedal()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.GoldMedal);
        NamespacedKey nsk = new NamespacedKey(plugin, "GoldMedal");
        ShapelessRecipe recipe = new ShapelessRecipe(nsk, item);
        recipe.addIngredient(Material.GOLD_INGOT);
        recipe.addIngredient(Material.IRON_BLOCK);
        recipe.addIngredient(Material.STRING);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addStevePlush()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.StevePlush);
        NamespacedKey nsk = new NamespacedKey(plugin, "StevePlush");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("A", "B", "C");
        recipe.setIngredient('A', Material.YELLOW_WOOL);
        recipe.setIngredient('B', Material.LIGHT_BLUE_WOOL);
        recipe.setIngredient('C', Material.BLUE_WOOL);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addCarrotStack()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.StackOfCarrots);
        NamespacedKey nsk = new NamespacedKey(plugin, "StackOfCarrots");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("AA", "AA");
        recipe.setIngredient('A', Material.CARROT);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addStool()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.Stool);
        NamespacedKey nsk = new NamespacedKey(plugin, "Stool");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("PPP", "PPP", "P P");
        recipe.setIngredient('P', Material.OAK_PLANKS);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addLogStool()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.StoolLog);
        NamespacedKey nsk = new NamespacedKey(plugin, "StoolLog");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("LLL", "PPP", "P P");
        recipe.setIngredient('L', Material.OAK_LOG);
        recipe.setIngredient('P', Material.OAK_PLANKS);
        plugin.getServer().addRecipe(recipe);
    }

    private static void addCaveDiorama()
    {
        ItemStack item = HeadManager.getSkull(HeadManager.HeadType.CaveDiorama);
        NamespacedKey nsk = new NamespacedKey(plugin, "CaveDiorama");
        ShapedRecipe recipe = new ShapedRecipe(nsk, item);
        recipe.shape("PSS", "SSS", "SSZ");
        recipe.setIngredient('P', Material.LIGHT_BLUE_WOOL);
        recipe.setIngredient('S', Material.STONE);
        recipe.setIngredient('Z', Material.GREEN_WOOL);
        plugin.getServer().addRecipe(recipe);
    }
}
