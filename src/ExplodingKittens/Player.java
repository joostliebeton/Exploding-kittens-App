package ExplodingKittens;

import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;
import ExplodingKittens.Model.Deck;
import ExplodingKittens.Model.DiscardPile;

import java.util.*;

public class Player {
    private static boolean nopeCardPlayed = false;
    private String name;
    private ArrayList<Card> hand;
    private Game game;
    private Deck deck;
    private Card playedCard;
    int extraTurns;
    int turnsToSkip;
    private Player targetPlayer;
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
                    //ask players if they want to play a nope card
                    game.askPlayersNope();
                    playedCard.Nope();
                    break;
                case SHUFFLE:
                    game.askPlayersNope();
                    if (getNopeCardPlayed()) {
                        System.out.println("Nope card negated the Shuffle!");
                        setNopeCardPlayed(false); // Reset Nope card flag
                    } else {
                        shuffleDeck();
                    }
                    shuffleDeck();
                    break;
                case SKIP:
                    game.askPlayersNope();
                    if (getNopeCardPlayed()) {
                        System.out.println("Nope card negated the Skip!");
                        setNopeCardPlayed(false); // Reset Nope card flag
                    } else {
                        turnsToSkip++;
                    }
                    break;
                case SEE_THE_FUTURE:
                    game.askPlayersNope();
                    SeeTheFuture();
                    break;
                case CAT_CARD1:
                    catCardsInHand(CardType.CAT_CARD1);
                    break;
                case CAT_CARD2:
                    catCardsInHand(CardType.CAT_CARD2);
                    break;
                case CAT_CARD3:
                    catCardsInHand(CardType.CAT_CARD3);
                    break;
                case CAT_CARD4:
                    catCardsInHand(CardType.CAT_CARD4);
                    break;
                case CAT_CARD5:
                    catCardsInHand(CardType.CAT_CARD5);
                    break;
                case FAVOR:
                    game.askPlayersNope();
                    chooseplayer();
                    break;
                case ATTACK:
                    game.askPlayersNope();
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
    private void catCardsInHand(CardType typecard) {
        int catCardCount = 1;
        int deletedcardcount = 1;
        for (Card card : hand) {
            if (card.getType() == typecard) {
                catCardCount++;
            }
        }

        System.out.println(name + " has " + catCardCount + " " + typecard + " in hand.");
        if (catCardCount == 1) {
            System.out.println("You need at least 1 more " + typecard + " to play this card as an action card");
        } else if (catCardCount == 2) {
            System.out.println("Do you want to play the 2 " + typecard + " card? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("yes")) {
                twoCards(typecard);
            } else if (response.equals("no")) {
                System.out.println("oke continue");
            }

        }
        else if (catCardCount >= 3) {
            System.out.println("Do you want to play the 2 " + typecard + " card? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("yes")) {
                twoCards(typecard);
            }
            else if (response.equals("no")) {
                System.out.println("do you want to play the 3 " + typecard + " card? (yes/no)");
                response = scanner.nextLine().toLowerCase();
                if (response.equals("yes")) {

                    while (deletedcardcount < 3) {
                        for (int i = 0; i < hand.size(); i++) {
                            if (hand.get(i).getType() == typecard) {
                                playedCard = hand.remove(i);
                                deletedcardcount++;
                            }
                        }
                    }
                   chooseplayerChoice();
                }
            }
        }
    }

    private void twoCards(CardType typecard) {
        int deletedcardcount=1;
        while (deletedcardcount < 2) {
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i).getType() == typecard) {
                    playedCard = hand.remove(i);
                    deletedcardcount++;
                }
            }
        }
        chooseplayer();
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
       if (this.extraTurns >1) {
            extraTurns = 0;
            targetPlayer.extraTurns += 3;
            System.out.println(name + " played an Attack card. " + targetPlayer.getName() + " will have " + targetPlayer.extraTurns + " extra turns.");
        } else{
            targetPlayer.extraTurns += 1;
            System.out.println(name + " played an Attack card. " + targetPlayer.getName() + " will have " + targetPlayer.extraTurns + " extra turns.");
        }
    }

    private void setNopeCardPlayed(boolean bool){
        this.nopeCardPlayed = bool;
    }

    private boolean getNopeCardPlayed() {
        return nopeCardPlayed;
    }

    public void SeeTheFuture(){
      if (!(deck.isEmpty())) {
            Card[] cards = deck.peek();
            System.out.println(name + " played a See the Future card. The top three cards are: ");
            for (Card card : cards) {
                System.out.println(card.getType());
            }
        } else {
            System.out.println("Error: Deck is empty.");
        }
    }
    public void chooseplayerChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a player to take a card from (enter the player index): ");
        for (int i = 0; i < game.getPlayers().size(); i++) {
            if (!Objects.equals(game.getPlayers().get(i).getName(), name)) {
                System.out.println(i + " " + game.getPlayers().get(i).getName());
            }
        }
        int playerIndex = scanner.nextInt();
        if (playerIndex < game.getPlayers().size()) {
            Player targetPlayer = game.getPlayers().get(playerIndex);
            favorChoice(targetPlayer);
        } else {
            System.out.println("Invalid player index. Choose a player within the valid range.");
        }
    }
    public void favorChoice(Player targetPlayer) {
        // Ask the user to choose a card type
        System.out.println(name + ", you played a Favor card. Choose a card type to request from the other player: ");
        System.out.println("1: EXPLODING_KITTEN, 2: DEFUSE, 3: NOPE, 4: SKIP, 5: ATTACK, 6:  SEE_THE_FUTURE, 7:FAVOR, 8: SHUFFLE, 9: CAT_CARD1, 10: CAT_CARD2, 11: CAT_CARD3, 12: CAT_CARD4, 13: CAT_CARD5 ");

        Scanner scanner = new Scanner(System.in);
        int chosenType = scanner.nextInt();

        // Check if the target player has the requested card type
        CardType requestedType = CardType.values()[chosenType - 1];
        if (targetPlayer.hasCardType(requestedType)) {
            // Target player has the requested card type, take a card from them
            Card takenCard = targetPlayer.takeCard(requestedType);
            hand.add(takenCard);
            assert takenCard != null;
            System.out.println(name + " took a " + takenCard.getType() + " card from " + targetPlayer.getName() + ".");
        } else {
            // Target player doesn't have the requested card type
            System.out.println(targetPlayer.getName() + " doesn't have the requested card type. You get nothing.");
        }
    }

    private boolean hasCardType(CardType type) {
        for (Card card : hand) {
            if (card.getType() == type) {
                return true;
            }
        }
        return false;
    }

    private Card takeCard(CardType type) {
        for (Card card : hand) {
            if (card.getType() == type) {
                this.hand.remove(card);
                return card;
            }
        }
        return null;
    }

    public void chooseplayer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a player to take a card from (enter the player index): ");
        for (int i = 0; i < game.getPlayers().size(); i++) {
            if (!Objects.equals(game.getPlayers().get(i).getName(), name)) {
                System.out.println(i +" "+ game.getPlayers().get(i).getName());
            }
        }

    int playerIndex = scanner.nextInt();
    if (playerIndex < game.getPlayers().size()) {
        Player targetPlayer = game.getPlayers().get(playerIndex);
        favor(targetPlayer);
    } else {
            System.out.println("Invalid player index. Choose a player within the valid range.");
        }
    }
    public void favor(Player targetPlayer) {
        System.out.println(name + " played a Favor card. " + targetPlayer.getName() + " must give you a card.");
        targetPlayer.giveCard(this);
    }

    private void giveCard(Player player) {
        Scanner scanner = new Scanner(System.in);
        int cardIndex;

        do {
            System.out.println(name + ", choose a card to give to " + player.getName() + " (enter the card index): ");
            for (int i = 0; i < hand.size(); i++) {
                System.out.println(i + ": " + hand.get(i).getType());
            }
            cardIndex = scanner.nextInt();

            // Check if the entered index is valid
            if (cardIndex < 0 || cardIndex >= hand.size()) {
                 System.out.println("Invalid card index. Please try again.");
            }
        } while (cardIndex < 0 || cardIndex >= hand.size());

        Card card = hand.remove(cardIndex);
        player.hand.add(card);
        System.out.println(name + " gave " + player.getName() + " a " + card.getType() + " card.");
    }

    public void askNope() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to play a Nope card? (yes/no)");
        String response = scanner.nextLine().toLowerCase();
        if (response.equals("yes")) {
            // Player wants to play Nope card
            playCard(hand.indexOf(playedCard), discardPile1);
            System.out.println(name + " played a Nope card.");
            setNopeCardPlayed(true);
        } else {
            // Player does not want to play Nope card
            System.out.println(name + " did not play a Nope card.");
        }
    }
}






