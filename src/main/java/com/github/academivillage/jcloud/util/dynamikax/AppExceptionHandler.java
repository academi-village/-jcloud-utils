package com.github.academivillage.jcloud.util.dynamikax;

import com.github.academivillage.jcloud.errors.AppError;
import com.github.academivillage.jcloud.errors.AppException;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AppExceptionHandler
        extends ResponseEntityExceptionHandler {

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
}
