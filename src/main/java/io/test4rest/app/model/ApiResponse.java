package io.test4rest.app.model;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {
    private String body;
    private Map<String, String> headers;
    private int statusCode;
    private long duration;
    private String prettyText;

    public ApiResponse(String body, Map<String, String> headers, int statusCode, long duration, String prettyText) {
        this.body = body;
        this.headers = headers;
        this.statusCode = statusCode;
        this.duration = duration;
        this.prettyText = prettyText;
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
}
