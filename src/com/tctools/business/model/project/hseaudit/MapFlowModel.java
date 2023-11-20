package com.tctools.business.model.project.hseaudit;

import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.project.map.hseaudit.HseAuditMapFlow;
import com.tctools.business.dto.user.User;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.business.*;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.PageData;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import java.util.*;


public class MapFlowModel {

    public static List<HseAuditMapFlow> searchForMap(Params params) throws VantarException {
        PageData data = CommonModelMongo.search(
            params,
            new HseAuditQuestionnaire.ViewableTiny()
        );

        List<HseAuditMapFlow> items = new ArrayList<>(data.data.size());
        for (Dto dto : data.data) {
            HseAuditQuestionnaire.ViewableTiny flow = (HseAuditQuestionnaire.ViewableTiny) dto;
            if (flow.site.location == null || flow.site.location.isEmpty()) {
                continue;
            }
            items.add(new HseAuditMapFlow(flow));
        }
        return items;
    }

    public static ResponseMessage assign(Params params, User assignor) throws VantarException {
        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow = CommonModelMongo.getById(params, flow);

        flow.assigneeId = params.getLong("assigneeId");
        if (NumberUtil.isIdInvalid(flow.assigneeId)) {
            throw new InputException(AppLangKey.INVALID_ASSIGNEE);
        }

        flow.subContractorId = params.getLong("subContractorId");
        if (NumberUtil.isIdInvalid(flow.assigneeId)) {
            throw new InputException(VantarKey.INVALID_ID, "id (HseAuditQuestionnaire.subContractorId)");
        }

        String activity = params.getString("activity");
        if (StringUtil.isEmpty(activity)) {
            throw new InputException(VantarKey.REQUIRED, "activity");
        }
        flow.activity = HseAuditActivity.valueOf(activity);

        flow.scheduledDateTimeFrom = params.getDateTime("scheduledDateTimeFrom");
        flow.scheduledDateTimeTo = params.getDateTime("scheduledDateTimeTo");
        if (flow.scheduledDateTimeFrom == null || flow.scheduledDateTimeTo == null) {
            throw new InputException(VantarKey.REQUIRED, "scheduledDateTimeFrom, scheduledDateTimeTo");
        }

        flow.assignorId = assignor.getId();
        flow.lastState = HseAuditFlowState.Planned;
        flow.lastStateDateTime = new DateTime();
        flow.assignDateTime = flow.lastStateDateTime;
        flow.state.add(new State(flow.lastState, flow.lastStateDateTime, assignor));

        return CommonModelMongo.update(flow);
    }

    public static ResponseMessage removeAssign(Params params, User remover) throws VantarException {
        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow.id = params.getLong("id");
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (HseAuditQuestionnaire.id)");
        }

        flow = CommonModelMongo.getById(params, flow);

        flow.assignable = true;
        flow.copyable = false;
        flow.assigneeId = null;
        flow.assignorId = null;
        flow.assignDateTime = null;
        flow.scheduledDateTimeFrom = null;
        flow.scheduledDateTimeTo = null;
        flow.auditDateTime = null;
        flow.answers = null;
        flow.inCompleteImages = null;
        flow.criticalNoCount = null;
        flow.majorNoCount = null;
        flow.minorNoCount = null;
        flow.isFailed = null;

        flow.lastState = HseAuditFlowState.Pending;
        flow.lastStateDateTime = new DateTime();
        flow.state.add(new State(flow.lastState, flow.lastStateDateTime, remover));

        flow.setNullProperties(
            "assigneeId",
            "assignorId",
            "assignDateTime",
            "scheduledDateTimeFrom",
            "scheduledDateTimeTo",
            "auditDateTime",
            "answers",
            "inCompleteImages",
            "criticalNoCount",
            "majorNoCount",
            "minorNoCount",
            "isFailed"
        );

        return CommonModelMongo.update(flow);
    }

    public static ResponseMessage createChild(Params params) throws VantarException {
        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow = CommonModelMongo.getById(params, flow);

        Long assigneeId = flow.assigneeId;

        if (flow.parents == null) {
            flow.parents = new ArrayList<>();
        }
        flow.parents.add(flow.id);
        flow.id = null;
        flow.copyable = false;
        flow.assignable = true;
        flow.assignorId = null;
        flow.assigneeId = null;
        flow.assignDateTime = null;
        flow.scheduledDateTimeFrom = null;
        flow.scheduledDateTimeTo = null;
        flow.auditDateTime = null;
        flow.answers = null;
        flow.inCompleteImages = null;
        flow.criticalNoCount = null;
        flow.majorNoCount = null;
        flow.minorNoCount = null;
        flow.isFailed = null;

        flow.lastState = HseAuditFlowState.Pending;
        flow.lastStateDateTime = new DateTime();
        flow.state = new ArrayList<>();
        flow.state.add(new State(flow.lastState, flow.lastStateDateTime));

        return CommonModelMongo.insert(flow);
    }
}