package com.github.academivillage.jcloud.gcp.appengine.standard.java8;

import com.github.academivillage.jcloud.gcp.CloudMetadata;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;

/**
 * The default implementation of {@link CloudMetadata} for App Engine Standard Java 8.
 */
public class AeStandard8CloudMetadata implements CloudMetadata {

    private final AppIdentityService appIdentityService;

    private final String serviceAccountName;

    public AeStandard8CloudMetadata(AppIdentityService appIdentityService) {
        this.appIdentityService = appIdentityService;
        this.serviceAccountName = appIdentityService.getServiceAccountName();
    }

    public AeStandard8CloudMetadata() {
        this(AppIdentityServiceFactory.getAppIdentityService());
    }

    @Override
    public String serviceAccountName() {
        return serviceAccountName;
    }
}
