package ExplodingKittens;

import java.util.ArrayList;

import static ExplodingKittens.Card.Cards.DEFUSE;
import static ExplodingKittens.Card.Cards.EXPLODINGKITTEN;

public class Card {
    public Card Card;
    enum Cards{
      EXPLODINGKITTEN, DEFUSE, NOPE, SHUFFLE, SKIP, SEETHEFUTURE, CATCARD, FAVOR, ATTACK
        //if its correct this is how it is correct
    }
    // id; int
    public int id;
    // methode is playable()
    public boolean playable(){
        return true;
    }
    // methode play()
    public Card play(){
                return Card;
    }
    // the functionality of the cards
    public void ExplodingKitten(){
        if(Deck.drawCard.equals(EXPLODINGKITTEN)){
            if(player.hasCard(DEFUSE)){
                play(DEFUSE);
                hand.remove(DEFUSE); // removes the defuse card from the hand
                discardpile.add(DEFUSE);
                // player can secretly put the exploding kitten back in the deck
                Normalpile.add(EXPLODINGKITTEN);
                // after
            // is this the right way to do it?
                // player can defuse the exploding kitten

            }
            else{
                // player is out of the game
            }
            // player is out of the game
        }
        else{
            // player is still in the game
        }
        // we are making the game "exploding kittens", so we need to make the cards and implement methods regarding the cards
        // if the exploding kitten is drawn, the player is out of the game
        // if the player has a defuse card, he can defuse the exploding kitten and put it back in the deck
        // if the player has no defuse card, he is out of the game
        // if the player has a nope card, he can NOT nope the exploding kitten


    }
    public void  Defuse(){
        //card.remove
    }
    public void  Nope  (){

        }
    public void  Shuffle(){

        }
    public void  Skip(){

        }
    public void  SeeTheFuture(){ 
        }
    public void  CatCard(){

        }
    public void Favor (){

        }
    public void  Attack () {
    }
    
}

