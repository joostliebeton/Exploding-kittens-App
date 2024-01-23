package ExplodingKittens;


import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;
import ExplodingKittens.Model.Deck;
import ExplodingKittens.View.TUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Game {
    //players: Arraylist<player>
    private boolean nopeCardPlayed = false;

    private Deck deck;
    private Player currentPlayer;
    private ArrayList<Player> eliminatedplayers = new ArrayList<>();
    private List<Player> players;
    private Deck discardPile;
    public Card playedCard;

    public List<Player> getPlayers() {
        return players;
    }
    public TUI tui;
    private int currentPlayerIndex;


    public Game(String[] Players) {
        tui = new TUI();
        initializeDeck();
        initializePlayers(Players);
        initilializehands();
//        initializeDiscardPile();

        currentPlayerIndex = 0;
    }
    public Game getGame(){
        return this;
    }

    private void initializeDeck() {
        deck = new Deck();
        deck.initializeDeck();
        discardPile = new Deck();
        // Add Exploding Kittens to the deck after initial cards have been dealt

    }

    private void initilializehands() {
        for (int i = 0; i < /*player.size()*/ 5; i++) {
            for (Player player : players) {
                drawCard(deck, player);
            }
        }
        for (int i = 0; i < 4; i++) {
            deck.addExplodingKitten();
        }
        deck.shuffle();
    }

    private void initializePlayers(String[] names) {
        players = new ArrayList<>();
        for (String name : names) {
            players.add(new Player(name, this));
        }
    }

    public void turn() {
        currentPlayer = getCurrentPlayer();
        while (currentPlayer.getExtraTurns() > 0) {
            tui.turnMessage(currentPlayer, 1);
            //System.out.println(currentPlayer.getName() + " has " + currentPlayer.extraTurns + " extra turns!");
            playCard(getPlayerInput(), discardPile);
            if (discardPile.getDeck()[discardPile.length() - 1].getType() == CardType.ATTACK) {
                currentPlayer.setExtraTurns(0,0);
                currentPlayer.setTurnsToSkip(1, currentPlayer.getTurnsToSkip());
                Player targetPlayer = getNextPlayer();
                targetPlayer.setExtraTurns(3, targetPlayer.getExtraTurns());
                tui.turnMessage(targetPlayer, 2);
                //System.out.println(targetPlayer.getName() + " has " + targetPlayer.extraTurns + " extra turns!");
                handleTurnEnd();
                return;
            }
            currentPlayer.setExtraTurns(-1, currentPlayer.getExtraTurns());
            drawCard(deck, currentPlayer);
        }
        playCard(getPlayerInput(), discardPile);
        Scanner scanner = new Scanner(System.in);
        tui.turnMessage(1);
        //System.out.println("Do you want to end your turn? (yes/no)");
        String response = scanner.nextLine().toLowerCase();
        if (response.equals("yes")) {
            handleTurnEnd();
        } else {
            turn();
        }
    }

    private void handleTurnEnd() {
        if (currentPlayer.getTurnsToSkip() > 0) {
            tui.handleTurnEndMessage(currentPlayer);
            //System.out.println(currentPlayer.getName() + " skips a turn.");
            currentPlayer.setTurnsToSkip(-1, currentPlayer.getTurnsToSkip());
            endTurnNoDraw();
        } else {
            endTurn();
        }
    }
    public void choosePlayerChoice() {
        Scanner scanner = new Scanner(System.in);
        tui.choosePlayerChoiceMessage(1);
        //System.out.println("Choose a player to take a card from (enter the player index): ");
        for (int i = 0; i < this.getPlayers().size(); i++) {
            if (!Objects.equals(this.getPlayers().get(i).getName(), currentPlayer.getName())) {
                tui.choosePlayerChoiceMessage(this, i);
                //System.out.println(i + " " + game.getPlayers().get(i).getName());
            }
        }
        int playerIndex = scanner.nextInt();
        if (playerIndex < this.getPlayers().size()) {
            Player targetPlayer = this.getPlayers().get(playerIndex);
            favorChoice(targetPlayer);
        } else {
            tui.choosePlayerChoiceMessage(2);
            //System.out.println("Invalid player index. Choose a player within the valid range.");
        }
    }
    public void favorChoice(Player targetPlayer) {
        // Ask the user to choose a card type
        tui.favorChoiceMessage(currentPlayer.getName());
        //System.out.println(name + ", you played a Favor card. Choose a card type to request from the other player: ");
        tui.favorChoiceMessage(1);
        //System.out.println("1: EXPLODING_KITTEN, 2: DEFUSE, 3: NOPE, 4: SKIP, 5: ATTACK, 6:  SEE_THE_FUTURE, 7:FAVOR, 8: SHUFFLE, 9: CAT_CARD1, 10: CAT_CARD2, 11: CAT_CARD3, 12: CAT_CARD4, 13: CAT_CARD5 ");

        Scanner scanner = new Scanner(System.in);
        int chosenType = scanner.nextInt();

        // Check if the target player has the requested card type
        CardType requestedType = CardType.values()[chosenType - 1];
        if (targetPlayer.getHand().hasCardType(requestedType)) {
            // Target player has the requested card type, take a card from them
            Card takenCard = targetPlayer.takeCard(requestedType);
            currentPlayer.getHand().add(takenCard);
            assert takenCard != null;
            tui.favorChoiceMessage(currentPlayer.getName(), targetPlayer, takenCard, 1);
            //System.out.println(name + " took a " + takenCard.getType() + " card from " + targetPlayer.getName() + ".");
        } else {
            // Target player doesn't have the requested card type
            tui.favorChoiceMessage(currentPlayer.getName(), targetPlayer, null, 2); // check whether gives correct output, not sure if null is correctly implemented
            //System.out.println(targetPlayer.getName() + " doesn't have the requested card type. You get nothing.");
        }
    }

    private int getPlayerInput() {
        Scanner scanner = new Scanner(System.in);
        int cardIndex;

        do {
            tui.getPlayerInputMessage(currentPlayer);
            //System.out.println(currentPlayer.getName() + ", choose a card to play (enter the card index): ");
            for (int i = 0; i < currentPlayer.getHand().size(); i++) {

                tui.outputPlayerHand(currentPlayer, i);
                //System.out.println(i + ": " + currentPlayer.getHand().get(i).getType());
            }
            cardIndex = scanner.nextInt();

            // Check if the entered index is valid
            if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size()) {
                tui.getPlayerInputMessage(1);
                //System.out.println("Invalid card index. Please try again.");
            }
        } while (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size());

        return cardIndex;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getNextPlayer() {
        return players.get((currentPlayerIndex + 1) % players.size());
    }

    public void endTurn() {
        drawCard(deck, currentPlayer);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public void endTurnNoDraw() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

    }

    boolean isGameOver() {
        return players.size() == 1;
    }

    // Other methods as needed
    public void getDiscardPile() {
        for (Card card : discardPile.getDeck()) {
            tui.getDiscardPileMessage(card);
            //System.out.println(card.getType());
        }
    }

    public void getDeck() {
        for (Card card : deck.getDeck()) {
            tui.getDeckMessage(card);
            //System.out.println(card.getType());
        }
    }

    public void eliminatePlayer(Player player) {
        players.remove(player);
        eliminatedplayers.add(player);
        tui.eliminatePlayerMessage(player);
        //System.out.println(player.getName() + " has been eliminated!");
        currentPlayerIndex = (currentPlayerIndex - 1) % players.size();
    }

    public ArrayList<Player> getEliminatedPlayers() {
        return eliminatedplayers;
    }

    public void askPlayersNope() {
        for (Player player : players) {
            if (player != currentPlayer) {
                int nopecardindex =0;
                for(Card card : player.getHand().getHandlist()){
                    nopecardindex++;
                    if(card.getType() == CardType.NOPE){
                        askNope(player, nopecardindex);
                        return;
                    }
                }
            }
        }
    }
    public void drawCard(Deck deck, Player player) {
        Card drawnCard = deck.draw();
        if (drawnCard != null) {
            player.getHand().addCard(drawnCard);
            tui.drawCardMessage(player.getName(), drawnCard);
            //System.out.println(name + " drew a " + drawnCard.getType() + " card.");
            if (drawnCard.getType() == CardType.EXPLODING_KITTEN) {
                // Check if the player has a Defuse card
                if (player.getHand().hasDefuseCard()) {
                    // Player has a Defuse card, ask if they want to play it
                    tui.drawCardMessage(player.getName(), 1);
                    //System.out.println(name + ", you drew an Exploding Kitten! Do you want to play a Defuse card? (yes/no)");
                    Scanner scanner = new Scanner(System.in);
                    String response = scanner.nextLine().toLowerCase();

                    if (response.equals("yes")) {
                        // Player wants to play Defuse card
                        playCard(currentPlayer.getHand().indexOf(drawnCard), discardPile);
                        tui.drawCardMessage(currentPlayer.getName(), 2);
                        //System.out.println(name + " played a Defuse card.");
                    } else {
                        // Player does not want to play Defuse card, eliminate them
                        eliminatePlayer(currentPlayer);
                    }
                } else {
                    // Player does not have a Defuse card, eliminate them
                    eliminatePlayer(currentPlayer);
                }
            }
        }

    }
    public void askNope(Player player, int index) {
        Scanner scanner = new Scanner(System.in);
        tui.askNopeMessage(1);
        //System.out.println("Do you want to play a Nope card? (yes/no)");
        String response = scanner.nextLine().toLowerCase();
        if (response.equals("yes")) {
            // Player wants to play Nope card
            playCard(index, discardPile);
            tui.askNopeMessage(player.getName(), 1);
            //System.out.println(name + " played a Nope card.");
            setNopeCardPlayed(true);
        } else {
            // Player does not want to play Nope card
            tui.askNopeMessage(player.getName(), 2);
            //System.out.println(name + " did not play a Nope card.");
        }
    }
    private void setNopeCardPlayed(boolean bool){
        this.nopeCardPlayed = bool;
    }

    private boolean getNopeCardPlayed() {
        return nopeCardPlayed;
    }
    public void playCard(int cardIndex, Deck discardPile) {
        if (cardIndex >= 0 && cardIndex < currentPlayer.getHand().size() && currentPlayer.getHand().get(cardIndex).playable()) {
            playedCard = currentPlayer.getHand().get(cardIndex);
            currentPlayer.getHand().remove(playedCard);
            tui.playCardMessage(currentPlayer.getName(), playedCard);
            //System.out.println(name + " played a " + playedCard.getType() + " card.");
            discardPile.addCard(playedCard);
            switch (playedCard.getType()) {
                case NOPE:
                    //ask players if they want to play a nope card
                    this.askPlayersNope();
                    playedCard.Nope();
                    break;
                case SHUFFLE:
                    this.askPlayersNope();
                    if (getNopeCardPlayed()) {
                        tui.playCardMessage(1);
                        //System.out.println("Nope card negated the Shuffle!");
                        setNopeCardPlayed(false); // Reset Nope card flag
                    } else {
                        playedCard.Shuffle(deck, currentPlayer);
                    }
                    break;
                case SKIP:
                    this.askPlayersNope();
                    if (getNopeCardPlayed()) {
                        tui.playCardMessage(2);
                        //System.out.println("Nope card negated the Skip!");
                        setNopeCardPlayed(false); // Reset Nope card flag
                    } else {
                        currentPlayer.setTurnsToSkip(1, currentPlayer.getTurnsToSkip());
                    }
                    break;
                case SEE_THE_FUTURE:
                    this.askPlayersNope();
                    playedCard.seeTheFuture(deck, currentPlayer);
                    break;
                case CAT_CARD1:
                    currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD1, currentPlayer);
                    break;
                case CAT_CARD2:
                    currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD2, currentPlayer);
                    break;
                case CAT_CARD3:
                    currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD3, currentPlayer);
                    break;
                case CAT_CARD4:
                    currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD4, currentPlayer);
                    break;
                case CAT_CARD5:
                    currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD5, currentPlayer);
                    break;
                case FAVOR:
                    this.askPlayersNope();
                    chooseplayer();
                    break;
                case ATTACK:
                    this.askPlayersNope();
                    currentPlayer.setTurnsToSkip(1, currentPlayer.getTurnsToSkip());
                    playedCard.Attack(currentPlayer, this.getNextPlayer());
                    break;
            }
            // Implement the specific action associated with the played card
            // For example, triggering a special ability or resolving effects
        } else {
            tui.playCardMessage(3);
            //System.out.println("Invalid card index. Choose a card within the valid range.");

        }
    }
    public void chooseplayer() {
        Scanner scanner = new Scanner(System.in);
        tui.choosePlayerMessage(1);
        //System.out.println("Choose a player to take a card from (enter the player index): ");
        for (int i = 0; i < this.getPlayers().size(); i++) {
            if (!Objects.equals(this.getPlayers().get(i).getName(), currentPlayer.getName())) {
                tui.choosePlayerMessage(this, i);
                //System.out.println(i +" "+ game.getPlayers().get(i).getName());
            }
        }

        int playerIndex = scanner.nextInt();
        if (playerIndex < this.getPlayers().size()) {
            Player targetPlayer = this.getPlayers().get(playerIndex);
            favor(targetPlayer);
        } else {
            tui.choosePlayerMessage(2);
            //System.out.println("Invalid player index. Choose a player within the valid range.");
        }
    }
    public void favor(Player targetPlayer) {
        tui.favorMessage(currentPlayer.getName(), targetPlayer);
        //System.out.println(name + " played a Favor card. " + targetPlayer.getName() + " must give you a card.");
        giveCard(targetPlayer, currentPlayer);
    }
    public void twoCards(CardType typecard) {
        int deletedcardcount=1;
        while (deletedcardcount < 2) {
            for (int i = 0; i < currentPlayer.getHand().size(); i++) {
                if (currentPlayer.getHand().get(i).getType() == typecard) {
                    Card playedCard = currentPlayer.getHand().get(i);
                    currentPlayer.getHand().remove(playedCard);
                    deletedcardcount++;
                }
            }
        }
        chooseplayer();
    }
    public void giveCard(Player player, Player currentplayer) {
        Scanner scanner = new Scanner(System.in);
        int cardIndex;

        do {
            tui.giveCardMessage(player.getName(), currentplayer);
            //System.out.println(name + ", choose a card to give to " + player.getName() + " (enter the card index): ");
            for (int i = 0; i < player.getHand().size()-1; i++) {
                tui.outputPlayerHand(player, i);
                //System.out.println(i + ": " + hand.get(i).getType());
            }
            cardIndex = scanner.nextInt();

            // Check if the entered index is valid
            if (cardIndex < 0 || cardIndex >= player.getHand().size()) {
                tui.giveCardMessage(1);
                //System.out.println("Invalid card index. Please try again.");
            }
        } while (cardIndex < 0 || cardIndex >= player.getHand().size());
        Card card = player.getHand().get(cardIndex);
        player.getHand().remove(card);
        player.getHand().add(card);
        tui.giveCardMessage(player.getName(), currentplayer, card);
        //System.out.println(name + " gave " + player.getName() + " a " + card.getType() + " card.");
    }
}