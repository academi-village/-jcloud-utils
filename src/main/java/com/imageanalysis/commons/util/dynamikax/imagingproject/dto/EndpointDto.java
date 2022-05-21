package com.imageanalysis.commons.util.dynamikax.imagingproject.dto;

import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Setter
@ToString
public class EndpointDto {
    public Long    id;
    public Instant created;
    public String  name;
}
