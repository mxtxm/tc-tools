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
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import java.util.*;


public class ComplainModel {

    public static ResponseMessage insert(Params params, User user) throws VantarException {
        RadioMetricComplain complain = new RadioMetricComplain();
        complain.creatorId = user.id;
        return ModelMongo.insert(params, complain, new CommonModel.WriteEvent() {

            @Override
            public void beforeSet(Dto dto) {

            }

            @Override
            public void beforeWrite(Dto dto) throws VantarException {
                RadioMetricComplain complain = (RadioMetricComplain) dto;
                completeData(complain);
                uploadImage(params, complain);
            }

            @Override
            public void afterWrite(Dto dto) {

            }
        });
    }

    public static ResponseMessage update(Params params) throws VantarException {
        RadioMetricComplain complain = ModelMongo.getById(params, new RadioMetricComplain());

        return ModelMongo.update(params, complain, new CommonModel.WriteEvent() {

            @Override
            public void beforeSet(Dto dto) {

            }

            @Override
            public void beforeWrite(Dto dto) throws VantarException {
                RadioMetricComplain complain = (RadioMetricComplain) dto;
                if (NumberUtil.isIdInvalid(complain.id)) {
                    throw new InputException(VantarKey.INVALID_ID);
                }
                completeData(complain);
                uploadImage(params, complain);
            }

            @Override
            public void afterWrite(Dto dto) throws VantarException {
                RadioMetricComplain complain = (RadioMetricComplain) dto;
                if (NumberUtil.isIdInvalid(complain.workFlowId)) {
                    return;
                }

                RadioMetricFlow flow = new RadioMetricFlow();
                flow.id = complain.workFlowId;
                flow.complain = complain;
                ModelMongo.update(flow);
            }
        });
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
        RadioMetricComplain complain = ModelMongo.getById(params, new RadioMetricComplain());

        try {
            RadioMetricFlow r = new RadioMetricFlow();
            r.id = complain.workFlowId;
            if (NumberUtil.isIdValid(r.id)) {
                ModelMongo.deleteById(r);
            }
        } catch (VantarException ignore) {

        }

        return ModelMongo.deleteById(complain);
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
                site = ModelMongo.getById(site);
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
            site = ModelMongo.getFirst(q);
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
        complain.siteId = ModelMongo.insert(site).dto.getId();
    }

    public static RadioMetricComplain.Viewable get(Params params) throws VantarException {
        return ModelMongo.getById(params, new RadioMetricComplain.Viewable());
    }

    public static PageData search(Params params) throws VantarException {
        return ModelMongo.search(params, new RadioMetricComplain.Viewable());
    }

    public static Object assignable(Params params) throws VantarException {
        return getAssignQuery(params, true);
    }

    public static Object assigned(Params params) throws VantarException {
        return getAssignQuery(params, false);
    }

    private static PageData getAssignQuery(Params params, boolean assignable) throws VantarException {
        return ModelMongo.search(params, new RadioMetricComplain.Viewable()
            , new CommonModel.QueryEvent() {
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
