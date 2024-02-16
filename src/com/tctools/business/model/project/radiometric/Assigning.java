package com.tctools.business.model.project.radiometric;

import com.tctools.business.dto.project.radiometric.complain.*;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.Site;
import com.tctools.business.dto.user.User;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.util.SendMessage;
import com.vantar.business.*;
import com.vantar.database.nosql.mongo.Mongo;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.service.log.ServiceLog;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import java.util.*;


public class Assigning {

    public static ResponseMessage assignComplain(Params params, User assignor) throws VantarException {
        DateTime now = new DateTime();

        RadioMetricFlow flow = new RadioMetricFlow();
        flow.assigneeId = params.getLong("assigneeId");
        flow.complain = new RadioMetricComplain();
        flow.complain.id = params.getLong("complainId");
        flow.assignDateTime = now;
        flow.lastStateDateTime = now;

        CommonModel.validateRequired("complain.id", flow.complain.id, "assigneeId", flow.assigneeId);

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().equal("complain.id", flow.complain.id);
        if (ModelMongo.exists(q)) {
            ServiceLog.error(Assigning.class, "Duplicate complain assign attempt", flow.complain.id);
            throw new InputException(AppLangKey.DUPLICATE, "radiometric flow for complain", flow.complain.id);
        }

        flow.complain = ModelMongo.getById(flow.complain, params.getLang());

        Site site = new Site();
        site.id = flow.complain.siteId;
        site = ModelMongo.getById(site, params.getLang());

        if (ComplainType.Normal.equals(flow.complain.type)) {
            q = new QueryBuilder(new RadioMetricFlow());
            q.condition().equal("site.code", site.code);
            flow.reRadioMetric = ModelMongo.exists(q);
        }

        flow.site = site;
        flow.assignorId = assignor.getId();
        flow.provinceId = flow.complain.provinceId;
        flow.cityId = flow.complain.cityId;
        flow.siteAddress = site.address == null ? flow.complain.address : site.address;
        flow.siteLocation = site.location == null || site.location.isEmpty() ?  flow.complain.location : site.location;
        flow.sectors = site.sectors;

        flow.state = new ArrayList<>(2);
        flow.lastStateDateTime = now;
        flow.lastState = RadioMetricFlowState.Planned;
        // 1 - Pending
        State stateA = new State(RadioMetricFlowState.Pending, flow.complain.complainTime);
        flow.state.add(stateA);
        // 2- Planned
        User assignee = Services.get(ServiceDtoCache.class).getDto(User.class, flow.assigneeId);
        State stateB = new State(flow.lastState, params.getString("comments"));
        stateB.assignorId = assignor.id;
        stateB.assignorName = assignor.fullName;
        stateB.assigneeId = assignee.id;
        stateB.assigneeName = assignee.fullName;
        flow.state.add(stateB);

        flow.complain.assignable = false;
        flow.complain.assigneeId = flow.assigneeId;

        RadioMetricComplain complainToUpdate = new RadioMetricComplain();
        complainToUpdate.id = flow.complain.id;
        complainToUpdate.assignable = false;
        complainToUpdate.assignTime = now;

        try {
            flow.id = Mongo.Sequence.getNext(flow);
            flow.complain.workFlowId = flow.id;
            ModelMongo.update(flow.complain);
            flow.setClearIdOnInsert(false);
            ModelMongo.insert(flow);

            if (StringUtil.isNotEmpty(assignee.mobile)) {
                SendMessage.sendSms(
                    assignee.mobile,
                    "با سلام" + "\n" + " سایت شکایتی " + flow.site.code + " برای پرتوسنجی به کاربر شما اساین شد "
                );
            }

            return ResponseMessage.success(VantarKey.INSERT_SUCCESS, flow.complain.workFlowId);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.INSERT_FAIL);
        }
    }

    public static ResponseMessage assign(Params params, User assignor) throws VantarException {
        List<Long> flowIds = params.getLongList("ids");
        if (flowIds == null || flowIds.isEmpty()) {
            throw new InputException(VantarKey.INVALID_ID, "ids");
        }
        Long assigneeId = params.getLong("assigneeId");
        if (NumberUtil.isIdInvalid(assigneeId)) {
            throw new InputException(AppLangKey.INVALID_ASSIGNEE);
        }

        User assignee = Services.get(ServiceDtoCache.class).getDto(User.class, assigneeId);
        DateTime now = new DateTime();

        State state = new State(RadioMetricFlowState.Planned, now, params.getString("comments"));
        state.assignorId = assignor.id;
        state.assignorName = assignor.fullName;
        state.assigneeId = assignee.id;
        state.assigneeName = assignee.fullName;

        for (long id : flowIds) {
            RadioMetricFlow flow = new RadioMetricFlow();
            flow.id = id;
            flow = ModelMongo.getById(flow);
            flow.assigneeId = assigneeId;
            flow.assignorId = assignor.getId();
            flow.sectors = flow.site.sectors;
            flow.lastStateDateTime = now;
            flow.assignDateTime = now;
            flow.lastState = RadioMetricFlowState.Planned;
            flow.state.add(state);
            ModelMongo.update(flow);
        }

        if (StringUtil.isNotEmpty(assignee.mobile)) {
            SendMessage.sendSms(
                assignee.mobile,
                "با سلام" + "\n" + flowIds.size() + " سایت برای پرتوسنجی به کاربر شما اساین شد "
            );
        }

        return ResponseMessage.success(VantarKey.UPDATE_SUCCESS);
    }

    public static ResponseMessage assignRemove(Params params, User remover) throws VantarException {
        try {
            RadioMetricFlow flow = ModelMongo.getById(params, new RadioMetricFlow());
            if (!RadioMetricComplain.isEmpty(flow.complain)) {
                ModelMongo.deleteById(flow);

                RadioMetricComplain complain = new RadioMetricComplain();
                complain.id = flow.complain.id;
                complain = ModelMongo.getById(complain, params.getLang());
                complain.assignable = true;
                complain.assigneeId = null;
                complain.workFlowId = null;
                complain.assignTime = null;
                complain.setNullProperties("assigneeId", "workFlowId", "assignTime");
                ModelMongo.update(complain);
                return ResponseMessage.success(VantarKey.DELETE_SUCCESS);
            }

            flow.setNullProperties("assigneeId", "assignorId", "assignDateTime", "measurementDateTime");
            flow.assignable = true;
            flow.lastStateDateTime = new DateTime();
            flow.lastState = RadioMetricFlowState.Pending;
            flow.logDateTime100 = null;
            flow.logDateTime150 = null;
            flow.logDateTime170 = null;

            User assignee = Services.get(ServiceDtoCache.class).getDto(User.class, flow.assigneeId);
            State state = new State(flow.lastState, params.getString("comments"));
            state.assignorId = remover.id;
            state.assignorName = remover.fullName;
            state.assigneeId = flow.assigneeId;
            state.assigneeName = assignee.fullName;
            flow.state.add(state);

            return ModelMongo.update(flow);

        } catch (NoContentException e) {
            QueryBuilder q = new QueryBuilder(new RadioMetricComplain());
            q.condition().equal("workFlowId", params.getLong("id"));
            RadioMetricComplain complain = ModelMongo.getFirst(q);
            complain.assignable = true;
            complain.assigneeId = null;
            complain.workFlowId = null;
            complain.assignTime = null;
            complain.setNullProperties("assigneeId", "workFlowId", "assignTime");
            return ModelMongo.update(complain);
        }
    }
}
