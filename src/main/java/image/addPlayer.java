package image;

import botMain.Game;
import botMain.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class addPlayer {


    static int[] semiY = {90, 200, 330, 440, 570, 680, 820, 925};

    static int[] semiY2 = {140, 380, 630, 865};
    static int[] semiY3 = {270, 750};
    static int last = 510;
    static int[] xs = {68, 473, 873, 1423};
    public void add(String a, int gameId, Game g){
        if(g.getPlayer1() == null){

        }else{

        }
    }


    public static void reload() throws IOException {
        Font font = new Font("Minecraft", Font.PLAIN, 40);
        File h = new File("src/main/java/image/bbracketrevamp22.png");
        File h2 = new File("src/main/java/image/bbracketrevamp2.png");

        System.out.println(h.getAbsolutePath());

        BufferedImage image;
        image = ImageIO.read(h);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.setFont(font);
        Game[] gs = Main.brackets.bulletTourney.getAllGames();

        for(int i = 0 ;i<4;i++){
            String p1 = gs[i].getPlayer1();
            String p2 = gs[i].getPlayer2();
            if(!Main.testingMode){
                p1 = Main.getBot().getUserById(p1).getAsTag();
                p2 = Main.getBot().getUserById(p1).getAsTag();
            }
            p1 = p1.substring(0, Math.min(16, p1.length()));
            p2 = p2.substring(0, Math.min(16, p2.length()));
            draw(g, p1, xs[0], semiY[i*2]);
            draw(g, p2, xs[0], semiY[i*2 + 1]);
        }
        for(int i = 0 ;i<2;i++){
            if(gs[i+4] != null ) {
                String p1 = gs[i + 4].getPlayer1();
                String p2 = "";
                if(gs[i + 4].getPlayer2() != null){
                    p2 = gs[i + 4].getPlayer2();
                }
                if(gs[1].getWinner() != null && gs[1].getWinner().equals(p1)){
                    String temp = p1;
                    p1 = p2;
                    p2 = temp;
                }
                if(gs[3].getWinner() != null && gs[3].getWinner().equals(p1)){
                    String temp = p1;
                    p1 = p2;
                    p2 = temp;
                }
                if (!Main.testingMode) {
                    p1 = Main.getBot().getUserById(p1).getAsTag();
                    p2 = Main.getBot().getUserById(p1).getAsTag();
                }
                p1 = p1.substring(0, Math.min(16, p1.length()));
                if(p2.length()!=0) p2 = p2.substring(0, Math.min(16, p2.length()));
                draw(g, p1, xs[1], semiY2[i * 2]);
                draw(g, p2, xs[1], semiY2[i * 2 + 1]);
            }
        }
        if(gs[6] != null){
            String p1 = gs[6].getPlayer1();
            String p2 = "";
            if(gs[6].getPlayer2() != null){
                p2 = gs[6].getPlayer2();
            }
            if (!Main.testingMode) {
                p1 = Main.getBot().getUserById(p1).getAsTag();
                p2 = Main.getBot().getUserById(p1).getAsTag();
            }
            p1 = p1.substring(0, Math.min(16, p1.length()));
            if(p2.length() != 0) p2 = p2.substring(0, Math.min(16, p2.length()));
            draw(g, p1, xs[2], semiY3[0]);
            draw(g, p2, xs[2], semiY3[1]);
        }
        if(Main.brackets.bulletTourney.getWinner() != null){
            draw(g, Main.brackets.bulletTourney.getWinner(), xs[3], last);
        }

        ImageIO.write(image,"png",h2);
    }
    public static void draw(Graphics g, String s, int x, int y){
        g.setColor(Color.BLACK);
        g.drawString(s, x+3, y-3);
        g.drawString(s, x+3, y+3);
        g.drawString(s, x-3, y-3);
        g.drawString(s, x-3, y+3);
        g.setColor(Color.WHITE);
        g.drawString(s, x, y);
    }
}
