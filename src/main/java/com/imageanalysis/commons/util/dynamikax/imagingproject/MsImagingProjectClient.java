package com.imageanalysis.commons.util.dynamikax.imagingproject;

import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.FlexibleConfigDto;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.PatientDto;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.SiteConfigDto;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.VisitConfigDto;
import org.springframework.core.env.Environment;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MsImagingProjectClient {

    Map<Long, Instant> fetchVisitConfigsScanDate(List<Long> visitConfigIds);

    VisitConfigDto fetchVisitConfig(long visitConfigId);

    /**
     * Fetches the reading configs for the give studyId. The endpoint name would be fetched from {@link Environment}.
     * The required property name is {@code app.endpoint.name}.
     *
     * @return The all flexible configs for the given studyId.
     */
    Set<FlexibleConfigDto> fetchReadingConfigs(long studyId);

    /**
     * Fetches the reading configs for the give studyId.
     *
     * @return The all flexible configs for the given studyId.
     */
    Set<FlexibleConfigDto> fetchReadingConfigs(long studyId, String endpointName);

    SiteConfigDto fetchSiteConfig(long studyId);

    Set<PatientDto> fetchPatientsByIds(Set<Long> patientIds);

    PatientDto fetchPatientById(long patientId);
}
