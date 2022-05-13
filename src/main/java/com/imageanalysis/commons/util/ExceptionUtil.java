package com.imageanalysis.commons.util;

import com.imageanalysis.commons.errors.AppError;
import com.imageanalysis.commons.errors.AppException;
import lombok.SneakyThrows;

public class ExceptionUtil {

    public static <T> T TODO() {throw new TodoException();}

    @SneakyThrows
    public static <T> T rethrow(Throwable throwable) {throw throwable;}

    public static Throwable getRootCause(Throwable original) {
        Throwable rootCause = original;
        Throwable cause     = original.getCause();
        while (cause != null && cause != rootCause) {
            rootCause = cause;
            cause     = cause.getCause();
        }
        return rootCause;
    }

    private static class TodoException extends AppException {

        private static final AppError API_NOT_IMPLEMENTED_ERROR = new AppError() {

            @Override
            public String getCode() {return "api.not_implemented";}

            @Override
            public int getHttpStatusCode() {return 500;}

            @Override
            public String getMessage() {return "API is not implemented yet.";}
        };

        private TodoException() {super(API_NOT_IMPLEMENTED_ERROR);}
    }
}
