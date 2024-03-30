/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author NC
 */
public class UserPermissionController implements Initializable {

    @FXML
    private Label fileIdLabel;
    @FXML
    private Label fileNameLabel;
    @FXML
    private Label filePathLabel;
    @FXML
    private TableView userTableView;
    @FXML
    private TableColumn<?, ?> userIdColumn;
    @FXML
    private TableColumn<?, ?> usernameColumn;
    @FXML
    private Button btnRead;
    @FXML
    private Button btnWrite;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnReadWrite;
    @FXML
    private Label lblId;
    @FXML
    private Label lblPath;
    @FXML
    private Label lblFileName;

    private int fileId;  // Assuming fileId is an int
    private String fileName;
    private String filePath;
    private int author_id;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void initialize(int author_id, int fileId, String fileName, String filePath) {
        this.author_id = author_id;
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;

        // Set the file information labels
        lblId.setText(fileId + "");
        lblFileName.setText(fileName);
        lblPath.setText(filePath);

        // Initialize the user table
        initializeUserTable();
    }

    private void initializeUserTable() {
        // Assuming your DB class has a method to get all users
        DB myObj = new DB();
        ObservableList<UserLoad> userData = myObj.getAllUsers();
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        // Add more columns as needed for user information
        userTableView.setItems(userData);
    }

    @FXML
    private void btnReadAction(ActionEvent event) {
        grantPermission("READ");
    }

    @FXML
    private void btnWriteAction(ActionEvent event) {
        grantPermission("WRITE");
    }

    @FXML
    private void btnDeleteAction(ActionEvent event) {
        grantPermission("DELETE");
    }

    @FXML
    private void btnReadWriteAction(ActionEvent event) {
        grantPermission("READ-WRITE");
    }

    private void grantPermission(String accessLevel) {
        // Get the selected user from the table
        UserLoad selectedUser = (UserLoad) userTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Assuming your DB class has a method to save permission to the database
            DB myObj = new DB();
            try {
                myObj.savePermission(author_id, fileId, selectedUser.getUserId(), accessLevel);

                // Show success message
                showAlert("Permission Granted", "Permission granted successfully!", Alert.AlertType.INFORMATION);

                // Update the user table to reflect the changes
                initializeUserTable();
            } catch (Exception ex) {
                Logger.getLogger(UserPermissionController.class.getName()).log(Level.SEVERE, null, ex);
                // Handle the exception appropriately
                showAlert("Error", "Error granting permission: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("No User Selected", "Please select a user to grant permission.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
