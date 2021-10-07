package com.tctools.business.model.location;

import com.tctools.business.dto.location.City;
import com.tctools.common.util.ModelUtil;
import com.vantar.web.Params;
import java.util.*;


public class CityModel {

    public static List<City.Localed> getAll(Params params) {
        return ModelUtil.getAll(new City(), new City.Localed(), params.getLang());
    }

    public static Map<Long, String> getAsKeyValue(Params params) {
        return ModelUtil.getAsKeyValue(new City(), params.getLang());
    }

    public static Map<Long, String> getAcAsKeyValue(Params params) {
        return ModelUtil.getAcAsKeyValue(new City());
    }
}