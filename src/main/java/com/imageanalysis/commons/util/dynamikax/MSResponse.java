package com.imageanalysis.commons.util.dynamikax;

import com.imageanalysis.commons.errors.AppException;
import com.imageanalysis.commons.errors.ProjectError;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated Do NOT use this if you could. Must be removed.
 */
@Slf4j
@Getter
@Setter
@ToString
public class MSResponse<T> {
    public Integer responseCode;

    @Nullable
    public String responseMessage;

    @Nullable
    public T data;

    @Nullable
    public String errorCode;

    /**
     * If the response code is not 200 (OK) throws an exception.
     */
    public MSResponse<T> checkStatus() {
        if (responseCode != 200) {
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
}
