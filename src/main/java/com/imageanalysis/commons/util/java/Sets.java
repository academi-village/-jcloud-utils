package com.imageanalysis.commons.util.java;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Sets<E> {

    /**
     * Returns an unmodifiable set containing zero elements.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E> the {@code Set}'s element type
     * @return an empty {@code Set}
     * @since 9
     */
    public static <E> Set<E> of() {
        return Collections.emptySet();
    }

    /**
     * Returns an unmodifiable set containing one element.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E> the {@code Set}'s element type
     * @param e1  the single element
     * @return a {@code Set} containing the specified element
     * @throws NullPointerException if the element is {@code null}
     * @since 9
     */
    public static <E> Set<E> of(E e1) {
        return Collections.singleton(e1);
    }

    /**
     * Returns an unmodifiable set containing two elements.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E> the {@code Set}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @return a {@code Set} containing the specified elements
     * @throws IllegalArgumentException if the elements are duplicates
     * @throws NullPointerException     if an element is {@code null}
     * @since 9
     */
    public static <E> Set<E> of(E e1, E e2) {
        return new LinkedHashSet<>(Arrays.asList(e1, e2));
    }

    /**
     * Returns an unmodifiable set containing three elements.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E> the {@code Set}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @return a {@code Set} containing the specified elements
     * @throws IllegalArgumentException if there are any duplicate elements
     * @throws NullPointerException     if an element is {@code null}
     * @since 9
     */
    public static <E> Set<E> of(E e1, E e2, E e3) {
        return new LinkedHashSet<>(Arrays.asList(e1, e2, e3));
    }

    /**
     * Returns an unmodifiable set containing four elements.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E> the {@code Set}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @return a {@code Set} containing the specified elements
     * @throws IllegalArgumentException if there are any duplicate elements
     * @throws NullPointerException     if an element is {@code null}
     * @since 9
     */
    public static <E> Set<E> of(E e1, E e2, E e3, E e4) {
        return new LinkedHashSet<>(Arrays.asList(e1, e2, e3, e4));
    }

    /**
     * Returns an unmodifiable set containing five elements.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E> the {@code Set}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @param e5  the fifth element
     * @return a {@code Set} containing the specified elements
     * @throws IllegalArgumentException if there are any duplicate elements
     * @throws NullPointerException     if an element is {@code null}
     * @since 9
     */
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5) {
        return new LinkedHashSet<>(Arrays.asList(e1, e2, e3, e4, e5));
    }

    /**
     * Returns an unmodifiable set containing six elements.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E> the {@code Set}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @param e5  the fifth element
     * @param e6  the sixth element
     * @return a {@code Set} containing the specified elements
     * @throws IllegalArgumentException if there are any duplicate elements
     * @throws NullPointerException     if an element is {@code null}
     * @since 9
     */
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
        return new LinkedHashSet<>(Arrays.asList(e1, e2, e3, e4, e5, e6));
    }

    /**
     * Returns an unmodifiable set containing seven elements.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E> the {@code Set}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @param e5  the fifth element
     * @param e6  the sixth element
     * @param e7  the seventh element
     * @return a {@code Set} containing the specified elements
     * @throws IllegalArgumentException if there are any duplicate elements
     * @throws NullPointerException     if an element is {@code null}
     * @since 9
     */
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
        return new LinkedHashSet<>(Arrays.asList(e1, e2, e3, e4, e5, e6, e7));
    }

    /**
     * Returns an unmodifiable set containing eight elements.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E> the {@code Set}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @param e5  the fifth element
     * @param e6  the sixth element
     * @param e7  the seventh element
     * @param e8  the eighth element
     * @return a {@code Set} containing the specified elements
     * @throws IllegalArgumentException if there are any duplicate elements
     * @throws NullPointerException     if an element is {@code null}
     * @since 9
     */
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return new LinkedHashSet<>(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8));
    }

    /**
     * Returns an unmodifiable set containing nine elements.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E> the {@code Set}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @param e5  the fifth element
     * @param e6  the sixth element
     * @param e7  the seventh element
     * @param e8  the eighth element
     * @param e9  the ninth element
     * @return a {@code Set} containing the specified elements
     * @throws IllegalArgumentException if there are any duplicate elements
     * @throws NullPointerException     if an element is {@code null}
     * @since 9
     */
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return new LinkedHashSet<>(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8, e9));
    }

    /**
     * Returns an unmodifiable set containing ten elements.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E> the {@code Set}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @param e5  the fifth element
     * @param e6  the sixth element
     * @param e7  the seventh element
     * @param e8  the eighth element
     * @param e9  the ninth element
     * @param e10 the tenth element
     * @return a {@code Set} containing the specified elements
     * @throws IllegalArgumentException if there are any duplicate elements
     * @throws NullPointerException     if an element is {@code null}
     * @since 9
     */
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return new LinkedHashSet<>(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10));
    }

    /**
     * Returns an unmodifiable set containing an arbitrary number of elements.
     * See <a href="#unmodifiable">Unmodifiable Sets</a> for details.
     *
     * @param <E>      the {@code Set}'s element type
     * @param elements the elements to be contained in the set
     * @return a {@code Set} containing the specified elements
     * @throws IllegalArgumentException if there are any duplicate elements
     * @throws NullPointerException     if an element is {@code null} or if the array is {@code null}
     * @apiNote This method also accepts a single array as an argument. The element type of
     * the resulting set will be the component type of the array, and the size of
     * the set will be equal to the length of the array. To create a set with
     * a single element that is an array, do the following:
     *
     * <pre>{@code
     *     String[] array = ... ;
     *     Set<String[]> list = Set.<String[]>of(array);
     * }</pre>
     * <p>
     * This will cause the {@link Set#of(Object) Set.of(E)} method
     * to be invoked instead.
     * @since 9
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <E> Set<E> of(E... elements) {
        switch (elements.length) { // implicit null check of elements
            case 0:
                return Collections.emptySet();
            case 1:
                return Collections.singleton(elements[0]);
            default:
                return new LinkedHashSet<>(Arrays.asList(elements));
        }
    }
}
