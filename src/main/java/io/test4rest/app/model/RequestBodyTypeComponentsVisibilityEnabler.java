package io.test4rest.app.model;

public class RequestBodyTypeComponentsVisibilityEnabler {
    private boolean enableNoRequestBodyInfoText;
    private boolean enableXFormUrlEncodedTableView;
    private boolean enableRequestBodyTextInputTextArea;

    public void enableNoRequestBodyInfoText() {
        this.enableNoRequestBodyInfoText = true;
        this.enableRequestBodyTextInputTextArea = false;
        this.enableXFormUrlEncodedTableView = false;
    }

    public void enableRequestBodyTextInputTextArea() {
        this.enableNoRequestBodyInfoText = false;
        this.enableRequestBodyTextInputTextArea = true;
        this.enableXFormUrlEncodedTableView = false;
    }

    public void enableXFormUrlEncodedTableView() {
        this.enableNoRequestBodyInfoText = false;
        this.enableRequestBodyTextInputTextArea = false;
        this.enableXFormUrlEncodedTableView = true;
    }

    public boolean isEnableNoRequestBodyInfoText() {
        return enableNoRequestBodyInfoText;
    }

    public boolean isEnableRequestBodyTextInputTextArea() {
        return enableRequestBodyTextInputTextArea;
    }

    public boolean isEnableXFormUrlEncodedTableView() {
        return enableXFormUrlEncodedTableView;
    }
}
