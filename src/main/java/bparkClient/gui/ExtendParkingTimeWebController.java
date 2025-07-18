package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

import bparkClient.logic.AppSessionData;
import bparkClient.logic.ParkingSessionController;

/**
 * Controller for ExtendParkingTimeWeb.fxml.
 * Handles the extension of parking time for web users.
 */
public class ExtendParkingTimeWebController {

    @FXML
    private ComboBox<Integer> newExitTimeComboBox; 

    @FXML
    private Button extendtimebtn, backEnterparkingcode, homewelcomeWeb;

    @FXML
    private Label errorLabel;
    
    private String parkingCode;

    /**
     * Sets the parking code used for the extension operation.
     * @param code the parking session code
     */
    public void setParkingCode(String code) {
        this.parkingCode = code;
        System.out.println(">> parkingCode set to: " + code);
    }

    /**
     * Handles the action to extend parking time based on user selection.
     */
    @FXML
    private void handleextendtimebtn() {
        Integer selectedHours = newExitTimeComboBox.getValue();

        if (selectedHours == null) {
            errorLabel.setText("Please select an hour.");
            return;
        }
        if (selectedHours >= 1 && selectedHours <= 4) {
        	int option = ParkingSessionController.getInstance().extendSession(parkingCode, selectedHours * 60);
            errorLabel.setText("");
            switch (option) {
            case 1:
            	showAlert(AlertType.WARNING, "Extend Session Failed", "There is no such parking session");
                break;
            case 2:
            	showAlert(AlertType.WARNING, "Extend Session Failed", "Extension not allowed 15 minutes before session end.");
                break;
            case 3:
            	showAlert(AlertType.WARNING, "Extend Session Failed", "Max 3 extensions allowed.");
                break;
            case 4:
            	showAlert(AlertType.WARNING, "Extend Session Failed", "Cannot extend session past 21:00.");
                break;
            case 5:
            	showAlert(AlertType.WARNING, "Extend Session Failed", "Conflicting reservation exists.");
                break;
            case 6:
            	loadScene("/bparkClient/fxml/pickupWeb.fxml"); 
                break;
            default:
                System.out.println("System Error");
                break;
        }
            
        } else {
        	showAlert(AlertType.WARNING, "Missing Fields", "Please fill all the fields.");
        }
    }

    /**
     * Handles navigation back to the enter parking code web screen.
     */
    @FXML
    private void handlebackEnterparkingcode() {
        loadScene("/bparkClient/fxml/EnterParkingCodeWeb.fxml");
    }

    /**
     * Initializes the combo box with extension hour options.
     */
    @FXML
    private void initialize() {
        newExitTimeComboBox.getItems().addAll(1, 2, 3, 4);
    }

    /**
     * Handles navigation to the home screen.
     */
    @FXML
    private void handlehomewelcomeWeb() {
        loadScene("/bparkClient/fxml/AccessSelection.fxml");
    }

    /**
     * Loads a new scene from the given FXML path.
     * @param fxmlPath the FXML file path
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) newExitTimeComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog.
     * @param type the alert type
     * @param title the alert title
     * @param message the alert message
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
