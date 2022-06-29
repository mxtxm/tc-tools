package com.tctools.business.dto.project.radiometric.workflow;


public enum RadioMetricProximityType {

    HealthFacility,
    EducationalInstitution,
    OperatorSite;

    public String getFilename() {
        return name().replaceAll("(.)([A-Z])", "$1 $2");
    }
}
