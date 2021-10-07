package com.tctools.business.model.site;

import com.tctools.business.dto.site.*;
import com.tctools.common.util.ModelUtil;
import java.util.*;


public class BtsTowerModel {

    public static List<BtsTowerType> getAll() {
        return ModelUtil.getAll(new BtsTowerType());
    }

    public static Map<Long, String> getAsKeyValue() {
        return ModelUtil.getAsKeyValue(new BtsTowerType());
    }
}