package com.tctools.business.model.project.hseaudit.export;

import com.tctools.business.dto.location.Province;
import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.user.User;
import com.tctools.web.patch.TestController;
import com.vantar.business.*;
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

    public static Map<String, Integer> stateAggregate() throws ServerException {
        Map<String, Integer> data = new HashMap<>();

        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire());
        q.addGroup("lastState", Mongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        int total = 0;
        try {
            for (Document document : MongoQuery.getAggregate(q)) {
                Integer count = document.getInteger("count");
                data.put(document.getString(Mongo.ID), count);
                if (count != null) {
                    total += count;
                }
            }
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        data.put("total", total);
        return data;
    }

    public static Map<String, Integer> activityAggregate() throws ServerException {
        Map<String, Integer> data = new HashMap<>();

        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire());
        q.condition().isNotNull("activity");
        q.addGroup("activity", Mongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        int total = 0;
        try {
            for (Document document : MongoQuery.getAggregate(q)) {
                Integer count = document.getInteger("count");
                data.put(document.getString(Mongo.ID), count);
                if (count != null) {
                    total += count;
                }
            }
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        data.put("total", total);
        return data;
    }

    public static Map<String, HashMap<String, Integer>> provinceAudit() throws ServerException {
        Map<String, HashMap<String, Integer>> data = new HashMap<>();
        try {
            putProvinceAudit(data, HseAuditFlowState.Completed);
            putProvinceAudit(data, HseAuditFlowState.Approved);
            putProvinceAudit(data, HseAuditFlowState.PreApproved);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
        return data;
    }

    private static void putProvinceAudit(Map<String, HashMap<String, Integer>> data, HseAuditFlowState state) throws DatabaseException {
        Map<Long, Province> provinces;
        try {
            provinces = Services.get(ServiceDtoCache.class).getMap(Province.class);
        } catch (ServiceException e) {
            return;
        }

        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire());
        q.condition().equal("lastState", state);
        q.addGroup("site.provinceId", Mongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        int total = 0;
        for (Document document : MongoQuery.getAggregate(q)) {
            Integer count = document.getInteger("count");
            String name = ((Province) provinces.get(document.getLong(Mongo.ID))).name.get("fa");

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

    // todo: what is this?
    public static Map<String, Integer> answerCount() throws ServerException {
        Map<String, Integer> data = new HashMap<>();

        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire());
        q.addGroup("answers.7", Mongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        try {
            for (Document document : MongoQuery.getAggregate(q)) {
//                data.put(
                TestController.log.error(">>>> {}",
                    document.getInteger("criticalNoCount")
                );
  //              );
            }
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
        return data;
    }

    public static Map<String, HashMap<String, Integer>> userDoneAggregate(Params params) throws ServerException, InputException {
        Long userId = params.getLong("userId");
        if (userId == null || userId < 1) {
            throw new InputException(VantarKey.INVALID_ID, "userId");
        }

        Map<String, HashMap<String, Integer>> data = new HashMap<>();
        try {
            putUserDoneAggregate(data, userId, HseAuditFlowState.Completed);
            putUserDoneAggregate(data, userId, HseAuditFlowState.Approved);
            putUserDoneAggregate(data, userId, HseAuditFlowState.PreApproved);
            putUserDoneAggregate(data, userId, HseAuditFlowState.Planned);
            putUserDoneAggregate(data, userId, HseAuditFlowState.Incomplete);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
        return data;
    }

    private static void putUserDoneAggregate(Map<String, HashMap<String, Integer>> data,
        long userId, HseAuditFlowState state) throws DatabaseException {

        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire());
        q.condition().in("lastState", state);
        q.condition().equal("assigneeId", userId);
        q.addGroup("auditDateTime", Mongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        DateTime ts = new DateTime();
        for (Document document : MongoQuery.getAggregate(q)) {
            Integer count = document.getInteger("count");
            Long date = document.getLong(Mongo.ID);
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

    // {user: {}}
    public static Map<String, Map<String, HashMap<String, Integer>>> usersDoneAggregate(Params params) throws VantarException {
        QueryBuilder q = new QueryBuilder(new User());
        q.condition().equal("projectTypes", ProjectType.HseAudit);

        List<User> users = CommonModelMongo.getData(q, params.getLang());

        Map<String, Map<String, HashMap<String, Integer>>> allData = new HashMap<>(users.size());
        for (User user : users) {
            Map<String, HashMap<String, Integer>> data = new HashMap<>();
            try {
                putUserDoneAggregateMonth(data, user.id, HseAuditFlowState.Completed);
                putUserDoneAggregateMonth(data, user.id, HseAuditFlowState.Approved);
                putUserDoneAggregateMonth(data, user.id, HseAuditFlowState.PreApproved);
                putUserDoneAggregateMonth(data, user.id, HseAuditFlowState.Planned);
                putUserDoneAggregateMonth(data, user.id, HseAuditFlowState.Incomplete);

                allData.put(user.id + "||" + user.username + "||" + user.fullName, data);
            } catch (DatabaseException e) {
                throw new ServerException(VantarKey.FETCH_FAIL);
            }
        }

        return allData;
    }

    private static void putUserDoneAggregateMonth(Map<String, HashMap<String, Integer>> data,
        long userId, HseAuditFlowState state) throws DatabaseException {

        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire());
        q.condition().in("lastState", state);
        q.condition().equal("assigneeId", userId);
        q.addGroup("auditDateTime", Mongo.ID);
        q.addGroup(QueryGroupType.COUNT, "count");

        DateTime ts = new DateTime();
        for (Document document : MongoQuery.getAggregate(q)) {
            Integer count = document.getInteger("count");
            Long date = document.getLong(Mongo.ID);
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