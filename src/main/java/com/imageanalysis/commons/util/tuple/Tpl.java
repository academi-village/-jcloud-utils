package com.imageanalysis.commons.util.tuple;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Tpl<V1, V2> {
    public final V1 v1;
    public final V2 v2;
}
