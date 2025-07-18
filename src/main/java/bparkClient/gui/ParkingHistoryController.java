package bparkClient.gui;

import bparkClient.logic.AppSessionData;
import bparkClient.logic.SubscriberController;
import common.entities.ParkingSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for ParkingHistory.fxml.
 * Displays the parking session history for a subscriber.
 */
public class ParkingHistoryController {

    @FXML
    private TableView<ParkingSession> parkingHistoryTable;

    @FXML
    private TableColumn<ParkingSession, String> spaceIdCol;

    @FXML
    private TableColumn<ParkingSession, String> dateCol;

    @FXML
    private TableColumn<ParkingSession, String> startTimeCol;

    @FXML
    private TableColumn<ParkingSession, Integer> durationCol;

    @FXML
    private TableColumn<ParkingSession, String> codeCol;

    @FXML
    private Button backsubscriberdetailsbtn;

    @FXML
    private Button homewelcomeweb;

    /**
     * Initialises the parking history table with data.
     * Binds table columns to ParkingSession properties and loads the session list.
     */
    @FXML
    public void initialize() {
        // spaceId column
        spaceIdCol.setCellValueFactory(new PropertyValueFactory<>("spaceId"));

        // Date column from startTime
        dateCol.setCellValueFactory(cellData -> {
            LocalDateTime start = cellData.getValue().getStartTime();
            String dateStr = (start != null) ? start.toLocalDate().toString() : "";
            return new SimpleStringProperty(dateStr);
        });

        // Time column from startTime
        startTimeCol.setCellValueFactory(cellData -> {
            LocalDateTime start = cellData.getValue().getStartTime();
            String timeStr = (start != null) ? start.toLocalTime().toString() : "";
            return new SimpleStringProperty(timeStr);
        });

        // Duration column
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));

        // Parking code column
        codeCol.setCellValueFactory(new PropertyValueFactory<>("parkingCode"));

        // Fetching the data
        String subId = AppSessionData.getInstance().getSubscriberCode();
        List<ParkingSession> sessions = SubscriberController.getInstance().generateParkingHistory(subId);

        // Inserting the data into the table
        parkingHistoryTable.getItems().setAll(sessions);
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
        loadScene("/bparkClient/fxml/AccessSelection.fxml");
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
