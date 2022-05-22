package com.imageanalysis.commons.errors.spring;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.alidg.errors.HttpError;
import me.alidg.errors.WebErrorHandlerPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import static com.imageanalysis.commons.errors.spring.AppExceptionHandler.SERVER_ERROR_CODE;

/**
 * Responsible for logging the unknown errors.
 *
 * @author Younes Rahimi
 */
@Slf4j
@Component
public class ErrorHandlerPostProcessor implements WebErrorHandlerPostProcessor {

    /**
     * @see WebErrorHandlerPostProcessor#process(HttpError)
     */
    @Override
    public void process(HttpError error) {
        if (error.getOriginalException() == null) {
            log.error("Original exception is null! {}", error);
            return;
        }

        String requestPath      = getRequestPath(error);
        val    fingerprint      = error.getFingerprint();
        val    isUnhandledError = error.getErrors().stream().anyMatch(it -> SERVER_ERROR_CODE.equals(it.getCode()));
        if (isUnhandledError) {
            val errorMsg = String.format("Error did NOT handled. %s - Fingerprint: %s", requestPath, fingerprint);
            log.error(errorMsg, error.getOriginalException());
        } else {
            log.debug("Error handled. {} - Fingerprint: {} - {}",
                    requestPath, fingerprint, error.getOriginalException().toString());
        }
    }

    private String getRequestPath(HttpError error) {
        if (error.getRequest() instanceof ServletWebRequest) {
            val req = ((ServletWebRequest) error.getRequest());
            return req.getHttpMethod() + " " + req.getRequest().getRequestURI();
        }

        return "Path Info Not Applicable";
    }
}
