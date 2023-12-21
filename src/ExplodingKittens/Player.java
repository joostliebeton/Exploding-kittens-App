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

        public void playCard(Card card, DiscardPile discardPile) {
            if (hand.contains(card)) {
                hand.remove(card);
                System.out.println(name + " played a " + card.getType() + " card.");
                discardPile.discardCard(card);

            }
            // Implement card-playing logic based on the rules of the game
            // This can involve checking if the card is playable and updating the game state
            // Special card functionality can be added here
        }
    }
