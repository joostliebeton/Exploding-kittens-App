package ExplodingKittens.Controller;

public class ProtocolMessages {
    public static final String DELIMITER = "~";
    /////////////////BOTH CLIENT AND SERVER/////////////////////
    public static final String HI = "HI"; // [par] supported extionsions
    //////////////Client to server////////////////////////
    public static final String CONNECT = "CONNECT"; // [par] name (CONNECT~name)
    public static final String REQUEST_GAME = "REQUEST_GAME"; // [par] totalamount of player in game (REQUEST_GAME~amount) >=2, amount of which are AI players in the game int >=0 (REQUEST_GAME~amount~amountAI)
    public static final String PLAY_CARD = "PLAY_CARD"; // [par] card_value (PLAY_CARD~card_type) (note there is also the Play_combo_card and the Play favor card)
    public static final String DRAW_CARD = "DRAW_CARD"; // [par] none (DRAW_CARD)
    public static final String CHOOSE_CARD_IN_HAND = "CHOOSE_CARD_IN_HAND"; // [par] card_value (CHOOSE_CARD_IN_HAND~card_type) favor card response.
    public static final String PLAY_FAVOR = "PLAY_FAVOR"; // [par] username victim(STRING) (PLAY_FAVOR~victim)
    public static final String PLAY_COMBO = "PLAY_COMBO"; // [par] card_value, amount of cards, username victim(STRING) (PLAY_COMBO_CARD~card_type~amount~victim)
    public static final String GENERAL_CARD_RESPONSE = "GENERAL_CARD_RESPONSE"; // [par] card_value (GENERAL_CARD_RESPONSE~card_value) // tells the receiver that the sender has played a card //to: all clients
    public static final String PLAY_DEFUSE = "PLAY_DEFUSE"; // [par] index of draw pile, (PLAY_DEFUSE, index))
    public static final String DRAW_PILE_SIZE = "DRAW_PILE_SIZE"; // [par] none (DRAW_PILE_SIZE) request the draw pile size.
    public static final String USERS_HAND_SIZE = "USERS_HAND_SIZE"; // [par] player username (String) (USERS_HAND_SIZE~username)
    public static final String REQUEST_ALIVE_PLAYERS = "REQUEST_ALIVE_PLAYERS"; // [par] none (REQUEST_ALIVE_PLAYERS)
    public static final String REQUEST_PLAYERS_LOBBY = "REQUEST_PLAYERS_LOBBY"; // [par] none (REQUEST_PLAYERS_LOBBY)
    public static final String REQUEST_CARD_IN_HAND = "REQUEST_CARD_IN_HAND"; // [par] none (REQUEST_CARD_IN_HAND)

    /////////////////server to client/////////////////////

    public static final String CONNECTED = "CONNECTED"; // [par] none (CONNECTED) // tells the client that the connection was succesfull//to: client that sent the connect command
    public static final String GAME_STARTED = "GAME_STARTED"; // [par] EXTENSIONS (GAME_STARTED~POSSIBLE-EXTENSIONS) // tells the client that the game has started//to: all clients
    public static final String TURN = "TURN"; // [par] username (TURN~username) // tells the client that it is his turn //to: ALL CLIENTS
    public static final String ANNOUNCEMENT = "ANNOUNCEMENT"; // [par] player username, message (ANNOUNCEMENT~name~message) // tells the client that something happened //to: ALL CLIENTS
    public static final String GETS_NOPED = "GETS_NOPED"; // [par]Message, victim username, card_value, player username(GETS_NOPED~victim~card_value~player) // tells the client that something was noped //to: ALL CLIENTS
    public static final String ATTACKED = "ATTACKED"; // [par] attacker username, player username (ATTACKED~attacker~username) // tells the client that he was attacked //to: ALL CLIENTS
    public static final String CARD_EXCHANGED = "CARD_EXCHANGED"; // [par] REASON, victim username, player username, card_value (CARD_EXCHANGED~reason~victim~player) // tells the client that he exchanged a card //to: ALL CLIENTS
    public static final String CARD_RECEIVED = "CARD_RECEIVED"; // [par] REASON, victim username, player username, card_value (CARD_RECEIVED~reason~victim~Card_value) // tells the client that he recieved a card //to: reciever of card
    public static final String CARD_GIVEN = "CARD_GIVEN"; // [par] REASON, reciever username, card_value (CARD_GIVEN~reason~receiver~Card_value) // tells the client that he gave a card //to: giver of card (victim)
    public static final String FUTURE = "FUTURE"; // [par] card_value, card_value, card_value (FUTURE~card_value~card_value~card_value) // tells the client the top 3 cards of the draw pile //to: client that requested the future
    public static final String PICK_CARD_IN_HAND = "PICK_CARD_IN_HAND"; // [par] reciever username (PICK_CARD_IN_HAND~receiver) // tells the client that he has to pick a card from his hand //to: client that has to pick a card from his hand //favor
    public static final String GAME_FINISHED = "GAME_FINISHED"; // [par] winner username (GAME_FINISHED~winner) // tells the client that the game has finished //to: all clients
    public static final String PLAY_NOPED = "PLAY_NOPED"; // [par] none (PLAY_NOPED) // tells the client that HE CAN NOPE FOR 5 SEC//to: ALL CLIENTS
    public static final String RESPONSE_DRAW_PILE_SIZE = "RESPONSE_DRAW_PILE_SIZE"; // [par] size of draw pile (RESPONSE_DRAW_PILE_SIZE~size) // tells the client the size of the draw pile //to: client that requested the size of the draw pile
    public static final String RESPONSE_USERS_HAND_SIZE = "RESPONSE_USERS_HAND_SIZE"; // [par] size of hand (RESPONSE_USERS_HAND_SIZE~size) // tells the client the size of the hand of a player //to: client that requested the size of the hand of a player
    public static final String RESPONSE_ALIVE_PLAYERS = "RESPONSE_ALIVE_PLAYERS"; // [par] player username, player username, player username, player username, player username (RESPONSE_ALIVE_PLAYERS~username~username~username~username~username) // tells the client the alive players //to: client that requested the alive players
    public static final String RESPONSE_PLAYERS_LOBBY = "RESPONSE_PLAYERS_LOBBY"; // [par] player username, player username, player username, player username, player username (RESPONSE_PLAYERS_LOBBY~username~username~username~username~username) // tells the client the players in the lobby //to: client that requested the players in the lobby
    public static final String EXCEPTION = "EXCEPTION"; // [par] exception type (EXCEPTION~exception type) // tells the client that something went wrong //to: client that sent the command that caused the exception
    public static final String GENERAL_CARD_REQUEST = "GENERAL_CARD_REQUEST"; // [par] VICTIM (GENERAL_CARD_REQUEST~VICTIM) // requests the receiver to select a card of a kind //to: player needing to pick a kind of card
    public static final String REQUEST_CARD_IN_HAND_RESPONSE = "REQUEST_CARD_IN_HAND_RESPONSE"; // [par] card_value, card_value (REQUEST_CARD_IN_HAND_RESPONSE~card_value~card_value) // tells the client his hands//to: client that requested the card


    ///////////////////////optional////////////////////////
//      public static final String CHAT = "CHAT"; // [par] message (CHAT~message)
//      public static final String CHAT = "CHAT"; // [par] sender username, message (CHAT~sender~message)

    //////////////////NOTES//////////////////////////
    ///Note about double card/cat combos: You will notice that there is no
    // command to select a card for a double card/cat combo. Given that we
    // did not decide on such a command in the protocol session, the most logical choice seems to
    // be that this selection simply be done by the server selecting a random card from the player's hand.
}
