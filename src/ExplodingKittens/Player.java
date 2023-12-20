package ExplodingKittens;

import java.util.ArrayList;

public class Player {
    // name String
    public String name;
    // hand: Arraylist<Card>
    public static ArrayList<Card> hand;
    public Card Card;

    // drawCard()
    public Card drawcard(){

        return Card;
    }
    public void playCard(Card.Cards cardPlayed) {
//this is the code for the cards; when played, a function will compile the code of that card.
        switch (cardPlayed) {

            case DEFUSE:
                Card.Defuse();
                break;
            case NOPE:
                Card.Nope();
                break;
            case SHUFFLE:
                Card.Shuffle();
                break;
            case SKIP:
                Card.Skip();
                break;
            case SEETHEFUTURE:
                Card.SeeTheFuture();
                break;
            case CATCARD1, CATCARD2, CATCARD3, CATCARD4, CATCARD5:
                Card.CatCard();
                break;
            case FAVOR:
                Card.Favor();
                break;
            case ATTACK:
                Card.Attack();
                break;
        }
    }

    public static boolean hasCard(Card.Cards card){
        return true;
    }
}
