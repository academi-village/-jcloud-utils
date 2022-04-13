package com.github.academivillage.jcloud.errors;

import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

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

    public AppException(AppError error)                           {this.error = error;}

    public AppException(AppError error, @Nullable Object details) {this.error = new DetailedError(error, details);}
}
