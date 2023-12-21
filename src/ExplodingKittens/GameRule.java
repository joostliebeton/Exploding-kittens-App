package ExplodingKittens;

public class GameRule {
    public boolean validatePlay(){
        return true;
    }
    public static void main(String[] args) {
        // Create a game with 3 players
        Game game = new Game(3);
        System.out.println("Game created with " + game.getPlayers().size() + " players.");
        System.out.println(game.getPlayers().get(0).getName() + " has " + game.getPlayers().get(0).getHand().size() + " cards in hand.");
        // Start the game
        game.startGame();
        System.out.println(game.getPlayers().get(0).getName() + " has " + game.getPlayers().get(0).getHand().size() + " cards in hand.");
    }
    //validatePlay() validates the played action
}
