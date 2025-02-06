package io.test4rest.app.controller;

import io.test4rest.app.constants.http.HttpMethod;
import io.test4rest.app.model.ApiRequest;
import io.test4rest.app.model.ApiResponse;
import io.test4rest.app.model.KeyValue;
import io.test4rest.app.service.ApiService;
import io.test4rest.app.service.impl.DefaultApiServiceImpl;
import io.test4rest.app.util.JsonUtils;
import io.test4rest.app.util.StringUtils;
import io.test4rest.app.util.XmlUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static io.test4rest.app.constants.CommonConstants.EMPTY_SPACE;
import static io.test4rest.app.constants.CommonConstants.EMPTY_STRING;
import static io.test4rest.app.constants.CommonConstants.MILLIS_SHORT_FORM;
import static io.test4rest.app.constants.http.HttpMethod.DELETE;
import static io.test4rest.app.constants.http.HttpMethod.GET;
import static io.test4rest.app.constants.http.HttpMethod.HEAD;
import static io.test4rest.app.constants.http.HttpMethod.OPTIONS;
import static io.test4rest.app.constants.http.HttpMethod.PATCH;
import static io.test4rest.app.constants.http.HttpMethod.POST;
import static io.test4rest.app.constants.http.HttpMethod.PUT;
import static io.test4rest.app.util.JsonUtils.isJsonResponse;
import static io.test4rest.app.util.XmlUtils.isXmlResponse;

public class MainScreenController implements Initializable {
    private static final Logger log = LogManager.getLogger(MainScreenController.class);
    @FXML
    public AnchorPane main_screen_cont;
    @FXML
    public ChoiceBox<HttpMethod> http_method_selector;
    @FXML
    public TextField url_field;
    @FXML
    public Button send_bttn;
    @FXML
    public TextArea request_body_input;
    @FXML
    public TextArea response_body_output;
    @FXML
    public Text response_time;
    @FXML
    public Text response_status_code;
    @FXML
    public ImageView zoom_in;
    @FXML
    public ImageView zoom_out;
    @FXML
    public ImageView text_wrap;
    @FXML
    public ImageView copy_responseTxt;
    @FXML
    public TableView<KeyValue> query_param_table;
    @FXML
    public TableColumn<KeyValue, String> queryParamKey;
    @FXML
    public TableColumn<KeyValue, String> queryParamValue;
    @FXML
    public TableView<KeyValue> header_table;
    @FXML
    public TableColumn<KeyValue, String> headerKey;
    @FXML
    public TableColumn<KeyValue, String> headerValue;

    private boolean isResponsePrettified;
    private ApiResponse response;

    @FXML
    public void callApi() {
        ApiService apiService = new DefaultApiServiceImpl();
        ApiRequest request = new ApiRequest();

        // setting url to request
        request.setUrl(url_field.getText().trim());

        // adding query params from table view
        addQueryParamsToRequest(request);

        // setting http method from choice box
        request.setMethod(http_method_selector.getValue());

        // setting body to request
        if (StringUtils.hasText(request_body_input.getText())) {
            request.setBody(request_body_input.getText());
        }

        // adding headers from headers table view
        addHeadersToRequest(request);

        response = apiService.callApi(request);
        response_body_output.clear();
        response_body_output.setText(response.getBody());
        response_time.setText(response.getDuration() + EMPTY_SPACE + MILLIS_SHORT_FORM);

        String status = response.getStatusCode() +
                (StringUtils.hasText(response.getResponseStatus()) ? EMPTY_SPACE + response.getResponseStatus() : EMPTY_STRING);
        response_status_code.setText(status);

        isResponsePrettified = false;
        prettifyResponse(null);
    }

    @FXML
    public void prettifyResponse(ActionEvent event) {
        if (response != null && StringUtils.hasText(response.getBody())) {
            if (isResponsePrettified) {
                response_body_output.setText(response.getBody());
                isResponsePrettified = false;
            } else {
                if (response.getPrettyText() == null) {
                    String prettyTxt;
                    if (isJsonResponse(response)) {
                        prettyTxt = JsonUtils.prettifyJson(response.getBody());
                    } else if (isXmlResponse(response)) {
                        prettyTxt = XmlUtils.prettifyXml(response.getBody());
                    } else {
                        return;
                    }
                    response.setPrettyText(prettyTxt);
                }
                response_body_output.setText(response.getPrettyText());
                isResponsePrettified = true;
            }
        }
    }

    private void zoomInResponseText(MouseEvent event) {
        Font initialFont = response_body_output.getFont();
        double size = initialFont.getSize();
        if (size < 20) {
            Font font = Font.font(initialFont.getFamily(), size + 1.0);
            response_body_output.setFont(font);
        }
    }

    private void zoomOutResponseText(MouseEvent event) {
        Font initialFont = response_body_output.getFont();
        double size = initialFont.getSize();
        if (size > 12) {
            Font font = Font.font(initialFont.getFamily(), size - 1.0);
            response_body_output.setFont(font);
        }
    }

    private void wrapResponseText(MouseEvent event) {
        if (response_body_output.isWrapText()) {
            response_body_output.wrapTextProperty().setValue(false);
        } else {
            response_body_output.wrapTextProperty().setValue(true);
        }
    }

    private void copyResponseText(MouseEvent event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        if (response != null && StringUtils.hasText(response.getBody())) {
            if (isResponsePrettified) {
                clipboardContent.putString(response.getPrettyText());
            } else {
                clipboardContent.putString(response.getBody());
            }
        } else {
            clipboardContent.putString(EMPTY_STRING);
        }
        clipboard.setContent(clipboardContent);
    }

    @FXML
    public void updateQueryParamKey(TableColumn.CellEditEvent<KeyValue, String> cellEditEvent) {
        KeyValue selectedQueryParam = query_param_table.getSelectionModel().getSelectedItem();
        selectedQueryParam.setKey(cellEditEvent.getNewValue());
    }

    @FXML
    public void updateQueryParamValue(TableColumn.CellEditEvent<KeyValue, String> cellEditEvent) {
        KeyValue selectedQueryParam = query_param_table.getSelectionModel().getSelectedItem();
        selectedQueryParam.setValue(cellEditEvent.getNewValue());
    }

    @FXML
    public void updateHeaderKey(TableColumn.CellEditEvent<KeyValue, String> cellEditEvent) {
        KeyValue selectedHeader = header_table.getSelectionModel().getSelectedItem();
        selectedHeader.setKey(cellEditEvent.getNewValue());
    }

    @FXML
    public void updateHeaderValue(TableColumn.CellEditEvent<KeyValue, String> cellEditEvent) {
        KeyValue selectedHeader = query_param_table.getSelectionModel().getSelectedItem();
        selectedHeader.setValue(cellEditEvent.getNewValue());
    }

    private void addHeadersToRequest(ApiRequest request) {
        if (header_table != null && !header_table.getItems().isEmpty()) {
            header_table.getItems().forEach(keyValue -> request.addHeader(keyValue.getKey(), keyValue.getValue()));
        }
    }

    private void addQueryParamsToRequest(ApiRequest request) {
        if (query_param_table != null && !query_param_table.getItems().isEmpty()) {
            query_param_table.getItems().forEach(keyValue -> request.addQueryParam(keyValue.getKey(), keyValue.getValue()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialising zoom in and out buttons
        Image zoomIn = new Image("/static/icons/zoom-in-24.png");
        Image zoomOut = new Image("/static/icons/zoom-out-24.png");
        zoom_in.setImage(zoomIn);
        zoom_out.setImage(zoomOut);
        zoom_in.addEventHandler(MouseEvent.MOUSE_CLICKED, this::zoomInResponseText);
        zoom_out.addEventHandler(MouseEvent.MOUSE_CLICKED, this::zoomOutResponseText);

        // initialising Http method ChoiceBox selector
        HttpMethod[] methods = {GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS};
        http_method_selector.setItems(FXCollections.observableArrayList(methods));
        http_method_selector.setValue(GET);
        http_method_selector.getSelectionModel()
                .selectedIndexProperty()
                .addListener((observable, oldValue, newValue) ->
                        http_method_selector.setValue(methods[newValue.intValue()])
                );

        // initialising response text wrap button
        Image wordWrapIcon = new Image("/static/icons/word-wrap-24.png");
        text_wrap.setImage(wordWrapIcon);
        text_wrap.addEventHandler(MouseEvent.MOUSE_CLICKED, this::wrapResponseText);
        response_body_output.wrapTextProperty().setValue(true);

        // initialising copy response text button
        Image copyResponseText = new Image("/static/icons/copy-24.png");
        copy_responseTxt.setImage(copyResponseText);
        copy_responseTxt.addEventHandler(MouseEvent.MOUSE_CLICKED, this::copyResponseText);

        // initialising query param tableview -> refer https://youtu.be/LQlwTIayyl4
        queryParamKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        queryParamValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        query_param_table.setEditable(true);
        queryParamKey.setCellFactory(TextFieldTableCell.forTableColumn());
        queryParamValue.setCellFactory(TextFieldTableCell.forTableColumn());

        // initialising header tableview
        headerKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        headerValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        header_table.setEditable(true);
        headerKey.setCellFactory(TextFieldTableCell.forTableColumn());
        headerValue.setCellFactory(TextFieldTableCell.forTableColumn());

        // adding sample data - to be replaced later with database implementation
        header_table.setItems(getSampleParams());
        query_param_table.setItems(getSampleQueryParams());
    }

    // sample data - to be removed later
    private ObservableList<KeyValue> getSampleParams() {
        KeyValue param1 = new KeyValue("desc1", new SimpleStringProperty("city"), new SimpleStringProperty("Bangalore"));
        KeyValue param2 = new KeyValue("desc2", new SimpleStringProperty("Content-Type"), new SimpleStringProperty("application/json"));
//        KeyValue param3 = new KeyValue("desc3", new SimpleStringProperty("Authorizationknvlkav;alkvn;oave;kve;lkvnlnvbkjsbksjdb"), new SimpleStringProperty("Bearer some_token"));
//        KeyValue param4 = new KeyValue("desc2", new SimpleStringProperty("Content-Type"), new SimpleStringProperty("application/json"));
//        KeyValue param5 = new KeyValue("desc3", new SimpleStringProperty("Authorization"), new SimpleStringProperty("Bearer some_toknaejvnernenbw;ebek;bjaekjbebejnggegheiugierhrjnfhierughrgrgiruhgregnfgiuergrengrekgnierugerigernggerigeen"));
//        KeyValue param6 = new KeyValue("desc2", new SimpleStringProperty("Content-Type"), new SimpleStringProperty("application/json"));
//        KeyValue param7 = new KeyValue("desc3", new SimpleStringProperty("Authorization"), new SimpleStringProperty("Bearer some_token"));
        return FXCollections.observableList(List.of(/*param1, */param2/*, param3, param4, param5, param6, param7*/));
    }

    // sample data - to be removed later
    private ObservableList<KeyValue> getSampleQueryParams() {
        KeyValue param1 = new KeyValue("desc1", new SimpleStringProperty("city"), new SimpleStringProperty("Bangalore"));
        return FXCollections.observableList(List.of(param1));
    }
}
