package bparkClient.gui;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for menu.fxml.
 * Manages the main menu interface and handles navigation to web and terminal submenus and related screens.
 */
public class MenuController {

    @FXML private VBox webSubMenu;
    @FXML private VBox terminalSubMenu;

    /**
     * Toggles the visibility of the web submenu.
     */
    @FXML
    private void toggleWebMenu() {
        webSubMenu.setVisible(!webSubMenu.isVisible());
    }

    /**
     * Toggles the visibility of the terminal submenu.
     */
    @FXML
    private void toggleTerminalMenu() {
        terminalSubMenu.setVisible(!terminalSubMenu.isVisible());
    }

    /**
     * Loads a new scene given the FXML file name (without extension).
     * @param fxmlName the name of the FXML file to load (without ".fxml")
     */
    private void loadScene(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/" + fxmlName + ".fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(fxmlName);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the CurrentlyAvailableSpacesGUser screen.
     */
    @FXML
    private void goToCurrentlyAvailableSpacesGUser() {
        loadScene("CurrentlyAvailableSpacesGUser");
    }

    /**
     * Navigates to the VerifySubscriptionOptions screen.
     */
    @FXML
    private void goToVerifySubscriptionOptions() {
        loadScene("VerifySubscriptionOptions");
    }

    /**
     * Navigates to the EmployeeVerify screen.
     */
    @FXML
    private void goToEmployeeVerify() {
        loadScene("EmployeeVerify");
    }
}
