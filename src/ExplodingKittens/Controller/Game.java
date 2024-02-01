package ExplodingKittens.Controller;


import ExplodingKittens.Model.*;
import ExplodingKittens.View.ClientTUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Game {
    public Player1 playerBeforeNope;
    //players: Arraylist<player>
    public String gameName;
    private Player1 targetPlayer;
    private Deck deck;
    private Player1 currentPlayer;
    private ArrayList<Player1> eliminatedplayers = new ArrayList<>();
    private List<Player1> players;
    private Deck discardPile;
    public Card playedCard;

    public List<Player1> getPlayers() {
        return players;
    }

    private int currentPlayerIndex;
    public int getDeckLength(){
        return deck.length();
    }

    public Game(String name) {
        this.gameName = name;
        this.players= new ArrayList<>();
        initializeDeck();

//        initializePlayers(Players); //CHANGE THE WAY PLAYER ARE MADE
//        initilializehands();
//        initializeDiscardPile();
        //gameStart();
    }
    public void gameStart(){
        initilializehands();
        currentPlayerIndex = 0;
        currentPlayer = getCurrentPlayer();
    }

    private void initializeDeck() {
        deck = new Deck();
        deck.initializeDeck();
        discardPile = new Deck();
        // Add Exploding Kittens to the deck after initial cards have been dealt

    }
    private void initilializehands() {
        for (int i = 0; i < /*player.size()*/ 4; i++) {
            for (Player1 player : players) {
                drawCard(player);
            }
        }
        int defusecount = 0;
        for (Player1 player : players){
            player.getHand().addCard(new Card(CardType.DEFUSE));
            defusecount++;
        }
        for (int i = 0; i < deck.getInitialCardCount(CardType.EXPLODING_KITTEN); i++) {
            deck.addExplodingKitten();
        }
        for (int i = 0; i < deck.getInitialCardCount(CardType.DEFUSE)-defusecount; i++) {
            deck.addExplodingKitten();
        }
        deck.shuffle();
    }


    public void nextCurrentPlayer(){
        this.currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    public Player1 getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player1 getNextPlayer() {
        return players.get((currentPlayerIndex + 1) % players.size());
    }

    public void endTurn() {
        drawCard(currentPlayer);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public void endTurnNoDraw() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

    }

    public boolean isGameOver() {
        return players.size() == 1;
    }

    // Other methods as needed

    public String eliminatePlayer(Player1 player) {

        players.remove(player);
        eliminatedplayers.add(player);

        //clientTui.eliminatePlayerMessage(player);
        //System.out.println(player.getName() + " has been eliminated!");
        currentPlayerIndex = (currentPlayerIndex - 1) % players.size();
        return (ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER +  player .getName() + " has been eliminated!");
    }
    public Player1 cardReciever= null;
    public String drawCard(Player1 player) {
        currentPlayer = player;
        Card drawnCard = deck.draw();
        if (drawnCard != null) {
            //clientTui.drawCardMessage(player.getName(), drawnCard);
            //System.out.println(name + " drew a " + drawnCard.getType() + " card.");
            if (drawnCard.getType() == CardType.EXPLODING_KITTEN) {
                System.out.println(drawnCard.getType().name());
                // Check if the player has a Defuse card
                if (player.getHand().hasDefuseCard()) {
                    return (ProtocolMessages.DRAWN + ProtocolMessages.DELIMITER + drawnCard.getType());
                    //return (ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + player.getName() + " drew an Exploding Kitten! They used a Defuse card to defuse it.");
                } else {
                    // Player does not have a Defuse card, eliminate them
                    return eliminatePlayer(currentPlayer);
                    //return (ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER + player.getName() + " drew an exploding kitten! They have been eliminated!");
                }
            }
            player.getHand().addCard(drawnCard);
            return (ProtocolMessages.DRAWN + ProtocolMessages.DELIMITER + drawnCard.getType());
        }
        return null;
    }
    public void playDefuse(int index) {
        deck.putCard(index, new Card(CardType.EXPLODING_KITTEN));

    }
    public Card cardBeforeNope = null;
    public  String playCard(int cardIndex, Player1 currentPlayer) {
        if (cardIndex >= 0 && cardIndex < currentPlayer.getHand().size() && currentPlayer.getHand().get(cardIndex).playable()) {
            playedCard = currentPlayer.getHand().get(cardIndex);
            currentPlayer.getHand().remove(playedCard);
            //clientTui.playCardMessage(currentPlayer.getName(), playedCard);
            //System.out.println(name + " played a " + playedCard.getType() + " card.");
            discardPile.addCard(playedCard);
            switch (playedCard.getType()) {
                case NOPE:
                    playedCard.Nope();
                    break;
                case SHUFFLE:
                    return (playedCard.Shuffle(deck) + currentPlayer.getName() + ProtocolMessages.DELIMITER + " played a " + playedCard.getType() + " card");
                case SKIP:
                    currentPlayer.setExtraTurns(-1, currentPlayer.getExtraTurns());
                    return (ProtocolMessages.GENERAL_CARD_RESPONSE + ProtocolMessages.DELIMITER + "SKIP" );
                case SEE_THE_FUTURE:
                    //this.askPlayersNope();
                    return (playedCard.seeTheFuture(this.deck));
                case CAT_CARD1:
                    return currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD1, currentPlayer);

                case CAT_CARD2:
                    return currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD2, currentPlayer);

                case CAT_CARD3:
                    return currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD3, currentPlayer);

                case CAT_CARD4:
                     return currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD4, currentPlayer);

                case CAT_CARD5:
                    return currentPlayer.getHand().catCardsInHand(CardType.CAT_CARD5, currentPlayer);

                //case FAVOR: handled in gameserver
//                    this.askPlayersNope();
//                    favor();
//                    break;
                case ATTACK:
                    playedCard.Attack(currentPlayer, this.getNextPlayer());
                    return (ProtocolMessages.ATTACKED + ProtocolMessages.DELIMITER + currentPlayer.getName() + ProtocolMessages.DELIMITER + this.getNextPlayer().getName());
            }
            // Implement the specific action associated with the played card
            // For example, triggering a special ability or resolving effects
        } else {
            //clientTui.playCardMessage(3);
            //System.out.println("Invalid card index. Choose a card within the valid range.");

        }
        return null;
    }

    public void twoCards(CardType typecard) {
        int deletedcardcount=1;
        while (deletedcardcount < 2) {
            for (int i = 0; i < currentPlayer.getHand().size(); i++) {
                if (currentPlayer.getHand().get(i).getType() == typecard) {
                    Card playedCard = currentPlayer.getHand().get(i);
                    currentPlayer.getHand().remove(playedCard);
                    deletedcardcount++;
                }
            }
        }
    }

    public void setTargetPlayer(Player1 player) {
        this.targetPlayer = player;
    }
    public Player1 getTargetPlayer(){
        return targetPlayer;
    }
    public void giveCard(Card card, Player1 player) {
        player.getHand().add(card);
        //System.out.println(name + " gave " + player.getName() + " a " + card.getType() + " card.");
    }
    public String giveCard(Player1 currentPlayer, Player1 targetPlayer){
        Card card = targetPlayer.getHand().getHandlist().get(0);
        targetPlayer.getHand().remove(0);
        currentPlayer.getHand().add(card);
        return (card.getType().name());
    }
    public void addPlayer(String name) {
        players.add(new HumanPlayer(name, this));
    }

    public Player1 getPlayer(String word) {
        for (Player1 player : players) {
            if (player.getName().equals(word)) {
                return player;
            }
        }
        return null;
    }

    public void setCurrentPlayer(Player1 player) {
        this.currentPlayer = player;
    }

    public Player1 getWinner() {
        return players.get(0);
    }
}