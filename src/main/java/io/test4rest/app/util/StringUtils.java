package io.test4rest.app.util;

public class StringUtils {
    public static boolean hasText(String string) {
        return (string != null && !string.isBlank());
    }
}
