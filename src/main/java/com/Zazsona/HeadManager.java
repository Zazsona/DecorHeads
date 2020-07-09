package com.Zazsona;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HeadManager
{
    public enum HeadType
    {
        //V1
        Beer,
        Books,
        SnowmanHead,
        Apple,
        Blueberry,
        Coconut,
        MiniOakLog,
        MiniDirt,
        MiniLeaves,
        MiniMelon,
        MiniPumpkin,
        MiniCactus,
        Sushi,
        Bread,
        Cookie,
        Cupcake,
        Burger,
        ChickenDinner,
        MiniRedstoneBlock,
        MiniGravel,
        MiniCobblestone,
        OakCharacterExclamation,
        OakCharacterQuestion,
        OakCharacterLeftArrow,
        OakCharacterRightArrow,
        OakCharacterUpArrow,
        OakCharacterDownArrow,

        //V2
        Dice,
        TV,
        Toaster,
        Clock,
        WoodenClock,
        Aquarium,
        Lantern,
        Football,
        Present,
        CookieJar,
        CardboardBox,
        Chimney,
        PottedPoppy,
        PottedDandelion,
        PottedDaisy,

        //V3
        Slimeball,
        ToiletPaper,
        Potion,
        InkJar,
        Newspapers,
        FruitBasket,
        WoodenCrate,

        //V4 (Mob update)
        Chicken,
        Cow,
        Fox,
        Mooshroom,
        Ocelot,
        Pig,
        Sheep,
        Squid,
        Villager,
        WanderingTrader,

        Spider,
        CaveSpider,
        Enderman,
        ZombiePigman,
        IronGolem,
        Dolphin,

        Blaze,
        Creeper,
        Drowned,
        ElderGuardian,
        Ghast,
        Guardian,
        Husk,
        MagmaCube,
        Pillager,
        Skeleton,
        Slime,
        Stray,
        Vex,
        Witch,
        WitherSkeleton,
        Zombie,
        ZombieVillager,

        //V5 (Summer Update)
        BeachBall,
        Sandcastle,
        PicnicBasket,
        SandBucket,

        PorcelainVase,
        GoldMedal,
        StevePlush
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
                return createSkull( "Beer", "e3e6b7a8-4246-41c4-9663-eddc9e", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA1M2UyNjg2N2JiNTc1MzhlOTc4OTEzN2RiYmI1Mzc3NGUxOGVkYTZmZWY1MWNiMmVkZjQyNmIzNzI2NCJ9fX0=\"}]");
            case Books:
                return createSkull( "Books", "6e166933-a807-4a73-8a19-e44053", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFjNjNkOWI5ZmQ4NzQyZWFlYjA0YzY5MjE3MmNiOWRhNDM3ODE2OThhNTc1Y2RhYmUxYzA0ZGYxMmMzZiJ9fX0=\"}]");
            case Cupcake:
                return createSkull("Cupcake", "534c47c4-d04d-416a-bf99-c3efd6" , "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjkxMzY1MTRmMzQyZTdjNTIwOGExNDIyNTA2YTg2NjE1OGVmODRkMmIyNDkyMjAxMzllOGJmNjAzMmUxOTMifX19\"}]");
            case MiniDirt:
                return createSkull("Mini Dirt", "ca021f3f-5002-46b2-bf34-985779", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFiNDNiOGMzZDM0ZjEyNWU1YTNmOGI5MmNkNDNkZmQxNGM2MjQwMmMzMzI5ODQ2MWQ0ZDRkN2NlMmQzYWVhIn19fQ==\"}]");
            case Bread:
                return createSkull("Bread", "a75e3f60-2242-4429-8ece-bcde77", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM0ODdkNDU3ZjkwNjJkNzg3YTNlNmNlMWM0NjY0YmY3NDAyZWM2N2RkMTExMjU2ZjE5YjM4Y2U0ZjY3MCJ9fX0=\"}]");
            case MiniMelon:
                return createSkull("Mini Melon", "f092a8f6-ffe6-4d98-801a-b48716","textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQxNDEyYjhjNmZkNTdlNGUxNjIxNjZkZGZkNzRiMTQ4YTU5NmY5ZWIxZDE1OTNjMDQ2OTYzOGM4ZDcxNCJ9fX0=\"}]");
            case Sushi:
                return createSkull("Sushi", "d2e5a02a-a89f-41fc-a0e6-c340ec", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUxMmYyNjc5NTNlNzZhZTY2YThkZDAyNWEzMjg2YWVjYmM2NGI0YWQ5OGVlYjEwYjNjNjdhNjlhYWUxNSJ9fX0=\"}]");
            case Burger:
                return createSkull("Burger", "cd02b2ac-24de-4da3-b66d-8405fa", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0=\"}]");
            case MiniCactus:
                return createSkull("Mini Cactus", "c1f3eebc-85de-4ad6-9cb3-3a29e3730e3f", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmY1ODViNDFjYTVhMWI0YWMyNmY1NTY3NjBlZDExMzA3Yzk0ZjhmOGExYWRlNjE1YmQxMmNlMDc0ZjQ3OTMifX19\"}]");
            case Cookie:
                return createSkull("Cookie", "e0982397-09b4-4670-a6a1-db670f", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU5MmNmOWY0MmE1YThjOTk1OTY4NDkzZmRkMWIxMWUwYjY5YWFkNjQ3M2ZmNDUzODRhYmU1OGI3ZmM3YzcifX19\"}]");
            case MiniGravel:
                return createSkull("Mini Gravel", "37a25b47-c8e0-4db1-9c75-37e4b78e1a88", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc4OGI2MWZjZDQ2Y2FkOTIzNWNjMzJhYzU3YTU0ZjcwODExMDMyOTQ0N2JiMjRjMDg3Nzg4NmUzZjQ5MDdmMSJ9fX0=\"}]");
            case MiniLeaves:
                return createSkull("Mini Leaves", "247addd8-f0d8-440c-8219-0797966ae1d5", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc5NWVkZWViNmI3ZWQ0MWMyNjhjZWZlYWZiZTk2MGI3YzQ5NTUwZGFlYjYzMWI1NjE1NmJmNWZlYjk4NDcifX19\"}]");
            case MiniOakLog:
                return createSkull("Mini Oak Log", "1f77726e-867b-4a66-8015-1ed701753de0", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQyZTMxMDg3OWE2NDUwYWY1NjI1YmNkNDUwOTNkZDdlNWQ4ZjgyN2NjYmZlYWM2OWM4MTUzNzc2ODQwNmIifX19\"}]");
            case Coconut:
                return createSkull("Coconut", "961e817d-5384-4098-b43b-5ec6bece7b12", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJjNjJmZDhlNDc0ZDA5OTQwNjA0ZjgyNzEyYTQ0YWJiMjQ5ZDYzYWZmODdmOTk4Mzc0Y2E4NDlhYjE3NDEyIn19fQ==\"}]");
            case MiniPumpkin:
                return createSkull("Mini Pumpkin", "9650240d-aa92-4ceb-9cfa-676f0c15a8f8", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdkN2FkMmRjYjU3ZGZhOWYwMjNkYmI5OWI2OThmYzUzMDc1YzNlOWQ2NTQ1MDYxMzlhNjQ3YWM5MDdmZGRjNSJ9fX0=\"}]");
            case SnowmanHead:
                return createSkull("Snowman Head", "d32431d0-eed2-4b6c-b53c-e6d2dd231f74", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU4ZDIwNmY2MWU2ZGU4YTc5ZDBjYjBiY2Q5OGFjZWQ0NjRjYmZlZmM5MjFiNDE2MGEyNTI4MjE2MzExMmEifX19\"}]");
            case Apple:
                return createSkull("Apple", "6679814b-8e75-46e2-93de-3b1b90e8fe54", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdiMTVmYzBmODk0NTNiYjljODY5ZGJjNDdhNjZjZjJlNzVlOGQzNzEzYjVmYmY3Yzg0Y2JmMmM2MzIxOTYifX19\"}]");
            case Blueberry:
                return createSkull("Blueberry", "5a878583-fa32-4a44-91ba-e2bed03eef7c", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFlOGUzNTJiM2ZiYWRjZTk5OGYxNTM1OTVkODM0MGRlZDI4NWMwNWFmNGFmNTdjNDQ1MWU4MjE0ZTFhZmIyIn19fQ==\"}]");
            case MiniCobblestone:
                return createSkull("Mini Cobblestone", "b966f5be-50de-4813-8233-472103", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk1NTM0ZTAyYzU5YjMzZWNlNTYxOTI4MDMzMTk3OTc3N2UwMjVmYTVmYTgxYWU3NWU5OWZkOGVmZGViYjgifX19\"}]");
            case ChickenDinner:
                return createSkull("Chicken Dinner", "bf871b6c-92c7-454c-aa05-174e6cf98c45", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjA2NTU1NzA2YjY0MWZkYWY0MzZjMDc2NjNmOTIzYWZjNWVlNzIxNDZmOTAxOTVmYjMzN2I5ZGU3NjY1ODhkIn19fQ==\"}]");
            case MiniRedstoneBlock:
                return createSkull("Mini Redstone Block", "4feade5d-26ec-4398-a0d8-537e6d", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA4ZWU2ZWRmYTk4ZGI1ZWFlOWI5Yzk5MzZlOTQ0ODliMmQ0YmJiZDNkMmI0YjZiNDg4NWEzMjI0MDYxM2MifX19\"}]");
            case OakCharacterExclamation:
                return createSkull( "Exclamation", "165c8c0f-d1ba-4c9e-9836-cbff5b", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE1M2JkZDE1NDU1MzFjOWViYjljNmY4OTViYzU3NjAxMmY2MTgyMGU2ZjQ4OTg4NTk4OGE3ZTg3MDlhM2Y0OCJ9fX0=\"}]");
            case OakCharacterQuestion:
                return createSkull("Question", "210665a1-0f17-4353-b85a-426e2c", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTE2M2RhZmFjMWQ5MWE4YzkxZGI1NzZjYWFjNzg0MzM2NzkxYTZlMThkOGY3ZjYyNzc4ZmM0N2JmMTQ2YjYifX19\"}]");
            case OakCharacterLeftArrow:
                return createSkull("Left Arrow", "69b9a08d-4e89-4878-8be8-551cae", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ViZjkwNzQ5NGE5MzVlOTU1YmZjYWRhYjgxYmVhZmI5MGZiOWJlNDljNzAyNmJhOTdkNzk4ZDVmMWEyMyJ9fX0=\"}]");
            case OakCharacterRightArrow:
                return createSkull("Right Arrow", "15f49744-9b61-46af-b1c3-71c626", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI2ZjFhMjViNmJjMTk5OTQ2NDcyYWVkYjM3MDUyMjU4NGZmNmY0ZTgzMjIxZTU5NDZiZDJlNDFiNWNhMTNiIn19fQ==\"}]");
            case OakCharacterUpArrow:
                return createSkull("Up Arrow", "ff1654b0-10f2-48b6-9c05-483b75", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ4Yjc2OGM2MjM0MzJkZmIyNTlmYjNjMzk3OGU5OGRlYzExMWY3OWRiZDZjZDg4ZjIxMTU1Mzc0YjcwYjNjIn19fQ==\"}]");
            case OakCharacterDownArrow:
                return createSkull("Down Arrow", "9afa272b-ca4a-4502-8073-c4be1b", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmRhZGQ3NTVkMDg1MzczNTJiZjdhOTNlM2JiN2RkNGQ3MzMxMjFkMzlmMmZiNjcwNzNjZDQ3MWY1NjExOTRkZCJ9fX0=\"}]");


            case Dice:
                return createSkull("Dice", "9b0146a5-2305-4675-ba3e-e5a3524ebb69", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzgzMTEzOGMyMDYxMWQzMDJjNDIzZmEzMjM3MWE3NDNkMTc0MzdhMTg5NzNjMzUxOTczNDQ3MGE3YWJiNCJ9fX0=\"}]");
            case TV: //Black model
                return createSkull("TV", "df045cc0-7ec9-4cbc-8219-2ea32e", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQxZTk5NzkyODlmMDMwOTlhN2M1ODdkNTJkNDg4ZTI2ZTdiYjE3YWI1OTRiNjlmOTI0MzhkNzdlYWJjIn19fQ==\"}]");
            case Clock: //Cyan model
                return createSkull("Clock", "f952235c-44a2-4f91-a6aa-0fcf36", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmRlNGUyNzgzZjg1YTM5MTIyMWRkOTE2NTZiYTY4OGU3ZTQyZDE2ZjZhYmJmYmNmYWQ5Y2E1MzYxN2ZjYTYifX19\"}]");
            case WoodenClock:
                return createSkull("Wooden Clock", "b4f1661d-8f18-470f-a5e9-65c0c9e05388", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ3N2RhZmM4YzllYTA3OTk2MzMzODE3OTM4NjZkMTQ2YzlhMzlmYWQ0YzY2ODRlNzExN2Q5N2U5YjZjMyJ9fX0=\"}]");
            case Aquarium:
                return createSkull("Aquarium", "89abda82-5116-4a40-88c1-c98602", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzI4NDdjZDU3MTdlNWY1YTY0ZTFiYTljYjQ4MWRjOWUyMmM3OGNhMjNmODUxNmQ1NTNmNTU0MTJmYTExM2UwIn19fQ==\"}]");
            case Toaster: //Steel
                return createSkull("Toaster", "eed7f9fd-3174-45a3-83dc-cff99e", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmI1M2U4ZDM3NGI0ZjZmNTczZDEyODY2ODFiZjg0MTA1NWI4OWE0NjJmN2NkZDk5ZThlNjNkMmY1MTRlNDUifX19\"}]");
            case Lantern: //Wood
                return createSkull("Lantern", "8c0662db-972a-4039-a360-3dc1a9", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2NjMjE3YTliOWUzY2UzY2QwNDg0YzdlOGNlNDlkMWNmNzQxMjgxYmRkYTVhNGQ2Y2I4MjFmMzc4NzUyNzE4In19fQ==\"}]");
            case Football:
                return createSkull("Football", "b74e58ec-4460-44b9-b0ca-2a1b9f", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU0YTcwYjdiYmNkN2E4YzMyMmQ1MjI1MjA0OTFhMjdlYTZiODNkNjBlY2Y5NjFkMmI0ZWZiYmY5ZjYwNWQifX19\"}]");
            case Present:
                return createSkull("Present", "dca29a3a-76d3-4979-88a2-2da034b99212", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlZjlhYTE0ZTg4NDc3M2VhYzEzNGE0ZWU4OTcyMDYzZjQ2NmRlNjc4MzYzY2Y3YjFhMjFhODViNyJ9fX0=\"}]");
            case PottedPoppy:
                return createSkull("Potted Poppy", "2d8ef167-5297-4aa4-adf5-5d1d36", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWRiYTM4ZTlmYzY3ZjcyYzQ1OGZkYWM4ZWNkN2NhYmFlZDNlYjgzNzM3MTQzYTAxMjgzNTBhMWFiMzgxZTNlIn19fQ==\"}]");
            case PottedDandelion:
                return createSkull("Potted Dandelion", "e0234bf4-a604-4c14-a2cb-342cb8", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjNmZmZjODk1NWIwZTgzMDI4OThmN2YwMTVkODQ5ZjBhMDFkYmJiMDQyNzQxNzUwNmZiODllYWQ1NGQ0NWY2In19fQ==\"}]");
            case PottedDaisy:
                return createSkull("Potted Daisy", "e5fdfdc7-f8a4-4a77-8d7c-17d98c", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWRjNGMxMmJmMjYxOWNiZmM4ZjIyZGM2MmMwMjJjZTE1MTI2Y2VhM2UyMTJjMjhkOWY5NmVhMzEwYWM0YzQyIn19fQ==\"}]");
            case Chimney:
                return createSkull("Chimney", "b2512ca2-7b0d-45f5-86c2-379d41da9e6b", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGEyOWVlZjljYTM2OWVhZGQ5ODlhNjgzMmY1MDk3MGY4YjA3NGY5ZDcwNjU5NGUzNzhhOGI4ODRhZjRiZDllIn19fQ==\"}]");
            case CookieJar:
                return createSkull("Cookie Jar", "1313137c-8b5e-413b-8c22-0a7878a74b86", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQ3NjY5NWRmY2MxNzBiZDc0ZWFkNGNiYWYxNzVlMTM3ODdjYmRmN2YzMmYwOGZmY2FhZmY2Mzg1OGUzYzEyMCJ9fX0=\"}]");
            case CardboardBox:
                return createSkull("Cardboard Box", "ab92d0bd-2e1f-4783-821e-00d97b86d93a", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWYyNTg0OWU4Y2Q1ZTUzMmMzMGNiYThkZThiZWQwNjQwY2ZiMmVlYzUwOTI5OTk4YjEwNzgzOWJmYjBmMjRkNyJ9fX0=\"}]");

            case Slimeball:
                return createSkull("Slimeball", "d9316bb7-755b-4141-a4c9-c6958d9047f6", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDkzNGE5ZjVhYjE3ODlhN2Q4ZGQ5NmQzMjQ5M2NkYWNmZjU3N2Q4YzgxZTdiMjM5MTdkZmYyZTMyYmQwYmMxMCJ9fX0=\"}]");
            case ToiletPaper:
                return createSkull("Toilet Paper", "315f66b8-a234-4796-af12-c38ff6f765ee", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM4YzIyOTg1YjAzMzZmODE3ZmMzMDdjYmE2YzE1ZGE0ODRiZjIzNjFjNjBmNWEzM2U2YTk1NTdlZTJkIn19fQ==\"}]");
            case Potion:
                return createSkull("Potion", "6319cfce-4956-4329-8e54-1f786f710018", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFmODE1MWRlYjM5NTUzMjkyYjlhNjk2NzQ4MzJkZGE4MDAxNzgwY2E5ODE4MGEwNmVhZjliMzY4OTMwYjViYiJ9fX0=\"}]");
            case InkJar:
                return createSkull("Ink Jar", "5edbc35c-86ac-4c9b-aaed-ad2e89ef037a", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTAyOGQ0NzRjNGEzMzJkY2Y4ZTM0Mzk1N2NlMGNhNGVlYmFjZTI1OGMzN2Y2ZDkwNGNmMjI4ZDM0NTY3ZjU1MSJ9fX0=\"}]");
            case Newspapers:
                return createSkull("Newspapers", "70d13334-cd01-4cc4-ac47-ccfddb027de3", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODNkODc0ZWI4YzRjNjk3YjNmODMyYmQ4NzQ0MjZmZGY2ZDIxYmFlMzM5ZjMxNzExMDgxZmRlNTk4MzgzODZlMSJ9fX0=\"}]");
            case FruitBasket:
                return createSkull("Fruit Basket", "0d139359-480c-41ca-b3c4-45dde2814025", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU3ZTJjNTc1NmQ0ZjQ0ZDM4MzQ1ODI2NjNlNmQ4NDhlMTE2NDE2MzRjYjIwNWI3Y2EzZWE0YWQyODU3MzQxIn19fQ==\"}]");
            case WoodenCrate:
                return createSkull("Wooden Crate", "4b0e454c-c36a-4fa1-aae6-4171704738d9", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQ3NjFjYzE2NTYyYzg4ZDJmYmU0MGFkMzg1MDJiYzNiNGE4Nzg1OTg4N2RiYzM1ZjI3MmUzMGQ4MDcwZWVlYyJ9fX0=\"}]");


            case Chicken:
                return createSkull("Chicken", "7d3a8ace-e045-4eba-ab71-71dbf525daf1", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYzODQ2OWE1OTljZWVmNzIwNzUzNzYwMzI0OGE5YWIxMWZmNTkxZmQzNzhiZWE0NzM1YjM0NmE3ZmFlODkzIn19fQ==\"}]");
            case Cow:
                return createSkull("Cow", "97ddf3b3-9dbe-4a3b-8a0f-1b19ddeac0bd", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ2YzZlZGE5NDJmN2Y1ZjcxYzMxNjFjNzMwNmY0YWVkMzA3ZDgyODk1ZjlkMmIwN2FiNDUyNTcxOGVkYzUifX19\"}]");
            case Fox:
                return createSkull("Fox", "237a2651-7da8-457a-aaea-3714bcc196a2", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg5NTRhNDJlNjllMDg4MWFlNmQyNGQ0MjgxNDU5YzE0NGEwZDVhOTY4YWVkMzVkNmQzZDczYTNjNjVkMjZhIn19fQ==\"}]");
            case Mooshroom:
                return createSkull("Mooshroom", "e206ac29-ae69-475b-909a-fb523d894336", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDBiYzYxYjk3NTdhN2I4M2UwM2NkMjUwN2EyMTU3OTEzYzJjZjAxNmU3YzA5NmE0ZDZjZjFmZTFiOGRiIn19fQ==\"}]");
            case Ocelot:
                return createSkull("Ocelot", "664dd492-3fcd-443b-9e61-4c7ebd9e4e10", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTY1N2NkNWMyOTg5ZmY5NzU3MGZlYzRkZGNkYzY5MjZhNjhhMzM5MzI1MGMxYmUxZjBiMTE0YTFkYjEifX19\"}]");
            case Pig:
                return createSkull("Pig", "e1e1c2e4-1ed2-473d-bde2-3ec718535399", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIxNjY4ZWY3Y2I3OWRkOWMyMmNlM2QxZjNmNGNiNmUyNTU5ODkzYjZkZjRhNDY5NTE0ZTY2N2MxNmFhNCJ9fX0=\"}]");
            case Sheep:
                return createSkull("Sheep", "fa234925-9dbe-4b8f-a544-7c70fb6b6ac5", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMxZjljY2M2YjNlMzJlY2YxM2I4YTExYWMyOWNkMzNkMThjOTVmYzczZGI4YTY2YzVkNjU3Y2NiOGJlNzAifX19\"}]");
            case Squid:
                return createSkull("Squid", "f95d9504-ea2b-4b89-b2d0-d400654a7010", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDE0MzNiZTI0MjM2NmFmMTI2ZGE0MzRiODczNWRmMWViNWIzY2IyY2VkZTM5MTQ1OTc0ZTljNDgzNjA3YmFjIn19fQ==\"}]");
            case Villager:
                return createSkull("Villager", "0a9e8efb-9191-4c81-80f5-e27ca5433156", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIyZDhlNzUxYzhmMmZkNGM4OTQyYzQ0YmRiMmY1Y2E0ZDhhZThlNTc1ZWQzZWIzNGMxOGE4NmU5M2IifX19\"}]");
            case WanderingTrader:
                return createSkull("Wandering Trader", "943947ea-3e1a-4fdc-85e5-f538379f05e9", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzc5YTgyMjkwZDdhYmUxZWZhYWJiYzcwNzEwZmYyZWMwMmRkMzRhZGUzODZiYzAwYzkzMGM0NjFjZjkzMiJ9fX0=\"}]");


            case Spider:
                return createSkull("Spider", "8bdb71d0-4724-48b2-9344-e79480424798", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q1NDE1NDFkYWFmZjUwODk2Y2QyNThiZGJkZDRjZjgwYzNiYTgxNjczNTcyNjA3OGJmZTM5MzkyN2U1N2YxIn19fQ==\"}]");
            case CaveSpider:
                return createSkull("Cave Spider", "39173a7a-c957-4ec1-ac1a-43e5a64983df", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE2NDVkZmQ3N2QwOTkyMzEwN2IzNDk2ZTk0ZWViNWMzMDMyOWY5N2VmYzk2ZWQ3NmUyMjZlOTgyMjQifX19\"}]");
            case Enderman:
                return createSkull("Enderman", "0de98464-1274-4dd6-bba8-370efa5d41a8", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2E1OWJiMGE3YTMyOTY1YjNkOTBkOGVhZmE4OTlkMTgzNWY0MjQ1MDllYWRkNGU2YjcwOWFkYTUwYjljZiJ9fX0=\"}]");
            case ZombiePigman:
                return createSkull("Zombie Pigman", "6540c046-d6ea-4aff-9766-32a54ebe6958", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRlOWM2ZTk4NTgyZmZkOGZmOGZlYjMzMjJjZDE4NDljNDNmYjE2YjE1OGFiYjExY2E3YjQyZWRhNzc0M2ViIn19fQ==\"}]");
            case IronGolem:
                return createSkull("Iron Golem", "7cb6e9a5-994f-40d5-9bfc-4ba5d796d21e", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkwOTFkNzllYTBmNTllZjdlZjk0ZDdiYmE2ZTVmMTdmMmY3ZDQ1NzJjNDRmOTBmNzZjNDgxOWE3MTQifX19\"}]");
            case Dolphin:
                return createSkull("Dolphin", "8b7ccd6d-36de-47e0-8d5a-6f6799c6feb8", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5Njg4Yjk1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ==\"}]");


            case Blaze:
                return createSkull("Blaze", "7ceb88b2-7f5f-4399-abb9-7068251baa9d", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc4ZWYyZTRjZjJjNDFhMmQxNGJmZGU5Y2FmZjEwMjE5ZjViMWJmNWIzNWE0OWViNTFjNjQ2Nzg4MmNiNWYwIn19fQ==\"}]");
            case Creeper:
                return createSkull("Creeper", "eed2d903-ca32-4cc7-b33b-ca3bdbe18da4", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQyNTQ4MzhjMzNlYTIyN2ZmY2EyMjNkZGRhYWJmZTBiMDIxNWY3MGRhNjQ5ZTk0NDQ3N2Y0NDM3MGNhNjk1MiJ9fX0=\"}]");
            case Drowned:
                return createSkull("Drowned", "8c2ddebb-2b2e-4a0d-a4e4-601e58fcf2a6", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg0ZGY3OWM0OTEwNGIxOThjZGFkNmQ5OWZkMGQwYmNmMTUzMWM5MmQ0YWI2MjY5ZTQwYjdkM2NiYmI4ZTk4YyJ9fX0=\"}]");
            case ElderGuardian:
                return createSkull("Elder Guardian", "e56a8749-8a4a-40cc-9ded-3c90f8ae8c63", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM3OTc0ODJhMTRiZmNiODc3MjU3Y2IyY2ZmMWI2ZTZhOGI4NDEzMzM2ZmZiNGMyOWE2MTM5Mjc4YjQzNmIifX19\"}]");
            case Ghast:
                return createSkull("Ghast", "807f287f-6499-4e93-a887-0a298ab3091f", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI2YTcyMTM4ZDY5ZmJiZDJmZWEzZmEyNTFjYWJkODcxNTJlNGYxYzk3ZTVmOTg2YmY2ODU1NzFkYjNjYzAifX19\"}]");
            case Guardian:
                return createSkull("Guardian", "628f15d0-e39c-4fd9-9c4e-8c41d4f54b29", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTBiZjM0YTcxZTc3MTViNmJhNTJkNWRkMWJhZTVjYjg1Zjc3M2RjOWIwZDQ1N2I0YmZjNWY5ZGQzY2M3Yzk0In19fQ==\"}]");
            case Husk:
                return createSkull("Husk", "1abe147b-ea7a-470c-8e74-16ce8fed6cb6", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY3NGM2M2M4ZGI1ZjRjYTYyOGQ2OWEzYjFmOGEzNmUyOWQ4ZmQ3NzVlMWE2YmRiNmNhYmI0YmU0ZGIxMjEifX19\"}]");
            case MagmaCube:
                return createSkull("Magma Cube", "96aced64-5b85-4b99-b825-53cd7a", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzg5NTdkNTAyM2M5MzdjNGM0MWFhMjQxMmQ0MzQxMGJkYTIzY2Y3OWE5ZjZhYjM2Yjc2ZmVmMmQ3YzQyOSJ9fX0=\"}]");
            case Pillager:
                return createSkull("Pillager", "fef34161-8b8b-486e-9d63-8eb1faa7cc21", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk1NDEzNWRjODIyMTM5NzhkYjQ3ODc3OGFlMTIxMzU5MWI5M2QyMjhkMzZkZDU0ZjFlYTFkYTQ4ZTdjYmE2In19fQ==\"}]");
            case Skeleton:
                return createSkull("Skeleton", "99653361-7f17-4868-af3b-c95bc2743f5e", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzAxMjY4ZTljNDkyZGExZjBkODgyNzFjYjQ5MmE0YjMwMjM5NWY1MTVhN2JiZjc3ZjRhMjBiOTVmYzAyZWIyIn19fQ==\"}]");
            case Slime:
                return createSkull("Slime", "7f0b0873-df6a-4a19-9bcd-f6c90ef804c7", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODk1YWVlYzZiODQyYWRhODY2OWY4NDZkNjViYzQ5NzYyNTk3ODI0YWI5NDRmMjJmNDViZjNiYmI5NDFhYmU2YyJ9fX0=\"}]");
            case Stray:
                return createSkull("Stray", "644c9bad-958b-43ce-9d2f-199d85be607c", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhkZGY3NmU1NTVkZDVjNGFhOGEwYTVmYzU4NDUyMGNkNjNkNDg5YzI1M2RlOTY5ZjdmMjJmODVhOWEyZDU2In19fQ==\"}]");
            case Vex:
                return createSkull("Vex", "f6e25015-1a90-46eb-88b7-ce3f14bf00d4", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJlYzVhNTE2NjE3ZmYxNTczY2QyZjlkNWYzOTY5ZjU2ZDU1NzVjNGZmNGVmZWZhYmQyYTE4ZGM3YWI5OGNkIn19fQ==\"}]");
            case Witch:
                return createSkull("Witch", "7f92b3d6-5ee0-4ab6-afae-2206b9514a63", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBlMTNkMTg0NzRmYzk0ZWQ1NWFlYjcwNjk1NjZlNDY4N2Q3NzNkYWMxNmY0YzNmODcyMmZjOTViZjlmMmRmYSJ9fX0=\"}]");
            case WitherSkeleton:
                return createSkull("Wither Skeleton", "9b6eb2a3-8c99-4e14-9162-58debe3f9262", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzk1M2I2YzY4NDQ4ZTdlNmI2YmY4ZmIyNzNkNzIwM2FjZDhlMWJlMTllODE0ODFlYWQ1MWY0NWRlNTlhOCJ9fX0=\"}]");
            case Zombie:
                return createSkull("Zombie", "9959dd98-efb3-4ee9-a8fb-2fda0218cda0", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ==\"}]");
            case ZombieVillager:
                return createSkull("Zombie Villager", "dd1b157c-d732-4d1c-8185-da205188a8cf", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTVlMDhhODc3NmMxNzY0YzNmZTZhNmRkZDQxMmRmY2I4N2Y0MTMzMWRhZDQ3OWFjOTZjMjFkZjRiZjNhYzg5YyJ9fX0=\"}]");


            case BeachBall:
                return createSkull("Beach Ball", "146d93d9-fb52-43c7-895c-f18eb24facaa", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWIyNTEzYzhkMDhjNjBhZDM3ODVkM2E5YTY1MWI3MzI5YzVmMjY5MzdhY2EyZmM4ZGZhZjM0NDFjOWJkOWRhMiJ9fX0=\"}]");
            case Sandcastle:
                return createSkull("Sandcastle", "6d744145-05e0-47d0-8f8f-c7edbcbc0131", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWE0YjdlODc4MjFkYjRlNmU0MmU3OGQ3ZjI2MGI2Mzc5OTNkNmJlOGRlOTRmNDNlNDMxNzViYWZmOTFmNSJ9fX0=\"}]");
            case PicnicBasket:
                return createSkull("Picnic Basket", "6e9086e2-f0f1-4f23-a82f-059dab5bd3bd", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEwMDMzMTg5MzQyMDUwNTZjNWRhYWEzOGI3YjY2ZDgxZmI0NTAwYWZlMGJkYzQ0NzNhNGY4NmQ1MDI1Y2UzIn19fQ==\"}]");
            case SandBucket:
                return createSkull("Sand Bucket", "71b586db-59ad-4134-a8b8-1785b8641ad3", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQ0MzdkMTY4ODU5MjBjZTU1NzczM2Y0YzcxMGJiNTAwN2YwYTRmMWEwZGRkMzJkNTk0NjlhYmM0ZjdlN2JlMCJ9fX0=\"}]");

            case PorcelainVase:
                return createSkull("Vase", "c03cbe5a-dc07-487a-b4b7-1e87fa30d389", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY0NTA1NWZhNjQ4Y2RiYjJjNjIwYmE1NjUwYjI5ZWMwM2MwMzAyNmJhNGZhODhhZmYwNTUyOTc0NzM3YjJiNyJ9fX0=\"}]");
            case GoldMedal:
                return createSkull("Gold Medal", "c38cafc5-b2f1-4cb3-a98a-2369b9a37cbc", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMxNzhkMDFhYWYxMWU1Y2NmYTM4YjE5MGQ3MDg1ZmYzN2RkMDQ4ZDA5YjY2NGYwMjU3MDc3NmY5Yjk4MTQ2NiJ9fX0=\"}]");
            case StevePlush:
                return createSkull("Steve Plush", "4b853c0b-d0cd-48fc-bc27-3ff98931ce2b", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjYyMjE3ZDkwMmVkYzkyOWRlNGY1MWI5MDcxZjU2YmFhYWMwNDVkMDRlYzA0Yjg0MGNjYzhlYzk5NjJmNjJlMyJ9fX0=\"}]");
        }
        return null;
    }

    private static ItemStack createSkull(String skullName, String id, String properties)
    {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        Bukkit.getUnsafe().modifyItemStack(skull, "{display:{Name:\"{\\\"text\\\":\\\""+skullName+"\\\"}\"},SkullOwner:{Id:\""+id+"\",Properties:{"+properties+"}}}");
        return skull;
    }
}
