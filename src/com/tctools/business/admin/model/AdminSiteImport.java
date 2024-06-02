package com.tctools.business.admin.model;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.tctools.business.dto.location.*;
import com.tctools.business.dto.site.*;
import com.tctools.business.dto.system.Settings;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.util.Excel;
import com.vantar.admin.index.Admin;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.*;
import com.vantar.database.datatype.Location;
import com.vantar.database.dto.*;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.service.log.ServiceLog;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.*;
import com.vantar.util.object.ClassUtil;
import com.vantar.util.string.*;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class AdminSiteImport {

    private static Map<String, String> stringMaps;
    private static Map<String, Long> codeIdMap;
    protected static Set<String> siteCodes;


    private static Sector getSecondSector(Map<String, Sector> sectors, Sector sector) {
        Sector sector2;
        switch (sector.title) {
            case "A":
                return sectors.get("A2");
            case "B":
                return sectors.get("B2");
            case "C":
                return sectors.get("C2");
            case "D":
                return sectors.get("D2");
        }
        return null;
    }

    public static void importSites(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(AppLangKey.IMPORT_SITE_DATA), params, response, true);

        if (!params.contains("f")) {
            String fields =
                "Region\n" + //0 A
                "Province\n" + //1 B
                "City\n" + //2 C
                "code\n" + //3 D
                "comment\n" + //4 E
                "...\n" + //5 F
                "...\n" + //6 G
                "...\n" + //7 H
                "District\n" + //8 I
                "LocationType\n" + //9 J
                "name:fa\n" + //10 K
                "name:en\n" + //11 L
                "address\n" + //12 M
                "latitude\n" + //13 N
                "longitude\n" + //14 O
                "SiteOwnership\n" + //15 P
                "controller\n" + //16 Q
                "BtsStatus\n" + //17 R
                "setupDate\n" + //18 S
                "BtsOwnership\n" + //19 T
                "contractStartDate\n" + //20 U
                "...\n" + //21 V
                "etcTransceiverCount\n" + //22 W
                "SiteType\n" + //23 X
                "SiteClass\n" + //24 Y
                "BtsInstall\n" + //25 Z
                "BtsTowerType\n" + //26 AA
                "towerHeight\n" + //27 AB
                "buildingHeight\n" + //28 AC
                "frequencyBand\n" + //29 AD
                "Sector A:height\n" + //30 AE
                "Sector A:antennaCount\n" + //31 AF
                "Sector A:azimuth\n" + //32 AG
                "Sector A:mechanicalTilt\n" + //33 AH
                "Sector A:LocationType\n" + //34 AI
                "Sector A2:height\n" + //35 AJ
                "Sector A2:azimuth\n" + //36 AK
                "Sector A2:mechanicalTilt\n" + //37 AL
                "Sector B:height\n" + //38 AM
                "Sector B:antennaCount\n" + //39 AN
                "Sector B:azimuth\n" + //40 AO
                "Sector B:mechanicalTilt\n" + //41 AP
                "Sector B:LocationType\n" + //42 AQ
                "Sector B2:height\n" + //43 AR
                "Sector B2:azimuth\n" + //44 AS
                "Sector B2:mechanicalTilt\n" + //45 AT
                "Sector C:height\n" + //46 AU
                "Sector C:antennaCount\n" + //47 AV
                "Sector C:azimuth\n" + //48 AW
                "Sector C:mechanicalTilt\n" + //49 AX
                "Sector C:LocationType\n" + //50 AY
                "Sector C2:height\n" + //51 AZ
                "Sector C2:azimuth\n" + //52 BA
                "Sector C2:mechanicalTilt\n" + //53 BB
                "Sector D:height\n" + //54 BC
                "Sector D:antennaCount\n" + //55 BD
                "Sector D:azimuth\n" + //56 BE
                "Sector D:mechanicalTilt\n" + //57 BF
                "Sector D:SectorOptimization\n" + //58 BG
                "Sector D2:height\n" + //59 BH
                "Sector D2:azimuth\n" + //60 BI
                "Sector D2:mechanicalTilt\n" + //61 BJ
                "assignable\n" + //62 BK
                "Sector A:electricalTilt\n" + //63 BL
                "Sector B:electricalTilt\n" + //64 BM
                "Sector C:electricalTilt"; //65 BN

            ui.beginUploadForm()
                .addEmptyLine()
                .addFile("CSV file", "csv")
                .addInput("CSV on server", "csv-on-server")
                .addTextArea(Locale.getString(AppLangKey.IMPORT_BTS_FIELD_ORDER), "fields", fields)
                .addCheckbox("REMOVE SITE IF NOT IN csx/xlsx", "remove")
//                .addCheckbox("Synch all Radio Metric states", "synchallradiometric")
//                .addCheckbox("Synch all HSE Audit states", "synchallhse")
                .addSubmit(Locale.getString(VantarKey.ADMIN_IMPORT))
                .finish();
            return;
        }

        // > > > I M P O R T

        long t1 = System.currentTimeMillis();
        siteCodes = new HashSet<>(40000);
        int recordCount = 0;
//        boolean doAllStatesRadioMetric = params.isChecked("synchallradiometric");
//        boolean doAllStatesHseAudit = params.isChecked("synchallhse");
        boolean remove = params.isChecked("remove");

        ui.addMessage("db > sites").write();
        loadSites(ui);

        String[] fields = StringUtil.split(params.getString("fields"), '\n');
        String path = DirUtil.getTempDirectory() + new DateTime().getAsTimestamp();

        ui.addMessage("db > operators").write();
        Map<String, Long> operatorIds = new HashMap<>();
        Set<String> operatorNames = new HashSet<>();
        for (Operator operator : Services.get(ServiceDtoCache.class).getList(Operator.class)) {
            String name = normalizeMore(operator.name.get("fa"));
            operatorIds.put(name, operator.id);
            operatorNames.add(name);
            operatorNames.add(operator.name.get("en"));
        }

        try {
            // > > > UPLOAD
            boolean isXlsx;
            String pathX = params.getString("csv-on-server");
            if (pathX == null) {
                try (Params.Uploaded uploaded = params.upload("csv")) {
                    uploaded.moveTo(path);
                    isXlsx = StringUtil.contains(uploaded.getOriginalFilename(), ".xlsx");
                }
            } else {
                path = pathX;
                isXlsx = StringUtil.contains(path, ".xlsx");
            }

            // > > > CONVERT TO CSV IF NEEDED
            if (isXlsx) {
                ui.addMessage("xlsx > csv").write();
                Excel.excelToCSV(path, path + ".csv", 0, fields.length);
                FileUtil.removeFile(path);
                path += ".csv";
            }

            // > > > READ CSV
            ui.addMessage("processing csv data...").write();
            CSVReader reader = new CSVReader(new FileReader(path));
            String[] record;
            int i = 0;

            // > > > RECORD
            while ((record = reader.readNext()) != null) {
                if (++i == 1) {
                    continue;
                }

                String cityEn = record[6];
                if (StringUtil.isEmpty(record[2])) {
                    record[2] = record[5];
                }

                Site site = new Site();
                Long provinceId = null;
                site.location = new Location();
                Map<String, Sector> sectors = new HashMap<>(8);
                sectors.put("A", new Sector("A"));
                sectors.put("A2", new Sector("A2"));
                sectors.put("B", new Sector("B"));
                sectors.put("B2", new Sector("B2"));
                sectors.put("C", new Sector("C"));
                sectors.put("C2", new Sector("C2"));
                sectors.put("D", new Sector("D"));
                sectors.put("D2", new Sector("D2"));

                // > > > 21 share
                String shared = record[21];
                if (shared == null || shared.contains("مشترک نمی باشد")) {
                    site.collocationType = CollocationType.None;
                } else {
                    CollocationType collocationTypeOther;
                    if (shared.contains("میزبان")) {
                        collocationTypeOther = CollocationType.Host;
                        site.collocationType = CollocationType.Guest;
                    } else if (shared.contains("مهمان")) {
                        collocationTypeOther = CollocationType.Guest;
                        site.collocationType = CollocationType.Host;
                    } else {
                        collocationTypeOther = null;
                        site.collocationType = CollocationType.None;
                    }

                    if (collocationTypeOther != null) {
                        site.collocations = new ArrayList<>();
                        String text = normalizeMore(shared);
                        for (String operatorName : operatorNames) {
                            if (text.contains(operatorName)) {
                                Collocation c = new Collocation();
                                c.type = collocationTypeOther;
                                c.operatorId = operatorIds.get(operatorName);
                                site.collocations.add(c);
                            }
                        }
                    }
                }

                // > > > FIELD
                for (int k = 0; k < fields.length; ++k) {
                    if ("...".equals(fields[k])) {
                        continue;
                    }

                    String value = getString(record[k]);
                    String[] field = StringUtil.split(fields[k], ':');
                    String fieldName = field[0];
                    String fieldInfo = field.length == 2 ? field[1] : null;

                    if ("code".equals(fieldName)) {
                        siteCodes.add(value);
                    }

                    // > > > SECTOR
                    if (fieldName.startsWith("Sector")) {
                        Sector sector = sectors.get(fieldName.substring(Math.max(fieldName.length() - 2, 0)).trim());
                        fieldName = fieldInfo;

                        if (StringUtil.isEmpty(value)) {
                            continue;
                        }

                        String value2;
                        if (StringUtil.contains(value, '/')) {
                            String[] parts = StringUtil.split(value, '/');
                            value = parts[0];
                            value2 = parts[1];
                        } else {
                            value2 = null;
                        }

                        if (fieldName.equals("LocationType")) {
                            Dto dto = ClassUtil.getInstance("com.tctools.business.dto.location.LocationType");
                            sector.setPropertyValue("locationTypeId", getId(ui, dto, value, null));

                            if (value2 != null) {
                                Sector sector2 = getSecondSector(sectors, sector);
                                if (sector2 != null) {
                                    Dto dto2 = ClassUtil.getInstance("com.tctools.business.dto.location.LocationType");
                                    sector2.setPropertyValue("locationTypeId", getId(ui, dto2, value2, null));
                                }
                            }
                            continue;
                        }
                        if (fieldName.equals("SectorOptimization")) {
                            Dto dto = ClassUtil.getInstance("com.tctools.business.dto.site.SectorOptimization");
                            sector.setPropertyValue("sectorOptimizationId", getId(ui, dto, value, null));

                            if (value2 != null) {
                                Sector sector2 = getSecondSector(sectors, sector);
                                if (sector2 != null) {
                                    Dto dto2 = ClassUtil.getInstance("com.tctools.business.dto.site.SectorOptimization");
                                    sector2.setPropertyValue("sectorOptimizationId", getId(ui, dto2, value2, null));
                                }
                            }
                            continue;
                        }

                        sector.setPropertyValue(fieldName, value);
                        if (value.equalsIgnoreCase("omni")) {
                            sector.isOmni = true;
                        }
                        if (value.equalsIgnoreCase("directional")) {
                            sector.isDirectional = true;
                        }
                        if (sector.height != null) {
                            sector.onSiteHeight = sector.height;
                        }

                        if (value2 != null) {
                            Sector sector2 = getSecondSector(sectors, sector);
                            if (sector2 != null) {
                                sector2.setPropertyValue(fieldName, value2);
                                if (value2.equalsIgnoreCase("omni")) {
                                    sector2.isOmni = true;
                                }
                                if (value2.equalsIgnoreCase("directional")) {
                                    sector2.isDirectional = true;
                                }
                                if (sector2.height != null) {
                                    sector2.onSiteHeight = sector2.height;
                                }
                            }
                        }

                        continue;
                    }
                    // < < < SECTOR

                    //  > > > LOCATION
                    if (value != null && fieldName.equals("longitude")) {
                        site.location.longitude = StringUtil.toDouble(value);
                        continue;
                    }
                    if (value != null && fieldName.equals("latitude")) {
                        site.location.latitude = StringUtil.toDouble(value);
                        continue;
                    }
                    // < < < LOCATION

                    if (value != null && (fieldName.equals("btsShare") || fieldName.equals("BtsTowerType"))
                        && normalize(value).contains("نشده")) {

                        value = null;
                    }
                    if (StringUtil.isEmpty(value)) {
                        site.setPropertyValue(fieldName, null);
                        continue;
                    }

                    for (String p : new String[] {"com.tctools.business.dto.location", "com.tctools.business.dto.site"}) {
                        Dto dto = ClassUtil.getInstance(p + "." + fieldName);

                        // > > > SCALAR VALUE
                        if (dto == null) {
                            if (fieldInfo == null) {
                                if ((fieldName.equals("towerHeight") || fieldName.equals("buildingHeight"))
                                    && !StringUtil.isNumeric(value)) {

                                    site.setPropertyValue(fieldName, null);
                                    continue;
                                }

                                if (fieldName.equals("setupDate") || fieldName.equals("contractStartDate")) {
                                    DateTime dt;
                                    try {
                                        if (value.equals("قبل از سال 93")) {
                                            value = "1391-01-01";
                                        }
                                        if (value.equals("-") || value.isEmpty()) {
                                            dt = null;
                                        } else {
                                            dt = new DateTime(value);
                                        }
                                    } catch (DateTimeException e) {
                                        ui.addErrorMessage(
                                            "WARNING " + site.code + " > invalid date " + fieldName + " >" + value + "<"
                                        ).write();
                                        dt = null;
                                    }
                                    site.setPropertyValue(fieldName, dt);
                                    continue;
                                }

                                site.setPropertyValue(fieldName, value);
                                continue;
                            }
                            // > > > lang
                            Map<String, String> propertyValue = (Map<String, String>) site.getPropertyValue(fieldName);
                            if (propertyValue == null) {
                                propertyValue = new HashMap<>(2, 1);
                                site.setPropertyValue(fieldName, propertyValue);
                            }
                            propertyValue.put(fieldInfo, value);
                            continue;
                        }
                        // < < < SCALAR VALUE

                        // > > > REFERENCE VALUE
                        Long id;

                        if (dto.getClass().equals(City.class)) {
                            id = getId(ui, dto, value, cityEn, provinceId == null ? null : provinceId.toString());
                        } else {
                            id = getId(ui, dto, value);
                        }
                        if (dto.getClass().equals(Province.class)) {
                            provinceId = id;
                        }

                        site.setPropertyValue(getPropertyNameFromReference(dto), id);
                        // < < < REFERENCE VALUE
                    }
                }

                // < < < FIELD

                if (site.location.isEmpty()) {
                    site.location = null;
                }

                site.sectors = new ArrayList<>(8);
                site.sectors.addAll(sectors.values());

                List<ValidationError> validationErrors = site.validate(Dto.Action.INSERT);
                if (validationErrors != null && !validationErrors.isEmpty()) {
                    ServiceLog.log.error("! validation error site not added {} > {}", site.code, validationErrors);
                    ui.addErrorMessage("ERROR " + site.code + " > " + ValidationError.toString(validationErrors)).write();
                    continue;
                }

                try {
                    Long id = codeIdMap.get(site.code);
                    if (id != null) {
//                        ServiceLog.log.error(">>>>>>>{}", site);
//                        QueryBuilder q = new QueryBuilder(site);
//                        q.condition().equal("id", id);
                        site.id = id;
                        Db.modelMongo.update(new ModelCommon.Settings(site).logEvent(false).mutex(false));
                        ui.addMessage(
                            (i-1) + " " + Locale.getString(AppLangKey.UPDATED, site.getClass().getSimpleName(), site.code)
                        ).write();
                        site = Db.modelMongo.getById(site);
                    } else {
                        Db.modelMongo.insert(new ModelCommon.Settings(site).logEvent(false).mutex(false));
                        ui.addMessage(
                            (i-1) + " " + Locale.getString(AppLangKey.ADDED, site.getClass().getSimpleName(), site.code)
                        ).write();
                    }

                    // > > > hook to projects
                    AdminSynchRadiometric.synchWithSite(site, ui);
                    //AdminSynchHseAudit.synchWithSite(site, doAllStatesHseAudit, ui);
                    // < < < hook to projects

                    ++recordCount;
                } catch (VantarException e) {
                    ServiceLog.log.error("! unexpected error ({})", site, e);
                    ui.addErrorMessage(e).write();
                }
            }
            // < < < RECORD

        } catch (IOException | CsvValidationException e) {
            ui.addErrorMessage(e).write();
        }

        try {
            QueryBuilder q = new QueryBuilder(new Settings());
            q.condition().equal("key", Settings.KEY_ARAS_UPDATE);
            Db.modelMongo.delete(new ModelCommon.Settings(q).logEvent(false).mutex(false));
            Settings settings = new Settings();
            settings.key = Settings.KEY_ARAS_UPDATE;
            settings.value = new DateTime().formatter().getDate();
            Db.modelMongo.insert(new ModelCommon.Settings(settings).logEvent(false).mutex(false));
        } catch (Exception e) {
            ui.addErrorMessage(e);
        }

        if (remove) {
            removeRemovedSited(ui);
        }

        stringMaps = null;
        codeIdMap = null;
        siteCodes = null;

        ui.addMessage("Finished! " + recordCount + " took " + ((System.currentTimeMillis() - t1) / 1000 / 60 / 60) + " minutes").finish();
    }

    private static String getString(String value) {
        value = StringUtil.remove(value, '–');
        if (stringMaps == null) {
            stringMaps = new HashMap<>();
            stringMaps.put("BTS نصب نشده است", "نصب نشده");
            stringMaps.put("دکل نصب نشده است", "نصب نشده");
            stringMaps.put("NOK", null);
            stringMaps.put("NotExist", null);
            stringMaps.put("Not Exist", null);
            stringMaps.put("0000-00-00", null);
            stringMaps.put("توافقی", null);
            stringMaps.put("ندارد", null);
            stringMaps.put("not", null);
            stringMaps.put("مشترک نمی باشد", null);
            stringMaps.put("تهران - نامشخص", null);
            stringMaps.put("فاقد", null);
            stringMaps.put("نامشخص", null);
            stringMaps.put("no tower", null);
            stringMaps.put("not e", null);
            stringMaps.put("نامحدود", null);
        }

        for (Map.Entry<String, String> entry : stringMaps.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(value)) {
                String s = entry.getValue();
                return s == null ? null : s.trim();
            }
        }
        return value.trim();
    }

    private static void loadSites(WebUi ui) {
        codeIdMap = new HashMap<>();
        try {
            for (Dto dto : Db.modelMongo.getAll(new Site.ViewableIdCode())) {
                codeIdMap.put(((Site.ViewableIdCode) dto).code, ((Site.ViewableIdCode) dto).id);
            }
        } catch (NoContentException e) {
            ui.addMessage("no sites in database");
        } catch (VantarException e) {
            ServiceLog.log.error("! error loading sites", e);
            ui.addErrorMessage("can not load sites");
        }
    }

    private static <T extends Dto> Long getId(WebUi ui, T obj, String... values) {
        for (String name : values) {
            String nValue = normalize(name);
            if ("Out of city".equalsIgnoreCase(name)) {
                continue;
            }


//            if (StringUtil.isEmpty(nValue)) {
//                continue;
//            }

            List<T> dtos = (List<T>) Services.get(ServiceDtoCache.class).getList(obj.getClass());
            for (T dto : dtos) {
                if (dto.hasAnnotation("name", Localized.class)) {
                    for (String v : ((Map<String, String>) dto.getPropertyValue("name")).values()) {
                        if (equals(nValue, normalize(v))) {
                            return dto.getId();
                        }
                    }
                } else {
                    if (equals(nValue, normalize(dto.getPropertyValue("name").toString()))) {
                        return dto.getId();
                    }
                }
            }
        }

        try {
            if (obj instanceof City) {
                ((City) obj).provinceId = StringUtil.toLong(values[2]);
                Map<String, String> map = new HashMap<>(2);
                map.put("fa", values[0]);
                if (values[1].equalsIgnoreCase("Out of city")) {
                    values[1] = values[0];
                }
                map.put("en", values[1]);
                obj.setPropertyValue("name", map);

            } else {


                if (obj.hasAnnotation("name", Localized.class)) {
                    Map<String, String> map = (Map<String, String>) obj.getPropertyValue("name");
                    if (map == null) {
                        map = new HashMap<>(2);
                    }
                    map.put(Persian.containsPersian(values[0]) ? "fa" : "en", values[values.length > 1 ? 1 : 0]);
                    obj.setPropertyValue("name", map);
                } else {
                    obj.setPropertyValue("name", values[0]);
                }
            }

            ResponseMessage res = Db.modelMongo.insert(new ModelCommon.Settings(obj).logEvent(false).mutex(false));
            ui.addMessage(
                Locale.getString(AppLangKey.ADDED, obj.getClass().getSimpleName(), "(" + res.value + ") " + values[0])
            ).write();
            ServiceLog.log.warn("! inserted new({}) > {}", obj.getClass().getSimpleName(), values[0]);
            return (Long) res.value;
        } catch (Exception e) {
            ServiceLog.log.error("! failed insert ({}, {})", obj.getClass().getSimpleName(), values[0], e);
            ui.addErrorMessage(e).write();
            return null;
        }
    }

    private static String getPropertyNameFromReference(Dto dto) {
        char[] c = dto.getClass().getSimpleName().toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c) + "Id";
    }

    private static String normalize(String string) {
        return string == null ? "" : StringUtil.remove(string, '-', ',', '،', '_', '"', '\'', ' ');
    }

    private static String normalizeMore(String string) {
        return string == null ?
            "" : StringUtil.replace(StringUtil.remove(string, '-', ',', '،', '_', '"', '\'', ' '), 'آ', 'ا');
    }

    private static boolean equals(String v1, String v2) {
        if (v1 == null || v2 == null) {
            return false;
        }
        v1 = StringUtil.replace(v1, 'آ', 'ا');
        v2 = StringUtil.replace(v2, 'آ', 'ا');
        if (v1.equalsIgnoreCase(v2)) {
            return true;
        }
        List<Character> v1Set = v1.chars().mapToObj(e->(char) e).collect(Collectors.toList());
        List<Character> v2Set = v2.chars().mapToObj(e->(char) e).collect(Collectors.toList());

        Character prevChar = null;
        Iterator<Character> itr = v1Set.iterator();
        while (itr.hasNext()) {
            char c = itr.next();
            if (prevChar != null && prevChar == c) {
                itr.remove();
                continue;
            }
            prevChar = c;
        }
        itr = v2Set.iterator();
        while (itr.hasNext()) {
            char c = itr.next();
            if (prevChar != null && prevChar == c) {
                itr.remove();
                continue;
            }
            prevChar = c;
        }
        return v1Set.equals(v2Set);
    }

    private static void removeRemovedSited(WebUi ui) {
        try {
            for (Dto dto : Db.modelMongo.getAll(new Site())) {
                Site site = (Site) dto;
                if (!siteCodes.contains(site.code)) {
                    try {
                        Db.modelMongo.delete(new ModelCommon.Settings(site));
                        ui.addMessage("deleted " + site.code + " from Site");
                    } catch (VantarException e) {
                        ServiceLog.log.error("! can not delete {}", site);
                        ui.addErrorMessage(e);
                    }
                }
            }
        } catch (VantarException e) {
            ui.addErrorMessage(e);
        }

        //AdminSynchHseAudit.removeRemovedSited(ui);
        AdminSynchRadiometric.removeRemovedSited(ui);
    }
}