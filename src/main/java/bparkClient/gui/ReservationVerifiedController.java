package bparkClient.gui;

import java.io.IOException;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * Controller for ReservationVerified.fxml.
 * Handles navigation after reservation verification.
 */
public class ReservationVerifiedController {

    @FXML
    private Button backenterconfirmationcode;

    @FXML
    private Button homewelcometerminal;

    @FXML
    private Label parkingCodeLabel;

    private String parkingCode;

    /**
     * Sets the parking code to be displayed on the screen.
     * @param code the parking code
     */
    public void setParkingCode(String code) {
        this.parkingCode = code;
        if (parkingCodeLabel != null) {
            parkingCodeLabel.setText(code);
        }
    }

    /**
     * מחזיר למסך הקודם בו הוזן קוד האישור.
     */
    @FXML
    private void handlebackenterconfirmationcode() {
        loadScene("/bparkClient/fxml/EnterConfirmationCode.fxml");
    }

    /**
     * מחזיר למסך הראשי של הטרמינל.
     */
    @FXML
    private void handlehomewelcometerminal() {
        loadScene("/bparkClient/fxml/accessSelection.fxml");
    }

    /**
     * טוען מסך חדש לפי הנתיב שנשלח.
     * @param fxmlPath נתיב לקובץ ה־FXML לטעינה
     */
    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) backenterconfirmationcode.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
