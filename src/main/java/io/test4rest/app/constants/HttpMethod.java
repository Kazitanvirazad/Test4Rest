package io.test4rest.app.constants;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    HEAD("HEAD"),
    PATCH("PATCH");

    private final String httpMethodName;

    HttpMethod(String httpMethodName) {
        this.httpMethodName = httpMethodName;
    }

    public String getHttpMethodName() {
        return httpMethodName;
    }
}
