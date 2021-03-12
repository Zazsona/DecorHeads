package com.zazsona.decorheads;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.zazsona.decorheads.apiresponse.NameUUIDResponse;
import com.zazsona.decorheads.apiresponse.ProfileResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class HeadManager
{
    public enum HeadType
    {
        Apple,
        Aquarium,
        BeachBall,
        Beer,
        Blaze,
        Blueberry,
        Books,
        Bread,
        Burger,
        CardboardBox,
        CaveSpider,
        Chicken,
        ChickenDinner,
        Chimney,
        Clock,
        Coconut,
        Cookie,
        CookieJar,
        Cow,
        Creeper,
        Cupcake,
        Dice,
        Dolphin,
        Drowned,
        ElderGuardian,
        Enderman,
        Football,
        Fox,
        FruitBasket,
        Ghast,
        GoldMedal,
        Guardian,
        Husk,
        InkJar,
        IronGolem,
        Lantern,
        MagmaCube,
        MiniCactus,
        MiniCobblestone,
        MiniDirt,
        MiniGravel,
        MiniLeaves,
        MiniBirchLeaves,
        MiniJungleLeaves,
        MiniAcaciaLeaves,
        MiniSpruceLeaves,
        MiniMelon,
        MiniOakLog,
        MiniBirchLog,
        MiniJungleLog,
        MiniAcaciaLog,
        MiniDarkOakLog,
        MiniSpruceLog,
        MiniCrimsonStem,
        MiniWarpedStem,
        MiniPumpkin,
        MiniRedstoneBlock,
        Mooshroom,
        MushroomBrown,
        MushroomRed,
        Newspapers,
        OakCharacterDownArrow,
        OakCharacterExclamation,
        OakCharacterLeftArrow,
        OakCharacterQuestion,
        OakCharacterRightArrow,
        OakCharacterUpArrow,
        Ocelot,
        PicnicBasket,
        Pig,
        Pillager,
        PorcelainVase,
        Potion,
        PottedDaisy,
        PottedDandelion,
        PottedPoppy,
        Present,
        SandBucket,
        Sandcastle,
        Sheep,
        Skeleton,
        Slime,
        Slimeball,
        SnowmanHead,
        Spider,
        Squid,
        StackOfCarrots,
        StevePlush,
        Stray,
        Stool,
        StoolLog,
        Sushi,
        TV,
        Toaster,
        ToiletPaper,
        Vex,
        Villager,
        WanderingTrader,
        Witch,
        WitherSkeleton,
        WoodenClock,
        WoodenCrate,
        Zombie,
        ZombiePigman,
        ZombieVillager,
        Player
    }

    public static HeadType getHeadByName(String name)
    {
        name = name.replace(" ", "");
        for (HeadType head : HeadType.values())
        {
            if (head.name().replace("OakCharacter", "").equalsIgnoreCase(name))
            {
                return head;
            }
        }
        return null;
    }

    public static ItemStack getSkull(HeadType headType)
    {
        switch (headType)
        {
            case Beer:
                return createSkull( "Beer", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA1M2UyNjg2N2JiNTc1MzhlOTc4OTEzN2RiYmI1Mzc3NGUxOGVkYTZmZWY1MWNiMmVkZjQyNmIzNzI2NCJ9fX0=");
            case Books:
                return createSkull( "Books", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFjNjNkOWI5ZmQ4NzQyZWFlYjA0YzY5MjE3MmNiOWRhNDM3ODE2OThhNTc1Y2RhYmUxYzA0ZGYxMmMzZiJ9fX0=");
            case Cupcake:
                return createSkull("Cupcake", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjkxMzY1MTRmMzQyZTdjNTIwOGExNDIyNTA2YTg2NjE1OGVmODRkMmIyNDkyMjAxMzllOGJmNjAzMmUxOTMifX19");
            case MiniDirt:
                return createSkull("Mini Dirt", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFiNDNiOGMzZDM0ZjEyNWU1YTNmOGI5MmNkNDNkZmQxNGM2MjQwMmMzMzI5ODQ2MWQ0ZDRkN2NlMmQzYWVhIn19fQ==");
            case Bread:
                return createSkull("Bread", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM0ODdkNDU3ZjkwNjJkNzg3YTNlNmNlMWM0NjY0YmY3NDAyZWM2N2RkMTExMjU2ZjE5YjM4Y2U0ZjY3MCJ9fX0=");
            case MiniMelon:
                return createSkull("Mini Melon", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQxNDEyYjhjNmZkNTdlNGUxNjIxNjZkZGZkNzRiMTQ4YTU5NmY5ZWIxZDE1OTNjMDQ2OTYzOGM4ZDcxNCJ9fX0=");
            case Sushi:
                return createSkull("Sushi", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUxMmYyNjc5NTNlNzZhZTY2YThkZDAyNWEzMjg2YWVjYmM2NGI0YWQ5OGVlYjEwYjNjNjdhNjlhYWUxNSJ9fX0=");
            case Burger:
                return createSkull("Burger", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0=");
            case MiniCactus:
                return createSkull("Mini Cactus", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc0ZjU4YjAwMWJlZWJmNGVlMTU4MGQxN2UwZWMyOTdjNzY0OGIxNDhiNmVhOWMwNTc0ZTQ3ZjAwOGU1Y2QifX19");
            case Cookie:
                return createSkull("Cookie", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU5MmNmOWY0MmE1YThjOTk1OTY4NDkzZmRkMWIxMWUwYjY5YWFkNjQ3M2ZmNDUzODRhYmU1OGI3ZmM3YzcifX19");
            case MiniGravel:
                return createSkull("Mini Gravel", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc4OGI2MWZjZDQ2Y2FkOTIzNWNjMzJhYzU3YTU0ZjcwODExMDMyOTQ0N2JiMjRjMDg3Nzg4NmUzZjQ5MDdmMSJ9fX0=");
            case MiniLeaves:
                return createSkull("Mini Oak Leaves", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTRjNjI4NDhhZTE0YmE2ZDczNjkyMWRhOWFjMWY1ZDQ2MDZjMWM0ZmUxM2RjODQzOTYxZDZiZmJiMzMwYmMifX19");
            case MiniBirchLeaves:
                return createSkull("Mini Birch Leaves", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjVmYjQyM2EwZjg5Nzc3YTVhNTNjYTJkZDJhOTZmYzMwMjMxMzgwNzA0M2ZkMjliMGUwODQ1YTM3NzA1Yzg5OCJ9fX0=");
            case MiniJungleLeaves:
                return createSkull("Mini Jungle Leaves", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgzYWRmNDU2MGRlNDc0MTQwNDA5M2FjNjFjMzNmYjU1NmIzZDllZTUxNDBmNjIwMzYyNTg5ZmRkZWRlZmEyZCJ9fX0=");
            case MiniAcaciaLeaves:
                return createSkull("Mini Acacia Leaves", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjM2MTI3MjE0YWI0YTJlYjg1NTlmY2NmNGRlODZjODFkM2IyYjU5MjI0YzQyYzYyYzc3NDJhYzEyZGQ5NzBkNyJ9fX0=");
            case MiniSpruceLeaves:
                return createSkull("Mini Spruce Leaves", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFhOTFhODdiZGFlMjRmYzRmYTk1NjAxMjJjNWFlMWY3MmZlZjRkNDY5MDA1YjRiN2JhMTMwMWQ1ZjI2OTM1MCJ9fX0=");
            case MiniOakLog:
                return createSkull("Mini Oak Log", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQyZTMxMDg3OWE2NDUwYWY1NjI1YmNkNDUwOTNkZDdlNWQ4ZjgyN2NjYmZlYWM2OWM4MTUzNzc2ODQwNmIifX19");
            case MiniBirchLog:
                return createSkull("Mini Birch Log", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5MWYzYjczZWJiOWRlYzkxZWRkYzgzNjFjYTJmZWNmNTI4MGQyYzczM2VkYTllY2I2OTVmODNkMTU4MCJ9fX0=");
            case MiniJungleLog:
                return createSkull("Mini Jungle Log", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODVlMWQzMDhkYzNkNzI1M2YzMmUzMDBlZGE0ZDAwMDNmZTY0YTU2YmYzYTU0NjZmZDIwMTFiMzVmNDAzZGEzYiJ9fX0=");
            case MiniAcaciaLog:
                return createSkull("Mini Acacia Log", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWMxMDBkZDJlMWUyZGMyOTM4NjVmODc0N2M5MDRjNzM3YmFhYjIzNmU2ZTU0Y2NjMmQzYmVkNmQxZmE0MmQzYSJ9fX0=");
            case MiniDarkOakLog:
                return createSkull("Mini Dark Oak Log", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjllOGE3YjM3ZjM0MmU4ZmI0NThhOWM5ODlmNTE5NmMyNjUxYjg3ODExZGU1NGZlMDVlNjFiZjRkMjg3Yjk5YiJ9fX0=");
            case MiniSpruceLog:
                return createSkull("Mini Spruce Log", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY0YWYyODk4MjVlMTI3ZGFjMzIyMDhiMWUwMzg5YTkyN2VmNDA1NTk3YjZjOWE2NGNlZjAzZjIxZDkyNzNiNSJ9fX0=");
            case MiniCrimsonStem:
                return createSkull("Mini Crimson Stem", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQzYjRjODdmOTc4MThkNzFmMTNhZmE2ZGI5NDZmYjBhMjM1Y2Q3NzFhZTg2OTkyNGM5OGIzZDE3ZDc2NDk5YiJ9fX0=");
            case MiniWarpedStem:
                return createSkull("Mini Warped Stem", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFhNGJiNGE3ODJhNjM2NzJhMzM5YTc0Y2QxNzMxMTlkYjA5YTA0NTY4NTA0MDVhMzRjN2MwZjRiZmRiOWQxNSJ9fX0=");
            case Coconut:
                return createSkull("Coconut", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJjNjJmZDhlNDc0ZDA5OTQwNjA0ZjgyNzEyYTQ0YWJiMjQ5ZDYzYWZmODdmOTk4Mzc0Y2E4NDlhYjE3NDEyIn19fQ==");
            case MiniPumpkin:
                return createSkull("Mini Pumpkin", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdkN2FkMmRjYjU3ZGZhOWYwMjNkYmI5OWI2OThmYzUzMDc1YzNlOWQ2NTQ1MDYxMzlhNjQ3YWM5MDdmZGRjNSJ9fX0=");
            case SnowmanHead:
                return createSkull("Snowman Head", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU4ZDIwNmY2MWU2ZGU4YTc5ZDBjYjBiY2Q5OGFjZWQ0NjRjYmZlZmM5MjFiNDE2MGEyNTI4MjE2MzExMmEifX19");
            case Apple:
                return createSkull("Apple", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdiMTVmYzBmODk0NTNiYjljODY5ZGJjNDdhNjZjZjJlNzVlOGQzNzEzYjVmYmY3Yzg0Y2JmMmM2MzIxOTYifX19");
            case Blueberry:
                return createSkull("Blueberry", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFlOGUzNTJiM2ZiYWRjZTk5OGYxNTM1OTVkODM0MGRlZDI4NWMwNWFmNGFmNTdjNDQ1MWU4MjE0ZTFhZmIyIn19fQ==");
            case MiniCobblestone:
                return createSkull("Mini Cobblestone", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk1NTM0ZTAyYzU5YjMzZWNlNTYxOTI4MDMzMTk3OTc3N2UwMjVmYTVmYTgxYWU3NWU5OWZkOGVmZGViYjgifX19");
            case ChickenDinner:
                return createSkull("Chicken Dinner", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjA2NTU1NzA2YjY0MWZkYWY0MzZjMDc2NjNmOTIzYWZjNWVlNzIxNDZmOTAxOTVmYjMzN2I5ZGU3NjY1ODhkIn19fQ==");
            case MiniRedstoneBlock:
                return createSkull("Mini Redstone Block", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA4ZWU2ZWRmYTk4ZGI1ZWFlOWI5Yzk5MzZlOTQ0ODliMmQ0YmJiZDNkMmI0YjZiNDg4NWEzMjI0MDYxM2MifX19");
            case OakCharacterExclamation:
                return createSkull( "Exclamation", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE1M2JkZDE1NDU1MzFjOWViYjljNmY4OTViYzU3NjAxMmY2MTgyMGU2ZjQ4OTg4NTk4OGE3ZTg3MDlhM2Y0OCJ9fX0=");
            case OakCharacterQuestion:
                return createSkull("Question", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTE2M2RhZmFjMWQ5MWE4YzkxZGI1NzZjYWFjNzg0MzM2NzkxYTZlMThkOGY3ZjYyNzc4ZmM0N2JmMTQ2YjYifX19");
            case OakCharacterLeftArrow:
                return createSkull("Left Arrow", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ViZjkwNzQ5NGE5MzVlOTU1YmZjYWRhYjgxYmVhZmI5MGZiOWJlNDljNzAyNmJhOTdkNzk4ZDVmMWEyMyJ9fX0=");
            case OakCharacterRightArrow:
                return createSkull("Right Arrow", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI2ZjFhMjViNmJjMTk5OTQ2NDcyYWVkYjM3MDUyMjU4NGZmNmY0ZTgzMjIxZTU5NDZiZDJlNDFiNWNhMTNiIn19fQ==");
            case OakCharacterUpArrow:
                return createSkull("Up Arrow", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ4Yjc2OGM2MjM0MzJkZmIyNTlmYjNjMzk3OGU5OGRlYzExMWY3OWRiZDZjZDg4ZjIxMTU1Mzc0YjcwYjNjIn19fQ==");
            case OakCharacterDownArrow:
                return createSkull("Down Arrow", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmRhZGQ3NTVkMDg1MzczNTJiZjdhOTNlM2JiN2RkNGQ3MzMxMjFkMzlmMmZiNjcwNzNjZDQ3MWY1NjExOTRkZCJ9fX0=");


            case Dice:
                return createSkull("Dice", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzgzMTEzOGMyMDYxMWQzMDJjNDIzZmEzMjM3MWE3NDNkMTc0MzdhMTg5NzNjMzUxOTczNDQ3MGE3YWJiNCJ9fX0=");
            case TV: //Black model
                return createSkull("TV", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQxZTk5NzkyODlmMDMwOTlhN2M1ODdkNTJkNDg4ZTI2ZTdiYjE3YWI1OTRiNjlmOTI0MzhkNzdlYWJjIn19fQ==");
            case Clock: //Cyan model
                return createSkull("Clock", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmRlNGUyNzgzZjg1YTM5MTIyMWRkOTE2NTZiYTY4OGU3ZTQyZDE2ZjZhYmJmYmNmYWQ5Y2E1MzYxN2ZjYTYifX19");
            case WoodenClock:
                return createSkull("Wooden Clock", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ3N2RhZmM4YzllYTA3OTk2MzMzODE3OTM4NjZkMTQ2YzlhMzlmYWQ0YzY2ODRlNzExN2Q5N2U5YjZjMyJ9fX0=");
            case Aquarium:
                return createSkull("Aquarium", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzI4NDdjZDU3MTdlNWY1YTY0ZTFiYTljYjQ4MWRjOWUyMmM3OGNhMjNmODUxNmQ1NTNmNTU0MTJmYTExM2UwIn19fQ==");
            case Toaster: //Steel
                return createSkull("Toaster", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmI1M2U4ZDM3NGI0ZjZmNTczZDEyODY2ODFiZjg0MTA1NWI4OWE0NjJmN2NkZDk5ZThlNjNkMmY1MTRlNDUifX19");
            case Lantern: //Wood
                return createSkull("Lantern", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2NjMjE3YTliOWUzY2UzY2QwNDg0YzdlOGNlNDlkMWNmNzQxMjgxYmRkYTVhNGQ2Y2I4MjFmMzc4NzUyNzE4In19fQ==");
            case Football:
                return createSkull("Football", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU0YTcwYjdiYmNkN2E4YzMyMmQ1MjI1MjA0OTFhMjdlYTZiODNkNjBlY2Y5NjFkMmI0ZWZiYmY5ZjYwNWQifX19");
            case Present:
                return createSkull("Present", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlZjlhYTE0ZTg4NDc3M2VhYzEzNGE0ZWU4OTcyMDYzZjQ2NmRlNjc4MzYzY2Y3YjFhMjFhODViNyJ9fX0=");
            case PottedPoppy:
                return createSkull("Potted Poppy", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWRiYTM4ZTlmYzY3ZjcyYzQ1OGZkYWM4ZWNkN2NhYmFlZDNlYjgzNzM3MTQzYTAxMjgzNTBhMWFiMzgxZTNlIn19fQ==");
            case PottedDandelion:
                return createSkull("Potted Dandelion", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjNmZmZjODk1NWIwZTgzMDI4OThmN2YwMTVkODQ5ZjBhMDFkYmJiMDQyNzQxNzUwNmZiODllYWQ1NGQ0NWY2In19fQ==");
            case PottedDaisy:
                return createSkull("Potted Daisy", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWRjNGMxMmJmMjYxOWNiZmM4ZjIyZGM2MmMwMjJjZTE1MTI2Y2VhM2UyMTJjMjhkOWY5NmVhMzEwYWM0YzQyIn19fQ==");
            case Chimney:
                return createSkull("Chimney", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGEyOWVlZjljYTM2OWVhZGQ5ODlhNjgzMmY1MDk3MGY4YjA3NGY5ZDcwNjU5NGUzNzhhOGI4ODRhZjRiZDllIn19fQ==");
            case CookieJar:
                return createSkull("Cookie Jar", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQ3NjY5NWRmY2MxNzBiZDc0ZWFkNGNiYWYxNzVlMTM3ODdjYmRmN2YzMmYwOGZmY2FhZmY2Mzg1OGUzYzEyMCJ9fX0=");
            case CardboardBox:
                return createSkull("Cardboard Box", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWYyNTg0OWU4Y2Q1ZTUzMmMzMGNiYThkZThiZWQwNjQwY2ZiMmVlYzUwOTI5OTk4YjEwNzgzOWJmYjBmMjRkNyJ9fX0=");

            case Slimeball:
                return createSkull("Slimeball", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDkzNGE5ZjVhYjE3ODlhN2Q4ZGQ5NmQzMjQ5M2NkYWNmZjU3N2Q4YzgxZTdiMjM5MTdkZmYyZTMyYmQwYmMxMCJ9fX0=");
            case ToiletPaper:
                return createSkull("Toilet Paper", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM4YzIyOTg1YjAzMzZmODE3ZmMzMDdjYmE2YzE1ZGE0ODRiZjIzNjFjNjBmNWEzM2U2YTk1NTdlZTJkIn19fQ==");
            case Potion:
                return createSkull("Potion", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFmODE1MWRlYjM5NTUzMjkyYjlhNjk2NzQ4MzJkZGE4MDAxNzgwY2E5ODE4MGEwNmVhZjliMzY4OTMwYjViYiJ9fX0=");
            case InkJar:
                return createSkull("Ink Jar", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTAyOGQ0NzRjNGEzMzJkY2Y4ZTM0Mzk1N2NlMGNhNGVlYmFjZTI1OGMzN2Y2ZDkwNGNmMjI4ZDM0NTY3ZjU1MSJ9fX0=");
            case Newspapers:
                return createSkull("Newspapers", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODNkODc0ZWI4YzRjNjk3YjNmODMyYmQ4NzQ0MjZmZGY2ZDIxYmFlMzM5ZjMxNzExMDgxZmRlNTk4MzgzODZlMSJ9fX0=");
            case FruitBasket:
                return createSkull("Fruit Basket", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU3ZTJjNTc1NmQ0ZjQ0ZDM4MzQ1ODI2NjNlNmQ4NDhlMTE2NDE2MzRjYjIwNWI3Y2EzZWE0YWQyODU3MzQxIn19fQ==");
            case WoodenCrate:
                return createSkull("Wooden Crate", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQ3NjFjYzE2NTYyYzg4ZDJmYmU0MGFkMzg1MDJiYzNiNGE4Nzg1OTg4N2RiYzM1ZjI3MmUzMGQ4MDcwZWVlYyJ9fX0=");


            case Chicken:
                return createSkull("Chicken", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYzODQ2OWE1OTljZWVmNzIwNzUzNzYwMzI0OGE5YWIxMWZmNTkxZmQzNzhiZWE0NzM1YjM0NmE3ZmFlODkzIn19fQ==");
            case Cow:
                return createSkull("Cow", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ2YzZlZGE5NDJmN2Y1ZjcxYzMxNjFjNzMwNmY0YWVkMzA3ZDgyODk1ZjlkMmIwN2FiNDUyNTcxOGVkYzUifX19");
            case Fox:
                return createSkull("Fox", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg5NTRhNDJlNjllMDg4MWFlNmQyNGQ0MjgxNDU5YzE0NGEwZDVhOTY4YWVkMzVkNmQzZDczYTNjNjVkMjZhIn19fQ==");
            case Mooshroom:
                return createSkull("Mooshroom", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBiYzYxYjk3NTdhN2I4M2UwM2NkMjUwN2EyMTU3OTEzYzJjZjAxNmU3YzA5NmE0ZDZjZjFmZTFiOGRiIn19fQ==");
            case Ocelot:
                return createSkull("Ocelot", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTY1N2NkNWMyOTg5ZmY5NzU3MGZlYzRkZGNkYzY5MjZhNjhhMzM5MzI1MGMxYmUxZjBiMTE0YTFkYjEifX19");
            case Pig:
                return createSkull("Pig", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIxNjY4ZWY3Y2I3OWRkOWMyMmNlM2QxZjNmNGNiNmUyNTU5ODkzYjZkZjRhNDY5NTE0ZTY2N2MxNmFhNCJ9fX0=");
            case Sheep:
                return createSkull("Sheep", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMxZjljY2M2YjNlMzJlY2YxM2I4YTExYWMyOWNkMzNkMThjOTVmYzczZGI4YTY2YzVkNjU3Y2NiOGJlNzAifX19");
            case Squid:
                return createSkull("Squid", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDE0MzNiZTI0MjM2NmFmMTI2ZGE0MzRiODczNWRmMWViNWIzY2IyY2VkZTM5MTQ1OTc0ZTljNDgzNjA3YmFjIn19fQ==");
            case Villager:
                return createSkull("Villager", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIyZDhlNzUxYzhmMmZkNGM4OTQyYzQ0YmRiMmY1Y2E0ZDhhZThlNTc1ZWQzZWIzNGMxOGE4NmU5M2IifX19");
            case WanderingTrader:
                return createSkull("Wandering Trader", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzc5YTgyMjkwZDdhYmUxZWZhYWJiYzcwNzEwZmYyZWMwMmRkMzRhZGUzODZiYzAwYzkzMGM0NjFjZjkzMiJ9fX0=");


            case Spider:
                return createSkull("Spider", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q1NDE1NDFkYWFmZjUwODk2Y2QyNThiZGJkZDRjZjgwYzNiYTgxNjczNTcyNjA3OGJmZTM5MzkyN2U1N2YxIn19fQ==");
            case CaveSpider:
                return createSkull("Cave Spider", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE2NDVkZmQ3N2QwOTkyMzEwN2IzNDk2ZTk0ZWViNWMzMDMyOWY5N2VmYzk2ZWQ3NmUyMjZlOTgyMjQifX19");
            case Enderman:
                return createSkull("Enderman", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E1OWJiMGE3YTMyOTY1YjNkOTBkOGVhZmE4OTlkMTgzNWY0MjQ1MDllYWRkNGU2YjcwOWFkYTUwYjljZiJ9fX0=");
            case ZombiePigman:
                return createSkull("Zombie Pigman", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRlOWM2ZTk4NTgyZmZkOGZmOGZlYjMzMjJjZDE4NDljNDNmYjE2YjE1OGFiYjExY2E3YjQyZWRhNzc0M2ViIn19fQ==");
            case IronGolem:
                return createSkull("Iron Golem", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwOTFkNzllYTBmNTllZjdlZjk0ZDdiYmE2ZTVmMTdmMmY3ZDQ1NzJjNDRmOTBmNzZjNDgxOWE3MTQifX19");
            case Dolphin:
                return createSkull("Dolphin", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5Njg4Yjk1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ==");


            case Blaze:
                return createSkull("Blaze", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc4ZWYyZTRjZjJjNDFhMmQxNGJmZGU5Y2FmZjEwMjE5ZjViMWJmNWIzNWE0OWViNTFjNjQ2Nzg4MmNiNWYwIn19fQ==");
            case Creeper:
                return createSkull("Creeper", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQyNTQ4MzhjMzNlYTIyN2ZmY2EyMjNkZGRhYWJmZTBiMDIxNWY3MGRhNjQ5ZTk0NDQ3N2Y0NDM3MGNhNjk1MiJ9fX0=");
            case Drowned:
                return createSkull("Drowned", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg0ZGY3OWM0OTEwNGIxOThjZGFkNmQ5OWZkMGQwYmNmMTUzMWM5MmQ0YWI2MjY5ZTQwYjdkM2NiYmI4ZTk4YyJ9fX0=");
            case ElderGuardian:
                return createSkull("Elder Guardian", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM3OTc0ODJhMTRiZmNiODc3MjU3Y2IyY2ZmMWI2ZTZhOGI4NDEzMzM2ZmZiNGMyOWE2MTM5Mjc4YjQzNmIifX19");
            case Ghast:
                return createSkull("Ghast", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI2YTcyMTM4ZDY5ZmJiZDJmZWEzZmEyNTFjYWJkODcxNTJlNGYxYzk3ZTVmOTg2YmY2ODU1NzFkYjNjYzAifX19");
            case Guardian:
                return createSkull("Guardian", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBiZjM0YTcxZTc3MTViNmJhNTJkNWRkMWJhZTVjYjg1Zjc3M2RjOWIwZDQ1N2I0YmZjNWY5ZGQzY2M3Yzk0In19fQ==");
            case Husk:
                return createSkull("Husk", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY3NGM2M2M4ZGI1ZjRjYTYyOGQ2OWEzYjFmOGEzNmUyOWQ4ZmQ3NzVlMWE2YmRiNmNhYmI0YmU0ZGIxMjEifX19");
            case MagmaCube:
                return createSkull("Magma Cube", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzg5NTdkNTAyM2M5MzdjNGM0MWFhMjQxMmQ0MzQxMGJkYTIzY2Y3OWE5ZjZhYjM2Yjc2ZmVmMmQ3YzQyOSJ9fX0=");
            case Pillager:
                return createSkull("Pillager", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk1NDEzNWRjODIyMTM5NzhkYjQ3ODc3OGFlMTIxMzU5MWI5M2QyMjhkMzZkZDU0ZjFlYTFkYTQ4ZTdjYmE2In19fQ==");
            case Skeleton:
                return createSkull("Skeleton", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzAxMjY4ZTljNDkyZGExZjBkODgyNzFjYjQ5MmE0YjMwMjM5NWY1MTVhN2JiZjc3ZjRhMjBiOTVmYzAyZWIyIn19fQ==");
            case Slime:
                return createSkull("Slime", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODk1YWVlYzZiODQyYWRhODY2OWY4NDZkNjViYzQ5NzYyNTk3ODI0YWI5NDRmMjJmNDViZjNiYmI5NDFhYmU2YyJ9fX0=");
            case Stray:
                return createSkull("Stray", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhkZGY3NmU1NTVkZDVjNGFhOGEwYTVmYzU4NDUyMGNkNjNkNDg5YzI1M2RlOTY5ZjdmMjJmODVhOWEyZDU2In19fQ==");
            case Vex:
                return createSkull("Vex", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJlYzVhNTE2NjE3ZmYxNTczY2QyZjlkNWYzOTY5ZjU2ZDU1NzVjNGZmNGVmZWZhYmQyYTE4ZGM3YWI5OGNkIn19fQ==");
            case Witch:
                return createSkull("Witch", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBlMTNkMTg0NzRmYzk0ZWQ1NWFlYjcwNjk1NjZlNDY4N2Q3NzNkYWMxNmY0YzNmODcyMmZjOTViZjlmMmRmYSJ9fX0=");
            case WitherSkeleton:
                return createSkull("Wither Skeleton", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzk1M2I2YzY4NDQ4ZTdlNmI2YmY4ZmIyNzNkNzIwM2FjZDhlMWJlMTllODE0ODFlYWQ1MWY0NWRlNTlhOCJ9fX0=");
            case Zombie:
                return createSkull("Zombie", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ==");
            case ZombieVillager:
                return createSkull("Zombie Villager", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTVlMDhhODc3NmMxNzY0YzNmZTZhNmRkZDQxMmRmY2I4N2Y0MTMzMWRhZDQ3OWFjOTZjMjFkZjRiZjNhYzg5YyJ9fX0=");


            case BeachBall:
                return createSkull("Beach Ball", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWIyNTEzYzhkMDhjNjBhZDM3ODVkM2E5YTY1MWI3MzI5YzVmMjY5MzdhY2EyZmM4ZGZhZjM0NDFjOWJkOWRhMiJ9fX0=");
            case Sandcastle:
                return createSkull("Sandcastle", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWE0YjdlODc4MjFkYjRlNmU0MmU3OGQ3ZjI2MGI2Mzc5OTNkNmJlOGRlOTRmNDNlNDMxNzViYWZmOTFmNSJ9fX0=");
            case PicnicBasket:
                return createSkull("Picnic Basket", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEwMDMzMTg5MzQyMDUwNTZjNWRhYWEzOGI3YjY2ZDgxZmI0NTAwYWZlMGJkYzQ0NzNhNGY4NmQ1MDI1Y2UzIn19fQ==");
            case SandBucket:
                return createSkull("Sand Bucket", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQ0MzdkMTY4ODU5MjBjZTU1NzczM2Y0YzcxMGJiNTAwN2YwYTRmMWEwZGRkMzJkNTk0NjlhYmM0ZjdlN2JlMCJ9fX0=");

            case PorcelainVase:
                return createSkull("Vase", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0NTA1NWZhNjQ4Y2RiYjJjNjIwYmE1NjUwYjI5ZWMwM2MwMzAyNmJhNGZhODhhZmYwNTUyOTc0NzM3YjJiNyJ9fX0=");
            case GoldMedal:
                return createSkull("Gold Medal", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMxNzhkMDFhYWYxMWU1Y2NmYTM4YjE5MGQ3MDg1ZmYzN2RkMDQ4ZDA5YjY2NGYwMjU3MDc3NmY5Yjk4MTQ2NiJ9fX0=");
            case StevePlush:
                return createSkull("Steve Plush", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjYyMjE3ZDkwMmVkYzkyOWRlNGY1MWI5MDcxZjU2YmFhYWMwNDVkMDRlYzA0Yjg0MGNjYzhlYzk5NjJmNjJlMyJ9fX0=");

            case StackOfCarrots:
                return createSkull("Stack of Carrots", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmVkMGI3NTZmYzYyMzg1NTEzYjU3MDE1OTY3NDZiZDdiMWRhYTE1ZDdhZGFiZjQ1ZWYxNWJkMzQzNzNkOWY1YyJ9fX0=");
            case MushroomBrown:
                return createSkull("Brown Mushroom", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzZlMTJjZmYxMzhmZDJmZGMzMWM2OTU1NmNjYWFlYzdhZWQxNmY1ZDMxOGQ3YmMwOGU1MTRhZDY2MTg2MWJiMSJ9fX0=");
            case MushroomRed:
                return createSkull("Red Mushroom", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDAyNDdkMjNhZDg3Nzg2YTI3NTNiM2Q4ZGY1ZGRkMzIzZDMyMjdkNTQ4NzE5YzZlZDQ0NmRhNzMwOTczNyJ9fX0=");
            case Stool:
                return createSkull("Stool", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0ODE5OTEzOWJmMmI2Y2Q0NGU3NGFhMjQ5OGJhY2M1MGQxZWRlYzgyMWVjOTM0NDlmMzlkNmMxYzZmOTkwZSJ9fX0=");
            case StoolLog:
                return createSkull("Log Stool", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE1MGMzYTczMTJhYWJkMWFkNzA0N2MxOGY3ZGE1OGY0ZWQxOTllMTBjNTZkMDYwNGExMmRjYzg2Yjk0YTJiYiJ9fX0=");
        }
        return null;
    }
    private static ItemStack createSkull(String skullName, String texture)
    {
        try
        {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            UUID uuid = UUID.fromString("8a2de1bb-a9b2-4f00-b0e3-d360ad407928"); //Random UUID unassigned to any Minecraft account.
            GameProfile gameProfile = new GameProfile(uuid, skullName);
            gameProfile.getProperties().put("textures", new Property("textures", texture));
            Field skullProfile = skullMeta.getClass().getDeclaredField("profile");
            skullProfile.setAccessible(true);
            skullProfile.set(skullMeta, gameProfile);
            skullMeta.setDisplayName(skullName);
            ArrayList<String> loreList = new ArrayList<>();
            loreList.add(ChatColor.BLUE+"DecorHeads");
            skullMeta.setLore(loreList);
            skull.setItemMeta(skullMeta);
            return skull;
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "INVALID HEAD ITEM \""+skullName+"\" - NO PROFILE FIELD!");
            return new ItemStack(Material.PLAYER_HEAD);
        }
    }

    public static ItemStack getPlayerSkull(String username)
    {
        try
        {
            URL restRequest = new URL("https://api.mojang.com/users/profiles/minecraft/"+username);
            InputStreamReader inputStreamReader = new InputStreamReader(restRequest.openStream());
            BufferedReader reader = new BufferedReader((inputStreamReader));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                responseBuilder.append(line);
            }
            Gson gson = new Gson();
            NameUUIDResponse uuidResponse = gson.fromJson(responseBuilder.toString(), NameUUIDResponse.class);
            if (uuidResponse.isSuccess())
            {
                String uuid = uuidResponse.getId().replaceFirst( "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5" );
                return getPlayerSkull(UUID.fromString(uuid));
            }
        }
        catch (IOException | NullPointerException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
        return new ItemStack(Material.PLAYER_HEAD);
    }

    public static ItemStack getPlayerSkull(UUID uuid)
    {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta  = (SkullMeta) skull.getItemMeta();
        OfflinePlayer offlinePlayer = Core.getPlugin(Core.class).getServer().getOfflinePlayer(uuid);
        if (offlinePlayer.hasPlayedBefore())
        {
            skullMeta.setOwningPlayer(offlinePlayer);
            skull.setItemMeta(skullMeta);
        }
        else
        {
            try
            {
                URL restRequest = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid.toString());
                InputStreamReader inputStreamReader = new InputStreamReader(restRequest.openStream());
                BufferedReader reader = new BufferedReader((inputStreamReader));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    responseBuilder.append(line);
                }
                Gson gson = new Gson();
                ProfileResponse pr = gson.fromJson(responseBuilder.toString(), ProfileResponse.class);
                if (pr.isSuccess())
                    return createSkull(pr.getName(), pr.getPropertyByName("textures").getValue());
            }
            catch (IOException e)
            {
                Bukkit.getLogger().log(Level.SEVERE, e.toString());
                e.printStackTrace();
            }
        }
        return skull;
    }
}
