package io.test4rest.app.model;

import io.test4rest.app.constants.http.HttpMethod;
import io.test4rest.app.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static io.test4rest.app.constants.CommonConstants.EMPTY_STRING;

public class ApiRequest {
    private String url;
    private HttpMethod method;
    private List<HeaderKeyValue> headers;
    private String body;
    private List<KeyValue> queryParams;

    public ApiRequest(String url, HttpMethod method, List<HeaderKeyValue> headers, String body, List<KeyValue> queryParams) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.queryParams = queryParams;
    }

    public ApiRequest() {
        this.headers = new ArrayList<>();
        this.queryParams = new ArrayList<>();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<HeaderKeyValue> getHeaders() {
        if (this.headers == null) {
            this.headers = new ArrayList<>();
        }
        return headers;
    }

    public void setHeaders(List<HeaderKeyValue> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new ArrayList<>();
        }
        if (StringUtils.hasText(key)) {
            HeaderKeyValue header = new HeaderKeyValue();
            header.setKey(key.trim());
            header.setValue(StringUtils.hasText(value) ? value.trim() : EMPTY_STRING);
            this.headers.add(header);
        }
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

    public List<KeyValue> getQueryParams() {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<>();
        }
        return queryParams;
    }

    public void setQueryParams(List<KeyValue> queryParams) {
        this.queryParams = queryParams;
    }

    public void addQueryParam(String key, String value) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<>();
        }
        if (StringUtils.hasText(key)) {
            KeyValue query = new KeyValue();
            query.setKey(key);
            query.setValue(StringUtils.hasText(value) ? value : EMPTY_STRING);
            this.queryParams.add(query);
        }
    }
}
