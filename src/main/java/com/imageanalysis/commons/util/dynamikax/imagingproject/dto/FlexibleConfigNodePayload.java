package com.imageanalysis.commons.util.dynamikax.imagingproject.dto;

import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@ToString
public class FlexibleConfigNodePayload {

    public final static String HIDE_VISIT_HISTORY_CHRONOLOGY = "hide_visits_chronology";

    public List<FlexibleConfigReaderPayload> readers = new ArrayList<>();

    public EndpointPayload                     endpoint;
    public List<FlexibleConfigModalityPayload> modalities = new ArrayList<>();
    public String                              readingLevel;
    public String                              readingType;
    public String                              readingVersion;
    public Boolean                             moderationEnabled;
    public Boolean                             useDynamikaAI;
    public String                              hideVisitHistory;
}
