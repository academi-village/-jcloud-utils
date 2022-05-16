package com.imageanalysis.commons.errors;

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
    PROJECT_ID_NOT_AVAILABLE("projectId.not_available", "The GCP project ID is not available", HTTP_INTERNAL_ERROR),
    FILE_NOT_FOUND("file.not_found", "The requested file not found", HTTP_NOT_FOUND),
    MS_USER_LOGIN_FAILED("msUser.login_failed", "Calling MsUser microservice login API failed", HTTP_BAD_GATEWAY),
    REMOTE_SERVICE_FAILED("remote_service.failed", "The remote service is failed", HTTP_BAD_GATEWAY),
    USER_NOT_AUTHENTICATED("user.not_authenticated", "User is not authenticated", HTTP_UNAUTHORIZED),
    ACCESS_DENIED("access.denied", "You do not have permission to access this resource", HTTP_FORBIDDEN),
    READING_NOT_FOUND("reading.not_found", "The reading {} not found", HTTP_NOT_FOUND),
    READING_INVALID_STATUS_TRANSITION("reading.invalid_status_transition", "The reading status transition is not valid", HTTP_PRECON_FAILED),
    CAN_NOT_FOUND("resource.not_found", "{0} {1} cannot be found", HTTP_NOT_FOUND),
    ENDPOINT_ID_NOT_FOUND("endpoint_id.not_found", "The endpoint ID of {} not found", HTTP_INTERNAL_ERROR),
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
