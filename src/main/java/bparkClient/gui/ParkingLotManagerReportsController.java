package bparkClient.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.List;

/**
 * Controller for ParkingLotManagerReportsPage.fxml.
 * Handles navigation between different report views available to the parking lot manager.
 */
public class ParkingLotManagerReportsController {

    @FXML private Button backBtn;
    @FXML private Button homeBtn;

    /**
     * Handles navigation to the parking times graphical report screen.
     * @param event the action event
     */
    @FXML
    private void handleParkingTimesReport(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/ReportGraphsPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Graphical Report View");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load graphical report view.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        loadScene("/bparkClient/fxml/WelcomeParkingLotManager.fxml", event);
    }

    @FXML
    private void handleHome(ActionEvent event) {
        loadScene("/bparkClient/fxml/accessSelection.fxml", event);
    }

    /**
     * Handles navigation to the top users report screen.
     * @param event the action event
     */
    @FXML
    private void handleTopUsersReport(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/TopUsersReport.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Top Users Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load Top Users Report view.");
        }
    }


    @FXML
    private void handleLateParkingReportBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/LateParkingReportPage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Late Parking Report");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
