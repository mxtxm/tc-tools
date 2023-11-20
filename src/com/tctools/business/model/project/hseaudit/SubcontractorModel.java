package com.tctools.business.model.project.hseaudit;

import com.tctools.business.dto.project.hseaudit.SubContractor;
import com.vantar.business.CommonModelMongo;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.util.string.StringUtil;
import com.vantar.web.Params;
import java.util.*;


public class SubcontractorModel {

    public static PageData search(Params params) throws VantarException {
        return CommonModelMongo.search(params, new SubContractor.Viewable());
    }

    public static SubContractor.Viewable get(Params params) throws VantarException {
        return CommonModelMongo.getById(params, new SubContractor.Viewable());
    }

    public static Map<Long, String> autoComplete(Params params) throws VantarException {
        String name = params.getString("name");

        QueryBuilder q = new QueryBuilder(new SubContractor.Viewable());
        q.sort("name desc");
        if (StringUtil.isNotEmpty(name)) {
            q.condition().like("name", name);
        }

        Map<Long, String> values = new HashMap<>();
        try {
            for (Dto item : CommonModelMongo.getData(q, params.getLang())) {
                SubContractor.Viewable s = (SubContractor.Viewable) item;
                values.put(s.id, s.name + " - " + s.province.name);
            }
        } catch (NoContentException ignore) {

        }
        return values;
    }
}