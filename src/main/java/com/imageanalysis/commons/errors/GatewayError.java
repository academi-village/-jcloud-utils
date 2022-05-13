package com.imageanalysis.commons.errors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/**
 * Encapsulates the details of a remote server error.
 *
 * @author Younes Rahimi
 */
@Getter
@ToString
@RequiredArgsConstructor
@JsonAutoDetect(fieldVisibility = ANY)
public class GatewayError implements AppError {

    /**
     * Represents the error code.
     */
    private final String code;

    /**
     * Represents the humane readable message.
     */
    @Nullable
    private final String message;

    /**
     * Represents the equivalent http status code for this error.
     * Mainly used to differentiate an error response from a successful response.
     */
    @Setter
    private int httpStatusCode;
}
