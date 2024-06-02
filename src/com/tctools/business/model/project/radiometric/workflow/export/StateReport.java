package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.common.util.ExportCommon;
import com.vantar.database.common.Db;
import com.vantar.database.dto.Dto;
import com.vantar.database.nosql.mongo.DbMongo;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.util.datetime.*;
import com.vantar.util.json.Json;
import com.vantar.util.object.ObjectUtil;
import com.vantar.web.Params;
import org.bson.Document;
import java.util.*;


public class StateReport extends ExportCommon {

    private final static String MONTHLY_OVERVIEW = "StateReport.monthlyOverview";


    public static Map<String, Integer> getOverview(Params params) throws VantarException {
        Map<String, Integer> data = new HashMap<>(10);

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().isNotNull("lastState");
        q.addGroup("lastState", DbMongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        DateTimeRange dateTimeRange = params.getDateTimeRange("from", "to");
        if (dateTimeRange != null && dateTimeRange.isValid()) {
            q.condition().between("lastStateDateTime", dateTimeRange);
        }

        for (Document document : Db.mongo.getAggregate(q)) {
            data.put(document.get(DbMongo.ID).toString(), (Integer) document.get("count"));
        }

        return data;
    }

    public static void cacheMonthlyOverview() throws VantarException {
        Map<String, StateStatistic> data = new LinkedHashMap<>(40);

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.sort("lastStateDateTime");

        try {
            for (Dto dto : Db.modelMongo.getData(q)) {
                RadioMetricFlow flow = (RadioMetricFlow) dto;
                String ym = flow.lastStateDateTime.formatter().getDatePersianYmonth();

                StateStatistic stateCount = data.computeIfAbsent(ym, k -> new StateStatistic());
                stateCount.set(flow);
            }
        } catch (NoContentException ignore) {

        }

        cache(MONTHLY_OVERVIEW, data);
    }

    public static Map<String, StateStatistic> getMonthlyOverview() throws VantarException {
        String value = getFromCache(MONTHLY_OVERVIEW);
        return Json.d.mapFromJson(value, String.class, StateStatistic.class);
    }

    public static Map<String, StateStatistic> getMonthlyOverviewTarget() throws VantarException {
        Map<String, StateStatistic> withTarget = new LinkedHashMap<>();

        try {
            List<RadioMetricTarget> data = Db.modelMongo.getAll(new RadioMetricTarget());
            for (RadioMetricTarget t : data) {
                String ym = new DateTime(t.year + "-" + t.month + "-01").formatter().getDatePersianYmonth();
                withTarget.put(ym, new StateStatistic(t.value));
            }
        } catch (DateTimeException e) {
            log.error("! datetime", e);
        }

        Map<String, StateStatistic> overview = getMonthlyOverview();
        for (Map.Entry<String, StateStatistic> item : overview.entrySet()) {
            StateStatistic t = withTarget.get(item.getKey());
            if (t == null) {
                continue;
            }
            t.set(item.getValue());
        }

        return withTarget;
    }


    private static class StateStatistic {

        public int completed;
        public int verified;
        public int approved;

        public int total;
        public int target;


        public StateStatistic() {

        }

        public StateStatistic(Integer target) {
            this.target = target;
        }

        public void set(StateStatistic s) {
            completed = s.completed;
            verified = s.verified;
            approved = s.approved;
            total = s.approved + s.completed + s.verified;
        }

        public void set(RadioMetricFlow flow) {
            if (RadioMetricFlowState.Verified.equals(flow.lastState)) {
                ++verified;
            } else if (RadioMetricFlowState.Approved.equals(flow.lastState)) {
                ++approved;
            } else if (RadioMetricFlowState.Completed.equals(flow.lastState)) {
                ++completed;
            }
            ++total;
        }

        public String toString() {
            return ObjectUtil.toString(this);
        }
    }
}