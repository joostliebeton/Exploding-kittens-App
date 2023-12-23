package ExplodingKittens;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Scanner;



public class Card {
    private CardType type;

    public Card(CardType type) {
        this.type = type;
    }

    public CardType getType() {
        return type;
    }
    // id; int

    // methode is playable()

    public boolean playable(){
        if(getType() == CardType.EXPLODING_KITTEN){
            return false;
        }
        if (getType() == CardType.DEFUSE || getType() == CardType.NOPE ||
                getType() == CardType.SHUFFLE || getType() == CardType.SKIP ||
                getType() == CardType.SEE_THE_FUTURE || getType() == CardType.CAT_CARD1 || getType() == CardType.CAT_CARD2 || getType() == CardType.CAT_CARD3 || getType() == CardType.CAT_CARD4 || getType() == CardType.CAT_CARD5 ||
                getType() == CardType.FAVOR || getType() == CardType.ATTACK){
            return true;
        }
        else{
            return false;
        }
    }
    // methode play()
    public void play(){
        switch (type){
//            case EXPLODING_KITTEN:
//                ExplodingKitten();
//                break;
            case DEFUSE:
                Defuse();
                break;
            case NOPE:
                Nope();
                break;
            case SHUFFLE:
                Shuffle();
                break;
            case SKIP:
                Skip();
                break;
            case SEE_THE_FUTURE:
                SeeTheFuture();
                break;
            case CAT_CARD1, CAT_CARD2, CAT_CARD3, CAT_CARD4, CAT_CARD5:
                CatCard();
                break;
            case FAVOR:
                Favor();
                break;
            case ATTACK:
                Attack();
                break;
        }
    }
    // the functionality of the cards
//    public void ExplodingKitten(){
//        if(deck.draw().equals(EXPLODINGKITTEN)){
//            if(Player.hasCard(DEFUSE)){
//                Scanner intExplodingKitten = new Scanner(System.in);
//                play(DEFUSE);
//                boolean defuseRemoved = false;
//
//                // Remove only the first occurrence of DEFUSE from the hand
//                for (Card card : Hand.hand) {
//                    if (card.getCardType() == Cards.DEFUSE && !defuseRemoved) {
//                        Hand.hand.remove(card);
//                        defuseRemoved = true;
//                    }
//                }
//                addDiscardPile(DEFUSE);
//                int pileSize = pileSize();
//                System.out.println("The size of the pile is: " + pileSize());
//                                                                                                            // if player has defuse; give input for index newly placed exploding kitten
//                try{
//                    System.out.println("Enter a integer number between 1 and " + pileSize +" for the index of the ExplodingKitten to be put back in the deck.");
//                    if(intExplodingKitten.hasNextInt() && intExplodingKitten.nextInt() > 0 && intExplodingKitten.nextInt() < pileSize){
//                        intExplodingKitten.nextInt();
//                    }
//                }
//                catch(Exception e){
//                    System.out.println("You have to enter a valid integer number.");
//                }
//                finally {
//                    intExplodingKitten.close();
//                }
//                int userInput = intExplodingKitten.nextInt();
//                                                                                                             // could also implement that it is optional to defuse or not. that is what said in the rules.
//                Deck.playingDeck.add(userInput, EXPLODINGKITTEN);
//                //Game.nextPlayer();  (make sure that after a player drawn a card (in this case the explodingKitten), the turn goes to the next player // maybe there is an exception for attackcard (you need to draw 2x)
//            }
//            else{
//                Game.EleminatePlayer();
//                //Game.nextPlayer();  (make sure that after a player is eliminated, the turn goes to the next player)
//            }
//        }
//    }
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

