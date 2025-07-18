package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for NoParkingAvailable.fxml.
 * Displays a message when no parking is available and handles navigation.
 */
public class NoParkingAvailableController {

    @FXML
    private Button backdropofforpickupterminal;

    @FXML
    private Button homewelcometerminal;

    /**
     * Handles navigation back to the drop-off or pick-up terminal screen.
     */
    @FXML
    private void handlebackdropofforpickupterminal() {
        loadScene("/bparkClient/fxml/DropOffOrPickUpTerminal.fxml");
    }

    /**
     * Handles navigation to the terminal home screen.
     */
    @FXML
    private void handlehomewelcometerminal() {
        loadScene("/bparkClient/fxml/accessSelection.fxml");
    }

    /**
     * Loads a new scene from the given FXML path.
     * 
     * @param fxmlPath the FXML file path
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) backdropofforpickupterminal.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
}
