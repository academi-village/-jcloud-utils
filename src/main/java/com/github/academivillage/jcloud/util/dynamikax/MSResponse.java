package com.github.academivillage.jcloud.util.dynamikax;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @deprecated Must be removed
 */
@Getter
@Setter
@ToString
public class MSResponse<T> {
    public int    responseCode;
    public String responseMessage;
    public T      data;

    public String errorCode;
}
