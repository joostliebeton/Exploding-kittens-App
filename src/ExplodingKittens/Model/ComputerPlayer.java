package ExplodingKittens.Model;

import ExplodingKittens.Controller.Game;
import ExplodingKittens.Controller.ProtocolMessages;

import java.util.List;

public class ComputerPlayer extends Player1 {
    public ComputerPlayer(String name, Game game){
        super(name,game);

    }


    public Game getGame(){
        return game;
    }
    public int getTurnsToSkip() {
        return turnsToSkip;
    }
    public String setTurnsToSkip(int turnsToSkip, int oldturnstoskip) {
        this.turnsToSkip = turnsToSkip+oldturnstoskip;
        return "" + this.turnsToSkip;
    }
    public int getExtraTurns() {
        return extraTurns;
    }
    public void setExtraTurns(int extraTurns, int oldturns) {
        this.extraTurns = oldturns + extraTurns;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHandList() {
        return hand.getHandlist();
    }
    public Hand getHand() {
        return hand;
    }
    public void setPlayedCard(Card card) {
        this.playedCard = card;
    }
    public Card getPlayedCard(){
        return playedCard;
    }

    // create ComputerPlayer specific methods here:
    // such as "strategy" so that the computer plays on his own
}
