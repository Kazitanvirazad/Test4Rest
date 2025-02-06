package io.test4rest.app.model;

import io.test4rest.app.constants.http.HttpMethod;
import io.test4rest.app.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.test4rest.app.constants.CommonConstants.EMPTY_STRING;

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
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        if (StringUtils.hasText(key))
            this.headers.put(key, StringUtils.hasText(value) ? value : EMPTY_STRING);
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
        if (this.queryParams == null) {
            this.queryParams = new HashMap<>();
        }
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public void addQueryParam(String key, String value) {
        if (this.queryParams == null) {
            this.queryParams = new HashMap<>();
        }
        if (StringUtils.hasText(key))
            this.queryParams.put(key, StringUtils.hasText(value) ? value : EMPTY_STRING);
    }
}
