package ExplodingKittens.Controller;


import ExplodingKittens.Model.*;
import ExplodingKittens.View.ClientTUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
/**
 * The Game class represents the core functionality of the Exploding Kittens game.
 * It manages players, cards, the game deck, and gameplay mechanics.
 */
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
    /**
     * Constructs a new Game instance with the given name.
     *
     * @param name The name of the game.
     */
    public Game(String name) {
        this.gameName = name;
        this.players= new ArrayList<>();
        initializeDeck();
    }
    /**
     * Starts the game by initializing player hands and setting up the game state.
     */
    public void gameStart(){
        initilializehands();
        currentPlayerIndex = 0;
        currentPlayer = getCurrentPlayer();
    }
    /**
     * Initializes the game deck by creating and shuffling a new deck of cards.
     */
    private void initializeDeck() {
        deck = new Deck();
        deck.initializeDeck();
        discardPile = new Deck();
        // Add Exploding Kittens to the deck after initial cards have been dealt

    }
    /**
     * Initializes the players' hands with cards from the deck. the hand must contain an DEFUSE card and can't contain an exploding kitten card.
     */
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
    /**
     * Advances the current player to the next player in the game sequence.
     */

    public void nextCurrentPlayer(){
        this.currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    /**
     * Retrieves the current player in the game.
     *
     * @return The current player.
     */
    public Player1 getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    /**
     * Retrieves Next player in the game.
     *
     * @return The next player.
     */

    public Player1 getNextPlayer() {
        return players.get((currentPlayerIndex + 1) % players.size());
    }
    /**
     * Checks if the game is over, i.e., if only one player remains.
     *
     * @return True if the game is over, otherwise false.
     */

    public boolean isGameOver() {
        return players.size() == 1;
    }
    /**
     * Eliminates the specified player from the game.
     *
     * @param player The player to be eliminated.
     * @return A message indicating the player's elimination.
     */

    public String eliminatePlayer(Player1 player) {

        players.remove(player);
        eliminatedplayers.add(player);
        currentPlayerIndex = (currentPlayerIndex - 1) % players.size();
        return (ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER +  player .getName() + " has been eliminated!");
    }
    public Player1 cardReciever= null;
    /**
     * Draws a card for the specified player from the game deck.
     *
     * @param player The player to draw the card.
     * @return A message indicating the outcome of the card draw.
     */
    public String drawCard(Player1 player) {
        currentPlayer = player;
        Card drawnCard = deck.draw();
        if (drawnCard != null) {

            if (drawnCard.getType() == CardType.EXPLODING_KITTEN) {
                System.out.println(drawnCard.getType().name());
                // Check if the player has a Defuse card
                if (player.getHand().hasDefuseCard()) {
                    return (ProtocolMessages.DRAWN + ProtocolMessages.DELIMITER + drawnCard.getType());
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
    /**
     * Plays a Defuse card from the player's hand to defuse an exploding kitten card.
     *
     * @param index The index of the Defuse card in the player's hand.
     */
    public void playDefuse(int index) {
        deck.putCard(index, new Card(CardType.EXPLODING_KITTEN));

    }
    public Card cardBeforeNope = null;
    /**
     * Plays a card from the player's hand based on the given index.
     * Handles different types of cards and executes corresponding actions.
     *
     * @param cardIndex     The index of the card to be played.
     * @param currentPlayer The player who is playing the card.
     * @return A message indicating the outcome of the card play.
     */
    public String playCard(int cardIndex, Player1 currentPlayer) {
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
    /**
     * Sets the target player for the game action.
     *
     * @param player The player to be targeted.
     */

    public void setTargetPlayer(Player1 player) {
        this.targetPlayer = player;
    }
    /**
     * Retrieves the target player for the game action.
     *
     * @return The targeted player.
     */
    public Player1 getTargetPlayer(){
        return targetPlayer;
    }
    /**
     * Gives a card from one player to another.
     *
     * @param card        The card to be given.
     * @param player      The player giving the card.
     */
    public void giveCard(Card card, Player1 player) {
        player.getHand().add(card);
        //System.out.println(name + " gave " + player.getName() + " a " + card.getType() + " card.");
    }
    /**
     * Gives a card from one player to another and returns the type of card given.
     *
     * @param currentPlayer The player giving the card.
     * @param targetPlayer  The player receiving the card.
     * @return The type of card given.
     */
    public String giveCard(Player1 currentPlayer, Player1 targetPlayer){
        Card card = targetPlayer.getHand().getHandlist().get(0);
        targetPlayer.getHand().remove(0);
        currentPlayer.getHand().add(card);
        return (card.getType().name());
    }
    /**
     * Adds a player to the game.
     *
     * @param name The name of the player to be added.
     */
    public void addPlayer(String name) {
        players.add(new HumanPlayer(name, this));
    }
    /**
     * Retrieves the player object by name.
     *
     * @param playerName The name of the player.
     * @return The player object.
     */
    public Player1 getPlayer(String word) {
        for (Player1 player : players) {
            if (player.getName().equals(word)) {
                return player;
            }
        }
        return null;
    }
    /**
     * Sets the current player.
     *
     * @param player The current player.
     */
    public void setCurrentPlayer(Player1 player) {
        this.currentPlayer = player;
    }
    /**
     * Retrieves the winner of the game.
     *
     * @return The player who won the game.
     */
    public Player1 getWinner() {
        return players.get(0);
    }
}