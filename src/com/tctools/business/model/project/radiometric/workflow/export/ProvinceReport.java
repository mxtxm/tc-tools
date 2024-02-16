package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.location.Province;
import com.tctools.business.dto.project.radiometric.workflow.*;
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


public class ProvinceReport extends ExportCommon {

    private final static String LOCALE = "fa";
    private final static int MIN_YEAR = 1390;


    public static void cacheStatistics() throws VantarException {
        // <provinceId, <ym, ProvinceStatistic>>
        Map<Long, Map<Integer, ProvinceStatistic>> statistics = new LinkedHashMap<>(500);

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.sort("lastStateDateTime");

        int yMin = 5000;
        int yMax = 0;
        try {
            for (Dto dto : ModelMongo.getData(q, LOCALE)) {
                RadioMetricFlow flow = (RadioMetricFlow) dto;
                if (flow.provinceId == null || flow.lastStateDateTime == null) {
                    continue;
                }
                Map<Integer, ProvinceStatistic> dateStat = statistics.computeIfAbsent(flow.provinceId, k -> new HashMap<>());

                int y = flow.lastStateDateTime.formatter().getDateTimePersian().year;
                int m = flow.lastStateDateTime.formatter().getDateTimePersian().month;
                if (y < MIN_YEAR) {
                    log.warn("! record with wrong year >> date={} year={} id={} site={}",
                        y, flow.lastStateDateTime, flow.id, flow.site.code);
                    continue;
                }

                int ym = ymToInt(y, m);
                ProvinceStatistic stat = dateStat.get(ym);
                if (stat == null) {
                    stat = new ProvinceStatistic();
                    stat.setFirst(flow);
                    dateStat.put(ym, stat);
                }

                stat.countState(flow);
                yMin = Math.min(yMin, y);
                yMax = Math.max(yMax, y);
            }
        } catch (NoContentException e) {
            log.error("!", e);
        }

        ModelMongo.purge(new ProvinceStatistic());

        for (Province province : Services.get(ServiceDtoCache.class).getList(Province.class)) {
            Map<Integer, ProvinceStatistic> dateStat = statistics.get(province.id);
            if (dateStat == null) {
                dateStat = new HashMap<>();
            }

            for (int y = yMin; y <= yMax; ++y) {
                for (int m = 1; m <= 12; ++m) {
                    ProvinceStatistic stat = dateStat.get(ymToInt(y, m));
                    if (stat == null) {
                        stat = new ProvinceStatistic();
                        stat.completed = 0;
                        stat.verified = 0;
                        stat.approved = 0;
                        stat.total = 0;
                    }
                    stat.provinceId = province.id;
                    stat.provinceName = province.name.get(LOCALE);
                    stat.yearMonth = ymToInt(y, m);

                    try {
                        ModelMongo.insert(stat);
                    } catch (VantarException e) {
                        log.error("! {}=>", stat, e);
                    }
                }
            }
        }
    }

    public static Map<String, Map<String ,ProvinceStatistic.Viewable>> getMonthlyPerformance(Params params)
        throws VantarException {

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

        QueryBuilder q = new QueryBuilder(new ProvinceStatistic.Viewable());
        q.sort("yearMonth");

        q.condition().between(
            "yearMonth",
            ymToInt(yFrom, mFrom),
            ymToInt(yTo, mTo)
        );

        List<Long> provinceIds = params.getLongList("provinceids");
        if (provinceIds != null) {
            q.condition().inNumber("provinceId", provinceIds);
        }

        List<Dto> dtos = ModelMongo.getData(q);

        // <provinceName, <ym, ProvinceStatistic>>
        Map<String, Map<String ,ProvinceStatistic.Viewable>> statistics = new LinkedHashMap<>(500);
        for (Dto dto : dtos) {
            ProvinceStatistic.Viewable stat = (ProvinceStatistic.Viewable) dto;
            Map<String, ProvinceStatistic.Viewable> ymStat = statistics.computeIfAbsent(stat.provinceName, k -> new LinkedHashMap<>());
            ymStat.put(ymToString(stat.yearMonth), stat);
        }

        return statistics;
    }

    public static Map<String, Integer> getTotalPerformance(Params params) throws ServerException {
        QueryBuilder q = new QueryBuilder(new ProvinceStatistic());
        q.addGroup("provinceName", Mongo.ID);
        q.addGroup(QueryGroupType.SUM, "count", "total");

        List<Long> provinceIds = params.getLongList("provinceids");
        if (provinceIds != null) {
            q.condition().inNumber("provinceId", provinceIds);
        }

        // <provinceName, count>
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