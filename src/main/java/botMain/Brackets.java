package botMain;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Brackets {

    private final JDA bot;
    private final VoiceChannel waitingRoom;

    public Brackets(JDA bot, VoiceChannel waitingRoom) {
        this.bot = bot;
        this.waitingRoom = waitingRoom;
    }

    public VoiceChannel getWaitingRoom() {
        return waitingRoom;
    }

    public HashMap<Integer, String> playerNumToId = new HashMap<>();

    public final Map<Integer, Integer> feeding = Map.of(1, 5, 2, 5, 3, 6, 4, 6, 6, 7, 5, 7);


    public String[] good = {"361512261241536522", "148707908748574720", "829088868817043467", "284848470056108042", "336540382785437698"};
    //tphere, ogwilly, bucky, cheetah, speed



    public Tournament bulletTourney;

    public void CreateStartBrackets(Set<String> members) throws IOException {
//        image.reset.re();

        bulletTourney = new Tournament();

        int ct = 1;
        //set will shuffle it
        Object[] meme = members.toArray();
        Arrays.sort(meme);
        for (Object player : meme) {
            playerNumToId.put(ct, (String) player);
            ct++;
        }
        for (int i = 0; i < 4; i++) {
            String one = playerNumToId.get(i * 2 + 1);
            String two = playerNumToId.get(i * 2 + 2);

//            bulletTourney.quarterFinals[i].player1 = one;
//            bulletTourney.quarterFinals[i].player2 = two;
//            bulletTourney.currentGames.put(one, i);
//            bulletTourney.currentGames.put(two, i);
            bulletTourney.makeGame(one, two, i);
        }


        EmbedBuilder b = new EmbedBuilder();
        b.setTitle("Brackets [Testing mode: " + Main.testingMode + "]");


        for (int i = 0; i < 4; i++) {


            User one = bot.getUserById(Long.parseLong(bulletTourney.getGame(i).getPlayer1()));
            User two = bot.getUserById(Long.parseLong(bulletTourney.getGame(i).getPlayer2()));


            bulletTourney.getGame(i).start(Main.vcs[i]);

            String[] nums = {"", "one", "two", "three", "four"};

            if (!Main.testingMode) {

                Utils.move(one, Main.vcs[i]);
                Utils.move(two, Main.vcs[i]);

                b.addField("Game :" + nums[(i + 1)] + ":", one.getAsTag() + " :vs: " + two.getAsTag(), false);
            } else {
                b.addField("Game :" + nums[(i + 1)] + ":", bulletTourney.getGame(i).getPlayer1() + " :vs: " + bulletTourney.getGame(i).getPlayer2(), false);
            }


        }
        b.setColor(Color.PINK);
        b.setThumbnail("https://cdn.discordapp.com/avatars/361512261241536522/a_ef0857c45206dbd5b09477cde5dc9eeb.gif");
        Utils.print(b);


    }


}