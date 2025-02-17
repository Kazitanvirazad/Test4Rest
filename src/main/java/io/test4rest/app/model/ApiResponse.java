package io.test4rest.app.model;

import io.test4rest.app.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static io.test4rest.app.constants.CommonConstants.EMPTY_STRING;

public class ApiResponse {
    private String body;
    private List<HeaderKeyValue> headers;
    private int statusCode;
    private long duration;
    private String prettyText;
    private String responseStatus;
    private boolean networkError;
    private String errorDisplayMessage;

    public ApiResponse(String body, long duration, String errorDisplayMessage, List<HeaderKeyValue> headers, boolean networkError, String prettyText, String responseStatus, int statusCode) {
        this.body = body;
        this.duration = duration;
        this.errorDisplayMessage = errorDisplayMessage;
        this.headers = headers;
        this.networkError = networkError;
        this.prettyText = prettyText;
        this.responseStatus = responseStatus;
        this.statusCode = statusCode;
    }

    public ApiResponse() {
        this.headers = new ArrayList<>();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<HeaderKeyValue> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HeaderKeyValue> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new ArrayList<>();
        }
        if (key != null) {
            HeaderKeyValue header = new HeaderKeyValue();
            header.setKey(key);
            header.setValue(StringUtils.hasText(value) ? value : EMPTY_STRING);
            this.headers.add(header);
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPrettyText() {
        return prettyText;
    }

    public void setPrettyText(String prettyText) {
        this.prettyText = prettyText;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getErrorDisplayMessage() {
        return errorDisplayMessage;
    }

    public void setErrorDisplayMessage(String errorDisplayMessage) {
        this.errorDisplayMessage = errorDisplayMessage;
    }

    public boolean isNetworkError() {
        return networkError;
    }

    public void setNetworkError(boolean networkError) {
        this.networkError = networkError;
    }
}
