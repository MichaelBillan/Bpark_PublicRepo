package bparkClient.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Controller for EmployeeRoleSelection.fxml.
 * Handles employee role selection and navigation for web users.
 */
public class EmployeeRoleSelectionController {

    /**
     * Button to navigate to the parking lot manager section.
     */
    @FXML
    private Button parkinglotmanagerbtn;

    /**
     * Button to navigate to the usher section.
     */
    @FXML
    private Button usherbtn;

    @FXML
    private Button backroleselectionweb;

    @FXML
    private Button homewelcomeweb;

    // *** The logic need to be updated so the employee enters the employee code and based on the employee code we can know if he is usher or manager
    // only add two codes in this class that identifies the employee, no need to check in the database ***

    /**
     * Handles navigation to the parking lot manager welcome screen.
     * @param event the action event
     */
    @FXML
    void handleparkinglotmanagerbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/WelcomeParkingLotManager.fxml", event);
    }

    /**
     * Handles navigation to the usher main page.
     * @param event the action event
     */
    @FXML
    void handleusherbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/UsherMainPage.fxml", event);
    }

    /**
     * Handles navigation back to the web role selection screen.
     * @param event the action event
     */
    @FXML
    void handlebackroleselectionweb(ActionEvent event) {
        loadScene("/bparkClient/fxml/RoleSelectionWeb.fxml", event);
    }

    /**
     * Handles navigation to the web home screen.
     * @param event the action event
     */
    @FXML
    void handlehomewelcomeweb(ActionEvent event) {
        loadScene("/bparkClient/fxml/RoleSelectionWeb.fxml", event);
    }

    /**
     * Loads a new scene from the given FXML path using the provided event.
     * @param fxmlPath the FXML file path
     * @param event the action event
     */
    private void loadScene(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
