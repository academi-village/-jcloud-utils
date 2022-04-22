package com.github.academivillage.jcloud.util.dynamikax;

import lombok.Data;

/**
 * @deprecated Must be removed
 */
@Data
@Deprecated
public class MSResponse<T> {
    private int    responseCode;
    private String responseMessage;
    private T      data;

    private String errorCode;
}
