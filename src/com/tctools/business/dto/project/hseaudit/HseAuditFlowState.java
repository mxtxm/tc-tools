package com.tctools.business.dto.project.hseaudit;


public enum HseAuditFlowState {

    // assignable
    // > Planned (assign)
    Pending,
    Planned,
    Expired,
    Incomplete,
    Restricted,
    Completed,
    Terminated,
    PreApproved,
    Approved,
    MCI_Approve,
    MCI_Reject,
}