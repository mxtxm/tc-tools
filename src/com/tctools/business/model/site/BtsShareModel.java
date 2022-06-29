package com.tctools.business.model.site;

import com.tctools.business.dto.site.*;
import com.tctools.common.util.ModelUtil;
import com.vantar.web.Params;
import java.util.*;


public class BtsShareModel {

    public static List<BtsShare.Localed> getAll(Params params) {
        return ModelUtil.getAll(new BtsShare(), new BtsShare.Localed(), params.getLang());
    }

    public static Map<Long, String> getAsKeyValue(Params params) {
        return ModelUtil.getAsKeyValue(new BtsShare(), params.getLang());
    }
}