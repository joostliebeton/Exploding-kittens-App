package ExplodingKittens.Controller;

import ExplodingKittens.Model.Player1;
import ExplodingKittens.View.ClientTUI;
import ExplodingKittens.exceptions.ExitProgram;
import ExplodingKittens.exceptions.ProtocolException;
import ExplodingKittens.exceptions.ServerUnavailableException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;

public class PlayerClient {
    private Socket serverSock;
    private BufferedReader in;
    private BufferedWriter out;
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
     * Starts a new HotelClient by creating a connection, followed by the
     * HELLO handshake as defined in the protocol. After a successful
     * connection and handshake, the view is started. The view asks for
     * used input and handles all further calls to methods of this class.
     *
     * When errors occur, or when the user terminates a server connection, the
     * user is asked whether a new connection should be made.
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
     * @throws ExitProgram if a connection is not established and the user
     * 				       indicates to want to exit the program.
     * @ensures serverSock contains a valid socket connection to a server
     */
    public void createConnection() throws ExitProgram {
        clearConnection();
        while (serverSock == null) {
            String host = playerClientTUI.getString("Please enter the server IP.");
            int port = playerClientTUI.getInt("Please enter the server port.");
            this.name = playerClientTUI.getString("Please enter your name.");

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
//    private void handleUserInput() {
//        try (BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in))) {
//            String userInput;
//            while ((userInput = userInputReader.readLine()) != null) {
//                out.write(userInput);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Resets the serverSocket and In- and OutputStreams to null.
     *
     * Always make sure to close current connections via shutdown()
     * before calling this method!
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
     * @throws ServerUnavailableException if IO errors occur.
     */
    private void readFromServer() {
        try {
            String answer;
            while ((answer = in.readLine()) != null) {
                String command = answer.split(ProtocolMessages.DELIMITER)[0];
                switch (command) {
                    case ProtocolMessages.HI:
//                        doConnect(name);
//                        System.out.println("Welcome to the game "
//                                + "of Game: " +answer.split(ProtocolMessages.DELIMITER)[1] + "!");
//                        out.newLine();
//                        out.flush();
                        break;
                    case ProtocolMessages.TURN:
                        if (this.name.equals(answer.split(ProtocolMessages.DELIMITER)[1])) {
                            playerClientTUI.showMessage("it's your turn");
                            //playerClientTUI.showMessage("It's your turn!");
                        } else {
                            //playerClientTUI.showMessage("It's " + answer.split(ProtocolMessages.DELIMITER)[1] + "'s turn!");
                            playerClientTUI.showMessage("It's " + answer.split(ProtocolMessages.DELIMITER)[1] + "'s turn!");
                        }
                        break;
                    case ProtocolMessages.RESPONSE_ALIVE_PLAYERS:
                        for (int i = 1; i < answer.split(ProtocolMessages.DELIMITER).length; i++) {
                            playerClientTUI.showMessage(answer.split(ProtocolMessages.DELIMITER)[i]);
                        }
                        playerClientTUI.showMessage(answer);
                        break;
                    case ProtocolMessages.GENERAL_CARD_REQUEST:
                        playerClientTUI.showMessage("you where chosen by the favor card");
                        doGiveCard(playerClientTUI.getString("Please enter the card you want to give"));
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
                        if (playerClientTUI.getBoolean("Do you want to nope?")) {
                            sendMessage(ProtocolMessages.PLAY_NOPED);
                        } else {
                            playerClientTUI.showMessage("You have chosen not to nope");
                        }
                            break;
                    case ProtocolMessages.CARD_RECEIVED:
                        playerClientTUI.showMessage( "You have received a" + answer.split(ProtocolMessages.DELIMITER)[3] + "Card from" + answer.split(ProtocolMessages.DELIMITER)[2] +
                                "because of the " + answer.split(ProtocolMessages.DELIMITER)[1] + "card");
                        break;

                    default:
                        playerClientTUI.showMessage(answer);
                        break;
                }
            }
        } catch (IOException | ServerUnavailableException e) {
            e.printStackTrace();
        }
    }
    public String readLineFromServer() throws ServerUnavailableException {
            if (in != null) {
                try {
                    // Read and return answer from Server
                    String answer = in.readLine();
                    if (answer == null) {
                        throw new ServerUnavailableException("Could not read "
                                + "from server.");
                    }
                    String command = answer.split(ProtocolMessages.DELIMITER)[0];
                    switch (command) {
                        case ProtocolMessages.HI:
                            return answer;
                        case ProtocolMessages.TURN:
                            if (this.name.equals(answer.split(ProtocolMessages.DELIMITER)[1])) {
                                return "it's your turn";
                                //playerClientTUI.showMessage("It's your turn!");
                            } else {
                                //playerClientTUI.showMessage("It's " + answer.split(ProtocolMessages.DELIMITER)[1] + "'s turn!");
                                return "It's " + answer.split(ProtocolMessages.DELIMITER)[1] + "'s turn!";
                            }
                        case ProtocolMessages.RESPONSE_ALIVE_PLAYERS:
                            for (int i = 1; i < answer.split(ProtocolMessages.DELIMITER).length; i++) {
                                playerClientTUI.showMessage(answer.split(ProtocolMessages.DELIMITER)[i]);
                            }
                            return answer;
                        case ProtocolMessages.GENERAL_CARD_REQUEST:
                            playerClientTUI.showMessage("you where chosen by the favor card");
                            doGiveCard(playerClientTUI.getString("Please enter the card you want to give"));
                            break;
                        case ProtocolMessages.GAME_STARTED:
                            return "The game has started";
                        case ProtocolMessages.GENERAL_CARD_RESPONSE:
                            return "a " + answer.split(ProtocolMessages.DELIMITER)[1] + " has been played";
                        case ProtocolMessages.REQUEST_CARD_IN_HAND_RESPONSE:
                            for (int i = 1; i < answer.split(ProtocolMessages.DELIMITER).length; i++) {
                                playerClientTUI.showMessage(answer.split(ProtocolMessages.DELIMITER)[i]);
                            }
                            return "these are the cards in your hand";
                        case ProtocolMessages.PLAY_NOPED:
                            playerClientTUI.showMessage("You have the ability to nope");
                            playerClientTUI.getBoolean("Do you want to nope?");
                        case ProtocolMessages.CARD_RECEIVED:
                            return "You have received a" + answer.split(ProtocolMessages.DELIMITER)[3] + "Card from" + answer.split(ProtocolMessages.DELIMITER)[2] +
                                    "because of the " + answer.split(ProtocolMessages.DELIMITER)[1] + "card";
                        default:
                            return answer;
                    }

                } catch (ServerUnavailableException e) {
                    throw new ServerUnavailableException("Could not read "
                            + "from server.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new ServerUnavailableException("Could not read "
                        + "from server.");
            }            return null;

        }
    public String readMultipleLinesFromServer()
            throws ServerUnavailableException {
        if (in != null) {
            try {

                // Read and return answer from Server
                StringBuilder sb = new StringBuilder();
                for (String line = in.readLine(); line != null; line = in.readLine()) {
                    sb.append(line + System.lineSeparator());
                }
                return sb.toString();
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not read "
                        + "from server.");
            }
        } else {
            throw new ServerUnavailableException("Could not read "
                    + "from server.");
        }
    }
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
    public void handleHello() throws ServerUnavailableException, ProtocolException, IOException {
// Send HELLO
        sendMessage((ProtocolMessages.HI ));
// Read answer from Server
        String answer = readLineFromServer();
// Check if it is HELLO, split on delimiter
        String [] splitted = answer.split(ProtocolMessages.DELIMITER);
        if (splitted[0].equalsIgnoreCase(ProtocolMessages.HI )) {
            doConnect(name);

            System.out.println("Welcome to the game "
                    + "of Game: " + splitted[1] + "!");
            out.newLine();
            out.flush();
            Thread readerThread = new Thread(this::readFromServer);
            readerThread.start();
        } else {
// Throw protocol exception
            throw new ProtocolException ("No HI returned. "
                    + "Instead: " + answer );
        }
    }


    public void doConnect(String name) throws ServerUnavailableException {
        if(name != null) {
            sendMessage((ProtocolMessages.HI ));
            sendMessage(ProtocolMessages.CONNECT + ProtocolMessages.DELIMITER + name);
            //playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }
   public void requestPlayers() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.REQUEST_PLAYERS_LOBBY);
        //playerClientTUI.showMessage("> " + readLineFromServer());
    }
    public void requestAmountOfPlayers() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.REQUEST_PLAYERS_LOBBY);
        //playerClientTUI.showMessage("> " + readLineFromServer());
    }

    public void doPlay(String card) throws ServerUnavailableException {
        if(card != null) {
            sendMessage(ProtocolMessages.PLAY_CARD+ ProtocolMessages.DELIMITER + card);
            //playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }
    public void doRequestCardsInHand(String player) throws ServerUnavailableException {
        if(player != null) {
            sendMessage(ProtocolMessages.USERS_HAND_SIZE + ProtocolMessages.DELIMITER + player);
            //playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }
    public void doRequestCardsInHandtype() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.REQUEST_CARDS_IN_HAND);
        //playerClientTUI.showMessage(">" + readLineFromServer());

    }
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
    public void run(){
        try {

            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendExit() {
        //To be implemented
    }
    public void doGiveCard(String enterCardType) throws ServerUnavailableException {
        if(enterCardType != null) {
            sendMessage(ProtocolMessages.CHOOSE_CARD_IN_HAND + ProtocolMessages.DELIMITER + enterCardType);
//            playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }

    public void doDrawCard() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.DRAW_CARD);
//        playerClientTUI.showMessage("> " + readLineFromServer());
//        playerClientTUI.showMessage("> " + readLineFromServer());

    }

    public void PlayCombo(String input, String input1) throws ServerUnavailableException {
        sendMessage(ProtocolMessages.PLAY_COMBO + ProtocolMessages.DELIMITER + input + ProtocolMessages.DELIMITER + input1);
    }
}
