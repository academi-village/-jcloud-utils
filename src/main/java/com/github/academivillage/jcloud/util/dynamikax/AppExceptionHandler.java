package com.github.academivillage.jcloud.util.dynamikax;

import com.github.academivillage.jcloud.errors.AppError;
import com.github.academivillage.jcloud.errors.AppException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AppException.class})
    protected ResponseEntity<MSResponse<Object>> handleAppExceptionAsMsResponse(AppException ex, WebRequest request) {
        AppError error      = ex.getError();
        val      httpStatus = HttpStatus.valueOf(error.getHttpStatusCode());
        val msResponse = new MSResponse<Object>()
                .setResponseCode(httpStatus.value())
                .setResponseMessage(error.getMessage())
                .setErrorCode(error.getCode());

        return new ResponseEntity<>(msResponse, httpStatus);
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<MSResponse<?>> handleExceptions(Throwable ex, WebRequest request) {
        String extMessage = ex.getClass().getSimpleName() + " -> " + request.getDescription(true);
        String message    = ExceptionUtils.getRootCauseMessage(ex);
        extMessage = extMessage + " : " + message;
        log.error(extMessage, ex);

        MSResponse<String> msResponse = new MSResponse<String>()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setErrorCode("server.internal_error")
                .setResponseMessage(message)
                .setData(extMessage);

        return new ResponseEntity<>(msResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
