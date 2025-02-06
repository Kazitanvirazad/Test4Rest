package io.test4rest.app.service;

import io.test4rest.app.model.ApiRequest;
import io.test4rest.app.util.StringUtils;
import okhttp3.HttpUrl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static io.test4rest.app.helper.ApiServiceHelper.addHttpScheme;
import static io.test4rest.app.helper.ApiServiceHelper.addQueryParamsToHttpUrl;
import static io.test4rest.app.helper.ApiServiceHelper.getHttpUrl;
import static io.test4rest.app.helper.ApiServiceHelper.hasHttpSchemeInUrlString;
import static io.test4rest.app.helper.ApiServiceHelper.isLocalHostInUrl;

public abstract class AbstractOkHttpApiService implements ApiService {
    private final static Logger log = LogManager.getLogger(AbstractOkHttpApiService.class);

    public Optional<String> constructUrl(String url, ApiRequest request) {
        if (!StringUtils.hasText(url)) {
            return Optional.empty();
        }
        if (!hasHttpSchemeInUrlString(url)) {
            url = addHttpScheme(url, isLocalHostInUrl(url));
        }

        Optional<HttpUrl> optionalHttpUrl = getHttpUrl(url);
        if (optionalHttpUrl.isEmpty()) {
            return Optional.empty();
        }

        HttpUrl httpUrl = optionalHttpUrl.get();

        addQueryParamsToHttpUrl(request, httpUrl);
        return Optional.of(httpUrl.url().toString());
    }
}
