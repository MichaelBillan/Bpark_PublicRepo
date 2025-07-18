package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;

import bparkClient.logic.AppSessionData;
import bparkClient.logic.Bparkclient;
import bparkClient.logic.SubscriberController;
import common.RequestType;

/**
 * Controller for EntersubscriptioncodeWeb.fxml.
 * Handles subscriber verification via subscription code for web users.
 */
public class EntersubscriptioncodeWebController {

    /**
     * Text field for entering the subscriber's code.
     */
    @FXML
    private TextField subscriptionCodeFieldterminal;

    /**
     * Button to trigger verification of the subscriber code.
     */
    @FXML
    private Button verifysubcodebtn;

    @FXML
    private Button backRoleSelectionWeb;

    @FXML
    private Button homewelcomeweb;

    /**
     * Label to show error messages during validation.
     */
    @FXML
    private Label errorLabel;

    /**
     * Initializes the error label visibility.
     */
    @FXML
    private void initialize() {
        errorLabel.setVisible(false);
    }

    /**
     * Handles the verification of the subscription code entered by the user.
     */
    @FXML
    private void handleverifysubcodebtn() {
        String code = subscriptionCodeFieldterminal.getText();

        if (code == null || code.trim().isEmpty()) {
            errorLabel.setText("Invalid code");
            errorLabel.setVisible(true);
            return;
        }

        if (SubscriberController.getInstance().identifySubscriberViaSubId(code)) {
            AppSessionData.getInstance().setSubscriberCode(code);
            errorLabel.setVisible(false);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/SubscriberMenuWeb.fxml"));
                Parent root = loader.load();
                SubscriberMenuWebController controller = loader.getController();
                common.entities.Subscriber sub = bparkClient.logic.SubscriberController.getInstance().getSubDetails(code);
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
            loadScene("/bparkClient/fxml/ErrorsPageWeb.fxml");
        }
    }

    /**
     * Handles navigation back to the role selection web screen.
     */
    @FXML
    private void handlebackroleselectionweb() {
        loadScene("/bparkClient/fxml/RoleSelectionWeb.fxml");
    }

    /**
     * Navigates to the main access selection screen.
     */
    @FXML
    private void handlehomewelcomeweb() {
        loadScene("/bparkClient/fxml/AccessSelection.fxml");
    }

    /**
     * Loads a new scene from the given FXML path.
     * @param fxmlPath the FXML file path
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
