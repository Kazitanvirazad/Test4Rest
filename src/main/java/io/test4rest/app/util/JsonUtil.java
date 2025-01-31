package io.test4rest.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.test4rest.app.constants.CommonConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonUtil {
    private final static Logger log = LogManager.getLogger(JsonUtil.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String prettifyJson(String json) {
        try {
            JsonNode jsonNode = mapper.readValue(json, JsonNode.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (JsonProcessingException exception) {
            log.error(exception.getMessage());
        }
        return CommonConstants.EMPTY_STRING;
    }
}
