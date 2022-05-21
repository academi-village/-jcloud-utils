package com.imageanalysis.commons.util.tuple;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Tpl4<V1, V2, V3, V4> {
    public final V1 v1;
    public final V2 v2;
    public final V3 v3;
    public final V4 v4;

    public Tpl4(Tpl3<V1, V2, V3> tpl, V4 v4) {
        this(tpl.v1, tpl.v2, tpl.v3, v4);
    }
}
