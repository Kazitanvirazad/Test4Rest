package io.test4rest.app.constants.http;

import java.util.HashMap;
import java.util.Map;

import static io.test4rest.app.constants.http.MediaType.APPLICATION_JAVASCRIPT;
import static io.test4rest.app.constants.http.MediaType.APPLICATION_JSON;
import static io.test4rest.app.constants.http.MediaType.APPLICATION_XML;
import static io.test4rest.app.constants.http.MediaType.TEXT_HTML;
import static io.test4rest.app.constants.http.MediaType.TEXT_PLAIN;

/* static final arrays' elements sequence shall not be changed in case of adding new element in the array */
public final class ReqResConstants {
    public final static String[] REQUEST_BODY_TYPES =
            {"none", "form-data", "x-www-form-urlencoded", "raw", "binary"};
    public final static String[] REQUEST_BODY_TEXT_TYPES =
            {"Text", "Json", "Xml", "Html", "Javascript"};
    private final static Map<String, String> requestBodyTypeToMediaTypeMap = new HashMap<>();

    static {
        requestBodyTypeToMediaTypeMap.put("text", TEXT_PLAIN);
        requestBodyTypeToMediaTypeMap.put("json", APPLICATION_JSON);
        requestBodyTypeToMediaTypeMap.put("xml", APPLICATION_XML);
        requestBodyTypeToMediaTypeMap.put("html", TEXT_HTML);
        requestBodyTypeToMediaTypeMap.put("javascript", APPLICATION_JAVASCRIPT);
    }

    public static String mapRequestBodyTypeToMediaType(String selectedRequestBodyType) {
        return requestBodyTypeToMediaTypeMap.getOrDefault(selectedRequestBodyType.toLowerCase(), TEXT_PLAIN);
    }
}
