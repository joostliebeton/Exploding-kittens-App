package ExplodingKittens.Controller;

import java.io.*;
import java.net.Socket;

public class EKCHandler implements Runnable {
    /** The socket and In- and OutputStreams */
    private BufferedReader in;
    private BufferedWriter out;
    private Socket sock;

    /** The connected HotelServer */
    private GameServer server;

    /** Name of this ClientHandler */
    private String name;
    /**
     * Constructs a new EKCHandler. Opens the In- and OutputStreams.
     *
     * @param sock The client socket
     * @param srv  The connected server
     * @param name The name of this ClientHandler
     */
    public EKCHandler(Socket sock, GameServer srv, String name) {
        try {
            in = new BufferedReader(
                    new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(sock.getOutputStream()));
            this.sock = sock;
            this.server = srv;
            this.name = name;
        } catch (IOException e) {
            shutdown();
        }
    }
    /**
     * Continuously listens to client input and forwards the input to the
     * {@link #handleCommand(String)} method.
     */
    public void run() {
        try {
            String msg = in.readLine();
            while (msg != null) {
                System.out.println("> [" + name + "] Incoming: " + msg);
                handleCommand(msg);
                msg = in.readLine();
            }
//			shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }
    /**
     * Handles commands received from the client by calling the according
     * methods at the HotelServer. For example, when the message "i Name"
     * is received, the method doIn() of HotelServer should be called
     * and the output must be sent to the client.
     *
     * If the received input is not valid, send an "Unknown Command"
     * message to the server.
     *
     * @param msg command from client
     * @throws IOException if an IO errors occur.
     */
    public void handleCommand(String msg) throws IOException {
        String name = null;

        String[] words = msg.split(";");
        String command = words[0];
        if (words.length > 1) {
            name = words[1];
        }
        if (words.length > 2) {
            if ((command.charAt(0) == ProtocolMessages.ACT)) {
                password = words[2];
            } else if (command.charAt(0) == ProtocolMessages.BILL) {
                nightCount = Integer.parseInt(words[2]);
            } else {
                System.out.println("Error, look again at the menu.");
            }
        }

        if (command.length() == 1) {
            switch (command.charAt(0)) {
                case ProtocolMessages.HELLO:
                    System.out.println("Found command h");
                    out.write(ProtocolMessages.HELLO + ";" + srv.getHotelName());
                    out.newLine();
                    out.flush();
                    break;
                case ProtocolMessages.IN:
                    out.write(srv.doIn(name));
                    out.newLine();
                    out.flush();
                    break;
                case ProtocolMessages.OUT:
                    out.write(srv.doOut(name));
                    out.newLine();
                    out.flush();
                    break;
                case ProtocolMessages.ACT:
                    out.write(srv.doAct(name, password));
                    out.newLine();
                    out.flush();
                    break;
                case ProtocolMessages.BILL:
                    out.write(srv.doBill(name, String.valueOf(nightCount)));
                    out.newLine();
                    out.flush();
                    break;
                case ProtocolMessages.ROOM:
                    out.write(srv.doRoom(name));
                    out.newLine();
                    out.flush();
                    break;
                case ProtocolMessages.PRINT:
                    out.write(srv.doPrint());
                    out.newLine();
                    out.flush();
                    break;
                case ProtocolMessages.EXIT:
                    shutdown();
                    break;
                default:
                    out.write("Command not found");
                    out.newLine();
                    out.flush();
            }
        } else throw new IllegalArgumentException("Empty command");

    }
    private void shutdown() {
        System.out.println("> [" + name + "] Shutting down.");
        try {
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeClient(this);
    }
}
