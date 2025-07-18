package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import bparkClient.logic.ParkingSessionController;
import common.entities.ParkingSession;

/**
 * Controller for EnterParkingCodeTerminal.fxml.
 * Handles parking code entry and validation for terminal users.
 */
public class EnterParkingCodeTerminalController {

    /** Text field for user to input parking code. */
    @FXML
    private TextField parkingcodefield;

    /** Label to display error messages. */
    @FXML
    private Label errorLabel;

    /** Button to confirm parking code. */
    @FXML
    private Button confirmparkingcodebtn;

    /** Hyperlink to handle lost code scenario. */

    @FXML
    private Hyperlink lostcodelink;

    /** Button to navigate back to the drop-off or pickup options screen. */
    @FXML
    private Button backdropofforpickupoptionsbtn;

    /** Button to navigate to the terminal home screen. */
    @FXML
    private Button homewelcometerminal;


    /** Overlay shown during background loading operations. */
    @FXML
    private VBox loadingOverlay;

    /** Main content container that may be blurred during loading. */
    @FXML
    private VBox mainContent;

    /** Whether the entered code is valid. */
    private boolean isValid = false;

    /** Whether the code is valid and matches the subscriber. */
    private boolean validAndMatch = false;

    /** The parking session retrieved based on the code. */
    private ParkingSession session;

    /** The subscriber code stored in the current app session. */
    private String subCode;


    /**
     * Handles the confirmation of the parking code entered by the user.
     * Validates the code and checks if it matches the current subscriber.
     */
    @FXML
    private void handleconfirmparkingcodebtn() {
        String parkingCode = parkingcodefield.getText().trim();

        if (parkingCode.isEmpty()) {
            errorLabel.setText("Please enter a parking code.");
            return;
        }

        LoadingHelper.runWithLoading(
            loadingOverlay,
            mainContent,
            () -> {
                isValid = ParkingSessionController.getInstance().validateParkingCode(parkingCode);
                if (isValid) {
                    session = ParkingSessionController.getInstance().getSessionByParkingCode(parkingCode);
                    subCode = bparkClient.logic.AppSessionData.getInstance().getSubscriberCode();
                    if (session != null && subCode != null && subCode.equals(session.getSubId())) {
                        ParkingSessionController.getInstance().endSession(parkingCode);
                        validAndMatch = true;
                    }
                }
            },
            () -> {
                if (validAndMatch) {
                    loadScene("/bparkClient/fxml/pickup.fxml");
                } else {
                    errorLabel.setText("Invalid parking code. Please try again.");
                    errorLabel.setVisible(true);
                }
            }
        );
    }

    /**

     * Handles the event where user clicks the "lost code" link.
     * Navigates to the lost code assistance screen.

     */
    @FXML
    private void handlelostcodelink() {
        loadScene("/bparkClient/fxml/LostCodeTerminal.fxml");
    }

    /**
     * Handles navigation back to the drop-off or pickup options screen.
     */
    @FXML
    private void handlebackdropofforpickupoptionsbtn() {
        loadScene("/bparkClient/fxml/DropOffOrPickUpTerminal.fxml");
    }

    /**
     * Handles navigation to the home terminal welcome screen.
     */
    @FXML
    private void handlehomewelcometerminal() {
        loadScene("/bparkClient/fxml/accessSelection.fxml");
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
            Stage stage = (Stage) parkingcodefield.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
