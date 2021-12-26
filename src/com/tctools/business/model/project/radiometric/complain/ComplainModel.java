package com.tctools.business.model.project.radiometric.complain;

import com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.business.dto.site.Site;
import com.tctools.business.dto.user.User;
import com.tctools.common.Param;
import com.vantar.business.*;
import com.vantar.database.common.ValidationError;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.service.log.LogEvent;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import java.util.*;


public class ComplainModel {

    public static ResponseMessage insert(Params params, User user) throws InputException, ServerException {
        RadioMetricComplain complain = new RadioMetricComplain();
        complain.creatorId = user.id;
        return CommonModelMongo.insert(params, complain, new CommonModel.WriteEvent() {

            @Override
            public void beforeWrite(Dto dto) throws InputException, ServerException {
                RadioMetricComplain complain = (RadioMetricComplain) dto;
                completeData(complain);
                uploadImage(params, complain);
            }

            @Override
            public void afterWrite(Dto dto) {

            }
        });
    }

    public static ResponseMessage update(Params params) throws InputException, ServerException {
        RadioMetricComplain complain = new RadioMetricComplain();
        complain.id = params.getLong("id");

        if (NumberUtil.isIdInvalid(complain.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id");
        }

        try {
            complain = CommonRepoMongo.getById(complain);
        } catch (DatabaseException | NoContentException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        return CommonModelMongo.updateStrict(params, complain, new CommonModel.WriteEvent() {

            @Override
            public void beforeWrite(Dto dto) throws InputException, ServerException {
                RadioMetricComplain complain = (RadioMetricComplain) dto;
                if (NumberUtil.isIdInvalid(complain.id)) {
                    throw new InputException(VantarKey.INVALID_ID);
                }
                completeData(complain);
                uploadImage(params, complain);
            }

            @Override
            public void afterWrite(Dto dto) throws ServerException {
                RadioMetricComplain complain = (RadioMetricComplain) dto;
                try {
//                    complain = CommonRepoMongo.getById(complain);
                    if (NumberUtil.isIdInvalid(complain.workFlowId)) {
                        return;
                    }

                    RadioMetricFlow flow = new RadioMetricFlow();
                    flow.id = complain.workFlowId;
                    flow.complain = complain;
                    CommonRepoMongo.update(flow);
  //              } catch (NoContentException ignore) {

                } catch (DatabaseException e) {
                    LogEvent.error(ComplainModel.class, complain, e);
                    throw new ServerException(VantarKey.UPDATE_FAIL);
                }
            }
        });
    }

    private static void uploadImage(Params params, RadioMetricComplain complain) throws InputException {
        Params.Uploaded uploaded = params.upload(Param.FILE_UPLOAD);
        if (uploaded == null || !uploaded.isUploaded()) {
            return;
        }
        if (uploaded.isIoError()) {
            throw new InputException(VantarKey.IO_ERROR);
        }

        List<ValidationError> errors = new ArrayList<>(2);
        if (!uploaded.isType("png")) {
            errors.add(new ValidationError(Param.FILE_UPLOAD, VantarKey.FILE_TYPE, "png"));
        }
        if (uploaded.getSize() < Param.FILE_IMAGE_MIN_SIZE || uploaded.getSize() > Param.FILE_IMAGE_MAX_SIZE) {
            errors.add(
                new ValidationError(
                    Param.FILE_UPLOAD,
                    VantarKey.FILE_SIZE,
                    Param.FILE_IMAGE_MIN_SIZE/1024 + "KB ~ " + Param.FILE_IMAGE_MAX_SIZE/1024 + "KB"
                )
            );
        }
        if (!errors.isEmpty()) {
            throw new InputException(errors);
        }

        uploaded.moveTo(complain.getImageFilePath(false));
    }

    public static ResponseMessage delete(Params params) throws InputException, ServerException {
        RadioMetricComplain complain = new RadioMetricComplain();
        complain.id = params.getLong("id");
        if (NumberUtil.isIdInvalid(complain.id)) {
            throw new InputException(VantarKey.INVALID_ID);
        }

        try {
            try {
                complain = CommonRepoMongo.getById(complain);

                RadioMetricFlow r = new RadioMetricFlow();
                r.id = complain.workFlowId;
                if (NumberUtil.isIdValid(r.id)) {
                    CommonRepoMongo.delete(r);
                }
            } catch (NoContentException | DatabaseException ignore) {

            }

            CommonRepoMongo.delete(complain);

        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        return new ResponseMessage(VantarKey.DELETE_SUCCESS);
    }

    /**
     * if site id is given get siteCode and name
     * if siteCode is given get site id and name
     * if site not exists create site
     */
    private static void completeData(RadioMetricComplain complain) throws ServerException, InputException {
        Site site = new Site();
        if (NumberUtil.isIdValid(complain.siteId)) {
            site.id = complain.siteId;
            try {
                site = CommonRepoMongo.getById(site);
                complain.siteCode = site.code;
                complain.siteName = site.name;
                return;
            } catch (DatabaseException e) {
                throw new ServerException(VantarKey.FETCH_FAIL);
            } catch (NoContentException ignore) {

            }
        }

        if (StringUtil.isEmpty(complain.siteCode)) {
            throw new InputException(VantarKey.INVALID_FIELD, "siteCode");
        }

        site = new Site();
        QueryBuilder q = new QueryBuilder(site);
        q.condition().equal("code", complain.siteCode);
        try {
            site = CommonRepoMongo.getFirst(q);
            complain.siteId = site.id;
            complain.siteName = site.name;
            return;
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        } catch (NoContentException ignore) {

        }

        // insert new site
        site = new Site();
        site.code = complain.siteCode;
        site.provinceId = complain.provinceId;
        site.cityId = complain.cityId;
        site.location = complain.location;
        site.address = complain.address;
        try {
            complain.siteId = CommonRepoMongo.insert(site);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.INSERT_FAIL);
        }
    }

    public static RadioMetricComplain.Viewable get(Params params) throws ServerException, NoContentException, InputException {
        RadioMetricComplain.Viewable complain = new RadioMetricComplain.Viewable();
        complain.id = params.getLong("id");

        if (NumberUtil.isIdInvalid(complain.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (complain.id)");
        }

        try {
            return CommonRepoMongo.getFirst(complain, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }

    public static Object search(Params params) throws ServerException, NoContentException, InputException {
        QueryData queryData = params.getQueryData();
        if (queryData == null) {
            throw new InputException(VantarKey.NO_SEARCH_COMMAND);
        }
        queryData.setDto(new RadioMetricComplain(), new RadioMetricComplain.Viewable());

        try {
            return CommonRepoMongo.search(queryData, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }

    public static Object assignable(Params params) throws ServerException, NoContentException {
        return getAssignQuery(params, true);
    }

    public static Object assigned(Params params) throws ServerException, NoContentException {
        return getAssignQuery(params, false);
    }

    private static Object getAssignQuery(Params params, boolean assignable) throws ServerException, NoContentException {
        QueryBuilder q = new QueryBuilder(new RadioMetricComplain(), new RadioMetricComplain.Viewable());
        q.condition().equal("assignable", assignable);
        try {
            return CommonRepoMongo.search(q, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }
}
