package ExplodingKittens.Controller;

import ExplodingKittens.Model.CardType;

import java.io.*;
import java.net.Socket;

public class EKCHandler implements Runnable {
    /** The socket and In- and OutputStreams */
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;

    /** The connected HotelServer */
    private GameServer server;

    /** Name of this ClientHandler */
    private String name;
    /**
     * Constructs a new EKCHandler. Opens the In- and OutputStreams.
     *
     * @param sock The client socket
     * @param srv  The connected server
     * @param name The name of this ClientHandler
     */
    public EKCHandler(Socket sock, GameServer srv, String name) {
        try {
            in = new BufferedReader(
                    new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.server = srv;
            this.name = name;
        } catch (IOException e) {
            shutdown();
        }
    }
    /**
     * Continuously listens to client input and forwards the input to the
     * {@link #handleCommand(String)} method.
     */
    public void run() {
        try {
            String msg = in.readLine();
            while (msg != null) {
                System.out.println("> [" + name + "] Incoming: " + msg);
                handleCommand(msg);
                msg = in.readLine();
            }
//			shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }
    /**
     * Handles commands received from the client by calling the according
     * methods at the HotelServer. For example, when the message "i Name"
     * is received, the method doIn() of HotelServer should be called
     * and the output must be sent to the client.
     *
     * If the received input is not valid, send an "Unknown Command"
     * message to the server.
     *
     * @param msg command from client
     * @throws IOException if an IO errors occur.
     */
    public void handleCommand(String msg) throws IOException {
        String name = null;

        String[] words = msg.split(ProtocolMessages.DELIMITER);
        String command = words[0];
        switch (command) {
            case ProtocolMessages.HI:
                out.write(ProtocolMessages.HI + ";" + server.getGameName());
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.CONNECT:
                server.addPlayer(words[1]);
                out.write(ProtocolMessages.CONNECTED);
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.REQUEST_GAME:
                if(server.playersLobbySize() >=2 && Integer.parseInt(words[1]) == server.playersLobbySize()) {
                    server.startGame();
                    out.write(ProtocolMessages.GAME_STARTED);
                    out.newLine();
                    out.flush();
                } else if((Integer.parseInt(words[1]) >=2)) {
                    if (server.playersLobbySize() >= Integer.parseInt(words[1])) {
                        out.write(ProtocolMessages.GAME_STARTED);
                        server.startGame();
                        out.newLine();
                        out.flush();
                    } else if ((Integer.parseInt(words[1]) > server.playersLobbySize())) {
                        for (int i = server.playersLobbySize(); i < Integer.parseInt(words[1]); i++) {
                            server.addPlayer("Computer player" + i);
                        }
                        out.write(ProtocolMessages.GAME_STARTED);
                        server.startGame();
                        out.newLine();
                        out.flush();
                    }
                }
                break;
            case ProtocolMessages.PLAY_CARD:
                out.write(ProtocolMessages.GENERAL_CARD_RESPONSE + ProtocolMessages.DELIMITER + words[1]);
                server.playCardcmd(CardType.valueOf(words[1]));
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.DRAW_CARD:
                server.drawCard();
                out.write(server.getGame().getCurrentPlayer().getName() + "drew a card");
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.CHOOSE_CARD_IN_HAND:
                server.chooseCardInHand(CardType.valueOf(words[1]));
                out.write("Gave a card");
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.PLAY_FAVOR:
                server.playFavor(server.getGame().getPlayer(words[1]));
                out.write("Played favor");
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.PLAY_COMBO:
                if (words[2].equals("2")){
                    server.getGame().setTargetPlayer(server.getGame().getPlayer(words[3]));
                    server.playCombo2(CardType.valueOf(words[1]));
                    out.write("Played favor");
                    out.newLine();
                    out.flush();
                    break;
                } else if (words[2].equals("3")){
                    server.getGame().setTargetPlayer(server.getGame().getPlayer(words[3]));
                    server.playCombo3();
                    out.write("Played favor");
                    out.newLine();
                    out.flush();
                    break;
                } else {
                    throw new IllegalArgumentException("illegal command");
                }

            case ProtocolMessages.PLAY_DEFUSE:
                server.playFavor(server.getGame().getPlayer(words[1]));
                out.write("Played favor");
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.EXIT:
                shutdown();
                break;
            default:
                throw new IllegalArgumentException("Empty command");

                out.write("Command not found");
                out.newLine();
                out.flush();
                throw new IllegalArgumentException("Empty command");
        }
        }

    private void shutdown() {
        System.out.println("> [" + name + "] Shutting down.");
        try {
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeClient(this);
    }
}
