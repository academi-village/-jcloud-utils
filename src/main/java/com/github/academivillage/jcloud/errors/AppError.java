package com.github.academivillage.jcloud.errors;

import org.springframework.lang.Nullable;

/**
 * A general handler error to be used by handler implementations.
 *
 * @author Younes Rahimi
 */
public interface AppError {

    /**
     * Represents the error code.
     */
    String getCode();

    /**
     * Represents the equivalent http status code for this error.
     * Mainly used to differentiate an error response from a successful response.
     */
    int getHttpStatusCode();

    /**
     * Represents the humane readable message.
     */
    @Nullable
    default String getMessage() {
        return null;
    }

    /**
     * Encapsulates the more specific details of the error.
     * Typically, contains the gateway service response or the exception message.
     */
    @Nullable
    default Object getDetails() {
        return null;
    }
}
