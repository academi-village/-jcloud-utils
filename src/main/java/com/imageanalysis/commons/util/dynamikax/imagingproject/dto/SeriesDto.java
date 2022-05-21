package com.imageanalysis.commons.util.dynamikax.imagingproject.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
@ToString
public class SeriesDto {

    public Long         id;
    //    private VisitConfigPayload visitConfig;
    public Long         defaultWindow;
    public Long         defaultLevel;
    public String       defaultColormap;
    public String       label;
    public String       modality;
    public String       seriesUID;
    public String       studyUID;
    public Boolean      available;
    public Boolean      status;
    public String       bodyPart;
    public String       seriesDescription;
    public String       originalOrientation;
    public Instant      scanDate;
    public String       projectModality;
    public Float        seqDuration;
    public JsonNode     fieldOfView;
    public Long         uploadFileId;
    public Boolean      deletable;
    public String       viewerName;
    public String       mapName;
    public Long         readingConfigFlexibleId;
    public List<String> sliceNames = new ArrayList<>();
    public long         slicesSize;
}
