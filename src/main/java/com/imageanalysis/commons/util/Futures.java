package com.imageanalysis.commons.util;

import com.imageanalysis.commons.util.tuple.*;
import lombok.NonNull;

import java.util.concurrent.CompletableFuture;

public class Futures {

    /**
     * @see CompletableFuture#allOf(CompletableFuture[])
     */
    public static <V1, V2> CompletableFuture<Tpl<V1, V2>> allOf(
            @NonNull CompletableFuture<V1> cf1,
            @NonNull CompletableFuture<V2> cf2) {
        return cf1.thenCombineAsync(cf2, Tpl::new);
    }

    /**
     * @see CompletableFuture#allOf(CompletableFuture[])
     */
    public static <V1, V2, V3> CompletableFuture<Tpl3<V1, V2, V3>> allOf(
            @NonNull CompletableFuture<V1> cf1,
            @NonNull CompletableFuture<V2> cf2,
            @NonNull CompletableFuture<V3> cf3) {
        return cf1.thenCombineAsync(cf2, Tpl::new).thenCombineAsync(cf3, Tpl3::new);
    }

    /**
     * @see CompletableFuture#allOf(CompletableFuture[])
     */
    public static <V1, V2, V3, V4> CompletableFuture<Tpl4<V1, V2, V3, V4>> allOf(
            @NonNull CompletableFuture<V1> cf1,
            @NonNull CompletableFuture<V2> cf2,
            @NonNull CompletableFuture<V3> cf3,
            @NonNull CompletableFuture<V4> cf4
    ) {
        return cf1.thenCombineAsync(cf2, Tpl::new)
                .thenCombineAsync(cf3, Tpl3::new)
                .thenCombineAsync(cf4, Tpl4::new);
    }

    /**
     * @see CompletableFuture#allOf(CompletableFuture[])
     */
    public static <V1, V2, V3, V4, V5> CompletableFuture<Tpl5<V1, V2, V3, V4, V5>> allOf(
            @NonNull CompletableFuture<V1> cf1,
            @NonNull CompletableFuture<V2> cf2,
            @NonNull CompletableFuture<V3> cf3,
            @NonNull CompletableFuture<V4> cf4,
            @NonNull CompletableFuture<V5> cf5
    ) {
        return cf1.thenCombineAsync(cf2, Tpl::new)
                .thenCombineAsync(cf3, Tpl3::new)
                .thenCombineAsync(cf4, Tpl4::new)
                .thenCombineAsync(cf5, Tpl5::new);
    }

    /**
     * @see CompletableFuture#allOf(CompletableFuture[])
     */
    public static <V1, V2, V3, V4, V5, V6> CompletableFuture<Tpl6<V1, V2, V3, V4, V5, V6>> allOf(
            @NonNull CompletableFuture<V1> cf1,
            @NonNull CompletableFuture<V2> cf2,
            @NonNull CompletableFuture<V3> cf3,
            @NonNull CompletableFuture<V4> cf4,
            @NonNull CompletableFuture<V5> cf5,
            @NonNull CompletableFuture<V6> cf6
    ) {
        return cf1.thenCombineAsync(cf2, Tpl::new)
                .thenCombineAsync(cf3, Tpl3::new)
                .thenCombineAsync(cf4, Tpl4::new)
                .thenCombineAsync(cf5, Tpl5::new)
                .thenCombineAsync(cf6, Tpl6::new);
    }

    /**
     * @see CompletableFuture#allOf(CompletableFuture[])
     */
    public static <V1, V2, V3, V4, V5, V6, V7> CompletableFuture<Tpl7<V1, V2, V3, V4, V5, V6, V7>> allOf(
            @NonNull CompletableFuture<V1> cf1,
            @NonNull CompletableFuture<V2> cf2,
            @NonNull CompletableFuture<V3> cf3,
            @NonNull CompletableFuture<V4> cf4,
            @NonNull CompletableFuture<V5> cf5,
            @NonNull CompletableFuture<V6> cf6,
            @NonNull CompletableFuture<V7> cf7
    ) {
        return cf1.thenCombineAsync(cf2, Tpl::new)
                .thenCombineAsync(cf3, Tpl3::new)
                .thenCombineAsync(cf4, Tpl4::new)
                .thenCombineAsync(cf5, Tpl5::new)
                .thenCombineAsync(cf6, Tpl6::new)
                .thenCombineAsync(cf7, Tpl7::new);
    }
}
