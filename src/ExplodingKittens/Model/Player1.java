package ExplodingKittens.Model;
import ExplodingKittens.Controller.Game;
import ExplodingKittens.View.ClientTUI;
import java.util.List;

public abstract class Player1 {
    String name;
    Hand hand;
    Game game;
    boolean isAlive;
    Card playedCard; // does this belong in this class? or do we put this in the game class?
    int extraTurns;
    int turnsToSkip;
public Player1(String name, Game game){
    this.name = name;
    this.game = game;
    this.hand = new Hand();
    this.isAlive = true;
    this.extraTurns = 0;
    this.turnsToSkip = 0;
    this.playedCard = null;

}
    //getters
    public String getName(){return this.name;}
    public Hand getHand(){return this.hand;}
    public Game getGame(){return this.game;} // if we implement that we can play more games at once this might be a handy getter
    public boolean getIsAlive(){return this.isAlive;}
    public int getTurnsToSkip() {return turnsToSkip;}
    public int getExtraTurns() {return extraTurns;}
    public List<Card> getHandList() {return hand.getHandlist();}

    //setters
    public String setTurnsToSkip(int turnsToSkip, int oldTurnsToSkip) {
        this.turnsToSkip = turnsToSkip+oldTurnsToSkip;
        return ("" + this.turnsToSkip+ 13);
    }
    public void setExtraTurns(int extraTurns, int oldTurns) {
        this.extraTurns = oldTurns + extraTurns;
    }

}
