package com.cyx.component;

import com.cyx.constant.MessageType;
import com.cyx.pojo.Message;
import com.cyx.utils.FileUtils;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.text.Font;

import java.io.File;

public class MessageItemPane extends AnchorPane {

    public MessageItemPane(Message message) {
        int type = message.getType();
        String text = message.getText();
        String fileType = message.getFileType();
        String fileName = message.getFileName();
        long fileLength = message.getFileLength();
        String filePath = message.getFilePath();
        String url = message.getUrl();
        if (type == MessageType.TEXT_MESSAGE_SEND || type == MessageType.TEXT_MESSAGE_RECEIVE) {
            setTextPane(url, text, type);
        } else if (type == MessageType.IMAGE_MESSAGE_SEND || type == MessageType.IMAGE_MESSAGE_RECEIVE) {
            setImagePane(url, fileType, fileName, fileLength, filePath, type);
        } else {
            setFilePane(url, fileName, fileLength, type);
        }

    }

    public void setTextPane(String url, String text, int type) {


        ImageView profileView = new ImageView(new Image(url));
        profileView.setFitWidth(35);
        profileView.setFitHeight(35);
        Label msgLabel = new Label(text);
        msgLabel.setFont(new Font(14));

        msgLabel.setPadding(new Insets(5, 10, 5, 10));
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(220);

        this.getChildren().addAll(profileView, msgLabel);

        if (type == MessageType.TEXT_MESSAGE_RECEIVE) {
            msgLabel.setStyle("-fx-background-color: white");
            AnchorPane.setLeftAnchor(profileView, 25.0);
            AnchorPane.setTopAnchor(profileView, 10.0);
            AnchorPane.setLeftAnchor(msgLabel, 70.0);
            AnchorPane.setTopAnchor(msgLabel, 10.0);
        } else {
            msgLabel.setStyle("-fx-background-color: #9eea6a");
            AnchorPane.setRightAnchor(profileView, 25.0);
            AnchorPane.setTopAnchor(profileView, 10.0);
            AnchorPane.setRightAnchor(msgLabel, 70.0);
            AnchorPane.setTopAnchor(msgLabel, 10.0);
        }


    }

    private void setFilePane(String url, String fileName, long fileLength, int type) {
        ImageView profileView = new ImageView(new Image(url));
        profileView.setFitWidth(35);
        profileView.setFitHeight(35);
        System.out.println("url ==>" + url);
        System.out.println("filePath==>" + FileUtils.getFileTypeImagePath(fileName));
        File file = new File(FileUtils.getFileTypeImagePath(fileName));
        ImageView fileImage = new ImageView(new Image(FileUtils.getFileTypeImagePath(fileName)));
        fileImage.setFitHeight(45);
        fileImage.setFitWidth(40);
        Label fileNameLabel = new Label(fileName);
        fileNameLabel.setFont(new Font(14));
        fileNameLabel.setMaxWidth(130);
        Label fileLengthLabel = new Label(getFileLengthStr(fileLength));
        fileLengthLabel.setFont(new Font(12));
        fileLengthLabel.setStyle("-fx-text-fill: #999999");
        Label metaLabel = new Label("微信电脑版");
        metaLabel.setFont(new Font(12));
        metaLabel.setStyle("-fx-text-fill: #dadada");

        AnchorPane filePane = new AnchorPane();
        filePane.setStyle("-fx-background-color: white");
        filePane.setPrefWidth(220);
        filePane.setPrefHeight(90);
        filePane.getChildren().addAll(fileImage, fileNameLabel, fileLengthLabel, metaLabel);
        filePane.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            if (show) {
                filePane.setStyle("-fx-background-color: #f7f7f7");
            } else {
                filePane.setStyle("-fx-background-color: white");
            }
        });

        AnchorPane.setLeftAnchor(fileNameLabel, 10.0);
        AnchorPane.setTopAnchor(fileNameLabel, 5.0);

        AnchorPane.setLeftAnchor(fileLengthLabel, 10.0);
        AnchorPane.setTopAnchor(fileLengthLabel, 30.0);

        AnchorPane.setLeftAnchor(metaLabel, 10.0);
        AnchorPane.setBottomAnchor(metaLabel, 5.0);

        AnchorPane.setRightAnchor(fileImage, 25.0);
        AnchorPane.setTopAnchor(fileImage, 15.0);

        this.getChildren().addAll(filePane, profileView);

        if (type == MessageType.FILE_MESSAGE_RECEIVE) {

            AnchorPane.setLeftAnchor(profileView, 25.0);
            AnchorPane.setTopAnchor(profileView, 10.0);
            AnchorPane.setLeftAnchor(filePane, 70.0);
            AnchorPane.setTopAnchor(filePane, 10.0);
        } else {
            AnchorPane.setRightAnchor(profileView, 25.0);
            AnchorPane.setTopAnchor(profileView, 10.0);
            AnchorPane.setRightAnchor(filePane, 70.0);
            AnchorPane.setTopAnchor(filePane, 10.0);
        }

    }

    private void setImagePane(String url, String fileType, String fileName, long fileLength, String filePath, int type) {

    }

    private String getFileLengthStr(long fileLength) {
        String fileLengthStr;
        if (fileLength < 1024) {
            fileLengthStr = fileLength + "B";
        } else if (fileLength < 1024 * 1024) {
            fileLengthStr = fileLength / 1024 + "K";
        } else if (fileLength < 1024 * 1024 * 1024) {
            fileLengthStr = fileLength / 1024 / 1024 + "M";
        } else {
            fileLengthStr = fileLength / 1024 / 1024 / 1024 + "G";
        }

        return fileLengthStr;
    }
}