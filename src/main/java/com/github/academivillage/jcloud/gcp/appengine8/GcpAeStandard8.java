package com.github.academivillage.jcloud.gcp.appengine8;

import com.github.academivillage.jcloud.errors.AppException;
import com.github.academivillage.jcloud.errors.ProjectError;
import com.github.academivillage.jcloud.gcp.CloudMetadata;
import com.github.academivillage.jcloud.gcp.CloudStorage;
import com.github.academivillage.jcloud.gcp.StorageScope;
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
import com.google.common.io.BaseEncoding;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Pattern;

/**
 * The default implementation of {@link CloudMetadata} and {@link CloudStorage} for App Engine Standard Java 8.
 */
@Slf4j
public class GcpAeStandard8 implements CloudMetadata, CloudStorage {

    private static final int    BUFFER_SIZE = 4096;
    private final        String serviceAccountName;

    private final AppIdentityService appIdentityService;
    private final AccessTokenPool    accessTokenPool;

    /**
     * Used to work with Google Cloud Storage
     */
    private final GcpSdk gcpSdk;

    public GcpAeStandard8(AppIdentityService appIdentityService, GcpSdk gcpSdk) {
        this.serviceAccountName = appIdentityService.getServiceAccountName();
        this.appIdentityService = appIdentityService;
        this.accessTokenPool    = new AccessTokenPool(appIdentityService);
        this.gcpSdk             = gcpSdk;
    }

    public GcpAeStandard8() {
        this(AppIdentityServiceFactory.getAppIdentityService(), new GcpSdk());
    }

    @Nullable
    protected static String getAppEngineProjectIdFromAppId() {
        String projectId = SystemProperty.applicationId.get();
        if (projectId != null && projectId.contains(":")) {
            int colonIndex = projectId.indexOf(":");
            projectId = projectId.substring(colonIndex + 1);
        }

        return projectId;
    }

    @Override
    public String serviceAccountName() {
        return serviceAccountName;
    }

    @Override
    @SneakyThrows
    public String getSignedUrl(String bucketName, String storagePath, Duration expiration, StorageScope scope) {
        log.debug("About to create a signed URL for resource in Google Storage path {}/{} using AppEngine Java8 STD - Expiration: {}, Scope: {}",
                bucketName, storagePath, expiration, scope);
        storagePath = fixPath(storagePath);
        GetAccessTokenResult accessToken = accessTokenPool.getAccessToken(Lists.of(scope.getScopeUrl()));

        String BASE_URL = "https://storage.googleapis.com/" + bucketName + "/" + storagePath;
        return BASE_URL + "?GoogleAccessId=" + serviceAccountName
               + "&Expires=" + Instant.now().plus(expiration).getEpochSecond()
               + "&access_token=" + URLEncoder.encode(accessToken.getAccessToken(), StandardCharsets.UTF_8.name())
               + "&Signature=" + generateSignature(bucketName, storagePath, expiration);
    }

    @Override
    public String getSignedUrl(String bucketName, String directoryPrefix, Pattern fileNamePattern, Duration expiration, StorageScope scope) {
        return gcpSdk.getSignedUrl(bucketName, directoryPrefix, fileNamePattern, expiration, scope);
    }

    @Override
    public String getProjectId() {
        val projectId = getAppEngineProjectIdFromAppId();
        if (projectId == null)
            throw new AppException(ProjectError.PROJECT_ID_NOT_AVAILABLE);

        return projectId;
    }

    @Override
    @SneakyThrows
    public byte[] downloadInMemory(String bucketName, String storagePath) {
        log.debug("About to download file in memory from Google Storage path {}/{} using AppEngine Java8 STD", bucketName, storagePath);
        val downloadedFile = downloadInFile(bucketName, storagePath);

        return Files.readAllBytes(downloadedFile.toPath());
    }

    @Override
    @SneakyThrows
    public File downloadInFile(String bucketName, String storagePath) {
        log.debug("About to download file from Google Storage path {}/{} using AppEngine Java8 STD", bucketName, storagePath);
        storagePath = fixPath(storagePath);
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
        gcpSdk.uploadFile(bucketName, fixPath(storagePath), fileBytes);
    }

    @Override
    public void uploadFile(String bucketName, String storagePath, File file) {
        gcpSdk.uploadFile(bucketName, fixPath(storagePath), file);
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

    private String generateSignature(String bucketName, String storagePath, Duration expiration) {
        val Expiration = Instant.now().plus(expiration).toEpochMilli() / 1000;
        val StringToSign = HttpMethod.GET + "\n" +
                           Expiration + "\n" +
                           bucketName + "/" + storagePath;
        val signatureByte = appIdentityService.signForApp(StringToSign.getBytes()).getSignature();

        return BaseEncoding.base64Url().encode(signatureByte);
    }
}
