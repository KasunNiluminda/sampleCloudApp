/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author NC
 */
public class UpdateUserController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnupdate;
    @FXML
    private Button btnCancel;

    private User selectedUser;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    // Method to initialize data

    public void initData(User user) {
        selectedUser = user;
        // Set the existing user data in the UI for update
        txtUsername.setText(user.getUser());
        txtPassword.setText(user.getPass());
    }

    @FXML
    private void btnUpdateAction(ActionEvent event) {
        try {
            // Get the updated data from the UI
            String updatedUsername = txtUsername.getText();
            String updatedPassword = txtPassword.getText();

            // Perform the update operation, e.g., call your update method in DB class
            DB myObj = new DB();
            myObj.updateUser(selectedUser.getUser(), updatedUsername, updatedPassword);

            showSuccessAlert("Update Successful");

            // Close the update user stage
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.close();
        } catch (InvalidKeySpecException | ClassNotFoundException ex) {
            Logger.getLogger(UpdateUserController.class.getName()).log(Level.SEVERE, null, ex);
            showErrorAlert("Error updating data");
        }
    }

    @FXML
    private void btnCancelAction(ActionEvent event) {
        // Close the update user stage without saving changes
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.close();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
