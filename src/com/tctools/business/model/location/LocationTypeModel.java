package com.tctools.business.model.location;

import com.tctools.business.dto.location.LocationType;
import com.tctools.common.util.ModelUtil;
import com.vantar.database.common.Db;
import com.vantar.exception.VantarException;
import com.vantar.web.Params;
import java.util.*;


public class LocationTypeModel {

    public static List<LocationType.Localed> getAll(Params params) throws VantarException {
        return Db.modelMongo.getAllFromCache(params, LocationType.class, LocationType.Localed.class);
    }

    public static Map<Long, String> getAsKeyValue(Params params) {
        return ModelUtil.getAsKeyValue(new LocationType(), params.getLang());
    }
}