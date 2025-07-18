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
 * Controller for ErrorsPage.fxml (alternate or additional error page).
 * Handles navigation after an error event occurs.
 */
public class ErrorsPageController {

    /**
     * Loads a new scene from the given FXML path using the provided button as the source.
     *
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
