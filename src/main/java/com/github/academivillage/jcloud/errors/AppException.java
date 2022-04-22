package com.github.academivillage.jcloud.errors;

import lombok.Getter;
import lombok.ToString;

/**
 * Encapsulates an {@link AppError}.
 *
 * @author Younes Rahimi
 * @author Mohammad Reza Dehghani
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
}
