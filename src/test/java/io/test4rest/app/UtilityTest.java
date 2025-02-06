package io.test4rest.app;

import io.test4rest.app.constants.CommonConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class UtilityTest {
    static String urlAndQueryParamSplitRegex;

    @BeforeAll
    static void setUp() {
        urlAndQueryParamSplitRegex = CommonConstants.SPLIT_URL_AND_QUERY_PARAM_REGEX;
    }

    @Test
    @DisplayName(value = "Regular expression will split url and query with space into two parts")
    void regularExpressionWillSplitUrlAndQueryWithSpaceIntoTwoParts() {
        String url = "http://localhost:9090/api/hello?name=Kazi Tanvir Azad&city=Bangalore&role=Java Developer";
        String[] parts = {"http://localhost:9090/api/hello", "name=Kazi Tanvir Azad&city=Bangalore&role=Java Developer"};
        assertArrayEquals(parts, url.split(urlAndQueryParamSplitRegex, 2));
    }

    @Test
    @DisplayName(value = "Regular expression will split url and query without space into two parts")
    void regularExpressionWillSplitUrlAndQueryWithoutSpaceIntoTwoParts() {
        String url = "http://localhost:9090/api/hello?name=KaziTanvirAzad&city=Bangalore&role=Java Developer";
        String[] parts = {"http://localhost:9090/api/hello", "name=KaziTanvirAzad&city=Bangalore&role=Java Developer"};
        assertArrayEquals(parts, url.split(urlAndQueryParamSplitRegex, 2));
    }

    @Test
    @DisplayName(value = "Regular expression will not split url with no query params into two parts")
    void regularExpressionWillNotSplitUrlWithNoQueryParamsIntoTwoParts() {
        String url = "http://localhost:9090/api/hello";
        String[] parts = {"http://localhost:9090/api/hello"};
        assertArrayEquals(parts, url.split(urlAndQueryParamSplitRegex, 2));
    }

    @Test
    @DisplayName(value = "Regular expression will not split url with no query params but having query character in the end into two parts")
    void regularExpressionWillNotSplitUrlWithNoQueryParamsButHavingQueryCharacterInTheEndIntoTwoParts() {
        String url = "http://localhost:9090/api/hello?";
        String[] parts = {"http://localhost:9090/api/hello", ""};
        assertArrayEquals(parts, url.split(urlAndQueryParamSplitRegex, 2));
    }

    @Test
    @DisplayName(value = "Regular expression will split without url but having query into two parts")
    void regularExpressionWillSplitWithoutUrlButHavingQueryIntoTwoParts() {
        String url = "?name=Kazi Tanvir Azad&city=Bangalore&role=Java Developer";
        String[] parts = {"", "name=Kazi Tanvir Azad&city=Bangalore&role=Java Developer"};
        assertArrayEquals(parts, url.split(urlAndQueryParamSplitRegex, 2));
    }

    @Test
    @DisplayName(value = "Regular expression will not split empty url and query into two parts")
    void regularExpressionWillNotSplitEmptyUrlAndQueryIntoTwoParts() {
        String url = "";
        String[] parts = {""};
        assertArrayEquals(parts, url.split(urlAndQueryParamSplitRegex, 2));
    }

    @Test
    @DisplayName(value = "Regular expression will split url and query into two parts")
    void regularExpressionWillSplitUrlAndQueryIntoTwoParts() {
        String url = null;
        assertThrowsExactly(NullPointerException.class, () -> url.split(urlAndQueryParamSplitRegex, 2));
    }

    @Test
    @DisplayName(value = "Regular expression will split url and query with multiplt query character in the url into two parts based on the first occurrence")
    void regularExpressionWillSplitUrlAndQueryWithMultipleQueryCharacterInTheUrlIntoTwoPartsBasedOnTheFirstOccurrence() {
        String url = "http://localhost:9090/api/hello?name=Kazi?Tanvir Azad&city=?Bangalore&role=Java Developer";
        String[] parts = {"http://localhost:9090/api/hello", "name=Kazi?Tanvir Azad&city=?Bangalore&role=Java Developer"};
        assertArrayEquals(parts, url.split(urlAndQueryParamSplitRegex, 2));
    }
}