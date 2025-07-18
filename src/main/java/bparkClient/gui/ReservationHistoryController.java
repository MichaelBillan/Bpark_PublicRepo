package bparkClient.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.List;

import bparkClient.logic.AppSessionData;
import bparkClient.logic.SubscriberController;
import common.entities.Reservation;

/**
 * Controller for ReservationHistory.fxml.
 * Displays the reservation history for a subscriber.
 */
public class ReservationHistoryController {

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private Button backsubscriberdetailsbtn;

    @FXML
    private Button homewelcomeweb;

    @FXML private TableColumn<Reservation, String> resIdCol;
    @FXML private TableColumn<Reservation, String> confirmationCodeCol;
    @FXML private TableColumn<Reservation, String> statusCol;
    @FXML private TableColumn<Reservation, String> startCol;
    @FXML private TableColumn<Reservation, String> endCol;
    @FXML private TableColumn<Reservation, String> spaceIdCol;

    /**
     * Initializes the reservation history table with data.
     * Binds table columns to Reservation object properties and populates the table.
     */
    @FXML
    public void initialize() {
        resIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getResId()));
        confirmationCodeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConfirmationCode()));
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReservationStatus().toString()));
        startCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStartTime().toString()));
        endCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndTime().toString()));
        spaceIdCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSpaceId()));
        String subId = AppSessionData.getInstance().getSubscriberCode();
        List<Reservation> reservations = SubscriberController.getInstance().generateReservationHistory(subId);
        reservationTable.getItems().addAll(reservations);
    }

    /**
     * Handles navigation back to the subscriber details page.
     */
    @FXML
    private void handlebacksubscriberdetailsbtn() {
        loadScene("/bparkClient/fxml/SubscriberDetails.fxml");
    }

    /**
     * Handles navigation to the home screen.
     */
    @FXML
    private void handlehomewelcomeweb() {
        loadScene("/bparkClient/fxml/RoleSelectionWeb.fxml");
    }

    /**
     * Loads a new scene from the given FXML path.
     *
     * @param fxmlPath the FXML file path
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) backsubscriberdetailsbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load screen: " + fxmlPath);
        }
    }

    /**
     * Shows an error alert with the given message.
     *
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
