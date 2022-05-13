package com.imageanalysis.commons.util.dynamikax.imagingproject.dto;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class PatientPayload {

    public Long   id;
    public String patientCode;
//    public Set<VisitConfigPayload> visitConfigs = new HashSet<>(); TODO YR: Check this
}
