package io.test4rest.app;

import io.test4rest.app.service.MainAppLoaderService;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MainApp extends Application {
    private static final Logger log = LogManager.getLogger(MainApp.class);
    private final MainAppLoaderService mainAppLoaderService;

    public MainApp() {
        super();
        this.mainAppLoaderService = new MainAppLoaderService();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            mainAppLoaderService.loadMainAppScreenComponents(primaryStage);
            primaryStage.show();
        } catch (IOException | NullPointerException exception) {
            log.error("Application failed to start.");
            log.error(exception.getMessage());
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
