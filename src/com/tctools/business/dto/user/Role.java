package com.tctools.business.dto.user;

import com.vantar.service.auth.CommonUserRole;
import java.util.Set;


public enum Role implements CommonUserRole {
    ROOT,
    ADMIN,
    MANAGER,
    ENGINEER,
    TECHNICIAN,
    VENDOR,
    READONLY,
    MCI
    ;

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public boolean isRoot() {
        return equals(ROOT);
    }

    @Override
    public Set<String> getAllowedFeatures() {
        return null;
    }
}
