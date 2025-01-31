package io.test4rest.app.helper;

import io.test4rest.app.constants.HttpMethod;
import io.test4rest.app.service.ApiService;
import io.test4rest.app.service.impl.GetApiService;

public class ApiServiceHelper {
    public static ApiService getApiService(HttpMethod method) {
        return switch (method) {
            case GET -> new GetApiService();
//            case POST -> null;
//            case PUT -> null;
//            case DELETE -> null;
//            case OPTIONS -> null;
//            case HEAD -> null;
//            case PATCH -> null;
            default -> new GetApiService();
        };
    }
}
