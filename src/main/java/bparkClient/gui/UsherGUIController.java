package bparkClient.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

import bparkClient.logic.Bparkclient;
import bparkClient.logic.UsherController;
import common.RequestType;
import common.entities.ParkingSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for UsherMainPage.fxml.
 * Displays and manages active parking sessions for the usher role.
 */
public class UsherGUIController {

    @FXML private Button backBtn;
    @FXML private Button homeBtn;

    @FXML private TableView<ParkingSession> activeParkingTable;
    @FXML private TableColumn<ParkingSession, String> parkingCodeCol;
    @FXML private TableColumn<ParkingSession, String> subscriberIdCol;
    @FXML private TableColumn<ParkingSession, String> startTimeCol;
    @FXML private TableColumn<ParkingSession, Integer> durationCol;
    @FXML private TableColumn<ParkingSession, String> extendedCol;

    /**
     * Initialises the table with active parking sessions.
     */
    @FXML
    public void initialize() {
        if (activeParkingTable != null) { 
            parkingCodeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getParkingCode()));
            subscriberIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubId()));
            startTimeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStartTime().toString()));
            durationCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getDuration()).asObject());
            extendedCol.setCellValueFactory(data -> {
                int count = data.getValue().getExtensionCount();
                return new SimpleStringProperty(count > 0 ? "Yes" : "No");
            });

            List<ParkingSession> sessions = UsherController.getInstance().createActiveParkingList();
            activeParkingTable.getItems().addAll(sessions);
        }
    }

    /**
     * Handles navigation to the register new subscriber page.
     * @param event the action event
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void handleRegisterNewSubscriber(ActionEvent event) throws IOException {
        loadScene("/bparkClient/fxml/RegistPage.fxml", event);
    }

    /**
     * Handles navigation back to the access selection screen.
     * @param event the action event
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        loadScene("/bparkClient/fxml/RoleSelectionWeb.fxml", event);
    }

    /**
     * Handles navigation to the home screen.
     * @param event the action event
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void handlehomeBtn(ActionEvent event) throws IOException {
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
     * Handles navigation to the active parking details page.
     * @param event the action event
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void handleViewActiveParkingDetails(ActionEvent event) throws IOException {
        loadScene("/bparkClient/fxml/ActiveParkingDetailsManeger.fxml", event);
    }

    /**
     * Handles navigation to the all subscribers page.
     * @param event the action event
     * @throws IOException if FXML loading fails
     */
    @FXML
    private void handleShowAllSubscribers(ActionEvent event) throws IOException {
        loadScene("/bparkClient/fxml/AllSubscribers.fxml", event);
    }

    /**
     * Loads a new scene from the given FXML path.
     * @param fxmlPath the FXML file path
     * @param event the action event
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
     * Shows an error alert with the given message.
     * @param message the error message
     */
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
