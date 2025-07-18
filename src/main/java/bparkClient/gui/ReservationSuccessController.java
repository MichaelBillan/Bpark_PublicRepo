package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * Controller for ReservationSuccess.fxml.
 * Displays the confirmation code after a successful reservation.
 */
public class ReservationSuccessController {

    @FXML
    private Label confirmationCodeLabel;

    @FXML
    private Button backreserveparking;

    @FXML
    private Button homewelcomeweb;

    private String confirmationCode;

    /**
     * Sets the confirmation code to display.
     * @param confirmationCode the confirmation code
     */
    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
        if (confirmationCodeLabel != null) {
            confirmationCodeLabel.setText("Your Confirmation Code: " + confirmationCode);
        }
    }

    /**
     * Initializes the confirmation code label if the code is set.
     */
    @FXML
    public void initialize() {
        if (confirmationCode != null) {
            confirmationCodeLabel.setText("Your Confirmation Code: " + confirmationCode);
        }
    }

    /**
     * Handles navigation back to the reserve parking page.
     * @param event the button click event
     */
    @FXML
    private void handlebackreserveparking(javafx.event.ActionEvent event) {
        System.out.println("🔙 Back button clicked");
        loadScene("/bparkClient/fxml/ReserveParking.fxml", event);
    }

    /**
     * Handles navigation to the home screen.
     * @param event the button click event
     */
    @FXML
    private void handlehomewelcomeweb(javafx.event.ActionEvent event) {
        System.out.println("🏠 Home button clicked");
        loadScene("/bparkClient/fxml/accessSelection.fxml", event);
    }

    /**
     * Loads a new scene from the given FXML path.
     * @param fxmlPath the FXML file path
     * @param event the triggering ActionEvent
     */
    private void loadScene(String fxmlPath, javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
