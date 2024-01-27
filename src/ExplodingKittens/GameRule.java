package ExplodingKittens;

import ExplodingKittens.Controller.Game;
import ExplodingKittens.View.ClientTUI;

public class GameRule {
    private static String[] players = {"PlayerJoost", "PlayerJoris", "PlayerLenn"};
    private static ClientTUI clientTui;
    public boolean validatePlay(){
        return true;
    }
    public static void main(String[] args) {
        // Create a game with 3 players
        Game game = new Game("joost");

        // Run the game loop until it's over
        game.getDeck();
        while (!game.isGameOver()) {
            game.turn();
        }
        // Display the winner or any end-of-game information
        clientTui.mainMessage(game);
        //System.out.println("Game Over! Winner: " + game.getPlayers().get(0).getName());
    }
    //validatePlay() validates the played action
}
