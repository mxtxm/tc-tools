package com.tctools.business.dto.project.radiometric.workflow;

public enum RadioMetricFlowState {

    // assignable
    Pending,

    // Role:MANAGER > Role:TECHNICIAN
    Planned,

    // Role:TECHNICIAN > Role:MANAGER
    // assignable
    Problematic,

    // Role:TECHNICIAN > Role:MANAGER
    Completed,

    // Role:MANAGER > Role:TECHNICIAN
    Revise,

    // Role:MANAGER > nothing
    Terminated,

    // Role:MANAGER > Role:VENDOR
    Verified,

    // Role:VENDOR > Role:MANAGER
    // assignable
    Returned,

    // Role:VENDOR
    Approved,
}