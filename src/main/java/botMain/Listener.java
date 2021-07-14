package botMain;

import image.addPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class Listener extends ListenerAdapter {

    public Set<String> voiceChannelContains = new HashSet<>();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println(event.getJDA().getSelfUser().getName() + " is online!");
    }


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.startsWith("!")) {
            User author = event.getMessage().getAuthor();
//            if(System.currentTimeMillis() >= botMain.Main.last + 4000){
//                botMain.Main.last = System.currentTimeMillis();
//            }else{
//                botMain.Utils.print("Wait some more seconds");
//                return;
//            }
            if (message.startsWith("!start") && !Main.bracketInProgress) {

                if (Main.testingMode) {

                    String[] testingIds = {"728669301199995035", "690244520423850017", "96491218799775744", "927218070827565088", "999999999999999996", "999999999999999991", "999999999999999995", "999999999999999994"};
//                    String[] testingIds = {"728669301199995035", "690244520423850017", "86491218799775744", "327218070827565088", "999999999999999996", "999999999999999991", "999999999999999995", "999999999999999994"};
                    //ocasu, tphere, shmill, web

                    voiceChannelContains.addAll(Arrays.asList(testingIds));
                    try {
                        Main.brackets.CreateStartBrackets(voiceChannelContains);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Main.bracketInProgress = true;


                } else {


                    VoiceChannel g = Main.getBot().getVoiceChannelById(Main.voiceChannelId);
                    List<Member> gg = g.getMembers();
                    for (Member h : gg) voiceChannelContains.add(h.getId());

                    if (voiceChannelContains.size() >= 8 && voiceChannelContains.contains(author.getId())) {
                        try {
                            Main.brackets.CreateStartBrackets(voiceChannelContains);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Main.bracketInProgress = true;
                    } else if (voiceChannelContains.size() < 8)
                        Utils.print("Not enough people in Bullet Tourney vc to begin");
                    else Utils.print(event.getAuthor().getName() + " is not in Bullet Tourney vc");

                }
            } else if (Main.bracketInProgress && message.startsWith("!start")) {
                Utils.print("A tournament is already in progress");
            }

            if(message.startsWith("!status") && Main.bracketInProgress ){
                Utils.print(getTourney().getCurrentGames().toString());
                Utils.print(Arrays.toString(getTourney().getAllGames()));
            }

//            if(message.startsWith("!reload") && Main.bracketInProgress){
//                try {
//                    addPlayer.reload();
//                    sendBracketsUpdate.update();
//                    System.out.println("reloading");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            if (message.startsWith("!score") && Main.bracketInProgress && getTourney().getCurrentGames().containsKey(event.getAuthor().getId())) {

                int gameId = getTourney().getCurrentGames().get(event.getAuthor().getId());
                if (getTourney().getGame(gameId).getScoringMessageId() == null) {
                    getTourney().getGame(gameId).score(Main.brackets.bulletTourney);
                } else {
                    Utils.print("This game is already being scored");

                }

            }

            if(message.startsWith("!forfeit") && Main.bracketInProgress){
                int f = Main.brackets.bulletTourney.getCurrentGames().get(Objects.requireNonNull(event.getMember()).getUser().getId());
                Game g = Main.brackets.bulletTourney.getGame(f);
                g.forfeit(Objects.requireNonNull(event.getMember()).getUser().getId());
            }

            if(message.startsWith("!restart") && event.getMember().getRoles().contains(Main.getBot().getRoleById("862151580303884308"))){
                Main.bracketInProgress = false;
                Main.brackets = new Brackets(Main.getBot(), Main.getBot().getVoiceChannelById(Main.voiceChannelId));
                EmbedBuilder g = new EmbedBuilder();
                g.setTitle("Restarting bullet bot");
                g.addField(":bust_in_silhouette: User: ", event.getMember().getUser().getAsTag(), false);
                g.addField("Has admin:" , ":ballot_box_with_check:", true);
                Utils.print(g);
            }else if(message.startsWith("!restart")){
                EmbedBuilder g = new EmbedBuilder();
                g.setTitle("Attempted restart");
                g.addField(":bust_in_silhouette: User: ", event.getMember().getUser().getAsTag(), false);
                g.addField("Has admin:" , ":x:", true);
//                botMain.Main.getBot().getEmotesByName("DC_cross", false).get(0);
                Utils.print(g);
            }

        }

    }

    private Tournament getTourney() {
        return Main.brackets.bulletTourney;
    }


    //forfiet
    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);
        for(int i = 0;i<7;i++){
            if(Main.vcs[i] == event.getChannelLeft()) break;
        }
        int f = Main.brackets.bulletTourney.getCurrentGames().get(event.getMember().getUser().getId());
        Game g = Main.brackets.bulletTourney.getGame(f);
        g.forfeit(event);
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        super.onGuildMessageReactionRemove(event);
        if (getTourney().getCurrentScoringMessageIds().containsKey(event.getMessageId())){

            Game g = getTourney().getGame(getTourney().getCurrentGames().get(event.getUserId()));
            if(event.getReactionEmote().getEmoji().startsWith("\uD83C\uDD70")){
                g.getScoringReactions()[0]--;
                System.out.println("removed1");
            }else if(event.getReactionEmote().getEmoji().startsWith("\uD83C\uDD71")){
                g.getScoringReactions()[1]--;
                System.out.println("removed2");

                System.out.println(Arrays.toString(g.getScoringReactions()));
            }

        }
    }


    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {

        super.onGuildMessageReactionAdd(event);

        if (getTourney().getCurrentScoringMessageIds().containsKey(event.getMessageId())
                && !event.getUser().equals(event.getJDA().getSelfUser())) {

            int gameId = getTourney().getCurrentScoringMessageIds().get(event.getMessageId());
            Game g = getTourney().getGame(gameId);

            // if not game players, return
            if (!event.getUser().getId().equals(g.getPlayer1()) && !event.getUser().getId().equals(g.getPlayer2())) {
                Utils.print("Someone not in the bracket tried to react");
                event.getReaction().removeReaction(event.getUser()).queue();
                return;
            }


            System.out.println(event.getReactionEmote().getEmoji());
            if (event.getReactionEmote().getEmoji().startsWith("\uD83C\uDD70️")) {

                g.react(0);
                Utils.print("A player of this bracket claims player 1 has won");
                System.out.println(Arrays.toString(g.getScoringReactions()));

                if (g.getScoringReactions()[0] == (Main.testingMode ? 2 : 2)) {

                    //winner 1

                    EmbedBuilder h = new EmbedBuilder();
                    h.setTitle("Winner is Player 1!");
                    h.setColor(Color.PINK);

                    g.markDone(h, 1);

                    getTourney().getCurrentScoringMessageIds().remove(event.getMessageId());
                    getTourney().getCurrentGames().remove(g.getPlayer1());
                    getTourney().getCurrentGames().remove(g.getPlayer2());
                    if (g.getNumberId() == 6) { //is it game final
                        EmbedBuilder hh = new EmbedBuilder();
                        hh.setTitle("Winner is Player 1!");
                        getTourney().setWinner(g.getPlayer1());
                        hh.setColor(Color.PINK);
                        getTourney().setDone();
                        Main.bracketInProgress = false;
                        Utils.print(hh);
                    } else {
                        int feeding = Main.brackets.feeding.get(g.getNumberId() + 1);

                        if (!Main.testingMode) {
                            Utils.move(Main.getBot().getUserById(g.getPlayer2()), Main.brackets.getWaitingRoom());
                            Utils.move(Main.getBot().getUserById(g.getPlayer1()), Main.vcs[feeding - 1]);
                        } else {

                            Utils.print("Moving player 2 to waiting room");
                            Utils.print("Moving player 1 to vc #" + feeding);
                        }

                        getTourney().makeGame(4 + (feeding - 5));
                        Game next = getTourney().getGame(4 + (feeding - 5));
                        next.addPlayer(g, 1, feeding);
                    }

                    //winner 1 end
                }
            } else if (event.getReactionEmote().getEmoji().startsWith("\uD83C\uDD71")) {
                g.react(1);
                Utils.print("A player of this bracket claims player 2 has won");
                if (g.getScoringReactions()[1] == (Main.testingMode ? 2 : 2)) {
                    EmbedBuilder h = new EmbedBuilder();
                    h.setTitle("Winner is Player 2");
                    h.setColor(Color.PINK);

                    g.markDone(h, 2);

                    getTourney().getCurrentScoringMessageIds().remove(event.getMessageId());
                    getTourney().getCurrentGames().remove(g.getPlayer1());
                    getTourney().getCurrentGames().remove(g.getPlayer2());
                    if (g.getNumberId() == 6) {
                        EmbedBuilder hh = new EmbedBuilder();
                        hh.setTitle("Winner is Player 2!");
                        getTourney().setWinner(g.getPlayer2());
                        getTourney().setDone();
                        Main.bracketInProgress = false;
                        hh.setColor(Color.PINK);
                        Utils.print(hh);
                    } else {
                        int feeding = Main.brackets.feeding.get(g.getNumberId() + 1);

                        if (!Main.testingMode) {
                            Utils.move(Main.getBot().getUserById(g.getPlayer1()), Main.brackets.getWaitingRoom());
                            Utils.move(Main.getBot().getUserById(g.getPlayer2()), Main.vcs[feeding - 1]);
                        } else {
                            Utils.print("Moving player 1 to waiting room");
                            Utils.print("Moving player 2 to vc #" + feeding);
                        }

                        getTourney().makeGame(4 + (feeding - 5));
                        Game next = getTourney().getGame(4 + (feeding - 5));
                        next.addPlayer(g, 2, feeding);

                    }
                }
            }

            if(g.getScoringReactions()[0] ==1 && g.getScoringReactions()[1] == 1){
                Main.getBot().getTextChannelById(Main.botChannelId).retrieveMessageById(event.getMessageId()).queue((message) -> {
                    g.getScoringReactions()[0] = 0;
                    g.getScoringReactions()[1] = 0;
                    message.reply("Scoring disagreement, try again").queue();
                    message.clearReactions().queue();

                    message.addReaction("\uD83C\uDD70️").queue();
                    message.addReaction("\uD83C\uDD71").queue();
                }, (failure) -> {
                    Utils.print("Error");
                });
            }

        }
    }
}