package ExplodingKittens.Model;


import ExplodingKittens.Controller.ProtocolMessages;
import ExplodingKittens.View.ClientTUI;

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
        else if(getType() == CardType.DEFUSE || getType() == CardType.NOPE ||
                getType() == CardType.SHUFFLE || getType() == CardType.SKIP ||
                getType() == CardType.SEE_THE_FUTURE || getType() == CardType.CAT_CARD1 || getType() == CardType.CAT_CARD2 || getType() == CardType.CAT_CARD3 || getType() == CardType.CAT_CARD4 || getType() == CardType.CAT_CARD5 ||
                getType() == CardType.FAVOR || getType() == CardType.ATTACK){
            return true;
        }
        else{
            return false;
        }
    }

    public String seeTheFuture(Deck deck){
        if (!(deck.isEmpty())) {
            StringBuilder responseBuilder = new StringBuilder();
            responseBuilder.append(ProtocolMessages.FUTURE).append(ProtocolMessages.DELIMITER);
            Card[] cards = deck.peek();
            for (Card card : cards) {
                responseBuilder.append(card.getType()).append(ProtocolMessages.DELIMITER);
            }
            if (!(cards.length == 0)) {
                responseBuilder.deleteCharAt(responseBuilder.length() - 1);
            }
            return (responseBuilder.toString());
        } else {
            return ("DECK IS EMPTY");
        }
    }
    public void  Nope(){
        //make a new method for nope only when the other things are done.
        }
    public String Shuffle(Deck deck) {
        if (deck != null) {
            deck.shuffle();
            return (ProtocolMessages.ANNOUNCEMENT + ProtocolMessages.DELIMITER);

        } else {
            return ("DECK IS NULL");
        //System.out.println("Error: Deck reference is null.");
        }
    }
    public void Attack(Player1 currentplayer, Player1 targetPlayer){
        if (currentplayer.getExtraTurns() >1) {
            currentplayer.setExtraTurns(0,0);
            targetPlayer.setExtraTurns(3, targetPlayer.getExtraTurns());
            //System.out.println(currentplayer.getName() + " played an Attack card. " + targetPlayer.getName() + " will have " + targetPlayer.getExtraTurns() + " extra turns.");
        } else{
            targetPlayer.setExtraTurns(1, targetPlayer.getExtraTurns());

            //System.out.println(currentplayer.getName() + " played an Attack card. " + targetPlayer.getName() + " will have " + targetPlayer.getExtraTurns() + " extra turns.");
        }
    }


    public boolean isActionCard() {
        return getType() == CardType.ATTACK || getType() == CardType.FAVOR || getType() == CardType.SHUFFLE || getType() == CardType.SEE_THE_FUTURE || getType() == CardType.SKIP || getType() == CardType.NOPE;
    }

    public boolean isCatCard() {
        if (getType() == CardType.CAT_CARD1 || getType() == CardType.CAT_CARD2 || getType() == CardType.CAT_CARD3 || getType() == CardType.CAT_CARD4 || getType() == CardType.CAT_CARD5) {
            return true;
        } else {
            return false;
        }
    }
}

