package bparkClient.gui;

import java.io.IOException;
import java.util.List;

import bparkClient.logic.UsherController;
import common.entities.ParkingSession;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller for WelcomeParkingLotManager.fxml.
 * Manages navigation between parking lot manager views, including reports, subscriber details, and active sessions.
 */
public class ParkingLotManagerGUIController {

    @FXML private Button backBtn;
    @FXML private Button homeBtn;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        // No table population logic here anymore.
    }

    /**
     * Handles navigation to the reports page.
     * @param event the action event
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void handleViewReports(ActionEvent event) throws IOException {
        loadScene("/bparkClient/fxml/ParkingLotManagerReportsPage.fxml", event);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
    	loadScene("/bparkClient/fxml/EmployeeVerify.fxml", event);
    }

    @FXML
    private void handleHome(ActionEvent event) throws IOException {
    	loadScene("/bparkClient/fxml/accessSelection.fxml", event);
    }

    /**
     * Handles navigation to the subscriber details page.
     * @param event the action event
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void handleViewSubscriberDetails(ActionEvent event) throws IOException {
    	loadScene("/bparkClient/fxml/SubscriberDetails.fxml", event);
    }

    /**
     * Handles navigation to the active parking session details page.
     * @param event the action event
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void handleViewActiveParkingDetails(ActionEvent event) throws IOException {
    	loadScene("/bparkClient/fxml/ActiveParkingDetails.fxml", event);
    }

    /**
     * Handles navigation to the all subscribers page.
     * @param event the action event
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void handleShowAllSubscribers(ActionEvent event) throws IOException {
        loadScene("/bparkClient/fxml/AllSubscribersManager.fxml", event);
    }

    /**
     * Loads a new scene from the given FXML path using the triggering event.
     * @param fxmlPath the path to the FXML file
     * @param event the event that triggered the scene change
     */
    private void loadScene(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load screen: " + fxmlPath);
        }
    }

    /**
     * Displays an alert dialog with the specified message.
     * @param message the error message to show
     */
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
