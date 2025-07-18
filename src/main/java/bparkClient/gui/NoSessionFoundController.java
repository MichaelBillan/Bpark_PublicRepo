package bparkClient.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for NoSessionFound.fxml.
 * Handles user actions when no active parking session is found.
 */
public class NoSessionFoundController {

    @FXML
    private Button btnBack;

    /**
     * Handle the "Back" button click.
     * Navigates the user back to the main terminal menu.
     *
     * @param event the ActionEvent triggered by the button click
     */
    @FXML
    private void handleBack(ActionEvent event) {
        loadScene("/bparkClient/fxml/EnterConfirmationCode.fxml", btnBack);
    }

    /**
     * Loads the specified FXML scene using the current button's stage.
     *
     * @param fxmlPath the path to the FXML file to load
     * @param sourceButton the button that triggered the navigation, used to obtain the stage
     */
    private void loadScene(String fxmlPath, Button sourceButton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
