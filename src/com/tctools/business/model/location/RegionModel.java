package com.tctools.business.model.location;

import com.tctools.business.dto.location.Region;
import com.tctools.common.util.ModelUtil;
import com.vantar.web.Params;
import java.util.*;


public class RegionModel {

    public static List<Region.Localed> getAll(Params params) {
        return ModelUtil.getAll(new Region(), new Region.Localed(), params.getLang());
    }

    public static Map<Long, String> getAsKeyValue(Params params) {
        return ModelUtil.getAsKeyValue(new Region(), params.getLang());
    }
}