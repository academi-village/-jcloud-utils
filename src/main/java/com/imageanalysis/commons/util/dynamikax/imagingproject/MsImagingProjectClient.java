package com.imageanalysis.commons.util.dynamikax.imagingproject;

import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.FlexibleConfigPayload;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.PatientPayload;
import com.imageanalysis.commons.util.dynamikax.imagingproject.dto.VisitConfigPayload;
import lombok.NonNull;
import org.springframework.core.env.Environment;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MsImagingProjectClient {

    Map<Long, Instant> fetchVisitConfigsScanDate(@NonNull List<Long> visitConfigIds);

    VisitConfigPayload fetchVisitConfig(long visitConfigId);

    /**
     * Fetches the reading configs for the give studyId. The endpoint name would be fetched from {@link Environment}.
     * The required property name is {@code app.endpoint.name}.
     *
     * @return The all flexible configs for the given studyId.
     */
    List<FlexibleConfigPayload> fetchReadingConfigs(long studyId);

    /**
     * Fetches the reading configs for the give studyId.
     *
     * @return The all flexible configs for the given studyId.
     */
    List<FlexibleConfigPayload> fetchReadingConfigs(long studyId, @NonNull String endpointName);

    List<PatientPayload> fetchPatientsByIds(Set<Long> patientIds);
}
