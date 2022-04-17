package com.github.academivillage.jcloud.util.dynamikax;

import com.github.academivillage.jcloud.gcp.cloudrun.GcpCloud;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Profile {
    DEVELOP("develop", "develop", "dynamikax-dev", "develop-dot-"),
    UAT("uat", "uat", "dynamikax-dev", "uat-dot-"),
    PROD("master", "prod", "dynamikax", ""),
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
     * Note: It's better to get this value from {@link GcpCloud}.
     */
    private final String projectId;

    /**
     * Represents the branch name in Git repository.
     */
    private final String baseUrlPrefix;

    /**
     * @param microserviceName Represents the microservice name. Example: {@code msuser} or {@code msqualitycontrol}
     * @return The base URL of microservice on App Engine.
     * Example: {@code https://develop-dot-msuser-dot-dynamikax-dev.ew.r.appspot.com }
     */
    public String getAppEngineMsBaseUrl(String microserviceName) {
        return "https://" + baseUrlPrefix + microserviceName + "-dot-" + projectId + ".ew.r.appspot.com";
    }
}
