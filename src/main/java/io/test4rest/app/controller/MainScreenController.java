package io.test4rest.app.controller;

import io.test4rest.app.constants.http.HttpMethod;
import io.test4rest.app.model.ApiRequest;
import io.test4rest.app.model.ApiResponse;
import io.test4rest.app.model.KeyValue;
import io.test4rest.app.service.ApiService;
import io.test4rest.app.service.impl.DefaultApiServiceImpl;
import io.test4rest.app.util.CollectionUtils;
import io.test4rest.app.util.JsonUtils;
import io.test4rest.app.util.StringUtils;
import io.test4rest.app.util.TableColumnWrapTextCallback;
import io.test4rest.app.util.TableRowCopyKeyEventHandler;
import io.test4rest.app.util.XmlUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
import static io.test4rest.app.constants.http.ReqResConstants.REQUEST_BODY_TEXT_TYPES;
import static io.test4rest.app.constants.http.ReqResConstants.REQUEST_BODY_TYPES;
import static io.test4rest.app.util.JsonUtils.isJsonResponse;
import static io.test4rest.app.util.XmlUtils.isXmlResponse;

public class MainScreenController implements Initializable {
    private static final Logger log = LogManager.getLogger(MainScreenController.class);
    @FXML
    public AnchorPane mainScreenCont;
    @FXML
    public ChoiceBox<HttpMethod> httpMethodSelector;
    @FXML
    public TextField urlField;
    @FXML
    public Button sendButton;
    @FXML
    public TextArea requestBodyTextInput;
    @FXML
    public TextArea responseBodyOutput;
    @FXML
    public Text responseTime;
    @FXML
    public Text responseStatusCode;
    @FXML
    public ImageView zoomIn;
    @FXML
    public ImageView zoomOut;
    @FXML
    public ImageView textWrap;
    @FXML
    public ImageView copyResponseTxt;
    @FXML
    public TableView<KeyValue> queryParamTable;
    @FXML
    public TableColumn<KeyValue, String> queryParamTableKey;
    @FXML
    public TableColumn<KeyValue, String> queryParamTableValue;
    @FXML
    public TableView<KeyValue> headerTable;
    @FXML
    public TableColumn<KeyValue, String> headerTableKey;
    @FXML
    public TableColumn<KeyValue, String> headerTableValue;
    @FXML
    public TextField queryAddKey;
    @FXML
    public TextField queryAddValue;
    @FXML
    public Button addQueryButton;
    @FXML
    public TextField headerAddKey;
    @FXML
    public TextField headerAddValue;
    @FXML
    public Button addHeaderButton;
    @FXML
    public ImageView deleteSelectedQueries;
    @FXML
    public ImageView deleteSelectedHeaders;
    @FXML
    public ImageView responsePrettifier;
    @FXML
    public TableView<KeyValue> headerResponseTable;
    @FXML
    public TableColumn<KeyValue, String> headerResponseTableKey;
    @FXML
    public TableColumn<KeyValue, String> headerResponseTableValue;
    @FXML
    public ChoiceBox<String> requestBodyTypeSelector;
    @FXML
    public ChoiceBox<String> requestBodyTextTypeSelector;

    private boolean isResponsePrettified;
    private ApiResponse response;

    @FXML
    public void callApi() {
        ApiService apiService = new DefaultApiServiceImpl();
        ApiRequest request = new ApiRequest();

        // setting url to request
        request.setUrl(urlField.getText().trim());

        // adding query params from table view
        addQueryParamsToRequest(request);

        // setting http method from choice box
        request.setMethod(httpMethodSelector.getValue());

        // setting body to request
        if (StringUtils.hasText(requestBodyTextInput.getText())) {
            request.setBody(requestBodyTextInput.getText());
        }

        // adding headers from headers table view
        addHeadersToRequest(request);

        // calling api
        response = apiService.callApi(request);

        // clearing response body output textarea
        responseBodyOutput.clear();
        // setting response body output textarea
        responseBodyOutput.setText(response.getBody());
        // setting response duration
        responseTime.setText(response.getDuration() + EMPTY_SPACE + MILLIS_SHORT_FORM);

        String status = response.getStatusCode() +
                (StringUtils.hasText(response.getResponseStatus()) ? EMPTY_SPACE + response.getResponseStatus() : EMPTY_STRING);
        // setting response status code
        responseStatusCode.setText(status);

        // filling response header tableview
        fillResponseHeadersTable();
        isResponsePrettified = false;
        // prettifying response text for json and xml text
        prettifyResponse(null);
    }

    private void prettifyResponse(MouseEvent event) {
        if (response != null && StringUtils.hasText(response.getBody())) {
            if (isResponsePrettified) {
                responseBodyOutput.setText(response.getBody());
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
                responseBodyOutput.setText(response.getPrettyText());
                isResponsePrettified = true;
            }
        }
    }

    private void zoomInResponseText(MouseEvent event) {
        Font initialFont = responseBodyOutput.getFont();
        double size = initialFont.getSize();
        if (size < 20) {
            Font font = Font.font(initialFont.getFamily(), size + 1.0);
            responseBodyOutput.setFont(font);
        }
    }

    private void zoomOutResponseText(MouseEvent event) {
        Font initialFont = responseBodyOutput.getFont();
        double size = initialFont.getSize();
        if (size > 12) {
            Font font = Font.font(initialFont.getFamily(), size - 1.0);
            responseBodyOutput.setFont(font);
        }
    }

    private void wrapResponseText(MouseEvent event) {
        responseBodyOutput.wrapTextProperty().setValue(!responseBodyOutput.isWrapText());
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
        KeyValue selectedQueryParam = queryParamTable.getSelectionModel().getSelectedItem();
        selectedQueryParam.setKey(cellEditEvent.getNewValue());
    }

    @FXML
    public void updateQueryParamValue(TableColumn.CellEditEvent<KeyValue, String> cellEditEvent) {
        KeyValue selectedQueryParam = queryParamTable.getSelectionModel().getSelectedItem();
        selectedQueryParam.setValue(cellEditEvent.getNewValue());
    }

    @FXML
    public void updateHeaderKey(TableColumn.CellEditEvent<KeyValue, String> cellEditEvent) {
        KeyValue selectedHeader = headerTable.getSelectionModel().getSelectedItem();
        selectedHeader.setKey(cellEditEvent.getNewValue());
    }

    @FXML
    public void updateHeaderValue(TableColumn.CellEditEvent<KeyValue, String> cellEditEvent) {
        KeyValue selectedHeader = headerTable.getSelectionModel().getSelectedItem();
        selectedHeader.setValue(cellEditEvent.getNewValue());
    }

    private void addHeadersToRequest(ApiRequest request) {
        if (headerTable != null && !headerTable.getItems().isEmpty()) {
            headerTable.getItems().forEach(keyValue -> request.addHeader(keyValue.getKey(), keyValue.getValue()));
        }
    }

    private void addQueryParamsToRequest(ApiRequest request) {
        if (queryParamTable != null && !queryParamTable.getItems().isEmpty()) {
            queryParamTable.getItems().forEach(keyValue -> request.addQueryParam(keyValue.getKey(), keyValue.getValue()));
        }
    }

    @FXML
    public void addNewQueryParam(ActionEvent event) {
        KeyValue query = new KeyValue();
        if (StringUtils.hasText(queryAddKey.getText())) {
            query.setKey(queryAddKey.getText());
            query.setValue(StringUtils.hasText(queryAddValue.getText()) ? queryAddValue.getText() : EMPTY_STRING);
            List<KeyValue> queries = new ArrayList<>();
            queries.add(query);
            queries.addAll(queryParamTable.getItems());
            queryParamTable.setItems(FXCollections.observableList(queries));

            // clearing the input box
            queryAddKey.setText(EMPTY_STRING);
            queryAddValue.setText(EMPTY_STRING);
        }
    }

    @FXML
    public void addNewHeader(ActionEvent event) {
        KeyValue header = new KeyValue();
        if (StringUtils.hasText(headerAddKey.getText())) {
            header.setKey(headerAddKey.getText());
            header.setValue(StringUtils.hasText(headerAddValue.getText()) ? headerAddValue.getText() : EMPTY_STRING);
            List<KeyValue> headers = new ArrayList<>();
            headers.add(header);
            headers.addAll(headerTable.getItems());
            headerTable.setItems(FXCollections.observableList(headers));

            // clearing the input box
            headerAddKey.setText(EMPTY_STRING);
            headerAddValue.setText(EMPTY_STRING);
        }
    }

    private void deleteSelectedQueries(MouseEvent event) {
        // fetching all selected items from tableview
        ObservableList<KeyValue> selectedQueries = queryParamTable.getSelectionModel().getSelectedItems();
        if (selectedQueries != null && !selectedQueries.isEmpty()) {
            List<KeyValue> queries = queryParamTable.getItems().stream().filter(keyValue -> !selectedQueries.contains(keyValue)).collect(Collectors.toList());
            queryParamTable.setItems(FXCollections.observableList(queries));
        }
    }

    private void deleteSelectedHeaders(MouseEvent event) {
        // fetching all selected items from tableview
        ObservableList<KeyValue> selectedHeaders = headerTable.getSelectionModel().getSelectedItems();
        if (selectedHeaders != null && !selectedHeaders.isEmpty()) {
            List<KeyValue> headers = headerTable.getItems().stream().filter(keyValue -> !selectedHeaders.contains(keyValue)).collect(Collectors.toList());
            headerTable.setItems(FXCollections.observableList(headers));
        }
    }

    private void fillResponseHeadersTable() {
        if (response != null && !CollectionUtils.isEmpty(response.getHeaders())) {
            headerResponseTable.setItems(FXCollections.observableList(response.getHeaders()));
        }
    }

    private void onRequestBodyTypeSelectorChanged(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        requestBodyTypeSelector.setValue(REQUEST_BODY_TYPES[newValue.intValue()]);
    }

    private void onRequestBodyTextTypeSelectorChanged(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        requestBodyTextTypeSelector.setValue(REQUEST_BODY_TEXT_TYPES[newValue.intValue()]);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialising zoom in and out buttons
        Image zoomIn = new Image("/static/icons/zoom-in-24.png");
        Image zoomOut = new Image("/static/icons/zoom-out-24.png");
        this.zoomIn.setImage(zoomIn);
        this.zoomOut.setImage(zoomOut);
        this.zoomIn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::zoomInResponseText);
        this.zoomOut.addEventHandler(MouseEvent.MOUSE_CLICKED, this::zoomOutResponseText);

        // initialising Http method ChoiceBox selector
        HttpMethod[] methods = {GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS};
        httpMethodSelector.setItems(FXCollections.observableArrayList(methods));
        httpMethodSelector.setValue(GET);
        httpMethodSelector.getSelectionModel()
                .selectedIndexProperty()
                .addListener((observable, oldValue, newValue) ->
                        httpMethodSelector.setValue(methods[newValue.intValue()])
                );

        // initialising response text wrap button
        Image wordWrapIcon = new Image("/static/icons/word-wrap-24.png");
        textWrap.setImage(wordWrapIcon);
        textWrap.addEventHandler(MouseEvent.MOUSE_CLICKED, this::wrapResponseText);
        responseBodyOutput.wrapTextProperty().setValue(true);

        // initialising copy response text button
        Image copyResponseText = new Image("/static/icons/copy-24.png");
        copyResponseTxt.setImage(copyResponseText);
        copyResponseTxt.addEventHandler(MouseEvent.MOUSE_CLICKED, this::copyResponseText);

        // initialising query param tableview
        queryParamTable.setEditable(true);
        // allowing multiple row selection
        queryParamTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        queryParamTableKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        queryParamTableValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        queryParamTableKey.setCellFactory(TextFieldTableCell.forTableColumn());
        queryParamTableValue.setCellFactory(TextFieldTableCell.forTableColumn());
        // setting query param tableview row copy to clipboard functionality
        queryParamTable.setOnKeyPressed(new TableRowCopyKeyEventHandler());

        // initialising header tableview
        headerTable.setEditable(true);
        // allowing multiple row selection
        headerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        headerTableKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        headerTableValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        headerTableKey.setCellFactory(TextFieldTableCell.forTableColumn());
        headerTableValue.setCellFactory(TextFieldTableCell.forTableColumn());
        // setting header tableview row copy to clipboard functionality
        headerTable.setOnKeyPressed(new TableRowCopyKeyEventHandler());

        // adding sample data - to be replaced later with database implementation
        headerTable.setItems(getSampleParams());
        queryParamTable.setItems(getSampleQueryParams());

        // initialising delete queries and delete headers button
        Image deleteButton = new Image("/static/icons/delete-24.png");
        deleteSelectedQueries.setImage(deleteButton);
        deleteSelectedQueries.addEventHandler(MouseEvent.MOUSE_CLICKED, this::deleteSelectedQueries);
        deleteSelectedHeaders.setImage(deleteButton);
        deleteSelectedHeaders.addEventHandler(MouseEvent.MOUSE_CLICKED, this::deleteSelectedHeaders);

        // set visibility of query delete button if query tableview is empty
        deleteSelectedQueries.visibleProperty().bind(queryParamTable.itemsProperty().isNotEqualTo(FXCollections.emptyObservableList()));
        // set visibility of header delete button if header tableview is empty
        deleteSelectedHeaders.visibleProperty().bind(headerTable.itemsProperty().isNotEqualTo(FXCollections.emptyObservableList()));

        // setting response prettifier button
        Image responsePrettifierButton = new Image("/static/icons/curly-brackets-24.png");
        responsePrettifier.setImage(responsePrettifierButton);
        responsePrettifier.addEventHandler(MouseEvent.MOUSE_CLICKED, this::prettifyResponse);

        // initialising response header tableview
        headerResponseTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        headerResponseTableKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        headerResponseTableValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        // setting response header tableview row text wrapping
        headerResponseTableKey.setCellFactory(new TableColumnWrapTextCallback(headerResponseTableKey));
        headerResponseTableValue.setCellFactory(new TableColumnWrapTextCallback(headerResponseTableValue));
        // setting response header tableview row copy to clipboard functionality
        headerResponseTable.setOnKeyPressed(new TableRowCopyKeyEventHandler());

        // initialising request body type selector
        requestBodyTypeSelector.setItems(FXCollections.observableArrayList(REQUEST_BODY_TYPES));
        requestBodyTypeSelector.setValue(REQUEST_BODY_TYPES[0]);
        requestBodyTypeSelector.getSelectionModel()
                .selectedIndexProperty()
                .addListener(this::onRequestBodyTypeSelectorChanged);

        // initialising request body text type selector
        requestBodyTextTypeSelector.setItems(FXCollections.observableArrayList(REQUEST_BODY_TEXT_TYPES));
        requestBodyTextTypeSelector.setValue(REQUEST_BODY_TEXT_TYPES[0]);
        requestBodyTextTypeSelector.getSelectionModel()
                .selectedIndexProperty()
                .addListener(this::onRequestBodyTextTypeSelectorChanged);
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
