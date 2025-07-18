package bparkClient.gui;

import bparkClient.logic.SubscriberController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller for LostCodeWeb.fxml.
 * Handles user actions for recovering lost parking codes via web interface.
 */
public class LostCodeWebController {

    @FXML
    private Button sendparkingcodeviaemailbtn;

    @FXML
    private Button sendparkingcodeviasmsbtn;

    @FXML
    private Button backenterparkingcodebtn;

    @FXML
    private Button homewelcometerminal;

    @FXML
    private Label feedbackLabel;

    /**
     * Handles sending the parking code via email.
     * @param event the action event
     */
    @FXML
    void handlesendparkingcodeviaemailbtn(ActionEvent event) {
        String result = SubscriberController.getInstance().sendParkingCodeByEmail();
        feedbackLabel.setText(result);
        feedbackLabel.setStyle(result.startsWith("Failed") ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        feedbackLabel.setVisible(true);
    }

    /**
     * Handles sending the parking code via SMS (UI only).
     * @param event the action event
     */
    @FXML
    void handlesendparkingcodeviasmsbtn(ActionEvent event) {
        String phone = bparkClient.logic.AppSessionData.getInstance().getSubscriberCode();
        String phoneNumber = null;
        if (phone != null) {
            common.entities.Subscriber sub = bparkClient.logic.SubscriberController.getInstance().getSubDetails(phone);
            if (sub != null) {
                phoneNumber = sub.getPhoneNumber();
            }
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            feedbackLabel.setText("An SMS with your parking code has been sent to " + phoneNumber + ".");
        } else {
            feedbackLabel.setText("An SMS with your parking code has been sent to your registered phone number.");
        }
        feedbackLabel.setStyle("-fx-text-fill: green;");
        feedbackLabel.setVisible(true);
    }

    /**
     * Handles navigation back to the enter parking code screen.
     * @param event the action event
     */
    @FXML
    void handlebackenterparkingcodebtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/EnterParkingCodeWeb.fxml");
    }

    /**
     * Handles navigation to the terminal home screen.
     * @param event the action event
     */
    @FXML
    void handlehomewelcometerminal(ActionEvent event) {
        loadScene("/bparkClient/fxml/RoleSelectionWeb.fxml");
    }

    /**
     * Loads a new scene from the given FXML path.
     * @param fxmlPath the FXML file path
     */
    private void loadScene(String fxmlPath) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(fxmlPath));
            javafx.scene.Parent root = loader.load();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            javafx.stage.Stage stage = (javafx.stage.Stage) sendparkingcodeviaemailbtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
