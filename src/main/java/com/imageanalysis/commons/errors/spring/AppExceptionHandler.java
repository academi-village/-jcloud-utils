package com.imageanalysis.commons.errors.spring;

import com.imageanalysis.commons.errors.AppError;
import com.imageanalysis.commons.errors.AppException;
import com.imageanalysis.commons.util.dynamikax.MSResponse;
import com.imageanalysis.commons.util.java.Lists;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.alidg.errors.adapter.DefaultHttpErrorAttributesAdapter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static com.imageanalysis.commons.errors.spring.AppExceptionHandler.ErrorFormat.*;
import static com.imageanalysis.commons.errors.spring.AppExceptionHandler.ErrorsProperties;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@EnableConfigurationProperties(ErrorsProperties.class)
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * This error handler simply returns an error code representing an unknown error.
     */
    public static final String           SERVER_ERROR_CODE = "server.error";
    private final       ErrorsProperties errorsProps;

    @ExceptionHandler(value = {AppException.class})
    protected ResponseEntity<?> handleAppExceptionAsMsResponse(AppException ex, WebRequest request) {
        AppError error      = ex.getError();
        val      httpStatus = HttpStatus.valueOf(error.getHttpStatusCode());

        val fingerprint = generateFingerprint();
        logError(ex, request, fingerprint, httpStatus.value());

        val responseBody = getResponse(error, httpStatus, fingerprint);

        return new ResponseEntity<>(responseBody, httpStatus);
    }

    private MSResponse<Object> getResponse(AppError error, HttpStatus httpStatus, String fingerprint) {
        val response = new MSResponse<>().setFingerprint(fingerprint);

        ErrorFormat format = errorsProps.format;
        if (format == OLD || format == NEW_COMPATIBLE)
            response.setResponseCode(httpStatus.value())
                    .setResponseMessage(error.getMessage())
                    .setCode(error.getCode())
                    .setDetails(error.getDetails());

        if (format == NEW || format == NEW_COMPATIBLE) {
            val codedMessage = new MSResponse.CodedMessage(error.getCode(), error.getMessage(), error.getDetails());
            response.setErrors(Lists.of(codedMessage));
        }

        return response;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        val fingerprint = generateFingerprint();
        logError(ex, request, fingerprint, UNAUTHORIZED.value());
        String errorCode    = "user.not_authenticated";
        String message      = "JWT token is expired";
        val    responseBody = toNewErrorFormat(fingerprint, UNAUTHORIZED.value(), errorCode, message, null);

        return new ResponseEntity<>(responseBody, UNAUTHORIZED);
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<Object> handleExceptions(Throwable ex, WebRequest request) {
        val message    = ExceptionUtils.getRootCauseMessage(ex);
        val extMessage = request.getDescription(true) + " -> " + message;

        val fingerprint    = generateFingerprint();
        int httpStatusCode = INTERNAL_SERVER_ERROR.value();
        logError(ex, request, fingerprint, httpStatusCode);

        val responseBody = toNewErrorFormat(fingerprint, httpStatusCode, SERVER_ERROR_CODE, message, extMessage);

        return new ResponseEntity<>(responseBody, INTERNAL_SERVER_ERROR);
    }

    private void logError(Throwable ex, WebRequest request, String fingerprint, int httpStatusCode) {
        String requestPath      = getRequestPath(request);
        val    isUnhandledError = 500 <= httpStatusCode && httpStatusCode <= 599;
        val    rootEx           = ExceptionUtils.getRootCause(ex);
        if (isUnhandledError) {
            val errorMsg = String.format("Error did NOT handled. %s - Fingerprint: %s", requestPath, fingerprint);
            log.error(errorMsg, rootEx);
        } else {
            log.debug("Error handled. {} - Fingerprint: {} - {}", requestPath, fingerprint, rootEx.toString());
        }
    }

    private String getRequestPath(WebRequest webRequest) {
        if (webRequest instanceof ServletWebRequest) {
            val req = (ServletWebRequest) webRequest;
            return req.getHttpMethod() + " " + req.getRequest().getRequestURI();
        }

        return "Path Info Not Applicable";
    }

    private String generateFingerprint() {
        return DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes(UTF_8));
    }

    private MSResponse<Object> toNewErrorFormat(String fingerprint, int httpStatusCode, String errorCode, String message, String details) {
        val response = new MSResponse<>().setFingerprint(fingerprint);

        ErrorFormat format = errorsProps.format;
        if (format == OLD || format == NEW_COMPATIBLE)
            response.setResponseCode(httpStatusCode)
                    .setResponseMessage(message)
                    .setCode(errorCode)
                    .setDetails(details);

        if (format == NEW || format == NEW_COMPATIBLE) {
            val codedMessage = new MSResponse.CodedMessage(errorCode, message, details);
            response.setErrors(Lists.of(codedMessage));
        }

        return response;
    }

    public enum ErrorFormat {OLD, NEW, NEW_COMPATIBLE}

    @Setter
    @Validated
    @ConfigurationProperties("errors")
    public static class ErrorsProperties {

        /**
         * Whether to use the new error format? See {@link DefaultHttpErrorAttributesAdapter}.
         */
        @NotNull
        public ErrorFormat format = NEW_COMPATIBLE;
    }
}
