package com.imageanalysis.commons.util.dynamikax.imagingproject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.imageanalysis.commons.util.dynamikax.Microservice;
import com.imageanalysis.commons.util.dynamikax.RestClient;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.FlexibleConfigDto;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.PatientDto;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.SiteConfigDto;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.VisitConfigDto;
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
    private static final Duration LONG_EXPIRATION = Duration.ofDays(7);

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
        val path         = "/api/visit-config/get-scan-dates-by-visit-config-ids";

        return restClient.withCache(LONG_EXPIRATION).put(path).body(visitConfigIds).execute(responseType);
    }

    @Override
    public VisitConfigDto fetchVisitConfig(long visitConfigId) {
        log.debug("Fetching visit config of visitConfigId: {}", visitConfigId);
        val params     = Maps.of("visitConfigId", visitConfigId + "");
        val expiration = Duration.ofMinutes(30);
        val path       = "/api/visit-config/{visitConfigId}";
        return restClient.withCache(expiration).get(path, params).execute(VisitConfigDto.class);
    }

    @Override
    public Set<FlexibleConfigDto> fetchReadingConfigs(long studyId) {
        val endpointName = env.getRequiredProperty("app.endpoint.name");

        return fetchReadingConfigs(studyId, endpointName);
    }

    @Override
    public Set<FlexibleConfigDto> fetchReadingConfigs(long studyId, @NonNull String endpointName) {
        log.debug("Fetching reading (flexible) config of studyId {} and endpointName: {}", studyId, endpointName);
        val params = Maps.of(
                "studyId", studyId + "",
                "endpointId", fetchEndpointId(endpointName)
        );
        val responseType = new TypeReference<Set<FlexibleConfigDto>>() {};
        val path         = "/api/reading-config-flexible/get-active-by-study-id-and-endpoint-id/{studyId}/{endpointId}";

        return restClient.get(path, params).execute(responseType);
    }

    @Override
    public SiteConfigDto fetchSiteConfig(long studyId) {
        val expiration = Duration.ofHours(2);
        val path       = "/api/site-config/get-site-configs-by-study-id/{studyId}";
        return restClient.withCache(expiration).get(path, studyId).execute(SiteConfigDto.class);
    }

    @Override
    public Set<PatientDto> fetchPatientsByIds(Set<Long> patientIds) {
        log.debug("Fetching patients of IDs: {}", patientIds);
        val responseType = new TypeReference<Set<PatientDto>>() {};
        val path         = "/api/patient/get-patients-by-ids";

        return restClient.withCache().put(path).body(patientIds).execute(responseType);
    }

    @Override
    public PatientDto fetchPatientById(long patientId) {
        val expiration = Duration.ofDays(1);
        val path       = "/api/patient/{patientId}";
        return restClient.withCache(expiration).get(path, patientId).execute(PatientDto.class);
    }

    @Nullable
    private Long fetchEndpointId(String endpointName) {
        log.debug("Fetching endpointId of endpointName: {}", endpointName);
        val path     = "/api/endpoint/retrieve-all";
        val jsonNode = restClient.withCache(LONG_EXPIRATION).get(path).execute().asJsonNode();

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
