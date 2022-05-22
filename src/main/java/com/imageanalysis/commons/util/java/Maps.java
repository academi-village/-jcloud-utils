package com.imageanalysis.commons.util.java;

import lombok.val;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Maps<K, V> {

    /**
     * Returns an unmodifiable map containing zero mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return an empty {@code Map}
     */
    public static <K, V> Map<K, V> of() {
        return Collections.emptyMap();
    }

    /**
     * Returns an unmodifiable map containing a single mapping.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the mapping's key
     * @param v1  the mapping's value
     * @return a {@code Map} containing the specified mapping
     * @throws NullPointerException if the key or the value is {@code null}
     */
    public static <K, V> Map<K, V> of(K k1, V v1) {
        return Collections.singletonMap(k1, v1);
    }

    /**
     * Returns an unmodifiable map containing two mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if the keys are duplicates
     * @throws NullPointerException     if any key or value is {@code null}
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        val map = new LinkedHashMap<K, V>(2);
        map.put(k1, v1);
        map.put(k2, v2);

        return map;
    }

    /**
     * Returns an unmodifiable map containing three mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     * @since 9
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        val map = new LinkedHashMap<K, V>(3);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);

        return map;
    }

    /**
     * Returns an unmodifiable map containing four mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     * @since 9
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        val map = new LinkedHashMap<K, V>(4);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);

        return map;
    }

    /**
     * Returns an unmodifiable map containing five mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     * @since 9
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        val map = new LinkedHashMap<K, V>(5);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);

        return map;
    }

    /**
     * Returns an unmodifiable map containing six mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @param k6  the sixth mapping's key
     * @param v6  the sixth mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     * @since 9
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                      K k6, V v6) {
        val map = new LinkedHashMap<K, V>(6);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);

        return map;
    }

    /**
     * Returns an unmodifiable map containing seven mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @param k6  the sixth mapping's key
     * @param v6  the sixth mapping's value
     * @param k7  the seventh mapping's key
     * @param v7  the seventh mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     * @since 9
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                      K k6, V v6, K k7, V v7) {
        val map = new LinkedHashMap<K, V>(7);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);

        return map;
    }

    /**
     * Returns an unmodifiable map containing eight mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @param k6  the sixth mapping's key
     * @param v6  the sixth mapping's value
     * @param k7  the seventh mapping's key
     * @param v7  the seventh mapping's value
     * @param k8  the eighth mapping's key
     * @param v8  the eighth mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     * @since 9
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                      K k6, V v6, K k7, V v7, K k8, V v8) {
        val map = new LinkedHashMap<K, V>(8);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);

        return map;
    }

    /**
     * Returns an unmodifiable map containing nine mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @param k6  the sixth mapping's key
     * @param v6  the sixth mapping's value
     * @param k7  the seventh mapping's key
     * @param v7  the seventh mapping's value
     * @param k8  the eighth mapping's key
     * @param v8  the eighth mapping's value
     * @param k9  the ninth mapping's key
     * @param v9  the ninth mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     * @since 9
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                      K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        val map = new LinkedHashMap<K, V>(9);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);

        return map;
    }

    /**
     * Returns an unmodifiable map containing ten mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param k1  the first mapping's key
     * @param v1  the first mapping's value
     * @param k2  the second mapping's key
     * @param v2  the second mapping's value
     * @param k3  the third mapping's key
     * @param v3  the third mapping's value
     * @param k4  the fourth mapping's key
     * @param v4  the fourth mapping's value
     * @param k5  the fifth mapping's key
     * @param v5  the fifth mapping's value
     * @param k6  the sixth mapping's key
     * @param v6  the sixth mapping's value
     * @param k7  the seventh mapping's key
     * @param v7  the seventh mapping's value
     * @param k8  the eighth mapping's key
     * @param v8  the eighth mapping's value
     * @param k9  the ninth mapping's key
     * @param v9  the ninth mapping's value
     * @param k10 the tenth mapping's key
     * @param v10 the tenth mapping's value
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any key or value is {@code null}
     * @since 9
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                      K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        val map = new LinkedHashMap<K, V>(10);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);

        return map;
    }
}
