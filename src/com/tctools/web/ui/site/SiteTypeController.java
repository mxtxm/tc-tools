package com.tctools.web.ui.site;

import com.tctools.business.model.site.SiteTypeModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/site/type/get",
    "/ui/site/type/keyval",
})
public class SiteTypeController extends RouteToMethod {

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteTypeGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, SiteTypeModel.getAll(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteTypeKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, SiteTypeModel.getAsKeyValue(params));
    }
}