package com.github.academivillage.jcloud.gcp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @see <a href="https://cloud.google.com/storage/docs/authentication#oauth-scopes">Scope of Cloud Storage authentication</a>
 */
@Getter
@RequiredArgsConstructor
public enum Scope {

    /**
     * Only allows access to read data, including listing buckets.
     */
    READ_ONLY("https://www.googleapis.com/auth/devstorage.read_only"),

    /**
     * Allows access to read and change data, but not metadata like IAM policies.
     */
    READ_WRITE("https://www.googleapis.com/auth/devstorage.read_write"),

    /**
     * Allows full control over data, including the ability to modify IAM policies.
     */
    FULL_CONTROL("https://www.googleapis.com/auth/devstorage.full_control"),

    /**
     * View your data across Google Cloud services. For Cloud Storage, this is the same as devstorage.read-only.
     */
    CLOUD_PLATFORM_READ_ONLY("https://www.googleapis.com/auth/cloud-platform.read-only"),

    /**
     * View and manage data across all Google Cloud services. For Cloud Storage, this is the same as devstorage.full-control.
     */
    CLOUD_PLATFORM("https://www.googleapis.com/auth/cloud-platform");

    private final String scopeUrl;
}
