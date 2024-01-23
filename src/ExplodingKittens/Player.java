package ExplodingKittens;

import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;
import ExplodingKittens.Model.Deck;
import ExplodingKittens.View.TUI;

import java.util.*;

public class Player {
    private static boolean nopeCardPlayed = false;
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
    public int getExtraTurns() {
        return extraTurns;
    }
    public void setExtraTurns(int extraTurns, int oldturns) {
        this.extraTurns = oldturns + extraTurns;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand.getHand();
    }





    private boolean hasCardType(CardType type) {
        for (Card card : getHand()) {
            if (card.getType() == type) {
                return true;
            }
        }
        return false;
    }

    private Card takeCard(CardType type) {
        for (Card card : getHand()) {
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






