package ExplodingKittens;

import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;
import ExplodingKittens.View.TUI;

import java.util.ArrayList;
import java.util.Scanner;

public class Hand {
    private ArrayList<Card> hand;
    private TUI tui;
    public ArrayList<Card> getHandlist(){
        return hand;
    }
    public Hand getHand(){
        return this;
    }
    public Hand(){
        hand = new ArrayList<>();
        tui  = new TUI();
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
    public void catCardsInHand(CardType typeCard, Player player) {
        int catCardCount = 1;
        int deletedcardcount = 1;
        for (Card card : getHandlist()) {
            if (card.getType() == typeCard) {
                catCardCount++;
            }
        }
        tui.catCardsInHandMessage(player.getName(), typeCard, catCardCount);
        //System.out.println(name + " has " + catCardCount + " " + typeCard + " in hand.");
        if (catCardCount == 1) {
            tui.catCardsInHandMessage(typeCard, 1);
            //System.out.println("You need at least 1 more " + typeCard + " to play this card as an action card");
        } else if (catCardCount == 2) {
            tui.catCardsInHandMessage(typeCard, 2);
            //System.out.println("Do you want to play the 2 " + typeCard + " card? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("yes")) {
                player.getGame().twoCards(typeCard);
            } else if (response.equals("no")) {
                tui.catCardsInHandMessage(1);
                //System.out.println("oke continue");
            }

        }
        else if (catCardCount >= 3) {
            tui.catCardsInHandMessage(typeCard, 2);
            //System.out.println("Do you want to play the 2 " + typeCard + " card? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("yes")) {
                player.getGame().twoCards(typeCard);
            }
            else if (response.equals("no")) {
                tui.catCardsInHandMessage(typeCard, 3);
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
                    player.getGame().choosePlayerChoice();
                }
            }
        }
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
