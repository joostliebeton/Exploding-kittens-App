package ExplodingKittens;
import ExplodingKittens.Model.Deck;
import ExplodingKittens.Model.Card;
import ExplodingKittens.View.TUI;
import java.util.List;

public abstract class Player1 {
    String name;
    Hand hand;
    Game game;
    boolean isAlive;
    Deck deck;
    Card playedCard; // does this belong in this class? or do we put this in the game class?
    int extraTurns;
    int turnsToSkip;
    Deck discardPile;
    TUI tui;
    // do we want a target player in here? what is the function of a target player?
    // I think it might be more convinient to do this in game class? iterate over available players and then let the player choose which target to attack'.



public Player1(String name, Game game, Deck deck, Deck discardPile){
    this.name = name;
    this.game = game;
    this.deck = deck;
    this.hand = new Hand();
    this.isAlive = true;
    this.extraTurns = 0;
    this.turnsToSkip = 0;
    this.discardPile = discardPile;
    this.playedCard = null;
    this.tui = new TUI();

}
    //getters
    public String getName(){return this.name;}
    public Hand getHand(){return this.hand;}
    public Game getGame(){return this.game;} // if we implement that we can play more games at once this might be a handy getter
    public boolean getIsAlive(){return this.isAlive;}
    public Deck getDeck(){return this.deck;}
    public int getTurnsToSkip() {return turnsToSkip;}
    public int getExtraTurns() {return extraTurns;}
    public List<Card> getHandList() {return hand.getHandlist();}

    //setters
    public void setTurnsToSkip(int turnsToSkip, int oldTurnsToSkip) {
        this.turnsToSkip = turnsToSkip+oldTurnsToSkip;
    }
    public void setExtraTurns(int extraTurns, int oldTurns) {
        this.extraTurns = oldTurns + extraTurns;
    }
    public void setPlayedCard(Card card){this.playedCard = card;}
    public void setIsAlive(boolean isAlive){this.isAlive = isAlive;}



}
