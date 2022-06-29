package com.tctools.business.model.site;

import com.tctools.business.dto.site.Operator;
import com.tctools.common.util.ModelUtil;
import com.vantar.web.Params;
import java.util.*;


public class OperatorModel {

    public static List<Operator.Localized> getAll(Params params) {
        return ModelUtil.getAll(new Operator(), new Operator.Localized(), params.getLang());
    }

    public static Map<Long, String> getAsKeyValue(Params params) {
        return ModelUtil.getAsKeyValue(new Operator(), params.getLang());
    }
}