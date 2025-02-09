package io.test4rest.app.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.test4rest.app.model.ApiResponse;
import io.test4rest.app.model.KeyValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

import static io.test4rest.app.constants.CommonConstants.EMPTY_STRING;
import static io.test4rest.app.constants.http.HttpHeaders.CONTENT_TYPE;
import static io.test4rest.app.constants.http.MediaType.APPLICATION_JSON;
import static io.test4rest.app.constants.http.MediaType.APPLICATION_JSON_UTF8;

public class JsonUtils {
    private final static Logger log = LogManager.getLogger(JsonUtils.class);
    private final static Set<String> JSON_HEADER_MAPPER = new HashSet<>() {
        @Serial
        private static final long serialVersionUID = -2921413411820659939L;

        {
            add(APPLICATION_JSON.toLowerCase());
            add(APPLICATION_JSON_UTF8[0].toLowerCase());
            add(APPLICATION_JSON_UTF8[1].toLowerCase());
        }
    };

    public static String prettifyJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Object object = gson.fromJson(json, Object.class);
            return gson.toJson(object);
        } catch (RuntimeException exception) {
            log.error(exception.getMessage());
        }
        return EMPTY_STRING;
    }

    public static boolean isJsonResponse(ApiResponse response) {
        return response != null &&
                !response.getHeaders().isEmpty() &&
                mapJsonHeader(response);
    }

    private static boolean mapJsonHeader(ApiResponse response) {
        for (KeyValue keyValue : response.getHeaders()) {
            if (StringUtils.hasText(keyValue.getKey()) && StringUtils.hasText(keyValue.getValue())) {
                if (CONTENT_TYPE.equalsIgnoreCase(keyValue.getKey().trim()) && JSON_HEADER_MAPPER.contains(keyValue.getValue().trim().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
}
