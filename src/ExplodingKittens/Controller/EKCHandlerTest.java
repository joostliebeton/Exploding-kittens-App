package ExplodingKittens.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.Socket;



class EKCHandlerTest {

    private EKCHandler ekcHandler;
    private GameServerStub mockServer;
    private BufferedReader mockBufferedReader;
    private BufferedWriter mockBufferedWriter;
    private ByteArrayOutputStream outContent;
    private ByteArrayInputStream inContent;
    private Socket mockSocket;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new GameServerStub();
        String simulatedUserInput = ProtocolMessages.HI + "\n" + ProtocolMessages.CONNECT + ProtocolMessages.DELIMITER + "Player1" + "\n";
        inContent = new ByteArrayInputStream(simulatedUserInput.getBytes());
        outContent = new ByteArrayOutputStream();

        mockBufferedReader = new BufferedReader(new InputStreamReader(inContent));
        mockBufferedWriter = new BufferedWriter(new OutputStreamWriter(outContent));

        mockSocket = new Socket() {
            @Override
            public InputStream getInputStream() {
                return inContent;
            }

            @Override
            public OutputStream getOutputStream() {
                return outContent;
            }
        };

        ekcHandler = new EKCHandler(mockSocket, mockServer, "TestClient");
    }

    @Test
    void testHandleCommandHi() throws IOException {
        ekcHandler.run();
        mockBufferedWriter.flush();
        String output = outContent.toString();
        assert(output.contains(ProtocolMessages.HI));
    }

    @Test
    void testHandleCommandConnect() throws IOException {
        ekcHandler.run();
        mockBufferedWriter.flush();
        String output = outContent.toString();
        assert(mockServer.isPlayerAdded());
        assert(output.contains(ProtocolMessages.CONNECTED));
    }

    // Stub class for GameServer
    static class GameServerStub extends GameServer {
        private boolean playerAdded = false;

        @Override
        public void addPlayer(String name) {
            playerAdded = true;
        }

        public boolean isPlayerAdded() {
            return playerAdded;
        }

        // Implement other necessary methods or add logic as required
    }

    // Additional tests and utility methods...
}