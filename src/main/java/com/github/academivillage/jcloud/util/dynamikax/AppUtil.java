package com.github.academivillage.jcloud.util.dynamikax;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUtil {

    private final Environment env;

    public String getStorageBucketName() {
        String defaultBucketName = "dynamikax-storage-eu";
        for (final String profile : env.getActiveProfiles()) {
            if (profile.equals("develop"))
                return defaultBucketName;

            if (profile.equals("uat"))
                return "dynamikax-storage-eu-uat";

            if (profile.equals("prod"))
                return "dynamikax-storage-eu-prd";
        }

        return defaultBucketName;
    }
}
