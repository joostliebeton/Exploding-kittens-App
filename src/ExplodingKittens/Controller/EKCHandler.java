package ExplodingKittens.Controller;

import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;

import javax.management.monitor.StringMonitor;
import java.io.*;
import java.net.Socket;
import java.util.logging.Handler;

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

        String[] words = msg.split(ProtocolMessages.DELIMITER);
        String command = words[0];
        switch (command) {
            case ProtocolMessages.HI:
                out.write(ProtocolMessages.HI + ProtocolMessages.DELIMITER + server.getGameName());
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.CONNECT:
                server.addPlayer(words[1]);
                this.name = words[1];
                out.write(ProtocolMessages.CONNECTED);
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.REQUEST_GAME:
                if(server.getGame().getPlayers().size() >=2 && Integer.parseInt(words[1]) ==server.getGame().getPlayers().size()) {
                    out.write(ProtocolMessages.GAME_STARTED);
                    out.newLine();
                    out.flush();
                    server.startGameProcess();
                    out.newLine();
                    out.flush();
                } else if((Integer.parseInt(words[1]) >=2)) {
                    if (server.getGame().getPlayers().size() >= Integer.parseInt(words[1])) {
                        out.write(ProtocolMessages.GAME_STARTED);
                        out.newLine();
                        out.flush();
                        server.startGameProcess();
                        out.newLine();
                        out.flush();
                    } else if ((Integer.parseInt(words[1]) > server.getGame().getPlayers().size())) {
                        for (int i = server.getGame().getPlayers().size(); i < Integer.parseInt(words[1]); i++) {
                            server.addComputerplayer("Computer" + i);
                        }
                        out.write(ProtocolMessages.GAME_STARTED);
                        out.newLine();
                        out.flush();
                        server.startGameProcess();
                        out.newLine();
                        out.flush();
                    }
                }
                break;
            case ProtocolMessages.PLAY_CARD:
                out.write(ProtocolMessages.GENERAL_CARD_RESPONSE + ProtocolMessages.DELIMITER + words[1]);
                out.newLine();
                out.flush();
                out.write(server.playCardcmd(CardType.valueOf(words[1])));
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
                    server.playCombo3(CardType.valueOf(words[1]));
                    out.write("Played favor");
                    out.newLine();
                    out.flush();
                    break;
                } else {
                    throw new IllegalArgumentException("illegal command");
                }
            case ProtocolMessages.PLAY_DEFUSE:
                server.playDefuse(Integer.parseInt(words[1]));
                out.write("Played defuse");
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.DRAW_PILE_SIZE:
                out.write(server.drawPileSize());
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.USERS_HAND_SIZE:
                out.write(server.userHandSize(words[1]));
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.REQUEST_ALIVE_PLAYERS:
                out.write(server.requestAlivePlayers());
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.REQUEST_PLAYERS_LOBBY:
                out.write(server.requestAlivePlayers());
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.REQUEST_CARDS_IN_HAND:
                out.write(server.requestCardsInHand(this.name));
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.GENERAL_CARD_RESPONSE:
                out.write((server.giveCard(new Card(CardType.valueOf(words[1])))));
                out.newLine();
                out.flush();
            default:
                out.write("Command not found");
                out.newLine();
                out.flush();
                break;
//                throw new IllegalArgumentException("Empty command");

        }

    }

    private void shutdown() {
        System.out.println("> [" + name + "] Shutting down.");
        try {
            server.getGame().eliminatePlayer(server.getGame().getPlayer(name));
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeClient(this);
    }

    public Object getName() {
        return name;
    }

    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
