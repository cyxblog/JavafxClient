package com.cyx.component;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class SOSPane extends AnchorPane {

    public SOSPane(){

        this.setPrefWidth(250);
        this.setPrefHeight(60);
        this.setStyle("-fx-background-color: #f5f5f5");

        ImageView sosLogo = new ImageView(new Image("/images/logo/sos.png"));
        sosLogo.setFitHeight(40);
        sosLogo.setFitWidth(40);

        ImageView arrowLogo = new ImageView(new Image("/images/logo/right_arrow.png"));
        arrowLogo.setFitHeight(25);
        arrowLogo.setFitWidth(25);

        Label sosLabel = new Label("搜一搜");
        sosLabel.setFont(new Font(14));

        Label sosContentLabel = new Label("文章、视频、朋友圈等");
        sosContentLabel.setFont(new Font(12));
        sosContentLabel.setStyle("-fx-text-fill: #adadad");

        this.getChildren().addAll(sosLogo, arrowLogo, sosLabel, sosContentLabel);

        AnchorPane.setTopAnchor(sosLogo, 10.0);
        AnchorPane.setLeftAnchor(sosLogo, 10.0);

        AnchorPane.setTopAnchor(sosLabel, 10.0);
        AnchorPane.setLeftAnchor(sosLabel, 60.0);

        AnchorPane.setBottomAnchor(sosContentLabel, 10.0);
        AnchorPane.setLeftAnchor(sosContentLabel, 60.0);

        AnchorPane.setTopAnchor(arrowLogo, 15.0);
        AnchorPane.setRightAnchor(arrowLogo, 10.0);

        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                this.setStyle("-fx-background-color: #d1d1d1");
            }else{
                this.setStyle("-fx-background-color: #f5f5f5");
            }
        });
    }
}
