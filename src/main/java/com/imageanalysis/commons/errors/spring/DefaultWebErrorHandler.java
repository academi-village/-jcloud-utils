package com.imageanalysis.commons.errors.spring;

import me.alidg.errors.HandledException;
import me.alidg.errors.WebErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.imageanalysis.commons.errors.spring.AppExceptionHandler.SERVER_ERROR_CODE;

/**
 * The default fallback {@link WebErrorHandler} which will be used when all
 * other registered handlers refuse to handle a particular exception.
 *
 * @author Younes Rahimi
 * @see WebErrorHandler
 */
@Component("defaultWebErrorHandler")
public class DefaultWebErrorHandler implements WebErrorHandler {

    /**
     * Since this is the last resort error handler, this value would simply be ignored.
     *
     * @param exception The exception to examine.
     * @return Does not matter what!
     */
    @Override
    public boolean canHandle(@Nullable Throwable exception) {
        return false;
    }

    /**
     * Always return 500 Internal Error with {@code unknown_error} as the error code with no
     * arguments.
     *
     * @param exception The exception to handle.
     * @return 500 Internal Error with {@code unknown_error} as the error code and no arguments.
     */
    @Override
    public HandledException handle(@Nullable Throwable exception) {

        return new HandledException(SERVER_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR, Collections.emptyMap());
    }
}
