package com.tctools.business.model.site;

import com.tctools.business.dto.site.*;
import com.tctools.common.util.ModelUtil;
import java.util.*;


public class SectorOptimizationModel {

    public static List<SectorOptimization> getAll() {
        return ModelUtil.getAll(new SectorOptimization());
    }

    public static Map<Long, String> getAsKeyValue() {
        return ModelUtil.getAsKeyValue(new SectorOptimization());
    }
}