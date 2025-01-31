package io.test4rest.app.model;

import io.test4rest.app.constants.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class ApiRequest {
    private String url;
    private HttpMethod method;
    private Map<String, String> headers;
    private String body;
    private Map<String, String> queryParams;

    public ApiRequest(String url, HttpMethod method, Map<String, String> headers, String body, Map<String, String> queryParams) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.queryParams = queryParams;
    }

    public ApiRequest() {
        this.headers = new HashMap<>();
        this.queryParams = new HashMap<>();
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

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }
}
