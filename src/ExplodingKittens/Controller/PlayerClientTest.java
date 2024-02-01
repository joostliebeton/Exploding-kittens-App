package ExplodingKittens.Controller;

import ExplodingKittens.exceptions.ServerUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerClientTest {

    private PlayerClient playerClient;
    private BufferedReader in;
    private BufferedWriter out;
    private Socket socket;

    @BeforeEach
    void setUp() {
        // Create instances of socket, input, and output streams
        socket = new TestSocket();
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Initialize PlayerClient with the created instances
        playerClient = new PlayerClient();
    }

    @Test
    void testCreateConnection_Success() {
        assertDoesNotThrow(() -> playerClient.createConnection());
    }

    @Test
    void testCreateConnection_Failure() {
        // Simulate failure by setting input stream to null
        in = null;
        playerClient = new PlayerClient();
        Executable createConnection = () -> playerClient.createConnection();
        assertThrows(ExitProgram.class, createConnection, "User indicated to exit.");
    }

    @Test
    void testSendMessage_Success() {
        assertDoesNotThrow(() -> playerClient.sendMessage("Test message"));
    }

    // Other test methods...

    // Inner class to simulate Socket behavior
    private class TestSocket extends Socket {
        private ByteArrayOutputStream outputStream;

        public TestSocket() {
            this.outputStream = new ByteArrayOutputStream();
        }

        @Override
        public InputStream getInputStream() {
            // Simulate input stream behavior if needed
            return new ByteArrayInputStream("TURN:Player1".getBytes());
        }

        @Override
        public OutputStream getOutputStream() {
            return outputStream;
        }
    }
}
