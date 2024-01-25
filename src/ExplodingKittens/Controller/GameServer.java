package ExplodingKittens.Controller;

import ExplodingKittens.View.ServerTUI;
import ExplodingKittens.exceptions.ExitProgram;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer implements Runnable{
    private ServerSocket ssock;

    /** List of HotelClientHandlers, one for each connected client */
    private List<EKCHandler> clients;

    /** Next client number, increasing for every new connection */
    private int next_client_no;

    /** The view of this HotelServer */
    private ServerTUI view;

    /** The name of the Hotel */
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
     *
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
                    String name = "Client "
                            + String.format("%02d", next_client_no++);
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
     *
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
            int port = view.getInt("Please enter the server port.");

            // try to open a new ServerSocket
            try {
                view.showMessage("Attempting to open a socket at 127.0.0.1 "
                        + "on port " + port + "...");
                ssock = new ServerSocket(port, 0,
                        InetAddress.getByName("127.0.0.1"));
                view.showMessage("Server started at port " + port);
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on "
                        + "127.0.0.1" + " and port " + port + ".");

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
        gameName = view.getString("Please enter the name of the hotel.");
        game = new Game(gameName);
        // To be implemented.
    }
    public void removeClient(EKCHandler client) {
        this.clients.remove(client);
    }
//////////////////////////server methods///////////////////////// 0
    public void addPLayer







/////////////////////main//////////////////////////////////////
public static void main(String[] args) {
    GameServer gameServer = new GameServer();
    System.out.println("Welcome to the Game Server! Starting...");
    new Thread(gameServer).start();
}
}