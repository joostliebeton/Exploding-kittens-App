package ExplodingKittens;

import javax.swing.*;
import java.util.List;

public class GameRule {
    private static String[] players = {"Playerjoost", "Playerjoris", "Player 3"};

    public boolean validatePlay(){
        return true;
    }
    public static void main(String[] args) {
        // Create a game with 3 players
        Game game = new Game(players);

        // Run the game loop until it's over
        while (!game.isGameOver()) {
            game.turn();
        }

        // Display the winner or any end-of-game information
        System.out.println("Game Over! Winner: " + game.getPlayers().get(0).getName());
    }
    //validatePlay() validates the played action
}
