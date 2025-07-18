package bparkClient.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * Controller for ReservationCanceled.fxml.
 * Handles actions after reservation cancellation.
 */
public class ReservationCanceledController {

    @FXML
    private Button backToHomeBtn;

    @FXML
    private Button homeButton;

    /**
     * חוזר למסך הבית כאשר נלחץ כפתור Back.
     */
    @FXML
    private void handleBackToHome() {
        loadScene("/bparkClient/fxml/EnterConfirmationCode.fxml");
    }

    /**
     * חוזר למסך הבית כאשר נלחץ כפתור Home.
     */
    @FXML
    private void handleHome() {
        loadScene("/bparkClient/fxml/accessSelection.fxml");
    }

    /**
     * Utility method for loading a new scene.
     *
     * @param fxmlPath the path to the FXML file to load
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) backToHomeBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
