package com.github.academivillage.jcloud.errors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
public class ParameterizedError implements AppError {

    /**
     * Represents the error code.
     */
    private final String code;

    /**
     * Represents the humane readable message.
     */
    @Nullable
    private final String message;

    private final int httpStatusCode;

    /**
     * Represents the parameters that substituted in message.
     */
    private Object[] params;

    public ParameterizedError(AppError error, Object[] params) {
        var errorMessage = "";
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                errorMessage = errorMessage.replace("{" + i + "}", String.valueOf(params[i]));
            }
        }

        this.code           = error.getCode();
        this.message        = errorMessage;
        this.httpStatusCode = error.getHttpStatusCode();
        this.params         = params;
    }
}
