package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.common.util.ExportCommon;
import com.vantar.database.nosql.mongo.*;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import org.bson.Document;
import java.util.*;


public class MetricTypeReport extends ExportCommon {

    private final static String TYPE_OVERVIEW = "MetricTypeReport.typeOverview";


    public static void cacheCount() throws ServerException {
        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().in("lastState", RadioMetricFlowState.Completed, RadioMetricFlowState.Verified, RadioMetricFlowState.Approved);
        q.addGroup("isCc", Mongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        Map<String, Integer> result = new HashMap<>(2);
        try {
            for (Document document : MongoQuery.getAggregate(q)) {
                result.put((boolean) document.get(Mongo.ID) ? "CC" : "Normal", (int) document.get("count"));
            }
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        cache(TYPE_OVERVIEW, result);
    }

    public static String getCount() throws VantarException {
        return getFromCache(TYPE_OVERVIEW);
    }
}