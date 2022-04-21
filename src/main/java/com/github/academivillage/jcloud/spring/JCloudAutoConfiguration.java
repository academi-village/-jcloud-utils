package com.github.academivillage.jcloud.spring;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.academivillage.jcloud.gcp.sdk.GcpSdk;
import com.github.academivillage.jcloud.util.Serializer;
import com.github.academivillage.jcloud.util.dynamikax.Profile;
import com.github.academivillage.jcloud.util.dynamikax.msuser.MsUserClient;
import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Import({MsUserClient.class,})
public class JCloudAutoConfiguration {

    private final Environment env;

    /**
     * @param gcpProjectIdProvider See:  <a href="https://googlecloudplatform.github.io/spring-cloud-gcp/2.0.10/reference/html/index.html#project-id">Fetching Project ID</a>.
     *                             For local development add {@code spring.cloud.gcp.project-id=dynamikax-dev} to the {@code application.properties}.
     *                             Or set the environment variable {@code GOOGLE_CLOUD_PROJECT=dynamikax-dev}.<br><br>
     * @param credentialsProvider  See:  <a href="https://googlecloudplatform.github.io/spring-cloud-gcp/2.0.10/reference/html/index.html#credentials">Fetching Credentials</a>.
     *                             For local development add {@code spring.cloud.gcp.credentials.location=file:/usr/local/key.json} to the {@code application.properties}.
     *                             Or set the environment variable {@code GOOGLE_APPLICATION_CREDENTIALS=/usr/local/key.json}.
     *                             Read more: <a href="https://cloud.google.com/docs/authentication/production#passing_variable">Passing credentials via environment variable</a>
     */
    @Bean
    @SneakyThrows
    public GcpSdk gcpSdk(GcpProjectIdProvider gcpProjectIdProvider, CredentialsProvider credentialsProvider) {
        return new GcpSdk(gcpProjectIdProvider.getProjectId(), credentialsProvider.getCredentials());
    }

    @Bean
    public Serializer serializer(ObjectMapper objectMapper) {
        return new Serializer(objectMapper);
    }

    @Bean
    public Profile activeProfile() {
        return Arrays.stream(env.getActiveProfiles())
                .map(Profile::ofBranch)
                .findFirst()
                .flatMap(Function.identity())
                .orElse(Profile.DEVELOP);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> builder
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .failOnUnknownProperties(false)
                .failOnEmptyBeans(false)
                .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .autoDetectFields(true)
                .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .modules(new Jdk8Module(), new JavaTimeModule())
                .dateFormat(new StdDateFormat());
    }
}
