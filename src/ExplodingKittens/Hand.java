package ExplodingKittens;

import ExplodingKittens.Model.Card;

import java.util.ArrayList;

public class Hand {
    public ArrayList<Card> hand;
    public ArrayList<Card> getHand(){
        return hand;
    }
    public void setHand(ArrayList<Card> hand){
        this.hand = hand;
    }
    public void addCard(Card card){
        hand.add(card);
    }

    public int indexOf(Card drawnCard) {
        return hand.indexOf(drawnCard);
    }

    public void remove(Card card) {
        hand.remove(card);
    }

    public int size() {
        return hand.size();
    }

    public Card get(int cardIndex) {
        return hand.get(cardIndex);
    }

    public void add(Card takenCard) {
        hand.add(takenCard);
    }
}
