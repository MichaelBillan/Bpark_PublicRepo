package bparkServer.gui;

import java.io.IOException;
import java.net.InetAddress;

import com.mysql.cj.xdevapi.Client;

import bparkServer.logic.Bparkserver;
import bparkServer.logic.ClientInfo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ocsf.server.ConnectionToClient;

/**
 * Controller class for the server's graphical user interface (GUI).
 * Handles server initialization, client connection tracking, database schema loading, and user alerts.
 */
public class ServerGUIController {

    @FXML private TextField portxt;
    @FXML private TableView<ClientInfo> tblClients;
    @FXML private TableColumn<ClientInfo, String> clmHN;
    @FXML private TableColumn<ClientInfo, String> clmIP;
    @FXML private TableColumn<ClientInfo, String> clmCs;
    @FXML private Button btnDone;
    @FXML private Label statusLabel;
    @FXML private Button btnLoadSchema;

    private Bparkserver server;
    private final ObservableList<ClientInfo> clientList = FXCollections.observableArrayList();

    /**
     * Initializes the client table columns.
     * This method is automatically called when the FXML is loaded.
     */
    @FXML
    public void initialize() {
        clmHN.setCellValueFactory(new PropertyValueFactory<>("hostName"));
        clmIP.setCellValueFactory(new PropertyValueFactory<>("ip"));
        clmCs.setCellValueFactory(new PropertyValueFactory<>("status"));
        tblClients.setItems(clientList);
    }

    /**
     * Starts the server on the port entered by the user.
     * Sets up callbacks to handle client connections and disconnections.
     */
    @FXML
    public void Done() {
        int port;
        try {
            port = Integer.parseInt(portxt.getText());
            if (port < 0 || port > 65535) {
                throw new IllegalArgumentException("Port value out of range");
            }
        } catch (IllegalArgumentException e) {
            showAlert("Invalid Port", "Please enter a valid port number (0-65535).");
            return;
        }

        server = new Bparkserver(port);

        server.setClientConnectedCallback(client -> {
            Platform.runLater(() -> {
                try {
                    InetAddress address = client.getInetAddress();
                    ClientInfo info = new ClientInfo(address.getHostName(), address.getHostAddress(), "Connected");
                    client.setInfo("clientInfo", info);
                    clientList.add(info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        server.setClientDisconnectedCallback(client -> {
            ClientInfo info = (ClientInfo) client.getInfo("clientInfo");
            if (info != null) {
                Platform.runLater(() -> info.setStatus("Disconnected"));
            }
        });

        try {
            server.listen();
            portxt.clear();
            statusLabel.setText("Server Status: Listening on port " + port);
            showAlert("Server Started", "Server is listening on port " + port);
        } catch (Exception e) {
            showAlert("Server Error", "Could not start server: " + e.getMessage());
        }
    }

    /**
     * Displays an informational alert popup.
     *
     * @param title the title of the alert
     * @param message the message to display in the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Stops the server and exits the JavaFX application.
     *
     * @throws IOException if an error occurs while stopping the server
     */
    @FXML
    public void exit() throws IOException {
        if (server != null) {
            server.stopListening();
            showAlert("Server Stopped", "Server has stopped successfully.");
        }
        Platform.exit();
    }

    /**
     * Checks the database connection status when the user clicks the corresponding button.
     * Displays an alert with the result.
     */
    @FXML
    public void handleServerButtonClick() {
        boolean dbStatus = bparkServer.db.DataBaseController.testConnection();
        if (dbStatus) {
            showAlert("Database Status", "Successfully connected to the database.");
        } else {
            showAlert("Database Status", "Failed to connect to the database.\nCheck your connection settings.");
        }
    }

    /**
     * Handles the loading of predefined database schemas based on user selection.
     * Opens a choice dialog and loads the selected schema into the database.
     */
    @FXML
    public void handleLoadSchema() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("DB1", "DB1", "DB2", "Clear DB");
        dialog.setTitle("Load Database Schema");
        dialog.setHeaderText("Select a schema to load into the database:");
        dialog.setContentText("Schema:");
        dialog.getDialogPane().setMinWidth(400);
        dialog.getDialogPane().setMinHeight(200);
        dialog.setResizable(true);
        dialog.initOwner(btnLoadSchema.getScene().getWindow());
        Label contentLabel = (Label) dialog.getDialogPane().lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setWrapText(true);
            contentLabel.setMinWidth(350);
        }
        dialog.showAndWait().ifPresent(selected -> {
            int schemaId;
            switch (selected) {
                case "DB1":
                    schemaId = bparkServer.db.DataBaseController.SCHEMA_DB1;
                    break;
                case "DB2":
                    schemaId = bparkServer.db.DataBaseController.SCHEMA_DB2;
                    break;
                case "Clear DB":
                    schemaId = bparkServer.db.DataBaseController.SCHEMA_CLEAR_DB;
                    break;
                default:
                    showAlert("Error", "Unknown schema option selected.");
                    return;
            }
            bparkServer.db.DataBaseController.loadSchema(schemaId);
            showAlert("Schema Loaded", "Schema '" + selected + "' has been loaded into the database.");
        });
    }
}
