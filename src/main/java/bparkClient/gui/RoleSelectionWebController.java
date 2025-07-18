package bparkClient.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * Controller for RoleSelectionWeb.fxml.
 * Handles role selection for web users and navigation to respective screens.
 */
public class RoleSelectionWebController {

    @FXML
    private Button backButton;

    @FXML
    private Button checkparkingavailabilitywebbtn;

    @FXML
    private Button subscriberwebbtn;

    @FXML
    private Button employeewebbtn;

    /**
     * Handles navigation to the parking availability screen.
     * @param event the action event
     */
    @FXML
    void handlecheckparkingavailabilitywebbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/CurrentlyAvailableSpacesGUserWeb.fxml");
    }

    /**
     * Handles navigation back to the access selection screen.
     * @param event the action event
     */
    @FXML
    void handleBack(ActionEvent event) {
        loadScene("/bparkClient/fxml/AccessSelection.fxml");
    }

    /**
     * Handles navigation to the subscriber login/verification screen.
     * @param event the action event
     */
    @FXML
    void handlesubscriberwebbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/EntersubscriptioncodeWeb.fxml");
    }

    /**
     * Handles navigation to the employee role selection screen.
     * @param event the action event
     */
    @FXML
    void handleemployeewebbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/EmployeeVerify.fxml");
    }

    /**
     * Loads a new scene from the given FXML path.
     * @param fxmlPath the FXML file path
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) checkparkingavailabilitywebbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
