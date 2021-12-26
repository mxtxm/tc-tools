package com.tctools.business.model.project.hseaudit;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.user.*;
import com.tctools.business.repo.user.UserRepo;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.Param;
import com.tctools.common.util.SendMessage;
import com.vantar.business.*;
import com.vantar.database.common.ValidationError;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.service.log.LogEvent;
import com.vantar.util.collection.CollectionUtil;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class WorkFlowModel {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final String SMS_NOTIFY_NUMBERS = "09126214967,09129346154,09359947922,09197148361";


    public static ResponseMessage submit(Params params, User user) throws ServerException, InputException, NoContentException {
        // get flow
        FlowParams flowParams;
        try {
            flowParams = params.getJson(FlowParams.class);
        } catch (Exception e) {
            throw new InputException(VantarKey.INVALID_ID, "id (HseAuditQuestionnaire)");
        }
        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow.id = flowParams.id;
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (HseAuditQuestionnaire)");
        }

        try {
            flow = CommonRepoMongo.getById(flow, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
        flow.answers = null;
        flow.inCompleteImages = new HashSet<>();
        flow.lastStateDateTime = new DateTime();
        flow.auditDateTime = flow.lastStateDateTime;

        // set state
        flow.lastState = flowParams.notAllowed == null || !flowParams.notAllowed ?
            HseAuditFlowState.Incomplete :
            HseAuditFlowState.Restricted;
        flow.state.add(new State(flow.lastState, flow.lastStateDateTime, user));

        // get questions
        ServiceDtoCache cache = Services.get(ServiceDtoCache.class);
        Map<Long, HseAuditQuestion> questions = new HashMap<>();
        for (HseAuditQuestion question : cache.getList(HseAuditQuestion.class)) {
            questions.put(question.id, question);
        }

        return CommonModelMongo.updateJson(params, flow, new CommonModel.WriteEvent() {
            @Override
            public void beforeWrite(Dto dto) throws InputException {
                HseAuditQuestionnaire flow = (HseAuditQuestionnaire) dto;

                if (flow.answers == null) {
                    throw new InputException(VantarKey.REQUIRED, "answers");
                }

                ListIterator<HseAuditAnswer> it = flow.answers.listIterator();
                while (it.hasNext()) {
                    HseAuditAnswer answer = it.next();
                    if (!questions.containsKey(answer.questionId)) {
                        it.remove();
                        continue;
                    }
                    questions.remove(answer.questionId);

                    if (StringUtil.isNotEmpty(answer.imageName)) {
                        flow.inCompleteImages.add(answer.imageName);
                    }

                    if (answer.imageNames != null) {
                        flow.inCompleteImages.addAll(answer.imageNames);
                    }
                }

                for (Map.Entry<Long, HseAuditQuestion> q : questions.entrySet()) {
                    HseAuditAnswer answer = new HseAuditAnswer();
                    answer.questionId = q.getKey();
                    answer.question = q.getValue();
                    flow.answers.add(answer);
                }
            }

            @Override
            public void afterWrite(Dto dto) {
                HseAuditQuestionnaire flow = (HseAuditQuestionnaire) dto;
                try {
                    UserRepo.setDataUpdated(flow.assigneeId, ProjectType.HseAudit.name());
                } catch (DatabaseException ignore) {

                }
            }
        });
    }

    public static ResponseMessage imageUpload(Params params) throws ServerException, InputException, AuthException {
        Params.Uploaded uploaded = params.upload(Param.FILE_UPLOAD);

        if (!uploaded.isUploaded() || uploaded.isIoError()) {
            throw new InputException(VantarKey.REQUIRED, "file");
        }

        List<ValidationError> errors = new ArrayList<>();
        if (!uploaded.isType("jpeg")) {
            errors.add(new ValidationError("file", VantarKey.FILE_TYPE, "jpeg"));
        }
        if (uploaded.getSize() < Param.FILE_IMAGE_MIN_SIZE || uploaded.getSize() > Param.FILE_IMAGE_MAX_SIZE) {
            errors.add(
                new ValidationError(
                    "file",
                    VantarKey.FILE_SIZE,
                    Param.FILE_IMAGE_MIN_SIZE / 1024 + "KB ~ " + Param.FILE_IMAGE_MAX_SIZE / 1024 + "KB"
                )
            );
        }
        if (!errors.isEmpty()) {
            throw new InputException(errors);
        }

        String originalFilename = uploaded.getOriginalFilename();
        String tempPath = Param.TEMP_DIR + originalFilename;
        uploaded.moveTo(tempPath);

        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.TECHNICIAN);
        user.projectAccess(ProjectType.HseAudit);

        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow.id = params.getLong("id");
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (questionnaireId)");
        }

        String destinationPath = null;

        lock.lock();
        try {
            flow = CommonRepoMongo.getById(flow, params.getLang());
            if (flow.answers == null) {
                throw new InputException(VantarKey.REQUIRED, "answers");
            }

            if (flow.inCompleteImages != null) {
                for (String item : flow.inCompleteImages) {
                    if (item.contains(originalFilename)) {
                        flow.inCompleteImages.remove(item);
                        break;
                    }
                }
            }

            if (flow.inCompleteImages == null || flow.inCompleteImages.isEmpty()) {
                flow.inCompleteImages = null;
                flow.lastState = HseAuditFlowState.Completed;
                flow.lastStateDateTime = new DateTime();
                flow.state.add(new State(HseAuditFlowState.Completed, flow.lastStateDateTime, user));
            }

            for (HseAuditAnswer answer : flow.answers) {
                if (answer.imageName != null && StringUtil.containsIgnoreCase(answer.imageName, originalFilename)) {
                    destinationPath = answer.getNextImagePath(flow);
                    FileUtil.move(tempPath, destinationPath);
                    break;
                }

                if (answer.imageNames == null) {
                    continue;
                }
                boolean brk = false;
                for (String imageName : answer.imageNames) {
                    if (imageName != null && StringUtil.containsIgnoreCase(imageName, originalFilename)) {
                        destinationPath = answer.getNextImagePath(flow);
                        FileUtil.move(tempPath, destinationPath);
                        brk = true;
                        break;
                    }
                }
                if (brk) {
                    break;
                }
            }

            CommonRepoMongo.update(flow);
            notifyFailed(flow);

        } catch (DatabaseException e) {
            LogEvent.error("AppImageUpload", "db error", flow.id, tempPath, uploaded.getOriginalFilename(), uploaded.getSize(), uploaded.getType());
            throw new ServerException(VantarKey.FETCH_FAIL);
        } catch (NoContentException e) {
            LogEvent.error("AppImageUpload", "invalid id (no data)", flow.id, tempPath, uploaded.getOriginalFilename(), uploaded.getSize(), uploaded.getType());
            throw new InputException(AppLangKey.UNKNOWN_FILE, originalFilename);
        } finally {
            lock.unlock();
        }

        return new ResponseMessage(
            VantarKey.UPDATE_SUCCESS,
            StringUtil.replace(destinationPath, Param.HSE_AUDIT_FILES, Param.HSE_AUDIT_URL)
        );
    }

    private static void notifyFailed(HseAuditQuestionnaire flow) {
        if (!(flow.lastState == HseAuditFlowState.Completed && flow.isFailed)) {
            return;
        }
        SendMessage.sendSms(
            SMS_NOTIFY_NUMBERS,
            flow.site.code + "عدم رعایت اصول ایمنی در سایت "
                + "\nCritical: " + CollectionUtil.join(flow.criticalNoQuestions, ", ")
                + "\nMajor: " + CollectionUtil.join(flow.majorNoQuestions, ", ")
        );
    }

    public static ResponseMessage imageUploadDirect(Params params) throws ServerException, InputException, AuthException {
        Params.Uploaded uploaded = params.upload(Param.FILE_UPLOAD);
        if (!uploaded.isUploaded() || uploaded.isIoError()) {
            throw new InputException(VantarKey.REQUIRED, "file");
        }

        List<ValidationError> errors = new ArrayList<>();
        if (!uploaded.isType("jpeg")) {
            errors.add(new ValidationError("file", VantarKey.FILE_TYPE, "jpeg"));
        }
        if (uploaded.getSize() < Param.FILE_IMAGE_MIN_SIZE || uploaded.getSize() > Param.FILE_IMAGE_MAX_SIZE) {
            errors.add(
                new ValidationError(
                    "file",
                    VantarKey.FILE_SIZE,
                    Param.FILE_IMAGE_MIN_SIZE/1024 + "KB ~ " + Param.FILE_IMAGE_MAX_SIZE/1024 + "KB"
                )
            );
        }
        if (!errors.isEmpty()) {
            throw new InputException(errors);
        }

        String tempPath = FileUtil.getUniqueName(Param.TEMP_DIR);
        uploaded.moveTo(tempPath);

        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);

        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow.id = params.getLong("id");
        Long questionId = params.getLong("questionId");
        if (NumberUtil.isIdInvalid(flow.id) || NumberUtil.isIdInvalid(questionId)) {
            throw new InputException(VantarKey.INVALID_ID, "id (questionnaireId) / questionId");
        }

        String destinationPath = null;

        lock.lock();
        try {
            flow = CommonRepoMongo.getById(flow, params.getLang());
            if (flow.answers == null) {
                throw new InputException(VantarKey.REQUIRED, "answers in db");
            }
            for (HseAuditAnswer answer : flow.answers) {
                if (questionId.equals(answer.questionId)) {
                    destinationPath = answer.getNextImagePath(flow);
                    FileUtil.move(tempPath, destinationPath);
                    break;
                }
            }
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        } catch (NoContentException e) {
            throw new InputException(AppLangKey.UNKNOWN_FILE, uploaded.getOriginalFilename());
        } finally {
            lock.unlock();
        }

        return new ResponseMessage(
            VantarKey.UPDATE_SUCCESS,
            StringUtil.replace(destinationPath, Param.HSE_AUDIT_FILES, Param.HSE_AUDIT_URL)
        );
    }

    public static ResponseMessage delete(Params params) throws InputException, ServerException {
        return CommonModelMongo.delete(params, new HseAuditQuestionnaire());
    }

    public static ResponseMessage update(Params params) throws ServerException, InputException, NoContentException {
        // get flow
        FlowParams flowParams;
        try {
            flowParams = params.getJson(FlowParams.class);
        } catch (Exception e) {
            throw new InputException(VantarKey.INVALID_ID, "id (HseAuditQuestionnaire)");
        }
        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow.id = flowParams.id;
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (HseAuditQuestionnaire)");
        }

        try {
            flow = CommonRepoMongo.getById(flow, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        return CommonModelMongo.updateJsonStrict(params, flow);
    }

    public static ResponseMessage updateState(Params params, User user) throws InputException, ServerException, NoContentException {
        HseAuditFlowState state;
        try {
            state = HseAuditFlowState.valueOf(params.getString("state"));
        } catch (IllegalArgumentException e) {
            throw new InputException(AppLangKey.INVALID_STATE);
        }
        return updateState(params.getLong("id"), state, user, params.getString("comments"));
    }

    private static ResponseMessage updateState(Long id, HseAuditFlowState state, User user, String comments)
        throws InputException, ServerException, NoContentException {

        if (user.role == Role.ENGINEER && state == HseAuditFlowState.Approved) {
            throw new InputException(AppLangKey.INVALID_STATE);
        }

        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow.id = id;
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (HseAuditQuestionnaire.id)");
        }

        try {
            flow = CommonRepoMongo.getFirst(flow);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        int i = flow.state.size();
        if (i > 0 && flow.state.get(i-1).state.equals(state)) {
            throw new InputException(AppLangKey.INVALID_STATE);
        }

        if (state == HseAuditFlowState.Completed) {
            flow.inCompleteImages = null;
        }

        flow.lastState = state;
        flow.lastStateDateTime = new DateTime();
        flow.state.add(new State(state, flow.lastStateDateTime, user, comments));

        try {
            CommonRepoMongo.update(flow);
            UserRepo.setDataUpdated(flow.assigneeId, ProjectType.HseAudit.name());
            return new ResponseMessage(VantarKey.UPDATE_SUCCESS);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.INSERT_FAIL);
        }
    }

    public static Object search(Params params) throws ServerException, NoContentException, InputException {
        QueryData queryData = params.getQueryData();
        if (queryData == null) {
            throw new InputException(VantarKey.NO_SEARCH_COMMAND);
        }
        queryData.setDto(new HseAuditQuestionnaire(), new HseAuditQuestionnaire.Viewable());
        try {
            return CommonRepoMongo.search(queryData, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }

    public static List<HseAuditQuestionnaire.ViewableTiny> getAssigned(Params params, User user) throws ServerException, NoContentException {
        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire(), new HseAuditQuestionnaire.ViewableTiny());
        q   .sort("scheduledDateTimeFrom:asc")
            .condition().equal("assigneeId", user.id);

        try {
            return CommonRepoMongo.getData(q, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }

    public static HseAuditQuestionnaire.Viewable get(Params params) throws ServerException, NoContentException, InputException {
        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        flow.id = params.getLong("id");
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (HseAuditQuestionnaire.id)");
        }

        QueryBuilder q = new QueryBuilder(flow, new HseAuditQuestionnaire.Viewable());
        q.setConditionFromDtoEqualTextMatch();
        try {
            return CommonRepoMongo.getFirst(q, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }

    public static ResponseMessage deleteImage(Params params) throws InputException {
        String path = params.getString("path");
        if (StringUtil.isEmpty(path)) {
            throw new InputException(VantarKey.REQUIRED, "path");
        }
        FileUtil.removeFile(StringUtil.replace(path, Param.HSE_AUDIT_URL, Param.HSE_AUDIT_FILES));
        return new ResponseMessage(VantarKey.DELETE_SUCCESS);
    }


    private static class FlowParams {

        public Long id;
        public Boolean notAllowed;
    }
}