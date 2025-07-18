package bparkClient.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for RoleSelectionTerminal.fxml.
 * Handles role selection for terminal users and navigation to respective screens.
 */
public class RoleSelectionTerminalController {

    @FXML
    private Button checkparkingavailabilitybtn;

    @FXML
    private Button subscriberterminalbtn;

    @FXML
    private Button backAccessSelectionBtn;

    @FXML
    private Button homeAccessSelectionBtn;

    /**
     * Navigates to the screen showing current parking availability.
     * @param event the action event triggered by the button
     */
    @FXML
    private void handlecheckparkingavailabilitybtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/CurrentlyAvailableSpacesGUserTerminal.fxml", checkparkingavailabilitybtn);
    }

    /**
     * Navigates to the screen for verifying subscription via the terminal.
     * @param event the action event triggered by the button
     */
    @FXML
    private void handlesubscriberterminalbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/VerifySubscriptionOptions.fxml", subscriberterminalbtn);
    }

    /**
     * Handles navigation back to the access selection screen.
     * @param event the action event triggered by the button
     */
    @FXML
    private void handlebackAccessSelectionBtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/accessSelection.fxml", backAccessSelectionBtn);
    }

    /**
     * Handles navigation to the access selection screen from the home button.
     * @param event the action event triggered by the button
     */
    @FXML
    private void handlehomeAccessSelectionBtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/accessSelection.fxml", homeAccessSelectionBtn);
    }

    /**
     * Loads a new scene from the given FXML path using the provided button as reference.
     * @param fxmlPath the path to the FXML file to load
     * @param sourceButton the button that triggered the scene change
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
