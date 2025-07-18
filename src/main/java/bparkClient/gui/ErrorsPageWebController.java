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
 * Controller for ErrorsPageWeb.fxml.
 * Handles navigation from the error page back to the subscription code entry screen in the web interface.
 */
public class ErrorsPageWebController {

    @FXML
    private Button btnBack;

    /**
     * Handles navigation back to the subscription code entry screen.
     * @param event the action event triggered by clicking the back button
     */
    @FXML
    void handleBack(ActionEvent event) {
        loadScene("/bparkClient/fxml/EntersubscriptioncodeWeb.fxml", btnBack);
    }

    /**
     * Loads a new scene from the given FXML path using the provided button as the source.
     * @param fxmlPath the path to the FXML file
     * @param sourceButton the button that triggered the navigation
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
