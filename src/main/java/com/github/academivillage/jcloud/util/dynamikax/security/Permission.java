package com.github.academivillage.jcloud.util.dynamikax.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static com.github.academivillage.jcloud.util.dynamikax.security.VeryCompactJwtPermissions.ALPHABET_ENCODER;
import static java.util.stream.Collectors.toMap;

@Getter
@RequiredArgsConstructor
public enum Permission {
    CONFIGURATION_STUDY_DELETE("configuration.study.delete", 0),
    CONFIGURATION_STUDY_EDIT("configuration.study.edit", 1),
    CONFIGURATION_STUDY_CREATE("configuration.study.create", 2),
    CONFIGURATION_SITE_CREATE("configuration.site.create", 3),
    CONFIGURATION_SITE_DELETE("configuration.site.delete", 4),
    CONFIGURATION_SITE_EDIT("configuration.site.edit", 5),
    CONFIGURATION_USER_DEACTIVATE("configuration.user.deactivate", 6),
    CONFIGURATION_USER_DELETE("configuration.user.delete", 7),
    CONFIGURATION_IMAGING_DELETE("configuration.imaging.delete", 8),
    CONFIGURATION_READING_CREATE("configuration.reading.create", 9),
    CONFIGURATION_IMAGING_CREATE("configuration.imaging.create", 10),
    CONFIGURATION_IMAGING_EDIT("configuration.imaging.edit", 11),
    CONFIGURATION_READING_EDIT("configuration.reading.edit", 12),
    CONFIGURATION_READING_DELETE("configuration.reading.delete", 13),
    UPLOAD_VIEW_ALL_SITE_DATA("upload.view.all.site.data", 14),
    UPLOAD_DATA("upload.data", 15),
    UPLOAD_CREATE_OR_UPLOAD_EDTF("upload.create.or.upload.eDTF", 16),
    UPLOAD_CREATE_PATIENT("upload.create.patient", 17),
    QC_VIEW_ALL_UPLOADED_DATA("qc.view.all.uploaded.data", 18),
    QC_SELECT_UPLOADED_VISIT("qc.select.uploaded.visit", 19),
    UPLOAD_VIEW_EDTF("upload.view.eDTF", 20),
    QC_VIEW_RESULTS("qc.view.results", 21),
    READER_CENTRAL("reader.central", 22),
    CONFIGURATION_SPONSOR_CREATE("configuration.sponsor.create", 23),
    CONFIGURATION_SPONSOR_EDIT("configuration.sponsor.edit", 24),
    CONFIGURATION_SPONSOR_DELETE("configuration.sponsor.delete", 25),
    CONFIGURATION_USER_EDIT("configuration.user.edit", 26),
    CONFIGURATION_USER_CREATE("configuration.user.create", 27),
    CONFIGURATION_SCANNER_CREATE("configuration.scanner.create", 28),
    CONFIGURATION_SCANNER_EDIT("configuration.scanner.edit", 29),
    CONFIGURATION_SCANNER_DELETE("configuration.scanner.delete", 30),
    QUERIES_UPDATE_EDCF("queries.update.eDCF", 31),
    QUERIES_VIEW_EDCF("queries.view.eDCF", 32),
    QUERIES_RESOLVE_EDCF("queries.resolve.eDCF", 33),
    QUERIES_CREATE_EDCF("queries.create.eDCF", 34),
    CONFIGURATION_UPLOAD_VIEW_ALL_SITE_DATA("configuration.upload.view.all.site.data", 35),
    UPLOAD_PATIENT_DISCONTINUED("upload.patient.discontinued", 36),
    READER_DX_KL("reader.dx.kl", 37),
    READER_MRI_IF("reader.mri.if", 38),
    READER_WB_MRI_IF("reader.wb-mri.if", 39),
    READER_DCE_MRI_IF("reader.dce-mri.if", 40),
    READER_DX_IF("reader.dx.if", 41),
    CONFIGURATION_UPLOAD_EDIT("configuration.upload.edit", 42),
    CONFIGURATION_STUDY_OVERALL_VIEWER("configuration.study.overall.viewer", 43),
    QC_CREATE_EDCF("qc.create.eDCF", 44),
    DASHBOARD_LIST_BATCHES("dashboard.list.batches", 45),
    DASHBOARD_READ_DETAILS_BATCH("dashboard.read-details.batch", 46),
    dashboard_read_DETAILS_READING("dashboard.read-details.reading", 47),
    DASHBOARD_CREATE_EDIT_READING("dashboard.create-edit.reading", 48),
    DASHBOARD_CREATE_EDIT_BATCH("dashboard.create-edit.batch", 49),
    DASHBOARD_LIST_PROJECTS("dashboard.list.projects", 50),
    DASHBOARD_ACCESS("dashboard.access", 51),
    DASHBOARD_ACCESS_SPONSOR("dashboard.access.sponsor", 52),
    CLINICAL_TRIAL_ACCESS("clinicaltrial.access", 53),
    AUDIT_TRAILS_ACCESS("audittrails.access", 54),
    DASHBOARD_LIST_READINGS("dashboard.list.readings", 55),

    /* Webinar Permissions */
    @Deprecated
    WEBINAR_ADD_PROJECT("webinar.add.project", 10_000),
    @Deprecated
    WEBINAR_PROJECT_CREATE("webinar.project.create", 10_001),
    @Deprecated
    WEBINAR_PROJECT_LIST("webinar.project.list", 10_002),
    @Deprecated
    WEBINAR_PROJECT_DELETE("webinar.project.delete", 10_003),
    @Deprecated
    WEBINAR_PROJECT_EDIT("webinar.project.edit", 10_004),
    @Deprecated
    WEBINAR_QUESTION_LIST("webinar.question.list", 10_005),
    @Deprecated
    WEBINAR_QUESTION_GET("webinar.question.get", 10_006),
    @Deprecated
    WEBINAR_QUESTION_CREATE("webinar.question.create", 10_007),
    @Deprecated
    WEBINAR_QUESTION_EDIT("webinar.question.edit", 10_008),
    @Deprecated
    WEBINAR_QUESTION_DELETE("webinar.question.delete", 10_009),
    @Deprecated
    WEBINAR_ANSWER_LIST("webinar.answer.list", 10_010),
    @Deprecated
    WEBINAR_ANSWER_GET("webinar.answer.get", 10_011),
    @Deprecated
    WEBINAR_ANSWER_CREATE("webinar.answer.create", 10_012),
    @Deprecated
    WEBINAR_ANSWER_EDIT("webinar.answer.edit", 10_013),
    @Deprecated
    WEBINAR_ANSWER_DELETE("webinar.answer.delete", 10_014),
    @Deprecated
    WEBINAR_QUESTION_ADMIN("webinar.question.admin", 10_015),
    @Deprecated
    WEBINAR_QUESTION_ATTACH("webinar.question.attach", 10_016),
    @Deprecated
    WEBINAR_DASHBOARD_GET("webinar.dashboard.get", 10_017),
    @Deprecated
    WEBINAR_REPORT_CREATE("webinar.report.create", 10_018),
    @Deprecated
    WEBINAR_REPORT_GET("webinar.report.get", 10_019),
    @Deprecated
    WEBINAR_DESCRIPTION_CREATE("webinar.description.create", 10_020),
    @Deprecated
    WEBINAR_DESCRIPTION_DELETE("webinar.description.delete", 10_021),
    @Deprecated
    WEBINAR_DESCRIPTION_GET("webinar.description.get", 10_022),
    @Deprecated
    WEBINAR_DESCRIPTION_EDIT("webinar.description.edit", 10_023),
    ;

    private static final Map<String, Permission> codeToPermissionMap = Arrays.stream(Permission.values())
            .collect(toMap(it -> ALPHABET_ENCODER.encode(it.code), it -> it));

    private static final Map<String, Permission> nameToPermissionMap = Arrays.stream(Permission.values())
            .collect(toMap(it -> it.name, it -> it));

    private final String name;
    private final int    code;

    /**
     * Fetches the associated permission of the given {@code encodedCode}.
     *
     * @param encodedCode The encoded value of a permission encodedCode. Example: {@code N5}
     * @return The optional associated permission of the given {@code encodedCode}.
     */
    public static Optional<Permission> ofCode(String encodedCode) {
        return Optional.ofNullable(codeToPermissionMap.get(encodedCode));
    }

    /**
     * Fetches the associated permission of the given name.
     *
     * @param name The name of a permission encodedCode. Example: {@code dashboard.list.batches}
     * @return The optional associated permission of the given name.
     */
    public static Optional<Permission> ofName(String name) {
        return Optional.ofNullable(nameToPermissionMap.get(name));
    }
}
