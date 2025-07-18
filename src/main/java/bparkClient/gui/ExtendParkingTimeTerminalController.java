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
import common.entities.ParkingSession;


/**
 * Controller for ExtendParkingTimeTerminal.fxml.
 * Handles the extension of parking time for terminal users.
 */
public class ExtendParkingTimeTerminalController {

    @FXML
    private ComboBox<Integer> newExitTimeComboBox; 

    @FXML
    private Button extendtimebtn, cancelextendbtn, backparkinginfo, homewelcometerminal;

    @FXML
    private Label errorLabel;
    
    @FXML 
    private Label parkingCodeLabel;
    
    private String parkingCode;

    /**
     * Sets the parking code used to identify the session to be extended.
     * @param code the parking session code
     */
    public void setParkingCode(String code) {
        this.parkingCode = code;
        System.out.println(">> using parkingCode: " + parkingCode);
    }

    /**
     * Initialises the combo box with available extension hours.
     */
    @FXML
    private void initialize() {
        newExitTimeComboBox.getItems().addAll(1, 2, 3, 4);
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
            	try {
            	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/PleasePlaceYourCarOnConveyorBelt.fxml"));
            	    Parent root = loader.load();

            	    PlaceCarOnConveyController controller = loader.getController();
            	    controller.setParkingCode(parkingCode);

            	    Stage stage = (Stage) newExitTimeComboBox.getScene().getWindow();
            	    stage.setScene(new Scene(root));
            	    stage.show();
            	} catch (IOException e) {
            	    e.printStackTrace();
            	}
 
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
     * Handles cancellation of the parking time extension.
     */
    @FXML
    private void handlecancelextendbtn() {
        loadScene("/bparkClient/fxml/ParkingInfo.fxml");
    }

    /**
     * Handles navigation back to the parking info screen.
     */
    @FXML
    private void handlebackparkinginfo() {
        loadScene("/bparkClient/fxml/ParkingInfo.fxml");
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
     * Displays an alert dialog with the given type, title, and message.
     * @param type the type of the alert
     * @param title the title of the alert
     * @param message the message to display
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
