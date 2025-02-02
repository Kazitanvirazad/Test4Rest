package io.test4rest.app.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.test4rest.app.constants.CommonConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonUtil {
    private final static Logger log = LogManager.getLogger(JsonUtil.class);

    public static String prettifyJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Object object = gson.fromJson(json, Object.class);
            return gson.toJson(object);
        } catch (RuntimeException exception) {
            log.error(exception.getMessage());
        }
        return CommonConstants.EMPTY_STRING;
    }
}
