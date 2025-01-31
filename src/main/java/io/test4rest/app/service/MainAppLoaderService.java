package io.test4rest.app.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static io.test4rest.app.constants.CommonConstants.APP_TITLE;

public class MainAppLoaderService {

    public void loadMainAppScreenComponents(Stage stage) throws IOException {
        Parent appRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/app/scene/MainScreen.fxml")));
        stage.setTitle(APP_TITLE);
        Scene scene = new Scene(appRoot);
        Image appLogo = new Image("/static/logos/test4rest_logo.png");
        stage.setScene(scene);
        stage.getIcons().add(appLogo);
    }
}
