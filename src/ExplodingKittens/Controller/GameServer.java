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

    /**
     * List of HotelClientHandlers, one for each connected client
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
     * The name of the Hotel
     */
    private String gameName;
    private Game game;

    public GameServer() {
        this.clients = new ArrayList<>();
        this.view = new ServerTUI();
        this.next_client_no = 1;
    }

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
     */
    public void run() {
        boolean openNewSocket = true;
        while (openNewSocket) {
            try {
                // Sets up the hotel application
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
        // First, initialize the Hotel.
        setupGame();

        ssock = null;
        while (ssock == null) {
            //int port = view.getInt("Please enter the server port.");
        int port = 8888;
            // try to open a new ServerSocket
            try {
                view.showMessage("Attempting to open a socket at 145.126.38.21 "
                        + "on port " + port + "...");
//                ssock = new ServerSocket(port, 0, InetAddress.getByName("145.126.38.21"));
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
     * Asks the user for a hotel name and initializes
     * a new Hotel with this name.
     */
    public void setupGame() {
        //gameName = view.getString("Please enter the name of the game.");
        gameName = "test123";
        game = new Game(gameName);
        // To be implemented.
    }

    public void removeClient(EKCHandler client) {
        this.clients.remove(client);
    }

    //////////////////////////server methods///////////////////////// 0
    public void addPlayer(String name) {
        game.addPlayer(name);
    }


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

    private void goInWaitForNope(Player1 currentplayer, CardType cardType) {
        getGame().cardBeforeNope = new Card(cardType);
        getGame().playerBeforeNope = currentplayer;
    }
    public String action;
    private void goInWaitForNope(Player1 currentplayer, String action1) {
        action = action1;
        getGame().playerBeforeNope = currentplayer;
    }

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
    public Game getGame() {
        return game;
    }

    public void startGameProcess() {
        game.gameStart();
        turnMessage();
    }
    //}

    public void turnMessage(){
        for (Player1 player : game.getPlayers()){
            sendMessageToPlayer(player.getName(), ProtocolMessages.TURN + ProtocolMessages.DELIMITER + game.getCurrentPlayer().getName());
        }
    }

    private EKCHandler getPlayerHandler(String playerName) {
        for (EKCHandler handler : clients) {
            if (handler.getName().equals(playerName)) {
                return handler;
            }
        }
        return null;
    }

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


    public String playCombo2(CardType cardType){
        game.getCurrentPlayer().getHand().remove(getCardIndex(cardType, game.getCurrentPlayer()));
        game.getCurrentPlayer().getHand().remove(getCardIndex(cardType, game.getCurrentPlayer()));
        return (takerandomCard(game.getCurrentPlayer(),game.getTargetPlayer()));

    }

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
    public void sendMessageToAllOtherPlayers(String message) {
        for (EKCHandler handler : clients) {
            if (handler.getName() != game.getCurrentPlayer().getName()) {
                handler.sendMessage(message);
            }
        }
    }
    public void sendMessageToAllPlayers(String message) {
        for (EKCHandler handler : clients) {
            handler.sendMessage(message);
        }
    }
    public void playDefuse(int index) {
        game.playDefuse(index);
    }

    public String drawPileSize() {
        return ProtocolMessages.RESPONSE_DRAW_PILE_SIZE + ProtocolMessages.DELIMITER + game.getDeckLength();
    }
    public String userHandSize(String player) {
        Player1 player1 = game.getPlayer(player);
        if (player1 == null) {
        return ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER+"Player not found.";
        }
        return ProtocolMessages.RESPONSE_USERS_HAND_SIZE + ProtocolMessages.DELIMITER + player1.getHandList().size();
//        game.getClientTui().userHandSizeMessage(player.getName(), player.getHandList().size());
    }

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






/////////////////////main//////////////////////////////////////
    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        System.out.println("Welcome to the Game Server! Starting...");
        new Thread(gameServer).start();
    }


    public String requestMandatoryDraws(Player1 player) {
        return (ProtocolMessages.RESPONSE_MANDATORY_DRAWS+ ProtocolMessages.DELIMITER+ (player.getExtraTurns()+1));
    }

    public void endGame() {
        for (EKCHandler handler : clients) {
            handler.sendMessage(ProtocolMessages.GAME_FINISHED + ProtocolMessages.DELIMITER + game.getWinner().getName());
        }
    }

    public void sendChat(String s , String name) {
        for (EKCHandler handler : clients) {
            if (handler.getName() != name) {
                handler.sendMessage(s);
            }
        }
    }
}