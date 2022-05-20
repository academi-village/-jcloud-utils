package com.imageanalysis.commons.util.dynamikax.msuser;

import com.imageanalysis.commons.util.cache.Cache;
import com.imageanalysis.commons.util.cache.InMemoryCache;
import com.imageanalysis.commons.util.dynamikax.Microservice;
import com.imageanalysis.commons.util.dynamikax.RestClient;
import com.imageanalysis.commons.util.dynamikax.msuser.dto.MsUserToken;
import com.imageanalysis.commons.util.java.Maps;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

import static com.imageanalysis.commons.util.dynamikax.msuser.DefaultMsUserClient.MsUserProperties;
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
public class DefaultMsUserClient implements MsUserClient {

    private final MsUserProperties   props;
    private final Cache<MsUserToken> tokenCache = new InMemoryCache<>();

    public RestClient restClient;

    @Override
    public String getJwtToken() {
        return getJwtToken(props.email, props.password);
    }

    @Override
    public String getJwtToken(String email, String password) {
        val token = tokenCache.get(email, () -> getMsUserToken(email, password));

        return requireNonNull(token).jwtToken;
    }

    @NotNull
    private MsUserToken getMsUserToken(String email, String password) {
        val path     = "/api/user/authenticate-with-email-address-no-captcha";
        val body     = Maps.of("emailAddress", email, "password", password);
        val response = restClient.noAuth().post(path).body(body).execute(LoginResponse.class);

        return new MsUserToken(response.jwt);
    }

    public void setRestClient(RestClient restClient) {
        this.restClient = restClient.forMs(Microservice.MS_USER);
    }

    @Setter
    @Validated
    @ConfigurationProperties("msuser")
    public static class MsUserProperties {

        @NotBlank
        private String email;

        @NotBlank
        private String password;
    }

    @ToString
    public static class LoginResponse {
        public String jwt;
    }
}
