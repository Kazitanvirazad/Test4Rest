package io.test4rest.app.service;

import io.test4rest.app.model.ApiRequest;
import io.test4rest.app.model.ApiResponse;

public interface ApiService {
    ApiResponse callApi(ApiRequest request);
}
