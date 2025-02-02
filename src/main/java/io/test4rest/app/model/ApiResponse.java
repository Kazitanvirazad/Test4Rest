package io.test4rest.app.model;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {
    private String body;
    private Map<String, String> headers;
    private int statusCode;
    private long duration;
    private String prettyText;
    private String responseStatus;
    private boolean networkError;
    private String errorDisplayMessage;

    public ApiResponse(String body, long duration, String errorDisplayMessage, Map<String, String> headers, boolean networkError, String prettyText, String responseStatus, int statusCode) {
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
        this.headers = new HashMap<>();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
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
