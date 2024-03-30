package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

public class PrimaryController {

    Stage secondaryStage = null;
    @FXML
    private Button registerBtn;

    @FXML
    private TextField userTextField;

    @FXML
    private PasswordField passPasswordField;
    @FXML
    private Button loginButton;

    @FXML
    private void registerBtnHandler(ActionEvent event) {
        secondaryStage = new Stage();
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        DB myObj = new DB();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("register.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Register a new User");
            secondaryStage.setOnCloseRequest((WindowEvent event1) -> {
                event1.consume();
                switchToPrimary();
            });
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToSecondary(ActionEvent event) {
        secondaryStage = new Stage();
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            DB myObj = new DB();
            switch (myObj.validateUserTOLogin(userTextField.getText(), passPasswordField.getText())) {
                case 1: {
                    String[] credentials = {userTextField.getText(), passPasswordField.getText()};
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("secondary.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 772, 527);
                    secondaryStage.setScene(scene);
                    SecondaryController controller = loader.getController();
                    controller.initialise(credentials);
                    secondaryStage.setTitle("Admin Dashboard");
                    String msg = "some data sent from Primary Controller";
                    secondaryStage.setUserData(msg);
                    secondaryStage.setOnCloseRequest((WindowEvent event1) -> {
                        event1.consume();
                        // Show an alert
                        showAlert("Confirmation", "Are you sure you want to close this?", AlertType.CONFIRMATION);
                    });
                    secondaryStage.show();
                    primaryStage.close();
                    break;
                }
                case 2: {
                    String[] credentials = {userTextField.getText(), passPasswordField.getText()};
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("userDashboard.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 708, 623);
                    secondaryStage.setScene(scene);
                    secondaryStage.setTitle("User Dashboard");

                    UserDashboardController controller = loader.getController();
                    controller.initialise(credentials);

                    secondaryStage.setOnCloseRequest((WindowEvent event1) -> {
                        event1.consume();
                        // Show an alert
                        showAlert("Confirmation", "Are you sure you want to close this?", AlertType.CONFIRMATION);
                    });
                    secondaryStage.show();
                    primaryStage.close();
                    break;
                }
                default:
                    dialogue("Invalid User Name / Password", "Please try again!");
                    break;
            }

        } catch (IOException | ClassNotFoundException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void txtPassword(ActionEvent event) {
        switchToSecondary(event);
    }

    private void dialogue(String headerMsg, String contentMsg) {
//        Stage secondaryStage = new Stage();
//        Group root = new Group();
//        Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);

        Optional<ButtonType> result = alert.showAndWait();
    }

    private void showAlert(String title, String content, AlertType alertType) {
        Alert confirmationAlert = new Alert(alertType);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(content);
        // Set custom buttons "Yes" and "No"
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

        // Handle the default close button
//        confirmationAlert.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> {
//            event.consume(); // Consume the event to prevent the alert from closing
//        });
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                // User clicked "Yes," view primary view
                switchToPrimary();
            } else if (response == noButton) {
                // User clicked "No," do nothing or handle as needed
                // Check if the alert is showing and the scene is available
//                if (confirmationAlert.isShowing() && confirmationAlert.getDialogPane().getScene() != null) {
//                    confirmationAlert.getDialogPane().getScene().getWindow().hide();
//                }
            }
        });
    }

    private void switchToPrimary() {
        Stage primaryStage = (Stage) loginButton.getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);

            // Set the primary stage's scene to the new scene
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
            // Close the secondary stage
            secondaryStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
