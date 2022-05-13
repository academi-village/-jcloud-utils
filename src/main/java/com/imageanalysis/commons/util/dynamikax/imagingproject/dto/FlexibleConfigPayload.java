package com.imageanalysis.commons.util.dynamikax.imagingproject.dto;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class FlexibleConfigPayload {
    public Long                      id;
    public Long                      studyId;
    public Long                      endpointId;
    public FlexibleConfigNodePayload config;
}
