package com.github.academivillage.jcloud.gcp.appengine8;

import com.github.academivillage.jcloud.gcp.CloudMetadata;
import com.github.academivillage.jcloud.gcp.CloudStorage;
import com.github.academivillage.jcloud.gcp.Scope;
import com.github.academivillage.jcloud.gcp.sdk.GcpSdk;
import com.github.academivillage.jcloud.util.FileUtil;
import com.github.academivillage.jcloud.util.java.Lists;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityService.GetAccessTokenResult;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The default implementation of {@link CloudMetadata} and {@link CloudStorage} for App Engine Standard Java 8.
 */
@Slf4j
public class GcpAeStandard8 implements CloudMetadata, CloudStorage {

    private static final int    BUFFER_SIZE = 4096;
    private final        String serviceAccountName;

    private final AccessTokenPool accessTokenPool;

    /**
     * Used to work with Google Cloud Storage
     */
    private final GcpSdk gcpSdk;

    public GcpAeStandard8(AppIdentityService appIdentityService, GcpSdk gcpSdk) {
        this.serviceAccountName = appIdentityService.getServiceAccountName();
        this.accessTokenPool    = new AccessTokenPool(appIdentityService);
        this.gcpSdk             = gcpSdk;
    }

    public GcpAeStandard8() {
        this(AppIdentityServiceFactory.getAppIdentityService(), new GcpSdk());
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
        log.debug("About to create a signed URL for resource in Google Storage path {}/{} using AppEngine Java8 STD - Expiration: {}, Scope: {}",
                bucketName, storagePath, expiration, scope);
        storagePath = fixPath(storagePath);
        GetAccessTokenResult accessToken = accessTokenPool.getAccessToken(Lists.of(scope.getScopeUrl()));

        String BASE_URL = "https://storage.googleapis.com/" + bucketName + "/" + storagePath;
        return BASE_URL + "?GoogleAccessId=" + serviceAccountName
               + "&Expires=" + Instant.now().plus(expiration).getEpochSecond()
               + "&access_token=" + URLEncoder.encode(accessToken.getAccessToken());
    }

    @Override
    public String getSignedUrl(String bucketName, String directoryPrefix, Pattern fileNamePattern, Duration expiration, Scope scope) {
        return gcpSdk.getSignedUrl(bucketName, directoryPrefix, fileNamePattern, expiration, scope);
    }

    @Override
    @SneakyThrows
    public byte[] downloadInMemory(String bucketName, String storagePath) {
        log.debug("About to download file in memory from Google Storage path {}/{} using AppEngine Java8 STD", bucketName, storagePath);
        GcsFilename fileName   = new GcsFilename(bucketName, storagePath);
        GcsService  gcsService = GcsServiceFactory.createGcsService();
        try (val channel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
             val bos = new ByteArrayOutputStream()) {
            int bytesRead;
            val buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while ((bytesRead = channel.read(buffer)) > 0) {
                bos.write(buffer.array(), 0, bytesRead);
                buffer.clear();
            }
            return bos.toByteArray();
        }
    }

    @Override
    @SneakyThrows
    public File downloadInFile(String bucketName, String storagePath) {
        log.debug("About to download file from Google Storage path {}/{} using AppEngine Java8 STD", bucketName, storagePath);
        GcsFilename fileName   = new GcsFilename(bucketName, storagePath);
        GcsService  gcsService = GcsServiceFactory.createGcsService();
        File        file       = File.createTempFile("gcp-storage", FileUtil.getFileName(storagePath));
        try (val channel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
             val bos = new FileOutputStream(file)) {
            int bytesRead;
            val buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while ((bytesRead = channel.read(buffer)) > 0) {
                bos.write(buffer.array(), 0, bytesRead);
                buffer.clear();
            }
            return file;
        }
    }

    @Override
    public File downloadInFile(String bucketName, String directoryPrefix, Pattern fileNamePattern) {
        return gcpSdk.downloadInFile(bucketName, directoryPrefix, fileNamePattern);
    }

    @Override
    public void uploadFile(String bucketName, String storagePath, byte[] fileBytes) {
        gcpSdk.uploadFile(bucketName, storagePath, fileBytes);
    }

    @Override
    public void uploadFile(String bucketName, String storagePath, File file) {
        gcpSdk.uploadFile(bucketName, storagePath, file);
    }

    /**
     * Drops the leading {@code /} for the storage path.
     *
     * @return The fixed storage path.
     */
    private String fixPath(String storagePath) {
        if (storagePath.startsWith("/"))
            return storagePath.substring(1);

        return storagePath;
    }
}
