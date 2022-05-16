package com.imageanalysis.commons.util.dynamikax.msuser;

import com.imageanalysis.commons.errors.AppException;
import com.imageanalysis.commons.errors.ProjectError;
import com.imageanalysis.commons.util.cache.Cache;
import com.imageanalysis.commons.util.cache.InMemoryCache;
import com.imageanalysis.commons.util.dynamikax.MSResponse;
import com.imageanalysis.commons.util.dynamikax.Microservice;
import com.imageanalysis.commons.util.dynamikax.Profile;
import com.imageanalysis.commons.util.java.Maps;
import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotBlank;

import static com.imageanalysis.commons.util.dynamikax.msuser.MsUserClient.MsUserProperties;
import static java.util.Objects.requireNonNull;

/**
 * Responsible of generating JWT tokens.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean
@EnableConfigurationProperties(MsUserProperties.class)
@ConditionalOnProperty({"msuser.email", "msuser.password"})
public class MsUserClient {

    private final Profile            activeProfile;
    private final RestTemplate       restTemplate;
    private final MsUserProperties   props;
    private final Cache<MsUserToken> tokenCache = new InMemoryCache<>();

    /**
     * Generates the JWT token for the default user.
     *
     * @return The JWT token of the default user.
     */
    public String getJwtToken() {
        return getJwtToken(props.email, props.password);
    }

    /**
     * Generates the JWT token for the provided user.
     *
     * @return The JWT token of the provided user.
     */
    public String getJwtToken(String email, String password) {
        val token = tokenCache.get(email, () -> getMsUserToken(email, password));

        return requireNonNull(token).jwtToken;
    }

    @NotNull
    private MsUserToken getMsUserToken(String email, String password) {
        val url  = getBaseUrl() + "/api/user/authenticate-with-email-address-no-captcha";
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
            throw new AppException(ProjectError.MS_USER_LOGIN_FAILED.details(response));
        }

        return new MsUserToken(response.getBody().getData().jwt);
    }

    @NotNull
    protected String getBaseUrl() {
        if (Strings.hasText(props.baseUrl))
            return props.baseUrl;

        return Microservice.MS_USER.getAppEngineBaseUrl(activeProfile);
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
