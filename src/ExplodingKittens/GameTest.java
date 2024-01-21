package ExplodingKittens;

import ExplodingKittens.Model.Deck;
import org.junit.jupiter.api.Test;

class GameTest {
        String[] players = {"PlayerJoost", "PlayerJoris", "Player3"};
        @Test
        void testAmountOfPlayers() {
            Game game = new Game(this.players);
            assert(game.getPlayers().size() == 3) : "after this test the amount of players should be 3";
        }
        @Test void testDeckInitialization() {
            //Game game = new Game(this.players);
            Deck deck = new Deck();
            deck.addExplodingKitten(); // does this even do something
            assert deck.pileSize() == 56 : "Deck should have 56 cards but is "+ deck.pileSize();
            //assertNotEquals(0, deck.pileSize()); should check if deck is not empty because that cant be the case
        }
        @Test void testHandInitialization() {
            Game game = new Game(this.players);
            for (Player player : game.getPlayers()) {
                assert(!player.getHand().isEmpty());
                // test if hand is empty
            }
        }
        @Test void testEliminatePlayer() {
            Game game = new Game(this.players);
            Player player = game.getPlayers().get(0);
            game.eliminatePlayer(player);
            assert(!game.getPlayers().contains(player)) : "Player"+ player.getName() + " should be eliminate";
        }
        @Test void testEliminatePlayerList() {
            Game game = new Game(this.players);
            Player player = game.getPlayers().get(0);
            game.eliminatePlayer(player);
            assert(game.getEliminatedPlayers().contains(player));
        }
        @Test void testEliminatePlayerListSize() {
            Game game = new Game(this.players);
            Player player = game.getPlayers().get(0);
            Player player2 = game.getPlayers().get(1);
            game.eliminatePlayer(player);
            game.eliminatePlayer(player2);
            assert(game.getEliminatedPlayers().size() == 2);
        }


    }
