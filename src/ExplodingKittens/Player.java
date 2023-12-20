package ExplodingKittens;

import java.util.ArrayList;

public class Player {
    // name String
    Deck deck = new Deck();
    public String name;
    // hand: Arraylist<Card>
    public ArrayList<Card> hand;
    public Player(String name){
        this.name=name;
        this.hand = new ArrayList<>();
    }

    // drawCard()
    public Card drawcard(){
        ArrayList<Card> cards = deck.getCards();
        Card drawncard = cards.get(cards.size() - 1);
        return drawncard;
    }
    // playCard()

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
            case CATCARD1:
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

//    public Card playCard(Card playedCard){
//        return Card;
//    }
    // hasCard()
    public boolean hasCard(){
        return true;
    }
}
