package ExplodingKittens.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EKCHandlerTest {

    private EKCHandler ekcHandler;
    private ByteArrayOutputStream outContent;
    private ByteArrayInputStream inContent;

    @BeforeEach
    void setUp() {
        // Create a mock input and output stream
        outContent = new ByteArrayOutputStream();
        inContent = new ByteArrayInputStream("Test message\n".getBytes());

        // Create the handler with mocked streams
//        ekcHandler = new EKCHandler(new BufferedReader(new InputStreamReader(inContent)),
//                new BufferedWriter(new OutputStreamWriter(outContent)),
//                null, "TestClient");
    }

    @Test
    void testHandleCommandHi() throws IOException {
        ekcHandler.handleCommand(ProtocolMessages.HI);
        outContent.flush();
        String output = outContent.toString();
        assertEquals(ProtocolMessages.HI + ProtocolMessages.DELIMITER + "null", output.strip());
    }

    @Test
    void testHandleCommandConnect() throws IOException {
        ekcHandler.handleCommand(ProtocolMessages.CONNECT + ProtocolMessages.DELIMITER + "Player1");
        outContent.flush();
        String output = outContent.toString();
        assertEquals(ProtocolMessages.CONNECTED, output.strip());
    }

    // Add more tests for other command handling methods...

}
