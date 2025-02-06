package io.test4rest.app.util;

import io.test4rest.app.model.ApiRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.test4rest.app.constants.CommonConstants.AMPERSAND_CHAR;
import static io.test4rest.app.constants.CommonConstants.EMPTY_STRING;
import static io.test4rest.app.constants.CommonConstants.EQUALS_CHAR;
import static io.test4rest.app.constants.CommonConstants.SPLIT_URL_AND_QUERY_PARAM_REGEX;

public class URLUtils {
    private final static Logger log = LogManager.getLogger(URLUtils.class);

    public static StringBuilder getQueryParam(ApiRequest request) {
        StringBuilder queries = new StringBuilder();
        Map<String, String> queryParams = request.getQueryParams();
        if (!queryParams.isEmpty()) {
            for (String key : queryParams.keySet()) {
                StringBuilder query = new StringBuilder();
                String value = queryParams.get(key);
                query.append(key.trim()).append(EQUALS_CHAR).append(StringUtils.hasText(value) ? value : EMPTY_STRING);
                if (!queries.isEmpty()) {
                    queries.append(AMPERSAND_CHAR);
                }
                queries.append(query);
            }
        }
        return queries;
    }

    public static String[] splitUrlAndQueryParam(String url) {
        String[] res = new String[2];
        if (StringUtils.hasText(url)) {
            String[] splitted = url.split(SPLIT_URL_AND_QUERY_PARAM_REGEX, 2);
            res[0] = splitted[0];
            if (splitted.length == 1) {
                return res;
            }
            res[1] = splitted[1];
        }
        return res;
    }

    public static String joinQueryParams(String... params) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : params) {
            string = string.trim();
            if (string.isEmpty()) {
                continue;
            }
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append(AMPERSAND_CHAR);
            }
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    public static String urlEncode(String data) {
        if (StringUtils.hasText(data)) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                String urlEncoder = URLEncoder.encode(data.trim(), StandardCharsets.UTF_8);
                if (StringUtils.hasText(urlEncoder)) {
                    stringBuilder.append(urlEncoder);
                }
                return !stringBuilder.isEmpty() ? stringBuilder.toString() : EMPTY_STRING;
            } catch (RuntimeException exception) {
                log.warn(exception.getMessage());
            }
        }
        return EMPTY_STRING;
    }
}
