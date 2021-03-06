package com.cyx.component;

import com.cyx.constant.MessageType;
import com.cyx.pojo.Message;
import com.cyx.utils.FileUtils;
import com.sun.glass.ui.monocle.util.C;
import com.sun.javafx.scene.control.skin.ProgressIndicatorSkin;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;

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
            setImagePane(url, filePath, type);
            this.setVisible(false);
            this.setManaged(false);
        } else {
            setFilePane(url, fileName, fileLength, filePath, type);
        }

    }

    public void setTextPane(String url, String text, int type) {

        Image image;
        if (type == MessageType.FILE_MESSAGE_RECEIVE) {
            image = new Image(url);
        } else {
            image = new Image("file:" + url);
        }
        ImageView profileView = new ImageView(image);
        profileView.setFitWidth(35);
        profileView.setFitHeight(35);
        Label msgLabel = new Label(text);
        msgLabel.setFont(new Font(14));

        msgLabel.setPadding(new Insets(5, 10, 5, 10));
        msgLabel.setWrapText(true);
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

    private void setFilePane(String url, String fileName, long fileLength, String filePath, int type) {
        Image image;
        String metaInfo;
        if (type == MessageType.FILE_MESSAGE_RECEIVE) {
            image = new Image(url);
            metaInfo = "?????????   ????????????12???";
        } else {
            image = new Image("file:" + url);
            metaInfo = "?????????   ????????????5???";
        }
        ImageView profileView = new ImageView(image);
        profileView.setFitWidth(35);
        profileView.setFitHeight(35);
        ImageView fileImage = new ImageView(new Image(FileUtils.getFileTypeImagePath(fileName)));
        fileImage.setFitHeight(45);
        fileImage.setFitWidth(40);
        Label fileNameLabel = new Label(fileName);
        fileNameLabel.setFont(new Font(14));
        fileNameLabel.setMaxWidth(130);
        Label fileLengthLabel = new Label(getFileLengthStr(fileLength));
        fileLengthLabel.setFont(new Font(12));
        fileLengthLabel.setStyle("-fx-text-fill: #999999");
        Label metaLabel = new Label(metaInfo);
        metaLabel.setFont(new Font(12));
        metaLabel.setStyle("-fx-text-fill: #dadada");

        AnchorPane progressPane = getProgressPane();

        AnchorPane filePane = new AnchorPane();
        filePane.setStyle("-fx-background-color: white");
        filePane.setPrefWidth(220);
        filePane.setPrefHeight(90);
        filePane.setCursor(Cursor.HAND);
        filePane.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            if (show) {
                filePane.setStyle("-fx-background-color: #f7f7f7");
            } else {
                filePane.setStyle("-fx-background-color: white");
            }
        });

        filePane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                try {
                    File file = new File(filePath);
                    if(!file.exists()){
                        return;
                    }
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem copyItem = new MenuItem("??????");
                MenuItem forwardItem = new MenuItem("??????");
                MenuItem collectItem = new MenuItem("??????");
                MenuItem multiChoicesItem = new MenuItem("??????");
                MenuItem referenceItem = new MenuItem("??????");
                MenuItem anotherSaveItem = new MenuItem("?????????...");
                MenuItem showOnDirItem = new MenuItem("?????????????????????");
                MenuItem deleteItem = new MenuItem("??????");

                contextMenu.getItems().addAll(copyItem, forwardItem, collectItem, multiChoicesItem,
                        referenceItem, anotherSaveItem, showOnDirItem, deleteItem);

                showOnDirItem.setOnAction(event1 -> {

                    try {
                        File file = new File(filePath);
                        if(!file.exists()){
                            return;
                        }
                        File fileDir = file.getParentFile();
                        if(fileDir==null){
                            return;
                        }
                        Desktop.getDesktop().open(fileDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                contextMenu.show(filePane, event.getScreenX(), event.getScreenY());
            }
        });

        fileImage.setVisible(false);
        fileImage.setManaged(false);

        filePane.getChildren().addAll(progressPane, fileImage, fileNameLabel, fileLengthLabel, metaLabel);

        AnchorPane.setRightAnchor(progressPane, 25.0);
        AnchorPane.setTopAnchor(progressPane, 15.0);

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

    private AnchorPane getProgressPane() {
        Arc arc = new Arc();
        arc.setRadiusX(10);
        arc.setRadiusY(10);
        arc.setLength(0);
        arc.setType(ArcType.ROUND);
        arc.setStartAngle(90);
        arc.setFill(Paint.valueOf("#ffffff"));
        arc.setStroke(Paint.valueOf("transparent"));

        Circle base = new Circle();
        base.setRadius(10);
        base.setFill(Paint.valueOf("#414141"));
        base.setStroke(Paint.valueOf("transparent"));

        Circle circle = new Circle();
        circle.setRadius(8);
        circle.setFill(Paint.valueOf("#616161"));
        circle.setStroke(Paint.valueOf("transparent"));

        Line line1 = new Line(15, 0, 15, 5);
        line1.setStrokeWidth(2);
        line1.setStroke(Paint.valueOf("#ffffff"));
        Line line2 = new Line(15, 0, 15, 5);
        line2.setStrokeWidth(2);
        line2.setStroke(Paint.valueOf("#ffffff"));

        HBox lineBox = new HBox();
        lineBox.setSpacing(2);
        lineBox.getChildren().addAll(line1, line2);

        ImageView baseFileIV = new ImageView(new Image("/images/file/base_file.png"));
        baseFileIV.setFitHeight(45);
        baseFileIV.setFitWidth(40);

        AnchorPane progressPane = new AnchorPane();
        progressPane.setPrefWidth(40);
        progressPane.setPrefHeight(45);
        progressPane.setStyle("-fx-background-color: transparent");

        progressPane.getChildren().addAll(baseFileIV, base, arc, circle, lineBox);

        AnchorPane.setLeftAnchor(baseFileIV, 0.0);
        AnchorPane.setTopAnchor(baseFileIV, 0.0);
        AnchorPane.setLeftAnchor(base, 9.5);
        AnchorPane.setTopAnchor(base, 14.3);
        AnchorPane.setLeftAnchor(arc, 9.0);
        AnchorPane.setTopAnchor(arc, 14.0);
        AnchorPane.setLeftAnchor(circle, 11.6);
        AnchorPane.setTopAnchor(circle, 16.5);
        AnchorPane.setLeftAnchor(lineBox, 17.8);
        AnchorPane.setTopAnchor(lineBox, 21.5);
        return progressPane;
    }

    private void setImagePane(String url, String filePath, int type) {

        Image image;
        if (type == MessageType.IMAGE_MESSAGE_RECEIVE) {
            image = new Image(url);
        } else {
            image = new Image("file:" + url);
        }
        ImageView profileView = new ImageView(image);
        profileView.setFitWidth(35);
        profileView.setFitHeight(35);

        AnchorPane imagePane = new AnchorPane();
        imagePane.setStyle("-fx-background-color: white");

        ImageView imageView = new ImageView(new Image("file:" + filePath));
        imageView.setCursor(Cursor.HAND);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(220);
        imageView.setFitWidth(220);

        imagePane.getChildren().add(imageView);
        AnchorPane.setLeftAnchor(imageView, 0.0);
        AnchorPane.setTopAnchor(imageView, 0.0);
        AnchorPane.setRightAnchor(imageView, 0.0);
        AnchorPane.setBottomAnchor(imageView, 0.0);

        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                try {
                    File file = new File(filePath);
                    if(!file.exists()){
                        return;
                    }
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem copyItem = new MenuItem("??????");
                MenuItem editItem = new MenuItem("??????");
                MenuItem forwardItem = new MenuItem("??????");
                MenuItem collectItem = new MenuItem("??????");
                MenuItem multiChoicesItem = new MenuItem("??????");
                MenuItem referenceItem = new MenuItem("??????");
                MenuItem anotherSaveItem = new MenuItem("?????????...");
                MenuItem showOnDirItem = new MenuItem("?????????????????????");
                MenuItem deleteItem = new MenuItem("??????");

                contextMenu.getItems().addAll(copyItem, editItem, forwardItem, collectItem, multiChoicesItem,
                        referenceItem, anotherSaveItem, showOnDirItem, deleteItem);

                showOnDirItem.setOnAction(event1 -> {

                    try {
                        File file = new File(filePath);
                        if(!file.exists()){
                            return;
                        }
                        File fileDir = file.getParentFile();
                        Desktop.getDesktop().open(fileDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                contextMenu.show(imageView, event.getScreenX(), event.getScreenY());
            }
        });

        this.getChildren().addAll(imagePane, profileView);

        if (type == MessageType.IMAGE_MESSAGE_RECEIVE) {

            AnchorPane.setLeftAnchor(profileView, 25.0);
            AnchorPane.setTopAnchor(profileView, 10.0);
            AnchorPane.setLeftAnchor(imagePane, 70.0);
            AnchorPane.setTopAnchor(imagePane, 10.0);
        } else {
            AnchorPane.setRightAnchor(profileView, 25.0);
            AnchorPane.setTopAnchor(profileView, 10.0);
            AnchorPane.setRightAnchor(imagePane, 70.0);
            AnchorPane.setTopAnchor(imagePane, 10.0);
        }


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