package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.common.util.ExportCommon;
import com.vantar.database.common.Db;
import com.vantar.database.nosql.mongo.DbMongo;
import com.vantar.database.query.*;
import com.vantar.exception.VantarException;
import org.bson.Document;
import java.util.*;


public class MetricTypeReport extends ExportCommon {

    private final static String TYPE_OVERVIEW = "MetricTypeReport.typeOverview";


    public static void cacheCount() throws VantarException {
        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().in("lastState", RadioMetricFlowState.Completed, RadioMetricFlowState.Verified, RadioMetricFlowState.Approved);
        q.addGroup("isCc", DbMongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        Map<String, Integer> result = new HashMap<>(2);
        for (Document document : Db.mongo.getAggregate(q)) {
            result.put((boolean) document.get(DbMongo.ID) ? "CC" : "Normal", (int) document.get("count"));
        }

        cache(TYPE_OVERVIEW, result);
    }

    public static String getCount() throws VantarException {
        return getFromCache(TYPE_OVERVIEW);
    }
}