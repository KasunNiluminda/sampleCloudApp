/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class RegisterController {

    Stage secondaryStage = null;
    /**
     * Initializes the controller class.
     */
    @FXML
    private Button registerBtn;

    @FXML
    private Button backLoginBtn;

    @FXML
    private PasswordField passPasswordField;

    @FXML
    private PasswordField rePassPasswordField;

    @FXML
    private TextField userTextField;

    @FXML
    private Text fileText;

    @FXML
    private Button selectBtn;

    @FXML
    private void selectBtnHandler(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) selectBtn.getScene().getWindow();
        primaryStage.setTitle("Select a File");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            fileText.setText((String) selectedFile.getCanonicalPath());
        }

    }

    @FXML
    private void registerBtnHandler(ActionEvent event) {
        secondaryStage = new Stage();
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        try {
            DB myObj = new DB();
            if (!"".equals(userTextField.getText()) && !"".equals(passPasswordField.getText()) && !"".equals(rePassPasswordField.getText())) {
                if (passPasswordField.getText().equals(rePassPasswordField.getText())) {
                    if (!myObj.validateUserTORegister(userTextField.getText())) {

                        myObj.addDataToDB(userTextField.getText(), rePassPasswordField.getText());
                        if (!"".equals(fileText.getText())) {
                            myObj.addFilesToDB(userTextField.getText(), fileText.getText());
                        }

                        dialogueSuccess("User Registration Successful.!!!", "Successful!");

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("userDashboard.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root, 708, 623);
                        secondaryStage.setScene(scene);
                        secondaryStage.setTitle("User Dashboard");

                        String[] credentials = {userTextField.getText(), rePassPasswordField.getText()};
                        UserDashboardController controller = loader.getController();
                        controller.initialise(credentials);

                        secondaryStage.setOnCloseRequest((WindowEvent event1) -> {
                            event1.consume();
                            // Show an alert
                            showAlert("Confirmation", "Are you sure you want to close this?", Alert.AlertType.CONFIRMATION);
                        });
                        secondaryStage.show();
                        primaryStage.close();

                    } else {
                        dialogue("This user allredy registered.!!!", "Please try again!");
                    }

//                    secondaryStage.setTitle("Show users");
//                    String msg = "some data sent from Register Controller";
//                    secondaryStage.setUserData(msg);
                } else {
                    dialogue("Password and re-type password are not matching.!!!", "Please try again!");
//                    loader.setLocation(getClass().getResource("register.fxml"));
//                    Parent root = loader.load();
//                    Scene scene = new Scene(root, 640, 480);
//                    secondaryStage.setScene(scene);
//                    secondaryStage.setTitle("Register a new User");
                }
            } else {
                dialogue("All text feilds are mandatory.!!!", "Please try again!");

            }

        } catch (IOException | ClassNotFoundException | InvalidKeySpecException e) {
            dialogue("Error Ocurred now.!!!", e.toString());
            e.printStackTrace();
        }
    }

    @FXML
    private void backLoginBtnHandler(ActionEvent event) {
        secondaryStage = new Stage();
        Stage primaryStage = (Stage) backLoginBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            dialogue("Error Ocurred now.!!!", e.toString());
            e.printStackTrace();
        }
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

    private void dialogueSuccess(String headerMsg, String contentMsg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);

        Optional<ButtonType> result = alert.showAndWait();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert confirmationAlert = new Alert(alertType);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(content);
        // Set custom buttons "Yes" and "No"
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        confirmationAlert.getButtonTypes().setAll(yesButton, noButton);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                ActionEvent event = null;
                secondaryStage.close();
                backLoginBtnHandler(event);

            }
        });

    }

}
