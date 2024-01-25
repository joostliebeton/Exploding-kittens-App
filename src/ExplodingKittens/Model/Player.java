package ExplodingKittens.Model;

import ExplodingKittens.Controller.Game;
import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;
import ExplodingKittens.Model.Hand;

import java.util.*;

public class Player {


    private String name;
    private Hand hand;
    private Game game;

    private Card playedCard;
    private int extraTurns;
    private int turnsToSkip;

    public Player(String name , Game game) {
        this.name = name;
        this.hand = new Hand();
        this.game = game;
        extraTurns = 0;
    }
    public Game getGame(){
        return game;
    }
    public int getTurnsToSkip() {
        return turnsToSkip;
    }
    public void setTurnsToSkip(int turnsToSkip, int oldturnstoskip) {
        this.turnsToSkip = turnsToSkip+oldturnstoskip;
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







    public Card takeCard(CardType type) {
        for (Card card : getHandList()) {
            if (card.getType() == type) {
                this.hand.remove(card);
                return card;
            }
        }
        return null;
    }




    public void setPlayedCard(Card card) {
        this.playedCard = card;
    }
    public Card getPlayedCard(){
        return playedCard;
    }
}






