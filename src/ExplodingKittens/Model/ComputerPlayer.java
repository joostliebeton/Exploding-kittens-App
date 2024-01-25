package ExplodingKittens.Model;

import ExplodingKittens.Controller.Game;

public class ComputerPlayer extends Player1 {
    ComputerPlayer(String name, Game game, Deck deck, Deck discardPile){super(name,game,deck,discardPile);
    }
    // create ComputerPlayer specific methods here:
    // such as "strategy" so that the computer plays on his own
}
