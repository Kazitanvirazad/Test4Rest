package io.test4rest.app.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class RequestBodyTypeComponentsVisibilityEnabler {
    private final BooleanProperty enableNoRequestBodyInfoText;
    private final BooleanProperty enableXFormUrlEncodedTableView;
    private final BooleanProperty enableRequestBodyTextInputTextArea;

    public RequestBodyTypeComponentsVisibilityEnabler() {
        this.enableNoRequestBodyInfoText = new SimpleBooleanProperty(false);
        this.enableXFormUrlEncodedTableView = new SimpleBooleanProperty(false);
        this.enableRequestBodyTextInputTextArea = new SimpleBooleanProperty(false);
    }

    public void enableNoRequestBodyInfoText() {
        this.enableNoRequestBodyInfoText.setValue(true);
        this.enableRequestBodyTextInputTextArea.setValue(false);
        this.enableXFormUrlEncodedTableView.setValue(false);
    }

    public void enableRequestBodyTextInputTextArea() {
        this.enableNoRequestBodyInfoText.setValue(false);
        this.enableRequestBodyTextInputTextArea.setValue(true);
        this.enableXFormUrlEncodedTableView.setValue(false);
    }

    public void enableXFormUrlEncodedTableView() {
        this.enableNoRequestBodyInfoText.setValue(false);
        this.enableRequestBodyTextInputTextArea.setValue(false);
        this.enableXFormUrlEncodedTableView.setValue(true);
    }

    public BooleanProperty isEnableNoRequestBodyInfoText() {
        return enableNoRequestBodyInfoText;
    }

    public BooleanProperty isEnableRequestBodyTextInputTextArea() {
        return enableRequestBodyTextInputTextArea;
    }

    public BooleanProperty isEnableXFormUrlEncodedTableView() {
        return enableXFormUrlEncodedTableView;
    }
}
