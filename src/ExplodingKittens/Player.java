package ExplodingKittens;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
        private List<Card> hand;

        public Player(String name) {
            this.name = name;
            this.hand = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public List<Card> getHand() {
            return hand;
        }

        public void drawCard(Deck deck) {
            Card drawnCard = deck.draw();
            if (drawnCard != null) {
                hand.add(drawnCard);
                System.out.println(name + " drew a " + drawnCard.getType() + " card.");
            }
        }

    public void playCard(int cardIndex, DiscardPile discardPile) {
        if (cardIndex >= 0 && cardIndex < hand.size()) {
            Card playedCard = hand.remove(cardIndex);
            System.out.println(name + " played a " + playedCard.getType() + " card.");
            discardPile.discardCard(playedCard);
            // Implement the specific action associated with the played card
            // For example, triggering a special ability or resolving effects
        } else {
            System.out.println("Invalid card index. Choose a card within the valid range.");

        }
    }
    }
