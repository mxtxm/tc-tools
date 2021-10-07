package com.tctools.business.model.location;

import com.tctools.business.dto.location.LocationType;
import com.tctools.common.util.ModelUtil;
import com.vantar.web.Params;
import java.util.*;


public class LocationTypeModel {

    public static List<LocationType.Localed> getAll(Params params) {
        return ModelUtil.getAll(new LocationType(), new LocationType.Localed(), params.getLang());
    }

    public static Map<Long, String> getAsKeyValue(Params params) {
        return ModelUtil.getAsKeyValue(new LocationType(), params.getLang());
    }
}