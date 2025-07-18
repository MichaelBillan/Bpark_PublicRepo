package bparkClient.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import bparkClient.logic.AppSessionData;
import bparkClient.logic.ReservationController;
import bparkClient.logic.SubscriberController;

/**
 * Controller for ReserveParking.fxml.
 * Handles the reservation of parking spaces for subscribers.
 */
public class ReserveParkingController {

    /** DatePicker for selecting reservation date. */
    @FXML
    private DatePicker datepickerreserveparking;

    /** Text field for entering the entry time (HH:mm format). */
    @FXML
    private TextField entryTimereserveField;

    /** Button to confirm the reservation. */
    @FXML
    private Button confirmreserveparkingbtn;

    /** Button to navigate back to the subscriber menu. */
    @FXML
    private Button backsubscribermenuweb;

    /** Button to return to the home screen. */
    @FXML
    private Button homeBtn;


    /** Text field for entering the exit time (HH:mm format). */
    @FXML
    private TextField exitTimereserveField;

    /** Overlay displayed during background operations. */
    @FXML
    private VBox loadingOverlay;

    /** Main content area to be blurred during loading. */
    @FXML
    private VBox mainContent;

    /** Holds the generated confirmation code after reservation. */
    private String confirmationCode;


    /**
     * Handles the confirmation of a parking reservation.
     * Validates the input and delegates reservation creation to {@link ReservationController}.
     * Displays appropriate messages or transitions based on the outcome.
     */
    @FXML
    public void handleconfirmreserveparkingbtn() {
        LocalDate selectedDate = datepickerreserveparking.getValue();
        String entryTimeStr = entryTimereserveField.getText();
        String exitTimeStr = exitTimereserveField.getText();

        if (selectedDate == null || entryTimeStr.isEmpty() || exitTimeStr.isEmpty()) {
            showAlert("Please fill all the fields");
            return;
        }


        LoadingHelper.runWithLoading(
            loadingOverlay,
            mainContent,
            () -> {
                try {
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                    LocalTime entryTime = LocalTime.parse(entryTimeStr, timeFormatter);
                    LocalTime exitTime = LocalTime.parse(exitTimeStr, timeFormatter);

                    LocalDateTime fullEntryDateTime = LocalDateTime.of(selectedDate, entryTime);
                    LocalDateTime fullExitDateTime = LocalDateTime.of(selectedDate, exitTime);

                    LocalDateTime now = LocalDateTime.now();

                    if (fullEntryDateTime.isBefore(now)) {
                        throw new IllegalArgumentException("Entry time must be in the future.");
                    }

                    if (!fullExitDateTime.isAfter(fullEntryDateTime)) {
                        throw new IllegalArgumentException("Exit time must be after entry time.");
                    }

                    confirmationCode = SubscriberController.getInstance()
                        .reserveParking(AppSessionData.getInstance().getSubscriberCode(), fullEntryDateTime, fullExitDateTime);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid time format. Please use HH:mm (e.g., 09:30).");
                }
            },
            () -> {
                switch (confirmationCode) {
                    case "Pick valid time":
                        showAlert("Reservation failed. Please choose a time at least 24 hours from now and within 7 days.");
                        break;
                    case "Reservation hours must be between 07:00 and 21:00":
                        showAlert("Invalid hours. Reservations must start and end between 07:00 and 21:00.");
                        break;
                    case "The parking lot is full":
                        showAlert("Reservation failed. There are not enough available spots.");
                        break;
                    case "ERROR":
                    case "System error":
                        showAlert("System error. An unexpected error occurred. Please try again later.");
                        break;
                    default:
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/ReservationSuccess.fxml"));
                            Parent root = loader.load();
                            ReservationSuccessController controller = loader.getController();
                            controller.setConfirmationCode(confirmationCode);
                            Stage stage = (Stage) confirmreserveparkingbtn.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            showAlert("Failed to load Reservation Success screen.");
                            e.printStackTrace();
                        }
                        break;
                }
            },
            error -> {
                if (error instanceof IllegalArgumentException) {
                    showAlert(error.getMessage());
                } else {
                    showAlert("Unexpected error occurred.");
                    error.printStackTrace();
                }

            }
        );
    }

    /**
     * Handles navigation back to the subscriber menu web page.

     *
     * @param event the ActionEvent triggered by the button

     */
    @FXML
    public void handlebacksubscribermenuweb(ActionEvent event) {
        loadScene("/bparkClient/fxml/SubscriberMenuWeb.fxml", backsubscribermenuweb);
    }

    /**

     * Handles navigation to the home screen.
     *
     * @param event the ActionEvent triggered by the button
     */
    @FXML
    public void handleHome(ActionEvent event) {
        loadScene("/bparkClient/fxml/accessSelection.fxml", homeBtn);
    }

    /**

     * Loads a new scene based on the provided FXML path.
     *
     * @param fxmlPath     the path to the FXML file
     * @param sourceButton the button used to get the current stage
     */
    private void loadScene(String fxmlPath, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Failed to load screen: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Displays an information alert with the given message.
     *
     * @param message the message to show
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
