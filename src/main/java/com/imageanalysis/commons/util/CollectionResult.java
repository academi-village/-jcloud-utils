package com.imageanalysis.commons.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/**
 * DTO to wrap collection of elements.
 *
 * @author Younes Rahimi
 */
@ToString
@RequiredArgsConstructor
@JsonAutoDetect(fieldVisibility = ANY)
public class CollectionResult<T> {

    /**
     * The actual elements.
     */
    private final Collection<T> elements;
}
