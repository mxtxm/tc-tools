package com.tctools.business.dto.site;

import com.tctools.business.dto.location.LocationType;
import com.vantar.database.dto.*;

@NoStore
public class Sector extends DtoBase {

    public Boolean selected;
    public String title;

    public Integer antennaCount;
    public Long locationTypeId;
    public Long sectorOptimizationId;

    public String antennaType;
    public Integer azimuth;
    public Double height;
    public Double onSiteHeight;
    public Integer mechanicalTilt;
    public Integer electricalTilt;
    public Boolean isOmni;
    public Boolean isDirectional;


    public Sector() {

    }

    public Sector(String title) {
        this.title = title;
        selected = false;
        isDirectional = false;
        isOmni = false;
    }

    public boolean isEmpty() {
        return !((azimuth != null && height != null) ||
            ((isOmni != null && isOmni) || (isDirectional != null && isDirectional)));
    }


    public void empty() {
        selected = false;
        antennaCount = null;
        locationTypeId = null;
        sectorOptimizationId = null;
        antennaType = null;
        azimuth = null;
        height = null;
        onSiteHeight = null;
        mechanicalTilt = null;
        electricalTilt = null;
    }


    public static class Viewable extends DtoBase {

        public Boolean selected;
        public String title;

        public Integer antennaCount;
        @FetchCache("com.tctools.business.dto.location.LocationType:locationTypeId")
        public LocationType.Localed locationType;
        @FetchCache("sectorOptimizationId")
        public SectorOptimization sectorOptimization;

        public String antennaType;
        public Integer azimuth;
        public Double height;
        public Double onSiteHeight;
        public Integer mechanicalTilt;
        public Integer electricalTilt;


        public boolean isEmpty() {
            return azimuth == null || title == null || height == null;
        }
    }
}
