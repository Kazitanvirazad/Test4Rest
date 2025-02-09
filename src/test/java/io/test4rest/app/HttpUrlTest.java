package io.test4rest.app;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class HttpUrlTest {

    @Test
    @DisplayName(value = "Url with query params parse correctly")
    void urlWithQueryParamsParseCorrectly() {
        String url = "http://localhost:9090/api/hello?name=kazi&city=Bangalore";
        HttpUrl httpUrl = HttpUrl.parse(url);
        assertEquals("name=kazi&city=Bangalore", httpUrl.query());
    }

    @Test
    @DisplayName(value = "Url with query params with space parse correctly")
    void urlWithQueryParamsWithSpaceParseCorrectly() {
        String url = "http://localhost:9090/api/hello?n ame=kazi&city=Ba ngalore&ro le=Java Developer";
        HttpUrl httpUrl = HttpUrl.parse(url);
        assertEquals("n ame=kazi&city=Ba ngalore&ro le=Java Developer", httpUrl.query());
    }

    @Test
    @DisplayName(value = "Url with query params with space parse correctly with fragment")
    void urlWithQueryParamsWithSpaceParseCorrectlyWithFragment() {
        String url = "http://localhost:9090/api/hello?n ame=kazi&city=Ba ngalore&ro le=Java Developer#comments";
        HttpUrl httpUrl = HttpUrl.parse(url);
        assertEquals("n ame=kazi&city=Ba ngalore&ro le=Java Developer", httpUrl.query());
    }

    @Test
    @DisplayName(value = "Url with query params with space parse correctly with fragment and extra manually added query")
    void urlWithQueryParamsWithSpaceParseCorrectlyWithFragmentAndExtraManuallyAddedQuery() {
        String url = "http://localhost:9090/api/hello?n ame=kazi&city=Ba ngalore&ro le=Java Developer#comments";
        HttpUrl httpUrl = HttpUrl.parse(url);
        httpUrl = httpUrl.newBuilder().setQueryParameter("wife", "Tabasum").build();
        assertEquals("n ame=kazi&city=Ba ngalore&ro le=Java Developer&wife=Tabasum", httpUrl.query());
    }

    @Test
    @DisplayName(value = """
            Url with query params with space parse correctly with fragment and extra manually 
            added query should read correct fragment""")
    void urlWithQueryParamsWithSpaceParseCorrectlyWithFragmentAndExtraManuallyAddedQueryShouldReadCorrectFragment() {
        String url = "http://localhost:9090/api/hello?n ame=kazi&city=Ba ngalore&ro le=Java Developer#comments";
        HttpUrl httpUrl = HttpUrl.parse(url);
        httpUrl = httpUrl.newBuilder().setQueryParameter("wife", "Tabasum").build();
        assertEquals("comments", httpUrl.fragment());
    }

    @Test
    @DisplayName(value = """
            Url with query params with space parse correctly with manually added fragment""")
    void urlWithQueryParamsWithSpaceParseCorrectlyWithManuallyAddedFragment() {
        String url = "http://localhost:9090/api/hello?n ame=kazi&city=Ba ngalore&ro le=Java Developer";
        HttpUrl httpUrl = HttpUrl.parse(url);
        httpUrl = httpUrl.newBuilder().fragment("comments").build();
        assertEquals("comments", httpUrl.fragment());
    }

    @Test
    @DisplayName(value = "Null url string will throw NullPointerException")
    void nullUrlStringWillThrowNullPointerException() {
        String url = null;
        assertThrowsExactly(NullPointerException.class, () -> HttpUrl.parse(url));
    }

    @Test
    @DisplayName(value = "Invalid url with empty string will return null")
    void InvalidUrlWithEmptyStringWillReturnNull() {
        String url = "";
        assertNull(HttpUrl.parse(url));
    }

    @Test
    @DisplayName(value = "Invalid url with blank string will return null")
    void InvalidUrlWithBlankEmptyStringWillReturnNull() {
        String url = " ";
        assertNull(HttpUrl.parse(url));
    }

    @Test
    @DisplayName(value = "Invalid url with invalid data will return null")
    void InvalidUrlWithInvalidDataWillReturnNull() {
        String url = "kazitanvirazad";
        assertNull(HttpUrl.parse(url));
    }

    @Test
    @DisplayName(value = "Invalid url with invalid data will return null")
    void InvalidUrlWithInvalidDataWillReturnNull2() {
        String url = "//kazitanvirazad";
        assertNull(HttpUrl.parse(url));
    }

    @Test
    @DisplayName(value = "Invalid url without http scheme will return null")
    void InvalidUrlWithoutHttpSchemeWillReturnNull() {
        String url = "www.google.com";
        assertNull(HttpUrl.parse(url));
    }

    @Test
    @DisplayName(value = "Valid url with http scheme will parse correctly")
    void validUrlWithHttpSchemeWillParseCorrectly() {
        String url = "http://www.google.com";
        HttpUrl httpUrl = HttpUrl.parse(url);
        assertEquals("http://www.google.com/", httpUrl.url().toString());
    }

    @Test
    @DisplayName(value = "Valid url will parse correctly with encoding done for spaces and other characters")
    void validUrlWillParseCorrectlyWithEncodingDoneForSpacesAndOtherCharacters() {
        String url = "http://www.google.com/name=kazi&role=java developer#comment";
        HttpUrl httpUrl = HttpUrl.parse(url);
        assertEquals("http://www.google.com/name=kazi&role=java%20developer#comment", httpUrl.url().toString());
    }

    @Test
    @DisplayName(value = "Add same query param manually already present in url will not replace the existing one and place it in the end of the url")
    void addSameQueryParamManuallyAlreadyPresentInUrlWillNotReplaceTheExistingOneAndPlaceItInTheEndOfTheUrl() {
        String url = "http://www.google.com?name=kazi&city=bangalore&role=java";
        HttpUrl httpUrl = HttpUrl.parse(url);
        assertEquals("http://www.google.com/?name=kazi&city=bangalore&role=java&name=tanvir", httpUrl.newBuilder()
                .addQueryParameter("name", "tanvir").build().url().toString());
    }

    @Test
    @DisplayName(value = "Add multiple same query param in url should remain same after parsing")
    void addMultipleSameQueryParamInUrlShouldRemainSameAfterParsing() {
        String url = "http://www.google.com?name=kazi&city=bangalore&name=kazi";
        HttpUrl httpUrl = HttpUrl.parse(url);
        assertEquals("http://www.google.com/?name=kazi&city=bangalore&name=kazi", httpUrl.url().toString());
    }

    @Test
    @DisplayName(value = "Add multiple same query param with different value in url should remain same after parsing")
    void addMultipleSameQueryParamWithDifferentValueInUrlShouldRemainSameAfterParsing() {
        String url = "http://www.google.com?name=kazi&city=bangalore&name=fahim";
        HttpUrl httpUrl = HttpUrl.parse(url);
        assertEquals("http://www.google.com/?name=kazi&city=bangalore&name=fahim", httpUrl.url().toString());
    }

    @Test
    @DisplayName(value = "Adding multiple same query param manually will be not be replaced by next similar value")
    void addingMultipleSameQueryParamManuallyWillNotBeReplacedByNextSimilarLastValue() {
        String url = "http://www.google.com/";
        HttpUrl httpUrl = HttpUrl.parse(url);
        assertEquals("http://www.google.com/?name=tanvir&role=java&name=tanvir", httpUrl.newBuilder()
                .addQueryParameter("name", "tanvir")
                .addQueryParameter("role", "java")
                .addQueryParameter("name", "tanvir").build().url().toString());
    }

    @Test
    @DisplayName(value = "Should parse url with only key")
    void shouldParseUrlWithOnlyKey() {
        String url = "http://www.google.com?name";
        HttpUrl httpUrl = HttpUrl.parse(url);
        assertEquals("http://www.google.com/?name", httpUrl.url().toString());

    }

    @Test
    @DisplayName(value = "Should parse url with only key and return key")
    void shouldParseUrlWithOnlyKeyAndReturnKey() {
        String url = "http://www.google.com?name";
        HttpUrl httpUrl = HttpUrl.parse(url);
        assertEquals("name", httpUrl.query());
    }
}
