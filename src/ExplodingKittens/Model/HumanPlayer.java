package ExplodingKittens.Model;

import ExplodingKittens.Controller.Game;
import ExplodingKittens.View.ClientTUI;

public class HumanPlayer extends Player1{
   HumanPlayer(String name, Game game, Deck deck, Deck discardPile){
       super(name, game, deck, discardPile);
       ClientTUI clientTui = new ClientTUI();
   }
   // create HumanPlayer specific methods here:

    // determineMove

}
