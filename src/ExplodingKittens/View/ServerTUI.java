package ExplodingKittens.View;
import ExplodingKittens.utils.TextIO;
import java.io.PrintWriter;

/**
 * Game Server TUI for user input and user messages.
 */
public class ServerTUI {
    /** The PrintWriter to write messages to */
    private PrintWriter console;

    /**
     * Constructs a new ServerTUI object.
     * Initializes the PrintWriter to write messages to the console.
     */
    public ServerTUI() {
        console = new PrintWriter(System.out, true);
    }

    /**
     * Displays a message to the user on the console.
     * @param message The message to be displayed.
     */
    public void showMessage(String message) {
        console.println(message);
    }

    /**
     * Prompts the user with a question and expects a boolean response.
     * @param question The question to be displayed to the user.
     * @return The boolean value entered by the user.
     */
    public boolean getBoolean(String question) {
        console.print(question);
        console.flush();
        return TextIO.getlnBoolean();
    }

    /**
     * Prompts the user with a question and expects a string response.
     * @param question The question to be displayed to the user.
     * @return The string entered by the user.
     */
    public String getString(String question) {
        console.print(question);
        console.flush();
        return TextIO.getlnString();
    }

    /**
     * Prompts the user with a question and expects an integer response.
     * @param s The question to be displayed to the user.
     * @return The integer entered by the user.
     */
    public int getInt(String s) {
        console.print(s);
        console.flush();
        return TextIO.getlnInt();
    }
}

