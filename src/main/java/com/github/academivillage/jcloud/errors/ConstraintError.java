package com.github.academivillage.jcloud.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

/**
 * Encapsulates the details of a validation error.
 *
 * @author Younes Rahimi
 */
@Getter
@ToString
@RequiredArgsConstructor
public class ConstraintError implements AppError {

    /**
     * The error code of validation error.
     */
    private final String code;

    /**
     * A fixed http status code which indicates a bad request.
     */
    private final int httpStatusCode = HTTP_BAD_REQUEST;

    /**
     * @return A null message.
     */
    @Nullable
    @Override
    public String getMessage() {
        return null;
    }
}
