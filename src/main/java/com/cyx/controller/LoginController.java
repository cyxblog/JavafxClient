package com.cyx.controller;

import com.cyx.ClientApplication;
import com.cyx.component.LoginPane;
import com.cyx.component.RegisterPane;
import com.cyx.constant.LoginState;
import com.cyx.handler.DragWindowHandler;
import com.cyx.pojo.User;
import com.cyx.service.UserServiceImpl;
import com.cyx.utils.FileUtils;
import com.cyx.view.MainView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import sun.applet.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class LoginController implements Initializable {

    @FXML
    private Button loginButton;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MainView mainView;

    @FXML
    private AnchorPane loginView;

    @FXML
    private ImageView profileView;

    @FXML
    private Label topicLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label changeAccountLabel;

    @FXML
    private Label onlyTransformFileLabel;

    @FXML
    private Line line;

    private Stage loginStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginStage = ClientApplication.getStage();
        DragWindowHandler handler = new DragWindowHandler(loginStage);
        loginView.setOnMousePressed(handler);
        loginView.setOnMouseDragged(handler);

        User currentUser = userService.getUserByLoginState(LoginState.NOT_LOGIN);

        //头像圆角
        profileView.setPreserveRatio(true);
        Rectangle rectangle = new Rectangle(profileView.prefHeight(-1), profileView.prefWidth(-1));
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);
        profileView.setClip(rectangle);

        if (currentUser == null) {
            profileView.setImage(new Image("/images/logo/logo.png"));
            usernameLabel.setText("无用户");
        } else {
            profileView.setImage(new Image("file:" + currentUser.getUrl()));
            usernameLabel.setText(currentUser.getUsername());
        }

    }

    /**
     * 登录按钮事件
     */
    public void onLogin() {

        User user = userService.getUserByUsername(usernameLabel.getText().trim());
        if (user == null) {
            return;
        }
        user.setLoginState(LoginState.IS_LOGIN);
        userService.updateUserByUsername(user);

        loginStage.setWidth(mainView.getView().prefWidth(0));
        loginStage.setHeight(mainView.getView().prefHeight(0));
        loginStage.centerOnScreen();
        ClientApplication.showView(MainView.class);
    }

    /**
     * 切换账号
     */
    public void onChangeAccount() {
        showOrHideIndexPane(false);

        showLoginPane();
    }

    private void showOrHideIndexPane(boolean isShow) {
        profileView.setVisible(isShow);
        profileView.setManaged(isShow);

        usernameLabel.setVisible(isShow);
        usernameLabel.setManaged(isShow);

        loginButton.setVisible(isShow);
        loginButton.setManaged(isShow);

        changeAccountLabel.setVisible(isShow);
        changeAccountLabel.setManaged(isShow);

        line.setVisible(isShow);
        line.setManaged(isShow);

        onlyTransformFileLabel.setVisible(isShow);
        onlyTransformFileLabel.setManaged(isShow);
    }

    private void showLoginPane() {
        topicLabel.setText("登录");
        LoginPane loginPane = new LoginPane();
        loginView.getChildren().add(loginPane);

        AnchorPane.setLeftAnchor(loginPane, 0.0);
        AnchorPane.setRightAnchor(loginPane, 0.0);
        AnchorPane.setBottomAnchor(loginPane, 0.0);

        TextField usernameField = (TextField) loginPane.getChildren().get(0);
        TextField passwordField = (TextField) loginPane.getChildren().get(1);
        Button registerButton = (Button) loginPane.getChildren().get(2);
        Button affirmButton = (Button) loginPane.getChildren().get(3);


        registerButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                loginPane.setVisible(false);
                loginPane.setManaged(false);
                showRegisterPane();
            }
        });

        affirmButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                User user = userService.getUserByUsername(username);
                if (!username.isEmpty() && !password.isEmpty() && user != null) {
                    if (user.getPassword().equals(password)) {
                        loginPane.setVisible(false);
                        loginPane.setManaged(true);

                        profileView.setImage(new Image("file:" + user.getUrl()));
                        usernameLabel.setText(user.getUsername());
                        showOrHideIndexPane(true);
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "无法登录", "", loginStage);
                }
            }
        });


    }

    private void showRegisterPane() {
        topicLabel.setText("注册");
        RegisterPane registerPane = new RegisterPane(loginStage);
        loginView.getChildren().add(registerPane);

        AnchorPane.setLeftAnchor(registerPane, 0.0);
        AnchorPane.setRightAnchor(registerPane, 0.0);
        AnchorPane.setBottomAnchor(registerPane, 0.0);

        TextField profileField = (TextField) registerPane.getChildren().get(0);
        TextField usernameField = (TextField) registerPane.getChildren().get(1);
        TextField passwordField = (TextField) registerPane.getChildren().get(2);
        Button backButton = (Button) registerPane.getChildren().get(3);


        Button registerButton = (Button) registerPane.getChildren().get(4);
        registerButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                String profilePath = profileField.getText().trim();
                String username = usernameField.getText().trim();
                String password = passwordField.getText();
                User user = userService.getUserByUsername(username);
                File profile = new File(profilePath);
                if (!username.isEmpty() && !password.isEmpty() &&
                        user == null && profile.isFile()) {
                    File savedProfile = new File("profile/" + profile.getName());
                    try {
                        FileCopyUtils.copy(profile, savedProfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    userService.addUser(new User(-1, username, password, savedProfile.getAbsolutePath(),
                            RandomStringUtils.randomAlphanumeric(10), LoginState.NOT_LOGIN));
                    showAlert(Alert.AlertType.CONFIRMATION, "注册成功！",
                            "您的账号为：" + username, loginStage);
                    registerPane.setVisible(false);
                    registerPane.setManaged(false);
                    showLoginPane();
                } else if (!username.isEmpty() && !password.isEmpty() && user != null
                        && new File(profilePath).isFile()) {
                    showAlert(Alert.AlertType.WARNING, "注册失败，该账号已存在。",
                            "您的账号为：" + username, loginStage);
                } else if (username.isEmpty() || password.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "注册失败，账号密码不能为空。",
                            "您的账号为：" + username, loginStage);
                } else {
                    showAlert(Alert.AlertType.WARNING, "注册失败！", "", loginStage);
                }
            }
        });

        backButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                registerPane.setVisible(false);
                registerPane.setManaged(false);
                showLoginPane();
            }
        });
    }

    private void showAlert(Alert.AlertType type, String headerText, String contentText, Stage ownerStage) {
        Alert alert = new Alert(type);
        alert.initOwner(ownerStage);
        alert.setContentText(contentText);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    /**
     * 关闭窗口事件
     */
    public void onClosed() {
        loginStage.close();
    }
}
