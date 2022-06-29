package com.tctools.business.dto.project.radiometric.workflow;

import com.vantar.database.dto.*;
import com.vantar.util.datetime.DateTime;

@NoStore
public class State extends DtoBase {

    public RadioMetricFlowState state;
    public DateTime dateTime;
    public String comments;

    public Long assigneeId;
    public String assigneeName;

    public Long assignorId;
    public String assignorName;


    public State() {

    }

    public State(RadioMetricFlowState state) {
        this.state = state;
        dateTime = new DateTime();
    }

    public State(RadioMetricFlowState state, String comments) {
        this.state = state;
        dateTime = new DateTime();
        this.comments = comments;
    }

    public State(RadioMetricFlowState state, DateTime dateTime) {
        this.state = state;
        this.dateTime = dateTime;
    }

    public State(RadioMetricFlowState state, DateTime dateTime, String comments) {
        this.state = state;
        this.dateTime = dateTime;
        this.comments = comments;
    }
}
