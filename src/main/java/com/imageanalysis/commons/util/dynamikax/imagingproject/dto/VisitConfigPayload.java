package com.imageanalysis.commons.util.dynamikax.imagingproject.dto;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Setter
@ToString
public class VisitConfigPayload implements Comparable<VisitConfigPayload> {

    public Long               id;
    public PatientPayload     patient;
    public VisitPayload       visit;
    public String             visitName;
    public String             visitBlindName;
    public VisitType          visitType;
    public Boolean            noUploads;
    public Set<SeriesPayload> series;

    @Override
    public int compareTo(VisitConfigPayload other) {
        if (visit == other.visit) {
            return 0;
        }
        if (visit.baseline && !other.visit.baseline) {
            return -1;
        }
        if (!visit.baseline && other.visit.baseline) {
            return 1;
        }
        return visit.durationTimeUnit.calcInDays(visit.durationTimeValue)
                .compareTo(other.visit.durationTimeUnit.calcInDays(other.visit.durationTimeValue));
    }

    @RequiredArgsConstructor
    public enum VisitType {
        BASELINE("B"),
        REGUALAR("R"),
        UNSCHEDULED_REGULAR("UR"),
        UNSCHEDULED_EARLY_TERMINATION("UET"),
        UNSCHEDULED_POST_TREATMENT("UPT"),
        TERMINATED("T");

        public final String code;
    }
}
