package com.imageanalysis.commons.util.tuple;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Tpl5<V1, V2, V3, V4, V5> {
    public final V1 v1;
    public final V2 v2;
    public final V3 v3;
    public final V4 v4;
    public final V5 v5;

    public Tpl5(Tpl4<V1, V2, V3, V4> tpl, V5 v5) {
        this(tpl.v1, tpl.v2, tpl.v3, tpl.v4, v5);
    }
}
