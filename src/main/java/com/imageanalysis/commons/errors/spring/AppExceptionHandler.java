package com.imageanalysis.commons.errors.spring;

import com.imageanalysis.commons.errors.AppError;
import com.imageanalysis.commons.errors.AppException;
import com.imageanalysis.commons.util.dynamikax.MSResponse;
import com.imageanalysis.commons.util.java.Lists;
import com.imageanalysis.commons.util.java.Maps;
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
import java.util.Map;
import java.util.UUID;

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

        val responseBody = errorsProps.newFormat
                           ? toNewErrorFormat(ex.getError(), fingerprint)
                           : new MSResponse<Object>()
                                   .setResponseCode(httpStatus.value())
                                   .setResponseMessage(error.getMessage())
                                   .setErrorCode(error.getCode())
                                   .setFingerprint(fingerprint)
                                   .setDetails(error.getDetails());

        return new ResponseEntity<>(responseBody, httpStatus);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        val fingerprint = generateFingerprint();
        logError(ex, request, fingerprint, UNAUTHORIZED.value());
        String errorCode = "user.not_authenticated";
        String message   = "JWT token is expired";
        val responseBody = errorsProps.newFormat
                           ? toNewErrorFormat(fingerprint, errorCode, message, null)
                           : new MSResponse<Object>()
                                   .setResponseCode(UNAUTHORIZED.value())
                                   .setResponseMessage(message)
                                   .setErrorCode(errorCode)
                                   .setFingerprint(fingerprint);

        return new ResponseEntity<>(responseBody, UNAUTHORIZED);
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<Object> handleExceptions(Throwable ex, WebRequest request) {
        val message    = ExceptionUtils.getRootCauseMessage(ex);
        val extMessage = request.getDescription(true) + " -> " + message;

        val fingerprint = generateFingerprint();
        logError(ex, request, fingerprint, INTERNAL_SERVER_ERROR.value());

        val responseBody = errorsProps.newFormat
                           ? toNewErrorFormat(fingerprint, SERVER_ERROR_CODE, message, extMessage)
                           : new MSResponse<Object>()
                                   .setResponseCode(INTERNAL_SERVER_ERROR.value())
                                   .setResponseMessage(message)
                                   .setDetails(extMessage)
                                   .setErrorCode(SERVER_ERROR_CODE)
                                   .setFingerprint(fingerprint);

        return new ResponseEntity<>(responseBody, INTERNAL_SERVER_ERROR);
    }

    private Object toNewErrorFormat(AppError appError, String fingerprint) {
        return Maps.of(
                "fingerprint", fingerprint,
                "errors", Lists.of(Maps.of(
                        "code", appError.getCode(),
                        "message", appError.getMessage(),
                        "details", appError.getDetails()
                ))
        );
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

    private Map<String, Object> toNewErrorFormat(String fingerprint, String errorCode, String message, String details) {
        return Maps.of(
                "fingerprint", fingerprint,
                "errors", Lists.of(Maps.of(
                        "code", errorCode,
                        "message", message,
                        "details", details
                ))
        );
    }

    @Setter
    @Validated
    @ConfigurationProperties("errors")
    public static class ErrorsProperties {

        /**
         * Whether to use the new error format? See {@link DefaultHttpErrorAttributesAdapter}.
         */
        @NotNull
        public Boolean newFormat = false;
    }
}
