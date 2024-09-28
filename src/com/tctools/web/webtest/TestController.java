package com.tctools.web.webtest;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.business.dto.site.Site;
import com.tctools.common.util.ExportCommon;
import com.vantar.database.common.Db;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.VantarException;
import com.vantar.util.json.Json;
import com.vantar.util.string.StringUtil;
import com.vantar.web.Params;
import com.vantar.web.Response;
import com.vantar.web.RouteToMethod;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet({
    "/old/workflow/count",
    "/old/workflow",

    "/test/a",
    "/test/k",
    "/test/t",
})
public class TestController extends RouteToMethod {

    public void a(Params params, HttpServletResponse response) throws VantarException {

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition()
                .in("patch", "OLD WORKFLOW");



        //        SendMessage.sendSms("09121933230", "پیامک تستی");
//        SendMessage.sendSms("+989121933230", "پیامک تستی");
//        SendMessage.sendSms("09128787860", "پیامک تستی");
//        SendMessage.sendSms("+989128787860", "پیامک تستی");
//
//
//        try {
//            Db.modelMongo.forEach(new RadioMetricFlow(), dto -> {
//                RadioMetricFlow f = (RadioMetricFlow) dto;
//                if (f.complain != null && BoolUtil.isFalse(f.isCc)) {
//                    log.error(">>>>a {} {}", f.id, f.site.code);
//                }
//                if (f.complain == null && BoolUtil.isTrue(f.isCc)) {
//                    log.error(">>>>b {} {}", f.id, f.site.code);
//                }
//                return true;
//            });
//        } catch (Exception e) {
//
//        }
    }

    public void workflowCount(Params params, HttpServletResponse response) throws VantarException {
        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().inspect().equal("comments", "OLD WORKFLOW");
        Response.writeString(response, " " + Db.modelMongo.count(q));
    }


    public void workflow(Params params, HttpServletResponse response) throws VantarException {
        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().inspect().equal("comments", "OLD WORKFLOW");

        Db.modelMongo.forEach(q, dto -> {
            Response.writeString(response, ((RadioMetricFlow) dto).site.code + "<br>");
            return true;
        });
    }

    public void k(Params params, HttpServletResponse response) throws VantarException {

        Map<String, List<Long>> sites = new HashMap<>(50000, 1);

        List<Site> sitesX = Db.modelMongo.getAll(new Site());
        for (Site site : sitesX) {
            List<Long> items = sites.computeIfAbsent(site.code, k -> new ArrayList<>(5));
            items.add(site.id);
        }

        sites.entrySet().removeIf(e -> e.getValue().size() == 1);
        log.error("{}", Json.d.toJsonPretty(sites));

        for (List<Long> items : sites.values()) {
            Long baseSiteId = items.remove(0);
            for (Long item : items) {
                log.error("map.put(" + item +"L, " + baseSiteId + "L);");
            }
        }
    }


    public void t(Params params, HttpServletResponse response) throws VantarException {
        Set<String> freqs = new HashSet<>(100);

        Db.modelMongo.forEach(new Site(), dto -> {
            String f = ((Site) dto).frequencyBand;

            if (f == null || f.isEmpty()) {

            } else {
                String s = StringUtil.replace(
                    StringUtil.remove(
                        ExportCommon.getValue(f).toUpperCase(),
                        "(", ")"
                    ), '-', ','
                );
                Set<String> set = StringUtil.splitToSet(s, ',');
                if (set != null) {
                    freqs.addAll(set);
                }
            }
            return true;
        });
        log.error(">>>>{}",freqs);

    }


}