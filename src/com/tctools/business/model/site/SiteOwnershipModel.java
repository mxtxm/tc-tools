package com.tctools.business.model.site;

import com.tctools.business.dto.site.SiteOwnership;
import com.tctools.common.util.ModelUtil;
import java.util.*;


public class SiteOwnershipModel {

    public static List<SiteOwnership> getAll() {
        return ModelUtil.getAll(new SiteOwnership());
    }

    public static Map<Long, String> getAsKeyValue() {
        return ModelUtil.getAsKeyValue(new SiteOwnership());
    }
}