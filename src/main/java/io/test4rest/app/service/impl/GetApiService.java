package io.test4rest.app.service.impl;

import io.test4rest.app.model.ApiRequest;
import io.test4rest.app.model.ApiResponse;
import io.test4rest.app.service.ApiService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static io.test4rest.app.constants.CommonConstants.AMPERSAND_CHAR;

public class GetApiService extends ApiService {
    private final static Logger log = LogManager.getLogger(GetApiService.class);

    @Override
    public ApiResponse callApi(ApiRequest request) {
        ApiResponse response = new ApiResponse();
        StringBuilder queries = getQueryParam(request);

        try {
            URI uri = new URI(request.getUrl());
            String query = uri.getQuery();
            if (query == null) {
                query = queries.toString();
            } else {
                query += AMPERSAND_CHAR + queries;
            }
            uri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), query, uri.getFragment());
            URL url = uri.toURL();
            long startTime = System.currentTimeMillis();
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // adding request headers
            addRequestHeaders(httpURLConnection, request);

            // adding request body
            addRequestBodyToHttpURLConnection(httpURLConnection, request);

            httpURLConnection.setRequestMethod(request.getMethod().getHttpMethodName());
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();

            response.setDuration(System.currentTimeMillis() - startTime);
            response.setStatusCode(httpURLConnection.getResponseCode());

            addResponseHeaders(httpURLConnection, response);

            String responseBody = getResponseBody(inputStream);
            response.setBody(responseBody);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
        return response;
    }
}
