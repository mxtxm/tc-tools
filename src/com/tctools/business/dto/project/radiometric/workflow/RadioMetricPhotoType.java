package com.tctools.business.dto.project.radiometric.workflow;


public enum RadioMetricPhotoType {

    TowerView,
    ProbeViewInFrontOfMeasuredSector,
    TripodInLocation,
    Address,
    Extra1,
    Extra2;

    public String getFilename() {
        switch (name()) {
            case "TowerView":
                return "Tower View";
            case "ProbeViewInFrontOfMeasuredSector":
                return "Probe View in Front of Measured Sector";
            case "TripodInLocation":
                return "Tripod in Location";
        }
        return name();
    }
}
