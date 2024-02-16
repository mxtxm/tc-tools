package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.user.*;
import com.tctools.common.util.ExportCommon;
import com.vantar.business.*;
import com.vantar.database.dto.Dto;
import com.vantar.database.nosql.mongo.*;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.string.StringUtil;
import com.vantar.web.Params;
import org.bson.Document;
import java.util.*;


public class TechReport extends ExportCommon {

    private final static String LOCALE = "fa";
    private final static int MIN_YEAR = 1390;


    public static void cacheStatistics() throws VantarException {
        // <userId, <ym, TechStatistic>>
        Map<Long, Map<Integer, TechStatistic>> statistics = new LinkedHashMap<>(500);

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.sort("lastStateDateTime");

        int yMin = 5000;
        int yMax = 0;
        try {
            for (Dto dto : ModelMongo.getData(q, LOCALE)) {
                RadioMetricFlow flow = (RadioMetricFlow) dto;
                if (flow.assigneeId == null || flow.lastStateDateTime == null) {
                    continue;
                }
                Map<Integer, TechStatistic> dateStat = statistics.computeIfAbsent(flow.assigneeId, k -> new HashMap<>());

                int y = flow.lastStateDateTime.formatter().getDateTimePersian().year;
                int m = flow.lastStateDateTime.formatter().getDateTimePersian().month;
                if (y < MIN_YEAR) {
                    log.warn("! record with wrong year({}<1396) >> id={} site={}", y, flow.id, flow.site.code);
                    continue;
                }

                int ym = ymToInt(y, m);
                TechStatistic stat = dateStat.get(ym);
                if (stat == null) {
                    stat = new TechStatistic();
                    stat.setFirst(flow);
                    dateStat.put(ym, stat);
                }

                stat.countState(flow);
                yMin = Math.min(yMin, y);
                yMax = Math.max(yMax, y);
            }
        } catch (VantarException e) {
            log.error("!", e);
        }

        ModelMongo.purge(new TechStatistic());

        for (User user : Services.get(ServiceDtoCache.class).getList(User.class)) {
            if (user.projectTypes == null || !user.projectTypes.contains(ProjectType.RadioMetric) ||
                !Role.TECHNICIAN.equals(user.role)) {
                continue;
            }

            Map<Integer, TechStatistic> dateStat = statistics.get(user.id);
            if (dateStat == null) {
                dateStat = new HashMap<>();
            }

            for (int y = yMin; y <= yMax; ++y) {
                for (int m = 1; m <= 12; ++m) {
                    TechStatistic stat = dateStat.get(ymToInt(y, m));
                    if (stat == null) {
                        stat = new TechStatistic();
                        stat.completed = 0;
                        stat.verified = 0;
                        stat.approved = 0;
                        stat.total = 0;
                    }
                    stat.userId = user.id;
                    stat.userName = user.fullName;
                    stat.yearMonth = ymToInt(y, m);

                    ModelMongo.insert(stat);
                }
            }
        }
    }

    public static Map<String, Map<String ,TechStatistic.Viewable>> getMonthlyPerformance(Params params) throws VantarException {

        String from = params.getString("from");
        String to = params.getString("to");
        if (from == null || to == null) {
            throw new InputException(VantarKey.INVALID_DATE);
        }
        String[] fromParts = StringUtil.split(from, '-');
        String[] toParts = StringUtil.split(to , '-');
        if (fromParts.length < 2 || toParts.length < 2) {
            throw new InputException(VantarKey.INVALID_DATE);
        }
        int yFrom = StringUtil.toInteger(fromParts[0]);
        int mFrom = StringUtil.toInteger(fromParts[1]);
        int yTo = StringUtil.toInteger(toParts[0]);
        int mTo = StringUtil.toInteger(toParts[1]);

        QueryBuilder q = new QueryBuilder(new TechStatistic.Viewable());
        q.sort("yearMonth");

        q.condition().between(
            "yearMonth",
            ymToInt(yFrom, mFrom),
            ymToInt(yTo, mTo)
        );

        List<Long> userIds = params.getLongList("userids");
        if (userIds != null) {
            q.condition().inNumber("userId", userIds);
        }

        List<Dto> dtos = ModelMongo.getData(q);

        // <userName, <ym, TechStatistic>>
        Map<String, Map<String ,TechStatistic.Viewable>> statistics = new LinkedHashMap<>(500);
        for (Dto dto : dtos) {
            TechStatistic.Viewable stat = (TechStatistic.Viewable) dto;
            Map<String, TechStatistic.Viewable> ymStat = statistics.computeIfAbsent(stat.userName, k -> new LinkedHashMap<>());
            ymStat.put(ymToString(stat.yearMonth), stat);
        }

        return statistics;
    }

    public static Map<String, Integer> getTotalPerformance(Params params) throws ServerException {
        QueryBuilder q = new QueryBuilder(new TechStatistic());
        q.addGroup("userName", Mongo.ID);
        q.addGroup(QueryGroupType.SUM, "count", "total");

        List<Long> userIds = params.getLongList("userids");
        if (userIds != null) {
            q.condition().inNumber("userId", userIds);
        }

        // <techName, count>
        Map<String, Integer> statistics = new LinkedHashMap<>(500);
        try {
            for (Document document : MongoQuery.getAggregate(q)) {
                statistics.put((String) document.get(Mongo.ID), (int) document.get("count"));
            }
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
        return statistics;
    }

    private static String ymToString(int ym) {
        return (ym / 100) + "-" + numberToMonth(ym % 100);
    }

    private static int ymToInt(int y, int m) {
        return StringUtil.toInteger(Integer.toString(y) + (m > 9 ? m : ("0" + m)));
    }
}