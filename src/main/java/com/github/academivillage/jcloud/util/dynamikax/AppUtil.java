package com.github.academivillage.jcloud.util.dynamikax;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AppUtil {
    private static final Set<String> BRANCHES = getBranches();

    private final Environment env;

    private static Set<String> getBranches() {
        val branches = new HashSet<String>();
        branches.add("develop");
        branches.add("uat");
        branches.add("master");
//        branches.add("prod"); //TODO check this with Wassim

        return branches;
    }

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

    public Profile getProfile() {
        return Arrays.stream(env.getActiveProfiles()).filter(BRANCHES::contains).findFirst().orElse("develop");
    }

    /**
     * TODO May NOT needed. Check the usage.
     */
    public String getBranch() {
        return Arrays.stream(env.getActiveProfiles()).filter(BRANCHES::contains).findFirst().orElse("develop");
    }
}
