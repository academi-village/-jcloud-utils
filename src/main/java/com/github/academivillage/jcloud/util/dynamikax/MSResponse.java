package com.github.academivillage.jcloud.util.dynamikax;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @deprecated Must be removed
 */
@Getter
@ToString
@RequiredArgsConstructor
public class MSResponse<T> {
    public final int    responseCode;
    public final String responseMessage;
    public final T      data;

    public final String errorCode;

    @Deprecated
    public static <T> MSResponse<T> ok(T responseBody) {
        return new MSResponse<>(200, "OK", responseBody, null);
    }
}
