package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.Sector;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.Param;
import com.tctools.common.util.*;
import com.vantar.database.common.Db;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.service.log.ServiceLog;
import com.vantar.util.bool.BoolUtil;
import com.vantar.util.collection.CollectionUtil;
import com.vantar.util.file.*;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.object.ObjectUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class ExportSite extends ExportCommon {

    private static final String UNCHECKED = "⬜";
    private static final String CHECKED = "⬛";


    public static void zip(Params params, HttpServletResponse response) throws VantarException {
        RadioMetricFlow.Viewable flow = Db.modelMongo.getById(params, new RadioMetricFlow.Viewable());

        String zipTempDir = DirUtil.getTempDirectory();
        String zipFile = flow.site.code
            + (flow.isCc && flow.complain.ccnumber != null ? " C.C-" + flow.complain.ccnumber : "") + ".zip";

        docx(params, response, false);

        if (flow.isCc) {
            QueryBuilder q = new QueryBuilder(new RadioMetricComplain.Viewable());
            q.condition().equal("ccnumber", flow.complain.ccnumber);
            try {
                flow.complain = Db.modelMongo.getFirst(q);
            } catch (Exception ignore) {

            }

            String imagePath = flow.complain.getImageFilePath(true);
            if (imagePath != null) {
                FileUtil.copy(imagePath, flow.getPath() + flow.complain.getImageFilename());
            }

            try {
                String ccnumber = flow.complain.ccnumber;
                String path = flow.getPath();

                DirUtil.browseDir(Param.RADIO_METRIC_FILES + flow.site.code + "/complain/", file -> {
                    if (file.getName().contains(ccnumber)) {
                        FileUtil.copy(file.getAbsolutePath(), path + file.getName());
                    }
                });
            } catch (Exception ignore) {

            }
        }

        String dir = Param.RADIO_METRIC_FILES + flow.site.code + "/measurement/" + flow.id;

        try {
            if (BoolUtil.isTrue(flow.isDrone)) {
                String csvX = flow.getPath() + flow.site.code + "__droneCM__OK.csv";
                String csvXx = flow.getPath() + flow.site.code + "__droneCM__OK.xlsx";
                if (flow.isMwCm2X) {
                    FileUtil.removeFile(csvX);
                    FileUtil.removeFile(csvXx);
                }
            } else {
                String csv100 = flow.getPath() + flow.site.code + "__100CM__OK.csv";
                String csv150 = flow.getPath() + flow.site.code + "__150CM__OK.csv";
                String csv170 = flow.getPath() + flow.site.code + "__170CM__OK.csv";
                String csv100x = flow.getPath() + flow.site.code + "__100CM__OK.xlsx";
                String csv150x = flow.getPath() + flow.site.code + "__150CM__OK.xlsx";
                String csv170x = flow.getPath() + flow.site.code + "__170CM__OK.xlsx";
                if (flow.isMwCm2100) {
                    FileUtil.removeFile(csv100);
                    FileUtil.removeFile(csv100x);
                }
                if (flow.isMwCm2150) {
                    FileUtil.removeFile(csv150);
                    FileUtil.removeFile(csv150x);
                }
                if (flow.isMwCm2170) {
                    FileUtil.removeFile(csv170);
                    FileUtil.removeFile(csv170x);
                }
            }
        } catch (Exception ignore) {

        }

        FileUtil.zip(dir, zipTempDir + zipFile, filename -> !filename.endsWith(".jpg.png"));

        response.setContentType("application/zip");
        Response.download(response, zipTempDir + zipFile, zipFile);
    }

    public static void docx(Params params, HttpServletResponse response, boolean outputResult) throws VantarException {
        RadioMetricFlow.Viewable flow = new RadioMetricFlow.Viewable();
        flow.id = params.getLong("id");
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "flow.id");
        }

        flow = Db.modelMongo.getById(flow, "fa");

        if (flow.site == null || flow.site.code == null) {
            throw new ServerException(AppLangKey.EXPORT_FAIL);
        }

        Map<String, Object> mapping = new HashMap<>();
        mapping.put("date", dateSet(getDateValue(flow.measurementDateTimeFa)));

        if (flow.site.frequencyBand == null || flow.site.frequencyBand.isEmpty()) {
            mapping.put("freq", "");
        } else {
            String s = StringUtil.replace(
                StringUtil.remove(
                    getValue(flow.site.frequencyBand).toUpperCase(),
                    "2G", "3G", "4G", "5G", "MHZ", "TDLTE", "(", ")"
                ), '-', ','
            );
            String[] set = StringUtil.splitToSet(s, ',').toArray(new String[0]);
            int[] freqs = new int[set.length];
            for (int i = 0; i < set.length; i++) {
                Integer v = StringUtil.toInteger(set[i]);
                if (v == null) {
                    log.error("! frequencyBand {} > {}", flow.site.frequencyBand, set);
                    continue;
                }
                freqs[i] = v;
            }
            Arrays.sort(freqs);
            mapping.put("freq", CollectionUtil.join(freqs, ',') + " (MHz)");
        }

        mapping.put("siteCode", getValue(flow.site.code));
        mapping.put("siteName", getValue(flow.site.name));

        mapping.put("siteAddress", flow.getSiteAddress());

        mapping.put("latitude", flow.siteLocation == null ? "" : getValue(flow.siteLocation.latitude));
        mapping.put("longitude", flow.siteLocation == null ? "" : getValue(flow.siteLocation.longitude));

        String selectedSector = "";
        StringBuilder sectorHeights = new StringBuilder();
        Set<String> missingSectors = new HashSet<>(3);
        missingSectors.add("A");
        missingSectors.add("B");
        missingSectors.add("C");

        List<Sector.Viewable> sectors = flow.sectors;
        if (ObjectUtil.isEmpty(sectors)) {
            sectors = flow.site.sectors;
        }
        if (sectors != null) {
            for (Sector.Viewable sector : sectors) {
                if (sector.isEmpty()) {
                    continue;
                }
                sector.title = sector.title.toUpperCase();
                sectorHeights.append("Sector ").append(sector.title).append("=").append(getValue(sector.onSiteHeight)).append("  ");

                if (sector.selected != null && (sector.selected || sectors.size() == 1)) {
                    selectedSector = sector.title;
                }

                if (!sector.title.equals("A") && !sector.title.equals("B") && !sector.title.equals("C") ) {
                    continue;
                }

                mapping.put("sAngle" + sector.title, getValue(sector.azimuth == null ? (Object) sector.height : (Object) sector.azimuth));
                mapping.put("mTilt" + sector.title, getValue(sector.mechanicalTilt));
                mapping.put("eTilt" + sector.title, getValue(sector.electricalTilt));

                missingSectors.remove(sector.title);
            }
        }
        selectedSector = StringUtil.remove(selectedSector, '1', '2', '3', '4', '0');
        for (String sector : missingSectors) {
            mapping.put("sAngle" + sector, "");
            mapping.put("mTilt" + sector, "");
            mapping.put("eTilt" + sector, "");
        }

        if (sectorHeights.length() > 2) {
            sectorHeights.setLength(sectorHeights.length() - 2);
        }
        mapping.put("sectorHeight",  sectorHeights.toString());

        boolean hasProximityFaculty = false;
        String proximityFacultyName = "";
        boolean hasProximityHealth = false;
        String proximityHealthName = "";

        boolean ob = false;
        String oNearName2 = "";
        String oNearSiteId2 = "";
        String oNearName3 = "";
        String oNearSiteId3 = "";
        String oNearName4 = "";
        String oNearSiteId4 = "";

        if (flow.proximities != null) {
            for (Proximity proximity : flow.proximities) {
                if (StringUtil.isEmpty(proximity.comments)) {
                    continue;
                }
                if (RadioMetricProximityType.EducationalInstitution.equals(proximity.proximityType)) {
                    hasProximityFaculty = true;
                    proximityFacultyName = getValue(proximity.comments);
                } else if (RadioMetricProximityType.HealthFacility.equals(proximity.proximityType)) {
                    hasProximityHealth = true;
                    proximityHealthName = getValue(proximity.comments);
                } else if (RadioMetricProximityType.OperatorSite.equals(proximity.proximityType)) {
                    ob = true;
                    String[] parts = StringUtil.split(getValue(proximity.comments), ',');

                    for (String p : parts) {
                        if (oNearName2.isEmpty()) {
                            oNearName2 = StringUtil.isNotEmpty(p) ? p : "نامشخص";
                            oNearSiteId2 = "نامشخص";
                            continue;
                        }

                        if (oNearName3.isEmpty()) {
                            oNearName3 = StringUtil.isNotEmpty(p) ? p : "نامشخص";
                            oNearSiteId3 = "نامشخص";
                            continue;
                        }

                        if (oNearName4.isEmpty()) {
                            oNearName4 = StringUtil.isNotEmpty(p) ? p : "نامشخص";
                            oNearSiteId4 = "نامشخص";
                        }
                    }
                }
            }
        }

        mapping.put("EB", hasProximityFaculty ? CHECKED : UNCHECKED);
        mapping.put("EK", hasProximityFaculty ? UNCHECKED : CHECKED);

        mapping.put("proximityFacultyName", proximityFacultyName);

        mapping.put("DB", hasProximityHealth ? CHECKED : UNCHECKED);
        mapping.put("DK", hasProximityHealth ? UNCHECKED : CHECKED);


        mapping.put("OB", ob ? CHECKED : UNCHECKED);
        mapping.put("OK", ob ? UNCHECKED : CHECKED);

        mapping.put("oNearName2", oNearName2);
        mapping.put("oNearSiteId2", oNearSiteId2);
        mapping.put("oNearName3", oNearName3);
        mapping.put("oNearSiteId3", oNearSiteId3);
        mapping.put("oNearName4", oNearName4);
        mapping.put("oNearSiteId4", oNearSiteId4);


        mapping.put("proximityHealthName", proximityHealthName);

        String greenField = UNCHECKED;
        String roofTop = UNCHECKED;
        String wallMount = UNCHECKED;
        String ibs = UNCHECKED;
        String cellUpgrade = UNCHECKED;
        String others = UNCHECKED;

        if (flow.site.siteType == null) {
            others = CHECKED;
        } else if (flow.site.siteType.name.equals("زمینی")) {
            greenField = CHECKED;
        } else if (flow.site.siteType.name.contains("پشت")) {
            roofTop = CHECKED;
        } else if (flow.site.siteType.name.equals("دیواری")) {
            wallMount = CHECKED;
        } else if (flow.site.siteType.name.equalsIgnoreCase("Ibs")) {
            ibs = CHECKED;
        } else if (flow.site.siteType.name.contains("بروز")) {
            cellUpgrade = CHECKED;
        } else {
            others = CHECKED;
        }
        mapping.put(
            "siteType",
            "Roof Top  " + roofTop + "        Green Field    " + greenField + "           Wall Mount " + wallMount
        );
        mapping.put(
            "siteTypeX",
            "        IBS    " + ibs + "       Cell Upgrade   " + cellUpgrade + "                 Others    " + others
        );
        mapping.put("floorNo", getValue(flow.siteFloor));

        boolean noCollocations = flow.collocations == null || flow.collocations.isEmpty();

        mapping.put("SK", noCollocations ? CHECKED : UNCHECKED);
        mapping.put("SB", noCollocations ? UNCHECKED : CHECKED);

        String operatorName2;
        String operatorName3;
        String operatorName4;
        if (noCollocations) {
            operatorName2 = "";
            operatorName3 = "";
            operatorName4 = "";
        } else {
            operatorName2 = getValue(flow.collocations.get(0).operator.name);
            operatorName3 = flow.collocations.size() > 1 ? getValue(flow.collocations.get(1).operator.name) : "";
            operatorName4 = flow.collocations.size() > 2 ? getValue(flow.collocations.get(2).operator.name) : "";

            if (StringUtil.isEmpty(operatorName2)) {
                operatorName2 = "";
            }
            if (StringUtil.isEmpty(operatorName3)) {
                operatorName3 = "";
            }
            if (StringUtil.isEmpty(operatorName4)) {
                operatorName4 = "";
            }
        }
        mapping.put("oName2", operatorName2);
        mapping.put("oSiteId2", noCollocations || StringUtil.isEmpty(operatorName2) ? "" : "نامشخص");
        mapping.put("oName3", operatorName3);
        mapping.put("oSiteId3",  noCollocations || StringUtil.isEmpty(operatorName3) ? "" : "نامشخص");
        mapping.put("oName4", operatorName4);
        mapping.put("oSiteId4",  noCollocations || StringUtil.isEmpty(operatorName4) ? "" : "نامشخص");

        mapping.put("comments", flow.comments == null ? "" : getValue(" - " + flow.comments));
        mapping.put("deviceName", getValue(flow.deviceTitle));
        mapping.put("deviceModel", getValue(flow.deviceModel));
        mapping.put("deviceSerialNo", getValue(flow.deviceSerialNumber));
        mapping.put("deviceBrand", getValue(flow.deviceManufacturer));
        mapping.put("deviceProbe", getValue(flow.deviceProbe));
        mapping.put(
            "deviceDate",
            flow.deviceCalibrationExpire == null ?
                "" : getDateValue(flow.deviceCalibrationExpire.formatter().getDatePersian())
        );


        String complainSector = selectedSector;
        String complainFloorCount = "";
        String complainDistance;
        String complainAntennaHeight;

        String address = getValue(flow.spotAddress);

        String complainLatitude;
        String complainLongitude;
        String complainFloor = "";
        String complainAddressNom = "";
        String complainPropertyType = "";
        String complainRoom = "";
        String complainComplainerName = "";
        String complainTel = "";
        String complainMobile = "";
        if (flow.isCc) {
            complainFloorCount = getValue(flow.complain.floorCount);
            complainComplainerName = getValue(flow.complain.complainerName);
            complainTel = getValue(flow.complain.complainerPhone);
            complainMobile = getValue(flow.complain.complainerMobile);
            complainFloor = getValue(flow.complain.floor);

            complainAddressNom = getValue(flow.complain.unitNumber);
            complainRoom = flow.complain.propertySection == null ? "" : getValue(flow.complain.propertySection.name);
            complainPropertyType = flow.complain.property == null ? "" : getValue(flow.complain.property.name);
        }

        complainLatitude = flow.spotLocation == null ? "" : getValue(flow.spotLocation.latitude);
        complainLongitude = flow.spotLocation == null ? "" : getValue(flow.spotLocation.longitude);


        complainAntennaHeight = getValue(flow.measurementHeight);
        complainDistance = getValue(flow.horizontalDistanceFromLocationEstimate);

        mapping.put("complainSector", complainSector);
        mapping.put("complainFloorCount", complainFloorCount);
        mapping.put("complainDistance", complainDistance);
        mapping.put("complainAntennaHeight", complainAntennaHeight);
        mapping.put("address", address);
        mapping.put("cLatitude", complainLatitude);
        mapping.put("cLongitude", complainLongitude);
        mapping.put("complainFloor", complainFloor);
        mapping.put("complainAddressNom", complainAddressNom);
        mapping.put("complainPropertyType", complainPropertyType);
        mapping.put("complainRoom", complainRoom);
        mapping.put("complainComplainerName", complainComplainerName);
        mapping.put("complainTel", complainTel);
        mapping.put("complainMobile", complainMobile);


        mapping.put("maxDensity170", getValueR(flow.densityMax170));
        mapping.put("avgDensity170", getValueR(flow.densityAverage6min170));
        mapping.put("maxDensity150", getValueR(flow.densityMax150));
        mapping.put("avgDensity150", getValueR(flow.densityAverage6min150));
        mapping.put("maxDensity100", getValueR(flow.densityMax100));
        mapping.put("avgDensity100", getValueR(flow.densityAverage6min100));

        mapping.put("technicianName", flow.assignee == null ? "" : flow.assignee.fullName);
        mapping.put("reporterNames", flow.assignee == null ? "" : flow.assignee.fullName);
        mapping.put("medicalName", "");
        mapping.put(
            "measurementDate",
            flow.measurementDateTime == null ? "" : dateSet(getDateValue(flow.measurementDateTimeFa))
        );
        mapping.put(
            "measurementTime",
            flow.measurementDateTime == null ? "" : flow.measurementDateTime.formatter().getTimeHm()
        );

        mapping.put(
            "reportDate",
            flow.measurementDateTime == null ? "" : dateSet(getDateValue(flow.measurementDateTimeFa))
        );

        mapping.put("measuredSector", selectedSector);

        mapping.put(
            "imgSignature",
            getImage(
                flow.assignee == null ? "" : flow.assignee.getSignature(false),
                100,
                100
            )
        );



        if (BoolUtil.isTrue(flow.isDrone)) {
            mapping.put(
                "imgLocation1",
                getImage(
                    RadioMetricFlow.getImagePath(RadioMetricPhotoType.Drone, flow.site.code, flow.id, false, true),
                    350, 450
                )
            );
            mapping.put(
                "imgLocation2",
                getImage(
                    RadioMetricFlow.getImagePath(RadioMetricPhotoType.TargetBuildingFromTower, flow.site.code, flow.id, false, true),
                    350, 450
                )
            );
            mapping.put(
                "imgLocation3",
                getImage(
                    RadioMetricFlow.getImagePath(RadioMetricPhotoType.TargetBuilding, flow.site.code, flow.id, false, true),
                    350, 450
                )
            );
            mapping.put(
                "imgLocation4",
                getImage(
                    RadioMetricFlow.getImagePath(RadioMetricPhotoType.FlightControlScreen, flow.site.code, flow.id, false, true),
                    350, 450
                )
            );
            mapping.put(
                "imgLocation5",
                getImage(
                    RadioMetricFlow.getImagePath(RadioMetricPhotoType.TowerTopView, flow.site.code, flow.id, false, true),
                    350, 450
                )
            );



        } else {
            mapping.put(
                "imgLocation1",
                getImage(
                    RadioMetricFlow.getImagePath(RadioMetricPhotoType.TowerView, flow.site.code, flow.id, false, true),
                    350, 450
                )
            );
            mapping.put(
                "imgLocation2",
                getImage(
                    RadioMetricFlow.getImagePath(RadioMetricPhotoType.ProbeViewInFrontOfMeasuredSector, flow.site.code, flow.id, false, true),
                    350, 450
                )
            );

            Map<String, String> proximityImages = Proximity.getImagePaths(flow.site.code, flow.id, false, true);
            String proximityImageItem = proximityImages.get(StringUtil.toKababCase(RadioMetricProximityType.HealthFacility.toString()));
            if (StringUtil.isEmpty(proximityImageItem)) {
                proximityImageItem = proximityImages.get(StringUtil.toKababCase(RadioMetricProximityType.EducationalInstitution.toString()));
            }
            if (!StringUtil.isEmpty(proximityImageItem)) {
                mapping.put(
                    "imgLocation3",
                    getImage(
                        proximityImageItem,
                        210, 300
                    )
                );
                mapping.put(
                    "dummy",
                    getImage(
                        RadioMetricFlow.getImagePath(RadioMetricPhotoType.Address, flow.site.code, flow.id, false, true),
                        210, 300
                    )
                );
            } else {
                mapping.put(
                    "imgLocation3",
                    getImage(
                        RadioMetricFlow.getImagePath(RadioMetricPhotoType.Address, flow.site.code, flow.id, false, true),
                        210, 300
                    )
                );
            }
            mapping.put(
                "imgLocation4",
                getImage(
                    RadioMetricFlow.getImagePath(RadioMetricPhotoType.TripodInLocation, flow.site.code, flow.id, false, true),
                    210, 300
                )
            );
            mapping.put(
                "dummy2",
                getImage(
                    RadioMetricFlow.getImagePath(RadioMetricPhotoType.Extra1, flow.site.code, flow.id, false, true),
                    210, 300
                )
            );
            mapping.put(
                "dummy3",
                getImage(
                    RadioMetricFlow.getImagePath(RadioMetricPhotoType.Extra2, flow.site.code, flow.id, false, true),
                    210, 300
                )
            );
        }


        String siteCode = flow.site.code.toUpperCase();
        //String filename = siteCode + (RadioMetricComplain.isEmpty(flow.complain) ? "" : ("__" + flow.complain.ccnumber)) + ".docx";
        String filename = siteCode + (!flow.isCc ? "" : ("__" + flow.complain.ccnumber)) + ".docx";
        String filePath = flow.getPath();
        try {
            Docx.createFromTemplate(Param.RADIO_METRIC_SITE_TEMPLATE, filePath, filename, mapping);
            if (!FileUtil.exists(filePath + filename)) {
                throw new ServerException("docx not created");
            }

            if (outputResult) {
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                Response.download(response, filePath + filename, filename);
            }
        } catch (VantarException e) {
            throw new ServerException(e);
        }
    }

    private static String getValueR(Double d) {
        if (d == null) {
            return "";
        }
        double dd = NumberUtil.round(d, 3);
        return getValue(dd == 0 ? d : dd);
    }

    private static String dateSet(String d) {
        try {
            String[] dt = StringUtil.split(d, ' ');
            String[] parts = StringUtil.split(dt[0], '/');
            return parts.length != 3 ? "" : parts[2] + '/' + parts[1] + '/' + parts[0];
        } catch (Exception e) {
            ServiceLog.log.error("!!!!", e);
            return "";
        }
    }

    private static Object getImage(String path, int w, int h) {
        return StringUtil.isEmpty(path) || !FileUtil.exists(path) ? "" : new Docx.Picture(path, w, h);
    }
}