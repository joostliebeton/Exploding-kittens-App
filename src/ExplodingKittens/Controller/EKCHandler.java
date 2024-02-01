package ExplodingKittens.Controller;

import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;


import javax.management.monitor.StringMonitor;
import java.io.*;
import java.net.Socket;
import java.util.Objects;
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
    public synchronized void handleCommand(String msg) throws IOException {

        String[] words = msg.split(ProtocolMessages.DELIMITER);
        String command = words[0];
        switch (command) {
            case ProtocolMessages.HI:
                if (words[1] != "CHAT"){
                    server.setChatFunction(false);
                }
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
                if (server.getGame().getPlayers().size() >= 2 && Integer.parseInt(words[1]) == server.getGame().getPlayers().size()) {
                    server.startGameProcess();
                    out.write(ProtocolMessages.GAME_STARTED);
                    out.newLine();
                    out.flush();
                } else if ((Integer.parseInt(words[1]) >= 2)) {
                    if (server.getGame().getPlayers().size() >= Integer.parseInt(words[1])) {
                        server.startGameProcess();
                        out.write(ProtocolMessages.GAME_STARTED);
                        out.newLine();
                        out.flush();
                    } else if ((Integer.parseInt(words[1]) > server.getGame().getPlayers().size())) {
                        out.write(ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + "Not enough players");
                        out.newLine();
                        out.flush();
                    }
                }
                break;
            case ProtocolMessages.PLAY_CARD:
                try {
                    if (words[1].equals("NOPE")) {
                        server.playerWhoNoped = server.getGame().getPlayer(name);
                        server.nopecardPlayed = true;
                        server.nopeanswer = true;
                        server.getGame().getPlayer(this.name).getHand().remove(server.getGame().getPlayer(this.name).getHand().indexOf(CardType.NOPE));
                        server.getGame().playerBeforeNope.getHand().remove(server.getGame().cardBeforeNope);
                        server.sendMessageToAllPlayers(ProtocolMessages.GETS_NOPED + ProtocolMessages.DELIMITER +
                                this.name + ProtocolMessages.DELIMITER + server.getGame().cardBeforeNope.toString() + ProtocolMessages.DELIMITER +
                                server.getGame().getCurrentPlayer().getName());
                        //server.playNopecmd();
//                            out.write(ProtocolMessages.GETS_NOPED + ProtocolMessages.DELIMITER +
//                                    this.name + ProtocolMessages.DELIMITER + server.getGame().playedCard + ProtocolMessages.DELIMITER +
//                                    server.getGame().getCurrentPlayer().getName());
//                            out.newLine();
//                            out.flush();
                        server.nopecardPlayed = false;
                        server.nopeanswer = false;
                        server.turnMessage();
                        break;
                    } else if (Objects.equals(server.getGame().getCurrentPlayer().getName(), name)) {
                        if (words[1].isEmpty()) {
                            out.write("No card selected");
                            out.newLine();
                            out.flush();
                            break;
                        } else if (words[1].equals("SKIP") || words[1].equals("ATTACK")) {
                            out.write(server.playCardcmd(CardType.valueOf(words[1]), server.getGame().getPlayer(name)));
                            out.newLine();
                            out.flush();
                            if (server.getGame().getPlayer(this.name).getExtraTurns() == -1) {
                                server.getGame().nextCurrentPlayer();
                                server.turnMessage();
                                server.getGame().getPlayer(this.name).setExtraTurns(0, 0);
                                break;
                            }break;
                        } else{
                            out.write(server.playCardcmd(CardType.valueOf(words[1]), server.getGame().getPlayer(name)));
                            out.newLine();
                            out.flush();
                            break;
                        }

                } else{
                out.write("Not your turn");
                out.newLine();
                out.flush();
                break;
            }
                }catch (IllegalArgumentException e){
                    out.write("CARD NOT FOUND");
                    out.newLine();
                    out.flush();
                    break;
                }
            case ProtocolMessages.DRAW_CARD:

                if (Objects.equals(this.name, server.getGame().getCurrentPlayer().getName())) {
                    String answer = server.drawCard();
                    if (answer.equals("EXPLODING_KITTEN")){
                        out.write(ProtocolMessages.DRAWN + ProtocolMessages.DELIMITER + answer);
                        out.newLine();
                        out.flush();
                        out.write(ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + "the deck is of size:" + server.getGame().getDeckLength());
                        out.newLine();
                        out.flush();
                        break;
                    }
                    if (server.getGame().isGameOver()) {
                        server.endGame();
                        out.newLine();
                        out.flush();
                        break;
                    }
                    out.write(ProtocolMessages.DRAWN + ProtocolMessages.DELIMITER + answer);
                    out.newLine();
                    out.flush();
                    server.getGame().getCurrentPlayer().setExtraTurns(-1, server.getGame().getCurrentPlayer().getExtraTurns());
                    if (server.getGame().getCurrentPlayer().getExtraTurns() == -1) {
                        server.getGame().getCurrentPlayer().setExtraTurns(0, 0);
                        server.getGame().nextCurrentPlayer();
                        server.turnMessage();
                        break;
                        }
                    out.write(ProtocolMessages.RESPONSE_MANDATORY_DRAWS + ProtocolMessages.DELIMITER + server.getGame().getCurrentPlayer().getExtraTurns());
                    out.newLine();
                    out.flush();
                    break;
                }
                out.write(ProtocolMessages.EXCEPTION + ProtocolMessages.DELIMITER + "Not your turn");
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.CHOOSE_CARD_IN_HAND:
                server.chooseCardInHand(CardType.valueOf(words[1]), server.getGame().getPlayer(this.name));
                server.getGame().setCurrentPlayer(server.getGame().cardReciever);
                out.write("Gave a card");
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.PLAY_FAVOR:
                out.write(server.playFavor(server.getGame().getPlayer(words[1])));
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.PLAY_COMBO:
                if (words[2].equals("2")){
                    server.getGame().setTargetPlayer(server.getGame().getPlayer(words[3]));
                    //server.playCombo2(CardType.valueOf(words[1]));
                    out.write(server.playCombo2(CardType.valueOf(words[1])));
                    out.newLine();
                    out.flush();
                    break;
                } else if (words[2].equals("3")){
                    server.getGame().setTargetPlayer(server.getGame().getPlayer(words[3]));
                    //server.playCombo3(CardType.valueOf(words[1]));
                    out.write(ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + "NOT POSSIBLE");
                    out.newLine();
                    out.flush();
                    break;
                } else {
                    throw new IllegalArgumentException("illegal command");
                }
            case ProtocolMessages.PLAY_DEFUSE:
                server.playDefuse(Integer.parseInt(words[1]));
                out.write(ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + "You played a defuse");
                out.newLine();
                out.flush();
                server.getGame().getCurrentPlayer().setExtraTurns(-1, server.getGame().getCurrentPlayer().getExtraTurns());
                if (server.getGame().getCurrentPlayer().getExtraTurns() == -1) {
                    server.getGame().getCurrentPlayer().setExtraTurns(0, 0);
                    server.getGame().nextCurrentPlayer();
                    server.turnMessage();
                    break;
                }
                out.write(ProtocolMessages.RESPONSE_MANDATORY_DRAWS + ProtocolMessages.DELIMITER + server.getGame().getCurrentPlayer().getExtraTurns()+1);
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
            case ProtocolMessages.REQUEST_MANDATORY_DRAWS:
                out.write(server.requestMandatoryDraws(server.getGame().getPlayer(this.name)));
                out.newLine();
                out.flush();
                break;
//            case ProtocolMessages.GENERAL_CARD_RESPONSE:
//                out.write((server.giveCard(new Card(CardType.valueOf(words[1])))));
//                out.newLine();
//                out.flush();
//                break;
            case ProtocolMessages.REFUSE_NOPE:
                if(Objects.equals(server.getGame().cardBeforeNope.getType(), new Card(CardType.FAVOR).getType())){
                    server.getGame().playerBeforeNope.getHand().add(new Card(server.getGame().cardBeforeNope.getType()));
                    server.sendMessageToPlayer((server.getGame().playerBeforeNope.getName()), server.playFavor(server.getGame().getTargetPlayer()));
                    out.write(ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + "You refused to nope");
                    out.newLine();
                    out.flush();
                    break;
                }
                else if(Objects.equals(server.getGame().cardBeforeNope.getType(), new Card(CardType.SKIP).getType())) {
                    out.write(ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + "You refused to nope");
                    out.newLine();
                    out.flush();
                    server.getGame().playerBeforeNope.getHand().add(new Card(CardType.SKIP));

//                server.getGame().playerBeforeNope.getHand().add(new Card(server.getGame().cardBeforeNope.getType()));
//                        server.getGame().playerBeforeNope.setExtraTurns(-1, server.getGame().playerBeforeNope.getExtraTurns());
                    server.sendMessageToPlayer(server.getGame().playerBeforeNope.getName(), (server.getGame().playCard(server.getGame().playerBeforeNope.getHand().indexOf(CardType.SKIP), server.getGame().playerBeforeNope)));
                    System.out.println(server.getGame().playerBeforeNope.getExtraTurns());
                    if (server.getGame().playerBeforeNope.getExtraTurns() == -1) {
                        server.getGame().playerBeforeNope.setExtraTurns(0, 0);
                        server.getGame().nextCurrentPlayer();
                        server.turnMessage();
                        break;
                    }
                    break;
                }
                else if(Objects.equals(server.getGame().cardBeforeNope.getType(), new Card(CardType.ATTACK).getType())) {
                    out.write(ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + "You refused to nope");
                    out.newLine();
                    out.flush();
                    server.getGame().playerBeforeNope.getHand().add(new Card(CardType.ATTACK));
                server.sendMessageToPlayer(server.getGame().playerBeforeNope.getName(), (server.getGame().playCard(server.getGame().playerBeforeNope.getHand().indexOf(CardType.ATTACK), server.getGame().playerBeforeNope)));
                    System.out.println(server.getGame().playerBeforeNope.getExtraTurns());
                    if (server.getGame().playerBeforeNope.getExtraTurns() == -1) {
                        server.getGame().playerBeforeNope.setExtraTurns(0, 0);
                        server.getGame().nextCurrentPlayer();
                        server.turnMessage();
                        break;
                    }
                    break;
                }
                else if(server.action == "COMBO2") {

                    server.sendMessageToPlayer(server.getGame().playerBeforeNope.getName(),
                            ProtocolMessages.CARD_RECEIVED + ProtocolMessages.DELIMITER + "COMBO2" + ProtocolMessages.DELIMITER + server.getGame().getTargetPlayer().getName() +
                                    ProtocolMessages.DELIMITER + server.getGame().giveCard(server.getGame().playerBeforeNope, server.getGame().getTargetPlayer()));
                    out.write(ProtocolMessages.CARD_EXCHANGED + ProtocolMessages.DELIMITER + "COMBO2" + ProtocolMessages.DELIMITER + server.getGame().getTargetPlayer().getName() +
                            ProtocolMessages.DELIMITER + server.getGame().playerBeforeNope.getName() + ProtocolMessages.DELIMITER + server.getGame().getTargetPlayer());
                    out.newLine();
                    out.flush();
                    break;
                }
                    server.sendMessageToPlayer((server.getGame().playerBeforeNope.getName()), server.getGame().playCard(server.getGame().getPlayer(server.getGame().playerBeforeNope.getName()).getHand().
                            indexOf(server.getGame().cardBeforeNope.getType()), server.getGame().playerBeforeNope));
                out.write(ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + "You refused to nope");
                out.newLine();
                out.flush();
                break;
            case ProtocolMessages.CHAT:
                if (server.getChatFunction()) {
                    server.sendChat(ProtocolMessages.CHAT + ProtocolMessages.DELIMITER + this.name + ProtocolMessages.DELIMITER + words[1], this.name);
                } else{
                    out.write("Chat function not available");
                    out.newLine();
                    out.flush();
                }
                break;
            case "":
                break;
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
