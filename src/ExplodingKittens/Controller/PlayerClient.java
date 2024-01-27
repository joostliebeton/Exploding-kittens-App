package ExplodingKittens.Controller;

import ExplodingKittens.Model.Player;
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
    private Player player;
    /**
     * Constructs a new PlayerClient. Initialises the view.
     */
    public PlayerClient() {
        ///implement//////
        this.playerClientTUI = new ClientTUI(this);
        this.player = new Player("Player", null);
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
    public String readLineFromServer()
            throws ServerUnavailableException {
        if (in != null) {
            try {
                // Read and return answer from Server
                String answer = in.readLine();
                if (answer == null) {
                    throw new ServerUnavailableException("Could not read "
                            + "from server.");
                }
                return answer;
            } catch (IOException e) {
                throw new ServerUnavailableException("Could not read "
                        + "from server.");
            }
        } else {
            throw new ServerUnavailableException("Could not read "
                    + "from server.");
        }
    }
//    public String readMultipleLinesFromServer()
//            throws ServerUnavailableException {
//        if (in != null) {
//            try {
//
//                // Read and return answer from Server
//                StringBuilder sb = new StringBuilder();
//                for (String line = in.readLine(); line != null && !line.equals(ProtocolMessages.EOT);
//                     line = in.readLine()) {
//                    sb.append(line + System.lineSeparator());
//                }
//                return sb.toString();
//            } catch (IOException e) {
//                throw new ServerUnavailableException("Could not read "
//                        + "from server.");
//            }
//        } else {
//            throw new ServerUnavailableException("Could not read "
//                    + "from server.");
//        }
//    }
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
            System.out.println("Welcome to the game "
                    + "of Game: " + splitted[1] + "!");
            doConnect(playerClientTUI.getString("what is your name?"));
            out.newLine();
            out.flush();
            System.out.println(readLineFromServer());
        } else {
// Throw protocol exception
            throw new ProtocolException ("No HI returned. "
                    + "Instead: " + answer );
        }
    }
    public void doConnect(String name) throws ServerUnavailableException {
        if(name != null) {
            sendMessage(ProtocolMessages.CONNECT + ProtocolMessages.DELIMITER + name);
            playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }
   public void requestAmountofPlayers() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.REQUEST_PLAYERS_LOBBY);
        playerClientTUI.showMessage("> " + readLineFromServer());
    }
    public void doPlay(String card) throws ServerUnavailableException {
        if(card != null) {
            sendMessage(ProtocolMessages.PLAY_CARD+ ProtocolMessages.DELIMITER + card);
            playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }
    public void doRequestCardsInHand(String player) throws ServerUnavailableException {
        if(player != null) {
            sendMessage(ProtocolMessages.CHOOSE_CARD_IN_HAND + ProtocolMessages.DELIMITER + player);
            playerClientTUI.showMessage("> " + readLineFromServer());
        }
    }
    public void doRequestCardsInHandtype() throws ServerUnavailableException {
        sendMessage(ProtocolMessages.REQUEST_CARDS_IN_HAND);
        playerClientTUI.showMessage("> " + readLineFromServer());

    }
    public void doStartGameRequest(String numberOfPlayers, String aiPlayers) throws ServerUnavailableException {
        if(numberOfPlayers != null && Objects.equals(aiPlayers, "0")) {
            sendMessage(ProtocolMessages.REQUEST_GAME + ProtocolMessages.DELIMITER + numberOfPlayers);
            playerClientTUI.showMessage("> " + readLineFromServer());
        } else{
            sendMessage(ProtocolMessages.REQUEST_GAME + ProtocolMessages.DELIMITER + numberOfPlayers + ProtocolMessages.DELIMITER + aiPlayers);
            playerClientTUI.showMessage("> " + readLineFromServer());

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



}
