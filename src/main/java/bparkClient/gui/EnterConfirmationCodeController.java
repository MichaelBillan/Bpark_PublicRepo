package bparkClient.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import bparkClient.logic.ParkingSessionController;

/**
 * Controller for EnterConfirmationCode.fxml.
 * Handles confirmation code entry and navigation for reservation verification.
 */
public class EnterConfirmationCodeController {


    /**
     * Text field where the user enters the reservation confirmation code.
     */
    @FXML
    private TextField confirmationCodeField;

    /**
     * Button that confirms the entered code.
     */
    @FXML
    private Button confirmconfirmationcodebtn;

    /** Button to navigate back to the drop-off or pick-up terminal screen. */
    @FXML
    private Button backdropofforpickupterminalbtn;

    /** Button to navigate to the terminal home screen. */
    @FXML
    private Button homewelcometerminal;


    /** Label for displaying error messages. */

    @FXML
    private Label errorLabel;

    /** Main content pane that may be blurred during loading. */
    @FXML
    private AnchorPane mainContent;

    /** Overlay shown during background loading operations. */
    @FXML
    private javafx.scene.layout.VBox loadingOverlay;

    /** Blur effect applied to the main content during loading. */
    private final javafx.scene.effect.GaussianBlur blurEffect = new GaussianBlur(10);

    /**
     * Handles the confirmation of the code entered by the user.
     *
     * @param event the action event triggered by clicking the confirm button
     */
    @FXML
    private void handleconfirmconfirmationcodebtn(ActionEvent event) {
        String enteredCode = confirmationCodeField.getText().trim();
        if (enteredCode == null || enteredCode.isEmpty()) {
            errorLabel.setText("Please enter a confirmation code.");
            return;
        }


        LoadingHelper.runWithLoading(mainContent, loadingOverlay, () -> {
            int result = bparkClient.logic.ReservationController.getInstance().isReservationValid(enteredCode);
            switch (result) {
                case 1 -> Platform.runLater(() -> loadScene("/bparkClient/fxml/NoParkingSessionFound.fxml"));
                case 2 -> Platform.runLater(() -> loadScene("/bparkClient/fxml/ReservationCancelled.fxml"));
                case 3 -> {
                    String parkingCode = ParkingSessionController.getInstance().createSessionFromReservation(enteredCode);
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/ReservationVerified.fxml"));
                            Parent root = loader.load();
                            ReservationVerifiedController controller = loader.getController();
                            controller.setParkingCode(parkingCode);
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                default -> Platform.runLater(() -> errorLabel.setText("Unknown error."));
            }
        });

    }

    /**
     * Handles navigation back to the drop-off or pick-up terminal screen.
     *
     * @param event the action event triggered by clicking the back button
     */
    @FXML
    private void handlebackdropofforpickupterminalbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/DropOffOrPickUpTerminal.fxml");
    }

    /**
     * Handles navigation to the terminal home screen.
     *
     * @param event the action event triggered by clicking the home button
     */
    @FXML
    private void handlehomewelcometerminal(ActionEvent event) {
        loadScene("/bparkClient/fxml/accessSelection.fxml");
    }

    /**
     * Loads a new scene from the given FXML path.
     *
     * @param fxmlPath the path to the FXML file to load
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) confirmationCodeField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
