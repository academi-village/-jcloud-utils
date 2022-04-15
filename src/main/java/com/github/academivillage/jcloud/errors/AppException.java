package com.github.academivillage.jcloud.errors;

import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

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

    public AppException(AppError error) {this.error = error;}

    public AppException(AppError error, @Nullable Object details) {
        this.error = new DetailedError(error, details);
    }

    public AppException(AppError error, Object... params) {
        this(new ParameterizedError(error, params));
    }

    public static AppException withDetails(AppError error, @Nullable Object details) {
        return new AppException(new DetailedError(error, details));
    }

    public static AppException withDetails(AppError error, @Nullable Object details, Object... params) {
        return new AppException(new DetailedError(error, details), params);
    }
}
