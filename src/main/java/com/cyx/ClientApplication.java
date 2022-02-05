package com.cyx;

import com.cyx.view.LoginView;
import com.cyx.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.SplashScreen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ClientApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {

        launch(ClientApplication.class, LoginView.class, new SplashScreen(){
            @Override
            public boolean visible() {
                return false;
            }
        }, args);
    }

    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
        stage.initStyle(StageStyle.UNDECORATED);
    }
}
