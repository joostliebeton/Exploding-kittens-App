package ExplodingKittens;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    //players: Arraylist<player>

    private Deck deck;
    private Player currentPlayer;
    private ArrayList<Player> eliminatedplayers = new ArrayList<>();
    private List<Player> players;
    private DiscardPile discardPile;

    public List<Player> getPlayers() {
        return players;
    }

    private int currentPlayerIndex;

    public Game(String[] Players) {
        initializeDeck();
        initializePlayers(Players);
        initilializehands();
        initializeDiscardPile();
        currentPlayerIndex = 0;
    }

    private void initializeDeck() {
        deck = new Deck();
        // Add Exploding Kittens to the deck after initial cards have been dealt

    }

    private void initilializehands() {
        for (int i = 0; i < /*player.size()*/ 5; i++) {
            for (Player player : players) {
                player.drawCard(deck);
            }
        }
        for (int i = 0; i < 4; i++) {
            deck.addExplodingKitten();
        }
        deck.shuffle();
    }

    private void initializeDiscardPile() {
        discardPile = new DiscardPile();
    }

    private void initializePlayers(String[] names) {
        players = new ArrayList<>();
        for (String name : names) {
            players.add(new Player(name, this, deck));
        }
    }

    public void turn() {
        currentPlayer = getCurrentPlayer();
        while (currentPlayer.extraTurns > 0) {
            System.out.println(currentPlayer.getName() + " has " + currentPlayer.extraTurns + " extra turns!");
            currentPlayer.playCard(getPlayerInput(), discardPile);
            if (discardPile.getDiscardPile()[discardPile.length() - 1].getType() == CardType.ATTACK) {
                currentPlayer.extraTurns = 0;
                currentPlayer.turnsToSkip = 1;
                Player targetPlayer = getNextPlayer();
                targetPlayer.extraTurns += 3;
                System.out.println(targetPlayer.getName() + " has " + targetPlayer.extraTurns + " extra turns!");
                handleTurnEnd();
                return;
            }
            currentPlayer.extraTurns--;
            currentPlayer.drawCard(deck);
        }
        currentPlayer.playCard(getPlayerInput(), discardPile);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to end your turn? (yes/no)");
        String response = scanner.nextLine().toLowerCase();
        if (response.equals("yes")) {
            handleTurnEnd();
        } else {
            turn();
        }
    }

    private void handleTurnEnd() {
        if (currentPlayer.turnsToSkip > 0) {
            System.out.println(currentPlayer.getName() + " skips a turn.");
            currentPlayer.turnsToSkip--;
            endTurnNoDraw();
        } else {
            endTurn();
        }
    }

    private int getPlayerInput() {
        Scanner scanner = new Scanner(System.in);
        int cardIndex;

        do {
            System.out.println(currentPlayer.getName() + ", choose a card to play (enter the card index): ");
            for (int i = 0; i < currentPlayer.getHand().size(); i++) {
                System.out.println(i + ": " + currentPlayer.getHand().get(i).getType());
            }
            cardIndex = scanner.nextInt();

            // Check if the entered index is valid
            if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size()) {
                System.out.println("Invalid card index. Please try again.");
            }
        } while (cardIndex < 0 || cardIndex >= currentPlayer.getHand().size());

        return cardIndex;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getNextPlayer() {
        return players.get((currentPlayerIndex + 1) % players.size());
    }

    public void endTurn() {
        currentPlayer.drawCard(deck);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public void endTurnNoDraw() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

    }

    boolean isGameOver() {
        return players.size() == 1;
    }

    // Other methods as needed
    public void getDiscardPile() {
        for (Card card : discardPile.getDiscardPile()) {
            System.out.println(card.getType());
        }
    }

    public void getDeck() {
        for (Card card : deck.getDeck()) {
            System.out.println(card.getType());
        }
    }

    public void eliminatePlayer(Player player) {
        players.remove(player);
        eliminatedplayers.add(player);
        System.out.println(player.getName() + " has been eliminated!");
        currentPlayerIndex = (currentPlayerIndex - 1) % players.size();
    }

    public ArrayList<Player> getEliminatedPlayers() {
        return eliminatedplayers;
    }
}