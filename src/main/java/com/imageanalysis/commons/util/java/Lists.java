package com.imageanalysis.commons.util.java;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Lists<E> {

    /**
     * Returns an unmodifiable list containing zero elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @return an empty {@code List}
     * @since 9
     */
    public static <E> List<E> of() {
        return Collections.emptyList();
    }

    /**
     * Returns an unmodifiable list containing one element.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1  the single element
     * @return a {@code List} containing the specified element
     * @throws NullPointerException if the element is {@code null}
     * @since 9
     */
    public static <E> List<E> of(E e1) {
        return Collections.singletonList(e1);
    }

    /**
     * Returns an unmodifiable list containing two elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     * @since 9
     */
    public static <E> List<E> of(E e1, E e2) {
        return Arrays.asList(e1, e2);
    }

    /**
     * Returns an unmodifiable list containing three elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     * @since 9
     */
    public static <E> List<E> of(E e1, E e2, E e3) {
        return Arrays.asList(e1, e2, e3);
    }

    /**
     * Returns an unmodifiable list containing four elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     * @since 9
     */
    public static <E> List<E> of(E e1, E e2, E e3, E e4) {
        return Arrays.asList(e1, e2, e3, e4);
    }

    /**
     * Returns an unmodifiable list containing five elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @param e5  the fifth element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     * @since 9
     */
    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5) {
        return Arrays.asList(e1, e2, e3, e4, e5);
    }

    /**
     * Returns an unmodifiable list containing six elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @param e5  the fifth element
     * @param e6  the sixth element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     * @since 9
     */
    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
        return Arrays.asList(e1, e2, e3, e4, e5,
                e6);
    }

    /**
     * Returns an unmodifiable list containing seven elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @param e5  the fifth element
     * @param e6  the sixth element
     * @param e7  the seventh element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     * @since 9
     */
    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
        return Arrays.asList(e1, e2, e3, e4, e5,
                e6, e7);
    }

    /**
     * Returns an unmodifiable list containing eight elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @param e5  the fifth element
     * @param e6  the sixth element
     * @param e7  the seventh element
     * @param e8  the eighth element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     * @since 9
     */
    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return Arrays.asList(e1, e2, e3, e4, e5,
                e6, e7, e8);
    }

    /**
     * Returns an unmodifiable list containing nine elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E> the {@code List}'s element type
     * @param e1  the first element
     * @param e2  the second element
     * @param e3  the third element
     * @param e4  the fourth element
     * @param e5  the fifth element
     * @param e6  the sixth element
     * @param e7  the seventh element
     * @param e8  the eighth element
     * @param e9  the ninth element
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     * @since 9
     */
    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return Arrays.asList(e1, e2, e3, e4, e5,
                e6, e7, e8, e9);
    }

    /**
     * Returns an unmodifiable list containing ten elements.
     * <p>
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E> the {@code List}'s element type
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
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null}
     * @since 9
     */
    public static <E> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return Arrays.asList(e1, e2, e3, e4, e5,
                e6, e7, e8, e9, e10);
    }

    /**
     * Returns an unmodifiable list containing an arbitrary number of elements.
     * See <a href="#unmodifiable">Unmodifiable List</a> for details.
     *
     * @param <E>      the {@code List}'s element type
     * @param elements the elements to be contained in the list
     * @return a {@code List} containing the specified elements
     * @throws NullPointerException if an element is {@code null} or if the array is {@code null}
     * @apiNote This method also accepts a single array as an argument. The element type of
     * the resulting list will be the component type of the array, and the size of
     * the list will be equal to the length of the array. To create a list with
     * a single element that is an array, do the following:
     *
     * <pre>{@code
     *     String[] array = ... ;
     *     List<String[]> list = List.<String[]>of(array);
     * }</pre>
     * <p>
     * This will cause the {@link List#of(Object) List.of(E)} method
     * to be invoked instead.
     * @since 9
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <E> List<E> of(E... elements) {
        switch (elements.length) { // implicit null check of elements
            case 0:
                return Collections.emptyList();
            case 1:
                return Collections.singletonList(elements[0]);
            default:
                return Arrays.asList(elements);
        }
    }
}
