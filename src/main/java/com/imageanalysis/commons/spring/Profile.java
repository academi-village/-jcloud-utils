package com.imageanalysis.commons.spring;

import com.imageanalysis.commons.gcp.sdk.GcpSdk;
import com.imageanalysis.commons.util.dynamikax.Microservice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Optional;

/**
 * Encapsulates the profile (or environment) specific information.
 *
 * @author Younes Rahimi
 */
@RequiredArgsConstructor
public enum Profile {
    LOCAL("develop", "local", "dynamikax-dev", "develop-dot-", "dynamikax-storage-eu"),
    DEVELOP("develop", "develop", "dynamikax-dev", "develop-dot-", "dynamikax-storage-eu"),
    UAT("uat", "uat", "dynamikax-dev", "uat-dot-", "dynamikax-storage-eu-uat"),
    PROD("master", "prod", "dynamikax", "", "dynamikax-storage-eu-prd"),
    ;

    static Environment env;

    /**
     * Represents the branch name in Git repository.
     */
    private final String branchName;

    /**
     * Represents the (Spring) application's profile.
     */
    @Getter
    private final String appProfile;

    /**
     * Represents the GCP project ID. Example: {@code dynamikax-dev} or {@code dynamikax}
     * Note: It's better to get this value from {@link GcpSdk}.
     */
    private final String projectId;

    /**
     * Represents the app-engine micro-service base url prefix.
     */
    private final String baseUrlPrefix;

    /**
     * Represents the corresponding bucket name on GCP Storage.
     */
    private final String storageBucketName;

    /**
     * Represents the default username of the active profile. Mainly used as default entities auditor.
     */
    private final String defaultUsername = "iag.microservice";

    /**
     * Finds the profile by (Spring) application's profile.
     *
     * @param appProfile Represents the (Spring) application's profile.
     * @return The optional profile. May be empty.
     */
    public static Optional<Profile> fromAppProfile(String appProfile) {
        return Arrays.stream(Profile.values())
                .filter(it -> it.appProfile.equals(appProfile))
                .findFirst();
    }

    /**
     * @param microserviceName Represents the microservice name. Example: {@code msuser} or {@code msqualitycontrol}
     * @return The base URL of microservice on App Engine.
     * Example: {@code https://develop-dot-msuser-dot-dynamikax-dev.ew.r.appspot.com }
     */
    public String getAppEngineBaseUrl(String microserviceName) {
        val defaultBaseUrl = "https://" + baseUrlPrefix + microserviceName + "-dot-" + projectId + ".ew.r.appspot.com";
        return env == null ? defaultBaseUrl : env.getProperty(microserviceName + ".base-url", defaultBaseUrl);
    }

    /**
     * @param microservice Represents the microservice on App Engine.
     * @return The base URL of microservice on App Engine.
     * Example: {@code https://develop-dot-msuser-dot-dynamikax-dev.ew.r.appspot.com }
     */
    public String getAppEngineBaseUrl(Microservice microservice) {
        return getAppEngineBaseUrl(microservice.getMsName());
    }

    public String getBranchName() {
        return env == null ? branchName : env.getProperty("app.profile.branch-name", branchName);
    }

    public String getProjectId() {
        return env == null ? projectId : env.getProperty("app.profile.project-id", projectId);
    }

    public String getBaseUrlPrefix() {
        return env == null ? baseUrlPrefix : env.getProperty("app.profile.base-url-prefix", baseUrlPrefix);
    }

    public String getStorageBucketName() {
        return env == null ? storageBucketName : env.getProperty("app.profile.storage-bucket-name", storageBucketName);
    }

    public String getDefaultUsername() {
        return env == null ? defaultUsername : env.getProperty("app.profile.default-username", defaultUsername);
    }
}
