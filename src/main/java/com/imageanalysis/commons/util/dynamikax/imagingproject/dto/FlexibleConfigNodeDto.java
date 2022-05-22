package com.imageanalysis.commons.util.dynamikax.imagingproject.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@ToString
@Getter(onMethod_ = @Deprecated)
public class FlexibleConfigNodeDto {

    public final static String HIDE_VISIT_HISTORY_CHRONOLOGY = "hide_visits_chronology";

    public List<FlexibleConfigReaderDto> readers = new ArrayList<>();

    public EndpointDto                     endpoint;
    public List<FlexibleConfigModalityDto> modalities = new ArrayList<>();
    public String                          readingLevel;
    public String                          readingType;
    public String                          readingVersion;
    public Boolean                         moderationEnabled;
    public Boolean                         useDynamikaAI;
    public String                          hideVisitHistory;
}
