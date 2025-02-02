package io.test4rest.app.service.impl;

import io.test4rest.app.helper.ApiServiceHelper;
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

public class DefaultApiServiceImpl implements ApiService {
    private final static Logger log = LogManager.getLogger(DefaultApiServiceImpl.class);
    private final ApiServiceHelper apiServiceHelper;

    public DefaultApiServiceImpl() {
        this.apiServiceHelper = new ApiServiceHelper();
    }

    @Override
    public ApiResponse callApi(ApiRequest request) {
        ApiResponse response = new ApiResponse();

        // creating query param string
        StringBuilder queries = apiServiceHelper.getQueryParam(request);

        try {
            URI uri = new URI(request.getUrl());
            String query = uri.getQuery();
            if (query != null || !queries.isEmpty()) {
                if (query == null) {
                    query = queries.toString();
                } else {
                    query += AMPERSAND_CHAR + queries;
                }
                uri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), query, uri.getFragment());
            }
            URL url = uri.toURL();
            long startTime = System.currentTimeMillis();
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // setting request method
            httpURLConnection.setRequestMethod(request.getMethod().getHttpMethodName());

            // adding request headers
            apiServiceHelper.addRequestHeaders(httpURLConnection, request);

            // adding request body
            apiServiceHelper.addRequestBodyToHttpURLConnection(httpURLConnection, request);
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();

            response.setDuration(System.currentTimeMillis() - startTime);
            response.setStatusCode(httpURLConnection.getResponseCode());

            apiServiceHelper.addResponseHeaders(httpURLConnection, response);

            String responseBody = apiServiceHelper.getResponseBody(inputStream);
            response.setBody(responseBody);
            response.setResponseStatus(httpURLConnection.getResponseMessage());
        } catch (Exception exception) {
            log.error(exception);
            response.setNetworkError(true);
            response.setErrorDisplayMessage(exception.toString());
        }
        return response;
    }
}
