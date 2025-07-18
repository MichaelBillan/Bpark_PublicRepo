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
 * Controller for accessSelection.fxml.
 * Handles access selection between web and terminal interfaces.
 */
public class AccessSelectionController {

    /**
     * Button for accessing the system via the web interface.
     */
    @FXML
    private Button Accessviawebbutton;

    /**
     * Button for accessing the system via the terminal interface.
     */
    @FXML
    private Button Accessviaterminalbutton;

    /**
     * Handles navigation to the web role selection screen.
     * @param event the action event
     */
    @FXML
    private void handleAccessViaWeb(ActionEvent event) {
        loadScene("/bparkClient/fxml/RoleSelectionWeb.fxml", Accessviawebbutton);
    }

    /**
     * Handles navigation to the terminal role selection screen.
     * @param event the action event
     */
    @FXML
    private void handleAccessViaTerminal(ActionEvent event) {
        loadScene("/bparkClient/fxml/RoleSelectionTerminal.fxml", Accessviaterminalbutton);
    }

    /**
     * Loads a new scene from the given FXML path using the provided button as the source.
     * @param fxmlPath the FXML file path
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
