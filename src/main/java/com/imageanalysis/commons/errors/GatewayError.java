package com.imageanalysis.commons.errors;

import lombok.*;
import org.jetbrains.annotations.Nullable;

/**
 * Encapsulates the details of a remote server error.
 *
 * @author Younes Rahimi
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GatewayError implements AppError {

    /**
     * Represents the error code.
     */
    private String code;

    /**
     * Represents the humane readable message.
     */
    @Nullable
    private String message;

    /**
     * Represents the equivalent http status code for this error.
     * Mainly used to differentiate an error response from a successful response.
     */
    @Setter
    private int httpStatusCode;
}
