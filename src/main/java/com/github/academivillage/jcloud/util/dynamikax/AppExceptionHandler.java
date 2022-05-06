package com.github.academivillage.jcloud.util.dynamikax;

import com.github.academivillage.jcloud.errors.AppError;
import com.github.academivillage.jcloud.errors.AppException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AppException.class})
    protected ResponseEntity<MSResponse<Object>> handleAppExceptionAsMsResponse(AppException ex, WebRequest request) {
        AppError error      = ex.getError();
        val      httpStatus = HttpStatus.valueOf(error.getHttpStatusCode());
        val      msResponse = new MSResponse<Object>(httpStatus.value(), error.getMessage(), null, error.getCode());

        return new ResponseEntity<>(msResponse, httpStatus);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<MSResponse<?>> handleExpiredJwtException(Throwable ex) {
        val msResponse = new MSResponse<String>(
                UNAUTHORIZED.value(),
                "JWT token is expired",
                null,
                "user.not_authenticated");

        return new ResponseEntity<>(msResponse, UNAUTHORIZED);
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<MSResponse<?>> handleExceptions(Throwable ex, WebRequest request) {
        String extMessage = ex.getClass().getSimpleName() + " -> " + request.getDescription(true);
        String message    = ExceptionUtils.getRootCauseMessage(ex);
        extMessage = extMessage + " : " + message;
        log.error(extMessage, ex);

        val msResponse = new MSResponse<String>(
                INTERNAL_SERVER_ERROR.value(),
                message,
                extMessage,
                "server.internal_error");

        return new ResponseEntity<>(msResponse, INTERNAL_SERVER_ERROR);
    }
}
