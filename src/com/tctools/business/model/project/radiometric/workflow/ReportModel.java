package com.tctools.business.model.project.radiometric.workflow;

import com.tctools.business.dto.location.Province;
import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.user.User;
import com.vantar.database.common.Db;
import com.vantar.database.nosql.mongo.*;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.string.StringUtil;
import com.vantar.web.Params;
import org.bson.Document;
import java.util.*;


public class ReportModel {

    public static Map<String, Integer> stateAggregate() throws VantarException {
        Map<String, Integer> data = new HashMap<>();

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.addGroup("lastState", DbMongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        int total = 0;
        for (Document document : Db.mongo.getAggregate(q)) {
            Integer count = document.getInteger("count");
            data.put(document.getString(DbMongo.ID), count);
            if (count != null) {
                total += count;
            }
        }

        data.put("total", total);
        return data;
    }

    public static Map<String, HashMap<String, Integer>> provinceAudit() throws VantarException {
        Map<String, HashMap<String, Integer>> data = new HashMap<>();
        putProvinceAudit(data, RadioMetricFlowState.Completed);
        putProvinceAudit(data, RadioMetricFlowState.Approved);
        return data;
    }

    private static void putProvinceAudit(Map<String, HashMap<String, Integer>> data, RadioMetricFlowState state) throws VantarException {
        Map<Long, Province> provinces = Services.get(ServiceDtoCache.class).getMap(Province.class);

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().equal("lastState", state);
        q.addGroup("site.provinceId", DbMongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        int total = 0;
        for (Document document : Db.mongo.getAggregate(q)) {
            Integer count = document.getInteger("count");
            String name = provinces.get(document.getLong(DbMongo.ID)).name.get("fa");

            HashMap<String, Integer> d = data.get(name);
            if (d == null) {
                d = new HashMap<>(3);
            }
            d.put(state.name(), count);
            data.put(name, d);

            if (count != null) {
                total += count;
            }
        }

        HashMap<String, Integer> d = data.get("total");
        if (d == null) {
            d = new HashMap<>(3);
        }
        d.put(state.name(), total);

        data.put("total", d);
    }

    public static Map<String, Map<String, HashMap<String, Integer>>> usersDoneAggregate(Params params) throws VantarException {
        QueryBuilder q = new QueryBuilder(new User());
        q.condition().equal("projectTypes", ProjectType.RadioMetric);

        List<User> users = Db.modelMongo.getData(q, params.getLang());

        Map<String, Map<String, HashMap<String, Integer>>> allData = new HashMap<>(users.size());
        for (User user : users) {
            Map<String, HashMap<String, Integer>> data = new HashMap<>();
            putUserDoneAggregateMonth(data, user.id, RadioMetricFlowState.Completed);
            putUserDoneAggregateMonth(data, user.id, RadioMetricFlowState.Approved);
            putUserDoneAggregateMonth(data, user.id, RadioMetricFlowState.Verified);
            putUserDoneAggregateMonth(data, user.id, RadioMetricFlowState.Planned);
            putUserDoneAggregateMonth(data, user.id, RadioMetricFlowState.Problematic);
            putUserDoneAggregateMonth(data, user.id, RadioMetricFlowState.Revise);
            putUserDoneAggregateMonth(data, user.id, RadioMetricFlowState.Returned);

            allData.put(user.id + "||" + user.username + "||" + user.fullName, data);
        }

        return allData;
    }

    public static Map<String, HashMap<String, Integer>> userDoneAggregate(Params params) throws VantarException {
        Long userId = params.getLong("userId");
        if (userId == null || userId < 1) {
            throw new InputException(VantarKey.INVALID_ID, "userId");
        }

        Map<String, HashMap<String, Integer>> data = new HashMap<>();
        putUserDoneAggregateMonth(data, userId, RadioMetricFlowState.Completed);
        putUserDoneAggregateMonth(data, userId, RadioMetricFlowState.Approved);
        putUserDoneAggregateMonth(data, userId, RadioMetricFlowState.Verified);
        putUserDoneAggregateMonth(data, userId, RadioMetricFlowState.Planned);
        putUserDoneAggregateMonth(data, userId, RadioMetricFlowState.Problematic);
        putUserDoneAggregateMonth(data, userId, RadioMetricFlowState.Revise);
        putUserDoneAggregateMonth(data, userId, RadioMetricFlowState.Returned);
        return data;
    }

    private static void putUserDoneAggregate(Map<String, HashMap<String, Integer>> data,
        long userId, RadioMetricFlowState state) throws VantarException {

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().in("lastState", state);
        q.condition().equal("assigneeId", userId);
        q.addGroup("auditDateTime", DbMongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        DateTime ts = new DateTime();
        for (Document document : Db.mongo.getAggregate(q)) {
            Integer count = document.getInteger("count");
            Long date = document.getLong(DbMongo.ID);
            if (date == null) {
                continue;
            }
            String day = ts.setTimeStamp(date).truncateTime().formatter().getDatePersian();

            HashMap<String, Integer> d = data.get(day);
            if (d == null) {
                d = new HashMap<>(5);
            }
            Integer stateCount = d.get(state.name());
            d.put(state.name(), (count == null ? 0 : count) + (stateCount == null ? 0 : stateCount));
            data.put(day, d);
        }
    }

    private static void putUserDoneAggregateMonth(Map<String, HashMap<String, Integer>> data,
        long userId, RadioMetricFlowState state) throws VantarException {

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().in("lastState", state);
        q.condition().equal("assigneeId", userId);
        q.addGroup("auditDateTime", DbMongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        DateTime ts = new DateTime();
        for (Document document : Db.mongo.getAggregate(q)) {
            Integer count = document.getInteger("count");
            Long date = document.getLong(DbMongo.ID);
            if (date == null) {
                continue;
            }
            String day = ts.setTimeStamp(date).truncateTime().formatter().getDatePersian();
            String[] ymd = StringUtil.split(day, "-");
            String month = ymd[0] + '-' + ymd[1];

            HashMap<String, Integer> d = data.get(month);
            if (d == null) {
                d = new HashMap<>(5);
            }
            Integer stateCount = d.get(state.name());
            d.put(state.name(), (count == null ? 0 : count) + (stateCount == null ? 0 : stateCount));
            data.put(month, d);
        }
    }
}