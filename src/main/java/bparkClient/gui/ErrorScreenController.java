package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Controller for ErrorsPage.fxml.
 * Displays error information and provides navigation options for the user.
 */
public class ErrorScreenController {

    /**
     * Button to retry the previous action.
     */
    @FXML
    private Button btnTryAgain;

    /**
     * Button to go back to the previous screen.
     */
    @FXML
    private Button btnBack;

    /**
     * Label to display the error reason.
     */
    @FXML
    private Label lblErrorReason;

    /**
     * Stores the reason for the error.
     */
    private String errorReason;

    /**
     * Sets a custom error reason to display on the error screen.
     * @param reason the reason to display
     */
    public void setErrorReason(String reason) {
        this.errorReason = reason;
        if (lblErrorReason != null) {
            lblErrorReason.setText(reason);
        }
    }

    /**
     * Initializes the error label if a reason is set.
     */
    @FXML
    private void initialize() {
        if (errorReason != null) {
            lblErrorReason.setText(errorReason);
        }
    }

    /**
     * Handles the "Try again" button action. Reloads the tag scanning screen.
     */
    @FXML
    private void handleTryAgain() {
        loadScene("/bparkClient/fxml/verifyViaTag.fxml", btnTryAgain);
    }

    /**
     * Handles the "Back" button action. Returns to subscription verification options.
     */
    @FXML
    private void handleBack() {
        loadScene("/bparkClient/fxml/VerifySubscriptionOptions.fxml", btnBack);
    }

    /**
     * Loads a new scene from the given FXML path using the provided button as the source.
     * @param fxmlPath the FXML file path
     * @param sourceButton the button that triggered the navigation
     */
    private void loadScene(String fxmlPath, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load scene: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
