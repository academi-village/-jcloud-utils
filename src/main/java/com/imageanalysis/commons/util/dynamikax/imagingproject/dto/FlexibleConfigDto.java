package com.imageanalysis.commons.util.dynamikax.imagingproject.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter(onMethod_ = @Deprecated)
public class FlexibleConfigDto {
    @EqualsAndHashCode.Include
    public Long                  id;
    public Long                  studyId;
    public Long                  endpointId;
    public FlexibleConfigNodeDto config;
}
