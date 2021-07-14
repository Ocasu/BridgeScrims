package image;

import org.w3c.dom.css.RGBColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class test {

    public static void main(String[] args) throws IOException {

        int[] semiY = {90, 200, 330, 440, 570, 680, 820, 925};
        int[] semiY2 = {140, 380, 630, 865};
        int[] semiY3 = {270, 750};
        int last = 510;

        reset.re();
        File h = new File("src/main/java/image/bbracketrevamp2.png");

        BufferedImage image = ImageIO.read(h);
        System.out.println(h.getAbsolutePath());

        String txt = "16_charactername";
        txt = txt.substring(0, Math.min(16, txt.length()));

        Font font = new Font("Minecraft", Font.PLAIN, 42);

        Graphics g = image.getGraphics();


        g.setFont(font);
        g.setColor(Color.BLACK);
        for(int j : semiY){
            g.drawString(txt, 65, j-3);
            g.drawString(txt, 65, j+3);
            g.drawString(txt, 71, j-3);
            g.drawString(txt, 71, j+3);
        }

        for(int j : semiY2){
            g.drawString(txt, 470, j-3);
            g.drawString(txt, 470, j+3);
            g.drawString(txt, 476, j-3);
            g.drawString(txt, 476, j+3);
        }
        for(int j : semiY3){
            g.drawString(txt, 870, j-3);
            g.drawString(txt, 870, j+3);
            g.drawString(txt, 876, j-3);
            g.drawString(txt, 876, j+3);
        }
        g.drawString(txt, 1420, last-3);
        g.drawString(txt, 1420, last+3);
        g.drawString(txt, 1426, last-3);
        g.drawString(txt, 1426, last+3);
//        for(int j : semiY) g.fillRect(65, j-35, 390, 40);

        g.setColor(Color.WHITE);

        for(int j : semiY) g.drawString(txt, 68, j);

        for(int j : semiY2) g.drawString(txt, 473, j);

        for(int j : semiY3) g.drawString(txt, 873, j);

        g.drawString(txt, 1423, last);

        ImageIO.write(image, "png", h);

    }
}
