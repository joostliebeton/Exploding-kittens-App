package ExplodingKittens.Controller;


import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;
import ExplodingKittens.Model.Deck;
import ExplodingKittens.Model.Player;
import ExplodingKittens.View.ClientTUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Game {
    //players: Arraylist<player>
    private boolean nopeCardPlayed = false;
    public String gameName;
    private Player targetPlayer;
    private Deck deck;
    private Player currentPlayer;
    private ArrayList<Player> eliminatedplayers = new ArrayList<>();
    private List<Player> players;
    private Deck discardPile;
    public Card playedCard;

    public List<Player> getPlayers() {
        return players;
    }
    public ClientTUI clientTui;
    private int currentPlayerIndex;
    public ClientTUI getClientTui(){
        return clientTui;
    }
    public int getDeckLength(){
        return deck.length();
    }

    public Game(String name) {
        this.gameName = name;
        clientTui = new ClientTUI();
        initializeDeck();

//        initializePlayers(Players); //CHANGE THE WAY PLAYER ARE MADE
//        initilializehands();
//        initializeDiscardPile();
        gameStart();

    }
    public void gameStart(){
        initilializehands();
        currentPlayerIndex = 0;
    }
    public Game getGame(){
        return this;
    }
    public void AddPlayer(String name){
        players.add(new Player(name, this));
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
                drawCard(player);
            }
        }
        for (int i = 0; i < 4; i++) {
            deck.addExplodingKitten();
        }
        deck.shuffle();
    }

//    private void initializePlayers(List<Player> names) {
//        players = new ArrayList<>();
//        for (String name : names) {
//            players.add(new Player(name, this));
//        }
//    }

    public void turn() {
        currentPlayer = getCurrentPlayer();
        while (currentPlayer.getExtraTurns() > 0) {
            clientTui.turnMessage(currentPlayer, 1);
            //System.out.println(currentPlayer.getName() + " has " + currentPlayer.extraTurns + " extra turns!");
            playCard(getPlayerInput());
            if (discardPile.getDeck()[discardPile.length() - 1].getType() == CardType.ATTACK) {
                currentPlayer.setExtraTurns(0,0);
                currentPlayer.setTurnsToSkip(1, currentPlayer.getTurnsToSkip());
                targetPlayer = getNextPlayer();
                targetPlayer.setExtraTurns(3, targetPlayer.getExtraTurns());
                clientTui.turnMessage(targetPlayer, 2);
                //System.out.println(targetPlayer.getName() + " has " + targetPlayer.extraTurns + " extra turns!");
                handleTurnEnd();
                return;
            }
            currentPlayer.setExtraTurns(-1, currentPlayer.getExtraTurns());
            drawCard(currentPlayer);
        }
        playCard(getPlayerInput());
        Scanner scanner = new Scanner(System.in);
        clientTui.turnMessage(1);
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
            clientTui.handleTurnEndMessage(currentPlayer);
            //System.out.println(currentPlayer.getName() + " skips a turn.");
            currentPlayer.setTurnsToSkip(-1, currentPlayer.getTurnsToSkip());
            endTurnNoDraw();
        } else {
            endTurn();
        }
    }
    public void choosePlayerChoice() {
        Scanner scanner = new Scanner(System.in);
        clientTui.choosePlayerChoiceMessage(1);
        //System.out.println("Choose a player to take a card from (enter the player index): ");
        for (int i = 0; i < this.getPlayers().size(); i++) {
            if (!Objects.equals(this.getPlayers().get(i).getName(), currentPlayer.getName())) {
                clientTui.choosePlayerChoiceMessage(this, i);
                //System.out.println(i + " " + game.getPlayers().get(i).getName());
            }
        }
        int playerIndex = scanner.nextInt();
        if (playerIndex < this.getPlayers().size()) {
            targetPlayer = this.getPlayers().get(playerIndex);
            favorChoice();
        } else {
            clientTui.choosePlayerChoiceMessage(2);
            //System.out.println("Invalid player index. Choose a player within the valid range.");
        }
    }
    public void favorChoice() {
        // Ask the user to choose a card type
        clientTui.favorChoiceMessage(currentPlayer.getName());
        //System.out.println(name + ", you played a Favor card. Choose a card type to request from the other player: ");
        clientTui.favorChoiceMessage(1);
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
            clientTui.favorChoiceMessage(currentPlayer.getName(), targetPlayer, takenCard, 1);
            //System.out.println(name + " took a " + takenCard.getType() + " card from " + targetPlayer.getName() + ".");
        } else {
            // Target player doesn't have the requested card type
            clientTui.favorChoiceMessage(currentPlayer.getName(), targetPlayer, null, 2); // check whether gives correct output, not sure if null is correctly implemented
            //System.out.println(targetPlayer.getName() + " doesn't have the requested card type. You get nothing.");
        }
    }

    private int getPlayerInput() {
        Scanner scanner = new Scanner(System.in);
        int cardIndex;

        do {
            clientTui.getPlayerInputMessage(currentPlayer);
            //System.out.println(currentPlayer.getName() + ", choose a card to play (enter the card index): ");
            for (int i = 0; i < currentPlayer.getHand().size(); i++) {

                clientTui.outputPlayerHand(currentPlayer, i);
                //System.out.println(i + ": " + currentPlayer.getHand().get(i).getType());
            }
            cardIndex = scanner.nextInt();

            // Check if the entered index is valid
            if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size()) {
                clientTui.getPlayerInputMessage(1);
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
        drawCard(currentPlayer);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public void endTurnNoDraw() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

    }

    public boolean isGameOver() {
        return players.size() == 1;
    }

    // Other methods as needed
    public void getDiscardPile() {
        for (Card card : discardPile.getDeck()) {
            clientTui.getDiscardPileMessage(card);
            //System.out.println(card.getType());
        }
    }

    public void getDeck() {
        for (Card card : deck.getDeck()) {
            clientTui.getDeckMessage(card);
            //System.out.println(card.getType());
        }
    }

    public void eliminatePlayer(Player player) {
        players.remove(player);
        eliminatedplayers.add(player);
        clientTui.eliminatePlayerMessage(player);
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
    public void drawCard(Player player) {
        Card drawnCard = deck.draw();
        if (drawnCard != null) {
            player.getHand().addCard(drawnCard);
            clientTui.drawCardMessage(player.getName(), drawnCard);
            //System.out.println(name + " drew a " + drawnCard.getType() + " card.");
            if (drawnCard.getType() == CardType.EXPLODING_KITTEN) {
                // Check if the player has a Defuse card
                if (player.getHand().hasDefuseCard()) {
                    // Player has a Defuse card, ask if they want to play it
                    clientTui.drawCardMessage(player.getName(), 1);
                    //System.out.println(name + ", you drew an Exploding Kitten! Do you want to play a Defuse card? (yes/no)");
                    Scanner scanner = new Scanner(System.in);
                    String response = scanner.nextLine().toLowerCase();

                    if (response.equals("yes")) {
                        // Player wants to play Defuse card
                        playCard(currentPlayer.getHand().indexOf(drawnCard));
                        clientTui.drawCardMessage(currentPlayer.getName(), 2);
                        //// ask the player to put the card somewhere in the deck 0 < i < deck.length() deck.addCard(i, card)
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
        clientTui.askNopeMessage(1);
        //System.out.println("Do you want to play a Nope card? (yes/no)");
        String response = scanner.nextLine().toLowerCase();
        if (response.equals("yes")) {
            // Player wants to play Nope card
            playCard(index);
            clientTui.askNopeMessage(player.getName(), 1);
            //System.out.println(name + " played a Nope card.");
            setNopeCardPlayed(true);
        } else {
            // Player does not want to play Nope card
            clientTui.askNopeMessage(player.getName(), 2);
            //System.out.println(name + " did not play a Nope card.");
        }
    }
    private void setNopeCardPlayed(boolean bool){
        this.nopeCardPlayed = bool;
    }

    private boolean getNopeCardPlayed() {
        return nopeCardPlayed;
    }
    public void playCard(int cardIndex) {
        if (cardIndex >= 0 && cardIndex < currentPlayer.getHand().size() && currentPlayer.getHand().get(cardIndex).playable()) {
            playedCard = currentPlayer.getHand().get(cardIndex);
            currentPlayer.getHand().remove(playedCard);
            clientTui.playCardMessage(currentPlayer.getName(), playedCard);
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
                        clientTui.playCardMessage(1);
                        //System.out.println("Nope card negated the Shuffle!");
                        setNopeCardPlayed(false); // Reset Nope card flag
                    } else {
                        playedCard.Shuffle(deck, currentPlayer);
                    }
                    break;
                case SKIP:
                    this.askPlayersNope();
                    if (getNopeCardPlayed()) {
                        clientTui.playCardMessage(2);
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
            clientTui.playCardMessage(3);
            //System.out.println("Invalid card index. Choose a card within the valid range.");

        }
    }
    public void chooseplayer() {
        Scanner scanner = new Scanner(System.in);
        clientTui.choosePlayerMessage(1);
        //System.out.println("Choose a player to take a card from (enter the player index): ");
        for (int i = 0; i < this.getPlayers().size(); i++) {
            if (!Objects.equals(this.getPlayers().get(i).getName(), currentPlayer.getName())) {
                clientTui.choosePlayerMessage(this, i);
                //System.out.println(i +" "+ game.getPlayers().get(i).getName());
            }
        }

        int playerIndex = scanner.nextInt();
        if (playerIndex < this.getPlayers().size()) {
            targetPlayer = this.getPlayers().get(playerIndex);
            favor();
        } else {
            clientTui.choosePlayerMessage(2);
            //System.out.println("Invalid player index. Choose a player within the valid range.");
        }
    }
    public void favor() {
        clientTui.favorMessage(currentPlayer.getName(), targetPlayer);
        //System.out.println(name + " played a Favor card. " + targetPlayer.getName() + " must give you a card.");
        //giveCard();
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
    public void setTargetPlayer(Player player) {
        this.targetPlayer = player;
    }
    public Player getTargetPlayer(){
        return targetPlayer;
    }
    public void chooseCard(){
        Scanner scanner = new Scanner(System.in);
        int cardIndex;

        do {
            clientTui.giveCardMessage(targetPlayer.getName(), currentPlayer);
            //System.out.println(name + ", choose a card to give to " + player.getName() + " (enter the card index): ");
            for (int i = 0; i < targetPlayer.getHand().size()-1; i++) {
                clientTui.outputPlayerHand(targetPlayer, i);
                //System.out.println(i + ": " + hand.get(i).getType());
            }
            cardIndex = scanner.nextInt();

            // Check if the entered index is valid
            if (cardIndex < 0 || cardIndex >= targetPlayer.getHand().size()) {
                clientTui.giveCardMessage(1);
                //System.out.println("Invalid card index. Please try again.");
            }
        } while (cardIndex < 0 || cardIndex >= targetPlayer.getHand().size());
        Card card = targetPlayer.getHand().get(cardIndex);
        CardType cardType = card.getType();
    }
    public void giveCard(Card card) {
        targetPlayer.getHand().remove(card);
        currentPlayer.getHand().add(card);
        clientTui.giveCardMessage(targetPlayer.getName(), currentPlayer, card);
        //System.out.println(name + " gave " + player.getName() + " a " + card.getType() + " card.");
    }

    public void addPlayer(String name) {
        players.add(new Player(name, this));
    }

    public void playDefuse(int index) {
        deck.putCard(index, new Card(CardType.EXPLODING_KITTEN));

    }

    public Object getAlivePlayers() {
        return players;
    }

    public Player getPlayer(String word) {
        for (Player player : players) {
            if (player.getName().equals(word)) {
                return player;
            }
        }
        return null;
    }
}