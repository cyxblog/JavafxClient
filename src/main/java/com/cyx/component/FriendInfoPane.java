package com.cyx.component;

import com.cyx.pojo.Friend;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FriendInfoPane extends AnchorPane {
    public FriendInfoPane(Friend friend){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/FriendInfoView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
