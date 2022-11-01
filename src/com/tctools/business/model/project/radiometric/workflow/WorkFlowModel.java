package com.tctools.business.model.project.radiometric.workflow;

import com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.Sector;
import com.tctools.business.dto.user.*;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.Param;
import com.tctools.common.util.Docx;
import com.vantar.business.*;
import com.vantar.database.common.ValidationError;
import com.vantar.database.datatype.Location;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.util.json.Json;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import org.slf4j.*;
import java.io.*;
import java.util.*;


public class WorkFlowModel {

    private static final Logger log = LoggerFactory.getLogger(WorkFlowModel.class);


    public static ResponseMessage delete(Params params) throws InputException, ServerException {
        return CommonModelMongo.delete(params, new RadioMetricFlow(), new CommonModel.WriteEvent() {

            @Override
            public void beforeSet(Dto dto) {

            }

            @Override
            public void beforeWrite(Dto dto) {

            }

            @Override
            public void afterWrite(Dto dto) {
                RadioMetricComplain complain = new RadioMetricComplain();
                complain.workFlowId = dto.getId();
                try {
                    CommonRepoMongo.delete(complain);
                } catch (DatabaseException ignore) {

                }
            }
        });
    }

    public static ResponseMessage update(Params params) throws InputException, ServerException {
        TempParams tempParams;
        try {
            tempParams = params.getJson(TempParams.class);
        } catch (Exception e) {
            throw new InputException(VantarKey.INVALID_JSON_DATA);
        }

        if (tempParams == null || NumberUtil.isIdInvalid(tempParams.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (RadioMetricFlow)");
        }

        RadioMetricFlow flow = new RadioMetricFlow();
        flow.id = tempParams.id;

        try {
            flow = CommonRepoMongo.getById(flow, params.getLang());
        } catch (NoContentException | DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        return CommonModelMongo.updateJson(params, flow, new CommonModel.WriteEvent() {

            @Override
            public void beforeSet(Dto dto) {

            }

            @Override
            public void beforeWrite(Dto dto) {

            }

            @Override
            public void afterWrite(Dto dto) {
                RadioMetricFlow flow = (RadioMetricFlow) dto;
                if (RadioMetricComplain.isEmpty(flow.complain)) {
                    return;
                }
                try {
                    CommonRepoMongo.update(flow.complain);
                } catch (DatabaseException ignore) {

                }
            }
        });
    }


    private static class TempParams {

        public Long id;

        @Override
        public String toString() {
            return "id=" + id;
        }
    }


    // > > > STATE


    public static ResponseMessage updateState(Params params, User user) throws InputException, ServerException, NoContentException {
        RadioMetricFlowState state;
        try {
            state = RadioMetricFlowState.valueOf(params.getString("state"));
        } catch (IllegalArgumentException e) {
            throw new InputException(AppLangKey.INVALID_STATE);
        }
        return updateState(params.getLong("id"), state, user, params.getString("comments"));
    }

    public static ResponseMessage commitMeasurements(Params params, User user) throws InputException, ServerException, NoContentException {
        List<Long> ids = params.getLongList("ids");
        if (ids == null || ids.isEmpty()) {
            throw new InputException(VantarKey.INVALID_ID, "ids");
        }
        for (Long id : ids) {
            updateState(id, RadioMetricFlowState.Completed, user, params.getString("comments"));
        }
        return ResponseMessage.success(VantarKey.UPDATE_SUCCESS, ((Number) ids.size()).longValue());
    }

    private static ResponseMessage updateState(Long flowId, RadioMetricFlowState state, User user, String comment)
        throws InputException, ServerException, NoContentException {

        // permission
        switch (user.role) {
            case VENDOR:
                if (state != RadioMetricFlowState.Returned && state != RadioMetricFlowState.Approved
                    && state != RadioMetricFlowState.Verified && state != RadioMetricFlowState.Terminated) {
                    throw new InputException(AppLangKey.INVALID_STATE);
                }
                break;

            case TECHNICIAN:
                if (state != RadioMetricFlowState.Problematic && state != RadioMetricFlowState.Completed
                    && state != RadioMetricFlowState.Revise) {

                    throw new InputException(AppLangKey.INVALID_STATE);
                }
                break;
        }

        // no direct access
        if (state == RadioMetricFlowState.Planned) {
            throw new InputException(AppLangKey.INVALID_STATE);
        }

        if (NumberUtil.isIdInvalid(flowId)) {
            throw new InputException(VantarKey.INVALID_ID, "RadioMetricFlow.id");
        }

        RadioMetricFlow flow = new RadioMetricFlow();
        flow.id = flowId;
        try {
            flow = CommonRepoMongo.getFirst(flow);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        int i = flow.state.size();
        if (i > 0 && flow.state.get(i-1).state.equals(state)) {
            throw new InputException(AppLangKey.INVALID_STATE);
        }

        flow.lastState = state;
        flow.lastStateDateTime = new DateTime();

        State s = new State(state, flow.lastStateDateTime, comment);
        s.assignorId = user.id;
        s.assignorName = user.fullName;
        flow.state.add(s);

        try {
            CommonRepoMongo.update(flow);
            return ResponseMessage.success(VantarKey.UPDATE_SUCCESS);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.INSERT_FAIL);
        }
    }


    // > > > SEARCH AND GET


    private static String pathToUrl(String path) {
        return StringUtil.replace(path, Param.RADIO_METRIC_FILES, Param.RADIO_METRIC_URL);
    }

    public static PageData search(Params params) throws ServerException, NoContentException, InputException {
        return CommonModelMongo.search(params, new RadioMetricFlow(), new RadioMetricFlow.Viewable());
    }

    public static RadioMetricFlow.Viewable get(Params params) throws ServerException, NoContentException, InputException {
        RadioMetricFlow flow = new RadioMetricFlow();
        flow.id = params.getLong("id");
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (flow.id)");
        }
        QueryBuilder q = new QueryBuilder(flow, new RadioMetricFlow.Viewable());
        q.setConditionFromDtoEqualTextMatch();
        try {
            return CommonRepoMongo.getFirst(q, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }

    public static ResponseMessage deleteLog(Params params) throws InputException, ServerException {
        RadioMetricFlow flow = new RadioMetricFlow();
        flow.id = params.getLong("id");
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID);
        }

        Integer height = params.getInteger("height");
        if (height == null || (height != 100 && height != 150 && height != 170)) {
            throw new InputException(VantarKey.INVALID_VALUE);
        }

        try {
            flow = CommonRepoMongo.getFirst(flow);
        } catch (DatabaseException | NoContentException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        String path = RadioMetricFlow.getMeasurementPath(flow.site.code, flow.id, height.toString(), false, false);
        if (path != null) {
            FileUtil.removeFile(path);
            FileUtil.removeFile(StringUtil.replace(path, ".csv", "__OK.xlsx"));
        }

        flow.setNullProperties(
            "measurementUrl" + height,
            "densityMin" + height,
            "densityMax" + height,
            "densityAverage6min" + height,
            "densityAverageDevice6min" + height,
            "minRadiationLevel" + height,
            "densityAverageDivMinRadiation" + height,
            "logDateTime" + height,
            "radiationStatus" + height,
            "icnirpPercent" + height
        );

        CommonModelMongo.update(flow);

        return ResponseMessage.success(VantarKey.UPDATE_SUCCESS);
    }


    // > > > MEASUREMENT


    public static ResponseMessage measurementSubmit(Params params) throws InputException, ServerException, NoContentException, AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.ADMIN, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN, Role.VENDOR);

        RadioMetricFlow flow = new RadioMetricFlow();
        flow.id = params.getLong("id");
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "RadioMetricFlow.id");
        }

        try {
            flow = CommonRepoMongo.getFirst(flow);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        List<ValidationError> errors = new ArrayList<>();
        Map<String, Object> success = new HashMap<>();

        uploadImages(params, flow, success, errors);

        applyMeasurement(params, "100", flow, success, errors);
        applyMeasurement(params, "150", flow, success, errors);
        applyMeasurement(params, "170", flow, success, errors);

        setDeviceData(flow);
        success.put("flow", flow);

        CommonModelMongo.update(flow);

        if (!errors.isEmpty()) {
            throw new InputException(errors);
        }

        return ResponseMessage.success(VantarKey.UPDATE_SUCCESS, success);
    }

    private static void setDeviceData(RadioMetricFlow flow) {
        RadioMetricDevice device = new RadioMetricDevice();
        QueryBuilder q = new QueryBuilder(device);
        q.condition().equal("serialNumber", flow.deviceSerialNumber);
        try {
            device = CommonRepoMongo.getFirst(q);
            flow.deviceTitle = device.title;
            flow.deviceManufacturer = device.manufacturer;
            flow.deviceCalibrationExpire = device.calibrationExpire;
        } catch (DatabaseException | NoContentException ignore) {

        }
    }

    public static Object uploadImages(Params params) throws InputException, NoContentException, ServerException, AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.ADMIN, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN, Role.VENDOR);

        RadioMetricFlow flow = new RadioMetricFlow();
        flow.id = params.getLong("id");
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "RadioMetricFlow.id");
        }

        try {
            flow = CommonRepoMongo.getFirst(flow);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        List<ValidationError> errors = new ArrayList<>();
        Map<String, Object> success = new HashMap<>();

        uploadImages(params, flow, success, errors);
        if (!errors.isEmpty()) {
            throw new InputException(errors);
        }

        CommonModelMongo.update(flow);
        return ResponseMessage.success(VantarKey.UPDATE_SUCCESS, success);
    }

    private static void uploadImages(Params params, RadioMetricFlow flow, Map<String, Object> success, List<ValidationError> errors) {
        RadioMetricFlow.getImagePaths(flow.site.code, flow.id, false, false).forEach((imageType, path) ->
            uploadImages(params, imageType, path, success, errors));

        Proximity.getImagePaths(flow.site.code, flow.id, false, false).forEach((imageType, path) ->
            uploadImages(params, imageType, path, success, errors));
    }

    private static void uploadImages(Params params, String imageType, String path
        , Map<String, Object> success, List<ValidationError> errors) {

        String key = Param.FILE_UPLOAD + "-" + StringUtil.replace(imageType.toLowerCase(), ' ', '-');
        try (Params.Uploaded uploaded = params.upload(key)) {
            if (!uploaded.isUploaded() || uploaded.isIoError()) {
                return;
            }

            if (!uploaded.isType("jpeg")) {
                errors.add(new ValidationError(key, VantarKey.FILE_TYPE, "jpeg"));
                return;
            }
            if (uploaded.getSize() < Param.FILE_IMAGE_MIN_SIZE || uploaded.getSize() > Param.FILE_IMAGE_MAX_SIZE) {
                errors.add(
                    new ValidationError(
                        key,
                        VantarKey.FILE_SIZE,
                        Param.FILE_IMAGE_MIN_SIZE / 1024 + "KB ~ " + Param.FILE_IMAGE_MAX_SIZE / 1024 + "KB"
                    )
                );
                return;
            }

            uploaded.moveTo(path);
            if (imageType.equals(RadioMetricPhotoType.TowerView.name())) {
                resize(path, 750, 999);
            }
            success.put(key, pathToUrl(path));
        }
    }

    public static void resize(String filePath, int x, int y) {
        String jsonFilePath = Param.TEMP_DIR + filePath + NumberUtil.random(1, 1000) + ".json";
        String command = "php -d memory_limit=512M " + Docx.class.getResource("/arta/app/docx/").getPath()
            + "resize.php "
            + jsonFilePath;

        FileUtil.giveAllPermissions(Docx.class.getResource("/arta/app/docx/").getPath());

        Map<String, Object> mapping = new HashMap<>(5);
        mapping.put("f", filePath);
        mapping.put("x", x);
        mapping.put("y", y);
        FileUtil.write(jsonFilePath, Json.d.toJson(mapping));

        try (
            BufferedReader input = new BufferedReader(
                new InputStreamReader(
                    Runtime.getRuntime()
                        .exec(command)
                        .getErrorStream()
                )
            )
        ) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null) {
                output.append(line).append("\n");
            }
            log.info("command={}\n\noutput={}\n\n", command, output);

            if (output.length() > 0) {
                throw new ServerException(output.toString());
            }

        } catch (Exception e) {
            log.error("! {} < {}", filePath, mapping, e);
        } finally {
            FileUtil.removeFile(jsonFilePath);
        }
    }

    private static void applyMeasurement(Params params, String height, RadioMetricFlow flow,
        Map<String, Object> success, List<ValidationError> errors) throws ServerException {

        String key = Param.FILE_UPLOAD + "-" + height;
        try (Params.Uploaded uploaded = params.upload(key)) {
            if (!uploaded.isUploaded() || uploaded.isIoError()) {
                return;
            }
            if (uploaded.getSize() < Param.FILE_MEASURE_CSV_MIN_SIZE || uploaded.getSize() > Param.FILE_MEASURE_CSV_MAX_SIZE) {
                errors.add(
                    new ValidationError(
                        key,
                        VantarKey.FILE_SIZE,
                        Param.FILE_MEASURE_CSV_MIN_SIZE / 1024 + "KB ~ " + Param.FILE_MEASURE_CSV_MAX_SIZE / 1024 + "KB"
                    )
                );
            }

            String path = RadioMetricFlow.getMeasurementPath(flow.site.code, flow.id, height, false, false);
            uploaded.moveTo(path);
            success.put(key, pathToUrl(path));

            Measurement measurement = new Measurement();
            measurement.applyCsv(path, flow, height, errors);
            measurement.createOkExcel(path);
            WorkFlowModel.setNearestSector(flow);
        }
    }

    private static double getAzimuth(Location centerPt, Location targetPt) {
        double v = Math.toDegrees(Math.atan2(targetPt.longitude - centerPt.longitude, targetPt.latitude - centerPt.latitude));
        return v > 0 ? v : (360 + v);
    }

    public static void setNearestSector(RadioMetricFlow flow) {
        if (flow.site.location == null || flow.spotLocation == null || flow.sectors == null || flow.sectors.isEmpty()) {
            return;
        }

        int count = 0;
        int bestFitIndex = -1;
        List<Sector> sectors = flow.sectors;
        for (int i = 0, sectorsSize = sectors.size(); i < sectorsSize; ++i) {
            if (!sectors.get(i).isEmpty()) {
                ++count;
                bestFitIndex = i;
            }
        }
        if (count == 1) {
            flow.sectors.get(bestFitIndex).selected = true;
            return;
        }

        double azimuth = getAzimuth(flow.site.location, flow.spotLocation);

        Sector selected = null;
        double minDelta = 10000;
        for (Sector s : flow.sectors) {
            s.selected = false;
            if (s.isEmpty() || s.azimuth == null) {
                continue;
            }

            double delta = Math.abs(azimuth - s.azimuth);
            double deltaAntiClockwise = 360 - Math.abs(azimuth - s.azimuth);

            if (delta < minDelta) {
                minDelta = delta;
                selected = s;
            }
            if (deltaAntiClockwise < minDelta) {
                minDelta = deltaAntiClockwise;
                selected = s;
            }
        }

        if (selected != null) {
            String selectedTitle = StringUtil.remove(selected.title, '1', '2', '3', '4', '0');
            for (Sector s : flow.sectors) {
                if (selectedTitle.equalsIgnoreCase(s.title)) {
                    selected.selected = true;
                    return;
                }
            }
            selected.selected = true;
        }
    }


    // > > > TECHNICIAN


    public static List<RadioMetricFlow.Viewable> getNewTasks(Params params, User user) throws ServerException, NoContentException {
        QueryBuilder q = new QueryBuilder(new RadioMetricFlow(), new RadioMetricFlow.Viewable());
        q.condition()
            .equal("assigneeId", user.id)
            .equal("lastState", RadioMetricFlowState.Planned);

        try {
            return CommonRepoMongo.getData(q, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }

    public static List<RadioMetricFlow.Viewable> getFinishedTasks(Params params, User user) throws ServerException, NoContentException {
        QueryBuilder q = new QueryBuilder(new RadioMetricFlow(), new RadioMetricFlow.Viewable());
        q.condition()
            .equal("assigneeId", user.id)
            .notEqual("lastState", RadioMetricFlowState.Planned);

        try {
            return CommonRepoMongo.getData(q, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }

    public static ResponseMessage deleteImage(Params params) throws InputException, ServerException {
        String path = params.getString("path");
        if (StringUtil.isEmpty(path)) {
            throw new InputException(VantarKey.REQUIRED, "path");
        }

        FileUtil.removeFile(StringUtil.replace(path, Param.RADIO_METRIC_URL, Param.RADIO_METRIC_FILES));
        return path.contains("/complain/") ? deleteImageComplain(params.getLong("complainid"), path) : deleteImageFlow(path);
    }

    private static ResponseMessage deleteImageFlow(String path) throws ServerException, InputException {
        String[] parts = StringUtil.split(path, "/measurement/");
        if (parts.length != 2) {
            throw new InputException(VantarKey.INVALID_VALUE, "path");
        }
        parts = StringUtil.split(parts[1], '/');

        RadioMetricFlow flow = new RadioMetricFlow();
        flow.id = StringUtil.scrapeLong(parts[0]);
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (RadioMetricFlow)");
        }

        try {
            flow = CommonRepoMongo.getFirst(flow);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        } catch (NoContentException e) {
            throw new InputException(VantarKey.INVALID_ID, "id (RadioMetricFlow)");
        }

        CommonModelMongo.update(flow);
        return ResponseMessage.success(VantarKey.DELETE_SUCCESS);
    }

    private static ResponseMessage deleteImageComplain(Long complainId, String path) throws ServerException, InputException {
        if (NumberUtil.isIdInvalid(complainId)) {
            throw new InputException(VantarKey.INVALID_ID, "complainid");
        }

        RadioMetricComplain complain = new RadioMetricComplain();
        complain.id = complainId;
        try {
            complain = CommonRepoMongo.getById(complain);
            complain.imageUrl = null;
            CommonModelMongo.update(complain);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        } catch (NoContentException e) {
            throw new InputException(VantarKey.INVALID_ID, "complainid");
        }

        if (complain.workFlowId != null) {
            RadioMetricFlow flow = new RadioMetricFlow();
            flow.id = complain.workFlowId;
            try {
                flow = CommonRepoMongo.getById(flow);
                flow.complain.imageUrl = null;
                CommonModelMongo.update(flow);

                FileUtil.removeFile(
                    StringUtil.replace(
                        StringUtil.replace(path, Param.RADIO_METRIC_URL, Param.RADIO_METRIC_FILES),
                        "/complain/",
                        "/measurement/" + flow.id + "/"
                    )
                );

            } catch (DatabaseException e) {
                throw new ServerException(VantarKey.FETCH_FAIL);
            } catch (NoContentException ignore) {

            }
        }

        return ResponseMessage.success(VantarKey.DELETE_SUCCESS);
   }
}