package io.test4rest.app.helper;

import io.test4rest.app.model.ApiRequest;
import io.test4rest.app.model.ApiResponse;
import io.test4rest.app.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ApiServiceHelper {
    private final static Logger log = LogManager.getLogger(ApiServiceHelper.class);

    public static void addRequestHeaders(HttpURLConnection httpURLConnection, ApiRequest request) {
        Map<String, String> headers = request.getHeaders();
        if (!headers.isEmpty()) {
            for (String key : headers.keySet()) {
                httpURLConnection.setRequestProperty(key, headers.get(key));
            }
        }
    }

    public static void addRequestBodyToHttpURLConnection(HttpURLConnection httpURLConnection, ApiRequest request) throws IOException {
        if (StringUtils.hasText(request.getBody())) {
            httpURLConnection.setDoOutput(true);
            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
                outputStream.write(request.getBody().getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    public static void addResponseHeaders(HttpURLConnection httpURLConnection, ApiResponse response) {
        Map<String, List<String>> responseHeaders = httpURLConnection.getHeaderFields();
        if (!responseHeaders.isEmpty()) {
            for (Map.Entry<String, List<String>> headerEntry : responseHeaders.entrySet()) {
                String key = headerEntry.getKey();
                StringBuilder values = new StringBuilder();
                List<String> valueList = headerEntry.getValue();
                valueList.forEach(val -> {
                    if (!values.isEmpty()) {
                        values.append(",");
                        values.append(val);
                    } else {
                        values.append(val);
                    }
                });
                if (StringUtils.hasText(key)) {
                    response.addHeader(key, values.toString());
                }
            }
        }
    }

    public static String getResponseBody(InputStream inputStream) throws IOException {
        StringBuilder responseBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            reader.lines().forEach(responseBody::append);
        }
        return responseBody.toString();
    }
}
