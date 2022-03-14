package com.tctools.business.dto.project.hseaudit;

import com.tctools.business.dto.site.Site;
import com.tctools.business.dto.user.User;
import com.tctools.common.Param;
import com.vantar.database.dto.*;
import com.vantar.exception.ServiceException;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.datetime.DateTime;
import java.util.*;

@Mongo
@Index({"site.id:1", "notAllowed:1", "site.code:1", "subContractorId:1", "activity:1", "scheduled:1", "audit:1",
    "lastState:1", "scheduledDateTimeFrom:1"})
public class HseAuditQuestionnaire extends DtoBase {

    public Long id;

    @Required
    public Site site;
    //@Depends(SubContractor.class)
    public Long subContractorId;
    public HseAuditActivity activity;

    public List<Long> parents;

    // assign > > >
    @Default("true")
    public Boolean assignable;
    @Default("false")
    public Boolean copyable;
    //@Depends(User.class)
    public Long assignorId;
    //@Depends(User.class)
    public Long assigneeId;
    public List<State> state;
    public HseAuditFlowState lastState;

    @Timestamp
    public DateTime lastStateDateTime;
    @Timestamp
    public DateTime assignDateTime;
    @Timestamp
    public DateTime scheduledDateTimeFrom;
    @Timestamp
    public DateTime scheduledDateTimeTo;
    @Timestamp
    public DateTime auditDateTime;
    // < < < assign

    public List<HseAuditAnswer> answers;
    public Set<String> inCompleteImages;

    public Integer criticalNoCount;
    public Integer majorNoCount;
    public Integer minorNoCount;
    public Integer criticalYesCount;
    public Integer majorYesCount;
    public Integer minorYesCount;
    public Integer criticalNaCount;
    public Integer majorNaCount;
    public Integer minorNaCount;
    public Boolean isFailed;

    @NoStore
    public List<Integer> criticalNoQuestions;
    @NoStore
    public List<Integer> majorNoQuestions;


    public static boolean isAssignable(HseAuditFlowState state) {
        return state == HseAuditFlowState.Pending
            || state == HseAuditFlowState.Expired
            || state == HseAuditFlowState.Restricted;
    }

    public static boolean isCopyable(HseAuditFlowState state) {
        return state == HseAuditFlowState.Incomplete
            || state == HseAuditFlowState.Restricted
            || state == HseAuditFlowState.Terminated
            || state == HseAuditFlowState.PreApproved
            || state == HseAuditFlowState.Approved
            || state == HseAuditFlowState.Completed
            || state == HseAuditFlowState.Expired;
    }

    @Override
    public boolean beforeInsert() {
        return beforeUpdate();
    }

    @Override
    public boolean beforeUpdate() {
        if (lastState == HseAuditFlowState.Planned && scheduledDateTimeTo != null && scheduledDateTimeTo.isBefore(new DateTime())) {
            lastState = HseAuditFlowState.Expired;
        }
        assignable = isAssignable(lastState);
        copyable = isCopyable(lastState);

        criticalNoCount = 0;
        majorNoCount = 0;
        minorNoCount = 0;
        criticalYesCount = 0;
        majorYesCount = 0;
        minorYesCount = 0;
        criticalNaCount = 0;
        majorNaCount = 0;
        minorNaCount = 0;

        criticalNoQuestions = new ArrayList<>();
        majorNoQuestions = new ArrayList<>();

        if (answers != null) {
            answers.sort((a1, a2) ->
                a1.question == null || a2.question == null || a1.question.order == null || a2.question.order == null ?
                    0 : a1.question.order.compareTo(a2.question.order));

            ServiceDtoCache cache;
            try {
                cache = Services.get(ServiceDtoCache.class);
            } catch (ServiceException e) {
                return true;
            }
            int i = 0;
            ListIterator<HseAuditAnswer> it = answers.listIterator();
            HashSet<Long> questionIds = new HashSet<>(100);
            while (it.hasNext()) {
                HseAuditAnswer answer = it.next();
                if (questionIds.contains(answer.questionId)) {
                    it.remove();
                }
                questionIds.add(answer.questionId);

                ++i;
                answer.question = cache.getDto(HseAuditQuestion.class, answer.questionId);
                if (answer.question == null || answer.question.significance == null) {
                    continue;
                }

                switch (answer.question.significance) {
                    case Critical:
                        if (answer.answer == HseAuditAnswerOption.Yes) {
                            ++criticalYesCount;
                        } else if (answer.answer == HseAuditAnswerOption.No) {
                            ++criticalNoCount;
                            criticalNoQuestions.add(i);
                        } else {
                            ++criticalNaCount;
                        }
                        break;
                    case Major:
                        if (answer.answer == HseAuditAnswerOption.Yes) {
                            ++majorYesCount;
                        } else if (answer.answer == HseAuditAnswerOption.No) {
                            ++majorNoCount;
                            majorNoQuestions.add(i);
                        } else {
                            ++majorNaCount;
                        }
                        break;
                    case Minor:
                        if (answer.answer == HseAuditAnswerOption.Yes) {
                            ++minorYesCount;
                        } else if (answer.answer == HseAuditAnswerOption.No) {
                            ++minorNoCount;
                        } else {
                            ++minorNaCount;
                        }
                        break;
                }
            }
        }
        isFailed = criticalNoCount >= Param.HSE_CRITICAL_FAIL_THRESHOLD || majorNoCount >= Param.HSE_MAJOR_FAIL_THRESHOLD;
        return true;
    }


    // > > > Viewable


    @Storage("HseAuditQuestionnaire")
    public static class Viewable extends DtoBase {

        public Long id;

        public List<Long> parents;

        public Site.Viewable site;

        @FetchCache(dto = SubContractor.class, field = "subContractorId")
        public SubContractor.Viewable subContractor;
        public HseAuditActivity activity;

        // assign > > >
        public Boolean copyable;
        public Boolean assignable;
        @FetchCache("assignorId")
        public User assignor;
        @FetchCache("assigneeId")
        public User assignee;
        public List<State> state;
        public HseAuditFlowState lastState;

        public DateTime lastStateDateTime;
        public DateTime assignDateTime;
        public DateTime scheduledDateTimeFrom;
        public DateTime scheduledDateTimeTo;
        public DateTime auditDateTime;

        public String lastStateDateTimeFa;
        public String assignDateTimeFa;
        public String scheduledDateTimeFromFa;
        public String scheduledDateTimeToFa;
        public String auditDateTimeFa;
        // < < < assign

        public List<HseAuditAnswer> answers;
        public Set<String> inCompleteImages;

        public Integer criticalNoCount;
        public Integer majorNoCount;
        public Integer minorNoCount;
        public Boolean isFailed;

        @Override
        public void afterFetchData() {
            if (lastState == HseAuditFlowState.Planned && scheduledDateTimeTo != null && scheduledDateTimeTo.isBefore(new DateTime())) {
                lastState = HseAuditFlowState.Expired;
                assignable = true;
                copyable = true;
            }

            if (answers == null) {
                return;
            }
            for (HseAuditAnswer answer : answers) {
                answer.setImageUrl(this);
            }

            if (lastStateDateTime != null) {
                lastStateDateTimeFa = lastStateDateTime.formatter().getDateTimePersianHm();
            }
            if (assignDateTime != null) {
                assignDateTimeFa = assignDateTime.formatter().getDateTimePersianHm();
            }
            if (scheduledDateTimeFrom != null) {
                scheduledDateTimeFromFa = scheduledDateTimeFrom.formatter().getDateTimePersianHm();
            }
            if (scheduledDateTimeTo != null) {
                scheduledDateTimeToFa = scheduledDateTimeTo.formatter().getDateTimePersianHm();
            }
            if (auditDateTime != null) {
                auditDateTimeFa = auditDateTime.formatter().getDateTimePersianHm();
            }
        }
    }


    // > > > Tiny


    @Storage("HseAuditQuestionnaire")
    public static class ViewableTiny extends DtoBase {

        public Long id;

        public List<Long> parents;

        public Site.ViewableTiny site;
        @FetchCache(dto = SubContractor.class, field = "subContractorId")
        public SubContractor.Viewable subContractor;
        public HseAuditActivity activity;

        // assign > > >
        public Boolean copyable;
        public Boolean assignable;
        @FetchCache("assignorId")
        public User assignor;
        @FetchCache("assigneeId")
        public User assignee;
        public HseAuditFlowState lastState;

        public Long lastStateTimestamp;
        public DateTime lastStateDateTime;
        public Long assignTimestamp;
        public DateTime assignDateTime;
        public Long scheduledTimestampFrom;
        public DateTime scheduledDateTimeFrom;
        public Long scheduledTimestampTo;
        public DateTime scheduledDateTimeTo;
        public Long auditTimestamp;
        public DateTime auditDateTime;
        // < < < assign

        public Set<String> inCompleteImages;

        public Integer criticalNoCount;
        public Integer majorNoCount;
        public Integer minorNoCount;
        public Boolean isFailed;


        @Override
        public void afterFetchData() {
            if (lastState == HseAuditFlowState.Planned && scheduledDateTimeTo != null && scheduledDateTimeTo.isBefore(new DateTime())) {
                lastState = HseAuditFlowState.Expired;
                assignable = true;
                copyable = true;
            }

            if (lastStateDateTime != null) {
                lastStateTimestamp = lastStateDateTime.getAsTimestampMilli();
            }
            if (assignDateTime != null) {
                assignTimestamp = assignDateTime.getAsTimestampMilli();
            }
            if (assignDateTime != null) {
                assignTimestamp = assignDateTime.getAsTimestampMilli();
            }
            if (scheduledDateTimeFrom != null) {
                scheduledTimestampFrom = scheduledDateTimeFrom.getAsTimestampMilli();
            }
            if (scheduledDateTimeTo != null) {
                scheduledTimestampTo = scheduledDateTimeTo.getAsTimestampMilli();
            }
            if (auditDateTime != null) {
                auditTimestamp = auditDateTime.getAsTimestampMilli();
            }
        }
    }
}