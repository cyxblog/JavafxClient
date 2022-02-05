package com.cyx.component;

import com.cyx.pojo.ChatListItem;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.io.IOException;

public class ChatListItemPane extends AnchorPane {

    public ChatListItemPane(ChatListItem item) {
        String url = item.getUrl();
        String username = item.getUsername();
        String msg = item.getMessage();
        String date = item.getDate();
        this.setChatListItemPane(url, username, msg, date);
    }

    public ChatListItemPane(String url, String username, String msg, String date) {
        this.setChatListItemPane(url, username, msg, date);
    }

    private void setChatListItemPane(String url, String username, String msg, String date) {
        this.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            if (show) {
                this.setStyle("-fx-background-color: #d6d6d6");
            } else {
                this.setStyle("-fx-background-color: #f5f5f5");
            }
        });

        this.setPrefHeight(60);
        ImageView imageView = new ImageView(new Image(url));
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        Label usernameLabel = new Label(username);
        usernameLabel.setFont(new Font(14));
        Label msgLabel = new Label(msg);
        msgLabel.setStyle("-fx-text-fill: #b2b2b2");
        msgLabel.setFont(new Font(12));
        Label dateLabel = new Label(date);
        dateLabel.setFont(new Font(12));
        dateLabel.setStyle("-fx-text-fill: #b2b2b2");
        this.getChildren().addAll(imageView, usernameLabel, msgLabel, dateLabel);
        AnchorPane.setLeftAnchor(imageView, 15.0);
        AnchorPane.setTopAnchor(imageView, 10.0);
        AnchorPane.setLeftAnchor(usernameLabel, 65.0);
        AnchorPane.setTopAnchor(usernameLabel, 10.0);
        AnchorPane.setLeftAnchor(msgLabel, 65.0);
        AnchorPane.setBottomAnchor(msgLabel, 10.0);
        AnchorPane.setRightAnchor(dateLabel, 20.0);
        AnchorPane.setTopAnchor(dateLabel, 10.0);
    }


}
