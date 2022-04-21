package com.github.academivillage.jcloud.gcp;

public interface CloudMetadata {

    /**
     * The project ID. Example: {@code dynamikax-dev}
     */
    String getProjectId();

    /**
     * The service account name (If available). Aka GoogleAccessId.<br/>
     * Value for this is available as an environment variable named {@code GOOGLE_APPLICATION_CREDENTIALS}.<br/>
     * Example: {@code dynamikax-dev@appspot.gserviceaccount.com}
     */
    String serviceAccountName();
}
