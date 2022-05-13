package com.imageanalysis.commons.util.dynamikax.imagingproject.dto;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class VisitPayload implements Comparable<VisitPayload> {

    public String name;

    public DurationTimeUnit durationTimeUnit;

    public Long durationTimeValue;

    public Boolean baseline;

    public Boolean repeatAllowed;

    @Override
    public int compareTo(VisitPayload otherVisit) {
        if (this == otherVisit) {
            return 0;
        }
        if (this.baseline && !otherVisit.baseline) {
            return -1;
        }
        if (!this.baseline && otherVisit.baseline) {
            return 1;
        }
        return this.durationTimeUnit.calcInDays(this.durationTimeValue).
                compareTo(otherVisit.durationTimeUnit.calcInDays(otherVisit.durationTimeValue));
    }

    @RequiredArgsConstructor
    public enum DurationTimeUnit {
        D(1L, "D"),
        W(7L, "W"),
        M(30L, "M");

        public final Long   days;
        public final String code;

        public Long calcInDays(Long timeInterval) {
            return this.days * timeInterval;
        }
    }
}
