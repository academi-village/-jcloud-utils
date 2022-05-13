package com.imageanalysis.commons.util.dynamikax.imagingproject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.imageanalysis.commons.util.dynamikax.RestClient;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.VisitConfigPayload;
import com.imageanalysis.commons.util.java.Maps;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.imageanalysis.commons.util.dynamikax.Microservice.MS_IMAGING_PROJECT;

@Component
public class ImagingProjectClient {

    private static final String PATIENTS_BY_IDS_URL         = "/api/patient/get-patients-by-ids";
    private static final String VISIT_CONFIG_URL            = "/api/visit-config/{visitConfigId}";
    private static final String ENDPOINT_ALL_URL            = "/api/endpoint/retrieve-all";
    private static final String READING_CONFIG_FLEXIBLE_URL = "/api/reading-config-flexible/get-active-by-study-id-and-endpoint-id/{studyId}/{endpointId}";
    private static final String SCAN_DATE_URL               = "/api/visit-config/get-scan-dates-by-visit-config-ids";

    private final RestClient restClient;

    public ImagingProjectClient(RestClient client) {restClient = client.forMs(MS_IMAGING_PROJECT);}

    public Map<Long, Instant> getVisitConfigsScanDate(List<Long> visitConfigIds) {
        val expiration   = Duration.ofDays(7);
        val responseType = new TypeReference<Map<Long, Instant>>() {};

        return restClient.withCache(expiration).put(SCAN_DATE_URL).body(visitConfigIds).execute(responseType);
    }

    public VisitConfigPayload getVisitConfig(Long visitConfigId) {
        val params = Maps.of("visitConfigId", visitConfigId.toString());
        return restClient.withCache().get(VISIT_CONFIG_URL, params).execute(VisitConfigPayload.class);
    }

    public List<FlexibleConfigPayload> getReadingConfigByStudyIdAndEndpointId(Long studyId, String token) throws JsonProcessingException {
        Map<String, String> params = new HashMap<>();
        params.put("studyId", String.valueOf(studyId));
        params.put("endpointId", String.valueOf(getEndpoint(token)));
        return
                jsonService.parseForReadingDataConfig(
                        restGet(
                                UriComponentsBuilder.
                                        fromHttpUrl(MSIMAGINGPROJECT + READING_CONFIG_FLEXIBLE_URL).
                                        buildAndExpand(params).
                                        toUriString(),
                                token
                        )
                );
    }

    public Long getEndpoint(String token) throws JsonProcessingException {
        return
                jsonService.parseForEndpointId(
                        restGet(MSIMAGINGPROJECT + ENDPOINT_ALL_URL, token),
                        endpointName
                );
    }

    public List<PatientPayload> getPatientByIds(Set<Long> patientIds, String token) throws JsonProcessingException {
        return
                jsonService.parseForPatients(
                        restPut(
                                MSIMAGINGPROJECT + PATIENTS_BY_IDS_URL,
                                jsonService.writeValueAsString(patientIds),
                                token
                        )
                );
    }
}
