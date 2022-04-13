package com.github.academivillage.jcloud.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

/**
 * Encapsulates the information of an application error with possible more details.
 *
 * @author Younes Rahimi
 */
@Getter
@ToString
@RequiredArgsConstructor
public class DetailedError implements AppError {

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
    private final int httpStatusCode;

    /**
     * Encapsulates the more specific details of the error.
     */
    @Nullable
    private final Object details;

    public DetailedError(AppError error, @Nullable Object details) {
        this.code           = error.getCode();
        this.httpStatusCode = error.getHttpStatusCode();
        this.message        = error.getMessage();
        this.details        = details;
    }
}
