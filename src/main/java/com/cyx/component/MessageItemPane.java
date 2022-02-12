package com.cyx.component;

import com.cyx.constant.MessageType;
import com.cyx.pojo.Message;
import com.cyx.utils.FileUtils;
import com.sun.glass.ui.monocle.util.C;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
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
        } else {
            setFilePane(url, fileName, fileLength, filePath, type);
        }

    }

    public void setTextPane(String url, String text, int type) {


        ImageView profileView = new ImageView(new Image("file:" + url));
        profileView.setFitWidth(35);
        profileView.setFitHeight(35);
        Label msgLabel = new Label(text);
        msgLabel.setFont(new Font(14));

        msgLabel.setPadding(new Insets(5, 10, 5, 10));
        msgLabel.setWrapText(true);
//        msgLabel.setMaxWidth(220);

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
        ImageView profileView = new ImageView(new Image("file:" + url));
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
        Label metaLabel = new Label("微信电脑版");
        metaLabel.setFont(new Font(12));
        metaLabel.setStyle("-fx-text-fill: #dadada");

        AnchorPane filePane = new AnchorPane();
        filePane.setStyle("-fx-background-color: white");
        filePane.setPrefWidth(220);
        filePane.setPrefHeight(90);
        filePane.setCursor(Cursor.HAND);
        filePane.getChildren().addAll(fileImage, fileNameLabel, fileLengthLabel, metaLabel);
        filePane.hoverProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean show) -> {
            if (show) {
                filePane.setStyle("-fx-background-color: #f7f7f7");
            } else {
                filePane.setStyle("-fx-background-color: white");
            }
        });

        filePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    try {
                        Desktop.getDesktop().open(new File(filePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) {
                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem copyItem = new MenuItem("复制");
                    MenuItem forwardItem = new MenuItem("转发");
                    MenuItem collectItem = new MenuItem("收藏");
                    MenuItem multiChoicesItem = new MenuItem("多选");
                    MenuItem referenceItem = new MenuItem("引用");
                    MenuItem anotherSaveItem = new MenuItem("另存为...");
                    MenuItem showOnDirItem = new MenuItem("在文件夹中显示");
                    MenuItem deleteItem = new MenuItem("删除");

                    contextMenu.getItems().addAll(copyItem, forwardItem, collectItem, multiChoicesItem,
                            referenceItem, anotherSaveItem, showOnDirItem, deleteItem);

                    showOnDirItem.setOnAction(event1 -> {

                        try {
                            File fileDir = new File(filePath).getParentFile();
                            Desktop.getDesktop().open(fileDir);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    contextMenu.show(filePane, event.getScreenX(), event.getScreenY());
                }
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

    private void setImagePane(String url, String filePath, int type) {

        ImageView profileView = new ImageView(new Image("file:" + url));
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
                    Desktop.getDesktop().open(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 1) {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem copyItem = new MenuItem("复制");
                MenuItem editItem = new MenuItem("编辑");
                MenuItem forwardItem = new MenuItem("转发");
                MenuItem collectItem = new MenuItem("收藏");
                MenuItem multiChoicesItem = new MenuItem("多选");
                MenuItem referenceItem = new MenuItem("引用");
                MenuItem anotherSaveItem = new MenuItem("另存为...");
                MenuItem showOnDirItem = new MenuItem("在文件夹中显示");
                MenuItem deleteItem = new MenuItem("删除");

                contextMenu.getItems().addAll(copyItem, editItem, forwardItem, collectItem, multiChoicesItem,
                        referenceItem, anotherSaveItem, showOnDirItem, deleteItem);

                showOnDirItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        try {
                            File fileDir = new File(filePath).getParentFile();
                            Desktop.getDesktop().open(fileDir);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                contextMenu.show(imageView, event.getScreenX(), event.getScreenY());
            }
        });

        this.getChildren().addAll(imagePane, profileView);

        if (type == MessageType.IMAGE_MESSAGE_RECEIVE) {

            AnchorPane.setLeftAnchor(profileView, 25.0);
            AnchorPane.setTopAnchor(profileView, 10.0);
            AnchorPane.setLeftAnchor(imageView, 70.0);
            AnchorPane.setTopAnchor(imageView, 10.0);
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