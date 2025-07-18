package bparkClient.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import bparkClient.logic.Bparkclient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller for ServerIP.fxml.
 * Handles server IP and port input and connection logic.
 */
public class ServerIpController {

    @FXML
    private TextField txtIP;

    @FXML
    private TextField txtPort;

    /**
     * Handles the action to get IP and port, connect to the server, and navigate to access selection.
     * @param event the action event
     */
    @FXML
    void getIP(ActionEvent event) {
        String ip = txtIP.getText().trim();
        String portText = txtPort.getText().trim();
        int port;

        try {
            port = Integer.parseInt(portText);
        } catch (NumberFormatException e) {
            new Alert(AlertType.ERROR, "Invalid port number. Please enter a numeric value.").showAndWait();
            return;
        }

        try {
            new Bparkclient(ip, port);
            //Bparkclient.getInstance().listenForResponses();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/accessSelection.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtIP.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Access Selection");
            stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Failed to connect: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
