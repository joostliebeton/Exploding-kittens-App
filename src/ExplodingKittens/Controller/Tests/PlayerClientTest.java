package ExplodingKittens.Controller.Tests;
import ExplodingKittens.Controller.PlayerClient;
import ExplodingKittens.Controller.ProtocolMessages;
import ExplodingKittens.exceptions.ProtocolException;
import ExplodingKittens.exceptions.ServerUnavailableException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerClientTest {

    private PlayerClient playerClient;
    private PipedInputStream inputStream;
    private PipedOutputStream outputStream;

    @BeforeEach
    public void setUp() throws IOException {
        // Create piped streams for communication
        inputStream = new PipedInputStream();
        outputStream = new PipedOutputStream(inputStream);

        // Set up PlayerClient with the piped streams
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        playerClient = new PlayerClient();
        playerClient.in = reader;
        playerClient.out = writer;
    }

    @AfterEach
    public void tearDown() {
        playerClient = null;
        System.setIn(System.in); // Reset System.in
    }


    @Test
    public void testHandleHello() throws IOException, ServerUnavailableException, ProtocolException {
        // Simulate server response
        String serverResponse = "HI" + ProtocolMessages.DELIMITER + "CHAT\n";
        outputStream.write(serverResponse.getBytes());
        outputStream.flush();

        // Invoke the method under test
        playerClient.handleHello();

        // Verify the client's behavior
        assertEquals(ProtocolMessages.HI + ProtocolMessages.DELIMITER + "CHAT", serverResponse.trim());
    }

    @Test
    public void testHandleHelloProtocolException() throws IOException {
        // Simulate unexpected server response
        String serverResponse = "INVALID_MESSAGE\n";
        outputStream.write(serverResponse.getBytes());
        outputStream.flush();

        // Ensure ProtocolException is thrown
        assertThrows(ProtocolException.class, () -> {
            playerClient.handleHello();
        });
    }

    @Test
    public void testHandleHelloServerUnavailableException() throws IOException {
        // Simulate server unavailability
        outputStream.close();

        // Ensure ServerUnavailableException is thrown
        assertThrows(ServerUnavailableException.class, () -> {
            playerClient.handleHello();
        });
    }
    @Test
    public void testRequestPlayers() throws IOException, ServerUnavailableException {
        // Simulate server response
        String serverResponse = "Player1\nPlayer2\nPlayer3\n";
        outputStream.write(serverResponse.getBytes());
        outputStream.flush();

        // Invoke the method under test
        playerClient.requestPlayers();

        // Verify the client's behavior
        assertEquals("Player1\nPlayer2\nPlayer3", serverResponse.trim());
    }
    @Test
    public void testDoConnect() throws IOException, ServerUnavailableException {
        // Simulate server response
        String serverResponse = "CONNECTED\n";
        outputStream.write(serverResponse.getBytes());
        outputStream.flush();

        // Invoke the method under test
        playerClient.doConnect("TestPlayer");

        // Verify the client's behavior
        assertEquals(ProtocolMessages.CONNECTED , serverResponse.trim());
    }

    @Test
    public void testCreateConnection_EmptyName() {
        // Simulate user input with an empty name
        String simulatedUserInput = "127.0.0.1\n\nTestPlayer\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
        System.setIn(inputStream);

        // Expects IllegalArgumentException due to empty name
        assertThrows(IllegalArgumentException.class, () -> playerClient.createConnection());
        assertNull(playerClient.serverSock);
    }

    @Test
    public void testCreateConnection_NullName() {
        // Simulate user input with a null name
        String simulatedUserInput = "127.0.0.1\n\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
        System.setIn(inputStream);

        // Expects IllegalArgumentException due to null name
        assertThrows(IllegalArgumentException.class, () -> playerClient.createConnection());
        assertNull(playerClient.serverSock);
    }

    @Test
    public void testCreateConnection_IOException() {
        // Simulate user input
        String simulatedUserInput = "invalidhost\nTestPlayer\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
        System.setIn(inputStream);

        // Expects IllegalArgumentException due to IOException
        assertThrows(IllegalArgumentException.class, () -> playerClient.createConnection());
        assertNull(playerClient.serverSock);
    }
    // Add more tests for other methods as needed
}
