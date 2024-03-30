package com.mycompany.javafxapplication1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;

/**
 * JavaFX App
 */
public class App extends Application {

    Stage secondaryStage = null;

    @Override
    public void start(Stage stage) throws IOException {
        secondaryStage = new Stage();
        DB myObj = new DB();
        myObj.log("-------- App starting ------------");
//        myObj.log("\n---------- Drop table ----------");
//        try {
//            myObj.delTable(myObj.getTableName());
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//        }
        myObj.log("\n---------- Create db table ----------");
        try {
            myObj.createTable(myObj.getTableName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
         myObj.log("\n---------- Create db table 2 ----------");
        try {
            myObj.createTable2(myObj.getDataBaseFileTable());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
         myObj.log("\n---------- Create db table 3 ----------");
        try {
            myObj.createUserPermissionTable(myObj.getDataBasePermissionTable());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            myObj.log("\n---------- Generating Login ----------");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login View");
            secondaryStage.setOnCloseRequest((WindowEvent event1) -> {
                event1.consume();
                // Show an alert
                showAlert("Confirmation", "Are you sure you want to logout?", Alert.AlertType.CONFIRMATION);
            });
            secondaryStage.show();

        } catch (IOException e) {
//            e.printStackTrace();
            myObj.log("--------error : " + e);
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public void showAlert(String title, String content, Alert.AlertType alertType) {
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
                // User clicked "Yes," view primary view
                secondaryStage.close();
            } 
        });
    }

}

