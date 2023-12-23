package ExplodingKittens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private Game game;
    private Deck deck;
    private Card playedCard;
    int extraTurns;
    int turnsToSkip;
    private DiscardPile discardPile1 = new DiscardPile();
    public Player(String name , Game game, Deck deck) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.game = game;
        this.deck=deck;
        this.playedCard = null;
        extraTurns = 0;
    }
    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }
    public Card getPlayedCard() {
        return playedCard;
    }
    public void drawCard(Deck deck) {
        Card drawnCard = deck.draw();
        if (drawnCard != null) {
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
    }
    }

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
            playedCard = hand.remove(cardIndex);
            System.out.println(name + " played a " + playedCard.getType() + " card.");
            discardPile1.discardCard(playedCard);
            switch (playedCard.getType()) {
                case NOPE:
                    playedCard.Nope();

                    break;
                case SHUFFLE:
                    shuffleDeck();

                    break;
                case SKIP:
                    playedCard.Skip();
                    turnsToSkip =1;
                    break;
                case SEE_THE_FUTURE:
                    playedCard.SeeTheFuture();

                    break;
                case CAT_CARD1, CAT_CARD2, CAT_CARD3, CAT_CARD4, CAT_CARD5:
                    playedCard.CatCard();

                    break;
                case FAVOR:
                    playedCard.Favor();

                    break;
                case ATTACK:
                    turnsToSkip++;
                    Attack(game.getNextPlayer());

                    break;
            }

            // Implement the specific action associated with the played card
            // For example, triggering a special ability or resolving effects
        } else {
            System.out.println("Invalid card index. Choose a card within the valid range.");

        }
    }
    public void shuffleDeck() {
        if (deck != null) {
            playedCard.Shuffle(deck);
            System.out.println(name + " shuffled the deck.");
        } else {
            System.out.println("Error: Deck reference is null.");
        }
    }
        // Delegate the shuffle to the Card class
        public void Attack(Player targetPlayer) {
            targetPlayer.extraTurns +=1;
            System.out.println(name + " played an Attack card. " + targetPlayer.getName() + " will have two turns.");
        }

    }


//    public void SeeTheFuture(){
//        Card[] cards = deck.peek();
//        System.out.println(name + " played a See the Future card. The top three cards are: ");
//        for (Card card : cards) {
//            System.out.println(card.getType());
//        }
//    }



