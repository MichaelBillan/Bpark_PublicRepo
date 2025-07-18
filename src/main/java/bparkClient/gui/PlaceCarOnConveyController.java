package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bparkClient.logic.ParkingSessionController;

/**
 * Controller for the PleasePlaceYourCarOnConveyorBelt.fxml.
 * Displays the pickup time and handles navigation for terminal users dropping off a vehicle.
 */
public class PlaceCarOnConveyController {

    @FXML
    private Label lblPickupTime;

    @FXML
    private Button backExtandParkingTimebtn;

    @FXML
    private Button homewelcometerminal;
    
    private String parkingCode;

    /**
     * Initializes the controller.
     * Currently does nothing on load.
     */
    @FXML
    public void initialize() {

    }

    /**
     * Sets the parking code and updates the label with the calculated pickup time.
     *
     * @param code the parking session code to use for retrieving pickup time
     */
    public void setParkingCode(String code) {
        this.parkingCode = code;
        System.out.println(">> parkingCode set in PlaceCarOnConveyController: " + code);

        LocalDateTime pickupTime = ParkingSessionController.getInstance().currentPickupTime(parkingCode);
        if (pickupTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            lblPickupTime.setText(pickupTime.format(formatter));
        } else {
            lblPickupTime.setText("Unavailable");
            System.err.println(">> pickupTime is null for parkingCode: " + code);
        }
    }

    /**
     * Handles the back button action.
     */
    @FXML
    private void handlebackExtandParkingTime() {
        loadScene("/bparkClient/fxml/ExtendParkingTimeTerminal.fxml", backExtandParkingTimebtn);
    }

    /**
     * Handles the home button action.
     */
    @FXML
    private void handlehomewelcometerminal() {
        loadScene("/bparkClient/fxml/accessSelection.fxml", homewelcometerminal);
    }

    /**
     * Loads a new scene based on given FXML path.
     *
     * @param fxmlPath the path to the FXML file
     * @param sourceButton the button triggering the navigation, used to get the stage
     */
    private void loadScene(String fxmlPath, Button sourceButton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace(); // או הצגת שגיאה גרפית
        }
    }
}
