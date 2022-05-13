package com.imageanalysis.commons.util.dynamikax;

import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Contains HTTP client configurations.
 *
 * @author Younes Rahimi
 */
@Configuration
@ConfigurationProperties(prefix = "http-client")
public class HttpClientConfiguration {

    /**
     * Represents the connecting timeout of a request.
     */
    @Setter
    private Duration connectTimeout = Duration.ofSeconds(5);

    /**
     * Represents the reading timeout of a request.
     */
    @Setter
    private Duration readTimeout = Duration.ofSeconds(60);

    /**
     * Registers a bean of type [RestTemplate] with customized timeouts.
     */
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.setConnectTimeout(connectTimeout).setReadTimeout(readTimeout).build();
    }
}
