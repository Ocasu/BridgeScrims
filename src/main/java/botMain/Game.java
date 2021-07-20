package botMain;

import image.addPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Game {
    private String player1;
    private String player2;
    private final int numberId;

    private VoiceChannel gameVCId;

    private String scoringMessageId;
    private String announcementMessageId;
    private int[] scoringReactions = {0, 0};

    private boolean started;
    private boolean finished;
    private String winner;

    @Override
    public String toString() {
        return "botMain.Game{" +
                "player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                '}';
    }

    public Game(String player1, String player2, int id) {
        this.player1 = player1;
        this.player2 = player2;
        this.numberId = id;
    }

    public Game(int id) {
        numberId = id;
    }

    public final String getPlayer1() {
        return player1;
    }

    public final String getPlayer2() {
        return player2;
    }

    public void start(VoiceChannel vc) {
        this.gameVCId = vc;
        this.started = true;
    }

    public void forfeit(GuildVoiceLeaveEvent event) throws IOException {
        boolean cont = false;
        for (int i = 0; i < 7; i++) {
            if (Main.vcs[i].getId().equals(event.getChannelLeft().getId())) {
                cont = true;
                break;
            }
        }
        if (cont) {
            //check which player
            if (this.getPlayer2().equals(event.getMember().getUser().getId())) {
                //player 1

                EmbedBuilder h = new EmbedBuilder();
                h.setTitle("Winner is Player 1!");
                h.setFooter("Due to player 2 forfeit");
                h.setColor(Color.PINK);

                this.markDone(h, 1);

                Main.brackets.bulletTourney.getCurrentGames().remove(this.getPlayer1());

                Main.brackets.bulletTourney.getCurrentGames().remove(this.getPlayer2());
                if (this.getNumberId() == 6) { //is it game final
                    EmbedBuilder hh = new EmbedBuilder();
                    hh.setTitle("Winner is Player 1!");
                    hh.setFooter("Due to player 2 forfeit");
                    Main.brackets.bulletTourney.setWinner(this.getPlayer1());
                    hh.setColor(Color.PINK);
                    Main.brackets.bulletTourney.setDone();
                    Main.bracketInProgress = false;
                    Utils.print(hh);
                } else {
                    int feeding = Main.brackets.feeding.get(this.getNumberId() + 1);

                    if (!Main.testingMode) {
//                        botMain.Utils.move(botMain.Main.getBot().getUserById(g.getPlayer2()), botMain.Main.brackets.getWaitingRoom());
                        Utils.move(Main.getBot().getUserById(this.getPlayer1()), Main.vcs[feeding - 1]);
                    } else {

//                        botMain.Utils.print("Moving player 2 to waiting room");
                        Utils.print("Moving player 1 to vc #" + feeding);
                    }

                    Main.brackets.bulletTourney.makeGame(4 + (feeding - 5));
                    Game next = Main.brackets.bulletTourney.getGame(4 + (feeding - 5));
                    next.addPlayer(this, 1, feeding);
                }
                //player 1 end

            } else if (this.getPlayer1().equals(event.getMember().getUser().getId())) {
                //player 2
                EmbedBuilder h = new EmbedBuilder();
                h.setTitle("Winner is Player 2");
                h.setFooter("Due to player 1 forfeit");
                h.setColor(Color.PINK);

                this.markDone(h, 2);

                Main.brackets.bulletTourney.getCurrentGames().remove(this.getPlayer1());
                Main.brackets.bulletTourney.getCurrentGames().remove(this.getPlayer2());
                if (this.getNumberId() == 6) {
                    EmbedBuilder hh = new EmbedBuilder();
                    hh.setTitle("TOUNREY winner is Player 2!");
                    hh.setFooter("Due to player 1 forfeit");
                    Main.brackets.bulletTourney.setWinner(this.getPlayer2());
                    Main.brackets.bulletTourney.setDone();
                    Main.bracketInProgress = false;
                    hh.setColor(Color.PINK);
                    Utils.print(hh);
                    addPlayer.reload();
                    sendBracketsUpdate.update();

                } else {
                    int feeding = Main.brackets.feeding.get(this.getNumberId() + 1);

                    if (!Main.testingMode) {
//                        botMain.Utils.move(botMain.Main.getBot().getUserById(g.getPlayer1()), botMain.Main.brackets.getWaitingRoom());
                        Utils.move(Main.getBot().getUserById(this.getPlayer2()), Main.vcs[feeding - 1]);
                    } else {
//                        botMain.Utils.print("Moving player 1 to waiting room");
                        Utils.print("Moving player 2 to vc #" + feeding);
                    }

                    Main.brackets.bulletTourney.makeGame(4 + (feeding - 5));
                    Game next = Main.brackets.bulletTourney.getGame(4 + (feeding - 5));
                    next.addPlayer(this, 2, feeding);

                }
                //player 2 end
            } else {
                //some error
                Utils.print("Error");
            }
        }
    }

    public void forfeit(String playerId) {

        if (this.getPlayer2().equals(playerId)) {
            //player 1

            EmbedBuilder h = new EmbedBuilder();
            h.setTitle("Winner is Player 1!");
            h.setFooter("Due to player 2 forfeit");
            h.setColor(Color.PINK);

            this.markDone(h, 1);

            Main.brackets.bulletTourney.getCurrentGames().remove(this.getPlayer1());

            Main.brackets.bulletTourney.getCurrentGames().remove(this.getPlayer2());
            if (this.getNumberId() == 6) { //is it game final
                EmbedBuilder hh = new EmbedBuilder();
                hh.setTitle("Winner is Player 1!");
                hh.setFooter("Due to player 2 forfeit");
                Main.brackets.bulletTourney.setWinner(this.getPlayer1());
                hh.setColor(Color.PINK);
                Main.brackets.bulletTourney.setDone();
                Main.bracketInProgress = false;
                Utils.print(hh);
            } else {
                int feeding = Main.brackets.feeding.get(this.getNumberId() + 1);

                if (!Main.testingMode) {
//                        botMain.Utils.move(botMain.Main.getBot().getUserById(g.getPlayer2()), botMain.Main.brackets.getWaitingRoom());
                    Utils.move(Main.getBot().getUserById(this.getPlayer1()), Main.vcs[feeding - 1]);
                } else {

//                        botMain.Utils.print("Moving player 2 to waiting room");
                    Utils.print("Moving player 1 to vc #" + feeding);
                }

                Main.brackets.bulletTourney.makeGame(4 + (feeding - 5));
                Game next = Main.brackets.bulletTourney.getGame(4 + (feeding - 5));
                next.addPlayer(this, 1, feeding);
            }
            //player 1 end

        } else if (this.getPlayer1().equals(playerId)) {
            //player 2
            EmbedBuilder h = new EmbedBuilder();
            h.setTitle("Winner is Player 2");
            h.setFooter("Due to player 1 forfeit");
            h.setColor(Color.PINK);

            this.markDone(h, 2);

            Main.brackets.bulletTourney.getCurrentGames().remove(this.getPlayer1());
            Main.brackets.bulletTourney.getCurrentGames().remove(this.getPlayer2());
            if (this.getNumberId() == 6) {
                EmbedBuilder hh = new EmbedBuilder();
                hh.setTitle("Winner is Player 2!");
                hh.setFooter("Due to player 1 forfeit");
                Main.brackets.bulletTourney.setWinner(this.getPlayer2());
                Main.brackets.bulletTourney.setDone();
                Main.bracketInProgress = false;
                hh.setColor(Color.PINK);
                Utils.print(hh);
            } else {
                int feeding = Main.brackets.feeding.get(this.getNumberId() + 1);

                if (!Main.testingMode) {
//                        botMain.Utils.move(botMain.Main.getBot().getUserById(g.getPlayer1()), botMain.Main.brackets.getWaitingRoom());
                    Utils.move(Main.getBot().getUserById(this.getPlayer2()), Main.vcs[feeding - 1]);
                } else {
//                        botMain.Utils.print("Moving player 1 to waiting room");
                    Utils.print("Moving player 2 to vc #" + feeding);
                }

                Main.brackets.bulletTourney.makeGame(4 + (feeding - 5));
                Game next = Main.brackets.bulletTourney.getGame(4 + (feeding - 5));
                next.addPlayer(this, 2, feeding);

            }
            //player 2 end
        }

    }

    public void react(int num) {
        scoringReactions[num]++;
    }

    public int getNumberId() {
        return numberId;
    }

    public String getScoringMessageId() {
        return scoringMessageId;
    }

    public int[] getScoringReactions() {
        return scoringReactions;
    }

    public void setScoringMessageId(String scoringMessageId) {
        this.scoringMessageId = scoringMessageId;
    }

    public void addPlayer(Game g, int winner, int feeding) {

        System.out.println(this);

        if (winner == 1) {
            if (this.player1 == null) {
                this.player1 = g.getPlayer1();
            } else {
                this.player2 = g.getPlayer1();
                this.started = true;
                this.gameVCId = Main.vcs[4 + (feeding - 5)];
                Main.brackets.bulletTourney.getCurrentGames().put(this.player1, 4 + (feeding - 5));
                Main.brackets.bulletTourney.getCurrentGames().put(this.player2, 4 + (feeding - 5));

            }
        } else {
            if (this.player1 == null) {
                this.player1 = g.getPlayer2();
            } else {
                this.player2 = g.getPlayer2();
                this.started = true;
                this.gameVCId = Main.vcs[4 + (feeding - 5)];
                Main.brackets.bulletTourney.getCurrentGames().put(this.player1, 4 + (feeding - 5));
                Main.brackets.bulletTourney.getCurrentGames().put(this.player2, 4 + (feeding - 5));
            }
        }
        try {
            addPlayer.reload();
            sendBracketsUpdate.update();
            System.out.println("reloading");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(winner);
        System.out.println(this);
        System.out.println(feeding);
        System.out.println("1:" + this.player1);
        System.out.println("2:" + this.player2);

    }

    /**
     * Sends message to channel for players confirm who won the given game
     *
     * @param t botMain.Tournament object
     */
    public void score(Tournament t) {
        System.out.println(player1 + " " + player2 + " " + numberId);
        Utils.reaction(player1, player2, t, numberId);
    }

    public void markDone(EmbedBuilder h, int winner) {
        this.finished = true;
        this.announcementMessageId = Utils.print(h);
        if (winner == 1) this.winner = this.player1;
        else if (winner == 2) this.winner = this.player2;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return numberId == game.numberId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberId);
    }

    public String getWinner() {
        return winner;
    }
}
