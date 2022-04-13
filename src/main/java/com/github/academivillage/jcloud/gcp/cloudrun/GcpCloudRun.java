package com.github.academivillage.jcloud.gcp.cloudrun;

import com.github.academivillage.jcloud.errors.AppException;
import com.github.academivillage.jcloud.errors.JCloudError;
import com.github.academivillage.jcloud.gcp.CloudMetadata;
import com.github.academivillage.jcloud.gcp.CloudStorage;
import com.github.academivillage.jcloud.gcp.Scope;
import com.google.cloud.ServiceOptions;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.SignUrlOption;
import com.google.cloud.storage.StorageOptions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * The default implementation of {@link CloudMetadata} and {@link CloudStorage} for App Engine Standard Java 8.
 */
@Slf4j
public class GcpCloudRun implements CloudMetadata, CloudStorage {

    private final Storage storage;
    private final String  serviceAccountName;

    public GcpCloudRun(Storage storage) {
        this.storage            = storage;
        this.serviceAccountName = storage.getServiceAccount(storage.getOptions().getProjectId()).getEmail();
    }

    public GcpCloudRun() {
        String projectId = getProjectId().orElseThrow(() -> new AppException(JCloudError.PROJECT_ID_NOT_AVAILABLE));
        this.storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();

        this.serviceAccountName = storage.getServiceAccount(projectId).getEmail();
    }

    @Override
    public Optional<String> getProjectId() {
        return Optional.ofNullable(ServiceOptions.getDefaultProjectId());
    }

    @Override
    public String serviceAccountName() {
        return serviceAccountName;
    }

    @Override
    public String getSignedUrl(String bucketName, String storagePath, Duration expiration, Scope scope) {
        // Define resource
        BlobInfo      blobInfo    = BlobInfo.newBuilder(BlobId.of(bucketName, storagePath)).build();
        SignUrlOption v4Signature = SignUrlOption.withV4Signature();
        URL           url         = storage.signUrl(blobInfo, expiration.getSeconds(), TimeUnit.SECONDS, v4Signature);

        return url.toString();
    }

    @Override
    public byte[] downloadInMemory(String bucketName, String storagePath) {
        return storage.readAllBytes(bucketName, storagePath);
    }

    @Override
    public File downloadInFile(String bucketName, String storagePath) {
        String fileName = getFileName(storagePath);
        File   tempFile = createTempFile(fileName);
        storage.get(BlobId.of(bucketName, storagePath)).downloadTo(tempFile.toPath());

        return tempFile;
    }

    @Override
    public void uploadFile(String bucketName, String storagePath, byte[] fileBytes) {
        if (storagePath.startsWith("/")) {
            storagePath = storagePath.substring(1);
        }

        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, storagePath)).build();
        storage.create(blobInfo, fileBytes, Storage.BlobTargetOption.detectContentType());
    }

    private String getFileName(String storagePath) {
        String[] split = storagePath.split("/");
        return split[split.length - 1];
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
}
