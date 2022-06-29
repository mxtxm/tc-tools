package com.tctools.business.model.project.radiometric.complain;

import com.tctools.business.dto.project.radiometric.complain.PropertyType;
import com.tctools.common.util.ModelUtil;
import java.util.*;


public class PropertyModel {

    public static List<PropertyType> getAll() {
        return ModelUtil.getAll(new PropertyType());
    }

    public static Map<Long, String> getAsKeyValue() {
        return ModelUtil.getAsKeyValue(new PropertyType());
    }
}