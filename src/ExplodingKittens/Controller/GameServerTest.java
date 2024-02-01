package ExplodingKittens.Controller;

import ExplodingKittens.Model.Card;
import ExplodingKittens.Model.CardType;
import ExplodingKittens.Model.Player1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameServerTest {
    private GameServer gameServer;

    @BeforeEach
    public void setUp() {
        gameServer = new GameServer();
        // You may need to initialize or mock dependencies here
    }

    @Test
    public void testSetupGame() {
        // Test the setupGame method
        gameServer.setupGame();
        assertNotNull(gameServer.getGameName());
        // Add more assertions as needed
    }
    @Test
    public void testAddPlayer() {
        // Test adding players to the game
        gameServer.setupGame();
        gameServer.addPlayer("Player1");
        gameServer.addPlayer("Player2");
        assertEquals(2, gameServer.getGame().getPlayers().size());
        // Add more assertions as needed
    }
    @Test
    public void testDrawCard() {
        gameServer.setupGame();
        gameServer.addPlayer("Player1");
        gameServer.addPlayer("Player2");
        gameServer.startGameProcess();
        Player1 currentPlayer = gameServer.getGame().getCurrentPlayer();
        int initialHandSize = currentPlayer.getHandList().size();
        gameServer.drawCard();
        assertEquals(initialHandSize + 1, currentPlayer.getHandList().size());
        assertNotEquals(currentPlayer.getName(), "Player2");

    }
    @Test
    public void testPlayCardcmd() {
        // Test playing a card by a player
        gameServer.setupGame();
        gameServer.addPlayer("Player1");
        gameServer.addPlayer("Player2");
        gameServer.startGameProcess();
        // Add players and cards to the game as needed
        Player1 currentPlayer = gameServer.getGame().getCurrentPlayer();
        Card card = gameServer.getGame().getPlayer("Player1").getHand().get(2);
        String result = gameServer.playCardcmd(card.getType(), currentPlayer);
        assert (result.contains(card.getType().name()) || result.contains("nope"));
        // Assert the result based on expected behavior
        // Add more assertions as needed
    }

    // Add more test methods to cover other functionalities

}

