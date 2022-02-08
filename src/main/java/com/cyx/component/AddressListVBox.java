package com.cyx.component;

import com.cyx.pojo.Friend;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;


public class AddressListVBox extends VBox {

    public AddressListVBox(List<FriendListItemPane> friendListItemPanes){
        AnchorPane managePane = new AnchorPane();
        managePane.setPrefHeight(35);
        managePane.setPrefWidth(225);
        managePane.setStyle("-fx-background-color: white;");
        managePane.setCursor(Cursor.HAND);

        ImageView manageLogo = new ImageView(new Image("/images/logo/friends.png"));
        manageLogo.setFitWidth(15);
        manageLogo.setFitHeight(15);

        Label manageLabel = new Label("通讯录管理");
        manageLabel.setFont(new Font(14));

        managePane.getChildren().addAll(manageLogo, manageLabel);

        AnchorPane.setLeftAnchor(manageLogo, 60.0);
        AnchorPane.setTopAnchor(manageLogo, 10.0);

        AnchorPane.setLeftAnchor(manageLabel, 80.0);
        AnchorPane.setTopAnchor(manageLabel, 7.0);

        AnchorPane firstPane = new AnchorPane();
        firstPane.getChildren().add(managePane);
        firstPane.setPrefWidth(this.getPrefWidth());
        firstPane.setPrefHeight(55);

        AnchorPane.setTopAnchor(managePane, 10.0);
        AnchorPane.setLeftAnchor(managePane, 10.0);

        AnchorPane newFriendLabelPane = new AnchorPane();
        newFriendLabelPane.setPrefWidth(250);
        newFriendLabelPane.setPrefHeight(25);

        Label newFriendTitle = new Label("新的朋友");
        newFriendTitle.setFont(new Font(12));
        newFriendTitle.setTextFill(Paint.valueOf("#999999"));
        newFriendLabelPane.getChildren().add(newFriendTitle);
        AnchorPane.setLeftAnchor(newFriendTitle,10.0);
        AnchorPane.setTopAnchor(newFriendTitle, 10.0);

        AnchorPane newFriendPane = new AnchorPane();
        newFriendPane.setPrefHeight(60);
        newFriendPane.setPrefWidth(250);

        ImageView newFriendLogo = new ImageView(new Image("/images/logo/new_friend.png"));
        newFriendLogo.setFitHeight(25);
        newFriendLogo.setFitWidth(25);
        StackPane logoPane = new StackPane();
        logoPane.setPrefWidth(40);
        logoPane.setPrefHeight(40);
        logoPane.getChildren().add(newFriendLogo);
        logoPane.setPadding(new Insets(5));
        logoPane.setStyle("-fx-background-color: #fa9d3b");

        Label newFriendLabel = new Label("新的朋友");
        newFriendLabel.setFont(new Font(14));

        newFriendPane.getChildren().addAll(logoPane, newFriendLabel);

        AnchorPane.setLeftAnchor(logoPane, 10.0);
        AnchorPane.setTopAnchor(logoPane, 12.5);

        AnchorPane.setLeftAnchor(newFriendLabel, 60.0);
        AnchorPane.setTopAnchor(newFriendLabel, 20.0);

        newFriendPane.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                newFriendPane.setStyle("-fx-background-color: #d1d1d1");
            }else{
                newFriendPane.setStyle("-fx-background-color: transparent");
            }
        });

        AnchorPane friendListPane = new AnchorPane();
        friendListPane.setPrefWidth(250);
        friendListPane.setPrefHeight(25);

        Label friendListTitle = new Label("好友列表");
        friendListTitle.setFont(new Font(12));
        friendListTitle.setTextFill(Paint.valueOf("#999999"));
        friendListPane.getChildren().add(friendListTitle);
        AnchorPane.setLeftAnchor(friendListTitle,10.0);
        AnchorPane.setTopAnchor(friendListTitle, 10.0);



        this.getChildren().addAll(firstPane,newFriendLabelPane, newFriendPane, friendListPane);
        this.getChildren().addAll(friendListItemPanes);

    }
}
