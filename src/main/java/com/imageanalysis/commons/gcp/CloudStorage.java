package com.imageanalysis.commons.gcp;

import java.io.File;
import java.time.Duration;
import java.util.regex.Pattern;

public interface CloudStorage {

    /**
     * Generates a signed URL for the specified file path.
     * The signed URL could be used to download the file or to upload to the specified storage path.
     *
     * @param bucketName  Represents the bucket name. Example: {@code dynamikax-storage-eu}, {@code dynamikax-storage-eu-uat}, {@code dynamikax-storage-eu-prd}
     * @param storagePath Represents the path of the file in the storage bucket. Example: {@code series/10003/1405055175_001.dcm}
     * @param expiration  Represents the expiration duration of the generated link.
     * @return A link to the specified file that could be downloaded/uploaded.
     * @see <a href="https://cloud.google.com/storage/docs/access-control/signed-urls">GCP Signed URLs</a>
     */
    String getSignedUrl(String bucketName, String storagePath, Duration expiration, StorageScope scope);

    /**
     * Generates a signed URL for the specified file path.
     * The signed URL could be used to download the file from the specified storage path.
     *
     * @param bucketName  Represents the bucket name. Example: {@code dynamikax-storage-eu}, {@code dynamikax-storage-eu-uat}, {@code dynamikax-storage-eu-prd}
     * @param storagePath Represents the path of the file in the storage bucket. Example: {@code series/10003/1405055175_001.dcm}
     * @return A link to the specified file that could be downloaded.
     * @see <a href="https://cloud.google.com/storage/docs/access-control/signed-urls">GCP Signed URLs</a>
     */
    default String getSignedUrl(String bucketName, String storagePath) {
        return getSignedUrl(bucketName, storagePath, Duration.ofDays(7), StorageScope.READ_ONLY);
    }

    /**
     * Generates a signed URL for the specified file path.
     * The signed URL could be used to download the file or to upload to the specified storage path.
     *
     * @param bucketName      Represents the bucket name. Example: {@code dynamikax-storage-eu}, {@code dynamikax-storage-eu-uat}, {@code dynamikax-storage-eu-prd}
     * @param directoryPrefix Represents a directory path in the bucket. Example: {@code series/10003/}
     * @param fileNamePattern Represents the file name pattern to indicate a unique file in the specified directory. Example: {@code *.dcm}
     * @param expiration      Represents the expiration duration of the generated link.
     * @return A link to the specified file that could be downloaded/uploaded.
     * @see <a href="https://cloud.google.com/storage/docs/access-control/signed-urls">GCP Signed URLs</a>
     */
    String getSignedUrl(String bucketName, String directoryPrefix, Pattern fileNamePattern, Duration expiration, StorageScope scope);

    /**
     * Generates a signed URL for the specified file path.
     * The signed URL could be used to download the file from the specified storage path.
     *
     * @param bucketName      Represents the bucket name. Example: {@code dynamikax-storage-eu}, {@code dynamikax-storage-eu-uat}, {@code dynamikax-storage-eu-prd}
     * @param directoryPrefix Represents a directory path in the bucket. Example: {@code series/10003/}
     * @param fileNamePattern Represents the file name pattern to indicate a unique file in the specified directory. Example: {@code *.dcm}
     * @return A link to the specified file that could be downloaded.
     * @see <a href="https://cloud.google.com/storage/docs/access-control/signed-urls">GCP Signed URLs</a>
     */
    default String getSignedUrl(String bucketName, String directoryPrefix, Pattern fileNamePattern) {
        return getSignedUrl(bucketName, directoryPrefix, fileNamePattern, Duration.ofDays(7), StorageScope.READ_ONLY);
    }

    /**
     * Downloads the requested resource.
     *
     * @param bucketName  Represents the bucket name. Example: {@code dynamikax-storage-eu}, {@code dynamikax-storage-eu-uat}, {@code dynamikax-storage-eu-prd}
     * @param storagePath Represents the path of the file in the storage bucket. Example: {@code series/10003/1405055175_001.dcm}
     * @return The content of the resource as a byte array.
     */
    byte[] downloadInMemory(String bucketName, String storagePath);

    /**
     * Downloads the requested resource as file.
     *
     * @param bucketName  Represents the bucket name. Example: {@code dynamikax-storage-eu}, {@code dynamikax-storage-eu-uat}, {@code dynamikax-storage-eu-prd}
     * @param storagePath Represents the path of the file in the storage bucket. Example: {@code series/10003/1405055175_001.dcm}
     * @return The downloaded file.
     */
    File downloadInFile(String bucketName, String storagePath);

    /**
     * Downloads the requested resource as file.
     *
     * @param bucketName      Represents the bucket name. Example: {@code dynamikax-storage-eu}, {@code dynamikax-storage-eu-uat}, {@code dynamikax-storage-eu-prd}
     * @param directoryPrefix Represents a directory path in the bucket. Example: {@code series/10003/}
     * @param fileNamePattern Represents the file name pattern to indicate a unique file in the specified directory. Example: {@code *.dcm}
     * @return The downloaded file.
     */
    File downloadInFile(String bucketName, String directoryPrefix, Pattern fileNamePattern);

    void uploadFile(String bucketName, String storagePath, byte[] fileBytes);

    void uploadFile(String bucketName, String storagePath, File file);
}
