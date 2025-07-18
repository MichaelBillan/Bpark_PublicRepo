package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.io.IOException;

import bparkClient.logic.AppSessionData;
import bparkClient.logic.Bparkclient;
import common.RequestType;
import bparkClient.logic.ReservationController;
import bparkClient.logic.ParkingSpaceController;

/**
 * Controller for DropOffOrPickUpTerminal.fxml.
 * Handles user selection between dropping off or picking up a vehicle in the terminal interface.
 */
public class DropOffOrPickUpTerminalController {

    /**
     * Button to start the drop-off vehicle process.
     */
    @FXML
    private Button dropoffmyvehiclebtn;

    /**
     * Button to start the pick-up vehicle process.
     */
    @FXML
    private Button pickupmyvehiclebtn;

    @FXML
    private Button backverifysuboptions;

    @FXML
    private Button homewelcometerminal;

    /**
     * Label that displays a personalized welcome message.
     */
    @FXML
    private Label welcomeLabel;

    /**
     * Overlay shown while loading server responses.
     */
    @FXML
    private VBox loadingOverlay;

    /**
     * Handles logic for dropping off a vehicle:
     * checks if the subscriber has a reservation or available parking spaces,
     * then loads the appropriate screen accordingly.
     */
    @FXML
    private void handledropoffmyvehiclebtn() {
        loadingOverlay.setVisible(true);
        new Thread(() -> {
            String subCode = AppSessionData.getInstance().getSubscriberCode();
            boolean hasReservation = ReservationController.getInstance().hasActiveReservation(subCode);
            boolean hasAvailableSpots = ParkingSpaceController.getInstance().hasAvailableSpots();
            Platform.runLater(() -> {
                loadingOverlay.setVisible(false);
                if (hasReservation) {
                    loadScene("/bparkClient/fxml/EnterConfirmationCode.fxml");
                } else if (hasAvailableSpots) {
                    loadScene("/bparkClient/fxml/ParkingInfo.fxml");
                } else {
                    loadScene("/bparkClient/fxml/NoParkingAvailable.fxml");
                }
            });
        }).start();
    }

    /**
     * Handles navigation to the screen where the user enters a parking code to pick up their vehicle.
     */
    @FXML
    private void handlepickupmyvehiclebtn() {
        loadScene("/bparkClient/fxml/EnterParkingCodeTerminal.fxml"); 
    }

    @FXML
    private void handlebackverifysuboptions() {
        loadScene("/bparkClient/fxml/VerifySubscriptionOptions.fxml");
    }

    @FXML
    private void handlehomewelcometerminal() {
        loadScene("/bparkClient/fxml/accessSelection.fxml");
    }

    /**
     * Loads a new scene from the given FXML file.
     * @param fxmlPath the path to the FXML file
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) dropoffmyvehiclebtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the welcome label to include the subscriber's name.
     * @param subscriberName the name of the subscriber
     */
    public void setWelcomeText(String subscriberName) {
        if (welcomeLabel != null && subscriberName != null) {
            welcomeLabel.setText("Welcome " + subscriberName + "!");
        }
    }
    
    /**
     * Displays an alert window.
     * @param type the type of alert
     * @param title the title of the alert
     * @param message the message content of the alert
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
