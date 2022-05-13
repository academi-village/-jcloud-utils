package com.imageanalysis.commons.util.dynamikax.imagingproject.dto;

import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Setter
@ToString
public class EndpointPayload {
    public Long    id;
    public Instant created;
    public String  name;
}
