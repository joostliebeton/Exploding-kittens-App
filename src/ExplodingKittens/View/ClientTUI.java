package ExplodingKittens.View;

import ExplodingKittens.Controller.Game;
import ExplodingKittens.Controller.PlayerClient;
import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;

import ExplodingKittens.Model.Player1;
import ExplodingKittens.exceptions.ExitProgram;
import ExplodingKittens.exceptions.ServerUnavailableException;
import ExplodingKittens.utils.TextIO;
import ExplodingKittens.Controller.ProtocolMessages;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Objects;


public class ClientTUI {
    private Thread userInputThread;
    private PlayerClient playerClient;
    private PrintWriter console;


    public String getString(String question) {
        console.print(question);
        console.flush();
        return TextIO.getlnString();
    }
    public int getInt(String question) {
        console.print(question);
        console.flush();
        return TextIO.getlnInt();


    }
    public boolean getBoolean(String question) {
        console.print(question);
        console.flush();
        return TextIO.getlnBoolean();

    }
    public ClientTUI(PlayerClient playerClient) {
        this.playerClient = playerClient;
        this.console = new PrintWriter(System.out, true);
    }
    public void start() throws ServerUnavailableException {
        userInputThread = new Thread(this::handleUserInput);
        userInputThread.start();
        printHelpMenu();
    }
    public void printHelpMenu() {
        System.out.println(("Commands :\n" +
                "Hand ..............request hand\n" +
                "play ..............you want to play a card\n" +
                "Players............requests players in game\n" +
                "Start i1 i2........starts a game with i1 players of which are i2 ai\n" +
                "handsize...........request a hand size of player\n" +
                "h .................help ( this menu )\n" +
                "give ..............give player a card\n" +
                "draw ..............draw a card\n" +
                "chat...............chat with other players\n"));
    }
    public void handleUserInput() {
        try {
            String input = "";
            input = TextIO.getln();
            //!input.equals(null) && !input.isEmpty()
            while (true) {
                String[] inputs = input.split(" ");
                String command = inputs[0];
                switch (command.toLowerCase()) {
                    case "players":
                        playerClient.requestPlayers();
                        break;
                    case "play":
                        try {
                            String cardType = inputs[1];
                            if (cardType.equals("FAVOR")) {
                                try {
                                    playerClient.doFavor(inputs[2]);
                                }catch (ArrayIndexOutOfBoundsException e){
                                    System.out.println("Please enter a player name together with the action");
                                }
                                break;
                            } else if (cardType.equals("NOPE")) {
                                playerClient.doNope();
                                break;
                            } else if(cardType.equals("DEFUSE")){
                                playerClient.PlayDefuse(inputs[2]);
                                break;
                            }
                            playerClient.doPlay(cardType);
                            break;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Please enter a card type together with the action");
                            break;
                        }

                    case "handsize":
                        if (inputs.length == 1) {
                            System.out.println("Please enter a player name");
                            break;
                        }
                        playerClient.doRequestCardsInHand(inputs[1]);
                        break;
                    case "hand":
                        playerClient.doRequestCardsInHandtype();
                        break;
                    case "start":
                        if(inputs.length == 1){
                            System.out.println("Please enter a player amount");
                            break;
                        }
                        else if (inputs.length <= 2) {
                            playerClient.doStartGameRequest(inputs[1], "0");
                            break;
                        } else {
                            playerClient.doStartGameRequest(inputs[1], inputs[2]);
                            break;
                        }
                    case "h":
                        printHelpMenu();
                        break;
                    case "give":
                        playerClient.doGiveCard(inputs[1]);
                        break;
                    case "favor":
                        break;
                    case "play_combo":
                        playerClient.PlayCombo(inputs[1], inputs[2], inputs[3]);
                    case "draw":
                        playerClient.doDrawCard();
                        break;
                    case "drawamount":
                        playerClient.requestDrawAmount();
                        break;
                    case "refuse":
                        if (Objects.equals(inputs[1], "NOPE")){
                            playerClient.doRefuseNope();
                        } else{
                            System.out.println("Please enter a valid action");
                        }
                        break;
                    case "chat":
                            playerClient.sendMessage(ProtocolMessages.CHAT + ProtocolMessages.DELIMITER + input.substring(5));
                            break;
                    case "":
                        break;
                    default:
                        System.out.println("Invalid Command");
                        break;
                }
                input = TextIO.getln();
            }
            //throw new ServerUnavailableException("Server is unavailable");
//
        }catch (ServerUnavailableException e){
            System.out.println("Server is unavailable");
        }
    }


    public void showMessage(String message) {
        System.out.println(message);
    }

}
