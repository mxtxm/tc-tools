package com.tctools.business.model.location;

import com.tctools.business.dto.location.*;
import com.tctools.common.util.ModelUtil;
import com.vantar.business.CommonModelMongo;
import com.vantar.exception.*;
import com.vantar.web.Params;
import java.util.*;


public class RegionModel {

    public static List<Region.Localed> getAll(Params params) throws ServerException, NoContentException {
        return CommonModelMongo.getAllFromCache(params, Region.class, Region.Localed.class);
    }

    public static Map<Long, String> getAsKeyValue(Params params) {
        return ModelUtil.getAsKeyValue(new Region(), params.getLang());
    }
}