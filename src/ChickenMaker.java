import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class ChickenMaker
{
    ArrayList<String> toIgnore = new ArrayList();

    BufferedImage[] result = new BufferedImage[3];
    int l = 0;

    public ChickenMaker()
    {
        toIgnore.add("FFA37D38");
        toIgnore.add("FF695124");
        toIgnore.add("FFB40000");
        toIgnore.add("FF000000");
        toIgnore.add("FF7B7039");
        toIgnore.add("FFE0CC69");
    }

    public void makeChicken(String hexColor, String chickenName)
    {
        String lowerChickenName = chickenName.toLowerCase().replace(" ", "_");
        int color = Integer.parseInt(hexColor, 16);

        try
        {
            editChicken(color, ImageIO.read(new File("chicken_block.png")), "resources/roost/textures/blocks/chicken/" + lowerChickenName + "_chicken.png");
            editChicken(color, ImageIO.read(new File("chicken_item.png")), "resources/roost/textures/items/chicken/" + lowerChickenName + "_chicken.png");
            editChicken(color, ImageIO.read(new File("chicken_entity.png")), "resources/contenttweaker/textures/entities/" + lowerChickenName + "_chicken.png");
            System.out.println("Made Chickens");
            editScript("scripts/contenttweaker/chickens.zs", lowerChickenName, hexColor, chickenName);
            editLang("resources/contenttweaker/lang/en_us.lang", lowerChickenName, chickenName);
            System.out.println("Edited Script and Lang");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void editLang(String loadfile, String lowerName, String upperName)
    {
        String langString = "entity." + lowerName + "_chicken.name=" + upperName + " Chicken\r\n";
        try
        {
            Files.write(Paths.get(loadfile), langString.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void editScript(String loadfile, String name, String hexColor, String extName)
    {
        String creationString = "//" + extName + "\r\nvar " + name + " = ChickenFactory.createChicken(\"" + name + "_chicken\", Color.fromInt(0x" + hexColor + "), <item:minecraft:bedrock>);\r\n" + name + ".setForegroundColor(Color.fromInt(0x" + hexColor + "));\r\n" + name + ".register();\r\n \r\n";
        try
        {
            Files.write(Paths.get(loadfile), creationString.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void editChicken(int colour, BufferedImage chicken, String filename)
    {
        try
        {
            result[l] = new BufferedImage(chicken.getWidth(), chicken.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < chicken.getHeight(); y++)
            {
                for (int x = 0; x < chicken.getWidth(); x++)
                {
                    Color clr = new Color(chicken.getRGB(x, y), true);
                    if (!toIgnore.contains(Integer.toHexString(clr.getRGB()).toUpperCase()))
                    {
                        int red = clr.getRed();
                        int green = clr.getGreen();
                        int blue = clr.getBlue();

                        int redDiff = 255 - red;
                        int greenDiff = 255 - green;
                        int blueDiff = 255 - blue;

                        int colorRed = (colour & 0x00ff0000) >> 16;
                        int colorGreen = (colour & 0x0000ff00) >> 8;
                        int colorBlue = colour & 0x000000ff;

                        int newRed = Math.max(colorRed - redDiff, 0);
                        int newGreen = Math.max(colorGreen - greenDiff, 0);
                        int newBlue = Math.max(colorBlue - blueDiff, 0);

                        result[l].setRGB(x, y, new Color(newRed, newGreen, newBlue, clr.getAlpha()).getRGB());
                    } else
                    {
                        result[l].setRGB(x, y, clr.getRGB());
                    }
                }
            }
            ImageIO.write(result[l], "png", new File(filename));
            l++;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
