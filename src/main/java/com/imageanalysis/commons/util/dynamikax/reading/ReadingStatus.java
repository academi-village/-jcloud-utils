package com.imageanalysis.commons.util.dynamikax.reading;

import com.imageanalysis.commons.util.java.Maps;
import com.imageanalysis.commons.util.java.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;

@Getter
@RequiredArgsConstructor
public enum ReadingStatus {

    /**
     * Reading just created
     */
    NEW_READING(100),

    /**
     * Reading is waiting for the reader to be assigned
     */
    READER_PENDING(200),

    /**
     * Reader is assigned
     */
    READER_ASSIGNED(300),

    /**
     * When batch option is enabled then this status
     * identifies readings available for the batch to be created
     */
    BATCH_PENDING(400),

    /**
     * Reading is assigned to some batch,
     * this status shows that reading is in the batch
     * but not ready to appear in the reader’s worklist.
     * waiting for the entire batch to be ready
     */
    BATCH_ASSIGNED(500),

    /**
     * Reading is in the reader’s worklist
     * and waiting for the reader to open it
     */
    READING_PENDING(600),

    /**
     * Reading form has been opened by
     * reader but not submitted as ready or terminated
     */
    IN_PROGRESS(700),

    /**
     * When adjudication option is enabled then
     * readings from level 1 must wait under this
     * status for other readings until adjudication rule check occurs
     */
    ADJUDICATION_CHECK_PENDING(800),

    /**
     * This is the final status of the reading
     * when reading has been done successfully
     */
    COMPLETE(900),

    /**
     * This is the final status of the reading
     * when reading has been cancelled
     */
    TERMINATED(1000),
    ;

    private static final Map<ReadingStatus, Set<ReadingStatus>> statusTransitionsMap = Maps.of(
            NEW_READING, Sets.of(READER_PENDING, TERMINATED),
            READER_PENDING, Sets.of(READER_ASSIGNED, TERMINATED),
            READER_ASSIGNED, Sets.of(READER_PENDING, BATCH_PENDING, TERMINATED),
            BATCH_PENDING, Sets.of(READER_PENDING, BATCH_ASSIGNED, READING_PENDING, TERMINATED),
            BATCH_ASSIGNED, Sets.of(READER_PENDING, BATCH_PENDING, READING_PENDING, TERMINATED),
            READING_PENDING, Sets.of(READER_PENDING, BATCH_PENDING, IN_PROGRESS, TERMINATED),
            IN_PROGRESS, Sets.of(ADJUDICATION_CHECK_PENDING, COMPLETE, TERMINATED),
            ADJUDICATION_CHECK_PENDING, Sets.of(COMPLETE, TERMINATED)
    );

    private final int code;

    public static ReadingStatus ofCode(int code) {
        return Arrays.stream(values()).filter(it -> it.getCode() == code)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Wrong Status Code: " + code));
    }

    public boolean canTransitTo(ReadingStatus newStatus) {
        return statusTransitionsMap.getOrDefault(this, emptySet()).contains(newStatus);
    }
}
