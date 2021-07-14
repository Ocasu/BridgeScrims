package botMain;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.io.File;
import java.util.Objects;

public class sendBracketsUpdate {

    public static void update(){
        MessageAction ma2 = Objects.requireNonNull(Main.getBot().getTextChannelById(Main.botChannelImageId)).sendMessage("!clear 99");
        ma2.complete();
        MessageAction ma = Objects.requireNonNull(Main.getBot().getTextChannelById(Main.botChannelImageId))
                .sendMessage("Brackets:").addFile(new File("src/main/java/image/bbracketrevamp2.png"));
        ma.complete();
    }
}
