package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

import java.io.IOException;

/**
 * Controller for ShowSubscriberDetailsPage.fxml.
 * Handles navigation from the subscriber details page for the usher.
 */
public class ShowSubscriberDetailsPageController {

    @FXML
    private Button backtowelcometerminal;

    @FXML
    private Button homewelcometerminal;

    /**
     * Navigates back to the usher main page when the back button is pressed.
     * @param event the action event
     */
    @FXML
    private void handleBackToWelcome(ActionEvent event) {
        loadScene("/bparkClient/fxml/UsherMainPage.fxml", backtowelcometerminal);
    }

    /**
     * Navigates to the parking lot manager's welcome screen when the home button is pressed.
     * @param event the action event
     */
    @FXML
    private void handleHomeWelcome(ActionEvent event) {
        loadScene("/bparkClient/fxml/WelcomeParkingLotManager.fxml", homewelcometerminal);
    }

    /**
     * Loads a new scene from the specified FXML file.
     * @param fxmlPath the path to the FXML file
     * @param sourceButton the button triggering the scene change
     */
    private void loadScene(String fxmlPath, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
