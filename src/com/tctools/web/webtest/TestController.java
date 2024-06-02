package com.tctools.web.webtest;

import com.tctools.business.dto.site.Site;
import com.vantar.database.common.Db;
import com.vantar.exception.VantarException;
import com.vantar.util.json.Json;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@WebServlet({
    "/test/k",
})
public class TestController extends RouteToMethod {

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

}