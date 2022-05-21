package com.imageanalysis.commons.util.dynamikax.imagingproject.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Setter
@ToString
@Getter(onMethod_ = @Deprecated)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PatientDto {

    @EqualsAndHashCode.Include
    public Long                    id;
    public String              patientCode;
    public Set<VisitConfigDto> visitConfigs = new HashSet<>(); /* Cyclic Reference */
}
