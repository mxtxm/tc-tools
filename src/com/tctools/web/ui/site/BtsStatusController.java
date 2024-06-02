package com.tctools.web.ui.site;

import com.tctools.business.model.site.BtsStatusModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/site/bts/status/get",
    "/ui/site/bts/status/keyval",
})
public class BtsStatusController extends RouteToMethod {

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteBtsStatusGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, BtsStatusModel.getAll(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteBtsStatusKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, BtsStatusModel.getAsKeyValue(params));
    }
}