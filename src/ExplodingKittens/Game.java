package ExplodingKittens;

import java.util.ArrayList;
import java.util.List;

public class Game {
    //players: Arraylist<player>
        private Deck deck;
        private List<Player> players;
        private DiscardPile discardPile;
        public List<Player> getPlayers() {
            return players;
        }
        private int currentPlayerIndex;

        public Game(int numPlayers) {
            initializePlayers(numPlayers);
            initializeDeckandhands();
            initializeDiscardPile();

            currentPlayerIndex = 0;
        }

        private void initializeDeckandhands() {
            deck = new Deck();
            for (int i = 0; i < 5; i++) {
                for (Player player : players) {
                    player.drawCard(deck);
                }
            }
            // Add Exploding Kittens to the deck after initial cards have been dealt
            for (int i = 0; i < 4; i++) {
                deck.addExplodingKitten();
            }
            deck.shuffle();
        }
        private void initializeDiscardPile() {
            discardPile = new DiscardPile();
        }

        private void initializePlayers(int numPlayers) {
            players = new ArrayList<>();
            for (int i = 1; i <= numPlayers; i++) {
                players.add(new Player("Player " + i));
            }
        }

        public void startGame() {
            // Deal initial cards to players

            // Play a round (for demonstration purposes)
            for (Player player : players) {
                player.playCard(player.getHand().get(0), discardPile); // Play the first card in hand
            }
            // Implement the game loop and overall game logic here
            // For example, dealing initial cards to players, handling turns, etc.
        }
        public Player getCurrentPlayer() {
            return players.get(currentPlayerIndex);
        }
    public void endTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

        private boolean isGameOver() {
            // Implement logic to check if the game is over
            // For example, if the deck is empty and there are no more cards to draw
            return players.size() == 1;
        }

    public ArrayList<Player> eliminatedPlayers;
    private Player currentPlayer;

    public Player Player;
    public  void EleminatePlayer(){


    }


    //nextPlayer() moves to the next player
    public Player nextPlayer(){
        for(Player player: players){
            if(player == currentPlayer){
                currentPlayer = players.get(players.indexOf(player)+1);
            }
        }
        return Player;
    }
//    private void play(){
//        startGame();
//        while(players.size()>1){
//            nextPlayer();
//            //playCard();
//        }
//        endGame();
//    }
    //endGame() ends the game when last kitten exploded
    public void endGame(){
        //end the game
    }

    public void getDiscardPile() {
        for (Card card : discardPile.getDiscardPile()) {
            System.out.println(card.getType());
        }
    }
    public void getDeck() {
        for (Card card : deck.getDeck()) {
            System.out.println(card.getType());
        }
    }
}
