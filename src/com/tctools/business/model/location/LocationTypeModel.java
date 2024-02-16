package com.tctools.business.model.location;

import com.tctools.business.dto.location.*;
import com.tctools.common.util.ModelUtil;
import com.vantar.business.ModelMongo;
import com.vantar.exception.*;
import com.vantar.web.Params;
import java.util.*;


public class LocationTypeModel {

    public static List<LocationType.Localed> getAll(Params params) throws VantarException {
        return ModelMongo.getAllFromCache(params, LocationType.class, LocationType.Localed.class);
    }

    public static Map<Long, String> getAsKeyValue(Params params) {
        return ModelUtil.getAsKeyValue(new LocationType(), params.getLang());
    }
}