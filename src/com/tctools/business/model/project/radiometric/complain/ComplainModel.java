package com.tctools.business.model.project.radiometric.complain;

import com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.business.dto.site.Site;
import com.tctools.business.dto.user.User;
import com.tctools.common.Param;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.*;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import java.util.*;


public class ComplainModel {

    public static ResponseMessage insert(Params params, User user) throws VantarException {
        RadioMetricComplain complainX = new RadioMetricComplain();
        complainX.creatorId = user.id;
        return Db.modelMongo.insert(
            new ModelCommon.Settings(params, complainX)
                .mutex(false)
                .setEventBeforeValidation(dto -> {
                    RadioMetricComplain complain = (RadioMetricComplain) dto;
                    completeData(complain);
                    uploadImage(params, complain);
                })
        );
    }

    public static ResponseMessage update(Params params) throws VantarException {
        RadioMetricComplain complainX = Db.modelMongo.getById(params, new RadioMetricComplain());
        return Db.modelMongo.update(
            new ModelCommon.Settings(params, complainX).mutex(false)
                .setEventBeforeValidation(dto -> {
                    RadioMetricComplain complain = (RadioMetricComplain) dto;
                    if (NumberUtil.isIdInvalid(complain.id)) {
                        throw new InputException(VantarKey.INVALID_ID);
                    }
                    completeData(complain);
                    try {
                        uploadImage(params, complain);
                    } catch (Exception ignore) {

                    }
                })
                .setEventAfterWrite(dto -> {
                    RadioMetricComplain complain = (RadioMetricComplain) dto;
                    RadioMetricFlow flow = new RadioMetricFlow();

                    if (NumberUtil.isIdInvalid(complain.workFlowId)) {
                        QueryBuilder q = new QueryBuilder(flow);
                        q.condition().equal("complain.ccnumber", complain.ccnumber);
                        try {
                            flow = Db.modelMongo.getFirst(q);
                        } catch (Exception e) {
                            return;
                        }
                    } else {
                        flow.id = complain.workFlowId;
                    }
                    flow.complain = complain;

                    Db.modelMongo.update(new ModelCommon.Settings(flow).mutex(false));
                })
        );
    }

    private static void uploadImage(Params params, RadioMetricComplain complain) throws InputException {
        try (Params.Uploaded uploaded = params.upload(Param.FILE_UPLOAD)) {
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
                        Param.FILE_IMAGE_MIN_SIZE / 1024 + "KB ~ " + Param.FILE_IMAGE_MAX_SIZE / 1024 + "KB"
                    )
                );
            }
            if (!errors.isEmpty()) {
                throw new InputException(errors);
            }

            uploaded.moveTo(complain.getImageFilePath(false));
        }
    }

    public static ResponseMessage delete(Params params) throws VantarException {
        RadioMetricComplain complain = Db.modelMongo.getById(params, new RadioMetricComplain());

        try {
            RadioMetricFlow r = new RadioMetricFlow();
            r.id = complain.workFlowId;
            if (NumberUtil.isIdValid(r.id)) {
                Db.modelMongo.delete(new ModelCommon.Settings(r));
            }
        } catch (VantarException ignore) {

        }

        return Db.modelMongo.delete(new ModelCommon.Settings(complain));
    }

    /**
     * if site id is given get siteCode and name
     * if siteCode is given get site id and name
     * if site not exists create site
     */
    private static void completeData(RadioMetricComplain complain) throws VantarException {
        Site site = new Site();
        if (NumberUtil.isIdValid(complain.siteId)) {
            site.id = complain.siteId;
            try {
                site = Db.modelMongo.getById(site);
                complain.siteCode = site.code;
                complain.siteName = site.name;
                return;
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
            site = Db.modelMongo.getFirst(q);
            complain.siteId = site.id;
            complain.siteName = site.name;
            return;
        } catch (NoContentException ignore) {

        }

        // insert new site
        site = new Site();
        site.code = complain.siteCode;
        site.provinceId = complain.provinceId;
        site.cityId = complain.cityId;
        site.location = complain.location;
        site.address = complain.address;
        complain.siteId = Db.modelMongo.insert(new ModelCommon.Settings(site).mutex(false)).dto.getId();
    }

    public static RadioMetricComplain.Viewable get(Params params) throws VantarException {
        return Db.modelMongo.getById(params, new RadioMetricComplain.Viewable());
    }

    public static PageData search(Params params) throws VantarException {
        return Db.modelMongo.search(params, new RadioMetricComplain.Viewable());
    }

    public static Object assignable(Params params) throws VantarException {
        return getAssignQuery(params, true);
    }

    public static Object assigned(Params params) throws VantarException {
        return getAssignQuery(params, false);
    }

    private static PageData getAssignQuery(Params params, boolean assignable) throws VantarException {
        return Db.modelMongo.search(params, new RadioMetricComplain.Viewable()
            , new ModelCommon.QueryEvent() {
                @Override
                public void beforeQuery(QueryBuilder q) {
                    q.condition().equal("assignable", assignable);
                }

                @Override
                public void afterSetData(Dto dto) {

                }

                @Override
                public void afterSetData(Dto dto, List<?> list) {

                }
            }
        );
    }
}
