/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.sql.Timestamp;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ntu-user
 */
public class User {

    private final SimpleStringProperty username;
    private final SimpleStringProperty pass;
    private final SimpleStringProperty registeredDateTime;

    User(String user, String pass, Timestamp registeredDateTime) {
        this.username = new SimpleStringProperty(user);
        this.pass = new SimpleStringProperty(pass);
        this.registeredDateTime = new SimpleStringProperty(registeredDateTime != null ? registeredDateTime.toString() : "");
    }

    public String getUser() {
        return username.get();
    }

    public void setUser(String user) {
        this.username.set(user);
    }

    public String getPass() {
        return pass.get();
    }

    public void setPass(String pass) {
        this.pass.set(pass);
    }

    public String getRegisteredDateTime() {
        return registeredDateTime.get();
    }

    public void setRegisteredDateTime(Timestamp registeredDateTime) {
        this.registeredDateTime.set(registeredDateTime != null ? registeredDateTime.toString() : "");
    }
}
//public class User {
//
//    private final SimpleStringProperty username;
//    private final SimpleStringProperty pass;
//    private SimpleStringProperty registeredDateTime;
//
//    User(String user, String pass) {
//        this.username = new SimpleStringProperty(user);
//        this.pass = new SimpleStringProperty(pass);
//        this.registeredDateTime = new SimpleStringProperty(registeredDateTime.toString());
//    }
//
//    public String getUser() {
//        return username.get();
//    }
//
//    public void setUser(String user) {
//        this.username.set(user);
//    }
//
//    public String getPass() {
//        return pass.get();
//    }
//
//    public void setPass(String pass) {
//        this.pass.set(pass);
//    }
//
//    public String getRegisteredDateTime() {
//        return registeredDateTime.get();
//    }
//
//    public void setRegisteredDateTime(Timestamp registeredDateTime) {
//        this.registeredDateTime.set(registeredDateTime.toString());
//    }
//
//}
