package com.tctools.business.dto.project.hseaudit;

import com.tctools.business.dto.user.User;
import com.vantar.database.dto.*;
import com.vantar.util.datetime.DateTime;

@NoStore
public class State extends DtoBase {

    public HseAuditFlowState state;
    public DateTime dateTime;
    public String comments;
    public Long userId;
    public String userName;


    public State() {

    }

    public State(HseAuditFlowState state, DateTime dateTime, User user, String comments) {
        this.state = state;
        this.dateTime = dateTime;
        this.comments = comments;
        this.userId = user.id;
        this.userName = user.fullName;
    }

    public State(HseAuditFlowState state, DateTime dateTime, User user) {
        this.state = state;
        this.dateTime = dateTime;
        this.userId = user.id;
        this.userName = user.fullName;
    }

    public State(HseAuditFlowState state, DateTime dateTime) {
        this.state = state;
        this.dateTime = dateTime;
    }
}
