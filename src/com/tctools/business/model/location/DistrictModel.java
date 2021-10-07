package com.tctools.business.model.location;

import com.tctools.business.dto.location.District;
import com.tctools.common.util.ModelUtil;
import java.util.*;


public class DistrictModel {

    public static List<District> getAll() {
        return ModelUtil.getAll(new District());
    }

    public static Map<Long, String> getAsKeyValue() {
        return ModelUtil.getAsKeyValue(new District());
    }
}