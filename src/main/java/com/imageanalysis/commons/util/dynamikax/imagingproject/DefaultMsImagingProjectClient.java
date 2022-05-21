package com.imageanalysis.commons.util.dynamikax.imagingproject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.imageanalysis.commons.util.dynamikax.Microservice;
import com.imageanalysis.commons.util.dynamikax.RestClient;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.FlexibleConfigPayload;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.PatientPayload;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.VisitConfigPayload;
import com.imageanalysis.commons.util.java.Maps;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

import static com.imageanalysis.commons.errors.ProjectError.ENDPOINT_ID_NOT_FOUND;

@Slf4j
@Component
@ConditionalOnMissingBean(MsImagingProjectClient.class)
public class DefaultMsImagingProjectClient implements MsImagingProjectClient {

    private static final String   PATIENTS_BY_IDS_URL         = "/api/patient/get-patients-by-ids";
    private static final String   VISIT_CONFIG_URL            = "/api/visit-config/{visitConfigId}";
    private static final String   ENDPOINT_ALL_URL            = "/api/endpoint/retrieve-all";
    private static final String   READING_CONFIG_FLEXIBLE_URL = "/api/reading-config-flexible/get-active-by-study-id-and-endpoint-id/{studyId}/{endpointId}";
    private static final String   SCAN_DATE_URL               = "/api/visit-config/get-scan-dates-by-visit-config-ids";
    private static final Duration LONG_EXPIRATION             = Duration.ofDays(7);

    private final RestClient  restClient;
    private final Environment env;

    public DefaultMsImagingProjectClient(RestClient client, Environment env) {
        restClient = client.forMs(Microservice.MS_IMAGING_PROJECT);
        this.env   = env;
    }

    @Override
    public Map<Long, Instant> fetchVisitConfigsScanDate(@NonNull List<Long> visitConfigIds) {
        log.debug("Fetching visit configs scan date of visitConfigIds: {}", visitConfigIds);
        val responseType = new TypeReference<Map<Long, Instant>>() {};

        return restClient.withCache(LONG_EXPIRATION).put(SCAN_DATE_URL).body(visitConfigIds).execute(responseType);
    }

    @Override
    public VisitConfigPayload fetchVisitConfig(long visitConfigId) {
        log.debug("Fetching visit config of visitConfigId: {}", visitConfigId);
        val      params     = Maps.of("visitConfigId", visitConfigId + "");
        Duration expiration = Duration.ofMinutes(30);
        return restClient.withCache(expiration).get(VISIT_CONFIG_URL, params).execute(VisitConfigPayload.class);
    }

    @Override
    public List<FlexibleConfigPayload> fetchReadingConfigs(long studyId) {
        val endpointName = env.getRequiredProperty("app.endpoint.name");

        return fetchReadingConfigs(studyId, endpointName);
    }

    @Override
    public List<FlexibleConfigPayload> fetchReadingConfigs(long studyId, @NonNull String endpointName) {
        log.debug("Fetching reading (flexible) config of studyId {} and endpointName: {}", studyId, endpointName);
        val params = Maps.of(
                "studyId", studyId + "",
                "endpointId", fetchEndpointId(endpointName)
        );
        val responseType = new TypeReference<List<FlexibleConfigPayload>>() {};

        return restClient.get(READING_CONFIG_FLEXIBLE_URL, params).execute(responseType);
    }

    @Override
    public List<PatientPayload> fetchPatientsByIds(Set<Long> patientIds) {
        log.debug("Fetching patients of IDs: {}", patientIds);
        val responseType = new TypeReference<List<PatientPayload>>() {};

        return restClient.withCache().put(PATIENTS_BY_IDS_URL).body(patientIds).execute(responseType);
    }

    @Nullable
    private Long fetchEndpointId(String endpointName) {
        log.debug("Fetching endpointId of endpointName: {}", endpointName);
        val jsonNode = restClient.withCache(LONG_EXPIRATION).get(ENDPOINT_ALL_URL).execute().unwrappedBody();

        return extractEndpointId(jsonNode, endpointName);
    }

    @Nullable
    private Long extractEndpointId(JsonNode json, String endpointName) {
        log.info("Finding the endpoint ID for endpointName {} from {}", endpointName, json);
        val details = Maps.of("endpointName", endpointName, "json", json);
        return StreamSupport.stream(json.spliterator(), false)
                .filter(jsonNode -> {
                    String name = jsonNode.get("name").asText();
                    return StringUtils.hasText(name) && name.equalsIgnoreCase(endpointName);
                })
                .map(jsonNode -> jsonNode.get("id").asLong()).findFirst()
                .orElseThrow(ENDPOINT_ID_NOT_FOUND.params(endpointName).details(details)::ex);
    }
}
