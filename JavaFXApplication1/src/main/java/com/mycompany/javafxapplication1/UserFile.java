/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.sql.Timestamp;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author NC
 */
public class UserFile {

    private final SimpleIntegerProperty fileId;
    private final SimpleStringProperty filePath;
    private final SimpleStringProperty fileName;
    private final SimpleStringProperty fileType;
    private final SimpleLongProperty fileSize;
    private final SimpleObjectProperty<Timestamp> uploadedTime;

    public UserFile(int fileId, String filePath, String fileName, String fileType, long fileSize, Timestamp uploadedTime) {
        this.fileId = new SimpleIntegerProperty(fileId);
        this.filePath = new SimpleStringProperty(filePath);
        this.fileName = new SimpleStringProperty(fileName);
        this.fileType = new SimpleStringProperty(fileType);
        this.fileSize = new SimpleLongProperty(fileSize);
        this.uploadedTime = new SimpleObjectProperty<>(uploadedTime);
    }

    public String getFilePath() {
        return filePath.get();
    }

    public String getFileName() {
        return fileName.get();
    }

    public String getFileType() {
        return fileType.get();
    }

    public long getFileSize() {
        return fileSize.get();
    }

    public Timestamp getUploadedTime() {
        return uploadedTime.get();
    }

    public int getFileId() {
        return fileId.get();
    }
}
