package io.test4rest.app.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static io.test4rest.app.constants.CommonConstants.APP_TITLE;

public class MainAppLoaderService {

    public void loadMainAppScreenComponents(Stage stage) throws IOException {
        Pane appRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/app/scene/MainScreen.fxml")));
        stage.setTitle(APP_TITLE);
        Scene scene = new Scene(appRoot);

        String css = Objects.requireNonNull(this.getClass().getResource("/style/MainScreenStyle.css")).toExternalForm();
        scene.getStylesheets().add(css);

        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
//            System.out.println("Width: " + newSceneWidth);

        });
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
//            System.out.println("Height: " + newSceneHeight);
        });

        Image appLogo = new Image("/static/logos/test4rest_logo_2.png");
        stage.setMinHeight(540.0);
        stage.setMinWidth(820.0);
        stage.setScene(scene);
        stage.getIcons().add(appLogo);
    }
}
