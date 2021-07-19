package image;

import botMain.Game;
import botMain.Main;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class addPlayer {


    static int[] semiY = {90, 200, 330, 440, 570, 680, 820, 925};

    static int[] semiY2 = {140, 380, 630, 865};
    static int[] semiY3 = {270, 750};
    static int last = 510;
    static int[] xs = {68, 473, 873, 1423};

    static Color member = Color.WHITE;
    static Color prime = new Color(125, 170, 230);
    static Color privat = new Color(200, 160, 200);
    static Color premium = new Color(176, 125,244);
    static long premId = 841843353992822801L;
    static long privId = 841843353992822800L;
    static long primId = 866819790844067860L;


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
            User a = Main.getBot().getUserById(p1);
            User b = Main.getBot().getUserById(p2);
            p1 = a.getName();
            p2 = b.getName();
            p1 = p1.substring(0, Math.min(16, p1.length()));
            p2 = p2.substring(0, Math.min(16, p2.length()));
            draw(g, p1, xs[0], semiY[i*2], a);
            draw(g, p2, xs[0], semiY[i*2 + 1], b);
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
                User a = Main.getBot().getUserById(p1);
                User b = Main.getBot().getUserById(p2);
                p1 = a.getAsTag();
                p2 = b.getAsTag();
                p1 = p1.substring(0, Math.min(16, p1.length()));
                if(p2.length()!=0) p2 = p2.substring(0, Math.min(16, p2.length()));
                draw(g, p1, xs[1], semiY2[i * 2], a);
                draw(g, p2, xs[1], semiY2[i * 2 + 1], b);
            }
        }
        if(gs[6] != null){
            String p1 = gs[6].getPlayer1();
            String p2 = "";
            if(gs[6].getPlayer2() != null){
                p2 = gs[6].getPlayer2();
            }
            User a = Main.getBot().getUserById(p1);
            User b = Main.getBot().getUserById(p2);
            p1 = a.getName();
            p2 = b.getName();
            p1 = p1.substring(0, Math.min(16, p1.length()));
            if(p2.length() != 0) p2 = p2.substring(0, Math.min(16, p2.length()));
            draw(g, p1, xs[2], semiY3[0], a);
            draw(g, p2, xs[2], semiY3[1], b);
        }
        if(Main.brackets.bulletTourney.getWinner() != null){
            draw(g, Main.getBot().getUserById(Main.brackets.bulletTourney.getWinner()).getName(), xs[3], last, Main.getBot().getUserById(Main.brackets.bulletTourney.getWinner()));
        }

        ImageIO.write(image,"png",h2);
    }
    public static void draw(Graphics g, String s, int x, int y, User a){
        g.setColor(Color.BLACK);
        List<Role> h = Main.guild.getMember(a).getRoles();
//        if(h.contains(Main.getBot().getRoleById(premId))){
//            g.setColor(premium.darker().darker());
//        }
        System.out.println(a.getAsTag() + " " +  h);

        g.drawString(s, x+1, y-1);
        g.drawString(s, x+1, y+1);
        g.drawString(s, x-1, y-1);
        g.drawString(s, x-1, y+1);
        g.setColor(Color.WHITE);
//        if(h.contains(Main.getBot().getRoleById(premId))){
//            g.setColor(premium);
//        }else if(h.contains(Main.getBot().getRoleById(privId))){
//            g.setColor(privat);
//        }else if(h.contains(Main.getBot().getRoleById(primId))){
//            g.setColor(prime);
//        }
        g.drawString(s, x, y);
    }
}
