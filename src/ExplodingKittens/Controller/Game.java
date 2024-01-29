package ExplodingKittens.Controller;


import ExplodingKittens.Model.*;
import ExplodingKittens.View.ClientTUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Game {
    //players: Arraylist<player>
    private boolean nopeCardPlayed = false;
    public String gameName;
    private Player1 targetPlayer;
    private Deck deck;
    private Player1 currentPlayer;
    private ArrayList<Player1> eliminatedplayers = new ArrayList<>();
    private List<Player1> players;
    private Deck discardPile;
    public Card playedCard;

    public List<Player1> getPlayers() {
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
        this.players= new ArrayList<>();
        initializeDeck();

//        initializePlayers(Players); //CHANGE THE WAY PLAYER ARE MADE
//        initilializehands();
//        initializeDiscardPile();
        //gameStart();

    }
    public void gameStart(){
        initilializehands();
        currentPlayerIndex = 0;
        currentPlayer = getCurrentPlayer();
    }
    public Game getGame(){
        return this;
    }
    public boolean isCardPlayed(){
        return playedCard != null;
    }

    private void initializeDeck() {
        deck = new Deck();
        deck.initializeDeck();
        discardPile = new Deck();
        // Add Exploding Kittens to the deck after initial cards have been dealt

    }


    private void initilializehands() {
        for (int i = 0; i < /*player.size()*/ 5; i++) {
            for (Player1 player : players) {
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
            //clientTui.turnMessage(currentPlayer, 1);
            //System.out.println(currentPlayer.getName() + " has " + currentPlayer.extraTurns + " extra turns!");
            playCard(getPlayerInput());
            if (discardPile.getDeck()[discardPile.length() - 1].getType() == CardType.ATTACK) {
                currentPlayer.setExtraTurns(0,0);
                currentPlayer.setTurnsToSkip(1, currentPlayer.getTurnsToSkip());
                targetPlayer = getNextPlayer();
                targetPlayer.setExtraTurns(3, targetPlayer.getExtraTurns());
                //clientTui.turnMessage(targetPlayer, 2);
                //System.out.println(targetPlayer.getName() + " has " + targetPlayer.extraTurns + " extra turns!");
                //handleTurnEnd();
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

    public void handleTurnEnd() {
        if (currentPlayer.getTurnsToSkip() > 0) {
            //clientTui.handleTurnEndMessage(currentPlayer);
            //System.out.println(currentPlayer.getName() + " skips a turn.");
            currentPlayer.setTurnsToSkip(-1, currentPlayer.getTurnsToSkip());
            endTurnNoDraw();
        } else {
            endTurn();
        }
        playedCard = null;
    }
    public void choosePlayerChoice() {
        Scanner scanner = new Scanner(System.in);
        //clientTui.choosePlayerChoiceMessage(1);
        //System.out.println("Choose a player to take a card from (enter the player index): ");
        for (int i = 0; i < this.getPlayers().size(); i++) {
            if (!Objects.equals(this.getPlayers().get(i).getName(), currentPlayer.getName())) {
                //clientTui.choosePlayerChoiceMessage(this, i);
                //System.out.println(i + " " + game.getPlayers().get(i).getName());
            }
        }
        int playerIndex = scanner.nextInt();
        if (playerIndex < this.getPlayers().size()) {
            targetPlayer = this.getPlayers().get(playerIndex);
            //favorChoice();
        } else {
            //clientTui.choosePlayerChoiceMessage(2);
            //System.out.println("Invalid player index. Choose a player within the valid range.");
        }
    }
    public void favorChoice(CardType cardType) {
        // Ask the user to choose a card type
        //clientTui.favorChoiceMessage(currentPlayer.getName());
        //System.out.println(name + ", you played a Favor card. Choose a card type to request from the other player: ");
        //clientTui.favorChoiceMessage(1);
        //System.out.println("1: EXPLODING_KITTEN, 2: DEFUSE, 3: NOPE, 4: SKIP, 5: ATTACK, 6:  SEE_THE_FUTURE, 7:FAVOR, 8: SHUFFLE, 9: CAT_CARD1, 10: CAT_CARD2, 11: CAT_CARD3, 12: CAT_CARD4, 13: CAT_CARD5 ");
        int deletedcardcount=1;
        while (deletedcardcount < 3) {
            for (int i = 0; i < currentPlayer.getHand().size(); i++) {
                if (currentPlayer.getHand().get(i).getType() == cardType) {
                    Card playedCard = currentPlayer.getHand().get(i);
                    currentPlayer.getHand().remove(playedCard);
                    deletedcardcount++;
                }
            }
        }
        Scanner scanner = new Scanner(System.in);
        int chosenType = scanner.nextInt();

        // Check if the target player has the requested card type
        CardType requestedType = CardType.values()[chosenType - 1];
        if (targetPlayer.getHand().hasCardType(requestedType)) {
            // Target player has the requested card type, take a card from them
            Card takenCard = takeCard(requestedType, targetPlayer);
            currentPlayer.getHand().add(takenCard);
            assert takenCard != null;
            //clientTui.favorChoiceMessage(currentPlayer.getName(), targetPlayer, takenCard, 1);
            //System.out.println(name + " took a " + takenCard.getType() + " card from " + targetPlayer.getName() + ".");
        } else {
            // Target player doesn't have the requested card type
            //clientTui.favorChoiceMessage(currentPlayer.getName(), targetPlayer, null, 2); // check whether gives correct output, not sure if null is correctly implemented
            //System.out.println(targetPlayer.getName() + " doesn't have the requested card type. You get nothing.");
        }
    }
    public Card takeCard(CardType type,Player1 targetPlayer) {
        for (Card card : targetPlayer.getHandList()) {
            if (card.getType() == type) {
                targetPlayer.getHand().remove(card);
                return card;
            }
        }
        return null;
    }

    private int getPlayerInput() {

        Scanner scanner = new Scanner(System.in);
        int cardIndex;
        do {
           clientTui.getPlayerInputMessage(currentPlayer);
            //System.out.println(currentPlayer.getName() + ", choose a card to play (enter the card index): ");
            for (int i = 0; i < currentPlayer.getHand().size(); i++) {

                //clientTui.outputPlayerHand(currentPlayer, i);
                //System.out.println(i + ": " + currentPlayer.getHand().get(i).getType());
            }
            cardIndex = scanner.nextInt();

            // Check if the entered index is valid
            if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size()) {
                //clientTui.getPlayerInputMessage(1);
                //System.out.println("Invalid card index. Please try again.");
            }
        } while (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size());

        return cardIndex;
    }
    public void nextCurrentPlayer(){
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    public Player1 getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player1 getNextPlayer() {
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
            //clientTui.getDiscardPileMessage(card);
            //System.out.println(card.getType());
        }
    }

    public void getDeck() {
        for (Card card : deck.getDeck()) {
            //clientTui.getDeckMessage(card);
            //System.out.println(card.getType());
        }
    }

    public String eliminatePlayer(Player1 player) {
        players.remove(player);
        eliminatedplayers.add(player);

        //clientTui.eliminatePlayerMessage(player);
        //System.out.println(player.getName() + " has been eliminated!");
        currentPlayerIndex = (currentPlayerIndex - 1) % players.size();
        return (ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER +  player .getName() + " has been eliminated!")
    }

    public ArrayList<Player1> getEliminatedPlayers() {
        return eliminatedplayers;
    }

    public void askPlayersNope() {
        for (Player1 player : players) {
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
    public String drawCard(Player1 player) {
        Card drawnCard = deck.draw();
        if (drawnCard != null) {
            player.getHand().addCard(drawnCard);

            //clientTui.drawCardMessage(player.getName(), drawnCard);
            //System.out.println(name + " drew a " + drawnCard.getType() + " card.");
            if (drawnCard.getType() == CardType.EXPLODING_KITTEN) {
                // Check if the player has a Defuse card
                if (player.getHand().hasDefuseCard()) {
                   //player.getHand().remove(player.getHand().get(player.getHand().indexOf(card.CardType.DEFUSE)));
                    }
                } else {
                    // Player does not have a Defuse card, eliminate them
                    eliminatePlayer(currentPlayer);
                }
            }
            return (ProtocolMessages.DRAWN + ProtocolMessages.DELIMITER + drawnCard.getType());
        }
    public void askNope(Player1 player, int index) {
        Scanner scanner = new Scanner(System.in);
        //clientTui.askNopeMessage(1);
        //System.out.println("Do you want to play a Nope card? (yes/no)");
        String response = scanner.nextLine().toLowerCase();
        if (response.equals("yes")) {
            // Player wants to play Nope card
            playCard(index);
            //clientTui.askNopeMessage(player.getName(), 1);
            //System.out.println(name + " played a Nope card.");
            setNopeCardPlayed(true);
        } else {
            // Player does not want to play Nope card
            //clientTui.askNopeMessage(player.getName(), 2);
            //System.out.println(name + " did not play a Nope card.");
        }
    }
    private void setNopeCardPlayed(boolean bool){
        this.nopeCardPlayed = bool;
    }

    private boolean getNopeCardPlayed() {
        return nopeCardPlayed;
    }
    public  String playCard(int cardIndex) {
        if (cardIndex >= 0 && cardIndex < currentPlayer.getHand().size() && currentPlayer.getHand().get(cardIndex).playable()) {
            playedCard = currentPlayer.getHand().get(cardIndex);
            currentPlayer.getHand().remove(playedCard);
            //clientTui.playCardMessage(currentPlayer.getName(), playedCard);
            //System.out.println(name + " played a " + playedCard.getType() + " card.");
            discardPile.addCard(playedCard);
            switch (playedCard.getType()) {
                case NOPE:
                    playedCard.Nope();
                    break;
                case SHUFFLE:
                    return (playedCard.Shuffle(deck) + currentPlayer.getName() + ProtocolMessages.DELIMITER + " played a " + playedCard.getType() + " card");
                case SKIP:
                    return ((currentPlayer.setTurnsToSkip(1, currentPlayer.getTurnsToSkip())));
                case SEE_THE_FUTURE:
                    //this.askPlayersNope();
                    return (playedCard.seeTheFuture(this.deck));

                case CAT_CARD1:
                    return currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD1, currentPlayer);

                case CAT_CARD2:
                    return currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD2, currentPlayer);

                case CAT_CARD3:
                    return currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD3, currentPlayer);

                case CAT_CARD4:
                     return currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD4, currentPlayer);

                case CAT_CARD5:
                    return currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD5, currentPlayer);

                //case FAVOR: handled in gameserver
//                    this.askPlayersNope();
//                    favor();
//                    break;
                case ATTACK:
                    this.askPlayersNope();
                    currentPlayer.setTurnsToSkip(1, currentPlayer.getTurnsToSkip());
                    playedCard.Attack(currentPlayer, this.getNextPlayer());
                    break;
            }
            // Implement the specific action associated with the played card
            // For example, triggering a special ability or resolving effects
        } else {
            //clientTui.playCardMessage(3);
            //System.out.println("Invalid card index. Choose a card within the valid range.");

        }
        return null;
    }
//    public void chooseplayer(Player1 targetPlayer) {
//        Scanner scanner = new Scanner(System.in);
//        clientTui.choosePlayerMessage(1);
//        //System.out.println("Choose a player to take a card from (enter the player index): ");
//        for (int i = 0; i < this.getPlayers().size(); i++) {
//            if (!Objects.equals(this.getPlayers().get(i).getName(), currentPlayer.getName())) {
//                clientTui.choosePlayerMessage(this, i);
//                //System.out.println(i +" "+ game.getPlayers().get(i).getName());
//            }
//        }
//
//        int playerIndex = scanner.nextInt();
//        if (playerIndex < this.getPlayers().size()) {
//            targetPlayer = this.getPlayers().get(playerIndex);
//            favor();
//        } else {
//            clientTui.choosePlayerMessage(2);
//            //System.out.println("Invalid player index. Choose a player within the valid range.");
//        }
//    }

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
    }
    public void setTargetPlayer(Player1 player) {
        this.targetPlayer = player;
    }
    public Player1 getTargetPlayer(){
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
        //System.out.println(name + " gave " + player.getName() + " a " + card.getType() + " card.");
    }

    public void addPlayer(String name) {
        players.add(new HumanPlayer(name, this));
    }
    public void addComputerplayer(String name) {
        players.add(new ComputerPlayer(name, this));
    }

    public void playDefuse(int index) {
        deck.putCard(index, new Card(CardType.EXPLODING_KITTEN));

    }

    public Object getAlivePlayers() {
        return players;
    }

    public Player1 getPlayer(String word) {
        for (Player1 player : players) {
            if (player.getName().equals(word)) {
                return player;
            }
        }
        throw new IllegalArgumentException("player doesnt Exist");
    }
}