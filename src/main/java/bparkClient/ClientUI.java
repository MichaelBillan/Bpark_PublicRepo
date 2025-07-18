package bparkClient;

import bparkClient.logic.Bparkclient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point for the BPARK JavaFX client application.
 * Handles launching the UI and managing server connection lifecycle.
 */
public class ClientUI extends Application {

    /** The host address of the server. */
    private static final String SERVER_HOST = "localhost";

    /** The port number of the server. */
    private static final int SERVER_PORT = 5555;

    /**
     * Called when the JavaFX application starts.
     * Loads the initial FXML screen (ServerIP.fxml) and sets up the stage.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/ServerIP.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Connect to Server");
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(event -> {
                if (Bparkclient.getInstance() != null) {
                    Bparkclient.getInstance().close();
                }
                System.out.println("Client UI closed (window X)");
            });
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the JavaFX application is stopping.
     * Closes the connection to the server and performs cleanup.
     */
    @Override
    public void stop() {
        Bparkclient.getInstance().close();
        System.out.println("Client UI closed");
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args); 
    }
}
