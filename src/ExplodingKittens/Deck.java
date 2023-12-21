package ExplodingKittens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {
        for (CardType type : CardType.values()) {
            if (type == CardType.EXPLODING_KITTEN) {
                continue;
            }
            for (int i = 0; i < getInitialCardCount(type); i++) {
                cards.add(new Card(type));
            }
        }
    }

    private int getInitialCardCount(CardType type) {
        // Return the initial count for each card type
        // You can customize this based on the rules of the game
        switch (type) {
            case EXPLODING_KITTEN:
                return 4;
            case DEFUSE:
                return 6;
            case SKIP:
                return 4;
            case ATTACK:
                return 4;
            case FAVOR:
                return 4;
            case SEE_THE_FUTURE:
                return 5;
            case SHUFFLE:
                return 4;
            case NOPE:
                return 5;
            case CAT_CARD1, CAT_CARD2, CAT_CARD3, CAT_CARD4, CAT_CARD5:
                return 4;
            // Add cases for other card types
            default:
                return 4;
        }
    }

    public void shuffle() {
        Random random = new Random();
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swapCards(i, j);
        }
        Collections.shuffle(cards);
    }

    private void swapCards(int i, int j) {
        Card temp = cards.get(i);
        cards.set(i, cards.get(j));
        cards.set(j, temp);
    }

    public Card draw() {
        if (!isEmpty()) {
            return cards.remove(0);
        }
        return null; // Deck is empty
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }


    public ArrayList<Card> getCards() {
        return cards;
    }
    // shuffle() shuffles the cards

    //drawCard() takes a card from the top of the deck and removes from the deck

    public int pileSize(){
        return cards.size();
    }

    public void addExplodingKitten() {
        cards.add(new Card(CardType.EXPLODING_KITTEN));
    }

    public Card[] getDeck() {
        return cards.toArray(new Card[0]);
    }
}
