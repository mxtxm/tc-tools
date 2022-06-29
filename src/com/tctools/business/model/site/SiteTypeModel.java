package com.tctools.business.model.site;

import com.tctools.business.dto.site.SiteType;
import com.tctools.common.util.ModelUtil;
import com.vantar.web.Params;
import java.util.*;


public class SiteTypeModel {

    public static List<SiteType.Localed> getAll(Params params) {
        return ModelUtil.getAll(new SiteType(), new SiteType.Localed(), params.getLang());
    }

    public static Map<Long, String> getAsKeyValue(Params params) {
        return ModelUtil.getAsKeyValue(new SiteType(), params.getLang());
    }
}