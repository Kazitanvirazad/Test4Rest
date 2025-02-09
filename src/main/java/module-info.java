module io.test4rest.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.net.http;
    requires com.google.gson;
    requires java.xml;
    requires okhttp3;
    requires jdk.compiler;
    requires java.xml.crypto;


    opens io.test4rest.app to javafx.fxml;
    exports io.test4rest.app;
    exports io.test4rest.app.controller;
    exports io.test4rest.app.constants;
    exports io.test4rest.app.model;
    exports io.test4rest.app.constants.http;
}
