package ExplodingKittens;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Scanner;

import static ExplodingKittens.Card.Cards.DEFUSE;
import static ExplodingKittens.Card.Cards.EXPLODINGKITTEN;


public class Card {
    private Deck deck;
    public Card Card;
    enum Cards{
          EXPLODINGKITTEN, DEFUSE, NOPE, SHUFFLE, SKIP, SEETHEFUTURE, CATCARD1,CATCARD2,CATCARD3, CATCARD4, CATCARD5, FAVOR, ATTACK

    }
    int amount;
    public HashMap<Cards, Integer> amountsCardstype = new HashMap<>();
    // id; int
    public int id;
    // methode is playable()
    public Cards cardType;
    public Card(int id, Cards cardType, Deck deck){
        this.id = id;
        this.cardType = cardType;
        this.deck = deck;
    }
    public Cards getCardType(){
        return cardType;
    }
    public void addDiscardPile(Card card){
        deck.discardPile.add(card);
    }
    public boolean playable(){
        return true;
    }
    // methode play()
    public void play(){
    }
    // the functionality of the cards
    public void ExplodingKitten(){
        if(Deck.drawCard().equals(EXPLODINGKITTEN)){
            if(Player.hasCard(DEFUSE)){
                Scanner intExplodingKitten = new Scanner(System.in);
                play(DEFUSE);
                boolean defuseRemoved = false;

                // Remove only the first occurrence of DEFUSE from the hand
                for (Card card : Hand.hand) {
                    if (card.getCardType() == Cards.DEFUSE && !defuseRemoved) {
                        Hand.hand.remove(card);
                        defuseRemoved = true;
                    }
                }
                addDiscardPile(DEFUSE);
                int pileSize = pileSize();
                System.out.println("The size of the pile is: " + pileSize());
                                                                                                            // if player has defuse; give input for index newly placed exploding kitten
                try{
                    System.out.println("Enter a integer number between 1 and " + pileSize +" for the index of the ExplodingKitten to be put back in the deck.");
                    if(intExplodingKitten.hasNextInt() && intExplodingKitten.nextInt() > 0 && intExplodingKitten.nextInt() < pileSize){
                        intExplodingKitten.nextInt();
                    }
                }
                catch(Exception e){
                    System.out.println("You have to enter a valid integer number.");
                }
                finally {
                    intExplodingKitten.close();
                }
                int userInput = intExplodingKitten.nextInt();
                                                                                                             // could also implement that it is optional to defuse or not. that is what said in the rules.
                Deck.playingDeck.add(userInput, EXPLODINGKITTEN);
                //Game.nextPlayer();  (make sure that after a player drawn a card (in this case the explodingKitten), the turn goes to the next player // maybe there is an exception for attackcard (you need to draw 2x)
            }
            else{
                Game.EleminatePlayer();
                //Game.nextPlayer();  (make sure that after a player is eliminated, the turn goes to the next player)
            }
        }
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

