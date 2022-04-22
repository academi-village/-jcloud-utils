package com.github.academivillage.jcloud.gcp.sdk;

import com.github.academivillage.jcloud.errors.AppException;
import com.github.academivillage.jcloud.errors.JCloudError;
import com.github.academivillage.jcloud.gcp.CloudMetadata;
import com.github.academivillage.jcloud.gcp.CloudStorage;
import com.github.academivillage.jcloud.gcp.StorageScope;
import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.storage.*;
import com.google.cloud.storage.Storage.SignUrlOption;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import static com.github.academivillage.jcloud.util.FileUtil.getFileName;

/**
 * The default implementation of {@link CloudMetadata} and {@link CloudStorage} for all GCP serverless services. <br/>
 * <p>
 * For local development set the environment variables {@code export GOOGLE_CLOUD_PROJECT=dynamikax-dev}
 * and {@code export GOOGLE_APPLICATION_CREDENTIALS=/path/to/private_key.json}.
 */
@Slf4j
public class GcpSdk implements CloudMetadata, CloudStorage {

    private final Storage storage;
    private final String  serviceAccountName;
    private final String  projectId;

    /**
     * @param projectId   See: <a href="https://googlecloudplatform.github.io/spring-cloud-gcp/2.0.10/reference/html/index.html#project-id">Fetching Project ID</a>.
     *                    For local development add {@code spring.cloud.gcp.project-id=dynamikax-dev} to the {@code application.properties}.
     *                    Or set the environment variable {@code export GOOGLE_CLOUD_PROJECT=dynamikax-dev}.<br><br>
     * @param credentials See:  <a href="https://googlecloudplatform.github.io/spring-cloud-gcp/2.0.10/reference/html/index.html#credentials">Fetching Credentials</a>.
     *                    For local development add {@code spring.cloud.gcp.credentials.location=file:/path/to/private_key.json} to the {@code application.properties}.
     *                    Or set the environment variable {@code export GOOGLE_APPLICATION_CREDENTIALS=/path/to/private_key.json}.
     *                    Read more: <a href="https://cloud.google.com/docs/authentication/production#passing_variable">Passing credentials via environment variable</a>
     */
    @SneakyThrows
    public GcpSdk(String projectId, Credentials credentials) {
        log.debug("Initiating GcpSdk. ProjectId: {}, Credentials : {} - {}", projectId, credentials.getClass(), credentials);
        val builder = StorageOptions.newBuilder().setCredentials(credentials);
        this.storage            = "no_app_id".equals(projectId)
                                  ? builder.build().getService()
                                  : builder.setProjectId(projectId).build().getService();
        this.projectId          = this.storage.getOptions().getProjectId();
        this.serviceAccountName = storage.getServiceAccount(this.projectId).getEmail();

        log.debug("Initiating GcpSdk done. Final ProjectId: {}, ServiceAccountName : {}",
                this.projectId, serviceAccountName);
    }

    /**
     * For local development set the environment variables {@code GOOGLE_CLOUD_PROJECT=dynamikax-dev}
     * and {@code export GOOGLE_APPLICATION_CREDENTIALS=/path/to/private_key.json}.
     * Read more: <a href="https://cloud.google.com/docs/authentication/production#passing_variable">Passing credentials via environment variable</a>
     */
    public GcpSdk() {
        var projectId = ServiceOptions.getDefaultProjectId();
        log.debug("Initiating GcpSdk by default constructor. ProjectId: {}", projectId);
        this.storage = "no_app_id".equals(projectId)
                       ? StorageOptions.newBuilder().build().getService()
                       : StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        this.projectId          = storage.getOptions().getProjectId();
        this.serviceAccountName = storage.getServiceAccount(this.projectId).getEmail();
        log.debug("Initiating GcpSdk by default constructor done. Final ProjectId: {}, ServiceAccountName : {}",
                this.projectId, serviceAccountName);
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    @Override
    public String serviceAccountName() {
        return serviceAccountName;
    }

    @Override
    public String getSignedUrl(String bucketName, String storagePath, Duration expiration, StorageScope scope) {
        log.debug("About to create a signed URL for resource in Google Storage path {}/{} using GCP SDK - Expiration: {}, Scope: {}",
                bucketName, storagePath, expiration, scope);
        // Define resource
        BlobInfo      blobInfo    = BlobInfo.newBuilder(BlobId.of(bucketName, fixPath(storagePath))).build();
        SignUrlOption v4Signature = SignUrlOption.withV4Signature();
        URL           url         = storage.signUrl(blobInfo, expiration.getSeconds(), TimeUnit.SECONDS, v4Signature);

        return url.toString();
    }

    @Override
    public String getSignedUrl(String bucketName, String directoryPrefix, Pattern fileNamePattern, Duration expiration, StorageScope scope) {
        log.debug("About to create a signed URL for resource in Google Storage path {}/{} using GCP SDK - File Name Pattern: {} - Expiration: {} - Scope: {}",
                bucketName, directoryPrefix, fileNamePattern.pattern(), expiration, scope);
        Blob blob = getBlob(bucketName, directoryPrefix, fileNamePattern);

        SignUrlOption v4Signature = SignUrlOption.withV4Signature();
        URL           url         = blob.signUrl(expiration.getSeconds(), TimeUnit.SECONDS, v4Signature);

        return url.toString();
    }

    @Override
    public byte[] downloadInMemory(String bucketName, String storagePath) {
        log.debug("About to download file in memory from Google Storage path {}/{} using GCP SDK", bucketName, storagePath);
        return storage.readAllBytes(bucketName, fixPath(storagePath));
    }

    @Override
    public File downloadInFile(String bucketName, String storagePath) {
        log.debug("About to download file from Google Storage path {}/{} using GCP SDK", bucketName, storagePath);
        storagePath = fixPath(storagePath);
        String fileName = getFileName(storagePath);
        File   tempFile = createTempFile(fileName);
        storage.get(BlobId.of(bucketName, storagePath)).downloadTo(tempFile.toPath());

        return tempFile;
    }

    @Override
    public File downloadInFile(String bucketName, String directoryPrefix, Pattern fileNamePattern) {
        log.debug("About to download file from Google Storage path {}/{} using GCP SDK - File Name Patter: {}",
                bucketName, directoryPrefix, fileNamePattern.pattern());
        String fileType = getFileType(fileNamePattern);
        String fileName = "gcp-run-" + Instant.now().toEpochMilli() + "." + fileType;
        File   tempFile = createTempFile(fileName);

        Blob blob = getBlob(bucketName, directoryPrefix, fileNamePattern);
        blob.downloadTo(tempFile.toPath());

        return tempFile;
    }

    @Override
    public void uploadFile(String bucketName, String storagePath, byte[] fileBytes) {
        log.debug("About to upload file to Google Storage path {}/{} using GCP SDK - File size: {} KB",
                bucketName, storagePath, fileBytes.length / 1024);

        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, fixPath(storagePath))).build();
        storage.create(blobInfo, fileBytes, Storage.BlobTargetOption.detectContentType());
    }

    @Override
    @SneakyThrows
    public void uploadFile(String bucketName, String storagePath, File file) {
        log.debug("About to upload file to Google Storage path {}/{} using GCP SDK - File path: {} - File size: {} KB",
                bucketName, storagePath, file.getName(), file.length() / 1024);
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, fixPath(storagePath))).build();
        storage.createFrom(blobInfo, file.toPath(), Storage.BlobWriteOption.detectContentType());
    }

    private String getFileType(Pattern fileNamePattern) {
        String[] split = fileNamePattern.pattern().split("\\.");
        if (split.length > 1) return split[split.length - 1];

        return fileNamePattern.pattern();
    }

    @SneakyThrows
    private File createTempFile(String fileName) {
        try {
            return Files.createTempFile("gcp-storage-", fileName).toFile();
        } catch (Exception e) {
            log.error("Failed to generate a temp file {}", fileName, e);
            throw e;
        }
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

    private Blob getBlob(String bucketName, String directoryPrefix, Pattern fileNamePattern) {
        Page<Blob> blobs = storage.list(
                bucketName,
                Storage.BlobListOption.prefix(directoryPrefix),
                Storage.BlobListOption.currentDirectory());

        Predicate<String> predicate = fileNamePattern.asPredicate();

        return StreamSupport.stream(blobs.iterateAll().spliterator(), false)
                .filter(it -> predicate.test(getFileName(it.getBlobId().getName())))
                .findFirst()
                .orElseThrow(() -> new AppException(JCloudError.FILE_NOT_FOUND));
    }
}
