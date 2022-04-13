package com.github.academivillage.jcloud.gcp;

import java.io.File;
import java.time.Duration;

public interface CloudStorage {

    /**
     * Generates a signed URL for specified file path.
     * The signed URL could be used to download the file or to upload to the specified storage path.
     *
     * @param bucketName  Represents the bucket name. Example: {@code dynamikax-storage-eu}, {@code dynamikax-storage-eu-uat}, {@code dynamikax-storage-eu-prd}
     * @param storagePath Represents the path of the file in the storage bucket. Example: {@code /series/10003/1405055175_001.dcm}
     * @param expiration  Represents the expiration duration.
     * @return A link to specified file that could be downloaded.
     * @see <a href="https://cloud.google.com/storage/docs/access-control/signed-urls">GCP Signed URLs</a>
     */
    String getSignedUrl(String bucketName, String storagePath, Duration expiration, Scope scope);

    /**
     * Downloads the requested resource.
     *
     * @param bucketName  Represents the bucket name. Example: {@code dynamikax-storage-eu}, {@code dynamikax-storage-eu-uat}, {@code dynamikax-storage-eu-prd}
     * @param storagePath Represents the path of the file in the storage bucket. Example: {@code /series/10003/1405055175_001.dcm}
     * @return The content of the resource as a byte array.
     */
    byte[] downloadInMemory(String bucketName, String storagePath);

    /**
     * Downloads the requested resource as file.
     *
     * @param bucketName  Represents the bucket name. Example: {@code dynamikax-storage-eu}, {@code dynamikax-storage-eu-uat}, {@code dynamikax-storage-eu-prd}
     * @param storagePath Represents the path of the file in the storage bucket. Example: {@code /series/10003/1405055175_001.dcm}
     * @return The content of the resource as a byte array.
     */
    File downloadInFile(String bucketName, String storagePath);

    void uploadFile(String bucketName, String storagePath, byte[] fileBytes);
}
