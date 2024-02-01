package ExplodingKittens.Controller;

import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;;
import ExplodingKittens.Model.Player1;
import ExplodingKittens.View.ServerTUI;
import ExplodingKittens.exceptions.ExitProgram;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameServer implements Runnable {
    private ServerSocket ssock;

    private boolean chatFunction = true;

    /**
     * List of EKCHandlers, one for each connected client
     */
    private List<EKCHandler> clients;

    /**
     * Next client number, increasing for every new connection
     */
    private int next_client_no;

    /**
     * The view of this gameServer
     */
    private ServerTUI view;

    /**
     * The name of the Game
     */
    private String gameName;
    /** The game instance */
    private Game game;

/**
     * Constructs a new GameServer. Initializes the clients list,
     * the view and the next_client_no.
     */
    public GameServer() {
        this.clients = new ArrayList<>();
        this.view = new ServerTUI();
        this.next_client_no = 1;
    }
    /**
     * Gets the name of the game.
     *
     * @return the name of the game
     */

    public String getGameName() {
        return this.gameName;
    }

    /**
     * Opens a new socket by calling {@link #setup()} and starts a new
     * EKCHandler for every connecting client.
     * <p>
     * If {@link #setup()} throws a ExitProgram exception, stop the program.
     * In case of any other errors, ask the user whether the setup should be
     * ran again to open a new socket.
     * @ensures a server socket is opened
     */
    public void run() {
        boolean openNewSocket = true;
        while (openNewSocket) {
            try {
                // Sets up the Game
                setup();
                while (true) {
                    Socket sock = ssock.accept();
                    String name = "Client " + String.format("%02d", next_client_no++);
                    view.showMessage("New client [" + name + "] connected!");
                    EKCHandler handler = new EKCHandler(sock, this, name);
                    new Thread(handler).start();
                    clients.add(handler);
                }

            } catch (ExitProgram e1) {
                // If setup() throws an ExitProgram exception,
                // stop the program.
                openNewSocket = false;
            } catch (IOException e) {
                System.out.println("A server IO error occurred: "
                        + e.getMessage());

                if (!view.getBoolean("Do you want to open a new socket?")) {
                    openNewSocket = false;
                }
            }
        }
        view.showMessage("See you later!");
    }

    public void setChatFunction(boolean chatFunction) {
        this.chatFunction = chatFunction;
    }
    public boolean getChatFunction() {
        return chatFunction;
    }

    /**
     * Sets up a new Game using {@link #setupGame()} and opens a new
     * ServerSocket at localhost on a user-defined port.
     * <p>
     * The user is asked to input a port, after which a socket is attempted
     * to be opened. If the attempt succeeds, the method ends, If the
     * attempt fails, the user decides to try again, after which an
     * ExitProgram exception is thrown or a new port is entered.
     *
     * @throws ExitProgram if a connection can not be created on the given
     *                     port and the user decides to exit the program.
     * @ensures a serverSocket is opened.
     */
    public void setup() throws ExitProgram {
        // First, initialize the Game
        setupGame();

        ssock = null;
        while (ssock == null) {
            //int port = view.getInt("Please enter the server port.");
        int port = 8888;
            // try to open a new ServerSocket
            try {
                view.showMessage("Attempting to open a socket at " + "127.0.0.1"  + " on port " + port + "...");
//              ssock = new ServerSocket(port, 0, InetAddress.getByName("145.126.38.21"));
                ssock = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"));
                view.showMessage("Server started at port " + port);
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on "
                        + "145.126.38.21" + " and port " + port + ".");

                if (!view.getBoolean("Do you want to try again?")) {
                    throw new ExitProgram("User indicated to exit the "
                            + "program.");
                }
            }
        }
    }

    /**
     * Asks the user for a Game name and initializes
     * a new Game with this name.
     */
    public void setupGame() {
        //gameName = view.getString("Please enter the name of the game.");
        gameName = "test123";
        game = new Game(gameName);
        // To be implemented.
    }
    /**
     * Removes a client from the list of connected clients.
     *
     * @param client The client to be removed.
     */
    public void removeClient(EKCHandler client) {
        this.clients.remove(client);
    }

    //////////////////////////server methods/////////////////////////
    /**
     * Adds a player to the game.
     *
     * @param name The name of the player to be added.
     */
    public void addPlayer(String name) {
        game.addPlayer(name);
    }
    /**
     * Plays a card with the specified card type by the current player.
     *
     * @param cardType      The type of card to be played.
     * @param currentplayer The current player who is playing the card.
     * @return A message indicating the result of playing the card.
     */

    public String playCardcmd(CardType cardType, Player1 currentplayer) {
        for (CardType cardType1 : CardType.values()) {
            if (cardType1 == cardType) {
                int index = 0;
                for (Card card : currentplayer.getHandList()) {
                    if (card.getType().equals(cardType1)) {
                        sendMessageToAllOtherPlayers(ProtocolMessages.GENERAL_CARD_RESPONSE + ProtocolMessages.DELIMITER + cardType);
                        if (card.isActionCard()) {
                            if (sendNopeMessage()){
                                goInWaitForNope(currentplayer, cardType);
                                return ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + "wait for nope cards";
                            } return (game.playCard(index,currentplayer));
                        } return (game.playCard(index,currentplayer));

                    }
                    index++;
                }return ("you don't have this card");
            }


        }return "this card doesnt exist";
    }
    /**
     * Marks the beginning of waiting for a "NOPE" response for the specified player and card type.
     *
     * @param currentplayer The player currently waiting for "NOPE" responses.
     * @param cardType      The type of card for which "NOPE" responses are awaited.
     */

    private void goInWaitForNope(Player1 currentplayer, CardType cardType) {
        getGame().cardBeforeNope = new Card(cardType);
        getGame().playerBeforeNope = currentplayer;
    }
    public String action;
    /**
     * Marks the beginning of waiting for a "NOPE" response for the specified player and action.
     *
     * @param currentplayer The player currently waiting for "NOPE" responses.
     * @param action1       The action for which "NOPE" responses are awaited.
     */
    private void goInWaitForNope(Player1 currentplayer, String action1) {
        action = action1;
        getGame().playerBeforeNope = currentplayer;
    }
    /**
     * Retrieves the index of a card with the specified card type from the player's hand.
     *
     * @param cardType The type of card to search for.
     * @param player   The player whose hand is being searched.
     * @return The index of the card if found, otherwise -1.
     */
    public int getCardIndex(CardType cardType, Player1 player){
        int index = 0;
        for (Card card : player.getHandList()) {
            if (card.getType().equals(cardType)) {
                return index;
            }
            index++;
        }
        return -1;
    }
    public Player1 playerWhoNoped = null;
    public boolean nopeanswer = false;

    public boolean nopecardPlayed = false;
    /**
     * Checks if any player has a "NOPE" card in their hand and sends the appropriate message.
     *
     * @return True if a "NOPE" card was found and a message was sent, otherwise false.
     */
    public boolean sendNopeMessage(){
        for (Player1 player : game.getPlayers()) {
            for (Card card1 : player.getHandList()) {
                if (card1.getType() == CardType.NOPE && player != game.getCurrentPlayer()) {
                    sendMessageToPlayer(player.getName(), "NOPE");
                    return true;
                }
            }
//                           return (ProtocolMessages.PLAY_NOPED + ProtocolMessages.DELIMITER + cardType.name());
        }
        return false;
    }
    /**
     * Retrieves the current game instance.
     *
     * @return The current game instance.
     */
    public Game getGame() {
        return game;
    }
    /**
     * Starts the game process by initiating the game and sending turn messages to players.
     */
    public void startGameProcess() {
        game.gameStart();
        turnMessage();
    }
    /**
     * Sends turn messages to all players indicating whose turn it is.
     */
    public void turnMessage(){
        for (Player1 player : game.getPlayers()){
            sendMessageToPlayer(player.getName(), ProtocolMessages.TURN + ProtocolMessages.DELIMITER + game.getCurrentPlayer().getName());
        }
    }
    /**
     * Retrieves the handler for a specific player.
     *
     * @param playerName The name of the player to retrieve the handler for.
     * @return The handler for the specified player, or null if not found.
     */
    private EKCHandler getPlayerHandler(String playerName) {
        for (EKCHandler handler : clients) {
            if (handler.getName().equals(playerName)) {
                return handler;
            }
        }
        return null;
    }
    /**
     * Draws a card for the current player and sends appropriate messages to other players.
     *
     * @return A message indicating the result of drawing the card.
     */
    public String drawCard() {
        Player1 currentplayer = game.getCurrentPlayer();
        sendMessageToAllOtherPlayers(ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + getGame().getCurrentPlayer().getName() + ProtocolMessages.DELIMITER + "drew a card");
        String answer = game.drawCard(currentplayer);
        if (answer.contains("EXPLODING_KITTEN")){
            sendMessageToAllOtherPlayers(ProtocolMessages.ANNOUNCEMENT+ ProtocolMessages.DELIMITER + "EXPLODING_KITTEN");
            return("EXPLODING_KITTEN");
        };
        return answer;
    }
    /**
     * Allows the current player to choose a card from another player's hand.
     *
     * @param cardType      The type of card to choose.
     * @param victimplayer  The player whose card is being chosen.
     */
    public void chooseCardInHand(CardType cardType, Player1 victimplayer) {
        //first remove card
        victimplayer.getHand().remove(getCardIndex(cardType, victimplayer));
        //then give card
        Card card = new Card(cardType);
        game.giveCard(card, game.cardReciever);
        for(Player1 player: game.getPlayers()) {
            if (player == game.getCurrentPlayer()){
                sendMessageToPlayer(player.getName(), ProtocolMessages.CARD_RECEIVED + ProtocolMessages.DELIMITER + "FAVOR" + ProtocolMessages.DELIMITER + game.getTargetPlayer().getName() + ProtocolMessages.DELIMITER + cardType.name());
            } else if(player == game.getTargetPlayer()){
                sendMessageToPlayer(player.getName(), ProtocolMessages.CARD_GIVEN + ProtocolMessages.DELIMITER + "FAVOR" + ProtocolMessages.DELIMITER + game.getCurrentPlayer().getName() + ProtocolMessages.DELIMITER + cardType.name());
            } else {
                sendMessageToPlayer(player.getName(), ProtocolMessages.CARD_EXCHANGED + ProtocolMessages.DELIMITER + game.getTargetPlayer().getName() + ProtocolMessages.DELIMITER + game.getCurrentPlayer().getName());
            }
        }
    }
    /**
     * Plays the Favor card, allowing the target player to select a card from his hand.
     *
     * @param targetPlayer The player from whose hand a card is given.
     * @return A message indicating the result of playing the Favor card.
     */
    public String playFavor(Player1 targetPlayer) {
        game.cardReciever = game.getCurrentPlayer();
        game.getCurrentPlayer().getHand().remove(getCardIndex(CardType.FAVOR, game.getCurrentPlayer()));
        game.setTargetPlayer(targetPlayer);
        if (sendNopeMessage()){
            goInWaitForNope(game.getCurrentPlayer(), CardType.FAVOR);
            return ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + "wait for nope cards";
        }
        sendMessageToPlayer(targetPlayer.getName(), ProtocolMessages.PICK_CARD_IN_HAND + ProtocolMessages.DELIMITER + game.getCurrentPlayer().getName());
        return ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER +game.getCurrentPlayer().getName() + " (you) played a Favor card";
    }
    /**
     * Plays the Combo2 card, which allows the current player to remove two cards of the specified type from their hand
     * and get a random card from the target player's hand.
     *
     * @param cardType The type of card to be removed from the current player's hand.
     * @return A message indicating the result of playing the Combo2 card.
     */

    public String playCombo2(CardType cardType){
        game.getCurrentPlayer().getHand().remove(getCardIndex(cardType, game.getCurrentPlayer()));
        game.getCurrentPlayer().getHand().remove(getCardIndex(cardType, game.getCurrentPlayer()));
        return (takerandomCard(game.getCurrentPlayer(),game.getTargetPlayer()));

    }
    /**
     * Takes a random card from the target player and gives it to the current player as part of the Combo2 card effect.
     *
     * @param currentPlayer The player initiating the Combo2 card effect.
     * @param targetPlayer The player from whose hand the card is taken.
     * @return A message indicating the result of the card exchange.
     */
    private String takerandomCard(Player1 currentPlayer, Player1 targetPlayer) {
        game.cardReciever = game.getCurrentPlayer();
        game.setTargetPlayer(targetPlayer);
        if (sendNopeMessage()){
            goInWaitForNope(game.getCurrentPlayer(), "COMBO2");
            return ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + "wait for nope cards";
        }
        sendMessageToAllOtherPlayers(ProtocolMessages.CARD_EXCHANGED + ProtocolMessages.DELIMITER + "COMBO2" + ProtocolMessages.DELIMITER + game.getTargetPlayer().getName() + ProtocolMessages.DELIMITER + game.getCurrentPlayer());
        return ProtocolMessages.CARD_RECEIVED + ProtocolMessages.DELIMITER + "COMBO2" + ProtocolMessages.DELIMITER + game.getTargetPlayer().getName() + ProtocolMessages.DELIMITER + game.giveCard(currentPlayer, targetPlayer);
    }

    /**
     * Sends a message to the specified player.
     *
     * @param playerName The name of the player to whom the message is sent.
     * @param message The message to be sent.
     */
    public void sendMessageToPlayer(String playerName, String message) {
        EKCHandler handler = getPlayerHandler(playerName);
        if (handler != null && Objects.equals(message, "NOPE")) {
            handler.sendMessage(ProtocolMessages.PLAY_NOPED);
        } else if (handler != null) {
            handler.sendMessage(message);
        } else if (playerName.contains("Computer")) {
            view.showMessage("Computer " + playerName + " " + message);
        } else {
            view.showMessage("Player " + playerName + " not found.");
        }
    }
    /**
     * Sends a message to all players except the current player.
     *
     * @param message The message to be sent.
     */
    public void sendMessageToAllOtherPlayers(String message) {
        for (EKCHandler handler : clients) {
            if (handler.getName() != game.getCurrentPlayer().getName()) {
                handler.sendMessage(message);
            }
        }
    }
    /**
     * Sends a message to all players.
     *
     * @param message The message to be sent.
     */
    public void sendMessageToAllPlayers(String message) {
        for (EKCHandler handler : clients) {
            handler.sendMessage(message);
        }
    }
    /**
     * Plays the Defuse card which places the exploding kitten at the specified index in the deck.
     *
     * @param index The index of the exploding kitten to be placed.
     */
    public void playDefuse(int index) {
        game.playDefuse(index);
    }
    /**
     * Retrieves the size of the draw pile.
     *
     * @return A message containing the size of the draw pile.
     */
    public String drawPileSize() {
        return ProtocolMessages.RESPONSE_DRAW_PILE_SIZE + ProtocolMessages.DELIMITER + game.getDeckLength();
    }
    /**
     * Retrieves the size of the specified player's hand.
     *
     * @param player The name of the player whose hand size is to be retrieved.
     * @return A message containing the size of the player's hand.
     */
    public String userHandSize(String player) {
        Player1 player1 = game.getPlayer(player);
        if (player1 == null) {
        return ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER+"Player not found.";
        }
        return ProtocolMessages.RESPONSE_USERS_HAND_SIZE + ProtocolMessages.DELIMITER + player1.getHandList().size();
//        game.getClientTui().userHandSizeMessage(player.getName(), player.getHandList().size());
    }
    /**
     * Retrieves the list of alive players in the game.
     *
     * @return A message containing the names of the alive players.
     */
    public String requestAlivePlayers() {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(ProtocolMessages.RESPONSE_ALIVE_PLAYERS).append("~");


        for (Player1 username : game.getPlayers()) {
            responseBuilder.append(username.getName()).append(ProtocolMessages.DELIMITER);
        }

        if (!game.getPlayers().isEmpty()) {
            responseBuilder.deleteCharAt(responseBuilder.length() - 1);
        }
        return responseBuilder.toString();
    }
    /**
     * Retrieves the list of cards in the hand of the specified player.
     *
     * @param player The name of the player whose cards are to be retrieved.
     * @return A message containing the types of cards in the player's hand.
     */
    public String requestCardsInHand(String player) {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(ProtocolMessages.REQUEST_CARD_IN_HAND_RESPONSE).append(ProtocolMessages.DELIMITER);
        Player1 player1 = game.getPlayer(player);
        // Append the card values in hand
        try{
            for (Card value : player1.getHandList()) {
                responseBuilder.append(value.getType().toString()).append(ProtocolMessages.DELIMITER);
            }
            if (!player1.getHandList().isEmpty()) {
                responseBuilder.deleteCharAt(responseBuilder.length() - 1);
            }
        } catch (NullPointerException e){
            return ("player has no cards");
        }
        return responseBuilder.toString();
    }
    /**
     * Sends a message to all clients indicating the winner.
     */

    public void endGame() {
        for (EKCHandler handler : clients) {
            System.out.println(game.getWinner().getName());
            handler.sendMessage(ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER + game.getWinner().getName());
        }
    }
    /**
     * Sends a chat message to all clients except the specified player.
     *
     * @param s The message to be sent.
     * @param name The name of the player sending the chat.
     */

    public void sendChat(String s , String name) {
        for (EKCHandler handler : clients) {
            if (handler.getName() != name) {
                handler.sendMessage(s);
            }
        }
    }
    /**
     * Retrieves the number of mandatory draws for the specified player.
     *
     * @param player The player for whom the number of mandatory draws is to be retrieved.
     * @return A message containing the number of mandatory draws for the player.
     */
    public String requestMandatoryDraws(Player1 player) {
        return (ProtocolMessages.RESPONSE_MANDATORY_DRAWS+ ProtocolMessages.DELIMITER+ (player.getExtraTurns()+1));
    }



/////////////////////main//////////////////////////////////////
    /**
     * The entry point of the Game Server application.
     * <p>
     * This method initializes a new instance of the {@code GameServer} class,
     * starts the server, and prints a welcome message to the console.
     * <p>
     * It creates a new thread for the {@code GameServer} instance and starts
     * the thread to handle client connections.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Create a new instance of the GameServer
        GameServer gameServer = new GameServer();

        // Print a welcome message to the console
        System.out.println("Welcome to the Game Server! Starting...");

        // Start the server in a new thread
        new Thread(gameServer).start();
    }




}