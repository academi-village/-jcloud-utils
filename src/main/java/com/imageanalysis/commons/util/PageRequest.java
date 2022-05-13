package com.imageanalysis.commons.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import static java.util.Objects.requireNonNull;

/**
 * Encapsulates the pagination information to fetch entities from database.
 *
 * @author Younes Rahimi
 */
@Getter
@Setter
@ToString
public class PageRequest {

    /**
     * Represents the page number; One indexed.
     */
    private Integer page;

    /**
     * Represents the page size.
     */
    private Integer size;

    /**
     * Converts the {@link #page} and {@link #size} to the offset of database query.
     */
    public int toOffset() {
        val page = requireNonNull(this.page);
        val size = requireNonNull(this.size);

        return (page - 1) * size;
    }

    /**
     * Converts the {@link #size} to the limit of database query.
     */
    public long toLimit() {
        return requireNonNull(this.size) + 1;
    }
}
