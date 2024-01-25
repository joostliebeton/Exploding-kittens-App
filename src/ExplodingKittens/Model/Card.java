package ExplodingKittens.Model;


import ExplodingKittens.View.ClientTUI;

public class Card {
    private ClientTUI clientTui = new ClientTUI();
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

    public void seeTheFuture(Deck deck, Player player){
        if (!(deck.isEmpty())) {
            Card[] cards = deck.peek();
            clientTui.seeTheFutureMessage(player, 1);
            for (Card card : cards) {
                clientTui.seeTheFutureMessage(card); //prints card
            }
        } else {
            clientTui.seeTheFutureMessage(player, 2);
        }
    }
    public void  Nope(){
        //make a new method for nope only when the other things are done.
        }
    public void Shuffle(Deck deck, Player player) {
        if (deck != null) {
            deck.shuffle();
            clientTui.ShuffleMessage(player, 1);
        //System.out.println(player.getName() + " shuffled the deck.");
        } else {
            clientTui.ShuffleMessage(player, 2);
        //System.out.println("Error: Deck reference is null.");
        }
    }
    public void Attack(Player currentplayer, Player targetPlayer){
        if (currentplayer.getExtraTurns() >1) {
            currentplayer.setExtraTurns(0,0);
            targetPlayer.setExtraTurns(3, targetPlayer.getExtraTurns());
            clientTui.AttackMessage(currentplayer, targetPlayer);
            //System.out.println(currentplayer.getName() + " played an Attack card. " + targetPlayer.getName() + " will have " + targetPlayer.getExtraTurns() + " extra turns.");
        } else{
            targetPlayer.setExtraTurns(1, targetPlayer.getExtraTurns());
            clientTui.AttackMessage(currentplayer, targetPlayer);
            //System.out.println(currentplayer.getName() + " played an Attack card. " + targetPlayer.getName() + " will have " + targetPlayer.getExtraTurns() + " extra turns.");
        }
    }




}

