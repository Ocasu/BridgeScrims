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

    public static Set<String> voiceChannelContains = new HashSet<>();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println(event.getJDA().getSelfUser().getName() + " is online!");
    }

    private boolean isAdmin(Member member) {
        String[] admins = {"760148398857912380", "759949547882545154", "834247683484024893"};
        boolean g = false;
        for(String y : admins){
            if(member.getRoles().contains(Main.getBot().getRoleById(y))){
                g = true;
                break;
            }
        }
        return g;
    }

    private boolean inTourney(User user) {
        return getTourney().getCurrentGames().containsKey(user.getId());
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        try {

            String message = event.getMessage().getContentRaw();
            if (!event.getChannel().getId().equals(Main.botChannelId) || !message.startsWith("!")) {
                return;
            }

            User author = event.getMessage().getAuthor();
//            if(System.currentTimeMillis() >= botMain.Main.last + 4000){
//                botMain.Main.last = System.currentTimeMillis();
//            }else{
//                botMain.Utils.print("Wait some more seconds");
//                return;
//            }
            if (message.startsWith("!start")) {

                if (Main.bracketInProgress) {
                    Utils.print("A tournament is already in progress");
                    return;
                }

                if (Main.testingMode) {

                    String[] testingIds = {"728669301199995035", "86491218799775744", "701197998121549844",
                            "426409201464508437", "488126256089530389", "566791733082456075", "691070187335581706",
                            "336927028999880716"};
//                    String[] testingIds = {"728669301199995035", "690244520423850017", "86491218799775744", "327218070827565088", "999999999999999996", "999999999999999991", "999999999999999995", "999999999999999994"};
                    //ocasu, tphere, shmill, web

                    voiceChannelContains.addAll(Arrays.asList(testingIds));
                    Main.brackets.CreateStartBrackets(voiceChannelContains);
                    Main.bracketInProgress = true;
                    return;
                }

                VoiceChannel g = Main.getBot().getVoiceChannelById(Main.voiceChannelId);
                List<Member> gg = g.getMembers();
                for (Member h : gg) voiceChannelContains.add(h.getId());

                if (!voiceChannelContains.contains(author.getId())) {
                    Utils.print(event.getAuthor().getName() + " is not in Bullet Tourney vc");
                    return;
                }

                if (voiceChannelContains.size() < 8) {
                    Utils.print("Not enough people in Bullet Tourney vc to begin [" + voiceChannelContains.size()
                            + "/8 players joined]");
                    return;
                }

                Main.brackets.CreateStartBrackets(voiceChannelContains);
                Main.bracketInProgress = true;
                return;
            }

            if (message.startsWith("!status") && Main.bracketInProgress && isAdmin(event.getMember())) {
                Utils.print(getTourney().getCurrentGames().toString());
                Utils.print(Arrays.toString(getTourney().getAllGames()));
                return;
            }

            if (message.startsWith("!score")) {

                if (!Main.bracketInProgress) {
                    Utils.print("A tournament is not in progress yet");
                    return;
                }

                if (!inTourney(event.getAuthor())) {
                    Utils.print("You have to be in a tournament to use this command");
                    return;
                }


                int gameId = getTourney().getCurrentGames().get(event.getAuthor().getId());
                if (getTourney().getGame(gameId).getScoringMessageId() == null) {
                    getTourney().getGame(gameId).score(Main.brackets.bulletTourney);
                } else {
                    Utils.print("This game is already being scored");
                }

                return;
            }

            if (message.startsWith("!forfeit")) {
                if (!Main.bracketInProgress) {
                    Utils.print("A tournament is not in progress yet");
                    return;
                }

                int f = Main.brackets.bulletTourney.getCurrentGames().get(Objects.requireNonNull(event.getMember()).getUser().getId());
                Game g = Main.brackets.bulletTourney.getGame(f);
                g.forfeit(Objects.requireNonNull(event.getMember()).getUser().getId());
                return;
            }

            if (message.startsWith("!test-toggle")) {

                if (!isAdmin(Objects.requireNonNull(event.getMember()))) {
                    EmbedBuilder g = new EmbedBuilder();
                    g.setTitle("Attempted toggle");
                    g.addField(":bust_in_silhouette: User: ", event.getMember().getUser().getAsTag(), false);
                    g.addField("Has admin:", ":x:", true);
                    Utils.print(g);
                    return;
                }

                Main.testingMode = !Main.testingMode;
                EmbedBuilder g = new EmbedBuilder();
                g.setTitle("Test mode: " + Main.testingMode);
                g.addField(":bust_in_silhouette: User: ", event.getMember().getUser().getAsTag(), false);
                g.addField("Has admin:", ":ballot_box_with_check:", true);
                Utils.print(g);

                if (Main.testingMode) Main.getBot().getPresence().setActivity(Activity.playing("Testing mode"));
                else Main.getBot().getPresence().setActivity(Activity.playing("Version -1.0.0"));

                return;
            }


            if (message.startsWith("!restart")) {

                if (!isAdmin(event.getMember())) {
                    EmbedBuilder g = new EmbedBuilder();
                    g.setTitle("Attempted restart");
                    g.addField(":bust_in_silhouette: User: ", event.getMember().getUser().getAsTag(), false);
                    g.addField("Has admin:", ":x:", true);
                    Utils.print(g);
                    return;
                }

                Main.bracketInProgress = false;
                Main.brackets = new Brackets(Main.getBot(), Main.getBot().getVoiceChannelById(Main.voiceChannelId));
                voiceChannelContains = new HashSet<>();

                EmbedBuilder g = new EmbedBuilder();
                g.setTitle("Restarting bullet bot");
                g.addField(":bust_in_silhouette: User: ", event.getMember().getUser().getAsTag(), false);
                g.addField("Has admin:", ":ballot_box_with_check:", true);
                Utils.print(g);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Tournament getTourney() {
        return Main.brackets.bulletTourney;
    }

    //forfeit
    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        try {
            super.onGuildVoiceLeave(event);
            boolean a = false;
            for (int i = 0; i < 7; i++) {
                if (Main.vcs[i].getId().equals(event.getChannelLeft().getId())) {
                    a = true;
                    break;
                }
            }
            if (!a) return;
            Integer f = Main.brackets.bulletTourney.getCurrentGames().get(event.getMember().getUser().getId());
            if (f == null) {
                return;
            }

            Game g = Main.brackets.bulletTourney.getGame(f);
            g.forfeit(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        super.onGuildMessageReactionRemove(event);
        if(!event.getChannel().getId().equals(Main.botChannelId + "")){
            return;
        }
        if (getTourney().getCurrentScoringMessageIds().containsKey(event.getMessageId())) {

            Game g = getTourney().getGame(getTourney().getCurrentGames().get(event.getUserId()));
            if (event.getReactionEmote().getEmoji().startsWith("\uD83C\uDD70")) {
                g.getScoringReactions()[0]--;
                System.out.println("removed1");
            } else if (event.getReactionEmote().getEmoji().startsWith("\uD83C\uDD71")) {
                g.getScoringReactions()[1]--;
                System.out.println("removed2");

                System.out.println(Arrays.toString(g.getScoringReactions()));
            }

        }
    }

    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {

        super.onGuildMessageReactionAdd(event);

        try {
            if(!event.getChannel().getId().equals(Main.botChannelId + "")){
                return;
            }

            if (!event.getReactionEmote().isEmoji()
                    || !getTourney().getCurrentScoringMessageIds().containsKey(event.getMessageId())
                    || event.getUser().equals(event.getJDA().getSelfUser())) {
                return;
            }

            int gameId = getTourney().getCurrentScoringMessageIds().get(event.getMessageId());
            Game g = getTourney().getGame(gameId);

            // if not game players, return
            if (!event.getUser().getId().equals(g.getPlayer1()) && !event.getUser().getId().equals(g.getPlayer2())) {
                Utils.print("Someone not in the bracket tried to react");
                event.getReaction().removeReaction(event.getUser()).queue();
                return;
            }

            System.out.println(event.getReactionEmote().getEmoji());
            int playerIndex;
            if (event.getReactionEmote().getEmoji().startsWith("\uD83C\uDD70️")) playerIndex = 0;
            else if (event.getReactionEmote().getEmoji().startsWith("\uD83C\uDD71")) playerIndex = 1;
            else return;

            int otherPlayerIndex = playerIndex == 0 ? 1 : 0;
            String playerDisplay = playerIndex == 0 ? "1" : "2";
            String otherPlayerDisplay = playerIndex == 0 ? "2" : "1";
            int winner = playerIndex == 0 ? 1 : 2;
            String winnerPlayer = playerIndex == 0 ? g.getPlayer1() : g.getPlayer2();
            String otherPlayer = playerIndex == 0 ? g.getPlayer2() : g.getPlayer1();

            g.react(playerIndex);
            if(playerDisplay.equals("1")) Utils.print("A player of this bracket claims player " + Main.getBot().getUserById(g.getPlayer1()) + " has won");
            if(playerDisplay.equals("2")) Utils.print("A player of this bracket claims player " + Main.getBot().getUserById(g.getPlayer2()) + " has won");

            System.out.println(Arrays.toString(g.getScoringReactions()));

            if (g.getScoringReactions()[playerIndex] == (Main.testingMode ? 2 : 2) &&
                    g.getScoringReactions()[otherPlayerIndex] == 0) {

                //winner
                EmbedBuilder h = new EmbedBuilder();
                h.setTitle("Winner is Player " + playerDisplay + "!");
                h.setColor(Color.PINK);

                g.markDone(h, winner);

                getTourney().getCurrentScoringMessageIds().remove(event.getMessageId());
                getTourney().getCurrentGames().remove(winnerPlayer);
                getTourney().getCurrentGames().remove(otherPlayer);
                if (g.getNumberId() == 6) { //is it game final
                    EmbedBuilder hh = new EmbedBuilder();
                    hh.setTitle("Winner is Player " + playerDisplay + "!");
                    if(playerDisplay.equals("1")) hh.setTitle("Winner is Player " + Main.getBot().getUserById(g.getPlayer1()) + "!");
                    if(playerDisplay.equals("2")) hh.setTitle("Winner is Player " + Main.getBot().getUserById(g.getPlayer2()) + "!");
                    getTourney().setWinner(winnerPlayer);
                    hh.setColor(Color.PINK);
                    getTourney().setDone();
                    Main.bracketInProgress = false;
                    Utils.print(hh);
                    addPlayer.reload();
                    sendBracketsUpdate.update();
                } else {
                    int feeding = Main.brackets.feeding.get(g.getNumberId() + 1);

                    if (!Main.testingMode) {
                        Utils.move(Main.getBot().getUserById(otherPlayer), Main.brackets.getWaitingRoom());
                        Utils.move(Main.getBot().getUserById(winnerPlayer), Main.vcs[feeding - 1]);
                    } else {

                        Utils.print("Moving player " + otherPlayerDisplay + " to waiting room");
                        Utils.print("Moving player " + playerDisplay + " to vc #" + feeding);
                    }

                    getTourney().makeGame(4 + (feeding - 5));
                    Game next = getTourney().getGame(4 + (feeding - 5));
                    next.addPlayer(g, winner, feeding);
                }

                //winner end
            }

            /*
            if (event.getReactionEmote().getEmoji().startsWith("\uD83C\uDD70️")) {

                g.react(0);
                Utils.print("A player of this bracket claims player 1 has won");
                System.out.println(Arrays.toString(g.getScoringReactions()));

                if (g.getScoringReactions()[0] == (Main.testingMode ? 2 : 2) &&
                        g.getScoringReactions()[1] == 0) {

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
             */

            if (g.getScoringReactions()[0] > 0 && g.getScoringReactions()[1] > 0) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}