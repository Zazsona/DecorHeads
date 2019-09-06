import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Settings
{
    public boolean Enabled = true;
    public boolean DropsEnabled = true;
    public boolean CraftingEnabled = true;

    public int BeerChance = 1;
    public int BooksChance = 10;
    public int SnowmanChance = 1;
    public int TreeFruitChance = 3;
    public int CoconutChance = 2;
    public int OakLogChance = 1;
    public int DirtChance = 1;
    public int LeavesChance = 1;
    public int MelonChance = 1;
    public int PumpkinChance = 2;
    public int CactusChance = 2;
    public int SushiChance = 5;
    public int BreadChance = 1;
    public int CookieChance = 1;
    public int CakeChance = 10;
    public int BurgerChance = 1;
    public int ChickenChance = 1;

    public Settings(File pluginFolder) throws IOException
    {
        File configFile = new File(pluginFolder.getPath()+"/config.yml");
        if (!configFile.exists())
        {
            save(pluginFolder, configFile);
        }
        else
        {
            load(configFile);
        }
    }

    public void load(File configFile) throws IOException
    {
        Scanner scanner = new Scanner(configFile);
        while (scanner.hasNextLine())
        {
            setValue(scanner.nextLine());
        }
        scanner.close();
    }

    public void save(File pluginFolder, File configFile) throws IOException
    {
        if (!configFile.exists())
        {
            pluginFolder.mkdirs();
            configFile.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(configFile, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
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

            }
        }
    }
}
