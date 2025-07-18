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
 * Controller for the failedPlaceATag screen that includes
 * background image and navigation buttons.
 */
public class FailedPlaceATagController {

    @FXML
    private Button backButton;

    @FXML
    private Button homeButton;

    /**
     * Handle the Back button click - goes to the previous screen.
     * @param event the action event triggered by the user
     */
    @FXML
    private void handleBack(ActionEvent event) {
        loadScene("/bparkClient/fxml/VerifySubscriptionOptions.fxml", backButton);
    }

    /**
     * Handle the Home button click - goes to the main/home screen.
     * @param event the action event triggered by the user
     */
    @FXML
    private void handleHome(ActionEvent event) {
        loadScene("/bparkClient/fxml/accessSelection.fxml", homeButton);
    }

    /**
     * Utility method to load a new scene from FXML.
     *
     * @param fxmlPath The path to the FXML file.
     * @param sourceButton A button from the current scene to get the Stage.
     */
    private void loadScene(String fxmlPath, Button sourceButton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace(); // אפשר להחליף בלוגר או התראה למשתמש
        }
    }
}
