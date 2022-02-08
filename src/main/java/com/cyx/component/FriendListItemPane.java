package com.cyx.component;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;


public class FriendListItemPane extends AnchorPane {

    public FriendListItemPane(String url, String username){
        ImageView profile = new ImageView(new Image(url));
        profile.setFitWidth(40);
        profile.setFitHeight(40);

        Label usernameLabel = new Label(username);
        usernameLabel.setFont(new Font(14));

        this.setPrefHeight(60);
        this.setWidth(250);

        this.getChildren().addAll(profile, usernameLabel);

        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                this.setStyle("-fx-background-color: #d1d1d1");
            }else{
                this.setStyle("-fx-background-color: transparent");
            }
        });

        AnchorPane.setTopAnchor(profile, 10.0);
        AnchorPane.setLeftAnchor(profile, 10.0);

        AnchorPane.setTopAnchor(usernameLabel, 20.0);
        AnchorPane.setLeftAnchor(usernameLabel, 60.0);

    }
}
