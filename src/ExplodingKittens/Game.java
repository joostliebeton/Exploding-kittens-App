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

        public Game(int numPlayers) {
            initializeDeck();
            initializeDiscardPile();
            initializePlayers(numPlayers);
        }

        private void initializeDeck() {
            deck = new Deck();
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
            for (Player player : players) {
                for (int i = 0; i < 5; i++) { // Assuming each player starts with 5 cards
                    player.drawCard(deck);
                }
            }

            // Play a round (for demonstration purposes)
            for (Player player : players) {
                player.playCard(player.getHand().get(0), discardPile); // Play the first card in hand
            }
            // Implement the game loop and overall game logic here
            // For example, dealing initial cards to players, handling turns, etc.
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
}
