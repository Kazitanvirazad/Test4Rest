module io.test4rest.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.net.http;
    requires com.google.gson;


    opens io.test4rest.app to javafx.fxml;
    exports io.test4rest.app;
    exports io.test4rest.app.controller;
    exports io.test4rest.app.constants;
}