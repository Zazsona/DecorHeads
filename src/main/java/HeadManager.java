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
        OakCharacterDown
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
                return createSkull("Thanauser", "Beer");
            case Books:
                return createSkull("GoodBook1", "Books");
            case Cake:
                createSkull("MHF_Cake", "Cupcake");
            case Dirt:
                return createSkull("Zyne", "Mini Dirt");
            case Bread:
                return createSkull("BedHeadBread", "Bread");
            case Melon:
                return createSkull("MHF_Melon", "Mini Melon");
            case Sushi:
                return createSkull("lmaoki", "Sushi");
            case Burger:
                return createSkull("Pesquiburguer", "Burger");
            case Cactus:
                return createSkull("MHF_Cactus", "Mini Cactus");
            case Cookie:
                return createSkull("QuadratCookie", "Cookie");
            case Gravel:
                createSkull("MushTurf", "Mini Gravel");
            case Leaves:
                return createSkull("Plant", "Mini Leaves");
            case OakLog:
                return createSkull("Log", "Mini Oak Log");
            case Coconut:
                return createSkull("MHF_CoconutB", "Coconut");
            case Pumpkin:
                createSkull("MHF_Pumpkin", "Mini Pumpkin");
            case Snowman:
                return createSkull("Snowman_7", "Snowman Head");
            case Apple:
                return createSkull("MHF_Apple", "Apple");
            case Blueberry:
                return createSkull("TheReapHorn", "Blueberry");
            case Cobblestone:
                return createSkull("JuanFco", "Mini Cobblestone");
            case ChickenDinner:
                return createSkull("Ernie77", "Chicken Dinner");
            case RedstoneBlock:
                createSkull("TheNewTsar", "Mini Redstone Block");
            case OakCharacterExclamation:
                return createSkull("MHF_Exclamation", "Exclamation");
            case OakCharacterQuestion:
                return createSkull("MHF_Question", "Question");
            case OakCharacterLeft:
                return createSkull("MHF_ArrowLeft", "Left Arrow");
            case OakCharacterRight:
                return createSkull("MHF_ArrowRight", "Right Arrow");
            case OakCharacterUp:
                return createSkull("MHF_ArrowUp", "Up Arrow");
            case OakCharacterDown:
                return createSkull("MHF_ArrowDown", "Down Arrow");
        }
        return null;
    }

    private static ItemStack createSkull(String playerName, String skullName)
    {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        Bukkit.getUnsafe().modifyItemStack(skull, "{display:{Name:\"{\\\"text\\\":\\\""+skullName+"\\\"}\"},SkullOwner:\""+playerName+"\"}");
        return skull;
    }
}
