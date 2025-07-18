package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.io.IOException;

import bparkClient.logic.AppSessionData;
import bparkClient.logic.Bparkclient;
import bparkClient.logic.ParkingSessionController;
import common.RequestType;
import common.entities.ParkingSession;

/**
 * Controller for ParkingInfo.fxml.
 * Displays parking code and session information for the user.
 */
public class ParkingInfoController {

    /** Label displaying the parking code to the user. */
    @FXML
    private Label parkingCodeLabel;

    /** Link for navigating to the extend parking time screen. */
    @FXML
    private Hyperlink doyouwanttoextendlink;


    /** Overlay displayed during background loading operations. */
    @FXML
    private VBox loadingOverlay;

    /** Main content container to which a blur effect is applied during loading. */
    @FXML
    private VBox mainContent;

    /** The current parking session associated with the subscriber. */
    private ParkingSession session;

    /**
     * Initializes the parking code label and creates a new parking session.
     * This method runs after the FXML is loaded.

     */
    @FXML
    public void initialize() {
        LoadingHelper.runWithLoading(
            loadingOverlay,
            mainContent,
            () -> {
                // Background task: create new session
                session = ParkingSessionController.getInstance().createSession(
                    AppSessionData.getInstance().getSubscriberCode()
                );
            },
            () -> {
                // UI thread: update parking code label
                parkingCodeLabel.setText(session.getParkingCode());
            }
        );
    }

    /**

     * Handles navigation to the screen for extending parking time.
     * Passes the current parking code to the next screen's controller.

     */
    @FXML
    private void handledoyouwanttoextendlink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/ExtendParkingTimeTerminal.fxml"));
            Parent root = loader.load();

            ExtendParkingTimeTerminalController controller = loader.getController();
            controller.setParkingCode(parkingCodeLabel.getText());

            Stage stage = (Stage) parkingCodeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles navigation back to the drop-off or pick-up terminal screen.
     */
    @FXML
    private void handlebackdropofforpickupterminal() {
        loadScene("/bparkClient/fxml/DropOffOrPickUpTerminal.fxml");
    }

    /**
     * Handles navigation to the terminal home screen.
     */
    @FXML
    private void handlehomewelcometerminal() {
        loadScene("/bparkClient/fxml/RoleSelectionTerminal.fxml");
    }

    /**

     * Loads and displays a new scene from the specified FXML path.
     *
     * @param fxmlPath the path to the FXML file to load

     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) parkingCodeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
