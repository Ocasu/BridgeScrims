package botMain;

import java.util.HashMap;

public class Tournament {
    private final Game[] allGames = new Game[7];

    public Game[] getAllGames() {
        return allGames;
    }

    private boolean done = false;
    private String winner;

    private final HashMap<String, Integer> currentGames = new HashMap<>();
    private final HashMap<String, Integer> currentScoringMessageIds = new HashMap<>();

    public Tournament() {
    }


    // 1. Get a game information given the game ID
    public void makeGame(String id1, String id2, int gameId){
        allGames[gameId] = new Game(id1, id2, gameId);
        currentGames.put(id1,gameId);
        currentGames.put(id2,gameId);
    }

    public void makeGame(int gameId){
        if(allGames[gameId] == null) allGames[gameId] = new Game(gameId);
    }

    // 2. Apply game result
    // 3. Get game
    public Game getGame(int gameId){
        return allGames[gameId];
    }

    public void setDone(){
        this.done = true;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public HashMap<String, Integer> getCurrentGames() {
        return currentGames;
    }

    public HashMap<String, Integer> getCurrentScoringMessageIds() {
        return currentScoringMessageIds;
    }

    public String getWinner() {
        return winner;
    }
}
