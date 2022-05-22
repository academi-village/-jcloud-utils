package com.imageanalysis.commons.util.dynamikax.msuser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.imageanalysis.commons.util.cache.Cache;
import com.imageanalysis.commons.util.cache.InMemoryCache;
import com.imageanalysis.commons.util.dynamikax.Microservice;
import com.imageanalysis.commons.util.dynamikax.RestClient;
import com.imageanalysis.commons.util.dynamikax.msuser.dto.MsUserToken;
import com.imageanalysis.commons.util.dynamikax.msuser.dto.UserDto;
import com.imageanalysis.commons.util.java.Lists;
import com.imageanalysis.commons.util.java.Maps;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

import static com.imageanalysis.commons.util.dynamikax.msuser.DefaultMsUserClient.MsUserProperties;
import static lombok.AccessLevel.PRIVATE;

/**
 * Responsible of generating JWT tokens.
 */
@Slf4j
@Component
@With(PRIVATE)
@AllArgsConstructor
//@ConditionalOnMissingBean(MsUserClient.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@EnableConfigurationProperties(MsUserProperties.class)
@ConditionalOnProperty({"msuser.email", "msuser.password"})
public class DefaultMsUserClient implements MsUserClient {

    private final MsUserProperties   props;
    private final Cache<MsUserToken> tokenCache = new InMemoryCache<>();

    private RestClient restClient;

    @Override
    public String getJwtToken() {
        return getJwtToken(props.email, props.password);
    }

    @Override
    public String getJwtToken(String email, String password) {
        val token = tokenCache.get(email, () -> getMsUserToken(email, password));

        return token.jwtToken;
    }

    public Optional<UserDto> fetchUser(Long userId) {
        val path      = "/api/user/get-users-by-ids";
        val usersType = new TypeReference<List<UserDto>>() {};
        return restClient.put(path).body(Lists.of(userId)).execute(usersType).stream().findFirst();
    }

    @NotNull
    private MsUserToken getMsUserToken(String email, String password) {
        val path = "/api/user/authenticate-with-email-address-no-captcha?compact=" + props.compactToken;
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

        @NotNull
        private Boolean compactToken = false;
    }
}
