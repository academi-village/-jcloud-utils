package com.github.academivillage.jcloud.spring;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.academivillage.jcloud.gcp.CloudStorage;
import com.github.academivillage.jcloud.gcp.sdk.GcpSdk;
import com.github.academivillage.jcloud.util.Serializer;
import com.github.academivillage.jcloud.util.dynamikax.AppExceptionHandler;
import com.github.academivillage.jcloud.util.dynamikax.Profile;
import com.github.academivillage.jcloud.util.dynamikax.msuser.MsUserClient;
import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.spring.core.GcpEnvironment;
import com.google.cloud.spring.core.GcpEnvironmentProvider;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.function.Function;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Import({MsUserClient.class, AppExceptionHandler.class})
public class JCloudAutoConfiguration {

    private final Environment env;

    /**
     * @param gcpProjectIdProvider See:  <a href="https://googlecloudplatform.github.io/spring-cloud-gcp/2.0.10/reference/html/index.html#project-id">Fetching Project ID</a>.
     *                             For local development add {@code spring.cloud.gcp.project-id=dynamikax-dev} to the {@code application.properties}.
     *                             Or set the environment variable {@code export GOOGLE_CLOUD_PROJECT=dynamikax-dev}.<br><br>
     * @param credentialsProvider  See:  <a href="https://googlecloudplatform.github.io/spring-cloud-gcp/2.0.10/reference/html/index.html#credentials">Fetching Credentials</a>.
     *                             For local development add {@code spring.cloud.gcp.credentials.location=file:/path/to/private_key.json} to the {@code application.properties}.
     *                             Or set the environment variable {@code export GOOGLE_APPLICATION_CREDENTIALS=/path/to/private_key.json}.
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

    /**
     * @param cloudKeyLocalPath For local development the {@code app.cloudkey.local.path} must be set in the
     *                          {@code application-local.properties}. Example: {@code /Users/username/projects/ia-grp/data/CloudKey/jwtRS256.key.pub}.
     *                          You could also set this property from environment variable: {@code export APP_CLOUDKEY_LOCAL_PATH=/Users/username/projects/ia-grp/data/CloudKey/jwtRS256.key.pub}
     */
    @Bean
    @SneakyThrows
    @ConditionalOnMissingBean
    public PublicKey publicKey(GcpEnvironmentProvider gcpEnvironmentProvider,
                               Profile activeProfile,
                               CloudStorage storage,
                               @Value("${app.cloudkey.storage.bucket:}") String cloudKeyBucketName,
                               @Value("${app.cloudkey.storage.path:dynamikax/cloudKeys/jwtRS256.key.pub}") String cloudKeyStoragePath,
                               @Value("${app.cloudkey.local.path:}") String cloudKeyLocalPath
    ) {
        val gcpEnv = gcpEnvironmentProvider.getCurrentEnvironment();
        log.debug("Gcp Environment: {} - Active Profile: {}", gcpEnv, activeProfile);
        File cloudKeyFile;
        if (gcpEnv != GcpEnvironment.UNKNOWN) {
            // Production
            val bucketName = StringUtils.hasText(cloudKeyBucketName) ? cloudKeyBucketName :
                             (activeProfile == Profile.PROD ? "dynamikax.appspot.com" : "dynamikax-dev.appspot.com");
            cloudKeyFile = storage.downloadInFile(bucketName, cloudKeyStoragePath);
        } else {
            // Local development server
            Assert.hasText(cloudKeyLocalPath, "The <app.cloudkey.local.path> is not set for local development. " +
                                              "Example path: </Users/username/projects/ia-grp/data/CloudKey/jwtRS256.key.pub>. " +
                                              "You could also set this property from environment variable: export APP_CLOUDKEY_LOCAL_PATH=/Users/username/projects/ia-grp/data/CloudKey/jwtRS256.key.pub");
            cloudKeyFile = new File(cloudKeyLocalPath);
        }

        val spec = new X509EncodedKeySpec(Files.readAllBytes(cloudKeyFile.toPath()));
        val kf   = KeyFactory.getInstance("RSA");

        return kf.generatePublic(spec);
    }
}
