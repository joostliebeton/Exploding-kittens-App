package ExplodingKittens.Model;

import ExplodingKittens.Controller.ProtocolMessages;
import ExplodingKittens.View.ClientTUI;

import java.util.ArrayList;
import java.util.Scanner;

public class Hand {
    private ArrayList<Card> hand;
    public ArrayList<Card> getHandlist(){
        return hand;
    }
    public Hand getHand(){
        return this;
    }
    public Hand(){
        hand = new ArrayList<>();
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
    public int indexOf(CardType type) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getType() == type) {
                return i;
            }
        }
        return -1;
    }

    public void remove(Card card) {
        hand.remove(card);
    }
    public void remove(int index) {
        hand.remove(index);
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
    public String catCardsInHand(CardType typeCard, Player1 player) {
        int catCardCount = 1;
        for (Card card : getHandlist()) {
            if (card.getType() == typeCard) {
                catCardCount++;
            }
        }
        //clientTui.catCardsInHandMessage(player.getName(), typeCard, catCardCount);
        //return(player.getName() + " has " + catCardCount + " " + typeCard + " in hand.");
        if (catCardCount == 1 ||  catCardCount == 2 || catCardCount ==3) {

            //clientTui.catCardsInHandMessage(typeCard, 1);
            return (ProtocolMessages.GENERAL_CARD_RESPONSE + ProtocolMessages.DELIMITER + typeCard);

        }
        return null;
   }

    public boolean hasDefuseCard() {
        for (Card card : hand) {
            if (card.getType() == CardType.DEFUSE) {
                hand.remove(card);
                return true;
            }
        }
        return false;
    }
}
