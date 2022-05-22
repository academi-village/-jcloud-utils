package com.imageanalysis.commons.util.dynamikax;

import com.imageanalysis.commons.errors.AppException;
import com.imageanalysis.commons.errors.ProjectError;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
public class MSResponse<T> {

    /**
     * @deprecated Do NOT use this if you could. Must be removed.
     */
    @Nullable
    public Integer responseCode;

    /**
     * @deprecated Do NOT use this if you could. Must be removed.
     */
    @Nullable
    public String responseMessage;

    /**
     * @deprecated Do NOT use this if you could. Must be removed.
     */
    @Nullable
    public String code;

    /**
     * @deprecated Do NOT use this if you could. Must be removed.
     */
    @Nullable
    public T data;

    /**
     * @deprecated Do NOT use this if you could. Must be removed.
     */
    @Nullable
    public Object details;

    @Nullable
    public List<CodedMessage> errors;

    @Nullable
    public String fingerprint;

    /**
     * If the response code is not 200 (OK) throws an exception.
     */
    public MSResponse<T> checkStatus() {
        if (responseCode != null && responseCode != 200) {
            log.error("The response status is not OK: {}", this);
            throw new AppException(ProjectError.REMOTE_SERVICE_FAILED.details(this));
        }

        return this;
    }

    @Deprecated
    public static <T> MSResponse<T> ok(T responseBody) {
        return new MSResponse<T>()
                .setResponseCode(200)
                .setResponseMessage("OK")
                .setData(responseBody);
    }

    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CodedMessage {

        /**
         * The error code.
         */
        public String code;

        /**
         * The error message.
         */
        public String message;

        @Nullable
        public Object details;
    }
}
