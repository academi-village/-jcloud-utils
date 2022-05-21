package com.imageanalysis.commons.util.tuple;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Tpl7<V1, V2, V3, V4, V5, V6, V7> {
    public final V1 v1;
    public final V2 v2;
    public final V3 v3;
    public final V4 v4;
    public final V5 v5;
    public final V6 v6;
    public final V7 v7;

    public Tpl7(Tpl6<V1, V2, V3, V4, V5, V6> tpl, V7 v7) {
        this(tpl.v1, tpl.v2, tpl.v3, tpl.v4, tpl.v5, tpl.v6, v7);
    }
}
