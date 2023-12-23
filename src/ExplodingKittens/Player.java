package ExplodingKittens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Player {
    private String name;
    private List<Card> hand;
    private Game game;
    private Deck deck;
    private DiscardPile discardPile1 = new DiscardPile();
    public Player(String name , Game game, Deck deck) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.game = game;
        this.deck=deck;
    }
    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }public void drawCard(Deck deck) {Card drawnCard = deck.draw();if (drawnCard != null) {
        hand.add(drawnCard);
        System.out.println(name + " drew a " + drawnCard.getType() + " card.");
        if (drawnCard.getType() == CardType.EXPLODING_KITTEN) {
            // Check if the player has a Defuse card
            if (hasDefuseCard()) {
                // Player has a Defuse card, ask if they want to play it
                System.out.println(name + ", you drew an Exploding Kitten! Do you want to play a Defuse card? (yes/no)");
                Scanner scanner = new Scanner(System.in);
                String response = scanner.nextLine().toLowerCase();

                if (response.equals("yes")) {
                    // Player wants to play Defuse card
                    playCard(hand.indexOf(drawnCard), discardPile1);
                    System.out.println(name + " played a Defuse card.");
                } else {
                    // Player does not want to play Defuse card, eliminate them
                    eliminate();
                }
            } else {
                // Player does not have a Defuse card, eliminate them
                eliminate();
            }
        }
    }}

    private void eliminate() {
        game.eliminatePlayer(this);
    }

    private boolean hasDefuseCard() {
        for (Card card : hand) {
            if (card.getType() == CardType.DEFUSE) {
                hand.remove(card);
                return true;
            }
        }
        return false;
    }

    public void playCard(int cardIndex, DiscardPile discardPile1) {
        if (cardIndex >= 0 && cardIndex < hand.size() && hand.get(cardIndex).playable()) {
            Card playedCard = hand.remove(cardIndex);
            switch (playedCard.getType()){
                case NOPE:
                    playedCard.Nope();
                    break;
                case SHUFFLE:
                    shuffleDeck();
                    break;
                case SKIP:
                    playedCard.Skip();
                    break;
                case SEE_THE_FUTURE:
                    playedCard.SeeTheFuture();
                    break;
                case CAT_CARD1:
                    playedCard.CatCard();
                    break;
                case CAT_CARD2:
                    playedCard.CatCard();
                    break;
                case CAT_CARD3:
                    playedCard.CatCard();
                    break;
                case CAT_CARD4:
                    playedCard.CatCard();
                    break;
                case CAT_CARD5:
                    playedCard.CatCard();
                    break;
                case FAVOR:
                    playedCard.Favor();
                    break;
                case ATTACK:
                    playedCard.Attack();
                    break;
            }
            System.out.println(name + " played a " + playedCard.getType() + " card.");
            discardPile1.discardCard(playedCard);
            // Implement the specific action associated with the played card
            // For example, triggering a special ability or resolving effects
        } else {
            System.out.println("Invalid card index. Choose a card within the valid range.");

        }
    }
    public void shuffleDeck() {
        if (deck != null) {
            Card shuffleCard = new Card(CardType.SHUFFLE);
            shuffleCard.Shuffle(deck);
            System.out.println(name + " shuffled the deck.");
        } else {
            System.out.println("Error: Deck reference is null.");
        }
        // Delegate the shuffle to the Card class

    }
    }
