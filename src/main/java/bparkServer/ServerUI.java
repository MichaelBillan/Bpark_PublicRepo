package bparkServer;

import bparkServer.notifications.NotificationController;
import bparkServer.scheduler.SchedulerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

/**
 * The main JavaFX application class for the Bpark server UI.
 * Responsible for launching the server GUI and starting the scheduler.
 */
public class ServerUI extends Application {

    /**
     * Called when the JavaFX application is started.
     * Loads the FXML layout and initializes the monthly report scheduler.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if FXML loading fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkServer/fxml/ServerGUI.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Bpark Server");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // Start the monthly report scheduler
        NotificationController notificationController = new NotificationController();
        SchedulerController scheduler = new SchedulerController(notificationController);
        scheduler.startMonthlyReportScheduler();
    }

    /**
     * The main entry point for the server application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
