package com.tctools.business.dto.project.radiometric.workflow;


public enum RadioMetricPhotoType {

    TowerView,
    ProbeViewInFrontOfMeasuredSector,
    TripodInLocation,
    Address,
    Extra1,
    Extra2,

    Drone,
    TargetBuildingFromTower,
    TargetBuilding,
    FlightControlScreen,
    TowerTopView,
    ;

    public String getFilename() {
        switch (name()) {
            case "TowerView":
                return "Tower View";
            case "ProbeViewInFrontOfMeasuredSector":
                return "Probe View in Front of Measured Sector";
            case "TripodInLocation":
                return "Tripod in Location";

            case "TargetBuildingFromTower":
                return "Target building from tower";
            case "TargetBuilding":
                return "Target building";
            case "FlightControlScreen":
                return "Flight control screen";
            case "TowerTopView":
                return "Tower top view";
        }
        return name();
    }
}
