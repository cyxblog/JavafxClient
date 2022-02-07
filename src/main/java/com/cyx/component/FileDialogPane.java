package com.cyx.component;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FileDialogPane extends AnchorPane {

    public FileDialogPane(String url, String username, String fileName){
        this.setPrefWidth(320);
        this.setPrefHeight(240);
        this.setStyle("-fx-background-color: white");

        Label sendToLabel = new Label("发送给：");
        sendToLabel.setFont(new Font(14));
        sendToLabel.setStyle("-fx-text-fill: #999999");

        Label userNameLabel = new Label(username);
        userNameLabel.setFont(new Font(14));


        Label fileNameLabel = new Label(fileName);
        fileNameLabel.setWrapText(true);
        fileNameLabel.setCursor(Cursor.HAND);
        fileNameLabel.setFont(new Font(14));
        fileNameLabel.setPrefWidth(270);
        fileNameLabel.setPrefHeight(60);
        fileNameLabel.setAlignment(Pos.TOP_LEFT);
        fileNameLabel.setStyle("-fx-text-fill: #999999;-fx-background-color: #f7f7f7;" +
                "-fx-label-padding: 8px");

        ImageView profileIV = new ImageView(new Image(url));
        profileIV.setFitWidth(40);
        profileIV.setFitHeight(40);

        Button sendButton = new Button("发送");
        sendButton.setPrefWidth(60);
        sendButton.setPrefHeight(25);
        sendButton.setFont(new Font(14));
        sendButton.setCursor(Cursor.HAND);
        sendButton.setStyle("-fx-background-color: #07c160;-fx-text-fill: white");
        sendButton.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            if (show) {
                sendButton.setStyle("-fx-background-color: #129611;" +
                        "-fx-text-fill: white");
            } else {
                sendButton.setStyle("-fx-background-color: #07c160;" +
                        "-fx-text-fill: white;");
            }
        });

        Button cancelButton = new Button("取消");
        cancelButton.setPrefWidth(60);
        cancelButton.setPrefHeight(25);
        cancelButton.setCursor(Cursor.HAND);
        cancelButton.setStyle("-fx-background-color: white;" +
                "-fx-border-style: solid;" +
                "-fx-border-color: #d1d1d1");
        cancelButton.setFont(new Font(14));
        cancelButton.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            if (show) {
                cancelButton.setStyle("-fx-background-color: d1d1d1;" +
                        "-fx-border-style: solid;" +
                        "-fx-border-color: #d1d1d1");
            } else {
                cancelButton.setStyle("-fx-background-color: white;" +
                        "-fx-border-style: solid;" +
                        "-fx-border-color: #d1d1d1");
            }
        });

        Button closeButton = new Button();
        closeButton.setPrefHeight(25);
        closeButton.setPrefWidth(30);
        closeButton.setCursor(Cursor.HAND);
        closeButton.setStyle("-fx-background-image: url(/images/button/close.png); " +
                "-fx-background-size: 15px, 15px;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-color: transparent;" +
                "-fx-background-position: center;" +
                "-fx-border-style: none");

        closeButton.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            if (show) {
                closeButton.setStyle("-fx-background-image: url(/images/button/close.png); " +
                        "-fx-background-size: 15px, 15px;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-color: #fb7373;" +
                        "-fx-background-position: center;" +
                        "-fx-border-style: none");
            } else {
                closeButton.setStyle("-fx-background-image: url(/images/button/close.png); " +
                        "-fx-background-size: 15px, 15px;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-color: transparent;" +
                        "-fx-background-position: center;" +
                        "-fx-border-style: none");
            }
        });


        this.getChildren().addAll(sendToLabel, userNameLabel, fileNameLabel, profileIV, closeButton,sendButton, cancelButton);

        AnchorPane.setLeftAnchor(sendToLabel, 10.0);
        AnchorPane.setTopAnchor(sendToLabel, 10.0);

        AnchorPane.setLeftAnchor(profileIV, 25.0);
        AnchorPane.setTopAnchor(profileIV, 40.0);

        AnchorPane.setLeftAnchor(userNameLabel, 75.0);
        AnchorPane.setTopAnchor(userNameLabel, 55.0);

        AnchorPane.setLeftAnchor(fileNameLabel, 25.0);
        AnchorPane.setTopAnchor(fileNameLabel, 90.0);
        AnchorPane.setRightAnchor(fileNameLabel, 25.0);

        AnchorPane.setRightAnchor(closeButton, 0.0);
        AnchorPane.setTopAnchor(closeButton, 0.0);

        AnchorPane.setBottomAnchor(sendButton, 30.0);
        AnchorPane.setRightAnchor(sendButton, 105.0);

        AnchorPane.setBottomAnchor(cancelButton, 30.0);
        AnchorPane.setRightAnchor(cancelButton, 25.0);

    }
}
