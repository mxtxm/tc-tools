package com.tctools.web.ui.site;

import com.tctools.business.model.site.SiteOwnershipModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/site/ownership/get",
    "/ui/site/ownership/keyval",
})
public class SiteOwnershipController extends RouteToMethod {

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteOwnershipGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, SiteOwnershipModel.getAll());
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteOwnershipKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, SiteOwnershipModel.getAsKeyValue());
    }
}