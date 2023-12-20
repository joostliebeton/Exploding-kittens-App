package ExplodingKittens;

import java.util.ArrayList;
import java.util.HashMap;

import static ExplodingKittens.Card.Cards.*;
import static ExplodingKittens.Card.Cards.NOPE;

public class Deck {
    // cards: Arraylist<card>
    public Deck(){
        this.cards=new ArrayList<>();
        initializeDeck();
    }
    private void initializeDeck(){
        cardlibrary();
        for(Card.Cards card: amountsCardstype.keySet()){
            int amount = amountsCardstype.get(card);
            for(int i=1; i<=amount;i++){
                Card Card1 = new Card(i, card);
                cards.add(Card1);
                }
            }
    public static ArrayList<Card> playingDeck;
    public ArrayList<Card> beginDeck;

    public static ArrayList<Card> discardPile;
    public void setBeginDeck() {
        beginDeck.add(ExplodingKittens.Card.Cards.);

        this.beginDeck = beginDeck;
    }
    public ArrayList<Card> cards;
    public ArrayList<Card> discardPile;
    public ArrayList<Card> getCards() {
        return cards;
    }
    // shuffle() shuffles the cards
    public void shuffle(){
        //randomizes the cards order
    }
    //drawCard() takes a card from the top of the deck and removes from the deck
    public static Card drawCard(){
        // remove one card from playingDeck list.
        return Card;

//    }
    public void removeCard(){

    }
    public HashMap<Card.Cards, Integer> amountsCardstype = new HashMap<>();
    public void cardlibrary(){
        amountsCardstype.put(SHUFFLE,4);
        amountsCardstype.put(SKIP,4);
        amountsCardstype.put(SEETHEFUTURE, 5);
        amountsCardstype.put(FAVOR, 4);
        amountsCardstype.put(CATCARD1, 4);
        amountsCardstype.put(CATCARD2, 4);
        amountsCardstype.put(CATCARD3, 4);
        amountsCardstype.put(CATCARD4, 4);
        amountsCardstype.put(CATCARD5, 4);
        amountsCardstype.put(DEFUSE, 6);
        amountsCardstype.put(EXPLODINGKITTEN, 4);
        amountsCardstype.put(NOPE, 5);
        amountsCardstype.put(ATTACK, 4);
    }

    public static int pileSize(){
        return playingDeck.size();
    }
}
