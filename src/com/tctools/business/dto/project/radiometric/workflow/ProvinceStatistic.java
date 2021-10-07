package com.tctools.business.dto.project.radiometric.workflow;

import com.vantar.database.dto.*;


@Mongo
@Index({"userName:1", "userId:1", "yearMonth:1"})
public class ProvinceStatistic extends DtoBase {

    public Long id;
    public Long provinceId;
    public String provinceName;
    public Integer yearMonth;
    public Integer completed;
    public Integer verified;
    public Integer approved;
    public Integer total;


    public void setFirst(RadioMetricFlow flow) {
        if (approved == null) {
            approved = 0;
        }
        if (verified == null) {
            verified = 0;
        }
        if (completed == null) {
            completed = 0;
        }
        if (total == null) {
            total = 0;
        }
    }

    public void countState(RadioMetricFlow flow) {
        if (RadioMetricFlowState.Approved.equals(flow.lastState)) {
            ++approved;
        } else if (RadioMetricFlowState.Verified.equals(flow.lastState)) {
            ++verified;
        } else if (RadioMetricFlowState.Completed.equals(flow.lastState)) {
            ++completed;
        }
        ++total;
    }


    // > > > Viewable


    @Storage("ProvinceStatistic")
    public static class Viewable extends DtoBase {

        public String provinceName;
        public Integer yearMonth;
        public Integer completed;
        public Integer verified;
        public Integer approved;
        public Integer total;
    }
}