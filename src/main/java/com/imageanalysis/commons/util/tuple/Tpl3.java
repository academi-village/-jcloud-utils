package com.imageanalysis.commons.util.tuple;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Tpl3<V1, V2, V3> {
    public final V1 v1;
    public final V2 v2;
    public final V3 v3;

    public Tpl3(Tpl<V1, V2> tpl, V3 v3) {
        this(tpl.v1, tpl.v2, v3);
    }
}
