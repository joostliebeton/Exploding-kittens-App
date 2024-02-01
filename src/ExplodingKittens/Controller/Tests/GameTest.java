package ExplodingKittens.Controller.Tests;

import ExplodingKittens.Controller.Game;
import ExplodingKittens.Model.HumanPlayer;
import ExplodingKittens.Model.Player1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class GameTest {
    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game("TestGame");
    }

    @Test
    public void testAddPlayer() {
        game.addPlayer("Player1");
        game.addPlayer("Player2");

        List<Player1> players = game.getPlayers();

        assertEquals(2, players.size());
        assertEquals("Player1", players.get(0).getName());
        assertEquals("Player2", players.get(1).getName());
    }

    @Test
    public void testDrawCard() {
        Player1 player = new HumanPlayer("Player1", game);
        game.addPlayer(player.getName());

        // Ensure the player's hand is empty initially
        assertTrue(player.getHandList().isEmpty());

        // Draw a card for the player
        game.drawCard(player);

        // Check if the player's hand contains the drawn card
        assertFalse(player.getHandList().isEmpty());
        assertEquals(1, player.getHand().size());
    }

    @Test
    public void testEliminatePlayer() {

        game.addPlayer("player1");
        game.addPlayer("player2");

        assertEquals(2, this.game.getPlayers().size());

        // Eliminate a player
        game.eliminatePlayer(game.getPlayer("player2"));

        // Check if the player is eliminated and removed from the game
        assertEquals(1, game.getPlayers().size());
        assertFalse(game.getPlayers().contains(game.getPlayer("player2")));

    }
}
