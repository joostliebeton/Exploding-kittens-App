package ExplodingKittens;

import java.util.ArrayList;

public class Game {
    //players: Arraylist<player>

    public static ArrayList<Player> players;
    public static ArrayList<Player> eliminatedPlayers;
    public ArrayList<Player> players;
    private Player currentPlayer;
    private boolean GameOVer;
    public ArrayList<Player> eliminatedPlayers;
    public String[] playernames;
    public void setPlayernames(String[] playernames) {
        this.playernames = playernames;
    }
    public Game(ArrayList<Player> players){
        this.players = players;
        this.currentPlayer=null;
        this.deck = new Deck();
    }
    public Player Player;
    static Player currentPlayer;
    public static void EleminatePlayer(){
        players.remove(currentPlayer);
        eliminatedPlayers.add(currentPlayer);

    }
    public static Player Player;
    //deck:Arraylist<Deck>
    public Deck deck;
    //startGame() starts the game and does all the things to be able to start (shuffle() etc.)
    public ArrayList<Player> makeplayerlist(String[] names){
        for(String name: names){
            Player player = new Player(name);
            players.add(player);
        }
        return players;
    }
    public void startGame(){
        Game game = new Game(players);
    //do all the things needed to start the game
    }
    //nextPlayer() moves to the next player
    public static Player nextPlayer(){
        for(Player player: players){
            if(player == currentPlayer){
                currentPlayer = players.get(players.indexOf(player)+1);
            }
            else{
                System.out.println("error nextPlayer");
            }
        }
        return Player;
    }
    private void play(){
        startGame();
        while(players.size()>1){
            nextPlayer();
            //playCard();
        }
        endGame();
    }
    //endGame() ends the game when last kitten exploded
    public void endGame(){
        //end the game
    }
}
