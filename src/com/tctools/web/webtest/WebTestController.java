package com.tctools.web.webtest;

import com.tctools.business.dto.system.WebTest;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.exception.VantarException;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/test/web",
    "/test/web/insert",
})
public class WebTestController extends RouteToMethod {

    public void webInsert(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, Db.modelMongo.insert(new ModelCommon.Settings(params, new WebTest())));
    }

    public void web(Params params, HttpServletResponse response) {
        Response.showTemplate(params, response, "/templates/web-test.jsp");
    }
}