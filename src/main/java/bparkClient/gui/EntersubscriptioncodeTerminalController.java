package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;

import bparkClient.logic.AppSessionData;
import bparkClient.logic.Bparkclient;
import bparkClient.logic.SubscriberController;
import common.RequestType;

/**
 * Controller for EntersubscriptioncodeTerminal.fxml.
 * Handles subscriber verification via subscription code for terminal users.
 */
public class EntersubscriptioncodeTerminalController {

    /** Text field for entering the subscription code. */
    @FXML
    private TextField subscriptionCodeFieldterminal;

    /** Button to trigger subscription code verification. */

    @FXML
    private Button verifysubcodebtn;

    /** Button to navigate back to verification options screen. */
    @FXML
    private Button backverifysuboptions;

    /** Button to navigate to the terminal home screen. */
    @FXML
    private Button homewelcometerminal;

    /** Label to display error messages. */
    @FXML
    private Label errorLabel;

    /** Overlay shown during background loading operations. */
    @FXML
    private VBox loadingOverlay;

    /** Main content container that may be blurred during loading. */
    @FXML
    private VBox mainContent;

    /** Indicates whether a matching subscriber was found. */
    private boolean found = false;

    /** The subscriber entity retrieved after successful verification. */
    private common.entities.Subscriber sub = null;


    /**
     * Initializes the error label visibility.
     */
    @FXML
    private void initialize() {
        errorLabel.setVisible(false);
    }

    /**
     * Handles the verification of the subscription code entered by the user.
     * If valid, sets the subscriber data and navigates to the next screen.
     */
    @FXML
    private void handleverifysubcodebtn() {
        String code = subscriptionCodeFieldterminal.getText();

        if (code == null || code.trim().isEmpty()) {
            errorLabel.setText("Invalid code");
            errorLabel.setVisible(true);
            return;
        }

        LoadingHelper.runWithLoading(
            loadingOverlay,
            mainContent,
            () -> {
                found = SubscriberController.getInstance().identifySubscriberViaSubId(code);
                if (found) {
                    AppSessionData.getInstance().setSubscriberCode(code);
                    sub = SubscriberController.getInstance().getSubDetails(code);
                }
            },
            () -> {
                if (found) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/DropOffOrPickUpTerminal.fxml"));
                        Parent root = loader.load();
                        DropOffOrPickUpTerminalController controller = loader.getController();
                        if (sub != null) {
                            controller.setWelcomeText(sub.getName());
                        }
                        Stage stage = (Stage) subscriptionCodeFieldterminal.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    loadScene("/bparkClient/fxml/ErrorsPage.fxml");
                }
            }
        );
    }

    /**
     * Handles navigation back to the verify subscription options screen.
     */
    @FXML
    private void handlebackverifysuboptions() {
        loadScene("/bparkClient/fxml/VerifySubscriptionOptions.fxml"); 
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
            Stage stage = (Stage) subscriptionCodeFieldterminal.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
