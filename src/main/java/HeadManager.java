import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HeadManager
{
    public enum HeadType
    {
        Beer,
        Books,
        Snowman,
        Apple,
        Blueberry,
        Coconut,
        OakLog,
        Dirt,
        Leaves,
        Melon,
        Pumpkin,
        Cactus,
        Sushi,
        Bread,
        Cookie,
        Cake,
        Burger,
        ChickenDinner,
        RedstoneBlock,
        Gravel,
        Cobblestone,
        OakCharacterExclamation,
        OakCharacterQuestion,
        OakCharacterLeft,
        OakCharacterRight,
        OakCharacterUp,
        OakCharacterDown,
    }

    public static HeadType getHeadByName(String name)
    {
        for (HeadType head : HeadType.values())
        {
            if (head.name().equalsIgnoreCase(name))
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
            case Cake:
                return createSkull("Cupcake", "534c47c4-d04d-416a-bf99-c3efd6" , "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjkxMzY1MTRmMzQyZTdjNTIwOGExNDIyNTA2YTg2NjE1OGVmODRkMmIyNDkyMjAxMzllOGJmNjAzMmUxOTMifX19\"}]");
            case Dirt:
                return createSkull("Mini Dirt", "ca021f3f-5002-46b2-bf34-985779", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFiNDNiOGMzZDM0ZjEyNWU1YTNmOGI5MmNkNDNkZmQxNGM2MjQwMmMzMzI5ODQ2MWQ0ZDRkN2NlMmQzYWVhIn19fQ==\"}]");
            case Bread:
                return createSkull("Bread", "a75e3f60-2242-4429-8ece-bcde77", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM0ODdkNDU3ZjkwNjJkNzg3YTNlNmNlMWM0NjY0YmY3NDAyZWM2N2RkMTExMjU2ZjE5YjM4Y2U0ZjY3MCJ9fX0=\"}]");
            case Melon:
                return createSkull("Mini Melon", "f092a8f6-ffe6-4d98-801a-b48716","textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjQxNDEyYjhjNmZkNTdlNGUxNjIxNjZkZGZkNzRiMTQ4YTU5NmY5ZWIxZDE1OTNjMDQ2OTYzOGM4ZDcxNCJ9fX0=\"}]");
            case Sushi:
                return createSkull("Sushi", "d2e5a02a-a89f-41fc-a0e6-c340ec", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUxMmYyNjc5NTNlNzZhZTY2YThkZDAyNWEzMjg2YWVjYmM2NGI0YWQ5OGVlYjEwYjNjNjdhNjlhYWUxNSJ9fX0=\"}]");
            case Burger:
                return createSkull("Burger", "cd02b2ac-24de-4da3-b66d-8405fa", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RhZGYxNzQ0NDMzZTFjNzlkMWQ1OWQyNzc3ZDkzOWRlMTU5YTI0Y2Y1N2U4YTYxYzgyYmM0ZmUzNzc3NTUzYyJ9fX0=\"}]");
            case Cactus:
                return createSkull("Mini Cactus", "c1f3eebc-85de-4ad6-9cb3-3a29e3730e3f", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmY1ODViNDFjYTVhMWI0YWMyNmY1NTY3NjBlZDExMzA3Yzk0ZjhmOGExYWRlNjE1YmQxMmNlMDc0ZjQ3OTMifX19\"}]");
            case Cookie:
                return createSkull("Cookie", "e0982397-09b4-4670-a6a1-db670f", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU5MmNmOWY0MmE1YThjOTk1OTY4NDkzZmRkMWIxMWUwYjY5YWFkNjQ3M2ZmNDUzODRhYmU1OGI3ZmM3YzcifX19\"}]");
            case Gravel:
                return createSkull("Mini Gravel", "37a25b47-c8e0-4db1-9c75-37e4b78e1a88", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc4OGI2MWZjZDQ2Y2FkOTIzNWNjMzJhYzU3YTU0ZjcwODExMDMyOTQ0N2JiMjRjMDg3Nzg4NmUzZjQ5MDdmMSJ9fX0=\"}]");
            case Leaves:
                return createSkull("Mini Leaves", "247addd8-f0d8-440c-8219-0797966ae1d5", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc5NWVkZWViNmI3ZWQ0MWMyNjhjZWZlYWZiZTk2MGI3YzQ5NTUwZGFlYjYzMWI1NjE1NmJmNWZlYjk4NDcifX19\"}]");
            case OakLog:
                return createSkull("Mini Oak Log", "1f77726e-867b-4a66-8015-1ed701753de0", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQyZTMxMDg3OWE2NDUwYWY1NjI1YmNkNDUwOTNkZDdlNWQ4ZjgyN2NjYmZlYWM2OWM4MTUzNzc2ODQwNmIifX19\"}]");
            case Coconut:
                return createSkull("Coconut", "961e817d-5384-4098-b43b-5ec6bece7b12", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJjNjJmZDhlNDc0ZDA5OTQwNjA0ZjgyNzEyYTQ0YWJiMjQ5ZDYzYWZmODdmOTk4Mzc0Y2E4NDlhYjE3NDEyIn19fQ==\"}]");
            case Pumpkin:
                return createSkull("Mini Pumpkin", "9650240d-aa92-4ceb-9cfa-676f0c15a8f8", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdkN2FkMmRjYjU3ZGZhOWYwMjNkYmI5OWI2OThmYzUzMDc1YzNlOWQ2NTQ1MDYxMzlhNjQ3YWM5MDdmZGRjNSJ9fX0=\"}]");
            case Snowman:
                return createSkull("Snowman Head", "d32431d0-eed2-4b6c-b53c-e6d2dd231f74", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU4ZDIwNmY2MWU2ZGU4YTc5ZDBjYjBiY2Q5OGFjZWQ0NjRjYmZlZmM5MjFiNDE2MGEyNTI4MjE2MzExMmEifX19\"}]");
            case Apple:
                return createSkull("Apple", "6679814b-8e75-46e2-93de-3b1b90e8fe54", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdiMTVmYzBmODk0NTNiYjljODY5ZGJjNDdhNjZjZjJlNzVlOGQzNzEzYjVmYmY3Yzg0Y2JmMmM2MzIxOTYifX19\"}]");
            case Blueberry:
                return createSkull("Blueberry", "5a878583-fa32-4a44-91ba-e2bed03eef7c", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFlOGUzNTJiM2ZiYWRjZTk5OGYxNTM1OTVkODM0MGRlZDI4NWMwNWFmNGFmNTdjNDQ1MWU4MjE0ZTFhZmIyIn19fQ==\"}]");
            case Cobblestone:
                return createSkull("Mini Cobblestone", "b966f5be-50de-4813-8233-472103", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk1NTM0ZTAyYzU5YjMzZWNlNTYxOTI4MDMzMTk3OTc3N2UwMjVmYTVmYTgxYWU3NWU5OWZkOGVmZGViYjgifX19\"}]");
            case ChickenDinner:
                return createSkull("Chicken Dinner", "bf871b6c-92c7-454c-aa05-174e6cf98c45", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjA2NTU1NzA2YjY0MWZkYWY0MzZjMDc2NjNmOTIzYWZjNWVlNzIxNDZmOTAxOTVmYjMzN2I5ZGU3NjY1ODhkIn19fQ==\"}]");
            case RedstoneBlock:
                return createSkull("Mini Redstone Block", "4feade5d-26ec-4398-a0d8-537e6d", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA4ZWU2ZWRmYTk4ZGI1ZWFlOWI5Yzk5MzZlOTQ0ODliMmQ0YmJiZDNkMmI0YjZiNDg4NWEzMjI0MDYxM2MifX19\"}]");
            case OakCharacterExclamation:
                return createSkull( "Exclamation", "165c8c0f-d1ba-4c9e-9836-cbff5b", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmE1M2JkZDE1NDU1MzFjOWViYjljNmY4OTViYzU3NjAxMmY2MTgyMGU2ZjQ4OTg4NTk4OGE3ZTg3MDlhM2Y0OCJ9fX0=\"}]");
            case OakCharacterQuestion:
                return createSkull("Question", "210665a1-0f17-4353-b85a-426e2c", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTE2M2RhZmFjMWQ5MWE4YzkxZGI1NzZjYWFjNzg0MzM2NzkxYTZlMThkOGY3ZjYyNzc4ZmM0N2JmMTQ2YjYifX19\"}]");
            case OakCharacterLeft:
                return createSkull("Left Arrow", "69b9a08d-4e89-4878-8be8-551cae", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ViZjkwNzQ5NGE5MzVlOTU1YmZjYWRhYjgxYmVhZmI5MGZiOWJlNDljNzAyNmJhOTdkNzk4ZDVmMWEyMyJ9fX0=\"}]");
            case OakCharacterRight:
                return createSkull("Right Arrow", "15f49744-9b61-46af-b1c3-71c626", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI2ZjFhMjViNmJjMTk5OTQ2NDcyYWVkYjM3MDUyMjU4NGZmNmY0ZTgzMjIxZTU5NDZiZDJlNDFiNWNhMTNiIn19fQ==\"}]");
            case OakCharacterUp:
                return createSkull("Up Arrow", "ff1654b0-10f2-48b6-9c05-483b75", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ4Yjc2OGM2MjM0MzJkZmIyNTlmYjNjMzk3OGU5OGRlYzExMWY3OWRiZDZjZDg4ZjIxMTU1Mzc0YjcwYjNjIn19fQ==\"}]");
            case OakCharacterDown:
                return createSkull("Down Arrow", "9afa272b-ca4a-4502-8073-c4be1b", "textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmRhZGQ3NTVkMDg1MzczNTJiZjdhOTNlM2JiN2RkNGQ3MzMxMjFkMzlmMmZiNjcwNzNjZDQ3MWY1NjExOTRkZCJ9fX0=\"}]");

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
