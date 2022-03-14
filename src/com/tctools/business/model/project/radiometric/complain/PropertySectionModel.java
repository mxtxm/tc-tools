package com.tctools.business.model.project.radiometric.complain;

import com.tctools.business.dto.project.radiometric.complain.PropertySection;
import com.tctools.common.util.ModelUtil;
import java.util.*;


public class PropertySectionModel {

    public static List<PropertySection> getAll() {
        return ModelUtil.getAll(new PropertySection());
    }

    public static Map<Long, String> getAsKeyValue() {
        return ModelUtil.getAsKeyValue(new PropertySection());
    }
}