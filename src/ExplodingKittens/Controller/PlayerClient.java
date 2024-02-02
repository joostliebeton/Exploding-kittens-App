package ExplodingKittens.Controller;

import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;
import ExplodingKittens.Model.Player1;
import ExplodingKittens.View.ClientTUI;
import ExplodingKittens.exceptions.ExitProgram;
import ExplodingKittens.exceptions.ProtocolException;
import ExplodingKittens.exceptions.ServerUnavailableException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Random;

public class PlayerClient {
    public Socket serverSock;
    public BufferedReader in;
    public BufferedWriter out;
    private ClientTUI playerClientTUI;
    private Player1 player;
    private String name;
    /**
     * Constructs a new PlayerClient. Initialises the view.
     */
    public PlayerClient() {
        ///implement//////
        this.playerClientTUI = new ClientTUI(this);

        // To be implemented
    }

    /**
     * Starts a new GameClient by creating a connection, followed by the
     * HELLO handshake as defined in the protocol. After a successful
     * connection and handshake, the view is started. The view asks for
     * used input and handles all further calls to methods of this class.
     *
     * When errors occur, or when the user terminates a server connection, the
     * user is asked whether a new connection should be made.
     */
    /**
     * Starts the client by creating a connection to the server and handling the handshake.
     * If the server is unavailable, it prompts the user to retry.
     * @requires ClientTUI instance to be initialized.
     * @ensures Client connection is established and handshake is completed.
     */

    public void start() {
        boolean trying = true;
        while (trying){
            try {
                createConnection();
                try {
                    handleHello();
                    ///game.start(); ????? or fill in name????
                    playerClientTUI.start();
                    trying = false;
                } catch (ServerUnavailableException e) {
                    System.out.println("Error " + e);
                    System.out.println("New connection (yes/no)? ");
                    try {
                        String input = in.readLine();
                        if (input.equalsIgnoreCase("yes")) {
                            start();
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (ProtocolException | ExitProgram e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Creates a connection to the server. Requests the IP and port to
     * connect to at the view (TUI).
     *
     * The method continues to ask for an IP and port and attempts to connect
     * until a connection is established or until the user indicates to exit
     * the program.
     *
     * @throws ExitProgram if a connection is not established and the user indicates to want to exit the program.
     *  @requires ClientTUI instance to be initialized.
     *
     * @ensures serverSock contains a valid socket connection to a server
     */
    public void createConnection() throws ExitProgram {
        clearConnection();
        while (serverSock == null) {
            String host = playerClientTUI.getString("Please enter the server IP.");
            //String host = "127.0.0.1";
            // host = playerClientTUI.getString("Please enter the server IP.");
            //String host = "127.0.0.1";
            //String host = "145.126.38.21";
            int port = playerClientTUI.getInt("Please enter the server port.");
            //int port = 8888;
            try {
                this.name = playerClientTUI.getString("Please enter your name.");
                while (this.name.isEmpty() || this.name.isBlank()) {
                    this.name = playerClientTUI.getString("Please enter your name.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid name.");
                return;
            }

            // try to open a Socket to the server
            try {
                InetAddress addr = InetAddress.getByName(host);
                System.out.println("Attempting to connect to " + addr + ":"
                        + port + "...");
                serverSock = new Socket(addr, port);
                in = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(serverSock.getOutputStream()));
            } catch (IOException e) {
                System.out.println("ERROR: could not create a socket on "
                        + host + " and port " + port + ".");

                //Do you want to try again? (ask user, to be implemented)
                boolean question = playerClientTUI.getBoolean("Do you want to try again? ");
                if(!question) {
                    throw new ExitProgram("User indicated to exit.");
                }
            }
        }
    }

    /**
     * Resets the serverSocket and In- and OutputStreams to null.
     * @ensures ServerSock, in, and out are set to null.
     */
    public void clearConnection() {
        serverSock = null;
        in = null;
        out = null;
    }
    /**
     * Sends a message to the connected server, followed by a new line.
     * The stream is then flushed.
     *
     * @param msg the message to write to the OutputStream.
     * @throws ServerUnavailableException if IO errors occur.
     * @requires out to be initialized.
     * @ensures Message is sent to the server
     */
    public synchronized void sendMessage(String msg)
            throws ServerUnavailableException {
        if (out != null) {
            try {
                out.write(msg);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new ServerUnavailableException("Could not write "
                        + "to server.");
            }
        } else {
            throw new ServerUnavailableException("Could not write "
                    + "to server.");
        }
    }
    /**
     * Reads and returns one line from the server.
     *
     * @return the line sent by the server.
     *  @ensures Messages from the server are processed accordingly.
     * @throws ServerUnavailableException if IO errors occur.
     */
    private void readFromServer() {
        try {
            String answer;
            while ((answer = in.readLine()) != null) {
                String command = answer.split(ProtocolMessages.DELIMITER)[0];
                String[] entries = answer.split(ProtocolMessages.DELIMITER);
                switch (command) {
                    case ProtocolMessages.TURN:
                        if (this.name.equals(answer.split(ProtocolMessages.DELIMITER)[1])) {
                            if (this.name.contains("Computer")) {
                                sendMessage(ProtocolMessages.DRAW_CARD);
                            }
                            playerClientTUI.showMessage("it's your turn");
                        } else {
                            playerClientTUI.showMessage("It's " + answer.split(ProtocolMessages.DELIMITER)[1] + "'s turn!");
                        }
                        break;
                    case ProtocolMessages.RESPONSE_ALIVE_PLAYERS:
                        for (int i = 1; i < entries.length; i++) {
                            playerClientTUI.showMessage(entries[i]);
                        }
                        break;
                    case ProtocolMessages.PICK_CARD_IN_HAND:
                        playerClientTUI.showMessage("you where chosen by the favor card");
                        playerClientTUI.showMessage("Please enter the card you want to give");
                        if (this.name.contains("Computer")) {
                            ArrayList<Card> cards = new ArrayList<>();
                            sendMessage(ProtocolMessages.REQUEST_CARDS_IN_HAND);
                            for (int i = 1; i < entries.length; i++) {
                                cards.add(new Card(CardType.valueOf(entries[i])));
                            }
                            sendMessage(ProtocolMessages.CHOOSE_CARD_IN_HAND + ProtocolMessages.DELIMITER +cards.get(1));
                        }
                            break;
                    case ProtocolMessages.GAME_STARTED:
                        playerClientTUI.showMessage("The game has started");
                        break;
                    case ProtocolMessages.GENERAL_CARD_RESPONSE:
                        playerClientTUI.showMessage("a " + answer.split(ProtocolMessages.DELIMITER)[1] + " has been played");
                        break;
                    case ProtocolMessages.REQUEST_CARD_IN_HAND_RESPONSE:
                        for (int i = 1; i < answer.split(ProtocolMessages.DELIMITER).length; i++) {
                            playerClientTUI.showMessage(answer.split(ProtocolMessages.DELIMITER)[i]);
                        }
                        playerClientTUI.showMessage("these are the cards in your hand");
                        break;
                    case ProtocolMessages.PLAY_NOPED:
                        playerClientTUI.showMessage("You have the ability to nope");
                        playerClientTUI.showMessage("if you want to nope do Play NOPE, otherwise Refuse NOPE");
                        if (this.name.contains("Computer")) {
                            Random random = new Random();
                            int randomNumber = random.nextInt(2);
                            if (randomNumber == 0) {
                                this.sendMessage(ProtocolMessages.REFUSE_NOPE);
                            } else {
                                this.sendMessage(ProtocolMessages.PLAY_CARD + ProtocolMessages.DELIMITER + "NOPE");
                            }
                        }
                            break;
                    case ProtocolMessages.CARD_RECEIVED:
                        playerClientTUI.showMessage( "You have received a" + answer.split(ProtocolMessages.DELIMITER)[3] + "Card from" + answer.split(ProtocolMessages.DELIMITER)[2] +
                                "because of the " + answer.split(ProtocolMessages.DELIMITER)[1] + "card");
                        break;
                    case ProtocolMessages.GAME_FINISHED:
                        if (this.name.equals(answer.split(ProtocolMessages.DELIMITER)[1])){
                            playerClientTUI.showMessage("The game has finished, you are the winner");
                        } else {
                            playerClientTUI.showMessage("The game has finished, you are a loser");
                            playerClientTUI.showMessage("The winner is " + answer.split(ProtocolMessages.DELIMITER)[1]);
                        }
                        closeConnection();
                        break;
                    case ProtocolMessages.DRAWN:
                        if (entries[1].equals("EXPLODING_KITTEN")) {
                            playerClientTUI.showMessage("You have drawn an exploding kitten. \n luckily you can defuse it with your defuse card. return play DEFUSE (index)");
                            if (this.name.contains("Computer")) {
                                Random random = new Random();
                                int randomNumber = random.nextInt(8);
                                sendMessage(ProtocolMessages.PLAY_DEFUSE + ProtocolMessages.DELIMITER + randomNumber);
                            }
                        }
                        playerClientTUI.showMessage("You have drawn a " + entries[1] + " card");
                        break;
                    case ProtocolMessages.CHAT:
                        playerClientTUI.showMessage(answer.split(ProtocolMessages.DELIMITER)[1] + ": " + answer.split(ProtocolMessages.DELIMITER)[2]);
                        break;
                    default:
                        playerClientTUI.showMessage(answer);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServerUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the line sent by the server.
     * @throws ServerUnavailableException if IO errors occur.
     * @requires in to be initialized.
     * @ensures A line is read from the server.
     */
        public String readLineFromServer() throws ServerUnavailableException {
            if (in != null) {
                try {
                    // Read and return answer from Server
                    return in.readLine();
                } catch (IOException e) {
                    throw new ServerUnavailableException("Could not read "
                            + "from server.");
                }
            }
            return null;
        }
    /**
     * Closes the connection to the server.
     * @ensures Connection to the server is closed.
     */
    public void closeConnection() {
        System.out.println("Closing the connection...");
        try {
            in.close();
            out.close();
            serverSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the initial handshake with the server.
     * @throws ServerUnavailableException if IO errors occur.
     * @throws ProtocolException if the protocol is violated.
     * @throws IOException if IO errors occur.
     * @requires ServerSock, in, and out to be initialized.
     * @ensures Handshake with the server is completed.
     */
    public void handleHello() throws ServerUnavailableException, ProtocolException, IOException {
// Send HELLO
        sendMessage((ProtocolMessages.HI + ProtocolMessages.DELIMITER + "CHAT" ));
// Read answer from Server

        String answer = readLineFromServer();
// Check if it is HELLO, split on delimiter
        String [] splitted = answer.split(ProtocolMessages.DELIMITER);
        if (splitted[0].equalsIgnoreCase(ProtocolMessages.HI )) {
            doConnect(name);

            System.out.println("Welcome to the game "
                    + "of Game: " + splitted[1] + "!");
            Thread readerThread = new Thread(this::readFromServer);
            readerThread.start();
        } else {
// Throw protocol exception
            throw new ProtocolException ("No HI returned. "
                    + "Instead: " + answer );
        }
    }
    /**
     * Connects to the server with the specified name.
     * @param name the name to connect with.
     * @throws ServerUnavailableException if connection to the server fails.
     * @requires name != null
     * @ensures Connection to the server is established with the specified name.
     */

    public void doConnect(String name) throws ServerUnavailableException {
        if(name != null) {
            sendMessage(ProtocolMessages.CONNECT + ProtocolMessages.DELIMITER + name);
            //playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }
    /**
     * Requests the list of players from the server lobby.
     * @throws ServerUnavailableException if connection to the server fails.
     * @ensures List of players in the lobby is requested.
     */
   public void requestPlayers() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.REQUEST_PLAYERS_LOBBY);
        //playerClientTUI.showMessage("> " + readLineFromServer());
    }
    /**
     * Requests the amount of players in the lobby from the server.
     * @throws ServerUnavailableException if connection to the server fails.
     * @ensures Amount of players in the lobby is requested.
     */
    public void requestAmountOfPlayers() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.REQUEST_PLAYERS_LOBBY);
        //playerClientTUI.showMessage("> " + readLineFromServer());
    }
    /**
     * Plays the specified card.
     * @param card the card to play.
     * @throws ServerUnavailableException if connection to the server fails.
     * @requires card != null
     * @ensures The specified card is played.
     */

    public void doPlay(String card) throws ServerUnavailableException {
        if(card != null) {
            sendMessage(ProtocolMessages.PLAY_CARD+ ProtocolMessages.DELIMITER + card);
            //playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }
    /**
     * Requests the cards in hand for the specified player from the server.
     * @param player the player whose hand to request.
     * @throws ServerUnavailableException if connection to the server fails.
     * @requires player != null
     * @ensures Cards in hand for the specified player are requested.
     */
    public void doRequestCardsInHand(String player) throws ServerUnavailableException {
        if(player != null) {
            sendMessage(ProtocolMessages.USERS_HAND_SIZE + ProtocolMessages.DELIMITER + player);
            //playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }
    /**
     * Requests the cards in hand from the server.
     * @throws ServerUnavailableException if connection to the server fails.
     * @ensures Cards in hand are requested.
     */
    public void doRequestCardsInHandtype() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.REQUEST_CARDS_IN_HAND);
        //playerClientTUI.showMessage(">" + readLineFromServer());

    }
    /**
     * Requests to start a game with the specified number of players and AI players.
     * @param numberOfPlayers the number of human players.
     * @param aiPlayers the number of AI players.
     * @throws ServerUnavailableException if connection to the server fails.
     * @requires numberOfPlayers != null && aiPlayers != null
     * @ensures A game is requested to be started with the specified settings.
     */
    public void doStartGameRequest(String numberOfPlayers, String aiPlayers) throws ServerUnavailableException {
        if(numberOfPlayers != null && Objects.equals(aiPlayers, "0")) {
            sendMessage(ProtocolMessages.REQUEST_GAME + ProtocolMessages.DELIMITER + numberOfPlayers);
            //playerClientTUI.showMessage("> " + readLineFromServer());
            //playerClientTUI.showMessage("> " + readLineFromServer());
        } else{
            sendMessage(ProtocolMessages.REQUEST_GAME + ProtocolMessages.DELIMITER + numberOfPlayers + ProtocolMessages.DELIMITER + aiPlayers);
            //playerClientTUI.showMessage("> " + readLineFromServer());
            //playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }
    /**
     * Plays the "favor" action card on the specified player.
     * @param enterPlayerName the name of the player to favor.
     * @throws ServerUnavailableException if connection to the server fails.
     * @requires enterPlayerName != null
     * @ensures The "favor" action card is played on the specified player.
     */
    public void doFavor(String enterPlayerName) throws ServerUnavailableException {
        if(enterPlayerName != null) {
            sendMessage(ProtocolMessages.PLAY_FAVOR + ProtocolMessages.DELIMITER + enterPlayerName);
            //playerClientTUI.showMessage("> " + readLineFromServer());
            //playerClientTUI.showMessage("> " + readLineFromServer());

        }
    }
    public static void main(String[] args) {
        (new PlayerClient()).run();
    }
    /**
     * Starts the client.
     * @ensures The client is started.
     */
    public void run(){
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Gives the specified card.
     * @param enterCardType the type of card to give.
     * @throws ServerUnavailableException if connection to the server fails.
     * @requires enterCardType != null
     * @ensures The specified card is given.
     */

    public void doGiveCard(String enterCardType) throws ServerUnavailableException {
        if(enterCardType != null) {
            sendMessage(ProtocolMessages.CHOOSE_CARD_IN_HAND + ProtocolMessages.DELIMITER + enterCardType);
//            playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }
    /**
     * Draws a card.
     * @throws ServerUnavailableException if connection to the server fails.
     * @ensures A card is drawn.
     */
    public void doDrawCard() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.DRAW_CARD);
//        playerClientTUI.showMessage("> " + readLineFromServer());
//        playerClientTUI.showMessage("> " + readLineFromServer());

    }
    /**
     * Plays a combo of cards.
     * @param input first input for combo.
     * @param input1 second input for combo.
     * @param input2 third input for combo.
     * @throws ServerUnavailableException if connection to the server fails.
     * @ensures The combo of cards is played.
     */
    public void PlayCombo(String input, String input1, String input2) throws ServerUnavailableException {
        sendMessage(ProtocolMessages.PLAY_COMBO + ProtocolMessages.DELIMITER + input + ProtocolMessages.DELIMITER + input1+ProtocolMessages.DELIMITER + input2);
    }
    /**
     * Plays the "nope" action card.
     * @throws ServerUnavailableException if connection to the server fails.
     * @ensures The "nope" action card is played.
     */
    public void doNope() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.PLAY_CARD + ProtocolMessages.DELIMITER + "NOPE");
    }
    /**
     * Requests the amount of mandatory draws from the server.
     * @throws ServerUnavailableException if connection to the server fails.
     * @ensures The amount of mandatory draws is requested.
     */
    public void requestDrawAmount() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.REQUEST_MANDATORY_DRAWS);
    }
    /**
     * Refuses the "nope" action card.
     * @throws ServerUnavailableException if connection to the server fails.
     * @ensures The "nope" action card is refused.
     */
    public void doRefuseNope() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.REFUSE_NOPE);
    }
    /**
     * Plays the "defuse" action card with the specified input.
     * @param input the input for the "defuse" action card.
     * @throws ServerUnavailableException if connection to the server fails.
     * @ensures The "defuse" action card is played with the specified input.
     */
    public void PlayDefuse(String input) throws ServerUnavailableException {
        sendMessage(ProtocolMessages.PLAY_DEFUSE + ProtocolMessages.DELIMITER + input);
    }
}
