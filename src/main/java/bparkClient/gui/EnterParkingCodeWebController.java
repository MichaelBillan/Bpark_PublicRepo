package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import bparkClient.logic.AppSessionData;
import bparkClient.logic.Bparkclient;
import bparkClient.logic.ParkingSessionController;
import common.RequestType;
import common.entities.ParkingSession;

/**
 * Controller for EnterParkingCodeWeb.fxml.
 * Handles parking code entry and validation for web users.
 */
public class EnterParkingCodeWebController {


    /** Text field for user to input parking code. */
    @FXML
    private TextField parkingcodefield;

    /** Label to display error messages. */
    @FXML
    private Label errorLabel;

    /** Button to confirm parking code. */
    @FXML
    private Button confirmparkingcodebtn;

    /** Hyperlink to navigate to lost code recovery screen. */

    @FXML
    private Hyperlink lostcodelink;

    /** Button to return to drop-off or pickup options screen. */
    @FXML
    private Button backdropofforpickupoptionsbtn;

    /** Button to navigate to the home screen. */
    @FXML
    private Button homewelcometerminal;

    /** Main content container that may be blurred during loading. */
    @FXML
    private VBox mainContent;

    /** Overlay shown during background loading operations. */
    @FXML
    private VBox loadingOverlay;

    /** Parking session associated with the entered code. */
    private ParkingSession session;

    /**
     * Handles the confirmation of the parking code entered by the user.
     * Validates the code and navigates to the extension screen if valid.
     */
    @FXML
    private void handleconfirmparkingcodebtn() {
        String parkingCode = parkingcodefield.getText().trim();
        if (parkingCode.isEmpty()) {
            errorLabel.setText("Please enter a parking code.");
            errorLabel.setVisible(true);
            return;
        }


        LoadingHelper.runWithLoading(
            loadingOverlay,
            mainContent,
            () -> {
                session = ParkingSessionController.getInstance().getSessionByParkingCode(parkingCode);
            },
            () -> {
                String subCode = AppSessionData.getInstance().getSubscriberCode();
                if (session == null || subCode == null || !subCode.equals(session.getSubId())) {
                    errorLabel.setText("Invalid parking code.");
                    errorLabel.setVisible(true);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/ExtendParkingTimeWeb.fxml"));
                        Parent root = loader.load();
                        ExtendParkingTimeWebController controller = loader.getController();
                        controller.setParkingCode(parkingCode);
                        Stage stage = (Stage) parkingcodefield.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        );

    }

    /**
     * Handles navigation to the lost code recovery screen.
     */
    @FXML
    private void handlelostcodelink() {
        loadScene("/bparkClient/fxml/LostCodeWeb.fxml");
    }

    /**
     * Handles navigation back to the subscriber menu web page.
     */
    @FXML
    private void handlebackdsubscribermenuwebbtn() {
        loadScene("/bparkClient/fxml/SubscriberMenuWeb.fxml");
    }

    /**
     * Handles navigation to the home screen.
     */
    @FXML
    private void handlehomewelcomeweb() {
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
