package com.github.academivillage.jcloud.util.dynamikax;

import com.github.academivillage.jcloud.gcp.sdk.GcpSdk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Encapsulates the profile (or environment) specific information.
 *
 * @author Younes Rahimi
 */
@Getter
@RequiredArgsConstructor
public enum Profile {
    LOCAL("develop", "local", "dynamikax-dev", "develop-dot-", "dynamikax-storage-eu"),
    DEVELOP("develop", "develop", "dynamikax-dev", "develop-dot-", "dynamikax-storage-eu"),
    UAT("uat", "uat", "dynamikax-dev", "uat-dot-", "dynamikax-storage-eu-uat"),
    PROD("master", "prod", "dynamikax", "", "dynamikax-storage-eu-prd"),
    ;

    /**
     * Represents the branch name in Git repository.
     */
    private final String branchName;

    /**
     * Represents the (Spring) application's profile.
     */
    private final String appProfile;

    /**
     * Represents the GCP project ID. Example: {@code dynamikax-dev} or {@code dynamikax}
     * Note: It's better to get this value from {@link GcpSdk}.
     */
    private final String projectId;

    /**
     * Represents the branch name in Git repository.
     */
    private final String baseUrlPrefix;

    /**
     * Represents the corresponding bucket name on GCP Storage.
     */
    private final String storageBucketName;

    public static Optional<Profile> ofBranch(String branchName) {
        return Arrays.stream(Profile.values()).filter(it -> it.branchName.equals(branchName))
                .findFirst();
    }

    /**
     * @param microserviceName Represents the microservice name. Example: {@code msuser} or {@code msqualitycontrol}
     * @return The base URL of microservice on App Engine.
     * Example: {@code https://develop-dot-msuser-dot-dynamikax-dev.ew.r.appspot.com }
     */
    public String getAppEngineBaseUrl(String microserviceName) {
        return "https://" + baseUrlPrefix + microserviceName + "-dot-" + projectId + ".ew.r.appspot.com";
    }

    /**
     * @param microservice Represents the microservice on App Engine.
     * @return The base URL of microservice on App Engine.
     * Example: {@code https://develop-dot-msuser-dot-dynamikax-dev.ew.r.appspot.com }
     */
    public String getAppEngineBaseUrl(Microservice microservice) {
        return getAppEngineBaseUrl(microservice.getMsName());
    }
}
