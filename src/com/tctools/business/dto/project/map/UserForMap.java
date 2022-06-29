package com.tctools.business.dto.project.map;

import com.tctools.business.dto.user.User;
import com.vantar.util.object.ObjectUtil;


public class UserForMap {

    public Long id;
    public String name;


    public UserForMap(User user) {
        id = user.id;
        name = user.fullName;
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}