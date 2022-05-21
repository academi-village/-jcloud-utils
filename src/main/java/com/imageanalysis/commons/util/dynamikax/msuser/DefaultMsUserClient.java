package com.imageanalysis.commons.util.dynamikax.msuser;

import com.imageanalysis.commons.util.cache.Cache;
import com.imageanalysis.commons.util.cache.InMemoryCache;
import com.imageanalysis.commons.util.dynamikax.Microservice;
import com.imageanalysis.commons.util.dynamikax.RestClient;
import com.imageanalysis.commons.util.dynamikax.msuser.dto.MsUserToken;
import com.imageanalysis.commons.util.java.Maps;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.imageanalysis.commons.util.dynamikax.msuser.DefaultMsUserClient.MsUserProperties;
import static lombok.AccessLevel.PRIVATE;

/**
 * Responsible of generating JWT tokens.
 */
@Slf4j
@Component
@With(PRIVATE)
@AllArgsConstructor
@ConditionalOnMissingBean(MsUserClient.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
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

        return token.jwtToken;
    }

    @Nullable
    public String getUserName(List<Long> userIds) throws Exception {
        val path = "/api/user/get-users-by-ids";
        return restClient.put(path).body(userIds).execute().asJsonNode().findPath("userName").asText();
    }

    @NotNull
    private MsUserToken getMsUserToken(String email, String password) {
        val path = "/api/user/authenticate-with-email-address-no-captcha";
        val body = Maps.of("emailAddress", email, "password", password);
        val jwt  = restClient.noAuth().post(path).body(body).execute().asJsonNode().path("jwt").asText();

        return new MsUserToken(jwt);
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
}
