package com.github.academivillage.jcloud.util.dynamikax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.academivillage.jcloud.gcp.cloudrun.GcpCloud;
import com.github.academivillage.jcloud.util.cache.Cache;
import com.github.academivillage.jcloud.util.cache.InMemoryCache;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import static com.github.academivillage.jcloud.util.dynamikax.MsUserClient.MsUserProperties;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(MsUserProperties.class)
public class MsUserClient {

    private final ObjectMapper       objectMapper;
    private final RestTemplate       restTemplate;
    private final Cache<MsUserToken> tokenCache = new InMemoryCache<>();

    public String getJwtTokenUsingEmail(String email, String password) {
        val token = tokenCache.get(email, () -> {

        });
    }

    @Setter
    @ToString
    @ConfigurationProperties("msuser")
    public static class MsUserProperties {

        private String baseUrl;

        private String defaultUsername = "iag.microservice@ia-grp.com";

        @Getter
        @ToString.Exclude
        private String defaultPassword;

        public String getBaseUrl() {
            if (StringUtils.hasText(baseUrl))
                return baseUrl;

            new GcpCloud().get
        }
    }
}
