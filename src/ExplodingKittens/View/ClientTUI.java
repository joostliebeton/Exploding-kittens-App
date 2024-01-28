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


public class ClientTUI {
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
        printHelpMenu();
        String input = "";
        input = TextIO.getln();
        boolean out = false;
        while (!out){
            try {
                if(!input.equals("") && input != null) {
                    handleUserInput(input);
                }
            } catch (ExitProgram e) {
                playerClient.sendExit();
                playerClient.closeConnection();
            }
            input = TextIO.getlnString();
        }
    }
    public void printHelpMenu() {
        System.out.println(("Commands :\n" +
                "Hand ..............request hand\n" +
                "play ..............you want to play a card\n" +
                "PlayerAmount.......requests player count\n" +
                "Start i1 i2........starts a game with i1 players of which are i2 ai\n" +
                "handsize...........request a hand size of player\n" +
                "h ................ help ( this menu )\n" +
                "p ................ print state of the hotel\n" +
                "x ................ exit\n"));
    }
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
        String[] inputs = input.split(" ");
        String command = inputs[0];
        switch (command.toLowerCase()) {
            case "playeramount":
                playerClient.requestAmountofPlayers();
                break;
            case "play":
                String cardType = getString("Enter card type");
                if(cardType.equals("FAVOR")){
                    playerClient.doFavor(getString("Enter player name"));
                    break;
                }
                playerClient.doPlay(cardType);
                break;
            case "handsize" :
                playerClient.doRequestCardsInHand(getString("Enter player name"));
                break;
            case "hand":
                playerClient.doRequestCardsInHandtype();
                break;
            case "start":
                if (inputs.length <= 2){
                    playerClient.doStartGameRequest(inputs[1], "0");
                    break;
                } else {
                    playerClient.doStartGameRequest(inputs[1], inputs[2]);
                    break;
                }
            case "h":
                printHelpMenu();
                break;

//            case ProtocolMessages.PRINT:
//                hotelClient.doPrint();
//                break;
//            case ProtocolMessages.HELP:
//                printHelpMenu();
//                break;
            default:
                System.out.println("Invalid Command");
                printHelpMenu();
        }
    }


    public void showMessage(String message) {
        System.out.println(message);
    }

    public InetAddress getIp() throws UnknownHostException {
        return InetAddress.getByName(getString("Enter IP address: "));
    }

    public void seeTheFutureMessage(Player1 player, int message) {
        switch (message) {
            case 1:
                System.out.println(player.getName() + " played a See the Future card. The top three cards are: ");
                break;
            case 2:
                System.out.println("Error: Deck is empty.");
                break;
        }
    }
    public void seeTheFutureMessage(Card card){
            System.out.println(card.getType());
            }

    public void ShuffleMessage(Player1 player, int message) {
        switch (message) {
            case 1:
                System.out.println(player.getName() + " shuffled the deck."); // hey homo
                break;
            case 2:
                System.out.println("Error: Deck reference is null.");
                break;
        }
    }
    public void AttackMessage(Player1 currentPlayer, Player1 targetPlayer) {
        System.out.println(currentPlayer.getName() + " played an Attack card. " + targetPlayer.getName() + " will have " + targetPlayer.getExtraTurns() + " extra turns.");
    }

    public void discardCardMessage(Card card) {
        System.out.println("Card discarded: " + card.getType());
    }


    // write overread methode of turn message but then with targetplayer
    public void turnMessage(Player1 player, int message) {
switch (message) {
            case 1:
                // this one is for the currentplayer
                System.out.println(player.getName() + " has " + player.getExtraTurns() + " extra turns!");
                break;
            case 2:
                // this one is for the targetplayer
                System.out.println(player.getName() + " has " + player.getExtraTurns() + " extra turns!");
                break;
        }
    }
    public void turnMessage(int message){
        switch (message) {
            case 1:
                System.out.println("Do you want to end your turn? (yes/no)");
                break;
        }
}

public void handleTurnEndMessage(Player1 player) {
    System.out.println(player.getName() + " skips a turn.");
}
    public void getPlayerInputMessage(Player1 player) {
        System.out.println(player.getName() + ", choose a card to play (enter the card index): ");

    }
    public void outputPlayerHand(Player1 player, int i){
        System.out.println(i + ": " + player.getHand().get(i).getType());
    }
        public void getPlayerInputMessage(int message) {
                switch (message){
                    case 1:
                        System.out.println("Invalid card index. Please try again.");
                        break;
                }

        }

    public void getDiscardPileMessage(Card card) {
            System.out.println(card.getType());
        }
    public void getDeckMessage(Card card) {
            System.out.println(card.getType());
        }
    public void eliminatePlayerMessage(Player1 player) {
        System.out.println(player.getName() + " has been eliminated!");
}

    public void mainMessage(Game game){
        System.out.println("Game Over! Winner: " + game.getPlayers().get(0).getName());
    }

    public void drawCardMessage(String name, Card drawnCard) {
        System.out.println(name + " drew a " + drawnCard.getType() + " card.");


    }
    public void drawCardMessage(String name, int message){
        switch (message){
            case 1:
                System.out.println(name + ", you drew an Exploding Kitten! Do you want to play a Defuse card? (yes/no)");
                break;
                case 2:
                    System.out.println(name + " played a Defuse card.");
                    break;
        }
    }

    public void playCardMessage(String name, Card playedCard) {
        System.out.println(name + " played a " + playedCard.getType() + " card.");
    }
    public void playCardMessage(int message){
        switch (message){
            case 1:
                System.out.println("Nope card negated the Shuffle!");
                break;
            case 2:
                System.out.println("Nope card negated the Skip!");
                break;
            case 3:
                System.out.println("Invalid card index. Choose a card within the valid range.");
                break;
        }
    }


    public void catCardsInHandMessage(String name, CardType typeCard, int catCardCount) {
        System.out.println(name + " has " + catCardCount + " " + typeCard + " in hand.");
    }
    public void catCardsInHandMessage(CardType typeCard, int message){
        switch (message){
            case 1:
                System.out.println("You need at least 1 more " + typeCard + " to play this card as an action card");
                break;
            case 2:
                System.out.println("Do you want to play the 2 " + typeCard + " card? (yes/no)");
                break;
            case 3:
                System.out.println("do you want to play the 3 " + typeCard + " card? (yes/no)");
                break;
            }
    }
    public void catCardsInHandMessage(int message){
        System.out.println("oke continue");
        }



        public void choosePlayerChoiceMessage(int message) {
        switch (message) {
            case 1:
                System.out.println("Choose a player to take a card from (enter the player index): ");
                break;
            case 2:
                System.out.println("Invalid player index. Choose a player within the valid range.");
                break;
        }
    }

       public void choosePlayerChoiceMessage(Game game, int message){
                System.out.println(message + " " + game.getPlayers().get(message).getName());
            }

            public void favorChoiceMessage(String name) {
                System.out.println(name + ", you played a Favor card. Choose a card type to request from the other player: ");
            }
            public void favorChoiceMessage(int message){
                switch (message){
                    case 1:
                        System.out.println("1: EXPLODING_KITTEN, 2: DEFUSE, 3: NOPE, 4: SKIP, 5: ATTACK, 6:  SEE_THE_FUTURE, 7:FAVOR, 8: SHUFFLE, 9: CAT_CARD1, 10: CAT_CARD2, 11: CAT_CARD3, 12: CAT_CARD4, 13: CAT_CARD5 ");
                        break;
                }
            }
            public void favorChoiceMessage(String name, Player1 targetPlayer, Card takenCard, int message){
                switch (message){
                    case 1:
                        System.out.println(name + " took a " + takenCard.getType() + " card from " + targetPlayer.getName() + ".");
                        break;
                    case 2:
                        System.out.println(targetPlayer.getName() + " doesn't have the requested card type. You get nothing.");
                        break;
                }

            }
    public void choosePlayerMessage(int message){
        switch (message){
            case 1:
                System.out.println("Choose a player to take a card from (enter the player index): ");
                break;
            case 2:
                System.out.println("Invalid player index. Choose a player within the valid range.");
                break;
        }
    }
    public void choosePlayerMessage(Game game, int message){
                System.out.println(message +" "+ game.getPlayers().get(message).getName());
            }

    public void favorMessage(String name, Player1 targetPlayer) {
            System.out.println(name + " played a Favor card. " + targetPlayer.getName() + " must give you a card.");
    }
    public void giveCardMessage(String name, Player1 player) {
        System.out.println(name + ", choose a card to give to " + player.getName() + " (enter the card index): ");
    }
    public void giveCardMessage(int message){
        switch (message){
            case 1:
                System.out.println("Invalid card index. Please try again.");
                break;
        }
    }
    public void giveCardMessage(String name, Player1 player, Card card) {
        System.out.println(name + " gave " + player.getName() + " a " + card.getType() + " card.");
    }

    public void askNopeMessage(String name, int message) {
        switch (message){
            case 1:
                System.out.println(name + " played a Nope card.");
                break;
            case 2:
                System.out.println(name + " did not play a Nope card.");
                break;
        }
    }
    public void askNopeMessage(int message){
        switch (message){
            case 1:
                System.out.println("Do you want to play a Nope card? (yes/no)");
                break;
        }
    }
    public void drawPileMessage(int message){
        System.out.println(message);
    }
    public void generalCardResponse(CardType cardType) {
        //////////////////////////////////to implement/////
        ////System.out.println(played a card) + cardtype
    }

    public void userHandSizeMessage(String name, int size) {
        System.out.println(name + " has " + size + " cards in hand.");
    }

    public void requestAlivePlayersMessage(Object alivePlayers) {
        // moet nog even naar gekeken worden
        System.out.println("Alive players: " + alivePlayers);
    }

    public void requestPlayersLobbyMessage(List<Player1> players) {
        System.out.println("Players in lobby: ");
        for (Player1 player : players) {
            System.out.println(player.getName());
        }
    }

    public void requestCardsInHandMessage(String name, List<Card> handList) {
        System.out.println(name + "'s hand: ");
        int i =0;
        for (Card card : handList) {
            System.out.println(i+  ": "+ card.getType());
            i++;
        }
    }
}
