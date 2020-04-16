import javax.imageio.ImageIO;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class ChickenCreator extends Applet implements ActionListener
{
    ArrayList<String> toIgnore = new ArrayList();
    TextField hexValue1;
    TextField chickenText;

    BufferedImage[] result = new BufferedImage[3];
    int l = 0;

    public void init()
    {
        this.setSize(800, 400);
        toIgnore.add("FFA37D38");
        toIgnore.add("FF695124");
        toIgnore.add("FFB40000");
        toIgnore.add("FF000000");
        toIgnore.add("FF7B7039");
        toIgnore.add("FFE0CC69");

        this.setName("Chicken Creator");
        Label chickenType = new Label("Chicken Resource Type:");
        chickenText = new TextField(20);
        Label hex1 = new Label("Hex Value 1:");
        hexValue1 = new TextField(6);
        Button create = new Button("Create");

        this.add(chickenType);
        this.add(chickenText);
        this.add(hex1);
        this.add(hexValue1);
        this.add(create);

        create.addActionListener(this);
    }

    public void editChicken(int colour, URL chickenPath, String filename)
    {
        try
        {
            BufferedImage chicken = ImageIO.read(chickenPath);
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
            ImageIO.write(result[l], "png", new File(this.getClass().getResource("").getPath() + "\\" + filename));
            l++;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        repaint();
    }

    public void actionPerformed(ActionEvent e)
    {
        editChicken(Integer.parseInt(hexValue1.getText(), 16), this.getClass().getResource("chicken_block.png"), chickenText.getText() + "_chicken_block.png");
        editChicken(Integer.parseInt(hexValue1.getText(), 16), this.getClass().getResource("chicken_entity.png"), chickenText.getText() + "_chicken_entity.png");
        editChicken(Integer.parseInt(hexValue1.getText(), 16), this.getClass().getResource("chicken_item.png"), chickenText.getText() + "_chicken_item.png");
    }

    public void paint(Graphics g)
    {
        if (result[0] != null)
        {
            g.drawImage(result[0], 10, 100, result[0].getWidth() * 10, result[0].getHeight() * 10, null);
            g.drawImage(result[1], 200, 100, result[1].getWidth() * 10, result[1].getHeight() * 10, null);
            g.drawImage(result[2], 550, 100, result[2].getWidth() * 10, result[2].getHeight() * 10, null);
            l = 0;
        }
    }
}
