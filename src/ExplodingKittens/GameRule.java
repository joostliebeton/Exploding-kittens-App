package ExplodingKittens;

public class GameRule {
    public boolean validatePlay(){
        return true;
    }
    public static void main(String[] args) {
        // Create a game with 3 players
        Game game = new Game(3);

        // Run the game loop until it's over
        while (!game.isGameOver()) {
            game.turn();
        }

        // Display the winner or any end-of-game information
        System.out.println("Game Over! Winner: " + game.getPlayers().get(0).getName());
    }
    //validatePlay() validates the played action
}
