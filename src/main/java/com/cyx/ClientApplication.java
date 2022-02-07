package com.cyx;

import com.cyx.view.LoginView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.SplashScreen;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collection;
import java.util.Collections;

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

    @Override
    public Collection<Image> loadDefaultIcons() {
        return Collections.singletonList(new Image("/images/logo/logo.png"));
    }

    
}
