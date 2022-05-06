package com.github.academivillage.jcloud.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT;
import static java.net.HttpURLConnection.*;

/**
 * A general handler error to be used by handler implementations.
 *
 * @author Younes Rahimi
 */
@Getter
@RequiredArgsConstructor
@JsonFormat(shape = OBJECT)
public enum ProjectError implements AppError {
    CAN_NOT_FOUND("resource.not_found", "{0} {1} cannot be found", HTTP_NOT_FOUND),
    PROJECT_ID_NOT_AVAILABLE("projectId.not_available", "The GCP project ID is not available", HTTP_INTERNAL_ERROR),
    FILE_NOT_FOUND("file.not_found", "The requested file not found", HTTP_NOT_FOUND),
    MS_USER_LOGIN_FAILED("msUser.login_failed", "Calling MsUser microservice login API failed", HTTP_BAD_GATEWAY),
    USER_NOT_AUTHENTICATED("user.not_authenticated", "User is not authenticated", HTTP_UNAUTHORIZED),
    ACCESS_DENIED("access.denied", "You do not have permission to access this resource", HTTP_FORBIDDEN),
    READING_INVALID_STATUS("reading.invalid_status", "The reading status can't be changed", HTTP_PRECON_FAILED),
    ;

    /**
     * Represents the error code.
     */
    private final String code;

    /**
     * Represents the humane readable message.
     */
    private final String message;

    /**
     * Represents the equivalent http status code for this error.
     * Mainly used to differentiate an error response from a successful response.
     */
    private final int httpStatusCode;
}
