package ExplodingKittens.View;
import ExplodingKittens.utils.TextIO;
import java.io.PrintWriter;

/**
 * Hotel Server TUI for user input and user messages
 */
public class ServerTUI {
    /** The PrintWriter to write messages to */
    private PrintWriter console;
    public ServerTUI() {
        console = new PrintWriter(System.out, true);
    }
    public void showMessage(String message) {
        console.println(message);
    }

    public boolean getBoolean(String question) {
        console.print(question);
        console.flush();
        return TextIO.getlnBoolean();

    }

}
