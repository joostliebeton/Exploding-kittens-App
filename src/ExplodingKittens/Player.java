package ExplodingKittens;

import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;
import ExplodingKittens.Model.Deck;
import ExplodingKittens.Model.DiscardPile;
import ExplodingKittens.View.TUI;

import java.util.*;

public class Player {
    private static boolean nopeCardPlayed = false;
    private String name;
    private Hand hand;
    private Game game;
    private Deck deck;
    private Card playedCard;
    int extraTurns;
    int turnsToSkip;
    private Player targetPlayer;
    private DiscardPile discardPile1 = new DiscardPile();
    public Player(String name , Game game, Deck deck) {
        this.name = name;
        this.hand = new Hand();
        this.game = game;
        this.deck=deck;
        this.playedCard = null;
        extraTurns = 0;
    }
    public int getExtraTurns() {
        return extraTurns;
    }
    public void setExtraTurns(int extraTurns, int oldturns) {
        this.extraTurns = oldturns + extraTurns;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand.getHand();
    }
    public void drawCard(Deck deck) {
        Card drawnCard = deck.draw();
        if (drawnCard != null) {
        hand.addCard(drawnCard);
            TUI.drawCardMessage(name, drawnCard);
        //System.out.println(name + " drew a " + drawnCard.getType() + " card.");
        if (drawnCard.getType() == CardType.EXPLODING_KITTEN) {
            // Check if the player has a Defuse card
            if (hasDefuseCard()) {
                // Player has a Defuse card, ask if they want to play it
                TUI.drawCardMessage(name, 1);
                //System.out.println(name + ", you drew an Exploding Kitten! Do you want to play a Defuse card? (yes/no)");
                Scanner scanner = new Scanner(System.in);
                String response = scanner.nextLine().toLowerCase();

                if (response.equals("yes")) {
                    // Player wants to play Defuse card
                    playCard(hand.indexOf(drawnCard), discardPile1);
                    TUI.drawCardMessage(name, 2);
                    //System.out.println(name + " played a Defuse card.");
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
        for (Card card : getHand()) {
            if (card.getType() == CardType.DEFUSE) {
                hand.remove(card);
                return true;
            }
        }
        return false;
    }

    public void playCard(int cardIndex, DiscardPile discardPile1) {
        if (cardIndex >= 0 && cardIndex < hand.size() && hand.get(cardIndex).playable()) {
            playedCard = hand.get(cardIndex);
            hand.remove(playedCard);
            TUI.playCardMessage(name, playedCard);
            //System.out.println(name + " played a " + playedCard.getType() + " card.");
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
                        TUI.playCardMessage(1);
                        //System.out.println("Nope card negated the Shuffle!");
                        setNopeCardPlayed(false); // Reset Nope card flag
                    } else {
                        playedCard.Shuffle(deck, this);
                    }
                    break;
                case SKIP:
                    game.askPlayersNope();
                    if (getNopeCardPlayed()) {
                        TUI.playCardMessage(2);
                        //System.out.println("Nope card negated the Skip!");
                        setNopeCardPlayed(false); // Reset Nope card flag
                    } else {
                        turnsToSkip++;
                    }
                    break;
                case SEE_THE_FUTURE:
                    game.askPlayersNope();
                    playedCard.seeTheFuture(deck, this);
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
                    playedCard.Attack(this, game.getNextPlayer());
                    break;
            }
            // Implement the specific action associated with the played card
            // For example, triggering a special ability or resolving effects
        } else {
            TUI.playCardMessage(3);
            //System.out.println("Invalid card index. Choose a card within the valid range.");

        }
    }
    private void catCardsInHand(CardType typeCard) {
        int catCardCount = 1;
        int deletedcardcount = 1;
        for (Card card : getHand()) {
            if (card.getType() == typeCard) {
                catCardCount++;
            }
        }
        TUI.catCardsInHandMessage(name, typeCard, catCardCount);
        //System.out.println(name + " has " + catCardCount + " " + typeCard + " in hand.");
        if (catCardCount == 1) {
            TUI.catCardsInHandMessage(typeCard, 1);
            //System.out.println("You need at least 1 more " + typeCard + " to play this card as an action card");
        } else if (catCardCount == 2) {
            TUI.catCardsInHandMessage(typeCard, 2);
            //System.out.println("Do you want to play the 2 " + typeCard + " card? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("yes")) {
                twoCards(typeCard);
            } else if (response.equals("no")) {
                TUI.catCardsInHandMessage(1);
                //System.out.println("oke continue");
            }

        }
        else if (catCardCount >= 3) {
            TUI.catCardsInHandMessage(typeCard, 2);
            //System.out.println("Do you want to play the 2 " + typeCard + " card? (yes/no)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().toLowerCase();
            if (response.equals("yes")) {
                twoCards(typeCard);
            }
            else if (response.equals("no")) {
                TUI.catCardsInHandMessage(typeCard, 3);
                //System.out.println("do you want to play the 3 " + typeCard + " card? (yes/no)");
                response = scanner.nextLine().toLowerCase();
                if (response.equals("yes")) {

                    while (deletedcardcount < 3) {
                        for (int i = 0; i < hand.size(); i++) {
                            if (hand.get(i).getType() == typeCard) {
                                playedCard = hand.get(i);
                                hand.remove(playedCard);
                                deletedcardcount++;
                            }
                        }
                    }
                   choosePlayerChoice();
                }
            }
        }
    }

    private void twoCards(CardType typecard) {
        int deletedcardcount=1;
        while (deletedcardcount < 2) {
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i).getType() == typecard) {
                    playedCard = hand.get(i);
                    hand.remove(playedCard);
                    deletedcardcount++;
                }
            }
        }
        chooseplayer();
    }
        // Delegate the shuffle to the Card class

    private void setNopeCardPlayed(boolean bool){
        this.nopeCardPlayed = bool;
    }

    private boolean getNopeCardPlayed() {
        return nopeCardPlayed;
    }

    public void choosePlayerChoice() {
        Scanner scanner = new Scanner(System.in);
        TUI.choosePlayerChoiceMessage(1);
        //System.out.println("Choose a player to take a card from (enter the player index): ");
        for (int i = 0; i < game.getPlayers().size(); i++) {
            if (!Objects.equals(game.getPlayers().get(i).getName(), name)) {
                TUI.choosePlayerChoiceMessage(game, i);
                //System.out.println(i + " " + game.getPlayers().get(i).getName());
            }
        }
        int playerIndex = scanner.nextInt();
        if (playerIndex < game.getPlayers().size()) {
            Player targetPlayer = game.getPlayers().get(playerIndex);
            favorChoice(targetPlayer);
        } else {
            TUI.choosePlayerChoiceMessage(2);
            //System.out.println("Invalid player index. Choose a player within the valid range.");
        }
    }
    public void favorChoice(Player targetPlayer) {
        // Ask the user to choose a card type
        TUI.favorChoiceMessage(name);
        //System.out.println(name + ", you played a Favor card. Choose a card type to request from the other player: ");
        TUI.favorChoiceMessage(1);
        //System.out.println("1: EXPLODING_KITTEN, 2: DEFUSE, 3: NOPE, 4: SKIP, 5: ATTACK, 6:  SEE_THE_FUTURE, 7:FAVOR, 8: SHUFFLE, 9: CAT_CARD1, 10: CAT_CARD2, 11: CAT_CARD3, 12: CAT_CARD4, 13: CAT_CARD5 ");

        Scanner scanner = new Scanner(System.in);
        int chosenType = scanner.nextInt();

        // Check if the target player has the requested card type
        CardType requestedType = CardType.values()[chosenType - 1];
        if (targetPlayer.hasCardType(requestedType)) {
            // Target player has the requested card type, take a card from them
            Card takenCard = targetPlayer.takeCard(requestedType);
            hand.add(takenCard);
            assert takenCard != null;
            TUI.favorChoiceMessage(name, targetPlayer, takenCard, 1);
            //System.out.println(name + " took a " + takenCard.getType() + " card from " + targetPlayer.getName() + ".");
        } else {
            // Target player doesn't have the requested card type
            TUI.favorChoiceMessage(name, targetPlayer, null, 2); // check whether gives correct output, not sure if null is correctly implemented
            //System.out.println(targetPlayer.getName() + " doesn't have the requested card type. You get nothing.");
        }
    }

    private boolean hasCardType(CardType type) {
        for (Card card : getHand()) {
            if (card.getType() == type) {
                return true;
            }
        }
        return false;
    }

    private Card takeCard(CardType type) {
        for (Card card : getHand()) {
            if (card.getType() == type) {
                this.hand.remove(card);
                return card;
            }
        }
        return null;
    }

    public void chooseplayer() {
        Scanner scanner = new Scanner(System.in);
        TUI.choosePlayerMessage(1);
        //System.out.println("Choose a player to take a card from (enter the player index): ");
        for (int i = 0; i < game.getPlayers().size(); i++) {
            if (!Objects.equals(game.getPlayers().get(i).getName(), name)) {
                TUI.choosePlayerMessage(game, i);
                //System.out.println(i +" "+ game.getPlayers().get(i).getName());
            }
        }

    int playerIndex = scanner.nextInt();
    if (playerIndex < game.getPlayers().size()) {
        Player targetPlayer = game.getPlayers().get(playerIndex);
        favor(targetPlayer);
    } else {
        TUI.choosePlayerMessage(2);
            //System.out.println("Invalid player index. Choose a player within the valid range.");
        }
    }
    public void favor(Player targetPlayer) {
        TUI.favorMessage(name, targetPlayer);
        //System.out.println(name + " played a Favor card. " + targetPlayer.getName() + " must give you a card.");
        targetPlayer.giveCard(this);
    }

    private void giveCard(Player player) {
        Scanner scanner = new Scanner(System.in);
        int cardIndex;

        do {
            TUI.giveCardMessage(name, player);
            //System.out.println(name + ", choose a card to give to " + player.getName() + " (enter the card index): ");
            for (int i = 0; i < hand.size(); i++) {
                TUI.giveCardMessage(i, player);
                //System.out.println(i + ": " + hand.get(i).getType());
            }
            cardIndex = scanner.nextInt();

            // Check if the entered index is valid
            if (cardIndex < 0 || cardIndex >= hand.size()) {
                TUI.giveCardMessage(1);
                 //System.out.println("Invalid card index. Please try again.");
            }
        } while (cardIndex < 0 || cardIndex >= hand.size());
        Card card = hand.get(cardIndex);
        hand.remove(card);
        player.hand.add(card);
        TUI.giveCardMessage(name, player, card);
        //System.out.println(name + " gave " + player.getName() + " a " + card.getType() + " card.");
    }

    public void askNope() {
        Scanner scanner = new Scanner(System.in);
        TUI.askNopeMessage(1);
        //System.out.println("Do you want to play a Nope card? (yes/no)");
        String response = scanner.nextLine().toLowerCase();
        if (response.equals("yes")) {
            // Player wants to play Nope card
            playCard(hand.indexOf(playedCard), discardPile1);
            TUI.askNopeMessage(name, 1);
            //System.out.println(name + " played a Nope card.");
            setNopeCardPlayed(true);
        } else {
            // Player does not want to play Nope card
            TUI.askNopeMessage(name, 2);
            //System.out.println(name + " did not play a Nope card.");
        }
    }
}






