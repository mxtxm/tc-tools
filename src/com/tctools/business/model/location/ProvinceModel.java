package com.tctools.business.model.location;

import com.tctools.business.dto.location.*;
import com.tctools.common.util.ModelUtil;
import com.vantar.business.CommonModelMongo;
import com.vantar.exception.*;
import com.vantar.web.Params;
import java.util.*;


public class ProvinceModel {

    public static List<Province.Localed> getAll(Params params) throws ServerException, NoContentException {
        return CommonModelMongo.getAllFromCache(params, Province.class, Province.Localed.class);
    }

    public static Map<Long, String> getAsKeyValue(Params params) {
        return ModelUtil.getAsKeyValue(new Province(), params.getLang());
    }

    public static Map<Long, String> getAcAsKeyValue(Params params) {
        return ModelUtil.getAcAsKeyValue(new Province());
    }
}