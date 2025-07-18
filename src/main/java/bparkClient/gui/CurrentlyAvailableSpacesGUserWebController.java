package bparkClient.gui;

import bparkClient.logic.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for CurrentlyAvailableSpacesGUserWeb.fxml.
 * Displays the number of currently available parking spaces for web users.
 */
public class CurrentlyAvailableSpacesGUserWebController {

    /**
     * Label displaying the number of available parking spaces.
     */
    @FXML
    private Label currentlyavailablespaceslabel;

    @FXML
    private Button backroleselectionterminal;

    @FXML
    private Button homewelcometerminal;

    /**
     * Initialises the label with the number of available parking spaces.
     */
    @FXML
    public void initialize() {
        ParkingSpaceController parkingSpaceController = ParkingSpaceController.getInstance();
        currentlyavailablespaceslabel.setText(parkingSpaceController.checkAvailableSpots());
    }

    /**
     * Handles navigation back to the role selection web screen.
     */
    @FXML
    private void handlebackroleselectionterminal() {
        loadScene("/bparkClient/fxml/RoleSelectionWeb.fxml");
    }

    /**
     * Handles navigation to the web home screen.
     */
    @FXML
    private void handlehomewelcometerminal() {
        loadScene("/bparkClient/fxml/RoleSelectionWeb.fxml");
    }

    /**
     * Loads a new scene from the given FXML file.
     * @param fxmlFile the FXML file path
     */
    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) currentlyavailablespaceslabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
