package com.tctools.business.dto.project.hseaudit;


public enum HseAuditFlowState {

    // assignable
    // > Planned (assign)
    Pending,

    // Role:MANAGER > Role:TECHNICIAN
    Planned,

    // schedule date is expired
    // assignable
    Expired,

    // BACKEND > APP
    // data is not complete yet
    // > Approve, Terminate
    Incomplete,

    // Role:TECHNICIAN > Role:MANAGER
    // > Terminated, Approved
    Restricted,
    // > Terminated, Approved
    Completed,

    // Role:MANAGER
    // assignable
    // > Approved
    Terminated,

    // Role:MANAGER
    // > Terminated
    PreApproved,

    // Role:MANAGER
    // > Terminated
    Approved,
}