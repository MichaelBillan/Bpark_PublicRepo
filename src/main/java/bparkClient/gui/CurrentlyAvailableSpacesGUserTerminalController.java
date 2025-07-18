package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import bparkClient.logic.Bparkclient;
import bparkClient.logic.ParkingSpaceController;
import common.RequestType;

/**
 * Controller for CurrentlyAvailableSpacesGUserTerminal.fxml.
 * Displays the number of currently available parking spaces for terminal users.
 */
public class CurrentlyAvailableSpacesGUserTerminalController {

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
     * Initializes the label with the number of available parking spaces.
     */
    @FXML
    public void initialize() {
        ParkingSpaceController parkingSpaceController = ParkingSpaceController.getInstance();
        currentlyavailablespaceslabel.setText(parkingSpaceController.checkAvailableSpots());
    }

    /**
     * Handles navigation back to the role selection terminal screen.
     */
    @FXML
    private void handlebackroleselectionterminal() {
        loadScene("/bparkClient/fxml/RoleSelectionTerminal.fxml");
    }

    /**
     * Handles navigation to the terminal home screen.
     */
    @FXML
    private void handlehomewelcometerminal() {
        loadScene("/bparkClient/fxml/RoleSelectionTerminal.fxml");
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
