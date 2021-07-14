package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class reset {
    public static void main(String[] args) throws IOException {
        File h = new File("src/main/java/image/bbracketrevamp2.png");
        File h2 = new File("src/main/java/image/bbracketrevamp22.png");

        BufferedImage image = ImageIO.read(h2);
        ImageIO.write(image, "png", h);
    }
    public static void re() throws IOException {
        File h = new File("src/main/java/image/bbracketrevamp2.png");
        File h2 = new File("src/main/java/image/bbracketrevamp22.png");

        BufferedImage image = ImageIO.read(h2);
        ImageIO.write(image, "png", h);
    }
}
