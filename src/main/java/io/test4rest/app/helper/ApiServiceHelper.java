package io.test4rest.app.helper;

import io.test4rest.app.model.ApiRequest;
import io.test4rest.app.model.ApiResponse;
import io.test4rest.app.model.HeaderKeyValue;
import io.test4rest.app.model.KeyValue;
import io.test4rest.app.util.StringUtils;
import okhttp3.HttpUrl;
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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.test4rest.app.constants.CommonConstants.COMMA;
import static io.test4rest.app.constants.CommonConstants.EMPTY_SPACE;
import static io.test4rest.app.constants.CommonConstants.EMPTY_STRING;
import static io.test4rest.app.constants.CommonConstants.HTTPS_SCHEME;
import static io.test4rest.app.constants.CommonConstants.HTTP_SCHEME;
import static io.test4rest.app.constants.CommonConstants.LOCALHOST;

public class ApiServiceHelper {
    private final static Logger log = LogManager.getLogger(ApiServiceHelper.class);

    public static void addRequestHeaders(HttpURLConnection httpURLConnection, ApiRequest request) {
        List<HeaderKeyValue> headers = request.getHeaders();
        if (!headers.isEmpty()) {
            for (KeyValue keyValue : headers) {
                httpURLConnection.setRequestProperty(keyValue.getKey(), keyValue.getValue());
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
                        values.append(COMMA).append(EMPTY_SPACE).append(val);
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

    public static boolean hasHttpSchemeInUrlString(String url) {
        try {
            Pattern pattern = Pattern.compile(HTTP_SCHEME, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(url);
            return matcher.find();
        } catch (RuntimeException exception) {
            log.warn(exception.getMessage());
        }
        return false;
    }

    public static boolean isLocalHostInUrl(String url) {
        try {
            Pattern pattern = Pattern.compile(LOCALHOST, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(url);
            return matcher.find();
        } catch (RuntimeException exception) {
            log.warn(exception.getMessage());
        }
        return false;
    }

    public static String addHttpScheme(String url, boolean isLocalHostInUrl) {
        return isLocalHostInUrl ? HTTP_SCHEME : HTTPS_SCHEME + "://" + url;
    }

    public static Optional<HttpUrl> getHttpUrl(String url) {
        try {
            return Optional.ofNullable(HttpUrl.parse(url));
        } catch (RuntimeException exception) {
            log.warn(exception.getMessage());
        }
        return Optional.empty();
    }

    public static void addQueryParamsToHttpUrl(ApiRequest request, HttpUrl httpUrl) {
        try {
            for (KeyValue query : request.getQueryParams()) {
                if (StringUtils.hasText(query.getKey())) {
                    httpUrl = httpUrl.newBuilder().addQueryParameter(query.getKey(),
                            StringUtils.hasText(query.getValue()) ? query.getValue() : EMPTY_STRING).build();
                }
            }
        } catch (Exception exception) {
            log.warn(exception.getMessage());
        }
    }
}
