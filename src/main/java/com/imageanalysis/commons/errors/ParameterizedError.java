package com.imageanalysis.commons.errors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.regex.Pattern;

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

    private static final Predicate<String> PLACE_HOLDER_MATCHER = Pattern.compile("\\{\\d+}").asPredicate();

    /**
     * Represents the error code.
     */
    private final String code;

    /**
     * Represents the humane readable message.
     */
    @Nullable
    private final String message;

    private final int    httpStatusCode;
    /**
     * Encapsulates the more specific details of the error.
     */
    @Nullable
    private final Object details;

    /**
     * Represents the parameters that substituted in message.
     */
    private final Object[] params;

    public ParameterizedError(AppError error, @Nullable Object... params) {
        this.code           = error.getCode();
        this.message        = substituteErrorMessage(error, params);
        this.httpStatusCode = error.getHttpStatusCode();
        this.details        = error.getDetails();
        this.params         = params;
    }

    @Nullable
    private String substituteErrorMessage(AppError error, @Nullable Object @NotNull [] params) {
        var errorMessage = error.getMessage();
        if (errorMessage != null)
            for (int i = 0; i < params.length; i++)
                 errorMessage = errorMessage.replace("{" + i + "}", String.valueOf(params[i]));

        if (errorMessage != null && PLACE_HOLDER_MATCHER.test(errorMessage))
            throw new IllegalArgumentException("Not all of the errorMessage placeholders filled: " + errorMessage);

        return errorMessage;
    }
}
