package com.cyx.controller;

import com.cyx.ClientApplication;
import com.cyx.handler.DragWindowHandler;
import com.cyx.view.MainView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import sun.applet.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class LoginController implements Initializable {

    @FXML
    private Button loginButton;

    @Autowired
    private MainView mainView;

    @FXML
    private AnchorPane loginView;

    @FXML
    private ImageView profileView;

    private Stage loginStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginStage = ClientApplication.getStage();
        DragWindowHandler handler = new DragWindowHandler(loginStage);
        loginView.setOnMousePressed(handler);
        loginView.setOnMouseDragged(handler);

        //头像圆角
        profileView.setPreserveRatio(true);
        Rectangle rectangle = new Rectangle(profileView.prefHeight(-1), profileView.prefWidth(-1));
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);
        profileView.setClip(rectangle);
    }

    /**
     * 登录按钮事件
     */
    public void onLogin() {

        loginStage.setWidth(mainView.getView().prefWidth(0));
        loginStage.setHeight(mainView.getView().prefHeight(0));

        loginStage.centerOnScreen();
        ClientApplication.showView(MainView.class);
    }

    /**
     * 关闭窗口事件
     */
    public void onClosed() {
        loginStage.close();
    }
}
