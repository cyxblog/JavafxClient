package com.cyx.component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;


public class LoginPane extends AnchorPane {

    public LoginPane(){
        this.setPrefHeight(300);
        this.setPrefWidth(280);

        ImageView logo = new ImageView(new Image("/images/logo/logo.png"));
        logo.setFitWidth(80);
        logo.setFitHeight(80);

        Label usernameLabel = new Label("账号");
        usernameLabel.setFont(Font.font(14));
        TextField usernameField = new TextField();
        usernameField.setFont(Font.font(14));
        usernameField.setCursor(Cursor.HAND);

        Label passwordLabel = new Label("密码");
        passwordLabel.setFont(Font.font(14));
        PasswordField passwordField = new PasswordField();
        passwordField.setFont(Font.font(14));
        passwordField.setCursor(Cursor.HAND);

        Button registerButton = new Button("注册");
        registerButton.setFont(Font.font(14));
        registerButton.setPrefWidth(80);
        registerButton.setStyle("-fx-background-color: #e2e2e2");
        registerButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                registerButton.setStyle("-fx-background-color: #d6d6d6");
            }else{
                registerButton.setStyle("-fx-background-color: #e2e2e2");
            }
        });
        registerButton.setCursor(Cursor.HAND);

        Button affirmButton = new Button("确定");
        affirmButton.setTextFill(Paint.valueOf("#ffffff"));
        affirmButton.setFont(Font.font(14));
        affirmButton.setPrefWidth(80);
        affirmButton.setStyle("-fx-background-color: #07c160");
        affirmButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                affirmButton.setStyle("-fx-background-color: #129611");
            }else{
                affirmButton.setStyle("-fx-background-color: #07c160");
            }
        });
        affirmButton.setCursor(Cursor.HAND);

        this.getChildren().addAll(usernameField, passwordField, registerButton, affirmButton,
                usernameLabel, passwordLabel, logo);

        AnchorPane.setLeftAnchor(logo, 100.0);
        AnchorPane.setTopAnchor(logo, 0.0);

        AnchorPane.setLeftAnchor(usernameLabel, 30.0);
        AnchorPane.setBottomAnchor(usernameLabel, 135.0);

        AnchorPane.setLeftAnchor(usernameField, 70.0);
        AnchorPane.setBottomAnchor(usernameField, 130.0);
        AnchorPane.setRightAnchor(usernameField, 30.0);

        AnchorPane.setLeftAnchor(passwordLabel, 30.0);
        AnchorPane.setBottomAnchor(passwordLabel, 85.0);

        AnchorPane.setLeftAnchor(passwordField, 70.0);
        AnchorPane.setBottomAnchor(passwordField, 80.0);
        AnchorPane.setRightAnchor(passwordField, 30.0);

        AnchorPane.setLeftAnchor(registerButton, 70.0);
        AnchorPane.setBottomAnchor(registerButton, 30.0);

        AnchorPane.setRightAnchor(affirmButton, 30.0);
        AnchorPane.setBottomAnchor(affirmButton, 30.0);
    }
}
