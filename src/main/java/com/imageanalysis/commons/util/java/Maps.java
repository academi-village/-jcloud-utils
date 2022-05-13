package com.imageanalysis.commons.util.java;

import java.util.Map;

public class Maps<K, V> {

    /**
     * Returns an unmodifiable map containing zero mappings.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @return an empty {@code Map}
     * @since 9
     */
    public static <K, V> Map<K, V> of() {
        return ImmutableCollections.emptyMap();
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
     * @since 9
     */
    public static <K, V> Map<K, V> of(K k1, V v1) {
        return new ImmutableCollections.Map1<>(k1, v1);
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
     * @since 9
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        return new ImmutableCollections.MapN<>(k1, v1, k2, v2);
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
        return new ImmutableCollections.MapN<>(k1, v1, k2, v2, k3, v3);
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
        return new ImmutableCollections.MapN<>(k1, v1, k2, v2, k3, v3, k4, v4);
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
        return new ImmutableCollections.MapN<>(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
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
        return new ImmutableCollections.MapN<>(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5,
                k6, v6);
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
        return new ImmutableCollections.MapN<>(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5,
                k6, v6, k7, v7);
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
        return new ImmutableCollections.MapN<>(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5,
                k6, v6, k7, v7, k8, v8);
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
        return new ImmutableCollections.MapN<>(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5,
                k6, v6, k7, v7, k8, v8, k9, v9);
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
        return new ImmutableCollections.MapN<>(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5,
                k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }

    /**
     * Returns an unmodifiable map containing keys and values extracted from the given entries.
     * The entries themselves are not stored in the map.
     * See <a href="#unmodifiable">Unmodifiable Maps</a> for details.
     *
     * @param <K>     the {@code Map}'s key type
     * @param <V>     the {@code Map}'s value type
     * @param entries {@code Map.Entry}s containing the keys and values from which the map is populated
     * @return a {@code Map} containing the specified mappings
     * @throws IllegalArgumentException if there are any duplicate keys
     * @throws NullPointerException     if any entry, key, or value is {@code null}, or if
     *                                  the {@code entries} array is {@code null}
     * @apiNote It is convenient to create the map entries using the {@link Map#entry Map.entry()} method.
     * For example,
     *
     * <pre>{@code
     *     import static java.util.Map.entry;
     *
     *     Map<Integer,String> map = Map.ofEntries(
     *         entry(1, "a"),
     *         entry(2, "b"),
     *         entry(3, "c"),
     *         ...
     *         entry(26, "z"));
     * }</pre>
     * @see Map#entry Map.entry()
     * @since 9
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <K, V> Map<K, V> ofEntries(Map.Entry<? extends K, ? extends V>... entries) {
        if (entries.length == 0) { // implicit null check of entries array
            return ImmutableCollections.emptyMap();
        } else if (entries.length == 1) {
            // implicit null check of the array slot
            return new ImmutableCollections.Map1<>(entries[0].getKey(),
                    entries[0].getValue());
        } else {
            Object[] kva = new Object[entries.length << 1];
            int      a   = 0;
            for (Map.Entry<? extends K, ? extends V> entry : entries) {
                // implicit null checks of each array slot
                kva[a++] = entry.getKey();
                kva[a++] = entry.getValue();
            }
            return new ImmutableCollections.MapN<>(kva);
        }
    }

    /**
     * Returns an unmodifiable {@link Map.Entry} containing the given key and value.
     * These entries are suitable for populating {@code Map} instances using the
     * {@link Map#ofEntries Map.ofEntries()} method.
     * The {@code Entry} instances created by this method have the following characteristics:
     *
     * <ul>
     * <li>They disallow {@code null} keys and values. Attempts to create them using a {@code null}
     * key or value result in {@code NullPointerException}.
     * <li>They are unmodifiable. Calls to {@link Map.Entry#setValue Entry.setValue()}
     * on a returned {@code Entry} result in {@code UnsupportedOperationException}.
     * <li>They are not serializable.
     * <li>They are <a href="../lang/doc-files/ValueBased.html">value-based</a>.
     * Callers should make no assumptions about the identity of the returned instances.
     * This method is free to create new instances or reuse existing ones. Therefore,
     * identity-sensitive operations on these instances (reference equality ({@code ==}),
     * identity hash code, and synchronization) are unreliable and should be avoided.
     * </ul>
     *
     * @param <K> the key's type
     * @param <V> the value's type
     * @param k   the key
     * @param v   the value
     * @return an {@code Entry} containing the specified key and value
     * @throws NullPointerException if the key or value is {@code null}
     * @apiNote For a serializable {@code Entry}, see {@link AbstractMap.SimpleEntry} or
     * {@link AbstractMap.SimpleImmutableEntry}.
     * @see Map#ofEntries Map.ofEntries()
     * @since 9
     */
    public static <K, V> Map.Entry<K, V> entry(K k, V v) {
        // KeyValueHolder checks for nulls
        return new KeyValueHolder<>(k, v);
    }

    /**
     * Returns an <a href="#unmodifiable">unmodifiable Map</a> containing the entries
     * of the given Map. The given Map must not be null, and it must not contain any
     * null keys or values. If the given Map is subsequently modified, the returned
     * Map will not reflect such modifications.
     *
     * @param <K> the {@code Map}'s key type
     * @param <V> the {@code Map}'s value type
     * @param map a {@code Map} from which entries are drawn, must be non-null
     * @return a {@code Map} containing the entries of the given {@code Map}
     * @throws NullPointerException if map is null, or if it contains any null keys or values
     * @implNote If the given Map is an <a href="#unmodifiable">unmodifiable Map</a>,
     * calling copyOf will generally not create a copy.
     * @since 10
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <K, V> Map<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableCollections.AbstractImmutableMap) {
            return (Map<K, V>) map;
        } else {
            return Maps.ofEntries(map.entrySet().toArray(new Map.Entry[0]));
        }
    }
}
