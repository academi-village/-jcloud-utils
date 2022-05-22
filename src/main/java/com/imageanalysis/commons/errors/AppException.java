package com.imageanalysis.commons.errors;

import lombok.Getter;
import lombok.ToString;

/**
 * Encapsulates an {@link AppError}.
 *
 * @author Younes Rahimi
 */
@Getter
@ToString
public class AppException extends RuntimeException {

    /**
     * Represents the actual error.
     */
    private final AppError error;

    public AppException(AppError error) {
        super(error.getMessage());
        this.error = error;
    }

    /**
     * @param params Used to interpolate the error message.
     */
    public AppException(AppError error, Object... params) {
        this(new ParameterizedError(error, params));
    }

    /**
     * @param params Used to interpolate the error message.
     */
    public AppException(AppError error, Exception ex, Object... params) {
        this(new ParameterizedError(error, params), ex);
    }

    public AppException(AppError error, Exception ex) {
        super(error.getMessage(), ex);
        this.error = error;
    }
}
