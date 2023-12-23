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
//    public void play(){
//        switch (type){
////            case EXPLODING_KITTEN:
////                ExplodingKitten();
////                break;
//            case DEFUSE:
//                Defuse();
//                break;
//            case NOPE:
//                Nope();
//                break;
//            case SHUFFLE:
//                Shuffle();
//                break;
//            case SKIP:
//                Skip();
//                break;
//            case SEE_THE_FUTURE:
//                SeeTheFuture();
//                break;
//            case CAT_CARD1, CAT_CARD2, CAT_CARD3, CAT_CARD4, CAT_CARD5:
//                CatCard();
//                break;
//            case FAVOR:
//                Favor();
//                break;
//            case ATTACK:
//                Attack();
//                break;
//        }
//    }

    public void  Defuse(){
        //card.remove
    }
    public void  Nope(){
        //make a new method for nope only when the other things are done.
        }
    public void Shuffle(Deck deck) {
        // Implement your shuffle logic on the deck directly
        deck.shuffle();
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

