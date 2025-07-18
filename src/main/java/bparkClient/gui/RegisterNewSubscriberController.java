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
 * Controller for the PleasePlaceYourCarOnConveyorBelt.fxml.
 * Displays the pickup time and handles navigation for terminal users dropping off a vehicle.
 */
public class RegisterNewSubscriberController {

    @FXML
    private Button backtowelcometerminal;

    @FXML
    private Button homewelcometerminal;

    /**
     * Handles navigation back to the Usher main page.
     *
     * @param event the action event triggered by the back button
     */
    @FXML
    private void handleBackToWelcome(ActionEvent event) {
        loadScene("/bparkClient/fxml/UsherMainPage.fxml", backtowelcometerminal);
    }

    /**
     * Handles navigation to the terminal home (access selection) screen.
     *
     * @param event the action event triggered by the home button
     */
    @FXML
    private void handleHomeWelcome(ActionEvent event) {
        loadScene("/bparkClient/fxml/accessSelection.fxml", homewelcometerminal);
    }

    /**
     * Loads a new scene from the specified FXML path.
     *
     * @param fxmlPath the path to the FXML file
     * @param sourceButton the button used to retrieve the current stage
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
