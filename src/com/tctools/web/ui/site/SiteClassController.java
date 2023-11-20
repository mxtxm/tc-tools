package com.tctools.web.ui.site;

import com.tctools.business.model.site.SiteClassModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/site/class/get",
    "/ui/site/class/keyval",
})
public class SiteClassController extends RouteToMethod {

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteClassGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, SiteClassModel.getAll(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteClassKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, SiteClassModel.getAsKeyValue(params));
    }
}