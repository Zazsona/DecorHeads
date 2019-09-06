import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Settings
{
    private transient File pluginFolder;
    private transient File configFile;

    public String Version = "1.0";

    public boolean Enabled = true;
    public boolean DropsEnabled = true;
    public boolean CraftingEnabled = true;

    public int BeerChance = 5;
    public int BooksChance = 10;
    public int SnowmanChance = 1;
    public int TreeFruitChance = 3;
    public int CoconutChance = 3;
    public int OakLogChance = 2;
    public int DirtChance = 1;
    public int LeavesChance = 2;
    public int MelonChance = 2;
    public int PumpkinChance = 2;
    public int CactusChance = 2;
    public int SushiChance = 8;
    public int BreadChance = 1;
    public int CookieChance = 3;
    public int CakeChance = 15;
    public int BurgerChance = 1;
    public int ChickenChance = 1;
    public int RedstoneBlockChance = 3;
    public int GravelChance = 2;
    public int CobblestoneChance = 2;
    public int OakCharactersChance = 4;

    public Settings(File pluginFolder) throws IOException
    {
        this.pluginFolder = pluginFolder;
        configFile = new File(pluginFolder.getPath()+"/config.yml");

        if (!configFile.exists())
        {
            save();
        }
        else
        {
            load();
            if (!getConfigVersion().equals(Version))
            {
                save();
            }
        }
    }

    public void load() throws IOException
    {
        Scanner scanner = new Scanner(configFile);
        while (scanner.hasNextLine())
        {
            setValue(scanner.nextLine());
        }
        scanner.close();
    }

    public void save() throws IOException
    {
        if (!configFile.exists())
        {
            pluginFolder.mkdirs();
            configFile.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(configFile, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("Version: "+Version);
        printWriter.println("Enabled: "+Enabled);
        printWriter.println("DropsEnabled: "+DropsEnabled);
        printWriter.println("CraftingEnabled: "+CraftingEnabled);
        printWriter.println("//% chance of items dropping");
        printWriter.println("BeerChance: "+BeerChance);
        printWriter.println("BooksChance: "+BooksChance);
        printWriter.println("SnowmanChance: "+SnowmanChance);
        printWriter.println("TreeFruitChance: "+TreeFruitChance);
        printWriter.println("CoconutChance: "+CoconutChance);
        printWriter.println("OakLogChance: "+OakLogChance);
        printWriter.println("DirtChance: "+DirtChance);
        printWriter.println("LeavesChance: "+ LeavesChance);
        printWriter.println("MelonChance: "+MelonChance);
        printWriter.println("PumpkinChance: "+PumpkinChance);
        printWriter.println("CactusChance: "+CactusChance);
        printWriter.println("SushiChance: "+SushiChance);
        printWriter.println("BreadChance: "+BreadChance);
        printWriter.println("CookieChance: "+CookieChance);
        printWriter.println("CakeChance: "+CakeChance);
        printWriter.println("BurgerChance: "+BurgerChance);
        printWriter.println("ChickenChance: "+ChickenChance);
        printWriter.println("RedstoneBlockChance: "+RedstoneBlockChance);
        printWriter.println("GravelChance: "+GravelChance);
        printWriter.println("CobblestoneChance: "+CobblestoneChance);
        printWriter.println("OakCharactersChance: "+OakCharactersChance);
        printWriter.close();
        fileWriter.close();
    }

    private void setValue(String configLine)
    {
        if (!configLine.startsWith("//"))
        {
            String key = configLine.substring(0, configLine.indexOf(":")).trim();
            String value = configLine.substring(configLine.indexOf(":")+1).trim();
            switch (key)
            {
                case "enabled":
                    Enabled = Boolean.parseBoolean(value);
                    break;
                case "dropsEnabled":
                    DropsEnabled = Boolean.parseBoolean(value);
                    break;
                case "craftingEnabled":
                    CraftingEnabled = Boolean.parseBoolean(value);
                    break;
                case "BeerChance":
                    BeerChance = Integer.parseInt(value);
                    break;
                case "BooksChance":
                    BooksChance = Integer.parseInt(value);
                    break;
                case "SnowmanChance":
                    SnowmanChance = Integer.parseInt(value);
                    break;
                case "TreeFruitChance":
                    TreeFruitChance = Integer.parseInt(value);
                    break;
                case "CoconutChance":
                    CoconutChance = Integer.parseInt(value);
                    break;
                case "OakLogChance":
                    OakLogChance = Integer.parseInt(value);
                    break;
                case "DirtChance":
                    DirtChance = Integer.parseInt(value);
                    break;
                case "LeavesChance":
                    LeavesChance = Integer.parseInt(value);
                    break;
                case "MelonChance":
                    MelonChance = Integer.parseInt(value);
                    break;
                case "PumpkinChance":
                    PumpkinChance = Integer.parseInt(value);
                    break;
                case "CactusChance":
                    CactusChance = Integer.parseInt(value);
                    break;
                case "SushiChance":
                    SushiChance = Integer.parseInt(value);
                    break;
                case "BreadChance":
                    BreadChance = Integer.parseInt(value);
                    break;
                case "CookieChance":
                    CookieChance = Integer.parseInt(value);
                    break;
                case "CakeChance":
                    CakeChance = Integer.parseInt(value);
                    break;
                case "BurgerChance":
                    BurgerChance = Integer.parseInt(value);
                    break;
                case "ChickenChance":
                    ChickenChance = Integer.parseInt(value);
                    break;
                case "RedstoneBlockChance":
                    RedstoneBlockChance = Integer.parseInt(value);
                    break;
                case "GravelChance":
                    GravelChance = Integer.parseInt(value);
                    break;
                case "CobblestoneChance":
                    CobblestoneChance = Integer.parseInt(value);
                    break;
                case "OakCharactersChance":
                    OakCharactersChance = Integer.parseInt(value);
                    break;

            }
        }
    }

    private String getConfigVersion() throws IOException
    {
        Scanner scanner = new Scanner(configFile);
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            if (line.startsWith("Version"))
            {
                return line.substring(line.indexOf(":")+1).trim();
            }
        }
        scanner.close();
        return "";
    }
}
