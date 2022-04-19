package com.github.academivillage.jcloud.util.dynamikax.msuser;

import com.github.academivillage.jcloud.errors.AppException;
import com.github.academivillage.jcloud.util.cache.Cache;
import com.github.academivillage.jcloud.util.cache.InMemoryCache;
import com.github.academivillage.jcloud.util.dynamikax.MSResponse;
import com.github.academivillage.jcloud.util.dynamikax.Microservice;
import com.github.academivillage.jcloud.util.dynamikax.Profile;
import com.github.academivillage.jcloud.util.java.Maps;
import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;

import static com.github.academivillage.jcloud.errors.JCloudError.MS_USER_LOGIN_FAILED;
import static com.github.academivillage.jcloud.util.dynamikax.msuser.MsUserClient.MsUserProperties;
import static java.util.Objects.requireNonNull;

/**
 * Responsible of generating JWT tokens.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(MsUserProperties.class)
public class MsUserClient {

    private final Profile            profile;
    private final RestTemplate       restTemplate;
    private final MsUserProperties   props;
    private final Cache<MsUserToken> tokenCache = new InMemoryCache<>();

    /**
     * Generates the JWT token for the default user.
     *
     * @return The JWT token of the default user.
     */
    public String getJwtToken() {
        return getJwtTokenUsingEmail(props.email, props.password);
    }

    /**
     * Generates the JWT token for the provided user.
     *
     * @return The JWT token of the provided user.
     */
    public String getJwtTokenUsingEmail(String email, String password) {
        val token = tokenCache.get(email, () -> getMsUserToken(email, password));

        return requireNonNull(token).jwtToken;
    }

    @NotNull
    private MsUserToken getMsUserToken(String email, String password) {
        val url = getBaseUrl()
                  + "/api/user/authenticate-with-email-address-no-captcha";
        val body = Maps.of("emailAddress", email, "password", password);

        val response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body),
                new ParameterizedTypeReference<MSResponse<LoginResponse>>() {});
        log.info("MsUser login response: {}", response);

        boolean failed = response.getStatusCodeValue() < 200
                         || response.getStatusCodeValue() > 299
                         || response.getBody() == null
                         || response.getBody().getResponseCode() != 200;
        if (failed) {
            log.error("Getting MsUser JWT token failed: {}", response);
            throw new AppException(MS_USER_LOGIN_FAILED.withDetails(response));
        }

        return new MsUserToken(response.getBody().getData().jwt);
    }

    @NotNull
    private String getBaseUrl() {
        if (Strings.hasText(props.baseUrl))
            return props.baseUrl;

        return Microservice.MS_USER.getAppEngineBaseUrl(profile);
    }

    @Setter
    @Validated
    @ConfigurationProperties("msuser")
    public static class MsUserProperties {

        @Nullable
        private String baseUrl;

        @NotBlank
        private String email;

        @NotBlank
        private String password;
    }

    @Setter
    @ToString
    public static class LoginResponse {
        public String jwt;
    }
}
