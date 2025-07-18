package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import bparkClient.logic.AppSessionData;
import bparkClient.logic.Bparkclient;
import bparkClient.logic.SubscriberController;
import common.RequestType;

/**
 * Controller for verifyViaTag.fxml.
 * Handles subscriber verification via RFID tag for terminal users.
 */
public class verifyViaTagController {

    @FXML
    private Button placeatagbtn;

    @FXML
    private Button backverifysuboptions;

    @FXML
    private Button homewelcometerminal;

    /**
     * Handles the action when the user places a tag for verification.
     * Navigates to the next screen based on verification result.
     */
    @FXML
    private void handleplaceatagbtn() {
        String subId = SubscriberController.getInstance().scanViaTag();

        if (subId != null) {
            AppSessionData.getInstance().setSubscriberCode(subId);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/DropOffOrPickUpTerminal.fxml"));
                Parent root = loader.load();
                DropOffOrPickUpTerminalController controller = loader.getController();
                common.entities.Subscriber sub = bparkClient.logic.SubscriberController.getInstance().getSubDetails(subId);
                if (sub != null) {
                    controller.setWelcomeText(sub.getName());
                }
                Stage stage = (Stage) placeatagbtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loadScene("/bparkClient/fxml/failedPlaceATag.fxml");
        }
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
     * Loads a new scene from the given FXML path.
     * @param fxmlPath the FXML file path
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) placeatagbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
