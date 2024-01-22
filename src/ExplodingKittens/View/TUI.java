package ExplodingKittens.View;

import ExplodingKittens.Game;
import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;
import ExplodingKittens.Model.Deck;
import ExplodingKittens.Model.DiscardPile;
import ExplodingKittens.Player;


public class TUI {
    public static void seeTheFutureMessage(Player player, int message) {
        switch (message) {
            case 1:
                System.out.println(player.getName() + " played a See the Future card. The top three cards are: ");
                break;
            case 2:
                System.out.println("Error: Deck is empty.");
                break;
        }
    }
        public static void seeTheFutureMessage(Card card){
            System.out.println(card.getType());
            }

    public static void ShuffleMessage(Player player, int message) {
        switch (message) {
            case 1:
                System.out.println(player.getName() + " shuffled the deck.");
                break;
            case 2:
                System.out.println("Error: Deck reference is null.");
                break;
        }
    }
    public static void AttackMessage(Player currentPlayer, Player targetPlayer) {
        System.out.println(currentPlayer.getName() + " played an Attack card. " + targetPlayer.getName() + " will have " + targetPlayer.getExtraTurns() + " extra turns.");
    }

    public static void discardCardMessage(Card card) {
        System.out.println("Card discarded: " + card.getType());
    }


    // write overread methode of turn message but then with targetplayer
    public static void turnMessage(Player player, int message) {
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
    public static void turnMessage(int message){
        switch (message) {
            case 1:
                System.out.println("Do you want to end your turn? (yes/no)");
                break;
        }
}

public static void handleTurnEndMessage(Player player) {
    System.out.println(player.getName() + " skips a turn.");
}
    public static void getPlayerInputMessage(Player player, int message) {
            switch (message){
                case 1:
                    System.out.println(player.getName() + ", choose a card to play (enter the card index): ");
                    break;
                case 2:
                    int i = message;
                    //
                    System.out.println(i + ": " + player.getHand().get(i).getType());
                    break;

            }
    }

        public static void getPlayerInputMessage(int message) {
                switch (message){
                    case 1:
                        System.out.println("Invalid card index. Please try again.");
                        break;
                }

        }

    public static void getDiscardPileMessage(Card card) {
            System.out.println(card.getType());
        }
    public static void getDeckMessage(Card card) {
            System.out.println(card.getType());
        }
    public static void eliminatePlayerMessage(Player player) {
        System.out.println(player.getName() + " has been eliminated!");
    }

    public static void mainMessage(Game game){
        System.out.println("Game Over! Winner: " + game.getPlayers().get(0).getName());
    }

    public static void drawCardMessage(String name, Card drawnCard) {
        System.out.println(name + " drew a " + drawnCard.getType() + " card.");


    }
    public static void drawCardMessage(String name, int message){
        switch (message){
            case 1:
                System.out.println(name + ", you drew an Exploding Kitten! Do you want to play a Defuse card? (yes/no)");
                break;
                case 2:
                    System.out.println(name + " played a Defuse card.");
                    break;
        }
    }

    public static void playCardMessage(String name, Card playedCard) {
        System.out.println(name + " played a " + playedCard.getType() + " card.");
    }
    public static void playCardMessage(int message){
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


    public static void catCardsInHandMessage(String name, CardType typeCard, int catCardCount) {
        System.out.println(name + " has " + catCardCount + " " + typeCard + " in hand.");
    }
    public static void catCardsInHandMessage(CardType typeCard, int message){
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
    public static void catCardsInHandMessage(int message){
        System.out.println("oke continue");
        }



        public static void choosePlayerChoiceMessage(int message) {
        switch (message) {
            case 1:
                System.out.println("Choose a player to take a card from (enter the player index): ");
                break;
            case 2:
                System.out.println("Invalid player index. Choose a player within the valid range.");
                break;
        }
    }

       public static void choosePlayerChoiceMessage(Game game, int message){
                System.out.println(message + " " + game.getPlayers().get(message).getName());
            }

            public static void favorChoiceMessage(String name) {
                System.out.println(name + ", you played a Favor card. Choose a card type to request from the other player: ");
            }
            public static void favorChoiceMessage(int message){
                switch (message){
                    case 1:
                        System.out.println("1: EXPLODING_KITTEN, 2: DEFUSE, 3: NOPE, 4: SKIP, 5: ATTACK, 6:  SEE_THE_FUTURE, 7:FAVOR, 8: SHUFFLE, 9: CAT_CARD1, 10: CAT_CARD2, 11: CAT_CARD3, 12: CAT_CARD4, 13: CAT_CARD5 ");
                        break;
                }
            }
            public static void favorChoiceMessage(String name, Player targetPlayer, Card takenCard, int message){
                switch (message){
                    case 1:
                        System.out.println(name + " took a " + takenCard.getType() + " card from " + targetPlayer.getName() + ".");
                        break;
                    case 2:
                        System.out.println(targetPlayer.getName() + " doesn't have the requested card type. You get nothing.");
                        break;
                }

            }
    public static void choosePlayerMessage(int message){
        switch (message){
            case 1:
                System.out.println("Choose a player to take a card from (enter the player index): ");
                break;
            case 2:
                System.out.println("Invalid player index. Choose a player within the valid range.");
                break;
        }
    }
    public static void choosePlayerMessage(Game game, int message){
                System.out.println(message +" "+ game.getPlayers().get(message).getName());
            }

    public static void favorMessage(String name, Player targetPlayer) {
            System.out.println(name + " played a Favor card. " + targetPlayer.getName() + " must give you a card.");
    }
    public static void giveCardMessage(String name, Player player) {
        System.out.println(name + ", choose a card to give to " + player.getName() + " (enter the card index): ");
    }
    public static void giveCardMessage(int message){
        switch (message){
            case 1:
                System.out.println("Invalid card index. Please try again.");
                break;
        }
    }
    public static void giveCardMessage(String name, Player player, Card card) {
        System.out.println(name + " gave " + player.getName() + " a " + card.getType() + " card.");
    }
    public static void  giveCardMessage(int message, Player player){
        System.out.println(message + ": " + player.getHand().get(message).getType());
    }
    public static void askNopeMessage(String name, int message) {
        switch (message){
            case 1:
                System.out.println(name + " played a Nope card.");
                break;
            case 2:
                System.out.println(name + " did not play a Nope card.");
                break;
        }
    }
    public static void askNopeMessage(int message){
        switch (message){
            case 1:
                System.out.println("Do you want to play a Nope card? (yes/no)");
                break;
        }
    }
}
