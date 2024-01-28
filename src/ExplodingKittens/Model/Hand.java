package ExplodingKittens.Model;

import ExplodingKittens.View.ClientTUI;

import java.util.ArrayList;
import java.util.Scanner;

public class Hand {
    private ArrayList<Card> hand;
    private ClientTUI clientTui;
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
    public String catCardsInHand(CardType typeCard, Player1 player) {
        int catCardCount = 1;
        int deletedcardcount = 1;
        for (Card card : getHandlist()) {
            if (card.getType() == typeCard) {
                catCardCount++;
            }
        }
        //clientTui.catCardsInHandMessage(player.getName(), typeCard, catCardCount);
        //return(player.getName() + " has " + catCardCount + " " + typeCard + " in hand.");
        if (catCardCount == 1) {

            //clientTui.catCardsInHandMessage(typeCard, 1);
            return ("You need at least 1 more " + typeCard + " to play this card as an action card");
        } else if (catCardCount == 2) {
            clientTui.catCardsInHandMessage(typeCard, 2);
            //System.out.println("Do you want to play the 2 " + typeCard + " card? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("yes")) {
                //command to server to play the 2 cat card
                player.getGame().twoCards(typeCard);
            } else if (response.equals("no")) {
                clientTui.catCardsInHandMessage(1);
                //playcardcmd for the 1 cat card
                //System.out.println("oke continue");
            }

        }
        else if (catCardCount >= 3) {
            //clientTui.catCardsInHandMessage(typeCard, 2);
            //System.out.println("Do you want to play the 2 " + typeCard + " card? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("yes")) {
                player.getGame().twoCards(typeCard);
                //command to server to play the 2 cat card
            }
            else if (response.equals("no")) {
                clientTui.catCardsInHandMessage(typeCard, 3);
                //System.out.println("do you want to play the 3 " + typeCard + " card? (yes/no)");
                response = scanner.nextLine().toLowerCase();
                if (response.equals("yes")) {

                    while (deletedcardcount < 3) {
                        for (int i = 0; i < hand.size(); i++) {
                            if (hand.get(i).getType() == typeCard) {
                                player.setPlayedCard(hand.get(i));
                                hand.remove(player.getPlayedCard());
                                deletedcardcount++;
                            }
                        }
                    }
                    //command to server to play the 3 cat card
                    player.getGame().choosePlayerChoice();
                }
            }
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
    public boolean hasCardType(CardType type) {
        for (Card card : hand) {
            if (card.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
