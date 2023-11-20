package com.tctools.web.ui.site;

import com.tctools.business.model.site.BtsInstallModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/site/bts/install/get",
    "/ui/site/bts/install/keyval",
})
public class BtsInstallController extends RouteToMethod {

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteBtsInstallGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, BtsInstallModel.getAll(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteBtsInstallKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, BtsInstallModel.getAsKeyValue(params));
    }
}