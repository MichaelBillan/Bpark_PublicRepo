package bparkClient.gui;

import java.io.IOException;
import javafx.scene.control.PasswordField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller for EmployeeVerify.fxml.
 * Handles staff code verification for employees.
 */
public class EmployeeVerifyController {

    /**
     * Password field where the staff enters their verification code.
     */
    @FXML
    private PasswordField staffCodeField;

    /**
     * Button that triggers the verification process.
     */
    @FXML
    private Button verifyStaffCodeBtn;

    /**
     * Initializes the controller (optional).
     */
    @FXML
    public void initialize() {
        System.out.println("EmployeeVerifyController initialized");
    }

    @FXML
    private Button backverifycode;

    @FXML
    private Button homewelcomeweb;

    /**
     * Handles the staff code verification when the Verify button is clicked.
     */
    @FXML
    private void handleVerifyStaffCode() {
        String code = staffCodeField.getText().trim();
        if (code.equals("1234"))
            loadScene("/bparkClient/fxml/UsherMainPage.fxml");
        else if (code.equals("1111"))
            loadScene("/bparkClient/fxml/WelcomeParkingLotManager.fxml");
        else if (code.isEmpty()) {
            showAlert("Error", "Staff code cannot be empty.");
        } else {
            showAlert("Error", "Invalid staff code. Please try again.");
        }
    }

    @FXML
    private void handlebackverifycode() {
        loadScene("/bparkClient/fxml/RoleSelectionWeb.fxml");
    }

    @FXML
    private void handlehomewelcomeweb() {
        loadScene("/bparkClient/fxml/accessSelection.fxml");
    }

    /**
     * Shows an alert dialog.
     *
     * @param title   the alert title
     * @param message the alert message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Loads a new scene from the given FXML file path.
     *
     * @param fxmlPath the FXML file path to load
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) verifyStaffCodeBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
