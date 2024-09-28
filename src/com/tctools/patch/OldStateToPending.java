package com.tctools.patch;

import com.tctools.business.dto.project.radiometric.workflow.*;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.database.query.*;
import com.vantar.exception.VantarException;
import com.vantar.service.log.ServiceLog;
import com.vantar.service.patch.*;
import com.vantar.util.bool.BoolUtil;
import com.vantar.web.WebUi;

@PatchDelayed
public class OldStateToPending implements Patcher.PatchInterface {

    @Override
    public void setUi(WebUi webUi) {

    }

    @Override
    public Patcher.Result run() {
        Patcher.Result result = new Patcher.Result();

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition(QueryOperator.OR)
            .equal("comments", "OLD WORKFLOW")
            .equal("comments", "CONVERT MSS MATCH")
            .equal("comments", "MANUALLY APPROVED");

        try {
            Db.modelMongo.forEach(q, dto -> {
                RadioMetricFlow flow = (RadioMetricFlow) dto;
                if (flow.complain != null) {
                    ServiceLog.log.error("----> {} {}", flow.id, flow.site.code);
                    return true;
                }
                if (BoolUtil.isTrue(flow.isCc)) {
                    ServiceLog.log.error("----> {} {}", flow.id, flow.site.code);
                    return true;
                }
                if (flow.measurementUrl100 != null) {
                    ServiceLog.log.error("----> {} {}", flow.id, flow.site.code);
                    return true;
                }
                flow.lastState = RadioMetricFlowState.Pending;
                flow.assignable = true;
                flow.assignorId = null;
                flow.assigneeId = null;
                flow.assignDateTime = null;
                flow.measurementDateTime = null;
                flow.comments = "OLD WORKFLOW";
                flow.setNullProperties("assignorId", "assigneeId", "assignDateTime", "measurementDateTime");
                try {
                    Db.modelMongo.update(new ModelCommon.Settings(dto).dtoHasFullData(true).mutex(false).logEvent(false));
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
                return true;
            });
        } catch (Exception e) {
            result.addFail(e).countFail();
        }
        return result;
    }
}
