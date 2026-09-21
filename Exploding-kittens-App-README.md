# Exploding Kittens – multiplayer client–server game (Java)

A networked, multiplayer version of the card game *Exploding Kittens*, built in Java with a
multithreaded server and a text-based client. Players connect to the server, join a lobby,
and play a full game against each other according to the official rules.

Developed primarily by **Joost Liebeton**, with **Joris Jacobs**, for the University of Twente courses
*Software Development* and *Programming* (Dec 2023 – Feb 2024). **Graded 9.**

## Features

- **Network play**: clients connect to a central server over TCP sockets and request a game
  with a chosen number of players.
- **Complete rule set**: Defuse, Nope, Skip, Attack, See the Future, Favor, Shuffle and
  cat-card combos, with the server keeping the authoritative game state.
- **In-game chat**: an extension on top of the base requirements.
- **Text user interfaces**: for both the server and the client.

## Architecture

The code follows the **Model–View–Controller** pattern:

| Package | Responsibility |
|---|---|
| `Model` | Game domain: `Card`, `CardType`, `Deck`, `Hand`, players |
| `View` | Text user interfaces for server and client (`ServerTUI`, `ClientTUI`) |
| `Controller` | Game logic (`Game`), networking (`GameServer`, `EKCHandler`, `PlayerClient`) and the protocol (`ProtocolMessages`) |
| `exceptions` | Protocol, connection and shutdown errors |

**Concurrency.** The server accepts connections on a `ServerSocket` and starts a dedicated
handler thread (`EKCHandler`) per connected client, so multiple players are served at the
same time. Shared command handling and message sending are `synchronized`. On the client
side, reading from the server and reading user input run on separate threads, so incoming
game events are shown while the player is typing.

**Protocol.** Client and server communicate with a custom text protocol of commands and
arguments separated by `~` (for example `CONNECT~name`, `PLAY_CARD~card_type`,
`TURN~username`). All messages are defined in `ProtocolMessages`.

## Testing

- **Unit tests** (JUnit 5) for the game logic, the server and the client:
  `GameTest`, `GameServerTest`, `PlayerClientTest`.
- **System tests** of complete multiplayer games, described in the project report.

## Running

Requires JDK 17 or newer (developed with JDK 20).

```bash
# compile (tests excluded)
javac -d out $(find src -name "*.java" -not -path "*/Tests/*")

# start the server, then enter a port when prompted
java -cp out ExplodingKittens.Controller.GameServer

# in one or more other terminals: start a client and connect to that port
java -cp out ExplodingKittens.Controller.PlayerClient
```

To run the tests, open the project in IntelliJ IDEA with JUnit 5 on the classpath.

## Documentation

The [project report](Programming_Report__Exploding_Kittens_Joris_Jacobs_Joost_Liebeton_1_.pdf)
covers the class diagram, the requirements mapping, the MVC design and the test approach.
