package com.cyx.component;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class RegisterPane extends AnchorPane {

    public RegisterPane(Stage loginStage){
        this.setPrefHeight(300);
        this.setPrefWidth(280);

        ImageView logo = new ImageView(new Image("/images/logo/logo.png"));
        logo.setFitWidth(80);
        logo.setFitHeight(80);

        Label profileLabel = new Label("头像");
        profileLabel.setFont(Font.font(14));
        TextField profileField = new TextField();
        profileField.setFont(Font.font(14));
        profileField.setCursor(Cursor.HAND);
        Button selectButton = new Button("选择");
        selectButton.setFont(Font.font(12));
        selectButton.setPrefHeight(30);
        selectButton.setPrefWidth(40);
        selectButton.setStyle("-fx-background-color: #e2e2e2");
        selectButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                selectButton.setStyle("-fx-background-color: #d6d6d6");
            }else{
                selectButton.setStyle("-fx-background-color: #e2e2e2");
            }
        });
        selectButton.setCursor(Cursor.HAND);
        selectButton.setOnMouseClicked(event -> {
            if(event.getButton()== MouseButton.PRIMARY){
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("图片", "*.png;*.jpeg;*.jpg"));
                File image = fileChooser.showOpenDialog(loginStage);
                if(image!=null){
                    profileField.setText(image.getAbsolutePath());
                    logo.setImage(new Image("file:"+image.getAbsolutePath()));
                }
            }
        });

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

        Button backButton = new Button("返回");
        backButton.setFont(Font.font(14));
        backButton.setPrefWidth(80);
        backButton.setStyle("-fx-background-color: #e2e2e2");
        backButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                backButton.setStyle("-fx-background-color: #d6d6d6");
            }else{
                backButton.setStyle("-fx-background-color: #e2e2e2");
            }
        });
        backButton.setCursor(Cursor.HAND);

        Button registerButton = new Button("注册");
        registerButton.setFont(Font.font(14));
        registerButton.setTextFill(Paint.valueOf("#ffffff"));
        registerButton.setPrefWidth(80);
        registerButton.setStyle("-fx-background-color: #07c160");
        registerButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                registerButton.setStyle("-fx-background-color: #129611");
            }else{
                registerButton.setStyle("-fx-background-color: #07c160");
            }
        });
        registerButton.setCursor(Cursor.HAND);

        this.getChildren().addAll(profileField, usernameField, passwordField, backButton, registerButton,
                profileLabel, usernameLabel, passwordLabel, logo,selectButton);

        AnchorPane.setLeftAnchor(logo, 100.0);
        AnchorPane.setTopAnchor(logo, 0.0);

        AnchorPane.setLeftAnchor(profileLabel, 30.0);
        AnchorPane.setBottomAnchor(profileLabel, 185.0);

        AnchorPane.setLeftAnchor(profileField, 70.0);
        AnchorPane.setRightAnchor(profileField, 80.0);
        AnchorPane.setBottomAnchor(profileField, 180.0);

        AnchorPane.setRightAnchor(selectButton, 30.0);
        AnchorPane.setBottomAnchor(selectButton, 180.0);

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

        AnchorPane.setLeftAnchor(backButton, 70.0);
        AnchorPane.setBottomAnchor(backButton, 30.0);

        AnchorPane.setRightAnchor(registerButton, 30.0);
        AnchorPane.setBottomAnchor(registerButton, 30.0);
    }
}
