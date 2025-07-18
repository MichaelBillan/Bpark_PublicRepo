package bparkClient;

/**
 * Entry point for launching the BPARK client application.
 * Delegates execution to the {@link ClientUI} class.
 */
public class ClientLauncher {

    /**
     * The main method that starts the client application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        ClientUI.main(args);
    }
}
