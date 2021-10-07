package com.tctools.business.model.project.hseaudit;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.project.map.hseaudit.HseAuditMapFlow;
import com.tctools.business.dto.user.User;
import com.tctools.business.repo.user.UserRepo;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.business.CommonRepoMongo;
import com.vantar.database.query.QueryData;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.object.ObjectUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import org.slf4j.*;
import java.util.*;


public class MapFlowModel {

    private static final Logger log = LoggerFactory.getLogger(WorkFlowModel.class);

    @SuppressWarnings({"unchecked"})
    public static List<HseAuditMapFlow> searchForMap(Params params) throws ServerException, NoContentException, InputException {
        QueryData queryData = params.getQueryData();
        if (queryData == null) {
            throw new InputException(VantarKey.NO_SEARCH_COMMAND);
        }
        queryData.setDto(new HseAuditQuestionnaire(), new HseAuditQuestionnaire.ViewableTiny());

        List<HseAuditMapFlow> items = new ArrayList<>();

        try {
            for (HseAuditQuestionnaire.ViewableTiny flow : (List<HseAuditQuestionnaire.ViewableTiny>) CommonRepoMongo.search(queryData, params.getLang())) {
                if (flow.site.location == null || flow.site.location.isEmpty()) {
                    continue;
                }
                items.add(new HseAuditMapFlow(flow));
            }
        } catch (DatabaseException e) {
            log.error("!", e);
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        return items;
    }

    public static ResponseMessage assign(Params params, User assignor) throws InputException, ServerException, NoContentException {
        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow.id = params.getLong("id");
        if (ObjectUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (HseAuditQuestionnaire.id)");
        }

        try {
            flow = CommonRepoMongo.getFirst(flow);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        flow.assigneeId = params.getLong("assigneeId");
        if (ObjectUtil.isIdInvalid(flow.assigneeId)) {
            throw new InputException(AppLangKey.INVALID_ASSIGNEE);
        }

        flow.subContractorId = params.getLong("subContractorId");
        if (ObjectUtil.isIdInvalid(flow.assigneeId)) {
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

        try {
            CommonRepoMongo.update(flow);
            UserRepo.setDataUpdated(flow.assigneeId, ProjectType.HseAudit.name());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.UPDATE_FAIL);
        }
        return new ResponseMessage(VantarKey.UPDATE_SUCCESS);
    }

    public static ResponseMessage removeAssign(Params params, User remover) throws InputException, ServerException, NoContentException {
        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow.id = params.getLong("id");
        if (ObjectUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (HseAuditQuestionnaire.id)");
        }

        try {
            flow = CommonRepoMongo.getFirst(flow);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        Long assigneeId = flow.assigneeId;

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

        try {
            CommonRepoMongo.update(flow);
            UserRepo.setDataUpdated(assigneeId, ProjectType.HseAudit.name());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.UPDATE_FAIL);
        }

        return new ResponseMessage(VantarKey.UPDATE_SUCCESS);
    }

    public static ResponseMessage createChild(Params params) throws InputException, ServerException, NoContentException {
        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow.id = params.getLong("id");
        if (ObjectUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (HseAuditQuestionnaire.id)");
        }

        try {
            flow = CommonRepoMongo.getFirst(flow);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

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

        try {
            CommonRepoMongo.insert(flow);
            UserRepo.setDataUpdated(assigneeId, ProjectType.HseAudit.name());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.UPDATE_FAIL);
        }

        return new ResponseMessage(VantarKey.INSERT_SUCCESS, flow.getId());
    }
}