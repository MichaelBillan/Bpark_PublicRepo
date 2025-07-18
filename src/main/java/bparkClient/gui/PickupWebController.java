package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * Controller for CarIsOnTheWay.fxml.
 * Displays a notification that the car is on the way and handles navigation.
 */
public class PickupWebController {

    @FXML
    private Button backEnterParkingCodebtn;

    @FXML
    private Button homewelcometerminal;

    /**
     * Initializes the controller and logs initialization.
     */
    @FXML
    public void initialize() {
        System.out.println("Car is on the way screen initialized.");
    }

    /**
     * Handles navigation back to the EnterParkingCodeWeb screen.
     * @param event the action event
     */
    @FXML
    void handlebackEnterParkingCode(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/EnterParkingCodeWeb.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("BPark - EnterParkingCodeWeb");
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load EnterParkingCodeWeb.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles navigation to the terminal home screen (Access Selection screen).
     * @param event the action event
     */
    @FXML
    void handlehomewelcometerminal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/AccessSelection.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("BPark - Access Selection");
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load AccessSelection.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
