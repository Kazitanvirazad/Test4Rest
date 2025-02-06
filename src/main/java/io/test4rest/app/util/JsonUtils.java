package io.test4rest.app.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.test4rest.app.model.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.test4rest.app.constants.CommonConstants.EMPTY_STRING;
import static io.test4rest.app.constants.http.HttpHeaders.CONTENT_TYPE;
import static io.test4rest.app.constants.http.MediaType.APPLICATION_JSON;

public class JsonUtils {
    private final static Logger log = LogManager.getLogger(JsonUtils.class);

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
                response.getHeaders().containsKey(CONTENT_TYPE) &&
                response.getHeaders().get(CONTENT_TYPE).equalsIgnoreCase(APPLICATION_JSON);
    }
}
