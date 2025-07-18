package bparkClient.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for SubscriberMenuWeb.fxml.
 * Handles navigation for subscriber menu options in the web interface.
 */
public class SubscriberMenuWebController {

    @FXML
    private Button backentersubcode;

    @FXML
    private Button homewelcomeweb;
    
    @FXML
    private Button myprofilesubwebbtn;

    @FXML
    private Button reserveparkingwebbtn;

    @FXML
    private Button extendparkingtimewebbtn;

    @FXML
    private Label welcomeLabel;

    /**
     * Navigate back to the subscription code entry screen.
     */
    @FXML
    private void handlebackEnterSubCode() {
        loadScene("/bparkClient/fxml/EntersubscriptioncodeWeb.fxml");
    }

    /**
     * Navigate to the home screen (AccessSelection).
     */
    @FXML
    private void handlehomewelcomeweb() {
        loadScene("/bparkClient/fxml/AccessSelection.fxml");
    }
    
    /**
     * Handles navigation to the subscriber profile page.
     * @param event the action event
     */
    @FXML
    void handlemyprofilesubwebbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/SubscriberDetails.fxml");
    }

    /**
     * Handles navigation to the reserve parking page.
     * @param event the action event
     */
    @FXML
    void handlereserveparkingwebbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/ReserveParking.fxml");
    }

    /**
     * Handles navigation to the extend parking time page.
     * @param event the action event
     */
    @FXML
    void handleextendparkingtimewebbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/EnterParkingCodeWeb.fxml");
    }

    /**
     * Loads a new scene from the given FXML path.
     * @param fxmlPath the FXML file path
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) myprofilesubwebbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the welcome label to include the subscriber's name.
     * @param subscriberName the name of the subscriber
     */
    public void setWelcomeText(String subscriberName) {
        if (welcomeLabel != null && subscriberName != null) {
            welcomeLabel.setText("Welcome " + subscriberName + "!");
        }
    }
}
