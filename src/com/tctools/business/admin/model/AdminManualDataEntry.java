package com.tctools.business.admin.model;


import com.tctools.business.dto.project.radiometric.workflow.Proximity;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlowState;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricRadiationStatus;
import com.tctools.business.dto.site.Collocation;
import com.tctools.business.dto.site.CollocationType;
import com.tctools.business.dto.site.Sector;
import com.tctools.business.dto.user.User;
import com.vantar.admin.index.Admin;
import com.vantar.database.datatype.Location;
import com.vantar.database.dto.NoList;
import com.vantar.exception.FinishException;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.Params;
import com.vantar.web.WebUi;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AdminManualDataEntry {

    public static void index(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi("Tools new ", params, response, false);

        Map<Long, String> users = new HashMap<>();
        for (User u : ServiceDtoCache.asList(User.class)) {
            users.put(u.id, u.fullName);
        }

        ui
            .addSelect("Type", "storeType", new String[] {"New radiometric", "Update existing"}, "New radiometric")

            .addEmptyLine()
            .addInput("Site code", "siteCode")

            .addEmptyLine()
            .addSelect("State", "state", EnumUtil.getEnumValues(RadioMetricFlowState.values()))
            .addSelect("Assignee", "assignee", users)
            .addSelect("Assignor", "assignor", users)
            .addInput("Assign date", "assignDateTime")
            .addInput("Measurement date", "measurementDateTime")

            .addEmptyLine()
            .addInput("Site address", "siteAddress")
            .addInput("Site location", "siteLocation")
            .addInput("Spot address", "spotAddress")
            .addInput("Spot location", "spotLocation")

        // proximity > > >
        @NoList
        public List<Proximity> proximities;
        // < < < proximity

        // sectors > > >
        @NoList
        public List<Sector> sectors;
        // < < < sectors

        // measurement device > > >
        @NoList
        public String deviceTitle;
        @NoList
        public String deviceManufacturer;
        @NoList
        public String deviceModel;
        @NoList
        public String deviceSerialNumber;
        @NoList
        public String deviceProbe;
        @NoList
        public DateTime deviceCalibrationExpire;
        // < < < measurement device

        // measurement > > >
        @NoList
        public Integer horizontalDistanceFromLocation;
        @NoList
        public Integer horizontalDistanceFromLocationEstimate;
        @NoList
        public Double measurementHeight;
        @NoList
        public String measurementType;

        // checking
        @NoList
        public String validationMessage;
        @NoList
        public String committableMessage;
        @NoList
        public Boolean isCommittable;

        @NoList
        public Boolean isMeasurementRecordCountAcceptableX;
        @NoList
        public Boolean isMeasurementGpsDataAvailableX;
        @NoList
        public Boolean isMeasurementTimeAcceptableX;

        @NoList
        public Boolean isMeasurementRecordCountAcceptable100;
        @NoList
        public Boolean isMeasurementGpsDataAvailable100;
        @NoList
        public Boolean isMeasurementTimeAcceptable100;

        @NoList
        public Boolean isMeasurementRecordCountAcceptable150;
        @NoList
        public Boolean isMeasurementGpsDataAvailable150;
        @NoList
        public Boolean isMeasurementTimeAcceptable150;

        @NoList
        public Boolean isMeasurementRecordCountAcceptable170;
        @NoList
        public Boolean isMeasurementGpsDataAvailable170;
        @NoList
        public Boolean isMeasurementTimeAcceptable170;

        @NoList
        public Boolean isMwCm2X;
        @NoList
        public Boolean isMwCm2100;
        @NoList
        public Boolean isMwCm2150;
        @NoList
        public Boolean isMwCm2170;

        // Density min (µw/cm2)
        @NoList
        public Double densityMinX;
        // Density max(µw/cm2)
        @NoList
        public Double densityMaxX;
        @NoList
        public Double densityAverage6minX;
        @NoList
        public Double densityAverageDevice6minX;
        // Min level of radiation(µw/cm2)
        @NoList
        public Double minRadiationLevelX;
        // Density average - 6 min(µw/cm2)
        @NoList
        public Double densityAverageDivMinRadiationX;
        @NoList
        public DateTime logDateTimeX;
        @NoList
        public RadioMetricRadiationStatus radiationStatusX;
        @NoList
        public Double icnirpPercentX;


        // Density min (µw/cm2)
        public Double densityMin100;
        // Density max(µw/cm2)
        public Double densityMax100;
        public Double densityAverage6min100;
        public Double densityAverageDevice6min100;
        // Min level of radiation(µw/cm2)
        public Double minRadiationLevel100;
        // Density average - 6 min(µw/cm2)
        public Double densityAverageDivMinRadiation100;
        public DateTime logDateTime100;
        public RadioMetricRadiationStatus radiationStatus100;
        public Double icnirpPercent100;

        public Double densityMin150;
        public Double densityMax150;
        public Double densityAverage6min150;
        public Double densityAverageDevice6min150;
        public Double minRadiationLevel150;
        public Double densityAverageDivMinRadiation150;
        public DateTime logDateTime150;
        public RadioMetricRadiationStatus radiationStatus150;
        public Double icnirpPercent150;

        public Double densityMin170;
        public Double densityMax170;
        public Double densityAverage6min170;
        public Double densityAverageDevice6min170;
        public Double minRadiationLevel170;
        public Double densityAverageDivMinRadiation170;
        public DateTime logDateTime170;
        public RadioMetricRadiationStatus radiationStatus170;
        public Double icnirpPercent170;
        // < < < measurement

        // observation > > >
        public CollocationType collocationType;
        public Set<Collocation> collocations;

        public Integer siteFloor;

        public String reportedProvince;
        public String reportedCity;
        // < < < observation

        public String comments;

        // measurement urls > > >
        public Map<String, String> imageUrls;
        public String measurementUrlX;
        public String measurementUrl100;
        public String measurementUrl150;
        public String measurementUrl170;
        // < < < measurement URLS

        // > > >
        public String reporter;
        public String healthPhysics;



        ui.finish();
    }

}