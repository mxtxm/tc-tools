package com.tctools.business.dto.project.radiometric.workflow;

import com.tctools.business.dto.location.*;
import com.tctools.business.dto.project.radiometric.complain.*;
import com.tctools.business.dto.site.*;
import com.tctools.business.dto.user.User;
import com.tctools.common.Param;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.database.datatype.Location;
import com.vantar.database.dto.*;
import com.vantar.exception.NoContentException;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.service.log.ServiceLog;
import com.vantar.util.bool.BoolUtil;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.util.string.StringUtil;
import java.util.*;

@Mongo
@Index({"code:1", "site.code:1", "site.id:1", "complain.id:1", "assignorId:1", "assigneeId:1", "lastState:1", "isCc:1",
    "lastStateDateTime:1", "assignDateTime:1", "measurementDateTime:1", "provinceId:1", "cityId:1", "site.location:2dsphere"})
public class RadioMetricFlow extends DtoBase {

    public Long id;

    @Default("false")
    public Boolean reRadioMetric;

    @Default("false")
    public Boolean isCc;

    @Default("false")
    public Boolean isDrone;

    @NoList
    @Required
    public Site site;

    @NoList
    public RadioMetricComplain complain;

    // assign > > >
    @Default("true")
    public Boolean assignable;
    //@Required
    //@Depends(User.class)
    public Long assignorId;
    //@Required
    //@Depends(User.class)
    public Long assigneeId;
    @NoList
    public List<State> state;
    public RadioMetricFlowState lastState;
    @Timestamp
    public DateTime lastStateDateTime;
    @Timestamp
    public DateTime assignDateTime;
    @Timestamp
    public DateTime measurementDateTime;
    // < < < assign

    // location > > >
    @Required
    //@Depends(Province.class)
    public Long provinceId;
    @Required
    //@Depends(City.class)
    public Long cityId;

    // default=Site.address technician can change after measurement
    @NoList
    public String siteAddress;
    public String siteAddressOld;
    // default=Site.location technician can change after measurement
    public Location siteLocation;
    // default=null technician must enter after measurement
    @NoList
    public String spotAddress;
    public String spotAddressOld;
    // default=null system must enter after measurement
    public Location spotLocation;
    // < < < location

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
    @NoList
    public Double densityMin100;
    // Density max(µw/cm2)
    @NoList
    public Double densityMax100;
    @NoList
    public Double densityAverage6min100;
    @NoList
    public Double densityAverageDevice6min100;
    // Min level of radiation(µw/cm2)
    @NoList
    public Double minRadiationLevel100;
    // Density average - 6 min(µw/cm2)
    @NoList
    public Double densityAverageDivMinRadiation100;
    @NoList
    public DateTime logDateTime100;
    @NoList
    public RadioMetricRadiationStatus radiationStatus100;
    @NoList
    public Double icnirpPercent100;

    @NoList
    public Double densityMin150;
    @NoList
    public Double densityMax150;
    @NoList
    public Double densityAverage6min150;
    @NoList
    public Double densityAverageDevice6min150;
    @NoList
    public Double minRadiationLevel150;
    @NoList
    public Double densityAverageDivMinRadiation150;
    @NoList
    public DateTime logDateTime150;
    @NoList
    public RadioMetricRadiationStatus radiationStatus150;
    @NoList
    public Double icnirpPercent150;

    @NoList
    public Double densityMin170;
    @NoList
    public Double densityMax170;
    @NoList
    public Double densityAverage6min170;
    @NoList
    public Double densityAverageDevice6min170;
    @NoList
    public Double minRadiationLevel170;
    @NoList
    public Double densityAverageDivMinRadiation170;
    @NoList
    public DateTime logDateTime170;
    @NoList
    public RadioMetricRadiationStatus radiationStatus170;
    @NoList
    public Double icnirpPercent170;
    // < < < measurement

    // observation > > >
    @NoList
    public CollocationType collocationType;
    @NoList
    public Set<Collocation> collocations;

    @NoList
    public Integer siteFloor;

    @NoList
    public String reportedProvince;
    @NoList
    public String reportedCity;
    // < < < observation

    public String comments;

    // measurement urls > > >
    @NoList
    public Map<String, String> imageUrls;
    @NoList
    public String measurementUrlX;
    @NoList
    public String measurementUrl100;
    @NoList
    public String measurementUrl150;
    @NoList
    public String measurementUrl170;
    // < < < measurement URLS

    // > > >
    @NoList
    public String reporter;
    @NoList
    public String healthPhysics;
    // < < <

    public List<String> patch;


    private boolean skipBeforeUpdate = false;


    public static String getPath(String siteCode, Long id) {
        return Param.RADIO_METRIC_FILES + siteCode + "/measurement/" + id + "/";
    }

    public String getPath() {
        return getPath(site.code, id);
    }

    public static boolean isAssignable(RadioMetricFlowState state, Long btsStatusId) {
        boolean isInService = btsStatusId != null && (btsStatusId == 3 || btsStatusId == 5 || btsStatusId == 6);

        boolean isStateAssignable =
            state == RadioMetricFlowState.Pending
                || state == RadioMetricFlowState.Problematic
                || state == RadioMetricFlowState.Returned
                || state == RadioMetricFlowState.Revise
                || state == RadioMetricFlowState.Terminated;

        return isInService && isStateAssignable;
    }

    @Override
    public boolean beforeInsert() {
        return beforeUpdate();
    }

    @Override
    public boolean beforeUpdate() {
        spotAddress = Site.normaliseAddress(
            spotAddress,
            null,
            null
        );
        siteAddress = Site.normaliseAddress(
            siteAddress,
            ServiceDtoCache.asDto(Province.class, provinceId).name.get("fa"),
            ServiceDtoCache.asDto(City.class, cityId).name.get("fa")
        );

        if (site != null) {
            site.addressOld = site.address;
            site.address = Site.normaliseAddress(
                site.address,
                ServiceDtoCache.asDto(Province.class, provinceId).name.get("fa"),
                ServiceDtoCache.asDto(City.class, cityId).name.get("fa")
            );
        }

        if (complain != null && complain.id != null) {
            isCc = ComplainType.CustomerComplain.equals(complain.type);
        } else {
            complain = null;
            isCc = false;
            addNullProperties("complain");
        }

        if (complain != null) {
            try {
                RadioMetricComplain complainX = new RadioMetricComplain();
                complainX.id = complain.id;
                complainX = Db.modelMongo.getById(complainX);
                complainX.lastState = lastState;
                complain.provinceId = complainX.provinceId;
                complain.cityId = complainX.cityId;
                Db.modelMongo.update(new ModelCommon.Settings(complainX).dtoHasFullData(true).logEvent(false).mutex(false));

                if (complain.provinceId == null || complain.cityId == null) {
                    ServiceLog.log.error("! bad complain province/city {}", id);
                } else {
                    complain.address = Site.normaliseAddress(
                        complain.address,
                        ServiceDtoCache.asDto(Province.class, complain.provinceId).name.get("fa"),
                        ServiceDtoCache.asDto(City.class, complain.cityId).name.get("fa")
                    );
                    complain.complainerAddress = Site.normaliseAddress(
                        complain.complainerAddress,
                        ServiceDtoCache.asDto(Province.class, complain.provinceId).name.get("fa"),
                        ServiceDtoCache.asDto(City.class, complain.cityId).name.get("fa")
                    );
                }
            } catch (NoContentException e) {
                complain = null;
                isCc = false;
                addNullProperties("complain");
                ServiceLog.log.error("! no complain {}", id);
            } catch (Exception e) {
                ServiceLog.log.error("! {}", id, e);
            }
        }

        if (skipBeforeUpdate) {
            return true;
        }

        isDrone = BoolUtil.isTrue(isDrone);

        measurementDateTime = isDrone ? logDateTimeX : logDateTime100;
        assignable = isAssignable(lastState, site == null ? null : site.btsStatusId);

        if (site != null) {
            imageUrls = getImagePaths(site.code, id, true, true);
            if (proximities != null) {
                for (Proximity p : proximities) {
                    p.setImages(site.code, id);
                }
            }

            if (isDrone) {
                measurementUrlX = getMeasurementPath(site.code, id, "X", true, true);
            } else {
                measurementUrl100 = getMeasurementPath(site.code, id, "100", true, true);
                measurementUrl150 = getMeasurementPath(site.code, id, "150", true, true);
                measurementUrl170 = getMeasurementPath(site.code, id, "170", true, true);
            }
        }


        if (siteLocation != null && spotLocation != null) {
            horizontalDistanceFromLocation = spotLocation.getDistanceM(siteLocation).intValue();
        }

        if (RadioMetricFlowState.Planned.equals(lastState)) {
            reportedCity = Services.get(ServiceDtoCache.class).getDto(City.class, site.cityId).name.get("fa");
            reportedProvince = Services.get(ServiceDtoCache.class).getDto(Province.class, site.provinceId).name.get("fa");

            StringBuilder msg = new StringBuilder();
            if (isDrone) {
                if (densityAverage6minX == null) {
                    msg.append("Xcm logfile not uploaded\n");
                }
            } else {
                if (densityAverage6min100 == null) {
                    msg.append("100cm logfile not uploaded\n");
                }
                if (densityAverage6min150 == null) {
                    msg.append("150cm logfile not uploaded\n");
                }
                if (densityAverage6min170 == null) {
                    msg.append("170cm logfile not uploaded\n");
                }
            }
            if (horizontalDistanceFromLocation == null) {
                msg.append("horizontal distance from location is missing\n");
            }
            if (measurementHeight == null) {
                msg.append("measurement height is missing\n");
            }

            // > > > sectors
            Set<String> availableSectors = new HashSet<>(5);
            if (sectors == null) {
                sectors = new ArrayList<>(8);
            } else {
                for (Sector s : sectors) {
                    availableSectors.add(s.title);
                }
            }
            boolean isSelected = false;
            for (Sector s : sectors) {
                if (s.isEmpty()) {
                    continue;
                }
                if (s.onSiteHeight == null) {
                    msg.append("sector ").append(s.title).append(" on site height is missing\n");
                }
                if (s.selected != null && s.selected) {
                    isSelected = true;
                }
            }
            if (!isSelected) {
                msg.append("measured sector is not selected\n");
            }
            if (!availableSectors.contains("A")) {
                sectors.add(new Sector("A"));
            }
            if (!availableSectors.contains("A2")) {
                sectors.add(new Sector("A2"));
            }
            if (!availableSectors.contains("B")) {
                sectors.add(new Sector("B"));
            }
            if (!availableSectors.contains("B2")) {
                sectors.add(new Sector("B2"));
            }
            if (!availableSectors.contains("C")) {
                sectors.add(new Sector("C"));
            }
            if (!availableSectors.contains("C2")) {
                sectors.add(new Sector("C2"));
            }
            if (!availableSectors.contains("D")) {
                sectors.add(new Sector("D"));
            }
            if (!availableSectors.contains("D2")) {
                sectors.add(new Sector("D2"));
            }
            // < < < sectors

            // > > > proximities
            Set<RadioMetricProximityType> availableProximities = new HashSet<>(5);
            if (proximities == null) {
                proximities = new ArrayList<>(5);
            } else {
                for (Proximity p : proximities) {
                    availableProximities.add(p.proximityType);
                }
            }
            for (RadioMetricProximityType t : RadioMetricProximityType.values()) {
                if (availableProximities.contains(t)) {
                    continue;
                }
                Proximity p = new Proximity();
                p.proximityType = t;
                proximities.add(p);
            }
            // < < < proximities

            if (site == null) {
                return true;
            }
            if (getImagePath(RadioMetricPhotoType.TowerView, site.code, id, false, true) == null) {
                msg.append("TowerView photo is not uploaded\n");
            }
            if (getImagePath(RadioMetricPhotoType.ProbeViewInFrontOfMeasuredSector, site.code, id, false, true) == null) {
                msg.append("ProbeViewInFrontOfMeasuredSector photo is not uploaded\n");
            }
            if (getImagePath(RadioMetricPhotoType.TripodInLocation, site.code, id, false, true) == null) {
                msg.append("TripodInLocation photo is not uploaded\n");
            }
            if (getImagePath(RadioMetricPhotoType.Address, site.code, id, false, true) == null) {
                msg.append("Address photo is not uploaded\n");
            }

            if (msg.length() > 0) {
                committableMessage = msg.toString();
            }
            isCommittable = committableMessage == null;
        } else {
            isCommittable = false;
        }

        return true;
    }

    public static Map<String, String> getImagePaths(String siteCode, long flowId, boolean isUrl, boolean exist) {
        Map<String, String> paths = new HashMap<>(RadioMetricPhotoType.values().length);
        for (RadioMetricPhotoType type : RadioMetricPhotoType.values()) {
            String u = getImagePath(type, siteCode, flowId, isUrl, exist);
            if (u != null) {
                paths.put(StringUtil.toKababCase(type.toString()), u);
            }
        }
        return paths;
    }

    public static String getImagePath(RadioMetricPhotoType type, String siteCode, long flowId, boolean isUrl, boolean exist) {
        String path = siteCode + "/measurement/" + flowId + "/";
        String filename = siteCode + "__" + type.getFilename() + ".jpg";
        String filepath = Param.RADIO_METRIC_FILES + path + filename;
        if (exist && !FileUtil.exists(filepath)) {
            return null;
        }
        return isUrl ? (Param.RADIO_METRIC_URL + path + filename) : filepath;
    }

    public static String getMeasurementPath(String siteCode, long flowId, String height, boolean isUrl, boolean exist) {
        String path = siteCode + "/measurement/" + flowId + "/";
        String filename = siteCode + "__" + height + "CM.csv";
        String filepath = Param.RADIO_METRIC_FILES + path + filename;
        if (exist && !FileUtil.exists(filepath)) {
            return null;
        }
        return isUrl ? (Param.RADIO_METRIC_URL + path + filename) : filepath;
    }

    public void skipBeforeUpdate() {
        skipBeforeUpdate = true;
    }

    public void afterFetchData() {
        if (site == null) {
            return;
        }
        if (siteLocation != null) {
            siteLocation.round(Param.LOCATION_DECIMALS);
        }
        if (spotLocation != null) {
            spotLocation.round(Param.LOCATION_DECIMALS);
        }
        if (site.location != null) {
            site.location.round(Param.LOCATION_DECIMALS);
        }
    }

    // > > > Viewable



    @Storage("RadioMetricFlow")
    public static class Viewable extends DtoBase {

        public Long id;

        public Boolean reRadioMetric;

        public Boolean isCc;
        public Boolean isDrone;

        public Site.Viewable site;
        public RadioMetricComplain.Viewable complain;

        // assign > > >
        public Boolean assignable;
        @FetchCache("assignorId")
        public User assignor;
        @FetchCache("assigneeId")
        public User assignee;
        public List<State> state;
        public RadioMetricFlowState lastState;
        public DateTime lastStateDateTime;
        public DateTime assignDateTime;
        public DateTime measurementDateTime;

        public String lastStateDateTimeFa;
        public String assignDateTimeFa;
        public String measurementDateTimeFa;
        // < < < assign

        // location > > >
        @FetchCache(dto = Province.class, field = "provinceId")
        public Province.Localed province;
        @FetchCache(dto = City.class, field = "cityId")
        public City.Localed city;

        public String siteAddress;
        public Location siteLocation;

        public String spotAddress;
        public Location spotLocation;
        // < < < location

        // proximity > > >
        public List<Proximity> proximities;
        // < < < proximity

        // sectors > > >
        public List<Sector.Viewable> sectors;
        // < < < sectors

        // measurement device > > >
        public String deviceTitle;
        public String deviceManufacturer;
        public String deviceModel;
        public String deviceSerialNumber;
        public String deviceProbe;
        public DateTime deviceCalibrationExpire;
        // < < < measurement device

        // measurement > > >
        public Integer horizontalDistanceFromLocation;
        public Integer horizontalDistanceFromLocationEstimate;
        public Double measurementHeight;
        public String measurementType;

        // checking
        public String committableMessage;
        public Boolean isCommittable;

        public Boolean isMeasurementRecordCountAcceptableX;
        public Boolean isMeasurementGpsDataAvailableX;
        public Boolean isMeasurementTimeAcceptableX;

        public Boolean isMeasurementRecordCountAcceptable100;
        public Boolean isMeasurementGpsDataAvailable100;
        public Boolean isMeasurementTimeAcceptable100;

        public Boolean isMeasurementRecordCountAcceptable150;
        public Boolean isMeasurementGpsDataAvailable150;
        public Boolean isMeasurementTimeAcceptable150;

        public Boolean isMeasurementRecordCountAcceptable170;
        public Boolean isMeasurementGpsDataAvailable170;
        public Boolean isMeasurementTimeAcceptable170;

        public Double densityMinX;
        public Double densityMaxX;
        public Double densityAverage6minX;
        public Double densityAverageDevice6minX;
        public Double minRadiationLevelX;
        public Double densityAverageDivMinRadiationX;
        public DateTime logDateTimeX;
        public RadioMetricRadiationStatus radiationStatusX;

        public Double densityMin100;
        public Double densityMax100;
        public Double densityAverage6min100;
        public Double densityAverageDevice6min100;
        public Double minRadiationLevel100;
        public Double densityAverageDivMinRadiation100;
        public DateTime logDateTime100;
        public RadioMetricRadiationStatus radiationStatus100;

        public Double densityMin150;
        public Double densityMax150;
        public Double densityAverage6min150;
        public Double densityAverageDevice6min150;
        public Double minRadiationLevel150;
        public Double densityAverageDivMinRadiation150;
        public DateTime logDateTime150;
        public RadioMetricRadiationStatus radiationStatus150;

        public Double densityMin170;
        public Double densityMax170;
        public Double densityAverage6min170;
        public Double densityAverageDevice6min170;
        public Double minRadiationLevel170;
        public Double densityAverageDivMinRadiation170;
        public DateTime logDateTime170;
        public RadioMetricRadiationStatus radiationStatus170;

        public Boolean isMwCm2X;
        public Boolean isMwCm2100;
        public Boolean isMwCm2150;
        public Boolean isMwCm2170;

        // < < < measurement

        // observation > > >
        public CollocationType collocationType;
        public List<Collocation.Viewable> collocations;

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
        // < < <


        public String getPath() {
            return RadioMetricFlow.getPath(site.code, id);
        }

        @Override
        public void afterFetchData() {
            if (lastStateDateTime != null) {
                lastStateDateTimeFa = lastStateDateTime.formatter().getDateTimePersianHm();
            }
            if (assignDateTime != null) {
                assignDateTimeFa = assignDateTime.formatter().getDateTimePersianHm();
            }
            if (measurementDateTime != null) {
                measurementDateTimeFa = measurementDateTime.formatter().getDateTimePersianHm();
            }
            if (siteLocation != null) {
                siteLocation.round(Param.LOCATION_DECIMALS);
            }
            if (spotLocation != null) {
                spotLocation.round(Param.LOCATION_DECIMALS);
            }
            if (site.location != null) {
                site.location.round(Param.LOCATION_DECIMALS);
            }
        }

        public String getSiteAddress() {
            String address = StringUtil.replace(siteAddress, "  ", " ");
            address = StringUtil.replace(address, "  ", " ");
            address = StringUtil.replace(address, "  ", " ");
            address = StringUtil.ltrim(address, province.name, city.name, "شهر", "استان", "-", "_", ",", "،");
            return " استان " + province.name + " - شهر " + city.name + " - " + address;
        }
    }



    // > > > ViewableTiny



    @Storage("RadioMetricFlow")
    public static class ViewableTiny extends DtoBase {

        public Long id;

        public Boolean reRadioMetric;

        public Boolean isCc;
        public Boolean isDrone;

        public Site.ViewableTiny site;
        public RadioMetricComplain.ViewableTiny complain;

        // assign > > >
        @Default("true")
        public Boolean assignable;
        @FetchCache("assignorId")
        public User assignor;
        @FetchCache("assigneeId")
        public User assignee;
        public RadioMetricFlowState lastState;
        public DateTime lastStateDateTime;
        public DateTime assignDateTime;
        public DateTime measurementDateTime;

        public String lastStateDateTimeFa;
        public String assignDateTimeFa;
        public String measurementDateTimeFa;
        // < < < assign

        public String reportedProvince;
        public String reportedCity;

        public String siteAddress;
        public Location siteLocation;

        public String spotAddress;
        public Location spotLocation;

        public Double densityAverage6minX;
        public Double densityAverage6min100;
        public Double densityAverage6min150;
        public Double densityAverage6min170;

        public Boolean isMwCm2X;
        public Boolean isMwCm2100;
        public Boolean isMwCm2150;
        public Boolean isMwCm2170;

        // > > > CHECKING
        public Boolean isCommittable;
        public String committableMessage;
        public String comments;
        // < < < CHECKING


        @Override
        public void afterFetchData() {
            if (lastStateDateTime != null) {
                lastStateDateTimeFa = lastStateDateTime.formatter().getDateTimePersianHm();
            }
            if (assignDateTime != null) {
                assignDateTimeFa = assignDateTime.formatter().getDateTimePersianHm();
            }
            if (measurementDateTime != null) {
                measurementDateTimeFa = measurementDateTime.formatter().getDateTimePersianHm();
            }

            if (siteLocation != null) {
                siteLocation.round(Param.LOCATION_DECIMALS);
            }
            if (spotLocation != null) {
                spotLocation.round(Param.LOCATION_DECIMALS);
            }
            if (site.location != null) {
                site.location.round(Param.LOCATION_DECIMALS);
            }
        }
    }
}