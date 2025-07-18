package bparkClient.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for VerifySubscriptionOptions.fxml.
 * Handles navigation for different subscription verification options.
 */
public class VerifySubscriptionOptionsController {
	
    /**
     * Loads a new scene from the given FXML file using the provided event.
     * @param fxmlFile the FXML file path
     * @param event the action event
     */
    private void loadScene(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles navigation to the tag verification screen.
     * @param event the action event
     */
    @FXML
    private void handleverifyviatagbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/verifyViaTag.fxml", event); 
    }

    /**
     * Handles navigation to the code verification screen.
     * @param event the action event
     */
    @FXML
    private void handleverifyviacodebtn(ActionEvent event) {
    	loadScene("/bparkClient/fxml/EntersubscriptioncodeTerminal.fxml", event);
    }

    /**
     * Handles navigation back to the terminal welcome screen.
     * @param event the action event
     */
    @FXML
    private void handlebacktowelcometerminal(ActionEvent event) {
        loadScene("/bparkClient/fxml/RoleSelectionTerminal.fxml", event);
    }

    /**
     * Handles navigation to the terminal home screen.
     * @param event the action event
     */
    @FXML
    private void handlehomewelcometerminal(ActionEvent event) {
        loadScene("/bparkClient/fxml/accessSelection.fxml", event);
    }
}
