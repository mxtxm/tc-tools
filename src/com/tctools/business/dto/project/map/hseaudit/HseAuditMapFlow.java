package com.tctools.business.dto.project.map.hseaudit;

import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.project.map.*;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.object.ObjectUtil;


public class HseAuditMapFlow {

    public Long id;
    public Boolean copyable;
    public Boolean assignable;
    public SiteForMap site;
    public Long subContractorId;

    public UserForMap assignor;
    public UserForMap assignee;
    public HseAuditFlowState lastState;

    public DateTime lastStateDateTime;
    public DateTime assignDateTime;
    public DateTime scheduledDateTimeFrom;
    public DateTime scheduledDateTimeTo;
    public DateTime auditDateTime;


    public HseAuditMapFlow(HseAuditQuestionnaire.ViewableTiny flow) {
        id = flow.id;
        assignable = flow.assignable;
        copyable = flow.copyable;

        site = new SiteForMap(flow.site);
        if (flow.subContractor != null) {
            subContractorId = flow.subContractor.id;
        }

        if (flow.assignor != null) {
            assignor = new UserForMap(flow.assignor);
        }
        if (flow.assignee != null) {
            assignee = new UserForMap(flow.assignee);
        }
        lastState = flow.lastState;

        lastStateDateTime = flow.lastStateDateTime;
        assignDateTime = flow.assignDateTime;
        scheduledDateTimeFrom = flow.scheduledDateTimeFrom;
        scheduledDateTimeTo = flow.scheduledDateTimeTo;
        auditDateTime = flow.auditDateTime;
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}
