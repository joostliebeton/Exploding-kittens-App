package Game;

import java.util.ArrayList;

public interface Players {
    public ArrayList<Player> ALL_PLAYERS = new ArrayList<>();
    public ArrayList<Player> ELIMINATED_PLAYERS = new ArrayList<>();
    public ArrayList<Player> PLAYERS_IN_GAME = new ArrayList<>();

    // players have names
    // cards in hand
    // state (eliminated or not)
    // their turn or not
    // can nope or not
}
