package com.tctools.business.dto.user;

import com.vantar.service.auth.CommonUserRole;


public enum Role implements CommonUserRole {

    ROOT,
    ADMIN,
    MANAGER,
    ENGINEER,
    TECHNICIAN,
    VENDOR,
}
