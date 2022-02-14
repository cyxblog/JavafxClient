package com.cyx.component;

import com.cyx.constant.LoginState;
import com.cyx.pojo.User;
import com.cyx.service.UserService;
import com.cyx.service.UserServiceImpl;
import de.felixroske.jfxsupport.FXMLView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SettingsPane extends AnchorPane {

    private Toggle tempToggle;

    private final Stage mainStage;

    private final User user;

    private final UserServiceImpl userService;

    public SettingsPane(Stage mainStage, User user, UserServiceImpl userService) {

        this.mainStage = mainStage;
        this.user = user;
        this.userService = userService;
        initView();

    }

    private void initView() {
        Label settingsLabel = new Label("设置");
        settingsLabel.setFont(Font.font(14));
        settingsLabel.setTextFill(Paint.valueOf("#999999"));

        Button minimizeButton = new Button();
        setWindowButton(minimizeButton, "#e2e2e2", "/images/button/shrink.png");

        Button closeButton = new Button();
        setWindowButton(closeButton, "#fb7373", "/images/button/close.png");

        ToggleGroup settingsGroup = new ToggleGroup();

        ToggleButton accountSettingsButton = new ToggleButton("账号设置");
        setToggleButton(settingsGroup, accountSettingsButton);
        accountSettingsButton.setUserData("account");

        ToggleButton infoNotifyButton = new ToggleButton("消息通知");
        setToggleButton(settingsGroup, infoNotifyButton);

        ToggleButton commonSettingsButton = new ToggleButton("通用设置");
        setToggleButton(settingsGroup, commonSettingsButton);

        ToggleButton fileManagedButton = new ToggleButton("文件管理");
        setToggleButton(settingsGroup, fileManagedButton);
        fileManagedButton.setUserData("file");

        ToggleButton shortcutKeyButton = new ToggleButton("快捷键");
        setToggleButton(settingsGroup, shortcutKeyButton);

        ToggleButton aboutButton = new ToggleButton("关于微信");
        aboutButton.setUserData("about");
        setToggleButton(settingsGroup, aboutButton);

        settingsGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == null && newValue != null) {
                if (tempToggle != null && tempToggle != newValue) {
                    ((ToggleButton) tempToggle).setTextFill(Paint.valueOf("#000000"));
                    ((ToggleButton) newValue).setTextFill(Paint.valueOf("07c160"));
                } else if (tempToggle == null) {
                    ((ToggleButton) newValue).setTextFill(Paint.valueOf("#07c160"));
                }
                tempToggle = newValue;

            }

            if (oldValue != null && newValue == null) {
                ((ToggleButton) tempToggle).setTextFill(Paint.valueOf("#07c160"));
                tempToggle = oldValue;
            }

            if (oldValue != null && newValue != null) {
                ((ToggleButton) newValue).setTextFill(Paint.valueOf("#07c160"));
                ((ToggleButton) oldValue).setTextFill(Paint.valueOf("#000000"));
                tempToggle = newValue;
            }

            ((AnchorPane) this.getChildren().get(this.getChildren().size() - 1)).getChildren().clear();

            if (tempToggle.getUserData() != null && tempToggle.getUserData().equals("about")) {
                setAboutView((AnchorPane) this.getChildren().get(this.getChildren().size() - 1));
            } else if (tempToggle.getUserData() != null && tempToggle.getUserData().equals("account")) {
                setAccountSettingsView((AnchorPane) this.getChildren().get(this.getChildren().size() - 1));
            } else if (tempToggle.getUserData() != null && tempToggle.getUserData().equals("file")) {
                setFileManagedView((AnchorPane) this.getChildren().get(this.getChildren().size() - 1));
            }
        });

        VBox settingsBox = new VBox();
        settingsBox.getChildren().addAll(accountSettingsButton, infoNotifyButton, commonSettingsButton, fileManagedButton,
                shortcutKeyButton, aboutButton);

        Line line = new Line();
        line.setLayoutX(201);
        line.setLayoutY(91);
        line.setStartX(-100);
        line.setStartY(0);
        line.setEndX(-100);
        line.setEndY(397);
        line.setStroke(Paint.valueOf("#d1d1d1"));

        AnchorPane publicPane = new AnchorPane();
        publicPane.setPrefWidth(450);
        publicPane.setPrefHeight(390);


        this.setPrefWidth(550);
        this.setPrefHeight(470);
        this.getChildren().addAll(settingsLabel, minimizeButton, closeButton, settingsBox, line, publicPane);


        AnchorPane.setLeftAnchor(settingsLabel, 5.0);
        AnchorPane.setTopAnchor(settingsLabel, 5.0);

        AnchorPane.setRightAnchor(minimizeButton, 30.0);
        AnchorPane.setTopAnchor(minimizeButton, 0.0);

        AnchorPane.setRightAnchor(closeButton, 0.0);
        AnchorPane.setTopAnchor(closeButton, 0.0);

        AnchorPane.setLeftAnchor(settingsBox, 0.0);
        AnchorPane.setTopAnchor(settingsBox, 80.0);

        AnchorPane.setLeftAnchor(line, 100.0);
        AnchorPane.setTopAnchor(line, 89.0);
        AnchorPane.setBottomAnchor(line, 0.0);

        AnchorPane.setRightAnchor(publicPane, 0.0);
        AnchorPane.setBottomAnchor(publicPane, 0.0);

        this.setStyle("-fx-effect: dropshadow(three-pass-box, #d1d1d1, 2, 2, 0, 0);" +
                "-fx-background-insets: 1;");
    }

    private void setWindowButton(Button button, String hoverColor, String imageUrl) {
        button.setPrefWidth(30);
        button.setPrefHeight(25);
        button.setStyle("-fx-background-color: transparent;" +
                "    -fx-border-style: none;" +
                "    -fx-border-radius: 0;" +
                "    -fx-background-size: 13px 13px;" +
                "    -fx-background-repeat: no-repeat;" +
                "    -fx-background-position: center;" +
                "    -fx-background-image: url(" + imageUrl + ");");

        button.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: " + hoverColor + ";" +
                        "    -fx-border-style: none;" +
                        "    -fx-border-radius: 0;" +
                        "    -fx-background-size: 13px 13px;" +
                        "    -fx-background-repeat: no-repeat;" +
                        "    -fx-background-position: center;" +
                        "    -fx-background-image: url(" + imageUrl + ");");
            } else {
                button.setStyle("-fx-background-color: transparent;" +
                        "    -fx-border-style: none;" +
                        "    -fx-border-radius: 0;" +
                        "    -fx-background-size: 13px 13px;" +
                        "    -fx-background-repeat: no-repeat;" +
                        "    -fx-background-position: center;" +
                        "    -fx-background-image: url(" + imageUrl + ");");
            }
        });
    }

    private void setToggleButton(ToggleGroup settingsGroup, ToggleButton button) {
        button.setCursor(Cursor.HAND);
        button.setToggleGroup(settingsGroup);
        button.setPrefWidth(100);
        button.setPrefHeight(30);
        button.setBackground(Background.EMPTY);
        button.setFont(Font.font(14));
    }

    private void setAboutView(AnchorPane publicPane) {

        Label versionLabel = new Label("版本信息");
        versionLabel.setFont(Font.font(14));

        Label helpLabel = new Label("微信帮助");
        helpLabel.setFont(Font.font(14));

        Label versionInfoLabel = new Label("微信3.5.0.46");
        versionInfoLabel.setFont(Font.font(14));

        Label rightLabel = new Label("Copyright © 2011-2022 Gzhu. All Rights Reserved. 广州大学 版权所有");
        rightLabel.setFont(Font.font(12));
        rightLabel.setTextFill(Paint.valueOf("#999999"));
        rightLabel.setAlignment(Pos.CENTER);

        Label serviceLabel = new Label("服务协议");
        serviceLabel.setFont(Font.font(12));
        serviceLabel.setTextFill(Paint.valueOf("#2ca1fa"));
        serviceLabel.setCursor(Cursor.HAND);
        serviceLabel.setAlignment(Pos.CENTER);

        Button checkUpdateButton = new Button("检查更新");
        checkUpdateButton.setFont(Font.font(14));
        checkUpdateButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#e9e9e9"), new CornerRadii(0), null)));
        checkUpdateButton.setPrefHeight(30);
        checkUpdateButton.setPrefWidth(76);
        checkUpdateButton.setCursor(Cursor.HAND);
        checkUpdateButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkUpdateButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#d2d2d2"), new CornerRadii(0), null)));
            } else {
                checkUpdateButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#e9e9e9"), new CornerRadii(0), null)));
            }
        });

        Button checkHelpButton = new Button("查看帮助");
        checkHelpButton.setFont(Font.font(14));
        checkHelpButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#e9e9e9"), new CornerRadii(0), null)));
        checkHelpButton.setPrefHeight(30);
        checkHelpButton.setPrefWidth(76);
        checkHelpButton.setCursor(Cursor.HAND);
        checkHelpButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkHelpButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#d2d2d2"), new CornerRadii(0), null)));
            } else {
                checkHelpButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#e9e9e9"), new CornerRadii(0), null)));
            }
        });

        publicPane.getChildren().addAll(checkUpdateButton, checkHelpButton, versionInfoLabel, serviceLabel, versionLabel, helpLabel, rightLabel);

        AnchorPane.setLeftAnchor(versionLabel, 70.0);
        AnchorPane.setTopAnchor(versionLabel, 0.0);

        AnchorPane.setLeftAnchor(helpLabel, 70.0);
        AnchorPane.setTopAnchor(helpLabel, 90.0);

        AnchorPane.setLeftAnchor(versionInfoLabel, 160.0);
        AnchorPane.setTopAnchor(versionInfoLabel, 0.0);

        AnchorPane.setLeftAnchor(checkUpdateButton, 160.0);
        AnchorPane.setTopAnchor(checkUpdateButton, 20.0);

        AnchorPane.setLeftAnchor(checkHelpButton, 160.0);
        AnchorPane.setTopAnchor(checkHelpButton, 90.0);

        AnchorPane.setLeftAnchor(rightLabel, 0.0);
        AnchorPane.setRightAnchor(rightLabel, 0.0);
        AnchorPane.setBottomAnchor(rightLabel, 20.0);

        AnchorPane.setLeftAnchor(serviceLabel, 0.0);
        AnchorPane.setRightAnchor(serviceLabel, 0.0);
        AnchorPane.setBottomAnchor(serviceLabel, 5.0);
    }

    private void setAccountSettingsView(AnchorPane publicPane) {

        ImageView profile = new ImageView(new Image("file:" + user.getUrl()));
        profile.setFitHeight(50);
        profile.setFitWidth(50);
        profile.setStyle("-fx-background-radius: 5px");

        Rectangle clip = new Rectangle(profile.getFitWidth(), profile.getFitHeight());
        clip.setArcHeight(10);
        clip.setArcWidth(10);
        profile.setClip(clip);

        Label usernameLabel = new Label(user.getUsername());
        usernameLabel.setFont(Font.font(20));

        Label weChatLabel = new Label("微信号：" + user.getWeChatAccount());
        weChatLabel.setFont(Font.font(12));
        weChatLabel.setTextFill(Paint.valueOf("#999999"));

        AnchorPane accountInfoPane = new AnchorPane();
        accountInfoPane.setPrefHeight(150);
        accountInfoPane.setStyle("-fx-background-radius: 5px;" +
                "-fx-background-color: white");

        accountInfoPane.getChildren().addAll(profile, usernameLabel, weChatLabel);

        AnchorPane.setLeftAnchor(profile, 25.0);
        AnchorPane.setTopAnchor(profile, 50.0);

        AnchorPane.setLeftAnchor(usernameLabel, 90.0);
        AnchorPane.setTopAnchor(usernameLabel, 50.0);

        AnchorPane.setLeftAnchor(weChatLabel, 90.0);
        AnchorPane.setTopAnchor(weChatLabel, 85.0);

        Label autoLoginLabel = new Label("自动登录");
        autoLoginLabel.setFont(Font.font(14));

        Label stateLabel = new Label("未开启");
        stateLabel.setFont(Font.font(14));

        Label stateDescriptionLabel = new Label("开启后，在本机登录微信无需手机确认。\n可在手机上开启。");
        stateDescriptionLabel.setFont(Font.font(12));
        stateDescriptionLabel.setTextFill(Paint.valueOf("999999"));

        Button loginOutButton = new Button("退出登录");
        loginOutButton.setFont(Font.font(14));
        loginOutButton.setStyle("-fx-background-color: #e9e9e9;");
        loginOutButton.setCursor(Cursor.HAND);
        loginOutButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                loginOutButton.setStyle("-fx-background-color: #d2d2d2;");
            } else {
                loginOutButton.setStyle("-fx-background-color: #e9e9e9;");

            }
        });
        loginOutButton.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                user.setLoginState(LoginState.NOT_LOGIN);
                userService.updateUserByUsername(user);

                mainStage.close();
            }
        });

        publicPane.getChildren().addAll(loginOutButton, stateLabel, stateDescriptionLabel, autoLoginLabel, accountInfoPane);

        AnchorPane.setLeftAnchor(accountInfoPane, 50.0);
        AnchorPane.setRightAnchor(accountInfoPane, 50.0);
        AnchorPane.setTopAnchor(accountInfoPane, 0.0);

        AnchorPane.setLeftAnchor(autoLoginLabel, 80.0);
        AnchorPane.setTopAnchor(autoLoginLabel, 180.0);

        AnchorPane.setLeftAnchor(stateLabel, 150.0);
        AnchorPane.setTopAnchor(stateLabel, 180.0);

        AnchorPane.setLeftAnchor(stateDescriptionLabel, 150.0);
        AnchorPane.setTopAnchor(stateDescriptionLabel, 210.0);

        AnchorPane.setLeftAnchor(loginOutButton, 170.0);
        AnchorPane.setRightAnchor(loginOutButton, 170.0);
        AnchorPane.setBottomAnchor(loginOutButton, 50.0);
    }

    private void setFileManagedView(AnchorPane publicPane) {

        Label autoDownloadLabel = new Label("自动下载");
        autoDownloadLabel.setFont(Font.font(14));

        CheckBox checkBox = new CheckBox();
        checkBox.setPrefWidth(25);
        checkBox.setPrefHeight(25);
        checkBox.setTextFill(Paint.valueOf("#07c160"));
        checkBox.setSelected(true);

        Label downloadDescriptionLabel = new Label("开启文件自动下载（200MB以内）");
        downloadDescriptionLabel.setFont(Font.font(14));

        Label publicKeyLabel = new Label("公钥管理");
        publicKeyLabel.setFont(Font.font(14));
        TextField publicKeyField = new TextField();
        publicKeyField.setStyle("-fx-background-color: white");
        publicKeyField.setPrefWidth(180);
        publicKeyField.setFont(Font.font(14));
        publicKeyField.setText("D:\\Documents\\WeChat Files\\");

        Button selectButton = new Button("选择...");
        selectButton.setPrefWidth(50);
        selectButton.setPrefHeight(30);
        selectButton.setStyle("-fx-background-color: #e9e9e9");
        selectButton.setCursor(Cursor.HAND);
        selectButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                selectButton.setStyle("-fx-background-color: #d2d2d2;");
            } else {
                selectButton.setStyle("-fx-background-color: #e9e9e9;");

            }
        });
        selectButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("公钥文件", "*.pem;*.pub"));
                File file = fileChooser.showOpenDialog(mainStage);
                if (file != null) {
                    publicKeyField.setText(file.getAbsolutePath());
                }
            }
        });

        Label fileManagedLabel = new Label("文件管理");
        fileManagedLabel.setFont(Font.font(14));

        TextField dirPathField = new TextField();
        dirPathField.setStyle("-fx-background-color: white");
        dirPathField.setPrefWidth(220);
        dirPathField.setFont(Font.font(14));
        dirPathField.setText("D:\\Documents\\WeChat Files\\");

        Label fileManagedDescriptionLabel = new Label("微信文件的默认保存位置");
        fileManagedDescriptionLabel.setFont(Font.font(13));
        fileManagedDescriptionLabel.setTextFill(Paint.valueOf("#999999"));

        Button changeButton = new Button("更改");
        changeButton.setFont(Font.font(14));
        changeButton.setStyle("-fx-background-color: #e9e9e9");
        changeButton.setPrefWidth(100);
        changeButton.setPrefHeight(30);
        changeButton.setCursor(Cursor.HAND);
        changeButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                changeButton.setStyle("-fx-background-color: #d2d2d2;");
            } else {
                changeButton.setStyle("-fx-background-color: #e9e9e9;");

            }
        });
        changeButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File dir = directoryChooser.showDialog(mainStage);
                if (dir != null) {
                    dirPathField.setText(dir.getAbsolutePath());
                }
            }
        });

        Button openDirButton = new Button("打开文件夹");
        openDirButton.setFont(Font.font(14));
        openDirButton.setStyle("-fx-background-color: #e9e9e9");
        openDirButton.setPrefWidth(100);
        openDirButton.setPrefHeight(30);
        openDirButton.setCursor(Cursor.HAND);
        openDirButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                openDirButton.setStyle("-fx-background-color: #d2d2d2;");
            } else {
                openDirButton.setStyle("-fx-background-color: #e9e9e9;");

            }
        });
        openDirButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                String dirPath = dirPathField.getText();
                if (!dirPath.isEmpty()) {
                    File dir = new File(dirPath);
                    if (dir.isDirectory()) {
                        try {
                            Desktop.getDesktop().open(dir);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        publicPane.getChildren().addAll(checkBox, dirPathField, publicKeyField, selectButton, changeButton, openDirButton,
                publicKeyLabel, autoDownloadLabel, downloadDescriptionLabel, fileManagedLabel, fileManagedDescriptionLabel);

        AnchorPane.setLeftAnchor(autoDownloadLabel, 80.0);
        AnchorPane.setTopAnchor(autoDownloadLabel, 5.0);

        AnchorPane.setLeftAnchor(checkBox, 150.0);
        AnchorPane.setTopAnchor(checkBox, 5.0);

        AnchorPane.setLeftAnchor(downloadDescriptionLabel, 180.0);
        AnchorPane.setTopAnchor(downloadDescriptionLabel, 5.0);

        AnchorPane.setLeftAnchor(publicKeyLabel, 80.0);
        AnchorPane.setTopAnchor(publicKeyLabel, 50.0);

        AnchorPane.setLeftAnchor(publicKeyField, 150.0);
        AnchorPane.setTopAnchor(publicKeyField, 45.0);

        AnchorPane.setLeftAnchor(selectButton, 340.0);
        AnchorPane.setTopAnchor(selectButton, 45.0);

        AnchorPane.setLeftAnchor(fileManagedLabel, 80.0);
        AnchorPane.setTopAnchor(fileManagedLabel, 90.0);

        AnchorPane.setLeftAnchor(dirPathField, 150.0);
        AnchorPane.setTopAnchor(dirPathField, 85.0);

        AnchorPane.setLeftAnchor(fileManagedDescriptionLabel, 150.0);
        AnchorPane.setTopAnchor(fileManagedDescriptionLabel, 115.0);

        AnchorPane.setLeftAnchor(changeButton, 150.0);
        AnchorPane.setTopAnchor(changeButton, 155.0);

        AnchorPane.setLeftAnchor(openDirButton, 265.0);
        AnchorPane.setTopAnchor(openDirButton, 155.0);
    }
}
