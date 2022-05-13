package com.imageanalysis.commons.util.dynamikax;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Encapsulates the micrso-service specific information.
 *
 * @author Younes Rahimi
 */
@Getter
@RequiredArgsConstructor
public enum Microservice {
    GLOBAL_STORAGE("global-storage"),
    MS_AUDIT_TRAILS("msaudittrails"),
    MS_BATCH_LOGIC("msbatchlogic"),
    MS_COMMANDS("mscommands"),
    MS_DASHBOARD_STAT("msdashboardstat"),
    MS_IMAGE_ANALYSIS("msimageanalysis"),
    MS_IMAGING_PROJECT("msimagingproject"),
    MS_NOTIFICATIONS("msnotifications"),
    MS_QUALITY_CONTROL("msqualitycontrol"),
    MS_QUERIES("msqueries"),
    MS_READING_CANDEN_SPARCC("msreadingcandensparcc"),
    MS_READING_DEMRIQ("msreadingdemriq"),
    MS_READING_HEEL("msreadingheel"),
    MS_READING_IF("msreadingif"),
    MS_READING_K_AND_L("msreadingkandl"),
    MS_READING_K_AND_L_AUTO("msreadingkandlauto"),
    MS_READING_MRANO("msreadingmrano"),
    MS_READING_NOVADIP5("msreadingnovadip5"),
    MS_READING_PSAMRIS("msreadingpsamris"),
    MS_READING_PSMAPET("msreadingpsmapet"),
    MS_READING_WBMRI("msreadingwbmri"),
    MS_REPORT_GENERATOR("msreportgenerator"),
    MS_UPLOAD("msupload"),
    MS_USER("msuser"),
    MS_READING_JSW("msreadingjsw"),
    MS_Reading_OARSI("msreadingoarsi"),

    /* Not App Engine Microservices:
    MS_DASHBOARD,
    MS_GBM_COMPUTE,
    MS_IMAGE_ANALYSIS_GBM,
    MS_READING_LUGANO ,
    MS_READING_MOAKS,
    MS_READING_RECIST,
     */;

    /**
     * Represents the microservice name on App Engine.
     */
    private final String msName;

    public String getAppEngineBaseUrl(Profile profile) {
        return profile.getAppEngineBaseUrl(this);
    }
}
