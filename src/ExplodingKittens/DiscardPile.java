package ExplodingKittens;
import java.util.ArrayList;
import java.util.List;

public class DiscardPile {
    public Card[] get;
    private List<Card> discardedCards;

    public DiscardPile() {
        discardedCards = new ArrayList<>();
    }

    public void discardCard(Card card) {
        discardedCards.add(card);
        System.out.println("Card discarded: " + card.getType());
    }

    public Card[] getDiscardPile() {
        return discardedCards.toArray(new Card[0]);
    }

    public int length() {
        return discardedCards.size();
    }
}