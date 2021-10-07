package com.tctools.business.model.site;

import com.tctools.business.dto.site.BtsInstall;
import com.tctools.common.util.ModelUtil;
import com.vantar.web.Params;
import java.util.*;


public class BtsInstallModel {

    public static List<BtsInstall.Localed> getAll(Params params) {
        return ModelUtil.getAll(new BtsInstall(), new BtsInstall.Localed(), params.getLang());
    }

    public static Map<Long, String> getAsKeyValue(Params params) {
        return ModelUtil.getAsKeyValue(new BtsInstall(), params.getLang());
    }
}