package com.github.academivillage.jcloud.gcp.appengine8;

import com.github.academivillage.jcloud.gcp.CloudMetadata;
import com.github.academivillage.jcloud.gcp.CloudStorage;
import com.github.academivillage.jcloud.gcp.Scope;
import com.github.academivillage.jcloud.gcp.cloudrun.GcpCloudRun;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityService.GetAccessTokenResult;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static java.util.Collections.singletonList;

/**
 * The default implementation of {@link CloudMetadata} and {@link CloudStorage} for App Engine Standard Java 8.
 */
@Slf4j
public class GcpAeStandard8 implements CloudMetadata, CloudStorage {

    private final String serviceAccountName;

    private final AccessTokenPool accessTokenPool;

    /**
     * Used to work with Google Cloud Storage
     */
    private final GcpCloudRun gcpCloudRun;

    public GcpAeStandard8(AppIdentityService appIdentityService, GcpCloudRun gcpCloudRun) {
        this.serviceAccountName = appIdentityService.getServiceAccountName();
        this.accessTokenPool    = new AccessTokenPool(appIdentityService);
        this.gcpCloudRun        = gcpCloudRun;
    }

    public GcpAeStandard8() {
        this(AppIdentityServiceFactory.getAppIdentityService(), new GcpCloudRun());
    }

    @Override
    public Optional<String> getProjectId() {
        return Optional.ofNullable(SystemProperty.applicationId.get());
    }

    @Override
    public String serviceAccountName() {
        return serviceAccountName;
    }

    @Override
    public String getSignedUrl(String bucketName, String storagePath, Duration expiration, Scope scope) {
        GetAccessTokenResult accessToken = accessTokenPool.getAccessToken(singletonList(scope.getScopeUrl()));

        String BASE_URL = "https://storage.googleapis.com/" + storagePath;
        return BASE_URL + "?GoogleAccessId=" + serviceAccountName
               + "&Expires=" + Instant.now().plus(expiration).getEpochSecond()
               + "&access_token=" + URLEncoder.encode(accessToken.getAccessToken());
    }

    @Override
    public byte[] downloadInMemory(String bucketName, String storagePath) {
        return gcpCloudRun.downloadInMemory(bucketName, storagePath);
    }

    @Override
    public File downloadInFile(String bucketName, String storagePath) {
        return gcpCloudRun.downloadInFile(bucketName, storagePath);
    }

    @Override
    public void uploadFile(String bucketName, String storagePath, byte[] fileBytes) {
        gcpCloudRun.uploadFile(bucketName, storagePath, fileBytes);
    }
}
