/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author NC
 */
public class UserDashboardController implements Initializable {

    File selectedFile = null;
    @FXML
    private TextField txtNameDisplay;
    @FXML
    private Button selectBtn;
    @FXML
    private Text fileText;
    @FXML
    private TableView<UserFile> userFilesTableView;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUplode;
    @FXML
    private Button btnViewOther;
    @FXML
    private Button btnPermision;

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

    public void initialise(String[] credentials) {
        txtNameDisplay.setText(credentials[0]);
        DB myObj = new DB();
        ObservableList<UserFile> userFileData;
        try {
            userFileData = myObj.getUserFiles(credentials[0]);

            if (!userFileData.isEmpty()) {
                // Display user files data
                TableColumn<UserFile, Integer> fileIdColumn = new TableColumn<>("File ID");
                fileIdColumn.setCellValueFactory(new PropertyValueFactory<>("fileId")); // Updated for fileId

                TableColumn<UserFile, String> filePathColumn = new TableColumn<>("File Path");
                filePathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));

                TableColumn<UserFile, String> fileNameColumn = new TableColumn<>("File Name");
                fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));

                TableColumn<UserFile, String> fileTypeColumn = new TableColumn<>("File Type");
                fileTypeColumn.setCellValueFactory(new PropertyValueFactory<>("fileType"));

                TableColumn<UserFile, Long> fileSizeColumn = new TableColumn<>("File Size");
                fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));

                TableColumn<UserFile, Timestamp> uploadedTimeColumn = new TableColumn<>("Uploaded Time");
                uploadedTimeColumn.setCellValueFactory(new PropertyValueFactory<>("uploadedTime"));

                userFilesTableView.setItems(userFileData);
                userFilesTableView.getColumns().addAll(fileIdColumn, filePathColumn, fileNameColumn, fileTypeColumn, fileSizeColumn, uploadedTimeColumn);
            } else {
                System.out.println("No data available");
                // You can customize this part based on your application's requirements.
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void selectBtnHandler(ActionEvent event) {
        Stage primaryStage = (Stage) selectBtn.getScene().getWindow();
//        primaryStage.setTitle("Select a File");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            try {
                fileText.setText((String) selectedFile.getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(UserDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void btnDeleteAction(ActionEvent event) {
        // Get the selected item from the TableView
        UserFile selectedFileToDelete = userFilesTableView.getSelectionModel().getSelectedItem();

        if (selectedFileToDelete != null) {
            // Show a confirmation alert before proceeding with deletion
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete the selected file?");

            // Set custom buttons "Yes" and "No"
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

            confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    // Perform the deletion operation
                    DB myObj = new DB();
                    try {
                        myObj.deleteFile(selectedFileToDelete); // Assuming you have a deleteFile method in DB class
                        // Remove the selected item from the TableView
                        userFilesTableView.getItems().remove(selectedFileToDelete);
                    } catch (Exception ex) {
                        Logger.getLogger(UserDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                        // Handle the exception appropriately
                    }
                }
            });
        } else {
            showAlert("No File Selected", "Please select a file to delete.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void btnRefreshAction() {
        selectedFile = null;
        fileText.setText(null);
        refreshTable();
    }

    private void refreshTable() {
        // Get the current user's name
        String username = txtNameDisplay.getText();

        // Get the updated user files data
        DB myObj = new DB();
        ObservableList<UserFile> updatedUserFileData;
        try {
            updatedUserFileData = myObj.getUserFiles(username);

            // Clear existing columns and items
            userFilesTableView.getColumns().clear();
            userFilesTableView.getItems().clear();

            if (!updatedUserFileData.isEmpty()) {
                // Display updated user files data
                TableColumn<UserFile, Integer> fileIdColumn = new TableColumn<>("File ID");
                fileIdColumn.setCellValueFactory(new PropertyValueFactory<>("fileId")); // Updated for fileId

                TableColumn<UserFile, String> filePathColumn = new TableColumn<>("File Path");
                filePathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));

                TableColumn<UserFile, String> fileNameColumn = new TableColumn<>("File Name");
                fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));

                TableColumn<UserFile, String> fileTypeColumn = new TableColumn<>("File Type");
                fileTypeColumn.setCellValueFactory(new PropertyValueFactory<>("fileType"));

                TableColumn<UserFile, Long> fileSizeColumn = new TableColumn<>("File Size");
                fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));

                TableColumn<UserFile, Timestamp> uploadedTimeColumn = new TableColumn<>("Uploaded Time");
                uploadedTimeColumn.setCellValueFactory(new PropertyValueFactory<>("uploadedTime"));

                userFilesTableView.setItems(updatedUserFileData);
                userFilesTableView.getColumns().addAll(fileIdColumn, filePathColumn, fileNameColumn, fileTypeColumn, fileSizeColumn, uploadedTimeColumn);
            } else {
                System.out.println("No data available");
                // You can customize this part based on your application's requirements.
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnUplodeAction(ActionEvent event) {
        if (selectedFile != null && (fileText.getText() != null || !"".equals(fileText.getText()))) {
            // Assuming you have a method in DB class to handle file upload
            try {
                DB myObj = new DB();
                String username = txtNameDisplay.getText(); // Get the username
                String filePath = fileText.getText();

                // Call a method in your DB class to upload the file
                myObj.addFilesToDB(username, filePath);
                dialogueSuccess("File Uploding Successful.!!!", "Successful!");
                // Refresh the table to show the updated data
                btnRefreshAction();
            } catch (ClassNotFoundException | InvalidKeySpecException ex) {
                Logger.getLogger(UserDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                // Handle the exception appropriately
            }
        } else {
            // Handle the case when no file is selected
            showAlert("No File Selected", "Please select a file to upload.", Alert.AlertType.WARNING);
        }
    }

    private void dialogueSuccess(String headerMsg, String contentMsg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);

        Optional<ButtonType> result = alert.showAndWait();
    }
    Stage secondaryStage = null;
    Stage primaryStage = null;

    @FXML
    private void btnViewOtherAction(ActionEvent event) {
        try {
            secondaryStage = new Stage();
            primaryStage = (Stage) btnViewOther.getScene().getWindow();
            String[] credentials = {txtNameDisplay.getText()};
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("secondary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 772, 527);
            secondaryStage.setScene(scene);
            SecondaryController controller = loader.getController();
            controller.initialise(credentials);
            secondaryStage.setTitle("All Users View");
            String msg = "some data sent from Primary Controller";
            secondaryStage.setUserData(msg);
            secondaryStage.setOnCloseRequest((WindowEvent event1) -> {
                event1.consume();
                switchToPrimary();
            });
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
            Logger.getLogger(UserDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void switchToPrimary() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("userDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 708, 623);

            String[] credentials = {txtNameDisplay.getText()};
            UserDashboardController controller = loader.getController();
            controller.initialise(credentials);

            // Set the primary stage's scene to the new scene
            primaryStage.setScene(scene);
            primaryStage.setTitle("User Dashboard");
            primaryStage.show();
            // Close the secondary stage
            secondaryStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnPermisionAction(ActionEvent event) {
        UserFile selectedFile1 = userFilesTableView.getSelectionModel().getSelectedItem();

        if (selectedFile1 != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("UserPermission.fxml"));
                Parent root = loader.load();

                try {
                    // Pass selected row data to UserPermissionController
                    UserPermissionController controller = loader.getController();
                    DB db = new DB();
                    int author_id = db.getUserId(txtNameDisplay.getText());
                    controller.initialize(author_id, selectedFile1.getFileId(), selectedFile1.getFileName(), selectedFile1.getFilePath());
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(UserDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }

                Scene scene = new Scene(root);
                Stage permissionStage = new Stage();
                permissionStage.setScene(scene);
                permissionStage.setTitle("User Permission Management");
                permissionStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            showAlert("No File Selected", "Please select a file to grant permissions.", Alert.AlertType.WARNING);
        }

    }
}
