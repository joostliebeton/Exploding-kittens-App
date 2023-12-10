package ExplodingKittens;

import java.util.ArrayList;

public class Game {
    //players: Arraylist<player>
    public ArrayList<Player> players;
    public Player Player;
    //deck:Arraylist<Deck>
    public Deck deck;
    //startGame() starts the game and does all the things to be able to start (shuffle() etc.)
    public void startGame(){
        //do all the things needed to start the game
    }
    //nextPlayer() moves to the next player
    public Player nextPlayer(){
        return Player;
    }
    //endGame() ends the game when last kitten exploded
    public void endGame(){
        //end the game
    }
}
