package bparkClient.gui;

import bparkClient.logic.Bparkclient;
import common.Request;
import common.RequestType;
import common.entities.Subscriber;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Controller for AllSubscribersManager.fxml.
 * Displays all subscribers in a read-only table.
 */
public class AllSubscribersManegerController {

    /**
     * Table displaying all subscribers.
     */
    @FXML private TableView<Subscriber> subscribersTable;

    /**
     * Column displaying the subscriber ID.
     */
    @FXML private TableColumn<Subscriber, String> idCol;

    /**
     * Column displaying the subscriber name.
     */
    @FXML private TableColumn<Subscriber, String> nameCol;

    /**
     * Column displaying the subscriber email.
     */
    @FXML private TableColumn<Subscriber, String> emailCol;

    /**
     * Column displaying the subscriber phone number.
     */
    @FXML private TableColumn<Subscriber, String> phoneCol;

    @FXML private Button backBtn;
    @FXML private Button homeBtn;

    /**
     * Initializes the table and loads all subscribers from the server.
     * Adds a double-click handler to view subscriber details.
     */
    @FXML
    public void initialize() {
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubId()));
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));
        fetchAndDisplaySubscribers();
        // Add double-click handler to table rows
        subscribersTable.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Subscriber> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Subscriber selected = row.getItem();
                    bparkClient.logic.AppSessionData.getInstance().setSubscriberCode(selected.getSubId());
                    bparkClient.logic.AppSessionData.getInstance().setDetailsOrigin("manager");
                    try {
                        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/bparkClient/fxml/SubscriberDetails.fxml"));
                        javafx.scene.Parent root = loader.load();
                        javafx.stage.Stage stage = (javafx.stage.Stage) subscribersTable.getScene().getWindow();
                        stage.setScene(new javafx.scene.Scene(root));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    /**
     * Fetches subscriber data from the server and populates the table.
     */
    private void fetchAndDisplaySubscribers() {
        // Send request to server to get all subscribers
        Object response = Bparkclient.getInstance().sendAndWait(RequestType.GET_ALL_SUBSCRIBERS, null);
        if (response instanceof java.util.List<?>) {
            java.util.List<?> list = (java.util.List<?>) response;
            if (!list.isEmpty() && list.get(0) instanceof common.entities.Subscriber) {
                javafx.collections.ObservableList<common.entities.Subscriber> data = javafx.collections.FXCollections.observableArrayList();
                for (Object obj : list) {
                    data.add((common.entities.Subscriber) obj);
                }
                subscribersTable.setItems(data);
            }
        }
    }

    /**
     * Handles navigation to the previous screen (Parking Lot Manager main page by default).
     */
    @FXML
    private void handleBack() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/bparkClient/fxml/WelcomeParkingLotManager.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) backBtn.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles navigation to the home (access selection) screen.
     */
    @FXML
    private void handleHome() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/bparkClient/fxml/AccessSelection.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) homeBtn.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
