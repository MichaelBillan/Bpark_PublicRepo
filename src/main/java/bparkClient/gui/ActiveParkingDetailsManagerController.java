package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.List;
import common.entities.ParkingSession;
import bparkClient.logic.UsherController;

import java.io.IOException;

/**
 * Controller for ActiveParkingDetails.fxml.
 * Handles active parking details.
 */
public class ActiveParkingDetailsManagerController {

    @FXML
    private Button backBtn;

    @FXML
    private Button homeBtn;

    /**
     * Table displaying active parking sessions.
     */
    @FXML
    private TableView<ParkingSession> activeParkingTable;

    /**
     * Column displaying the parking code.
     */
    @FXML
    private TableColumn<ParkingSession, String> parkingCodeCol;

    /**
     * Column displaying the subscriber ID.
     */
    @FXML
    private TableColumn<ParkingSession, String> subscriberIdCol;

    /**
     * Column displaying the parking start time.
     */
    @FXML
    private TableColumn<ParkingSession, String> startTimeCol;

    /**
     * Column displaying the parking duration.
     */
    @FXML
    private TableColumn<ParkingSession, Integer> durationCol;

    /**
     * Column indicating whether the session was extended.
     */
    @FXML
    private TableColumn<ParkingSession, String> extendedCol;

    /**
     * Initializes the table with active parking session data.
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
            activeParkingTable.getItems().setAll(sessions);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        loadScene("/bparkClient/fxml/WelcomeParkingLotManager.fxml", backBtn);
    }

    @FXML
    private void handleHome(ActionEvent event) {
        loadScene("/bparkClient/fxml/AccessSelection.fxml", homeBtn);
    }

    /**
     * Loads a new scene from the given FXML path using the provided button as the source.
     * @param fxmlPath the path to the FXML file
     * @param sourceButton the button that triggered the scene change
     */
    private void loadScene(String fxmlPath, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
