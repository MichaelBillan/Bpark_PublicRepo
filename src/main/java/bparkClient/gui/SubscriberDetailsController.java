package bparkClient.gui;

import bparkClient.logic.AppSessionData;
import bparkClient.logic.SubscriberController;
import common.entities.Subscriber;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for SubscriberDetails.fxml.
 * Displays and manages subscriber details, including editing contact information and viewing history.
 */
public class SubscriberDetailsController {

    @FXML
    private Label subscriberCodeField;

    @FXML
    private Label nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private Button editdetailsbtn;

    @FXML
    private Label successMessageLabel;

    private boolean isEditing = false;

    /**
     * Initializes the subscriber details screen with data fetched from the session.
     */
    @FXML
    public void initialize() {
        phoneField.setEditable(false);
        emailField.setEditable(false);
        successMessageLabel.setVisible(false);

        Subscriber current = SubscriberController.getInstance()
                .getSubDetails(AppSessionData.getInstance().getSubscriberCode());

        if (current != null) {
            subscriberCodeField.setText(current.getSubId());
            nameField.setText(current.getName());
            phoneField.setText(current.getPhoneNumber());
            emailField.setText(current.getEmail());
        }
    }

    /**
     * Handles the edit/save logic for subscriber contact details.
     * @param event the action event triggered by clicking the edit/save button
     */
    @FXML
    void handleEditDetails(ActionEvent event) {
        if (!isEditing) {
            phoneField.setEditable(true);
            emailField.setEditable(true);
            editdetailsbtn.setText("Save");
            isEditing = true;
        } else {
            String newPhone = phoneField.getText().trim();
            String newEmail = emailField.getText().trim();

            // ולידציה
            if (!newPhone.matches("\\d{10}")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Phone number must contain exactly 10 digits.");
                return;
            }

            if (!newEmail.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
                return;
            }

            // אם עבר ולידציה, עדכון המידע
            Subscriber current = SubscriberController.getInstance()
                    .getSubDetails(subscriberCodeField.getText());

            if (current != null) {
                if (!newPhone.equals(current.getPhoneNumber())) {
                    SubscriberController.getInstance().changePhone(current.getSubId(), newPhone);
                }
                if (!newEmail.equals(current.getEmail())) {
                    SubscriberController.getInstance().changeEmail(current.getSubId(), newEmail);
                }
            }

            phoneField.setEditable(false);
            emailField.setEditable(false);
            editdetailsbtn.setText("Edit Details");
            isEditing = false;

            successMessageLabel.setVisible(true);

            // הסתרת ההודעה לאחר כמה שניות
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(e -> successMessageLabel.setVisible(false));
            pause.play();
        }
    }

    /**
     * Handles navigation to the subscriber's parking history screen.
     * @param event the action event triggered by the user
     */
    @FXML
    void handleviewparkinghistorybtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/ParkingHistory.fxml");
    }

    /**
     * Handles navigation to the subscriber's reservation history screen.
     * @param event the action event triggered by the user
     */
    @FXML
    void handleviewreservationhistorybtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/ReservationHistory.fxml");
    }

    /**
     * Handles navigation back to the subscriber menu.
     * @param event the action event triggered by the user
     */
    @FXML
    void handlebacksubscribermenuwebbtn(ActionEvent event) {
        loadScene("/bparkClient/fxml/SubscriberMenuWeb.fxml");
    }

    /**
     * Handles navigation to the web home screen.
     * @param event the action event triggered by the user
     */
    @FXML
    void handlehomewelcomeweb(ActionEvent event) {
        loadScene("/bparkClient/fxml/RoleSelectionWeb.fxml");
    }

    /**
     * Handles navigation back to the subscribers table based on the user origin.
     * If origin is manager or usher, returns to their respective tables.
     * @param event the action event triggered by the user
     */
    @FXML
    void handleBackToTable(ActionEvent event) {
        String origin = AppSessionData.getInstance().getDetailsOrigin();
        String fxmlPath = null;
        if ("manager".equals(origin)) {
            fxmlPath = "/bparkClient/fxml/AllSubscribersManager.fxml";
        } else if ("usher".equals(origin)) {
            fxmlPath = "/bparkClient/fxml/AllSubscribers.fxml";
        }
        if (fxmlPath != null) {
            AppSessionData.getInstance().clearDetailsOrigin();
            loadScene(fxmlPath);
        } else {
            // Default: go back to subscriber menu (for regular subscribers)
            handlebacksubscribermenuwebbtn(event);
        }
    }

    /**
     * Loads a new scene from the given FXML file path.
     * @param fxmlPath the FXML file to load
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) editdetailsbtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog to the user.
     * @param alertType the type of alert
     * @param title the title of the alert window
     * @param content the message content to display
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
