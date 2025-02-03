package io.test4rest.app.controller;

import io.test4rest.app.constants.HttpMethod;
import io.test4rest.app.model.ApiRequest;
import io.test4rest.app.model.ApiResponse;
import io.test4rest.app.model.KeyValue;
import io.test4rest.app.service.ApiService;
import io.test4rest.app.service.impl.DefaultApiServiceImpl;
import io.test4rest.app.util.JsonUtil;
import io.test4rest.app.util.StringUtils;
import javafx.collections.FXCollections;
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
import java.util.ResourceBundle;

import static io.test4rest.app.constants.CommonConstants.EMPTY_SPACE;
import static io.test4rest.app.constants.CommonConstants.EMPTY_STRING;
import static io.test4rest.app.constants.CommonConstants.MILLIS_SHORT_FORM;
import static io.test4rest.app.constants.HttpMethod.DELETE;
import static io.test4rest.app.constants.HttpMethod.GET;
import static io.test4rest.app.constants.HttpMethod.HEAD;
import static io.test4rest.app.constants.HttpMethod.OPTIONS;
import static io.test4rest.app.constants.HttpMethod.PATCH;
import static io.test4rest.app.constants.HttpMethod.POST;
import static io.test4rest.app.constants.HttpMethod.PUT;

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

    private boolean isResponsePrettified;
    private ApiResponse response;

    @FXML
    public void callApi() {
        ApiService apiService = new DefaultApiServiceImpl();
        ApiRequest request = new ApiRequest();
        request.setUrl(url_field.getText().trim());
        request.setMethod(http_method_selector.getValue());
        if (StringUtils.hasText(request_body_input.getText())) {
            request.setBody(request_body_input.getText());
        }

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
                    String prettyTxt = JsonUtil.prettifyJson(response.getBody());
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


        // initialising query param tableview -> refer https://youtu.be/uh5R7D_vFto
        queryParamKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        queryParamKey.setCellValueFactory(new PropertyValueFactory<>("value"));

    }
}
