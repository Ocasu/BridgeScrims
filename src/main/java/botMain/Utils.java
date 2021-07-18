package botMain;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;
import java.util.Objects;

public class Utils {

    public static void reaction(String player1, String player2, Tournament t, int numberId){
        EmbedBuilder b = new EmbedBuilder();
        b.setTitle("Select winner");
        if(Main.testingMode){
            b.addField(player1 + " won: ", ("\uD83C\uDD70️"), false);
            b.addField(player2 + " won: ", ("\uD83C\uDD71️"), false);
        }else{
            b.addField(Main.getBot().getUserById(player1).getAsTag() + " won: ", ("\uD83C\uDD70️"), false);
            b.addField(Main.getBot().getUserById(player2).getAsTag() + " won: ", ("\uD83C\uDD71️"), false);
        }
        b.setColor(Color.PINK);
        MessageAction messageAction = Objects.requireNonNull(Main.getBot().getTextChannelById(Main.botChannelId)).sendMessage(b.build());
        Message m = messageAction.complete();
        m.addReaction("\uD83C\uDD70️").queue();
        m.addReaction("\uD83C\uDD71️").queue();
        t.getGame(numberId).setScoringMessageId(m.getId());
        t.getCurrentScoringMessageIds().put(m.getId(), numberId);

    }

    public static String print(String m){
        MessageAction ma = Objects.requireNonNull(Main.getBot().getTextChannelById(Main.botChannelId)).sendMessage(m);
        return ma.complete().getId();
    }
    public static String print(String m, String channelId){
        MessageAction ma = Objects.requireNonNull(Main.getBot().getTextChannelById(channelId)).sendMessage(m);
        return ma.complete().getId();
    }
    public static String print(EmbedBuilder b){
        MessageAction m = Objects.requireNonNull(Main.getBot().getTextChannelById(Main.botChannelId)).sendMessage(b.build());
        return m.complete().getId();
    }
    public static void move(User user, VoiceChannel d){
        Main.guild.moveVoiceMember(Objects.requireNonNull(Main.guild.getMember(user)), d).queue();
    }
}
