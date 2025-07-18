package bparkClient.gui;

import java.io.IOException;

import bparkClient.logic.UsherController;
import common.entities.Subscriber;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controller for RegistPage.fxml.
 * Handles registration of new subscribers via the usher interface.
 */
public class RegisterPageController {

    @FXML
    private Button backushermenu;

    @FXML
    private Button homewelcomeweb;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    /**
     * Handles the navigation to the home screen.
     */
    @FXML
    private void handlehomewelcomeweb() {
        loadScene("/bparkClient/fxml/accessSelection.fxml");
    }

    /**
     * Handles the navigation back to the usher menu.
     */
    @FXML
    private void handlebackushermenu() {
        loadScene("/bparkClient/fxml/UsherMainPage.fxml");
    }

    /**
     * Handles the registration process when the register button is clicked.
     * Validates input and delegates registration to UsherController.
     */
    @FXML
    private void handleRegister() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        // בדיקה: שדות ריקים
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            showAlert(AlertType.WARNING, "Missing Fields", "Please fill all the fields.");
            return;
        }

        // בדיקה: השם רק באנגלית ורווחים (ללא ספרות או סימנים)
        if (!name.matches("[a-zA-Z ]+")) {
            showAlert(AlertType.WARNING, "Invalid Name", "Name must contain English letters and spaces only (no digits or symbols).");
            return;
        }

        // בדיקה: הטלפון רק ספרות באורך של עד 10
        if (!phone.matches("\\d{10}")) {
            showAlert(AlertType.WARNING, "Invalid Phone", "Phone must contain only digits and 10 digits long.");
            return;
        }

        // בדיקה: אימייל לפי תבנית תקנית
        if (!isValidEmail(email)) {
            showAlert(AlertType.WARNING, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        // אם הכל תקין - רישום
        Subscriber sub = UsherController.registerSubscriber(name, phone, email);

        if (sub != null) {
            showAlert(AlertType.INFORMATION, "Success", "Subscriber registered: " + sub.getSubId());
            nameField.clear();
            phoneField.clear();
            emailField.clear();
        } else {
            showAlert(AlertType.ERROR, "Registration Failed", "Could not register subscriber.");
        }
    }

    /**
     * Validates the email address format.
     *
     * @param email the email string to validate
     * @return true if the email is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z][A-Za-z0-9._%+-]*@[A-Za-z]{2,}[A-Za-z0-9.-]*\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Loads a new scene from the given FXML path.
     *
     * @param fxmlPath the path to the FXML file
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) backushermenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not load the scene.");
        }
    }

    /**
     * Displays an alert dialog with the specified message.
     *
     * @param type    the type of alert (e.g., ERROR, INFORMATION)
     * @param title   the title of the alert window
     * @param message the content message to display
     */
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
