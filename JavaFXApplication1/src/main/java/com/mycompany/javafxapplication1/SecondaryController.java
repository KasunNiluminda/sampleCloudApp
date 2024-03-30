package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SecondaryController {

    Stage secondaryStage = null;
    @FXML
    private TextField userTextField;

    @FXML
    public TableView dataTableView;

    @FXML
    private Button secondaryButton;

    @FXML
    private Button refreshBtn;

    @FXML
    private TextField customTextField;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdateUser;
    @FXML
    private Button btnFileAdding;

    @FXML
    private void RefreshBtnHandler() {
//        Stage primaryStage = (Stage) customTextField.getScene().getWindow();
//        customTextField.setText((String) primaryStage.getUserData());
        try {
            // Refresh the table data
            DB myObj = new DB();
            customTextField.setText(null);
            dataTableView.getItems().clear();
            ObservableList<User> updatedData = myObj.getDataFromTable();
            dataTableView.setItems(updatedData);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void switchToPrimary(ActionEvent event) {
        secondaryStage = new Stage();
        Stage primaryStage = (Stage) secondaryButton.getScene().getWindow();
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");

            secondaryStage.setOnCloseRequest((WindowEvent event1) -> {
                event1.consume();
                // Show an alert
                showAlert("Confirmation", "Are you sure you want to logout?", Alert.AlertType.CONFIRMATION);

            });
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialise(String[] credentials) {
        userTextField.setText(credentials[0]);
        DB myObj = new DB();
        ObservableList<User> data;
        try {
            data = myObj.getDataFromTable();
            TableColumn<User, String> user = new TableColumn<>("Username");
            user.setCellValueFactory(new PropertyValueFactory<>("user"));

            TableColumn<User, String> pass = new TableColumn<>("Password");
            pass.setCellValueFactory(new PropertyValueFactory<>("pass"));

            TableColumn<User, Timestamp> registeredTime = new TableColumn<>("Registered Time");
            registeredTime.setCellValueFactory(new PropertyValueFactory<>("registeredDateTime"));

            dataTableView.setItems(data);
            dataTableView.getColumns().addAll(user, pass, registeredTime); // Add the new column
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                secondaryStage.close();
            }
        });
    }

    @FXML
    private void btnDeleteAction(ActionEvent event) {
        // Get the selected item from the TableView
        User selectedUser = (User) dataTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Show a confirmation alert before proceeding with deletion
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete the selected user?");

            // Set custom buttons "Yes" and "No"
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

            confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
//                    System.out.println(selectedUser.getUser());
                    // Delete the corresponding data from the database
                    if (!"admin".equals(selectedUser.getUser())) {
                        DB myObj = new DB();
                        try {
                            myObj.deleteUser(selectedUser.getUser()); // Assuming you have a deleteUser method in  DB class
                            // Remove the selected item from the TableView
                            dataTableView.getItems().remove(selectedUser);
                        } catch (Exception ex) {
                            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
                            // Handle the exception appropriately
                        }

                    } else {
                        showAlert2("Cant delete admin", "Try another.", Alert.AlertType.ERROR);
                    }
                }
            });
//            selectedUser.getUser();
        } else {
            showAlert2("No User Selected", "Please select a user to delete.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert2(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void btnUpdateAction(ActionEvent event) {
        // Get the selected user from the table
        User selectedUser = (User) dataTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            try {
                if (!"admin".equals(selectedUser.getUser())) {

                    // Load the new UI for updating user
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("updateUser.fxml"));
                    Parent root = loader.load();

                    // Pass the selected user data to the new controller
                    UpdateUserController updateUserController = loader.getController();
                    updateUserController.initData(selectedUser);

                    // Create a new stage for the update user UI
                    Stage updateStage = new Stage();
                    Scene scene = new Scene(root);
                    updateStage.setScene(scene);
                    updateStage.setTitle("Update User");
                    updateStage.show();
                } else {
                    showAlert2("Cant update admin", "Try another.", Alert.AlertType.ERROR);
                }

            } catch (IOException ex) {
                Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            showAlert2("Error", "Please select a user to update.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnFileAddingActoin(ActionEvent event) {
        secondaryStage = new Stage();
        Stage primaryStage = (Stage) btnFileAdding.getScene().getWindow();
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("adminFileAdd.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 708, 623);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("User File Adding");
//            secondaryStage.setTitle("Admin File Adding");

            String[] credentials = {userTextField.getText()};
            UserDashboardController controller = loader.getController();
            controller.initialise(credentials);

            secondaryStage.setOnCloseRequest((WindowEvent event1) -> {
                event1.consume();
                switchToAdminDashboard();

            });
            secondaryStage.show();
            primaryStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchToAdminDashboard() {
        Stage primaryStage = (Stage) btnFileAdding.getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Secondary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 772, 565);

            // Set the primary stage's scene to the new scene
            primaryStage.setScene(scene);
            primaryStage.setTitle("User Dashboard");
            primaryStage.show();
            String[] credentials = {userTextField.getText()};
            SecondaryController controller = loader.getController();
            controller.initialise(credentials);
            // Close the secondary stage
            secondaryStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
