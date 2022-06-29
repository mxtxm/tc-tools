package com.tctools.business.model.location;

import com.tctools.business.dto.location.*;
import com.tctools.common.util.ModelUtil;
import com.vantar.business.CommonModelMongo;
import com.vantar.exception.*;
import java.util.*;


public class DistrictModel {

    public static List<District> getAll() throws ServerException, NoContentException {
        return CommonModelMongo.getAll(new District());
    }

    public static Map<Long, String> getAsKeyValue() {
        return ModelUtil.getAsKeyValue(new District());
    }
}